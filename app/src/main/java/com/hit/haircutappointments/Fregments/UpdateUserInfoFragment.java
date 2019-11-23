package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hit.haircutappointments.Activities.MainAppActivity;
import com.hit.haircutappointments.Classes.Users.UserDetails;
import com.hit.haircutappointments.R;

import java.util.Objects;

public class UpdateUserInfoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPhoneNumber;
    private TextView mTextViewGender;
    private Spinner mSpinnerGender;
    private boolean mIsSave;
    private String mUserEmail;

    private OnUpdateUserInfoFragmentInteractionListener mListener;

    public UpdateUserInfoFragment() {
        // Required empty public constructor
    }

    public static UpdateUserInfoFragment newInstance(String param1, String param2) {
        UpdateUserInfoFragment fragment = new UpdateUserInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set Title for action bar
        Objects.requireNonNull(getActivity()).setTitle("Update Your Details");
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // to disappear the menu bar
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    private boolean validateDetails(String iFirstName, String iLastName,
                                    String iPhoneNumber, String iGender) {
        boolean isAllDetailsValid = true;

        if (iFirstName.isEmpty()) {
            mFirstName.setError("Name can't be empty");  // can't save to fireBase with empty name
            isAllDetailsValid = false;
        }

        if (iLastName.isEmpty()) {
            mLastName.setError("Last name can't be empty");  // can't save with empty last name
            isAllDetailsValid = false;
        }

        if (iPhoneNumber.isEmpty() || iPhoneNumber.length() != 10) {
            mPhoneNumber.setError("Phone Number must be 10 digits");  // error - only 10 digits
            isAllDetailsValid = false;
        }

        if (iGender.equals("Select Gender")) {
            mTextViewGender.setError("You must select a gender"); // error - must select gender
            isAllDetailsValid = false;
        }

        return isAllDetailsValid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_user_info, container, false);
        mUserEmail = null;

        // check the user id
        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserEmail = currentFireBaseUser.getEmail();

        // init data member
        mIsSave = false; // a boolean flag the tell if the data save correctly on fireBase
        mFirstName = view.findViewById(R.id.firstNameTextView);
        mLastName = view.findViewById(R.id.LastNameEditText);
        mPhoneNumber = view.findViewById(R.id.phoneTextView);
        mSpinnerGender = view.findViewById(R.id.spinner);
        mTextViewGender = view.findViewById(R.id.genderTextView);

        // set to spinner all the values from tne enum UserDetails.eGender
        mSpinnerGender.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_spinner_item, UserDetails.eGender.values()));

        // Just for clear error from text view of gender
        mSpinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view,
                                       int position,
                                       long id) {
                mTextViewGender.setError(null); // clear error from gender text view

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTextViewGender.setError(null); // clear error from gender text view
            }
        });

        Button saveButton = view.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = mFirstName.getText().toString(); // get name from text view
                String lastName = mLastName.getText().toString();// get last name from text view
                String phoneNumber = mPhoneNumber.getText().toString(); // get phone from text view
                String gender = mSpinnerGender.getSelectedItem().toString(); // get gender

                // validate if all field are correct
                if (!validateDetails(firstName, lastName, phoneNumber, gender)) {
                    Toast.makeText(getActivity(), "Some fields are invalid",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                UserDetails user = new UserDetails(firstName, lastName, phoneNumber,
                        UserDetails.eGender.valueOf(gender), mUserEmail);
                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Profile").setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getActivity(), "Saved",
                                    Toast.LENGTH_LONG).show();
                            mIsSave = true;
                        } else {
                            Toast.makeText(getActivity(), "Error",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        Button buttonNext = view.findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsSave) {
                    MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                    mainAppActivity.LoadDashBoardFragment();
                }
            }
        });


        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onUpdateUserInfoFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUpdateUserInfoFragmentInteractionListener) {
            mListener = (OnUpdateUserInfoFragmentInteractionListener) context;
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

    public interface OnUpdateUserInfoFragmentInteractionListener {
        void onUpdateUserInfoFragmentInteraction(Uri uri);
    }
}
