package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.hit.haircutappointments.Activities.IdentificationActivity;
import com.hit.haircutappointments.R;

public class SignupFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ProgressBar mProgressBar2;
    private OnSignUpFragmentInteractionListener mListener;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        final View view =  inflater.inflate(R.layout.fragment_signup, container, false);
        final EditText mEmailView = view.findViewById(R.id.email);
        final EditText mPasswordView = view.findViewById(R.id.password);
        final EditText mPasswordViewRepeat = view.findViewById(R.id.password_again);
        mProgressBar2 = view.findViewById(R.id.progressBar2);
        mProgressBar2.setVisibility(View.GONE);

        // get all info from the edit text and call attemptSignUp on the activity
        Button buttonSignUp = view.findViewById(R.id.buttonRegister);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailView.getText().toString().trim();
                final String password = mPasswordView.getText().toString();
                final String passwordAgain = mPasswordViewRepeat.getText().toString();
                IdentificationActivity loginActivity = (IdentificationActivity) getActivity();
                loginActivity.attemptSignUp(true,email,password, passwordAgain,  view);
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSignUpFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpFragmentInteractionListener) {
            mListener = (OnSignUpFragmentInteractionListener) context;
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

    public interface OnSignUpFragmentInteractionListener {
        void onSignUpFragmentInteraction(Uri uri);
    }}
