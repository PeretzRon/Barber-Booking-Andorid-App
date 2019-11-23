package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.hit.haircutappointments.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class DashBoardFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView mTextViewSundayThursdayHours;
    private TextView mTextViewFridayAndHolidayHours;
    private OnDashBoardFragmentInteractionListener mListener;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        Objects.requireNonNull(getActivity()).setTitle("DashBoard");
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mymenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainAppActivity mainAppActivity;
        switch (item.getItemId()) {
            case R.id.disconnect:
                // when that options pressed from action bar then do this:
                mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.showDialogLogOut();
                return true;

            default:
                break;
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setTitle("DashBoard"); // set name to action bar
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);

        // disable back button to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // init buttons / textView / and more
        LinearLayout orderEvent = view.findViewById(R.id.orderEvent);
        final LinearLayout adminArea = view.findViewById(R.id.adminArea);
        LinearLayout myEvents = view.findViewById(R.id.myEvents);
        LinearLayout disconnect = view.findViewById(R.id.Logoff);
        LinearLayout priceList = view.findViewById(R.id.priceList);
        LinearLayout myDetails = view.findViewById(R.id.myDetails);
        TextView textViewTitleHours = view.findViewById(R.id.textViewStoreHours);
        TextView textViewTitleSundayThursday = view.findViewById(R.id.textViewSundayThursday);
        TextView textViewTitleFridayHoliday = view.findViewById(R.id.textViewFriday);
        mTextViewSundayThursdayHours = view.findViewById(R.id.textViewTime1);
        mTextViewFridayAndHolidayHours = view.findViewById(R.id.textViewTime2);
        Typeface fontComic = Typeface.createFromAsset(getActivity().getAssets(), "comic.ttf");

        String text = "<font color=#000000><u>Store Hours</u></font>"; // this for under line

        textViewTitleHours.setText(Html.fromHtml(text)); // set the text with the underLine

        // set fonts
        mTextViewSundayThursdayHours.setTypeface(fontComic);
        mTextViewFridayAndHolidayHours.setTypeface(fontComic);
        textViewTitleHours.setTypeface(fontComic);
        textViewTitleSundayThursday.setTypeface(fontComic);
        textViewTitleFridayHoliday.setTypeface(fontComic);

        // add images of the dash board to arrayList
        final Random random = new Random();
        final ArrayList<Integer> imagesForDashBoard = new ArrayList<>();
        imagesForDashBoard.add(R.drawable.barbershop_1);
        imagesForDashBoard.add(R.drawable.barbershop_2);
        imagesForDashBoard.add(R.drawable.barbershop_3);
        imagesForDashBoard.add(R.drawable.women_haircut);
        imagesForDashBoard.add(R.drawable.man_haircut);
        imagesForDashBoard.add(R.drawable.man_haircut2);
        final int sizeOfArrayListImages = imagesForDashBoard.size(); // calc how much images exist

        final ImageView imageViewBanner = view.findViewById(R.id.imageViewBanner);

        // create a thread that swap between the images
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                int randomNum = random.nextInt(sizeOfArrayListImages);
                imageViewBanner.setImageResource(imagesForDashBoard.get(randomNum));
                handler.postDelayed(this, 2000); // 2 sec delay between image
            }
        }, 2000);


        // check if admin is the user that log in
        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserId = currentFireBaseUser.getUid();

        // read from fireBase the store hours
        DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference("StoreHours");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot time: dataSnapshot.getChildren()){
                    if(time.getValue() != null){
                        if(Objects.equals(time.getKey(), "1"))
                            mTextViewSundayThursdayHours.setText(time.getValue().toString());
                        else if(Objects.equals(time.getKey(), "2"))
                            mTextViewFridayAndHolidayHours.setText(time.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // onclick on options
        orderEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.LoadFragment(new DayListFragment(),"DayList");
            }
        });

        // onclick on options
        adminArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentUserId.equals("iAzYzzTaMBgxkyYAxqLIgcPTBaG2")) {

                    Toast.makeText(getActivity(), "Unauthorized - Only Admins",   // this is
                            // simple user. can't go to admin area
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.LoadFragment(new AdminFragment(),"AdminScreen");
            }
        });

        // onclick on options
        myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.LoadFragment(new MyComingEventsFragment(),"MyComingEvents");
            }
        });

        // onclick on options
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.showDialogLogOut();
            }
        });

        // onclick on options
        priceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.LoadFragment(new PriceListFragment(),"PriceList");
            }
        });

        // onclick on options
        myDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainAppActivity mainAppActivity = (MainAppActivity) getActivity();
                mainAppActivity.LoadFragment(new MyDetailsFragment(),"MyDetails");
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDashBoardFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDashBoardFragmentInteractionListener) {
            mListener = (OnDashBoardFragmentInteractionListener) context;
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

    public interface OnDashBoardFragmentInteractionListener {
        void onDashBoardFragmentInteraction(Uri uri);
    }
}
