package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hit.haircutappointments.Adapters.SpecificDayAdapter;
import com.hit.haircutappointments.Adapters.SpecificDayForAdminAdapter;
import com.hit.haircutappointments.Classes.Calander.Day;
import com.hit.haircutappointments.Classes.Calander.Event;
import com.hit.haircutappointments.Classes.Calander.EventTime;
import com.hit.haircutappointments.Classes.RecycleViewsClasses.SpecificDayEventList;
import com.hit.haircutappointments.Classes.RecycleViewsClasses.SpecificDayEventListAdmin;
import com.hit.haircutappointments.Classes.Users.UserDetails;
import com.hit.haircutappointments.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SpecificDayFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<Event> mEventOfDayArrayList;
    private ArrayList<SpecificDayEventList> mSpecificDayEventLists;
    private ArrayList<SpecificDayEventListAdmin> mSpecificDayForAdminEventLists;
    private ArrayList<UserDetails> mUsersDetailsArrayList;
    private HashMap<String, UserDetails> mUserIdToUserDetailsHashMap;
    private static SpecificDayAdapter mAdapter;
    private static SpecificDayForAdminAdapter mAdapterForAdmin;
    private RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView mRecyclerView;
    public static View.OnClickListener mMyOnClickListener;
    public static View.OnLongClickListener mMyOnLongClickListener;
    private Bitmap mAvailableEventImage;
    private Bitmap mInvitedEventImage;
    private Bitmap mOrderByPhoneEventImage;
    private Bitmap mUnavailableEventImage;
    private Bitmap mMyEventImage;
    private String mUserIdFromFireBase;
    private String mYear;
    private String mMount;
    private String mDay;
    private String mAdminUserId;
    private Parcelable mRecyclerViewState;
    private boolean isFirstTimeLoaded;


    private OnSpecificDayFragmentInteractionListener mListener;

    public SpecificDayFragment() {
        // Required empty public constructor
    }

    public static SpecificDayFragment newInstance(String param1, String param2) {
        SpecificDayFragment fragment = new SpecificDayFragment();
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
        Objects.requireNonNull(getActivity()).setTitle("Events");
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // add back button to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getArguments();
        mYear = "None";
        mMount = "None";
        mDay = "None";


        if (bundle != null) {
            mYear = bundle.getString("Year");
            mMount = bundle.getString("Mount");
            mDay = bundle.getString("Day");
        }

        View view = inflater.inflate(R.layout.fragment_specific_day, container, false);

        mAvailableEventImage = BitmapFactory.decodeResource(getResources(), R.drawable.available);
        mInvitedEventImage = BitmapFactory.decodeResource(getResources(), R.drawable.invited_event);
        mOrderByPhoneEventImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.order_by_phone);
        mUnavailableEventImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.cancle_by_admin);
        mMyEventImage = BitmapFactory.decodeResource(getResources(), R.drawable.my_event);
        isFirstTimeLoaded = true;

        // create the list and recycle views
        mEventOfDayArrayList = new ArrayList<>();
        mSpecificDayEventLists = new ArrayList<>();
        mSpecificDayForAdminEventLists = new ArrayList<>();
        mUsersDetailsArrayList = new ArrayList<>();
        mUserIdToUserDetailsHashMap = new HashMap<>();

        // init things for recycle view
        mMyOnClickListener = new SpecificDayFragment.MyOnClickListener(getActivity());
        mMyOnLongClickListener = new SpecificDayFragment.MyOnLongClickListener(getActivity());
        mRecyclerView = view.findViewById(R.id.my_recycler_view2);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdminUserId = "iAzYzzTaMBgxkyYAxqLIgcPTBaG2"; // admin user (next version will change)

        // check the user id
        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserIdFromFireBase = currentFireBaseUser.getUid();

        // get the users from fireBase
        DatabaseReference rootRef2 =
                FirebaseDatabase.getInstance().getReference("Users");
        rootRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsersDetailsArrayList.clear();
                for (DataSnapshot user : dataSnapshot.getChildren()) { // loop all children
                    UserDetails userDetails = user.child("Profile").getValue(UserDetails.class);
                    if (userDetails != null) {
                        UserDetails userDetails1 = new UserDetails(userDetails.getmFirstName(),
                                userDetails.getmLastName(), userDetails.getmPhoneNumber(),
                                userDetails.getmGender(), userDetails.getmEmail(), user.getKey());
                        mUsersDetailsArrayList.add(userDetails1);
                        mUserIdToUserDetailsHashMap.put(user.getKey(), userDetails1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // create a delay for the users load first
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final DatabaseReference rootRef =
                        FirebaseDatabase.getInstance().getReference("Days").child(mYear).child(mMount).child(mDay);
                rootRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            String nameOfClientIfTheEventIsOrdered = "";
                            String phoneOfClientIfTheEventIsOrdered = "";
                            mEventOfDayArrayList.clear(); // clear list for new instance
                            mSpecificDayForAdminEventLists.clear(); // clear list for new instance
                            mSpecificDayEventLists.clear(); // clear list for new instance
                            Bitmap bitmap = mInvitedEventImage;
                            Day theDay = dataSnapshot.getValue(Day.class);
                            mEventOfDayArrayList = theDay.getmEvents();
                            int index = 0; // that index is to know which key is will be on fireBase
                            for (Event elem : mEventOfDayArrayList) {
                                EventTime eventTime = elem.getmEventTime();
                                if (elem.getmEventStatus() == Event.eEventStatus.Available) {
                                    bitmap = mAvailableEventImage; // image for available event
                                } else if (elem.getmEventStatus() == Event.eEventStatus.UnAvailable) {
                                    bitmap = mInvitedEventImage; // image for Unavailable event
                                    if (elem.getmUserId().equals(mUserIdFromFireBase)) {
                                        bitmap = mMyEventImage; // if this is my event the other
                                        // image
                                    }
                                } else if (elem.getmEventStatus() == Event.eEventStatus.OrderedByPhone) {
                                    bitmap = mOrderByPhoneEventImage; // image for order By Phone
                                    try {
                                        if (!elem.getmNameOrderedByPhone().equals("")) {
                                            nameOfClientIfTheEventIsOrdered =
                                                    elem.getmNameOrderedByPhone();
                                            phoneOfClientIfTheEventIsOrdered = elem.getmPhoneOrderedByPhone();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    // event
                                } else if (elem.getmEventStatus() == Event.eEventStatus.CancelByManager) {
                                    bitmap = mUnavailableEventImage; // image cancel By manager
                                }

                                if (elem.getmEventStatus().toString().equals(Event.eEventStatus.UnAvailable.toString())) {
                                    nameOfClientIfTheEventIsOrdered =
                                            mUserIdToUserDetailsHashMap.get(elem.getmUserId()).getmFirstName()
                                                    + " " +
                                                    mUserIdToUserDetailsHashMap.get(elem.getmUserId()).getmLastName();
                                    phoneOfClientIfTheEventIsOrdered =
                                            mUserIdToUserDetailsHashMap.get(elem.getmUserId()).getmPhoneNumber();
                                } else if (elem.getmEventStatus().toString().equals(Event.eEventStatus.OrderedByPhone.toString())) {
                                    System.out.println("");
                                } else {
                                    nameOfClientIfTheEventIsOrdered = "Name: Unavailable";
                                    phoneOfClientIfTheEventIsOrdered = "Phone: Unavailable";
                                }

                                mSpecificDayForAdminEventLists.add(new SpecificDayEventListAdmin(eventTime.getmStartTimeString(),
                                        eventTime.getmEndTimeString(), bitmap,
                                        String.valueOf(index),
                                        nameOfClientIfTheEventIsOrdered,
                                        phoneOfClientIfTheEventIsOrdered));

                                mSpecificDayEventLists.add(new SpecificDayEventList(eventTime.getmStartTimeString(),
                                        eventTime.getmEndTimeString(), bitmap,
                                        String.valueOf(index)));
                                index++;
                            }
                            if (mUserIdFromFireBase.equals(mAdminUserId)) {
                                // that will create a recycle view with details of customer on
                                // the card
                                mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
                                mAdapterForAdmin =
                                        new SpecificDayForAdminAdapter(mSpecificDayForAdminEventLists);
                                mRecyclerView.setAdapter(mAdapterForAdmin);
                            } else {
                                // that will create a recycle view without details of customer on
                                // the card

                                //mLayoutManager.scrollToPosition(location);
                                if (isFirstTimeLoaded) {
                                    int resId = R.anim.layout_animation_from_right;
                                    LayoutAnimationController animation =
                                            AnimationUtils.loadLayoutAnimation(getActivity(),
                                                    resId);
                                    mRecyclerView.setLayoutAnimation(animation);
                                    isFirstTimeLoaded = false;
                                }
                                mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
                                mAdapter = new SpecificDayAdapter(mSpecificDayEventLists);
                                mRecyclerView.setAdapter(mAdapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 500);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            }
        });


        return view;
    }


    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int selectedItemPosition = mRecyclerView.getChildAdapterPosition(v);

            mRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();

            // get the event id (key from fireBase)
            TextView eventId =
                    mRecyclerView.findViewHolderForAdapterPosition(selectedItemPosition).itemView.findViewById(R.id.textViewEventId);
            final String eventID = eventId.getText().toString();
            FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final String currentUserId = currentFireBaseUser.getUid();

            // get the details of the user that order this event (If there is one)
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(
                    "Days").child(mYear).child(mMount).child(mDay).child("mEvents").child(eventID).child("mUserId");
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String userId = (String) dataSnapshot.getValue();
                    if (currentUserId.equals(mAdminUserId)) { // admin was select event
                        showDialogWithAdminOptions(eventID);
                    } else if (userId.equals("")) {   // the event is empty and it isn't admin user
                        showDialogSaveThisEvent(eventID);
                    } else if (userId.equals(currentUserId)) {  // user want to cancel the event
                        showDialogToCancelEvent(eventID);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private class MyOnLongClickListener implements View.OnLongClickListener {

        private final Context context;

        private MyOnLongClickListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onLongClick(View v) {
            if (mAdminUserId.equals(mUserIdFromFireBase)) {
                int selectedItemPosition = mRecyclerView.getChildAdapterPosition(v);
                TextView eventId =
                        mRecyclerView.findViewHolderForAdapterPosition(selectedItemPosition).itemView.findViewById(R.id.textViewEventId);
                final String eventID = eventId.getText().toString();
                showDialogForSaveByPhoneOnlyAdmin(eventID);
            }

            return false;
        }
    }

    private void showDialogForSaveByPhoneOnlyAdmin(final String iEventId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter new Action");
        builder.setIcon(R.drawable.ic_save_black_24dp);
        builder.setCancelable(true);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_for_save_by_phone, null);
        final EditText clientName = view.findViewById(R.id.editTextClientNameSave);
        final EditText phoneNumber = view.findViewById(R.id.editTextPhoneNumberSave);
        builder.setView(view);
        builder.setPositiveButton(
                "Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String clientNameFromEditText = clientName.getText().toString();
                        final String phoneNumberFromEditText = phoneNumber.getText().toString();
                        saveOrderByPhoneToFireBase(clientNameFromEditText,
                                phoneNumberFromEditText, iEventId);

                    }
                });
        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    // dialog for cancel event
    private void showDialogToCancelEvent(final String iEventId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cancel Event");
        builder.setMessage("Are you sure you want to cancel?");
        builder.setIcon(R.drawable.ic_delete_black_24dp);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveOrCancelEventByUser(iEventId, false); // update fireBase that the even is
                // available
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    // dialog for admin user only
    private void showDialogWithAdminOptions(final String iEventId) {
        final String[] adminSelect = new String[1];
        final String[] options =
                getActivity().getResources().getStringArray(R.array.AdminOptions);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose option to this event:");
        builder.setIcon(R.drawable.ic_settings_black_24dp);
        builder.setSingleChoiceItems(R.array.AdminOptions, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adminSelect[0] = options[which];
                    }
                });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateEventFromAdminSelect(adminSelect[0], iEventId);
            }
        });
        AlertDialog alert11 = builder.create();
        alert11.show();

    }

    private void saveOrderByPhoneToFireBase(final String iName, final String iPhoneNubmer,
                                            String iChildId) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(
                "Days").child(mYear).child(mMount).child(mDay).child("mEvents").child(iChildId);

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("mEventStatus").setValue(Event.eEventStatus.OrderedByPhone.toString());
                rootRef.child("mUserId").setValue(Event.eEventStatus.OrderedByPhone.toString());
                rootRef.child("mNameOrderedByPhone").setValue(iName);
                rootRef.child("mPhoneOrderedByPhone").setValue(iPhoneNubmer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // update fireBase according to admin select
    private void updateEventFromAdminSelect(final String iUserAdminChoice, String iEventId) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(
                "Days").child(mYear).child(mMount).child(mDay).child("mEvents").child(iEventId);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (Event.eEventStatus status : Event.eEventStatus.values()) {
                    if (status.toString().equals(iUserAdminChoice)) {
                        rootRef.child("mEventStatus").setValue(status.toString()); // set the
                        // status
                        rootRef.child("mUserId").setValue(mAdminUserId);
                        if (status.toString().equals(Event.eEventStatus.Available.toString())) {
                            // in case admin want to make te event Available so user id muse be
                            // empty
                            rootRef.child("mUserId").setValue("");
                            rootRef.child("mNameOrderedByPhone").removeValue();
                            rootRef.child("mPhoneOrderedByPhone").removeValue();
                        }
                        if (status.toString().equals(Event.eEventStatus.CancelByManager.toString())) {
                            rootRef.child("mNameOrderedByPhone").removeValue();
                            rootRef.child("mPhoneOrderedByPhone").removeValue();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // dialog for save the order
    private void showDialogSaveThisEvent(final String iEventId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Save Event");
        builder.setMessage("Are you sure you want to order this event?");
        builder.setIcon(R.drawable.ic_save_black_24dp);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveOrCancelEventByUser(iEventId, true);
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

    private void saveOrCancelEventByUser(String iEventId, final boolean iToSave) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(
                "Days").child(mYear).child(mMount).child(mDay).child("mEvents").child(iEventId);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (iToSave) {
                    rootRef.child("mEventStatus").setValue(Event.eEventStatus.UnAvailable.toString());
                    rootRef.child("mUserId").setValue(mUserIdFromFireBase);
                } else {
                    rootRef.child("mEventStatus").setValue(Event.eEventStatus.Available.toString());
                    rootRef.child("mUserId").setValue("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void onSpecificDayButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSpecificDayFragmentInteraction(uri);
        }
    }

    // to disappear the menu bar
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSpecificDayFragmentInteractionListener) {
            mListener = (OnSpecificDayFragmentInteractionListener) context;
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

    public interface OnSpecificDayFragmentInteractionListener {
        void onSpecificDayFragmentInteraction(Uri uri);
    }
}
