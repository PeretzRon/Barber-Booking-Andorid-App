package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hit.haircutappointments.Adapters.RegisteredUsersAdapter;
import com.hit.haircutappointments.Classes.SortCompertor.NameComparator;
import com.hit.haircutappointments.Classes.RecycleViewsClasses.RegisteredUsersList;
import com.hit.haircutappointments.Classes.Users.UserDetails;
import com.hit.haircutappointments.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class RegisteredUsersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ArrayList<RegisteredUsersList> mRegisteredUsersArrayList;
    public static RegisteredUsersAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView mRecyclerView;
    public static View.OnClickListener mMyOnClickListener;
    private Bitmap mMaleGenderSymbol;
    private Bitmap mFemaleGenderSymbol;
    private EditText mTextViewInputSearch;
    private Button mButtonDeleteText;
    private Parcelable mRecyclerViewState;
    private boolean isFirstTimeLoaded;

    private OnRegisteredUsersFragmentInteractionListener mListener;

    public RegisteredUsersFragment() {
        // Required empty public constructor
    }

    public static RegisteredUsersFragment newInstance(String param1, String param2) {
        RegisteredUsersFragment fragment = new RegisteredUsersFragment();
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
        Objects.requireNonNull(getActivity()).setTitle("Registered Customers");
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registered_users, container, false);

        // add back button to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRegisteredUsersArrayList = new ArrayList<>();
        mTextViewInputSearch = view.findViewById(R.id.inputSearch);
        mMaleGenderSymbol = BitmapFactory.decodeResource(getResources(), R.drawable.icon_male);
        mFemaleGenderSymbol = BitmapFactory.decodeResource(getResources(), R.drawable.icon_female);
        mButtonDeleteText = view.findViewById(R.id.buttonDeleteText);
        mButtonDeleteText.setVisibility(View.INVISIBLE);
        mMyOnClickListener = new RegisteredUsersFragment.MyOnClickListener(getActivity());
        mRecyclerView = view.findViewById(R.id.my_recycler_view_4);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        isFirstTimeLoaded = true;

        // get the users from the fireBase
        DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference("Users");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullName;
                String phoneNumber;
                String email;
                Bitmap bitmap;
                mRegisteredUsersArrayList.clear(); // clear the list for the next data change
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    // create new user
                    UserDetails userDetails = user.child("Profile").getValue(UserDetails.class);
                    if (userDetails != null) {
                        fullName = userDetails.getmFirstName() + " " + userDetails.getmLastName();
                        phoneNumber = userDetails.getmPhoneNumber();
                        email = userDetails.getmEmail();
                        if (userDetails.getmGender() == UserDetails.eGender.Male) {
                            // it is male then set boy image
                            bitmap = mMaleGenderSymbol;
                        } else {
                            // it is female then set boy image
                            bitmap = mFemaleGenderSymbol;
                        }

                        // add the user to arrayList
                        mRegisteredUsersArrayList.add(new RegisteredUsersList(fullName,
                                phoneNumber, email,
                                bitmap));
                    }
                }

                // sort the list by the names and set the recycle view
                Collections.sort(mRegisteredUsersArrayList, new NameComparator()); // sort list

                // set recycle view
                if(isFirstTimeLoaded){
                    int resId = R.anim.layout_animation_fall_down;
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                    mRecyclerView.setLayoutAnimation(animation);
                    isFirstTimeLoaded = false;
                }

                mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
                mAdapter = new RegisteredUsersAdapter(mRegisteredUsersArrayList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mButtonDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewInputSearch.setText(""); // when click on clear text, set text to empty
            }
        });

        mTextViewInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mButtonDeleteText.setVisibility(View.GONE);
                    int paddingDp = 10;
                    float density = getActivity().getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    mTextViewInputSearch.setPadding(paddingPixel, 0, 0, 0);
                    mTextViewInputSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
                } else {
                    mTextViewInputSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    int paddingDp = 63;
                    float density = getActivity().getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    mTextViewInputSearch.setPadding(paddingPixel, 0, 0, 0);
                    mButtonDeleteText.setVisibility(View.VISIBLE);
                }

                ArrayList<RegisteredUsersList> filterArrayList;
                filterArrayList = filterByNameAndEmail(mRegisteredUsersArrayList, s.toString());  // filter the list via search
                Collections.sort(filterArrayList, new NameComparator()); // sort list by name

                // set the recycle view

                mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
                mAdapter = new RegisteredUsersAdapter(filterArrayList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState(); // save location
            }
        });

        return view;
    }

    private ArrayList<RegisteredUsersList> filterByNameAndEmail(ArrayList<RegisteredUsersList> input, String word) // for search edit text - filter function
    {
        ArrayList<RegisteredUsersList> arr = new ArrayList<>();
        word = word.toLowerCase();
        for (RegisteredUsersList s : input) {
            // do the filter by name or email
            if (s.getmFullName().toLowerCase().contains(word) || s.getmEmail().toLowerCase().contains(word)) {
                arr.add(s);
            }
        }
        return arr;
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


    public void onRegisteredUsersButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRegisteredUsersFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisteredUsersFragmentInteractionListener) {
            mListener = (OnRegisteredUsersFragmentInteractionListener) context;
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

    public interface OnRegisteredUsersFragmentInteractionListener {
        void onRegisteredUsersFragmentInteraction(Uri uri);
    }
}
