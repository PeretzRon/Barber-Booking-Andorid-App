package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.hit.haircutappointments.Activities.IdentificationActivity;
import com.hit.haircutappointments.R;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText mEmailView;
    private EditText mPasswordView;
    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;
    private FirebaseAuth mAuth;
    private TextView mSignUpText;
    private CheckBox mCheckBoxRememberMe;
    private ProgressBar mProgressBar;
    private String mParam1;
    private String mParam2;

    private OnLoginFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_login, container, false);

        // init editText / TextView / and other..
        mEmailView = view.findViewById(R.id.email);
        mPasswordView = view.findViewById(R.id.password);
        mSignUpText = view.findViewById(R.id.signUpTextView);
        mCheckBoxRememberMe = view.findViewById(R.id.checkboxRememberMe);
        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        // init sharedPreference
        mPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditor = mPreference.edit();

        // set font
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "comic.ttf");
        mSignUpText.setTypeface(font);

        // that for make a string that part is regular and the other part is other color with underLine
        String text = "<font color=#FFFFFF>Not a member? </font> <font " +
                "color=#ccccff><u>Sign up</u></font>";

        mSignUpText.setText(Html.fromHtml(text));
        mSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdentificationActivity loginActivity = (IdentificationActivity) getActivity();
                loginActivity.LoadSignUpFragment();
            }
        });

        Button buttonLogin = view.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailView.getText().toString().trim();
                final String password = mPasswordView.getText().toString();
                if(mCheckBoxRememberMe.isChecked()){
                    // that means that the user does want to save for next login
                    mEditor.putString("checkbox", "True");
                    mEditor.apply();

                    mEditor.putString("email", email);
                    mEditor.apply();

                    mEditor.putString("password", password);
                    mEditor.apply();
                }else{
                    // that means that the user doesn't want to save for next login
                    mEditor.putString("checkbox", "False");
                    mEditor.apply();

                    mEditor.putString("email", "");
                    mEditor.apply();

                    mEditor.putString("password", "");
                    mEditor.apply();
                }
                IdentificationActivity loginActivity = (IdentificationActivity) getActivity();
                loginActivity.attemptLogin(false,email,password, view);
            }
        });

        checkSharedPreference();

        return view;
    }

    void checkSharedPreference() {
        // load data from SharedPreference
        String checkbox = mPreference.getString("checkbox", "False");
        String name = mPreference.getString("email", "");
        String password = mPreference.getString("password", "");
        mEmailView.setText(name);
        mPasswordView.setText(password);
        if (checkbox.equals("True"))
            mCheckBoxRememberMe.setChecked(true);
        else
            mCheckBoxRememberMe.setChecked(false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLoginFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginFragmentInteractionListener {
        void onLoginFragmentInteraction(Uri uri);
    }
}
