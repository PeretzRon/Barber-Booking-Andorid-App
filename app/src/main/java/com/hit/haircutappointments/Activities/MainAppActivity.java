package com.hit.haircutappointments.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.hit.haircutappointments.Fregments.AddRemoveDaysAdminFragment;
import com.hit.haircutappointments.Fregments.AdminFragment;
import com.hit.haircutappointments.Fregments.DashBoardFragment;
import com.hit.haircutappointments.Fregments.DayListFragment;
import com.hit.haircutappointments.Fregments.MyComingEventsFragment;
import com.hit.haircutappointments.Fregments.MyDetailsFragment;
import com.hit.haircutappointments.Fregments.PriceListFragment;
import com.hit.haircutappointments.Fregments.RegisteredUsersFragment;
import com.hit.haircutappointments.Fregments.SpecificDayFragment;
import com.hit.haircutappointments.Fregments.UpdateUserInfoFragment;
import com.hit.haircutappointments.R;


public class MainAppActivity extends AppCompatActivity implements
        UpdateUserInfoFragment.OnUpdateUserInfoFragmentInteractionListener,
        DayListFragment.OnDayListFragmentInteractionListener,
        DashBoardFragment.OnDashBoardFragmentInteractionListener,
        SpecificDayFragment.OnSpecificDayFragmentInteractionListener,
        AdminFragment.OnAdminFragmentInteractionListener,
        AddRemoveDaysAdminFragment.OnAddRemoveDaysFragmentInteractionListener,
        MyComingEventsFragment.OnMyComingEventsFragmentInteractionListener,
        RegisteredUsersFragment.OnRegisteredUsersFragmentInteractionListener,
        PriceListFragment.OnPriceListFragmentInteractionListener,
        MyDetailsFragment.OnMyDetailsFragmentInteractionListener {

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        Intent intent = getIntent();
        String ans = intent.getStringExtra("isExistInfo");

        /*
        if the user there is detail on fireBase then he will go direct to dashBoard screen
        but, if this is the first login / missing details he will need to insert them
         */
        if (ans.equals("Yes")) {
            // load dashBoard fragment
            mFragmentManager = getSupportFragmentManager();
            Fragment fragment = new DashBoardFragment();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.box2, fragment, "DashBoard").commit();
        } else {
            // load insert userDetails fragment
            mFragmentManager = getSupportFragmentManager();
            Fragment fragment = new UpdateUserInfoFragment();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.box2, fragment, "UpdateUser").commit();
        }

    }

    // load the DashBoard screen (without insert to stack, that the main screen (home screen))
    public void LoadDashBoardFragment() {
        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = new DashBoardFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter_from_right,
                R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
        fragmentTransaction.replace(R.id.box2, fragment,
                "DashBoard").commit();
    }

    // load other fragments
    public void LoadFragment(Fragment iFragmentToLoad, String iNameFragment){
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter_from_right,
                R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
        fragmentTransaction.replace(R.id.box2, iFragmentToLoad,
                iNameFragment).addToBackStack(null).commit();
    }

    public void LoadSpecificDayEventsFragment(String iYear, String iMount, String iDay) {
        Bundle bundle = new Bundle(); // to transfer data to other fragment
        bundle.putString("Year", iYear); // what to transfer
        bundle.putString("Mount", iMount);
        bundle.putString("Day", iDay);
        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = new SpecificDayFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter_from_right,
                R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
        fragmentTransaction.replace(R.id.box2, fragment,
                "SpecificDay").addToBackStack(null).commit();
    }

    // pop a dialog dis connect from user
    public void showDialogLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout?");
        builder.setIcon(R.drawable.ic_vpn_key_black_24dp);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(MainAppActivity.this, // start the activity
                                IdentificationActivity.class);
                        startActivity(i);
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

    @Override
    public void onBackPressed() {
        Fragment displayedFragment = mFragmentManager.findFragmentByTag("DashBoard");
        if (displayedFragment instanceof DashBoardFragment && displayedFragment.isVisible()) {
            showDialogLogOut();
            return;
        }

        displayedFragment = mFragmentManager.findFragmentByTag("DayList");
        if (displayedFragment instanceof DayListFragment && displayedFragment.isVisible()) {
            Fragment f;
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().getBackStackEntryCount();
            f = getSupportFragmentManager().findFragmentByTag("DashBoard");
            if (f != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.box2, f,
                        "DashBoard").commit();
            }

            return;
        }

        displayedFragment = mFragmentManager.findFragmentByTag("SpecificDay");
        if (displayedFragment instanceof SpecificDayFragment && displayedFragment.isVisible()) {
            Fragment f;
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().getBackStackEntryCount();
            f = getSupportFragmentManager().findFragmentByTag("DayList");
            if (f != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.box2, f,
                        "DayList").commit();
            }

            return;
        }

        displayedFragment = mFragmentManager.findFragmentByTag("AdminScreen");
        if (displayedFragment instanceof AdminFragment && displayedFragment.isVisible()) {
            Fragment f;
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().getBackStackEntryCount();
            f = getSupportFragmentManager().findFragmentByTag("DashBoard");
            if (f != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.box2, f,
                        "DashBoard").commit();
            }

            return;
        }

        displayedFragment = mFragmentManager.findFragmentByTag("AddDeleteDate");
        if (displayedFragment instanceof AddRemoveDaysAdminFragment && displayedFragment.isVisible()) {
            Fragment f;
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().getBackStackEntryCount();
            f = getSupportFragmentManager().findFragmentByTag("AdminScreen");
            if (f != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.box2, f,
                        "AdminScreen").commit();
            }

            return;
        }

        displayedFragment = mFragmentManager.findFragmentByTag("MyComingEvents");
        if (displayedFragment instanceof MyComingEventsFragment && displayedFragment.isVisible()) {
            Fragment f;
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().getBackStackEntryCount();
            f = getSupportFragmentManager().findFragmentByTag("DashBoard");
            if (f != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.box2, f,
                        "DashBoard").commit();
            }

            return;
        }

        displayedFragment = mFragmentManager.findFragmentByTag("RegisteredUsers");
        if (displayedFragment instanceof RegisteredUsersFragment && displayedFragment.isVisible()) {
            Fragment f;
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().getBackStackEntryCount();
            f = getSupportFragmentManager().findFragmentByTag("AdminScreen");
            if (f != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.box2, f,
                        "AdminScreen").commit();
            }

            return;
        }

        displayedFragment = mFragmentManager.findFragmentByTag("PriceList");
        if (displayedFragment instanceof PriceListFragment && displayedFragment.isVisible()) {
            Fragment f;
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().getBackStackEntryCount();
            f = getSupportFragmentManager().findFragmentByTag("DashBoard");
            if (f != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.box2, f,
                        "DashBoard").commit();
            }

            return;
        }

        displayedFragment = mFragmentManager.findFragmentByTag("MyDetails");
        if (displayedFragment instanceof MyDetailsFragment && displayedFragment.isVisible()) {
            Fragment f;
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().getBackStackEntryCount();
            f = getSupportFragmentManager().findFragmentByTag("DashBoard");
            if (f != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.box2, f,
                        "DashBoard").commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed(); // if the back press from the actionBar the preform backPress
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onUpdateUserInfoFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDayListFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDashBoardFragmentInteraction(Uri uri) {

    }


    @Override
    public void onSpecificDayFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAdminFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAddRemoveDaysFragmentInteraction(Uri uri) {

    }


    @Override
    public void onMyComingEventsFragmentInteraction(Uri uri) {

    }


    @Override
    public void onRegisteredUsersFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPriceListFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMyDetailsFragmentInteraction(Uri uri) {

    }


}
