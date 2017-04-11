package com.bzahov.elsys.godofrowing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
import com.bzahov.elsys.godofrowing.Fragments.AlertUserLoggedFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainControllerFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainGforceGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainLinAccGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainLinAccGraphFragment.LinAccelFrgCommunicationChannel;
import com.bzahov.elsys.godofrowing.Fragments.MainMapFragment;
import com.bzahov.elsys.godofrowing.Models.MyLocation;
import com.bzahov.elsys.godofrowing.Models.ResourcesFromActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity implements MainMapFragment.MapFrgCommunicationChannel, MainGraphFragment.GraphFrgCommunicationChannel,LinAccelFrgCommunicationChannel {

        private static final int WITHOUT_FRAGMENT = 0;
        private static final int MAP_FRAGMENT = 1;
        private static final int GRAPH_FRAGMENT = 2;
        private static final int GFORCE_FRAGMENT = 3;
        private static final int LINACC_FRAGMENT = 4;

        private static final String TAG = "MainActivity";

        private static final int REQUEST_LOGIN_INTENT = 1;

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

        private TextView paramMeter;
        private TextView speedView;
        private TextView aveSpeedView;
        private TextView stopFrg;
        private TextView startFrg;
        private TextView meterView;
        private LinearLayout activityControler;
        private FrameLayout detailsLayot;
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
    private int currentStrokeRateInt;
    private int strokeRateCount;
    private float currentStrokeRateSum;
    private ArrayList<Location> lastPausedLocations;
    private long pauseMeters;
    private ArrayList<ArrayList<Location>> allPausedLocations = new ArrayList<>();
    private TextView aveSPMView;
    private ArrayList<MyLocation> allMyLocations = new ArrayList<>();
    private int saveLocCounter = 0;
    protected boolean showSettingsAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showSettingsAlert = true;
        setViews();
        setDefautValues();
        setFragments();
        checkUserAuth();
    }

    private void setFragments() {
        graphFragment = new MainGraphFragment();
        gForceGraphFragment = new MainGforceGraphFragment();
        lAccelGraphFragment = new MainLinAccGraphFragment();
        mapFragment = new MainMapFragment();

        showFragment(mapFragment);
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
                    showSettingsAlert();
                } else {
                    // User is signed out

                   // showSettingsAlert = false;
                    Intent logInActInt = new Intent(MainActivity.this, LogInActivity.class);
                    startActivityForResult(logInActInt,REQUEST_LOGIN_INTENT);

                    Log.d(TAG, "onAuthStateChanged:signed_out: ");
                }
            }
        };
    }

    private void setViews() {
        speedView = (TextView) findViewById(R.id.main_table_Param_speed);
        aveSpeedView = (TextView) findViewById(R.id.main_table_Ave_Speed);
        RelativeLayout meterLayout = (RelativeLayout) findViewById(R.id.main_table_layout_meters);
        meterView = (TextView) meterLayout.findViewById(R.id.main_table_param_meters);
        //paramMeter = (TextView) findViewById(R.id.main_table_param_meters);
        chronometer = (Chronometer) findViewById(R.id.main_table_chronometer);
        activityControler = (LinearLayout) findViewById(R.id.mapController);
        detailsLayot = (FrameLayout) findViewById(R.id.details);
        startFrg = (TextView) findViewById(R.id.mapControllerStart);
        stopFrg = (TextView) findViewById(R.id.mapControllerStop);
        aveSPMView = ((TextView)findViewById(R.id.main_table_ave_SPM));
        chronometer.setBase(SystemClock.elapsedRealtime());

        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.main_HScrow_View);
        LayoutInflater li = LayoutInflater.from(getBaseContext());
        View v = li.inflate(R.layout.context_main_paramether, null, false);
        scrollView.addView(v);
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

    @Override
    public void setGraphCommunication(String speed) {
        TextView textView = (TextView) findViewById(R.id.main_table_Param_speed_mpersec);
        textView.setText(speed);
        Log.d(TAG,"set speed from graph Fragment" + speed);
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
            startFrg.setBackgroundColor(Color.GREEN);

            stopTime = System.currentTimeMillis();
            elapsedTime += stopTime - startTime;
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();

            pauseMeters = 0;
            isStarted =false;

        }else { //resume
            startFrg.setText("Pause");
            startFrg.setBackgroundColor(Color.rgb(255,69,0)); // Orange
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
            startTime = System.currentTimeMillis();
            //chronometer.setBase(System.currentTimeMillis());
            totalMeters = totalMeters-pauseMeters;
            isStarted = true;
            allPausedLocations.add(lastPausedLocations);
            lastPausedLocations = new ArrayList<>();

        }
    }

    public void StopActivity(View view) {
        //if (isStarted) {
            isStarted = false;
            stopTime = System.nanoTime();
            stopFrg.setVisibility(View.GONE);

            elapsedTimeStr = chronometer.getText().toString();

            SendDataToDatabase();

            setDefautValues();

            // TODO: showAnalisys()
            showAnalisys();
       // }
    }

    private void setDefautValues() {

        allLocations = new ArrayList<>();
        allSpeeds = new ArrayList<>();
        allStrokes = new ArrayList<>();
        lastPausedLocations = new ArrayList<>();

        isStarted = false;
        isFirst = true;
        currentStrokeRateSum = 0;

        database = FirebaseDatabase.getInstance();

        averageSpeed = 0;
        currentSpeed = 0;
        totalMeters = 0;
        /*allLocations.clear();
        allSpeeds.clear();
        allStrokes.clear();*/
        currentStrokeRate = 0;
        averageStrokeRate = 0;
        strokeRateCount = 0;
        numStrokes = 0;
        newStroke = 0;
        maxSpeed = 0;
        lastStroke = 0;
        elapsedTime = 0;
        isFirst = true;
        chronometer.stop();
        chronometer.setBase(System.currentTimeMillis());
        pauseMeters = 0;
        meterView.setText(Long.toString(totalMeters));
        aveSpeedView.setText(Long.toString(totalMeters) + "\nave meters");
        speedView.setText(Long.toString(totalMeters) + "\n m/s");

        startFrg.setText("Start Activity");
    }

    private void SendDataToDatabase() {
        DatabaseReference myRef = database.getReference();

        // ResourcesFromActivity rfa = new ResourcesFromActivity( allStrokes,
        //        allSpeeds,allLocations, totalMeters,averageStrokeRate,maxSpeed,averageSpeed);

        //elapsedTime = startTime - chronometer.getBase();

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date()).replaceAll("[,. ]", "");
        ;
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd-HH:mm:ss");
        DateFormat dateFormatWrap = new SimpleDateFormat("yyMMddHHmmss");
        String postTime = dateFormatWrap.format(new Date());
        String postTimePresentation = dateFormatWrap.format(new Date());

        ResourcesFromActivity rfa = new ResourcesFromActivity(allMyLocations,totalMeters,averageStrokeRate,maxSpeed,averageSpeed,elapsedTimeStr, postTime);
        DatabaseReference debugDataRef = database.getReference("message");//+ currentDateTimeString);
        debugDataRef.setValue(rfa);
        DatabaseReference dataRef = database.getReference().child("users/"+ mUser.getUid() + "/activities/" + currentDateTimeString);
        dataRef.setValue(rfa);
    }

    /*private void writeNew(String userId, String username, String title, String body) {
        User key = database.getReference().child("users").child(mUser.getUid()).push().;
        User user = new User(, username, title, body);
        Map<String, Object> postValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/message/" + key, postValues);
        childUpdates.put("/user/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }*/

    private void showAnalisys() {
        Intent resultIntent = new Intent(this.getBaseContext(), ResultActivity.class);
        showSettingsAlert = false;
        //showSettingsAlert=false;
        setDefautValues();
        startActivity(resultIntent);
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
        //TextView meterView = (TextView) findViewById(R.id.main_table_param_meters);
        TextView meterperSec = (TextView) findViewById(R.id.Row3_C_1);
        Location locationBefore;

        if (isStarted) {
            //int size = allLocations.size() - 1;
            allLocations.add(newLocation);
            if(newLocation.hasSpeed()) {
                currentSpeed = newLocation.getSpeed();
                allSpeeds.add(currentSpeed);
               // MyLocation a = new MyLocation(newLocation);//,elapsedTimeStr);/*maxSpeed,averageSpeed);/*,currentStrokeRate,averageStrokeRate,totalMeters);
                MyLocation a = new MyLocation(newLocation);/**,elapsedTimeStr,maxSpeed,averageSpeed,currentStrokeRate,averageStrokeRate,totalMeters);
                */
                saveLocCounter++;
                if ( saveLocCounter <= 2 ){
                    allMyLocations.add(a);
                }else {
                    int mylocSize = allMyLocations.size()-1;
                    allMyLocations.get(mylocSize);
                    if ( (saveLocCounter % 5 == 0 ) || (allMyLocations.get(mylocSize-1).distanceTo(allMyLocations.get(mylocSize)) >= 5)){
                        allMyLocations.add(a);
                    }
                }
                // allMySpeeds.add()
                calculateAverageSpeed();
                int speedCurrentToSec = calcSecondsPer500meters(currentSpeed);
                int speedAverageToSec = calcSecondsPer500meters(averageSpeed);
                int[] speedCurentSplitted = splitToComponentTimes(speedCurrentToSec);
                int[] speedAverageSplitted = splitToComponentTimes(speedAverageToSec);

                speedView.setText(speedCurentSplitted[1] + ":" + speedCurentSplitted[2] + "\nper 500m");
                aveSpeedView.setText(speedAverageSplitted[1] + ":" + speedAverageSplitted[2] + "\nave per 500m");
                meterperSec.setText(Float.toString(roundFloat(currentSpeed,1))+ "\n m per sec");
            }
            int size = allLocations.size()-1;
            if (size > 1) {
                locationBefore = allLocations.get(size-1);
                float newDistance = locationBefore.distanceTo(newLocation);
                if (newDistance < 100f){
                    totalMeters += newDistance;
                    meterView.setText(Long.toString(totalMeters));
                }
            }
        }else { // pause
            /*lastPausedLocations.add(newLocation);
            int size = lastPausedLocations.size()-1;
            if (size == 0){
                pauseMeters = 0;
                return;
            }
            if (size > 1) {
                locationBefore = lastPausedLocations.get(size-1);
                float newDistance = locationBefore.distanceTo(newLocation);
                if (newDistance < 100f){
                    pauseMeters += newDistance;
                }
              }*/
        }
    }

    public void strokeCounterRate(View view) {
        countStrokeRate();
    }

    private void calculateAverageSpeed() {
        float newAverSpeed = 0f;

        for (Float curSpeed : allSpeeds) {
            newAverSpeed += curSpeed;
            if(curSpeed>maxSpeed){
                maxSpeed = curSpeed;
            }
        }
        int count = allSpeeds.size()-1;
        averageSpeed =  newAverSpeed /  count ;
    }

    private static int calcSecondsPer500meters(float speed) {

        double seconds = 0;

        if (speed > 0) {
            seconds = 500 / speed;

            if (seconds > 1000) {
                seconds = 0;
            }
        }
        return (int)seconds;
    }

    private static int calcSecondsPerGivenMeters(float speed, int meters) {

        double seconds = 0;

        if (speed > 0) {
            seconds = meters / speed;

            if (seconds > 1000) {
                seconds = 0;
            }
        }
        return (int)seconds;
    }

    private static int[] splitToComponentTimes(long input) {
        int hours = (int) input / 3600;
        int remainder = (int) input - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] result = {hours , mins , secs};
        return result;
    }

    private float calcAverageStrokeRate() {
        currentStrokeRateSum += currentStrokeRate;
        averageStrokeRate =  currentStrokeRateSum /  numStrokes;
        Log.e("STROKE",currentStrokeRate + " " + currentStrokeRateSum + " " + numStrokes);
        return averageStrokeRate ;
    }

    private static float roundFloat(float source, int positions) {
        long multiplier = (long) Math.pow(10, positions);
        return  ((float)((int) (source * multiplier)) / multiplier);
    }

    private void showFragment(final Fragment fragment){

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

    private void hideFragment(final Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();//.addToBackStack(null);

        if (fragment.isAdded()) {
            //if (!fragment.isHidden()) {
            fragmentTransaction.hide(fragment);
            //}
        }

        fragmentTransaction.commitNow();
    }

    private void removeFragment(Fragment fragment) {
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

    private void displayFragment( final int fragmentNumber) {
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        /*if (showSettingsAlert){
            showSettingsAlert();
        }
        showSettingsAlert = false;*/
    }

    private void showSettingsAlert() {
        if (showSettingsAlert) {
            if (mUser != null){
                DialogFragment alertUserLoggedFragment = AlertUserLoggedFragment.newInstance(mUser.getEmail());
              //  alertUserLoggedFragment();
                  alertUserLoggedFragment.show(getSupportFragmentManager(), "alertUserLoggedFragment");

            } else{
                Intent i = new Intent(MainActivity.this, LogInActivity.class);

                startActivityForResult(i,REQUEST_LOGIN_INTENT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_LOGIN_INTENT) {
            if(resultCode == Activity.RESULT_OK){
                showSettingsAlert = data.getBooleanExtra("returnFromLogIn",false);
                Toast.makeText(this,Boolean.toString(showSettingsAlert),Toast.LENGTH_LONG).show();
                Log.e("dialog",Boolean.toString(showSettingsAlert));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //showSettingsAlert = false;
            }
        }
    }

    @Override
    public void notifyForNewStroke() {
        countStrokeRate();
    }

    private void countStrokeRate() {
        TextView strokeView = (TextView) findViewById(R.id.main_table_param_StrokePerMinute);

        numStrokes++;

        newStroke = System.currentTimeMillis();

        float timeBetweenStrokes = (newStroke - lastStroke) / 1000.0f;

        Toast.makeText(getBaseContext(),timeBetweenStrokes + " ",Toast.LENGTH_SHORT).show();
        if (numStrokes == 0 || timeBetweenStrokes == 0 || timeBetweenStrokes >=60){
            strokeView.setText(0 + "\nSPM");
            currentStrokeRate = 0;

        }else {
            currentStrokeRate = 60.0f/timeBetweenStrokes;
            //currentStrokeRateInt = 60/(int)timeBetweenStrokes;
            //Toast.makeText(getBaseContext(),currentStrokeRate + " "+ currentStrokeRate,Toast.LENGTH_SHORT).show();
            strokeView.setText((int)currentStrokeRate + "\nSPM");
            //allStrokes.add(roundFloat(currentStrokeRate,2));
            if (isStarted) {
                calcAverageStrokeRate();
            }
        }
        String s = Float.toString(roundFloat(averageStrokeRate,1));
        aveSPMView.setText(s + "\nave/SPM");
        lastStroke = newStroke;
    }

    @Override
    public void sendNewLimAccel(float l) {
        strokeCounterRate(null);
    }
    public void updateMapFragmentMark(ResourcesFromActivity rfa) {
       /* if(mapFragment!=null){
            mapFragment.receiveDataFromMain("");
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}