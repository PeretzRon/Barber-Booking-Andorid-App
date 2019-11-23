package com.hit.haircutappointments.Fregments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.hit.haircutappointments.Adapters.PriceListAdapter;
import com.hit.haircutappointments.Classes.RecycleViewsClasses.PriceList;
import com.hit.haircutappointments.Classes.SortCompertor.PriceComparator;
import com.hit.haircutappointments.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class PriceListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public static PriceListAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView mRecyclerView;
    public static View.OnClickListener mMyOnClickListener;
    private ArrayList<PriceList> mPricesArrayList;
    private String mCurrnetUserThatLogin;
    private String mAdminUser;

    private OnPriceListFragmentInteractionListener mListener;

    public PriceListFragment() {
        // Required empty public constructor
    }

    public static PriceListFragment newInstance(String param1, String param2) {
        PriceListFragment fragment = new PriceListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Price List");
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_price_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!mCurrnetUserThatLogin.equals(mAdminUser)) {
            Toast.makeText(getActivity(), "Unauthorized - Only Admins",   // this is simple user. can't go to admin area
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        switch (item.getItemId()) {
            case R.id.add_new_price:
                showDialogToSaveActionAndPrice();
                return true;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).setTitle("Price List"); // set title for action bar
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_list, container, false);

        // add back button to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPricesArrayList = new ArrayList<>();
        mMyOnClickListener = new MyOnClickListener(getActivity());
        mRecyclerView = view.findViewById(R.id.my_recycler_view_5);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdminUser = "iAzYzzTaMBgxkyYAxqLIgcPTBaG2";
        mCurrnetUserThatLogin = "";

        // check if admin is the user that log in
        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mCurrnetUserThatLogin = currentFireBaseUser.getUid();

        DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference("Prices");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPricesArrayList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    mPricesArrayList.add(new PriceList(item.getKey(), (String)item.getValue()));
                }

                Collections.sort(mPricesArrayList, new PriceComparator()); // sort list
                mAdapter = new PriceListAdapter(mPricesArrayList); // create adapter
                mRecyclerView.setAdapter(mAdapter); // set recycle view
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }



    private void showDialogToSaveActionAndPrice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter new Action");
        builder.setIcon(R.drawable.ic_save_black_24dp);
        builder.setCancelable(true);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view, null);
        final EditText action = view.findViewById(R.id.editTextActionName);
        final EditText price = view.findViewById(R.id.editTextPrice);
        builder.setView(view);
        builder.setPositiveButton(
                "Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String actionName = action.getText().toString();
                        final String priceCost = price.getText().toString();
                        saveActionAndPriceInFireBase(actionName, priceCost);

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

    // saving this action and price in fireBase
    private void saveActionAndPriceInFireBase(final String iAction, final String iPrice){
        final DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference(
                        "Prices").child(iAction);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.setValue(iPrice);
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if(mCurrnetUserThatLogin.equals(mAdminUser)){
                int selectedItemPosition = mRecyclerView.getChildAdapterPosition(v);
                TextView actionName =
                        mRecyclerView.findViewHolderForAdapterPosition(
                                selectedItemPosition).itemView.findViewById(R.id.textViewActionHairCut);

                final String actionNameString = actionName.getText().toString();

                showDialogDeleteOrUpdateThisAction(actionNameString);
            }
        }
    }

    private void showDialogDeleteOrUpdateThisAction(final String iAction) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete/Update Action");
        builder.setIcon(R.drawable.ic_find_replace_black_24dp);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view, null);
        final EditText action = view.findViewById(R.id.editTextActionName);
        final EditText price = view.findViewById(R.id.editTextPrice);
        builder.setCancelable(true);
        builder.setView(view);
        builder.setPositiveButton( // preform update to exist action
                "Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String actionName = action.getText().toString();
                        final String priceCost = price.getText().toString();
                        deleteAction(iAction);
                        saveActionAndPriceInFireBase(actionName, priceCost); // save in fireBase

                    }
                });
        // delete this action
        builder.setNeutralButton("Delete Action", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAction(iAction);
            }
        });

        // cancel this operation
        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    private void deleteAction(final String iAction){
        final DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference("Prices").child(iAction);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.removeValue(); // remove this action from fireBase
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onPriceListButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPriceListFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPriceListFragmentInteractionListener) {
            mListener = (OnPriceListFragmentInteractionListener) context;
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

    public interface OnPriceListFragmentInteractionListener {
        void onPriceListFragmentInteraction(Uri uri);
    }
}
