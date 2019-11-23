package com.hit.haircutappointments.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hit.haircutappointments.Classes.Users.UserDetails;
import com.hit.haircutappointments.Fregments.LoginFragment;
import com.hit.haircutappointments.Fregments.SignupFragment;
import com.hit.haircutappointments.R;

import java.util.regex.Pattern;

public class IdentificationActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener, SignupFragment.OnSignUpFragmentInteractionListener {

    private FirebaseAuth mAuth;
    private FragmentManager mFragmentManager;
    private boolean mFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
        mAuth = FirebaseAuth.getInstance();
        mFlag = true;

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.box, fragment, "0").commit();
    }


    // true - > register,   false -> login
    public void attemptLogin(boolean i_IsRegistering, String email, String password, View view) {
        final Button loginButton = view.findViewById(R.id.buttonLogin);
        loginButton.setEnabled(false);
        if (!validateDetailsLogin(email, password, view)) {
            loginButton.setEnabled(true);
            return;
        }

        if (!i_IsRegistering) {
            final ProgressBar mProgressBar = findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(IdentificationActivity.this, "Incorrect username " +
                                                "or password",
                                        Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                                loginButton.setEnabled(true);
                            } else {
                                loginButton.setEnabled(false);
                                closeKeyboard();
                                //create delay and show toast
//                                Toast.makeText(IdentificationActivity.this, "you have " +
//                                                "successfully" +
//                                                " " +
//                                                "connected",
//                                        Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Login successfully :)", Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.YELLOW);
                                snackbar.show();

                                mProgressBar.setVisibility(View.GONE);
                                String uid =
                                        FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final DatabaseReference myRef =
                                        FirebaseDatabase.getInstance().getReference(
                                                "Users").child(uid).child("Profile");
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                                        Intent i = new Intent(IdentificationActivity.this,
                                                MainAppActivity.class);
                                        if(userDetails == null)
                                        {
                                            i.putExtra("isExistInfo","NO");
                                        }
                                        else
                                        {
                                            i.putExtra("isExistInfo","Yes");
                                        }
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });
        }
    }

    public void attemptSignUp(boolean i_IsRegistering, String email, String password,
                              String passwordAgain, View view) {
        final Button signUpButton = view.findViewById(R.id.buttonRegister);
        signUpButton.setEnabled(false);
        if (!validDetailsSignUp(email, password, passwordAgain, view)) {
            signUpButton.setEnabled(true);
            return;
        }

        if (i_IsRegistering) {
            final ProgressBar progressBar2 = findViewById(R.id.progressBar2);
            progressBar2.setVisibility(View.VISIBLE);
            signUpButton.setEnabled(false);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(IdentificationActivity.this, "Could not Registered",
                                        Toast.LENGTH_SHORT).show();
                                progressBar2.setVisibility(View.GONE);
                                signUpButton.setEnabled(true);


                            } else // show username dialog for nick name
                            {
                                closeKeyboard();
                                //create delay and show toast
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final String titleMassage = "User Registered successfully";
                                        final String subMassage = "Now you can login!!";
                                        progressBar2.setVisibility(View.GONE);
                                        signUpButton.setEnabled(true);
                                        showDialogWithMassage(titleMassage, subMassage, true);
                                    }
                                }, 350); // delay time

                            }
                        }
                    });
        }
    }

    private boolean validateDetailsLogin(String email, String password, View view) {
        if (!isValidEmail(email)) {
            // in case that the user entered inValid email
            EditText mEmailViewTest = view.findViewById(R.id.email);
            mEmailViewTest.setError("Not validate email");
            return false;
        }

        if (password.isEmpty()) {
            // in case that the user enter empty password
            EditText mPasswordView = view.findViewById(R.id.password);
            mPasswordView.setError("Password is empty");
            return false;
        }

        return true;
    }

    private boolean validDetailsSignUp(String email, String password, String passwordRepeat,
                                       View view) {
        EditText mEmailViewTest = view.findViewById(R.id.email);
        EditText mPasswordView = view.findViewById(R.id.password);
        EditText mPasswordViewRepeat = view.findViewById(R.id.password_again);
        if (!isValidEmail(email)) {
            mEmailViewTest.setError("Not validate email");   // email doesn't valid
            return false;
        }

        if (password.isEmpty()) {
            mPasswordView.setError("Password is empty");  // password is empty
            return false;
        }

        if (!password.equals(passwordRepeat)) {
            mPasswordViewRepeat.setError("Passwords don't match"); // password don't match
            mPasswordView.setError("Passwords don't match");
            return false;
        }

        return true;
    }

    // check if the email id valid
    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // close the keyboard
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void LoadSignUpFragment() {
        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = new SignupFragment(); // create new instance of SignUp fragment
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter_from_right,
                R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
        fragmentTransaction.replace(R.id.box, fragment,
                (getSupportFragmentManager().getBackStackEntryCount() - 1) + "").addToBackStack(null).commit();
        mFlag = false;
    }

    public void Back() {
        Fragment f;
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().getBackStackEntryCount();
        f = getSupportFragmentManager().findFragmentByTag("0");
        getSupportFragmentManager().beginTransaction().replace(R.id.box, f,
                (getSupportFragmentManager().getBackStackEntryCount() - 1) + "").commit();
        mFlag = true;
    }


    public void forgotPassword(View view) {
        EditText emailView = findViewById(R.id.email);
        String email = "";
        try {
            email = emailView.getText().toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(IdentificationActivity.this, "Invalid Email",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        showDialogWithMassageYesOrNot("Password recovery", "Send an email to " + email + " for " +
                "recovery password?", email);

    }

    public void sendEmailForRecovery(final String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            final String msg = "We send an email to " + email + "\n" + "Please " +
                                    "check your email box" +
                                    " and follow the instructions";
                            showDialogWithMassage("Password recovery", msg, false);
                        }
                    }
                });
    }

    private void showDialogWithMassage(String massageTitle, String massage, final boolean isBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(massageTitle);
        builder.setIcon(R.drawable.ic_error_black_24dp);
        builder.setMessage(massage);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isBack) {
                            Back();
                        }
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    private void showDialogWithMassageYesOrNot(String massageTitle, String massage,
                                               final String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(massageTitle);
        builder.setIcon(R.drawable.ic_error_black_24dp);
        builder.setMessage(massage);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendEmailForRecovery(email);
                    }
                });
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    // pop a dialog Exit Game
    private void showDialogExitGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit?");
        builder.setIcon(R.drawable.ic_keyboard_return_black_24dp);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity(); // kill ALL
                    }
                });
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    @Override
    public void onSignUpFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLoginFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (!mFlag) {
            Back();
        } else {
            showDialogExitGame();
        }
    }
}
