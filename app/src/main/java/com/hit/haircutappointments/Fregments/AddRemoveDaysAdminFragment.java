package com.hit.haircutappointments.Fregments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hit.haircutappointments.Classes.Calander.Day;
import com.hit.haircutappointments.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class AddRemoveDaysAdminFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private DatePickerDialog datePickerDialog;
    private Calendar mCalendar;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private TextView mTextViewStart;
    private TextView mTextViewEnd;
    private TextView mTextViewResults;
    private TextView mTextViewShowResult;
    private Button mButtonCalc;
    private ArrayList<Calendar> mArrayListOfDatesBetweenStartToEnd;
    private ArrayList<Day> mArrayListDayToAddOrRemoveToFireBase;
    private boolean mIsStart;
    private DateFormat mDateFormat;
    private Button mSaveDatesButton;
    private Button mDeleteDatesButton;
    private String allLogs;
    private OnAddRemoveDaysFragmentInteractionListener mListener;
    private RadioGroup mRbGroup;
    private RadioButton mRadioButtonLongDay;
    private RadioButton mRadioButtonShortDay;
    private Day.eDayLong mLengthOfDay;

    public AddRemoveDaysAdminFragment() {
    }

    public static AddRemoveDaysAdminFragment newInstance(String param1, String param2) {
        AddRemoveDaysAdminFragment fragment = new AddRemoveDaysAdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Add/Remove Dates");
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
        Objects.requireNonNull(getActivity()).setTitle("Add/Remove Dates"); // set name action bar
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_remove_days_admin, container, false);
        // add back button to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mIsStart = false;
        mLengthOfDay = Day.eDayLong.LongDay; // default long day
        mStartDate = Calendar.getInstance(); // get the current date
        mEndDate = Calendar.getInstance();  // get the current date
        mArrayListOfDatesBetweenStartToEnd = new ArrayList<>();
        mArrayListDayToAddOrRemoveToFireBase = new ArrayList<>();
        Button buttonPickUpStartDate = view.findViewById(R.id.buttonPickUpStartDate);
        buttonPickUpStartDate.setOnClickListener(this);
        Button buttonPickUpEndDate = view.findViewById(R.id.buttonPickUpEndDate);
        buttonPickUpEndDate.setOnClickListener(this);
        mButtonCalc = view.findViewById(R.id.buttonCalcDates);
        mButtonCalc.setOnClickListener(this);
        mButtonCalc.setEnabled(false);
        mTextViewEnd = view.findViewById(R.id.textViewToDate);
        mTextViewStart = view.findViewById(R.id.textViewFromDate);
        mTextViewResults = view.findViewById(R.id.textViewShowSelectedDates);
        mSaveDatesButton = view.findViewById(R.id.buttonSaveFireBase);
        mDeleteDatesButton = view.findViewById(R.id.buttonDelete);
        mTextViewShowResult = view.findViewById(R.id.textViewShowResult);
        mRbGroup = view.findViewById(R.id.radio_group);
        mRadioButtonLongDay = view.findViewById(R.id.radio_button_long_day);
        mRbGroup.check(R.id.radio_button_long_day);
        mRadioButtonShortDay = view.findViewById(R.id.radio_button_short_day);

        setSaveAndDeleteButtonStatus(false); // disable save and delete buttons
        mTextViewResults.setMovementMethod(new ScrollingMovementMethod()); // make text view scroll
        mTextViewShowResult.setMovementMethod(new ScrollingMovementMethod()); // make text view
        // scroll
        mDateFormat = new SimpleDateFormat(getString(R.string.date_format));
        setSaveAndDeleteButtonStatus(false);

        // listener when press on the radio button
        mRbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRadioButtonLongDay.getId())
                    mLengthOfDay = Day.eDayLong.LongDay;
                else if (checkedId == mRadioButtonShortDay.getId())
                    mLengthOfDay = Day.eDayLong.ShortDay;
            }
        });

        mTextViewStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSaveAndDeleteButtonStatus(false);

                if (mTextViewEnd.getText().toString().isEmpty()) {
                    mButtonCalc.setEnabled(false);
                    return;
                }

                if (!mTextViewStart.getText().toString().isEmpty()) {
                    if (mStartDate.before(mEndDate) || mStartDate.equals(mEndDate)) {
                        // true if the dates are correct logical
                        mButtonCalc.setEnabled(true);
                    } else {
                        mButtonCalc.setEnabled(false);
                    }
                }
            }
        });

        mTextViewEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSaveAndDeleteButtonStatus(false);
                if (mTextViewStart.getText().toString().isEmpty()) {
                    mButtonCalc.setEnabled(false);
                    return;
                }

                if (mStartDate.before(mEndDate) || mStartDate.equals(mEndDate)) {
                    // true if the dates are correct logical
                    mButtonCalc.setEnabled(true);
                } else {
                    mButtonCalc.setEnabled(false);
                }

            }
        });

        mSaveDatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allLogs = "";
                mTextViewShowResult.setText(""); // reset the text view to empty string
                for (final Day day : mArrayListDayToAddOrRemoveToFireBase) {
                    final String[] dayMountYear;
                    final String fullDateString = mDateFormat.format(day.getmDay());
                    dayMountYear = fullDateString.split("/");
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Days"
                    ).child(dayMountYear[2]).child(dayMountYear[1]).child(dayMountYear[0]);
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (!snapshot.hasChildren()) {
                                // date doesn't exist - > save it.
                                FirebaseDatabase.getInstance().getReference("Days").child(dayMountYear[2]).child(dayMountYear[1]).child(dayMountYear[0]).setValue(day,
                                        new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError,
                                                                   @NonNull DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    mTextViewShowResult.setText(String.format("%s" +
                                                                    " " +
                                                                    "%s%s", fullDateString,
                                                            getString(R.string.date_save_successfully), "\n"));
                                                    mTextViewShowResult.setText(allLogs);

                                                } else {
                                                    mTextViewShowResult.setText(String.format("%s" +
                                                                    " " +
                                                                    "%s%s", fullDateString,
                                                            getString(R.string.error_not_save),
                                                            "\n"));
                                                    mTextViewShowResult.setText(allLogs);
                                                }
                                            }
                                        });
                            } else {
                                // date already in use, so can't save again
                                mTextViewShowResult.setText(String.format("%s %s%s", fullDateString,
                                        getString(R.string.date_in_use), "\n"));
                                mTextViewShowResult.setText(allLogs);
                            }

                            setSaveAndDeleteButtonStatus(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        mTextViewShowResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                allLogs = s.toString(); // before the text changed save to text in allLogs
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                allLogs += s.toString(); // concat before changed and update changed to allLogs

            }
        });

        mDeleteDatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allLogs = "";
                mTextViewShowResult.setText(""); // reset the text view to empty string
                for (final Day day : mArrayListDayToAddOrRemoveToFireBase) {
                    final String[] dayMountYear;
                    final String fullDateString = mDateFormat.format(day.getmDay());
                    dayMountYear = fullDateString.split("/"); // split to [day, mount, year]
                    final DatabaseReference rootRef =
                            FirebaseDatabase.getInstance().getReference("Days").child(dayMountYear[2]).child(dayMountYear[1]).child(dayMountYear[0]);
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                // date remove
                                rootRef.removeValue();
                                mTextViewShowResult.setText(String.format("%s %s%s", fullDateString,
                                        getString(R.string.remove), "\n"));
                                mTextViewShowResult.setText(allLogs);
                            } else {
                                // date does not exist
                                mTextViewShowResult.setText(String.format("%s %s%s", fullDateString,
                                        getString(R.string.error_date_is_not_exist), "\n"));
                                mTextViewShowResult.setText(allLogs);
                            }
                            setSaveAndDeleteButtonStatus(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        // get the current date
        mCalendar = Calendar.getInstance();
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int mount = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);

        // date picker dialog listener
        datePickerDialog = new DatePickerDialog(getActivity(), this, year, mount, day);

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (mIsStart) {
            mStartDate.set(year, month, dayOfMonth);
            mTextViewStart.setText(String.format("%s/%s/%s", dayOfMonth, month + 1, year));

        } else {
            mEndDate.set(year, month, dayOfMonth);
            mTextViewEnd.setText(String.format("%s/%s/%s", dayOfMonth, month + 1, year));

        }
    }

    // calc the dates between the range of tha start date to end date
    private void allDateBetweenDates(Calendar iStartDate, Calendar iEndDate) {
        mArrayListOfDatesBetweenStartToEnd.clear();
        while (iStartDate.before(iEndDate) || iStartDate.equals(iEndDate)) {
            Calendar dateToAdd = (Calendar) iStartDate.clone();
            mArrayListOfDatesBetweenStartToEnd.add(dateToAdd);
            iStartDate.add(Calendar.DAY_OF_MONTH, 1);
        }
    }


    // print Dates To Text ViewResults and add to arrayList for preform action
    private void printDatesToTextViewResults() {
        mArrayListDayToAddOrRemoveToFireBase.clear();
        StringBuilder stringBuilder = new StringBuilder();
        String fullDate, fullDateWithNewLine;
        for (Calendar item : mArrayListOfDatesBetweenStartToEnd) {
            fullDate = String.format("%s", mDateFormat.format(item.getTime()));
            fullDateWithNewLine = String.format("%s%s", mDateFormat.format(item.getTime()),
                    System.lineSeparator());
            mArrayListDayToAddOrRemoveToFireBase.add(new Day(item.getTime(), mLengthOfDay,
                    fullDate));
            stringBuilder.append(fullDateWithNewLine);
        }

        mTextViewResults.setText(stringBuilder.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPickUpStartDate:
            case R.id.buttonPickUpEndDate: {
                mIsStart = ((Button) v).getText().toString().equalsIgnoreCase("From Date: ");
                // this boolean flag keep if start time is pressed or end time is pressed
                datePickerDialog.show();
                break;
            }
            case R.id.buttonCalcDates:
                if (mButtonCalc.isClickable()) {
                    Calendar calendar = (Calendar) mStartDate.clone();
                    allDateBetweenDates(calendar, mEndDate);
                    setSaveAndDeleteButtonStatus(true);
                    printDatesToTextViewResults();
                }
                break;
        }
    }

    // enable or disable save and delete buttons
    private void setSaveAndDeleteButtonStatus(boolean isEnable) {
        mSaveDatesButton.setEnabled(isEnable);
        mDeleteDatesButton.setEnabled(isEnable);
    }

    public void onAddRemoveDaysButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAddRemoveDaysFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddRemoveDaysFragmentInteractionListener) {
            mListener = (OnAddRemoveDaysFragmentInteractionListener) context;
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


    public interface OnAddRemoveDaysFragmentInteractionListener {
        void onAddRemoveDaysFragmentInteraction(Uri uri);
    }
}
