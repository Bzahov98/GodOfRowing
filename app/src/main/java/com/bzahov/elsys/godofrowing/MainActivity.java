package com.bzahov.elsys.godofrowing;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
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
import com.bzahov.elsys.godofrowing.Model.ResourcesFromActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private MainControllerFragment controllerFragment;
    private long timeWhenStopped = 0;
    private List<Location> allLocations = new ArrayList<>();
    private List<Float> allSpeeds = new ArrayList<>();
    private List<Float> allStrokes = new ArrayList<>();


    private boolean isStarted;
    private boolean isFirst;

    private LinearLayout activityControler;
    private FrameLayout detLayot;

    private Chronometer chronometer;

    private long startTime;
    private long stopTime;

    private float averageSpeed;
    private float currentSpeed;
    private long totalMeters;
    private long lastStroke;
    private long newStroke;
    private float averageStrokeRate;
    private float currentStrokeRate;
    private int numStrokes;
    private long elapsedTime;
    private FirebaseDatabase database;
    private float maxSpeed;
    private String elapsedTimeStr;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
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


        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.main_HScrow_View);
        LayoutInflater li = LayoutInflater.from(getBaseContext());

        View v = li.inflate(R.layout.context_main_paramether, null, false);
        scrollView.addView(v);

        //----------- fragment set--------------------------

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

        lastStroke = 0;
        newStroke = 0;
        isStarted = false;
        isFirst = true;
        elapsedTime = 0;
        maxSpeed = 0;

        database = FirebaseDatabase.getInstance();
        chronometer = (Chronometer) findViewById(R.id.main_table_chronometer);

        startActivity(new Intent(MainActivity.this, LogInActivity.class));

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUser = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Toast.makeText(getBaseContext(),user.getEmail(),Toast.LENGTH_LONG);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

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
    public void setMapCommunication(String speed) {
        if  (isStarted){
            TextView textView = (TextView) findViewById(R.id.main_table_Param_speed);
            textView.setText(speed);
            Log.d(TAG,"set speed from map Fragment");
        }
    }

    @Override
    public void sendNewLocation(Location newLocation) {
        TextView speedView = (TextView) findViewById(R.id.main_table_Param_speed);
        TextView aveSpeedView = (TextView) findViewById(R.id.main_table_Ave_Speed);
        TextView meterView = (TextView) findViewById(R.id.main_table_param_meters);
        TextView testMperSec = (TextView) findViewById(R.id.Row3_C_1);
        if (isStarted) {
            //int size = allLocations.size() - 1;
            allLocations.add(newLocation);
            if(newLocation.hasSpeed()) {
                currentSpeed = newLocation.getSpeed();
                allSpeeds.add(currentSpeed);
                calculateAverageSpeed();
                int speed = calcsecondsPer500m(currentSpeed);
                int[] result = splitToComponentTimes(speed);
                testMperSec.setText(round(currentSpeed,2) + "\nm/s\nTest");
                speedView.setText(/*result[0] + ":" + */result[1] + ":" + result[2] + "\nper 500m");
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

    @Override
    public void setGraphCommunication(String speed) {
        TextView textView = (TextView) findViewById(R.id.main_table_Param_speed_accel);
        textView.setText(speed);
        Log.d(TAG,"set speed from graph Fragment");
    }

    public void StopActivity(View view) {
        //if (isStarted) {
            isStarted = false;
            stopTime = System.nanoTime();

            TextView startFrg = (TextView) findViewById(R.id.mapControllerStart);
            TextView stopFrg = (TextView) findViewById(R.id.mapControllerStop);

            TextView speedView = (TextView) findViewById(R.id.main_table_Param_speed);
            TextView aveSpeedView = (TextView) findViewById(R.id.main_table_Ave_Speed);
            TextView meterView = (TextView) findViewById(R.id.main_table_param_meters);

            stopFrg.setVisibility(View.GONE);

            DatabaseReference myRef = database.getReference();

           // ResourcesFromActivity rfa = new ResourcesFromActivity( allStrokes,
            //        allSpeeds,allLocations, totalMeters,averageStrokeRate,maxSpeed,averageSpeed);

            //elapsedTime = startTime - chronometer.getBase();
            elapsedTimeStr = chronometer.getText().toString();
        ((TextView) findViewById(R.id.Row3_C_1)).setText(elapsedTimeStr);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date()).replaceAll("[,. ]", "");

        ResourcesFromActivity rfa = new ResourcesFromActivity(totalMeters,averageStrokeRate,maxSpeed,averageSpeed,elapsedTimeStr);
            DatabaseReference data = database.getReference().child("message");//+ currentDateTimeString);
            DatabaseReference data2 = database.getReference().child("users/"+ mUser.getUid() + "/activities/" + currentDateTimeString);
            data2.setValue(rfa);
      //  data.setValue("aa");
            //data.setValue(rfa);


            averageSpeed = 0;
            currentSpeed = 0;
            totalMeters = 0;
            allLocations.clear();
            allSpeeds.clear();
            allStrokes.clear();
            currentStrokeRate = 0;
            averageStrokeRate = 0;
            newStroke = 0;
            maxSpeed = 0;
            lastStroke = 0;
            elapsedTime = 0;
            isFirst = true;
            chronometer.stop();
            chronometer.setBase(System.currentTimeMillis());


            meterView.setText(Long.toString(totalMeters) + "\n meters");
            aveSpeedView.setText(Long.toString(totalMeters) + "\nave meters");
            speedView.setText(Long.toString(totalMeters) + "\n m/s");

            startFrg.setText("Start Activity");

            // TODO: showAnalisys()
            showAnalisys();
       // }
    }

    private void showAnalisys() {
        Intent resultIntent = new Intent(this.getBaseContext(), ResultActivity.class);
        // Write a message to the database
        //database = FirebaseDatabase.getInstance();
       /* DatabaseReference myRef = database.getReference("message");
        //DatabaseReference myRef2 = database.getReference("aaa");
       // myRef2.setValue(allLocations);
       // myRef.setValue(allLocations);
        myRef.setValue(totalMeters);
*/
        startActivity(resultIntent);
    }

    private void calculateAverageSpeed() {
        float newAverSpeed = 0f;

        for (Float curSpeed : allSpeeds) {
            newAverSpeed += curSpeed;
            if(curSpeed>maxSpeed){
                maxSpeed = curSpeed;
            }
        }
        int count = allSpeeds.size();
        averageSpeed =  newAverSpeed /  count ;
    }

    public static int calcsecondsPer500m(float speed) {

        double seconds = 0;

        if (speed > 0) {
            seconds = 500 / speed;

            if (seconds > 1000) {
                seconds = 0;
            }
        }
        return (int)seconds;
    }

    public static int[] splitToComponentTimes(long input) {
        int hours = (int) input / 3600;
        int remainder = (int) input - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] result = {hours , mins , secs};
        return result;
    }

    public void startPauseActivity(View view) { // OnClick of Controller at MapFragment
        TextView startFrg = (TextView) findViewById(R.id.mapControllerStart);
        TextView stopFrg = (TextView) findViewById(R.id.mapControllerStop);
        if  (isFirst) {

            //chronometer.setBase(SystemClock.elapsedRealtime());
            //chronometer.setBase(SystemClock.currentThreadTimeMillis());
            chronometer.start();
            startTime = SystemClock.currentThreadTimeMillis();
            showFragment(mapFragment);
            stopFrg.setVisibility(View.VISIBLE);
            startTime = System.currentTimeMillis();
            isFirst = false;
        }
        if (isStarted){ //pause
            startFrg.setText("Resume");
            isStarted =false;
            stopTime = System.currentTimeMillis();
            elapsedTime += stopTime - startTime;
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();

        }else { //resume
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
            startTime = System.currentTimeMillis();
            //chronometer.setBase(System.currentTimeMillis());
            isStarted = true;
            startFrg.setText("Pause");
        }
    }

    public void strokeCounterRate(View view) {
        TextView strokeView = (TextView) findViewById(R.id.main_table_param_StrokePerMinute);

        numStrokes++;

        newStroke = System.currentTimeMillis();

        float timeBetweenStrokes = (newStroke - lastStroke) / 1000.0f;
        if (numStrokes == 0 || timeBetweenStrokes == 0 || timeBetweenStrokes >=60){
                strokeView.setText(0 + "\nSPM");
                currentStrokeRate = 0;
        }else {
            currentStrokeRate = 60.0f/timeBetweenStrokes;
            strokeView.setText((int)currentStrokeRate + "\nSPM");
            allStrokes.add(round(currentStrokeRate,2));
            averageStrokeRate = calcAverageStrokeRate();
        }

        lastStroke = newStroke;
    }

    private float calcAverageStrokeRate() {
        long newAverRate = 0;

        for (Float curStroke : allStrokes) {
           newAverRate += curStroke;
        }
        int count = allStrokes.size();
        averageStrokeRate =  newAverRate /  count;
        return averageSpeed ;
    }

    public static float round(float source, int positions) {
        long multiplier = (long) Math.pow(10, positions);
        return  ((float)((int) (source * multiplier)) / multiplier);
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
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
