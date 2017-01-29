package com.bzahov.elsys.godofrowing;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.Fragments.MainControllerFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainGforceGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainLinAccGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainMapFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

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
    private List<Location> allLocations = new ArrayList<>();
    private long totalMeters;
    private boolean isStarted;
    private List<Float> allSpeeds = new ArrayList<>();
    private MainControllerFragment controllerFragment;
    private LinearLayout activityControler;
    private FrameLayout detLayot;
    private float averageSpeed;
    private float currentSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView paramMeter = (TextView) findViewById(R.id.main_table_param_meters);

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
        isStarted = false;
        graphFragment = new MainGraphFragment();
        gForceGraphFragment = new MainGforceGraphFragment();
        lAccelGraphFragment = new MainLinAccGraphFragment();
        mapFragment = new MainMapFragment();
       // controllerFragment = new MainControllerFragment();
        //controllerFragment.
        activityControler = (LinearLayout) findViewById(R.id.mapController);
        detLayot = (FrameLayout) findViewById(R.id.details);
        //activityControler.getRootView().bringToFront();
        //detLayot.invalidate();

    }

    public void showFragment(final Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//.addToBackStack(null);

        if (fragment.isAdded()) {
            if (fragment.isHidden()) {
                fragmentTransaction.show(fragment);
            }
        }else { // fragment needs to be added to frame container
            fragmentTransaction.add(R.id.details, fragment, fragment.getTag());
            /*if (!controllerFragment.isAdded()){
                //controllerFragment.getView().
                fragmentTransaction.add(R.id.details, controllerFragment, fragment.getTag());
            }*/
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
        if  (isStarted){
            TextView textView = (TextView) findViewById(R.id.main_table_Param_speed);
            textView.setText(speed);
            Log.d(TAG,"set speed from map Fragment");
        }
    }

    public static float round(float source, int positions) {
        long multiplier = (long) Math.pow(10, positions);
        return  ((float)((int) (source * multiplier)) / multiplier);
    }

    @Override
    public void sendNewLocation(Location newLocation) {
        TextView speedView = (TextView) findViewById(R.id.main_table_Param_speed);
        TextView aveSpeedView = (TextView) findViewById(R.id.main_table_Ave_Speed);
        TextView meterView = (TextView) findViewById(R.id.main_table_param_meters);
        if (isStarted) {
            //int size = allLocations.size() - 1;
            allLocations.add(newLocation);
            if(newLocation.hasSpeed()) {
                currentSpeed = newLocation.getSpeed();
                allSpeeds.add(currentSpeed);
                calculateAverageSpeed();
                speedView.setText(round(currentSpeed,2) + "\n m/s");
                aveSpeedView.setText(round(averageSpeed,2) + "\nave m/s");
            }
            int size = allLocations.size()-1;
            if (size > 3) {
                Location locationBefore = allLocations.get(size-1);
                float newDistance = locationBefore.distanceTo(newLocation);
                if (newDistance < 100f){
                    totalMeters += newDistance;
                    meterView.setText(Long.toString(totalMeters) + "\n meters");
                }
            }
        }
    }

    private void calculateAverageSpeed() {
        float newAverSpeed = 0f;

        for (Float curSpeed : allSpeeds) {
            newAverSpeed += curSpeed;
        }
        int count = allSpeeds.size()-1;
        averageSpeed =  newAverSpeed /  count ;
    }

    @Override
    public void setGraphCommunication(String speed) {
        TextView textView = (TextView) findViewById(R.id.main_table_Param_speed_accel);
        textView.setText(speed);
        Log.d(TAG,"set speed from graph Fragment");
    }


    public void StopActivity(View view) {
        if (isStarted) {
            isStarted = false;
            TextView startFrg = (TextView) findViewById(R.id.mapControllerStart);
            TextView stopFrg = (TextView) findViewById(R.id.mapControllerStop);

            TextView speedView = (TextView) findViewById(R.id.main_table_Param_speed);
            TextView aveSpeedView = (TextView) findViewById(R.id.main_table_Ave_Speed);
            TextView meterView = (TextView) findViewById(R.id.main_table_param_meters);

            stopFrg.setVisibility(View.GONE);

            averageSpeed = 0;
            currentSpeed = 0;
            totalMeters = 0;
            allLocations.clear();
            allSpeeds.clear();

            meterView.setText(Long.toString(totalMeters) + "\n meters");
            aveSpeedView.setText(Long.toString(totalMeters) + "\nave meters");
            speedView.setText(Long.toString(totalMeters) + "\n m/s");

            startFrg.setText("Start Activity");

            // showAnalisys()
        }
    }

    public static int calcMilisecondsPer500m(float speed) {

        double seconds = 0;

        if (speed > 0) {
            seconds = 500 / speed;

            if (seconds > 1000) {
                seconds = 0;
            }
        }
        return (int)seconds * 1000;
    }

    public void StartPauseActivity(View view) { // OnClick of Controller at MapFragment
        TextView startFrg = (TextView) findViewById(R.id.mapControllerStart);
        TextView stopFrg = (TextView) findViewById(R.id.mapControllerStop);
        showFragment(mapFragment);
        stopFrg.setVisibility(View.VISIBLE);
        if (isStarted){ //pause
            startFrg.setText("Resume");
            isStarted =false;
        }else {
            isStarted = true;
            startFrg.setText("Pause");
        }
    }
}
