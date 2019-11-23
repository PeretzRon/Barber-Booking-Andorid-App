package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hit.haircutappointments.Activities.MainAppActivity;
import com.hit.haircutappointments.R;

import java.util.Objects;

public class AdminFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String mSundayThursdayStr;
    private String mFridayHolidayStr;

    private OnAdminFragmentInteractionListener mListener;

    public AdminFragment() {
        // Required empty public constructor
    }

    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Administrator DashBoard");
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setTitle("Administrator DashBoard");
    }



    // to disappear the menu bar
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        // add back button to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSundayThursdayStr = "";
        mFridayHolidayStr = "";

        LinearLayout addRemoveDates = view.findViewById(R.id.addRemoveDates);
        LinearLayout customersInfo = view.findViewById(R.id.customersDetails);
        LinearLayout myTodayEvents = view.findViewById(R.id.exitAdminTools);
        LinearLayout updateStoreHours = view.findViewById(R.id.storeHoursUpdate);

        // onclick on options
        addRemoveDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.LoadFragment(new AddRemoveDaysAdminFragment(),"AddDeleteDate");
            }
        });

        // onclick on options
        customersInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.LoadFragment(new RegisteredUsersFragment(),"RegisteredUsers");
            }
        });

        // onclick on options
        updateStoreHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdateStoreHours();
            }
        });

        // onclick on options
        myTodayEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.onBackPressed();
            }
        });

        final DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference(
                        "StoreHours");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSundayThursdayStr = (String) dataSnapshot.child("1").getValue();
                mFridayHolidayStr = (String) dataSnapshot.child("2").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void showDialogUpdateStoreHours() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Store Hours");
        builder.setIcon(R.drawable.ic_save_black_24dp);  // make icon to dialog
        builder.setCancelable(true);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view_open_hours, null);
        final EditText sundayThursday = view.findViewById(R.id.editTextSundayThursday);
        final EditText fridayHoliday = view.findViewById(R.id.editTextFridayHoliday);
        sundayThursday.setText(mSundayThursdayStr);
        fridayHoliday.setText(mFridayHolidayStr);
        builder.setView(view);
        builder.setPositiveButton(
                "Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mSundayThursdayStr = sundayThursday.getText().toString();
                        mFridayHolidayStr= fridayHoliday.getText().toString();

                        // after get the strings from editTexts save in fireBase
                        updateStoreHoursOnFireBase(mSundayThursdayStr, mFridayHolidayStr);

                    }
                });
        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    // saving to fireBase the store hours
    private void updateStoreHoursOnFireBase(final String iSundayThursday, final String iFridayHoliday){
        final DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference(
                        "StoreHours");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // set value from edit text to fireBase
                rootRef.child("1").setValue(iSundayThursday);
                rootRef.child("2").setValue(iFridayHoliday);
                Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void onAdminButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAdminFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAdminFragmentInteractionListener) {
            mListener = (OnAdminFragmentInteractionListener) context;
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


    public interface OnAdminFragmentInteractionListener {
        void onAdminFragmentInteraction(Uri uri);
    }
}
