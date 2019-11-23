package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hit.haircutappointments.Activities.MainAppActivity;
import com.hit.haircutappointments.Adapters.ComingAndPassedAdapter;
import com.hit.haircutappointments.Classes.Calander.Day;
import com.hit.haircutappointments.Classes.Calander.Event;
import com.hit.haircutappointments.Classes.RecycleViewsClasses.ComingAndPassedEventsList;
import com.hit.haircutappointments.Classes.RecyclerTouchHelper;
import com.hit.haircutappointments.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MyComingEventsFragment extends Fragment implements RecyclerTouchHelper.RecyclerItemTouchHelperListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String mUserIdFromFireBase;
    private ArrayList<ComingAndPassedEventsList> mEventsOfCurrentUser;
    public static ComingAndPassedAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView mRecyclerView;
    public static View.OnClickListener mMyOnClickListener;
    private Bitmap mScheduledEventImage;
    private Bitmap mPassedEventImage;
    private Bitmap mTodayEventImage;
    private Calendar mTodayDate;
    private DateFormat mDateFormat;
    private OnMyComingEventsFragmentInteractionListener mListener;
    private String mUserIdForUndoAction;
    private Parcelable mRecyclerViewState;
    private int mCountChildren;
    private boolean isFirstTimeLoaded;

    public MyComingEventsFragment() {
        // Required empty public constructor
    }

    public static MyComingEventsFragment newInstance(String param1, String param2) {
        MyComingEventsFragment fragment = new MyComingEventsFragment();
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
        Objects.requireNonNull(getActivity()).setTitle("My events");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_coming_events, container, false);

        // add back button to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isFirstTimeLoaded = true;
        mUserIdForUndoAction = "";
        mTodayDate = Calendar.getInstance();
        mEventsOfCurrentUser = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        mDateFormat = new SimpleDateFormat(getString(R.string.date_format));
        int mYear = calendar.get(Calendar.YEAR);

        // set images
        mScheduledEventImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.event_scheduled);
        mPassedEventImage = BitmapFactory.decodeResource(getResources(), R.drawable.event_passed);
        mTodayEventImage = BitmapFactory.decodeResource(getResources(), R.drawable.event_today);

        // set init for recycle view
        mMyOnClickListener = new MyOnClickListener(getActivity());
        mRecyclerView = view.findViewById(R.id.my_recycler_view_3);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // check the user id from fireBase
        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserIdFromFireBase = currentFireBaseUser.getUid();

        // get details from fireBase
        DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference("Days").child(String.valueOf(mYear));
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEventsOfCurrentUser.clear();
                Bitmap bitmap;
                for (DataSnapshot mount : dataSnapshot.getChildren()) {
                    for (DataSnapshot day : mount.getChildren()) {
                        Day theDay = day.getValue(Day.class);
                        for (Event event : theDay.getmEvents()) {
                            if (event.getmUserId().equals(mUserIdFromFireBase) &&
                                    event.getmEventStatus() == Event.eEventStatus.UnAvailable) {
                                if (theDay.getmFullDate().equals(mDateFormat.format(mTodayDate.getTime()))) {
                                    // to user there is event today, so put the suitable bitmap
                                    bitmap = mTodayEventImage;
                                } else if (theDay.getmDay().before(mTodayDate.getTime())) {
                                    // to user there is old event, so put the suitable bitmap
                                    bitmap = mPassedEventImage;
                                } else {
                                    // to user there is scheduled event, so put the suitable bitmap
                                    bitmap = mScheduledEventImage;
                                }

                                // add to the list the event
                                mEventsOfCurrentUser.add(new ComingAndPassedEventsList(
                                        theDay.getmFullDate(),
                                        event.getmEventTime().getmStartTimeString(),
                                        bitmap));
                            }
                        }
                    }
                }

                // message that if there no event for that user, so make a Toast message
                if (mEventsOfCurrentUser.size() == 0) {
                    Toast.makeText(getActivity(), "There are no events exist", Toast.LENGTH_LONG).show();
                }

                // create the recycle view
                if(isFirstTimeLoaded){
                    int resId = R.anim.layout_animation_fall_down;
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                    mRecyclerView.setLayoutAnimation(animation);
                    isFirstTimeLoaded = false;
                }

                mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
                mAdapter = new ComingAndPassedAdapter(mEventsOfCurrentUser);
                mRecyclerView.setAdapter(mAdapter);

                ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                        new RecyclerTouchHelper(0, ItemTouchHelper.LEFT,
                                MyComingEventsFragment.this);
                new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        String day = mEventsOfCurrentUser.get(viewHolder.getAdapterPosition()).getmFullDate();
        String eventTime = mEventsOfCurrentUser.get(viewHolder.getAdapterPosition()).getmTime();
        if(!isDateIsPassedFromToday(day)){
            String[] daySelected = day.split("/");
            mEventsOfCurrentUser.remove(position);
            mAdapter.notifyDataSetChanged();
            deleteEvent(daySelected[2], daySelected[1], daySelected[0],
                    eventTime);
        }
        else{
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(),"Can't delete passed events", Toast.LENGTH_LONG).show();
        }


    }

    private Date convertStringDateToDateFormat(String iDate) {
        Date dt1 = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            dt1 = format1.parse(iDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dt1;
    }

    private boolean isDateIsPassedFromToday(String iDate) {
        Date date = convertStringDateToDateFormat(iDate);
        boolean isDateIsBeforeToday = false;
        Calendar cal = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = myDateFormat.format(today.getTime());
        Date date1 = convertStringDateToDateFormat(formattedDate);
        today.setTime(date1);
        cal.setTime(date);
        if (cal.before(today)) {
            isDateIsBeforeToday = true;
        }

        return isDateIsBeforeToday;
    }


    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

        }
    }


    private void deleteEvent(final String iYear, final String iMount, final String iDay,
                             final String iStartEventTime) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(
                "Days").child(iYear).child(iMount).child(iDay);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Day day = dataSnapshot.getValue(Day.class);
                mCountChildren = 1;
                if (day != null) {
                    for (Event elem : day.getmEvents()) {
                        if (elem.getmEventTime().getmEndTimeString().equals(iStartEventTime)) {
                            rootRef.child("mEvents").child(String.valueOf(mCountChildren)).child(
                                    "mEventStatus").setValue(Event.eEventStatus.Available.toString());
                            mUserIdForUndoAction =
                                    (String) dataSnapshot.child("mEvents").child(String.valueOf(mCountChildren)).child(
                                            "mUserId").getValue();
                            rootRef.child("mEvents").child(String.valueOf(mCountChildren)).child(
                                    "mUserId").setValue("");
                            Snackbar snackbar =
                                    Snackbar.make(getView(), "", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    undoActionOfDeleteEvent(mUserIdForUndoAction,
                                            String.valueOf(mCountChildren), iYear, iMount, iDay);

                                }
                            });
                            snackbar.setActionTextColor(Color.YELLOW);
                            snackbar.show();
                            return;
                        }

                        mCountChildren++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void undoActionOfDeleteEvent(final String iUserId, String iChildName, String iYear,
                                         String iMount, String iDay) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(
                "Days").child(iYear).child(iMount).child(iDay);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("mEvents").child(String.valueOf(mCountChildren)).child(
                        "mUserId").setValue(iUserId);
                rootRef.child("mEvents").child(String.valueOf(mCountChildren)).child(
                        "mEventStatus").setValue(Event.eEventStatus.UnAvailable.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onMyComingEventsButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMyComingEventsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyComingEventsFragmentInteractionListener) {
            mListener = (OnMyComingEventsFragmentInteractionListener) context;
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

    public interface OnMyComingEventsFragmentInteractionListener {
        void onMyComingEventsFragmentInteraction(Uri uri);
    }
}
