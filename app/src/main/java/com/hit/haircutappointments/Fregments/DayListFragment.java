package com.hit.haircutappointments.Fregments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hit.haircutappointments.Activities.MainAppActivity;
import com.hit.haircutappointments.Adapters.OpenedDatesAdapter;
import com.hit.haircutappointments.Classes.Calander.Day;
import com.hit.haircutappointments.Classes.Calander.Event;
import com.hit.haircutappointments.Classes.RecycleViewsClasses.MainDateList;
import com.hit.haircutappointments.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DayListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ArrayList<MainDateList> mDaysArrayList;
    public static OpenedDatesAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView mRecyclerView;
    public static View.OnClickListener mMyOnClickListener;
    public static View.OnLongClickListener mMyOnLongClickListener;
    private int mDay;
    private int mMount;
    private int mYear;
    private Bitmap mAvailableEventsImage;
    private Bitmap mAllUnavailableEventsImage;
    private Parcelable mRecyclerViewState;
    private String mAdminUserId;
    private String mUserIdFromFireBase;
    private boolean isFirstTimeLoaded;


    private OnDayListFragmentInteractionListener mListener;

    public DayListFragment() {
        // Required empty public constructor
    }

    public static DayListFragment newInstance(String param1, String param2) {
        DayListFragment fragment = new DayListFragment();
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
        Objects.requireNonNull(getActivity()).setTitle("Days"); // set name to action bar
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
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setTitle("Days"); // set name to action bar
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_list, container, false);

        // add back button to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set images to variables
        mAvailableEventsImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.count_available_events);
        mAllUnavailableEventsImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.count_unavailable_events);

        mDaysArrayList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMount = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);
        isFirstTimeLoaded = true;

        // set the current date to string by year, mount, day
        final String currentYearFormat = String.valueOf(mYear);
        final String currentMountFormat = convertMount(mMount);
        final String currentDayFormat = convertDay(mDay);

        // set the next mount date to string by year, mount, day
        calendar.add(Calendar.MONTH, 1); // what is the next mount
        final String nextYearOfNextDay = String.valueOf(calendar.get(Calendar.YEAR));
        final String nextMountOfNextDay = convertMount(calendar.get(Calendar.MONTH));
        final String nextDayOfNextDay = convertDay(calendar.get(Calendar.DAY_OF_MONTH));


        // init things for the recycle view
        mMyOnClickListener = new MyOnClickListener(getActivity());
        mMyOnLongClickListener = new MyOnLongClickListener(getActivity());
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdminUserId = "iAzYzzTaMBgxkyYAxqLIgcPTBaG2"; // admin user (next version will change)
        // check the user id
        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserIdFromFireBase = currentFireBaseUser.getUid();

        // calc the days to the recycle view
        final DatabaseReference rootRef2 =
                FirebaseDatabase.getInstance().getReference("Days");
        rootRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDaysArrayList.clear();
                int counterAvailableEvents;
                Bitmap bitmap;
                for (DataSnapshot years : dataSnapshot.getChildren()) {
                    if (Objects.equals(years.getKey(), currentYearFormat)) {
                        for (DataSnapshot mount : years.getChildren()) {
                            if (Objects.equals(mount.getKey(), currentMountFormat)) {
                                for (DataSnapshot day : mount.getChildren()) {
                                    // that for that show only from today till end of mount.
                                    if (Integer.valueOf(Objects.requireNonNull(day.getKey())) >= Integer.valueOf(currentDayFormat)) {
                                        String fullDate =
                                                day.getKey() + "/" + currentMountFormat + "/" + currentYearFormat;
                                        Day currentDay = day.getValue(Day.class);
                                        int countEventOnThisDay = currentDay.getmEvents().size();
                                        counterAvailableEvents = 0; // counter for show how much
                                        // events are available
                                        for (Event event : currentDay.getmEvents()) {
                                            if (event.getmEventStatus().toString().equals(Event.eEventStatus.Available.toString())) {
                                                counterAvailableEvents++;
                                            }
                                        }

                                        String text;
                                        if (counterAvailableEvents == 0) {
                                            bitmap = mAllUnavailableEventsImage;
                                            text = "No Available events";
                                        } else {
                                            bitmap = mAvailableEventsImage;
                                            text = String.format("(%s/%s events are available)",
                                                    String.valueOf(counterAvailableEvents),
                                                    String.valueOf(countEventOnThisDay));
                                        }

                                        mDaysArrayList.add(new MainDateList(fullDate, day.getKey(),
                                                getDayNameByDate(fullDate),
                                                currentDay, text, bitmap));

                                    }
                                }
                            }
                        }
                    }
                }

                // for the next mount
                for (DataSnapshot years : dataSnapshot.getChildren()) {
                    if (Objects.equals(years.getKey(), nextYearOfNextDay)) {
                        for (DataSnapshot mount : years.getChildren()) {
                            if (Objects.equals(mount.getKey(), nextMountOfNextDay)) {
                                for (DataSnapshot day : mount.getChildren()) {
                                    String fullDate =
                                            day.getKey() + "/" + nextMountOfNextDay + "/" + nextYearOfNextDay;
                                    Day currentDay = day.getValue(Day.class);
                                    int countEventOnThisDay = currentDay.getmEvents().size();
                                    counterAvailableEvents = 0;
                                    for (Event event : currentDay.getmEvents()) {
                                        if (event.getmEventStatus().toString().equals(Event.eEventStatus.Available.toString())) {
                                            counterAvailableEvents++;
                                        }
                                    }

                                    String text;
                                    if (counterAvailableEvents == 0) {
                                        bitmap = mAllUnavailableEventsImage;
                                        text = "No Available events";
                                    } else {
                                        bitmap = mAvailableEventsImage;
                                        text = String.format("(%s/%s events are available)",
                                                String.valueOf(counterAvailableEvents),
                                                String.valueOf(countEventOnThisDay));
                                    }

                                    mDaysArrayList.add(new MainDateList(fullDate, day.getKey(),
                                            getDayNameByDate(fullDate),
                                            currentDay, text, bitmap));


                                }
                            }
                        }
                    }
                }

                // set the recycle view after the arrayList is ready
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
                if(isFirstTimeLoaded){
                    int resId = R.anim.layout_animation_from_right;
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                    mRecyclerView.setLayoutAnimation(animation);
                    isFirstTimeLoaded = false;
                }

                mAdapter = new OpenedDatesAdapter(mDaysArrayList);
                mRecyclerView.setAdapter(mAdapter);
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

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int selectedItemPosition = mRecyclerView.getChildAdapterPosition(v);
            TextView textFromCard =
                    mRecyclerView.findViewHolderForAdapterPosition(selectedItemPosition).itemView.findViewById(R.id.DateTextView);
            String day = textFromCard.getText().toString();
            String[] daySelected = day.split("/");
            MainAppActivity loadSpecificDayEvents = (MainAppActivity) getActivity();
            loadSpecificDayEvents.LoadSpecificDayEventsFragment(daySelected[2], daySelected[1],
                    daySelected[0]);
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
                TextView textFromCard =
                        mRecyclerView.findViewHolderForAdapterPosition(selectedItemPosition).itemView.findViewById(R.id.DateTextView);
                String day = textFromCard.getText().toString();
                String[] daySelected = day.split("/");
                showDialogToCancelAllEvents(daySelected);
            }

            return false;
        }
    }

    private void showDialogToCancelAllEvents(final String[] iDaySelected) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cancel All Events");
        builder.setMessage("Are you sure you want to set all events to unavailable?");
        builder.setIcon(R.drawable.ic_delete_black_24dp);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final DatabaseReference rootRef =
                                FirebaseDatabase.getInstance().getReference("Days").child(iDaySelected[2]).
                                        child(iDaySelected[1]).child(iDaySelected[0]);
                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Day currentDay = dataSnapshot.getValue(Day.class);
                                for (Event event : currentDay.getmEvents()) {
                                    event.setmEventStatus(Event.eEventStatus.CancelByManager);
                                    event.setmUserId(mAdminUserId);
                                }
                                rootRef.setValue(currentDay);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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

    // day must be in 2 digits (for example 4 --> 04)
    private String convertDay(int iDay) {
        if (iDay < 10) {
            return "0" + iDay;
        } else {
            return String.valueOf(iDay);
        }
    }

    // mount must be add + 1
    private String convertMount(int iMount) {
        if (iMount < 9) {
            return "0" + (1 + iMount);
        } else {
            return String.valueOf(1 + iMount);
        }
    }


    @SuppressLint("SimpleDateFormat")
    private String getDayNameByDate(String iDate) {
        DateFormat format2 = null;
        Date dt1 = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            dt1 = format1.parse(iDate);
            format2 = new SimpleDateFormat("EEEE");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format2.format(dt1);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDayListFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDayListFragmentInteractionListener) {
            mListener = (OnDayListFragmentInteractionListener) context;
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

    public interface OnDayListFragmentInteractionListener {
        void onDayListFragmentInteraction(Uri uri);
    }
}
