package com.bzahov.elsys.godofrowing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.bzahov.elsys.godofrowing.Fragments.MainGforceGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainLinAccGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainMapFragment;

public class MainActivity extends FragmentActivity implements MainMapFragment.MapFrgCommunicationChannel, MainGraphFragment.GraphFrgCommunicationChannel {

    private static final int WITHOUT_FRAGMENT = 0;
    private static final int MAP_FRAGMENT = 1;
    private static final int GRAPH_FRAGMENT = 2;
    private static final int GFORCE_FRAGMENT = 3;
    private static final int LINACC_FRAGMENT = 4;

    private static final String TAG = MainMapFragment.class.getSimpleName();

    private int choosedDetailOption;

    private FragmentManager fragmentManager;
    private Fragment mapFragment;
    private Fragment graphFragment;
    private MainGforceGraphFragment gForceGraphFragment;
    private MainLinAccGraphFragment lAccelGraphFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView paramMeter = (TextView) findViewById(R.id.param_Meters);

        paramMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TestActivity.class);
                startActivity(intent);
            }
        });
        paramMeter.setText("0000\nmeters");
        paramMeter.setTextSize(17);


        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.main_HScrow_View) ;
        LayoutInflater li = LayoutInflater.from(getBaseContext());

        View v = li.inflate(R.layout.context_main_paramether,null,false);
        scrollView.addView(v);

        //----------- fragment set--------------------------

        graphFragment = new MainGraphFragment();
        gForceGraphFragment = new MainGforceGraphFragment();
        lAccelGraphFragment = new MainLinAccGraphFragment();
        mapFragment = new MainMapFragment();
    }

    public void showFragment(final Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//.addToBackStack(null);

        if (fragment.isAdded()) {
            if (fragment.isHidden()) {
                fragmentTransaction.show(fragment);
            }
        }else { // fragment needs to be added to frame container
            fragmentTransaction.add(R.id.details, fragment, fragment.getTag());
        }
        fragmentTransaction.commitNow();
    }

    public void hideFragment(final Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//.addToBackStack(null);

        if (fragment.isAdded()) {
            //if (!fragment.isHidden()) {
                fragmentTransaction.hide(fragment);
            //}
        }
        fragmentTransaction.commitNow();
    }

    protected void displayFragment( final int fragmentNumber) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // TODO: for more fragments -> put at array and modify method...
        switch (fragmentNumber){

            case 0:
                removeFragment(mapFragment);
                removeFragment(graphFragment);
                removeFragment(gForceGraphFragment);
                removeFragment(lAccelGraphFragment);
                break;
            case 1: // MapFragment
                hideFragment(graphFragment);
                hideFragment(gForceGraphFragment);
                hideFragment(lAccelGraphFragment);
                showFragment(mapFragment);
                break;
            case 2: // GraphFragment
                hideFragment(mapFragment);
                hideFragment(gForceGraphFragment);
                hideFragment(lAccelGraphFragment);
                showFragment(graphFragment);
                break;
            case 3: // GforceFragment
                hideFragment(graphFragment);
                hideFragment(mapFragment);
                hideFragment(lAccelGraphFragment);
                showFragment(gForceGraphFragment);
                break;
            case 4:
                hideFragment(mapFragment);
                hideFragment(graphFragment);
                hideFragment(gForceGraphFragment);
                showFragment(lAccelGraphFragment);
            default:
                Log.d(TAG, fragmentNumber + " Doesn't exist");
                return;
        }
        ft.commit();
    }

    // ScrollView child on Click
    public void ScrollItem(View view) {

        switch (view.getId()) {

            case R.id.param_1:
                choosedDetailOption = 1;
                /*if(mapFragment != null) {mapFragment.getView().setVisibility(View.INVISIBLE);}*/
                /*if(graphFragment != null)*/ {displayFragment(GRAPH_FRAGMENT);}
                break;
            case R.id.param_2:
                choosedDetailOption = 2;
                // TODO BUG: Google map is maybe SurfaceView, for now Can't hide that fragment, i can show graph fragment onto MapFragment
                // TODO: add fragment for start/stop
                displayFragment(MAP_FRAGMENT);
                break;
            case R.id.param_3:
                choosedDetailOption = 3;
                displayFragment(GFORCE_FRAGMENT);
                break;
            case R.id.param_4:
                choosedDetailOption = 4;
                displayFragment(LINACC_FRAGMENT);

                break;
            case R.id.param_5:
                displayFragment(WITHOUT_FRAGMENT);
                removeFragment(mapFragment);
                choosedDetailOption = 5;

                break;
            case R.id.param_6:

                displayFragment(WITHOUT_FRAGMENT);
              //  mapFragment.getChildFragmentManager().
                choosedDetailOption = 6;

                break;
        }
    }


    public void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Log.d(TAG,"removeFragment: " + fragment.getClass().getSimpleName());
        if (fragment.isAdded()) {
            //if (!fragment.isHidden()) {
            fragmentTransaction.remove(fragment);

            fragment.onDestroy();
            //}
        }
        fragmentTransaction.commitNow();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed");
        if (choosedDetailOption == 2){

            //removeFragment(mapFragment);
        }
       super.onBackPressed();
    }

    @Override
    public void setMapCommunication(String speed) {
        TextView textView = (TextView) findViewById(R.id.main_table_speed_gps);
        textView.setText(speed);
        Log.d(TAG,"set speed from map Fragment");
    }

    @Override
    public void setGraphCommunication(String speed) {
        TextView textView = (TextView) findViewById(R.id.main_table_speed_accel);
        textView.setText(speed);
        Log.d(TAG,"set speed from graph Fragment");
    }
}
