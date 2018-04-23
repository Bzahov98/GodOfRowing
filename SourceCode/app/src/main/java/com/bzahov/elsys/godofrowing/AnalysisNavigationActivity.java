package com.bzahov.elsys.godofrowing;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
import com.bzahov.elsys.godofrowing.Fragments.AnalysisFragments.ResultContentAnalysisFragment;
import com.bzahov.elsys.godofrowing.Fragments.AnalysisFragments.ResultContentHistoryFragment;
import com.bzahov.elsys.godofrowing.Fragments.AnalysisFragments.ResultContentSplitterFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AnalysisNavigationActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN_INTENT = 2;
    private static final String GROUP_REFERENCE = "Groups";
    private static String TAG = AnalysisNavigationActivity.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    protected FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        checkUserAuth();
        /*fab = (FloatingActionButton) findViewById(R.id.res_analysis_fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

       public void fabOnClick(View view) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            Toast.makeText(getContext(),"bbbasdas",Toast.LENGTH_SHORT).show();

            return super.onContextItemSelected(item);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            Toast.makeText(getContext(),"asdas",Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_analysis_navigation, container, false);

            // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            //fab.setVisibility(View.GONE);
        }


        @Override
        public Fragment getItem(int position) {
           // Toast.makeText(getApplicationContext(),"asdas",Toast.LENGTH_SHORT).show();
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0){
               // fab.setVisibility(View.GONE);
                return new ResultContentAnalysisFragment();
            }
            if (position == 1){
                //fab.setVisibility(View.GONE);
                return new ResultContentSplitterFragment();
            }if (position == 2){
               /// fab.setVisibility(View.VISIBLE);
               // fab.setBackgroundDrawable(getDrawable(R.drawable.icon_timer));
                return new ResultContentHistoryFragment();
            }
           /* if (position == 2){
                fab.setVisibility(View.GONE);
                //return ChatFragment.newInstance(GROUP_REFERENCE,"Chat1");
            }else*/
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Last Training";
                case 1:
                    return "Last Month";
                case 2:
                    return "Training History";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_analysis_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.sign_out:
                signOut();
                break;
            case R.id.action_settings:
                settings();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void settings() {
        Toast.makeText(getApplicationContext(),"Comming soon",Toast.LENGTH_SHORT);
    }

    private void checkUserAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUser = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getEmail());
                    Toast.makeText(getBaseContext(),"Welcome " + user.getEmail(),Toast.LENGTH_SHORT).show();
                    //   showSettingsAlert();
                } else {
                    // User is signed out

                    // showSettingsAlert = false;
                    Intent logInActInt = new Intent(AnalysisNavigationActivity.this, LogInActivity.class);
                    startActivityForResult(logInActInt,REQUEST_LOGIN_INTENT);

                    Log.d(TAG, "onAuthStateChanged:signed_out: ");
                }
            }
        };
    }


    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(this, LogInActivity.class));
        finish();
    }
}
