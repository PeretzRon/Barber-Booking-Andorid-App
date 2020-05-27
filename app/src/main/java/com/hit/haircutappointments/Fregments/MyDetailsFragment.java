package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hit.haircutappointments.Classes.Users.UserDetails;
import com.hit.haircutappointments.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView mFullName;
    private TextView mPhoneNumber;
    private TextView mEmail;
    private TextView mGender;
    private TextView mFullNameTitle;
    private CircleImageView mCircleImageView;
    private String mCurrentUserId;

    private OnMyDetailsFragmentInteractionListener mListener;

    public MyDetailsFragment() {
    }

    public static MyDetailsFragment newInstance(String param1, String param2) {
        MyDetailsFragment fragment = new MyDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("My Details");
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setTitle("My Details");
    }

    // to disappear the menu bar
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_details, container, false);
        mCurrentUserId = "";
        // add back button to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // check if admin is the user that log in
        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mCurrentUserId = currentFireBaseUser.getUid();

        // init TextView, Images and more
        mFullNameTitle = view.findViewById(R.id.textViewMyNameTitle);
        mFullName = view.findViewById(R.id.textViewMyName);
        mPhoneNumber = view.findViewById(R.id.textViewMyPhone);
        mEmail = view.findViewById(R.id.textViewMyEmail);
        mGender = view.findViewById(R.id.textViewMyGender);
        mCircleImageView = view.findViewById(R.id.profile_image);

        final LinearLayout linearLayoutBackGround = view.findViewById(R.id.colorBg);

        // get the users from fireBase to ArrayList
        final DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUserId).child(
                        "Profile");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                if (userDetails != null) {
                    String fullName =
                            userDetails.getmFirstName() + " " + userDetails.getmLastName();
                    String userGender = userDetails.getmGender().toString();
                    mFullName.setText(fullName);
                    mFullNameTitle.setText(fullName);
                    mPhoneNumber.setText(userDetails.getmPhoneNumber());
                    mEmail.setText(userDetails.getmEmail());
                    mGender.setText(userGender);

                    // that for make the suitable image for the gender
                    if (userGender.equals(UserDetails.eGender.Male.toString())) {
                        mCircleImageView.setImageResource(R.drawable.boy);
                        linearLayoutBackGround.setBackgroundResource(R.color.colorAqua);
                    } else {
                        mCircleImageView.setImageResource(R.drawable.girl);
                        linearLayoutBackGround.setBackgroundResource(R.color.colorPinkRose);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageView imageViewEditName = view.findViewById(R.id.imageViewEditName);
        ImageView imageViewEditGender = view.findViewById(R.id.imageViewEditGender);
        ImageView imageViewEditPhone = view.findViewById(R.id.imageViewEditPhone);

        imageViewEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToEditDetails("Phone Number");
            }
        });

        imageViewEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToEditDetails("Name");
            }
        });

        imageViewEditGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToEditDetails("Gender");
            }
        });


        return view;
    }

    private void editGender(final String iWhatToEdit){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Gender");
        builder.setIcon(R.drawable.ic_edit_black2_24dp);  // make icon to dialog
        builder.setCancelable(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_gender, null);
        builder.setView(view);
        builder.setPositiveButton(
                "Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        final AlertDialog alert11 = builder.create();
        alert11.show();
        alert11.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = view.findViewById(R.id.edit_gender_radio_group);
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                System.out.println(radioButtonID);
                String newGender = radioButtonID == R.id.radio_male ? "Male" : "Female";
                updateChangesInFirBaas(iWhatToEdit, newGender);
                alert11.dismiss();
            }
        });
    }

    private void showDialogToEditDetails(final String iWhatToEdit) {
        if (iWhatToEdit.equals("Gender")) {
            // Gender dialog is radio button dialog
            editGender(iWhatToEdit);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update " + iWhatToEdit);
        builder.setIcon(R.drawable.ic_edit_black2_24dp);  // make icon to dialog
        builder.setCancelable(true);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_details, null);

        //set the previous data in the text view
        final EditText editTextDetails = view.findViewById(R.id.editTextDetails);
        switch (iWhatToEdit) {
            case "Name":
                editTextDetails.setText(mFullName.getText().toString());
                break;
            case "Phone Number":
                editTextDetails.setText(mPhoneNumber.getText().toString());
                break;
        }

        builder.setView(view);
        builder.setPositiveButton(
                "Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });

        final AlertDialog alert11 = builder.create();
        alert11.show();
        alert11.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wantToCloseDialog = true;
                final String theNewDetail = editTextDetails.getText().toString();
                if (!validateTheNewDetails(iWhatToEdit, theNewDetail)) {
                    Toast toast = Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    wantToCloseDialog = false;
                } else {
                    // after get the strings from editTexts save in fireBase
                    updateChangesInFirBaas(iWhatToEdit, theNewDetail);
                }
                if (wantToCloseDialog)
                    alert11.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog
                // especially if you set cancellable to false.
            }
        });
    }

    private void updateChangesInFirBaas(String iWhatToEdit, String iTheNewDetail) {
        final DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUserId).child(
                        "Profile");
        switch (iWhatToEdit) {
            case "Name":
                String[] theNewName = iTheNewDetail.split(" ", 2);
                rootRef.child("mFirstName").setValue(theNewName[0]);
                rootRef.child("mLastName").setValue(theNewName[1]);
                break;
            case "Gender":
                iTheNewDetail =
                        iTheNewDetail.substring(0, 1).toUpperCase() + iTheNewDetail.substring(1).toLowerCase();
                rootRef.child("mGender").setValue(iTheNewDetail);
                break;
            case "Phone Number":
                rootRef.child("mPhoneNumber").setValue(iTheNewDetail);
                break;
        }
    }

    private boolean validateTheNewDetails(String iWhatToEdit, String iTheNewDetail) {
        boolean isDetailIsValid = false;
        if (iTheNewDetail.isEmpty()) {
            return false;
        }

        switch (iWhatToEdit) {
            case "Name":
                String[] words = iTheNewDetail.split(" ");
                if (words.length > 1) {
                    isDetailIsValid = true;
                }

                break;
            case "Gender":
                if (iTheNewDetail.equalsIgnoreCase(UserDetails.eGender.Male.toString()) ||
                        iTheNewDetail.equalsIgnoreCase(UserDetails.eGender.Female.toString())) {
                    isDetailIsValid = true;
                }

                break;
            case "Phone Number":
                if (iTheNewDetail.length() == 10 && (iTheNewDetail.matches("[0-9]+"))) {
                    isDetailIsValid = true;
                }
                break;
        }
        return isDetailIsValid;
    }

    public void onMyDetailsButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMyDetailsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyDetailsFragmentInteractionListener) {
            mListener = (OnMyDetailsFragmentInteractionListener) context;
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

    public interface OnMyDetailsFragmentInteractionListener {
        void onMyDetailsFragmentInteraction(Uri uri);
    }
}
