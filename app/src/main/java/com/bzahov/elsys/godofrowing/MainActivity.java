package com.bzahov.elsys.godofrowing;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.bzahov.elsys.godofrowing.Fragments.MainGraphFragment;
import com.bzahov.elsys.godofrowing.Fragments.MainMapFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private static final int WITHOUT_FRAGMENT = 0;
    private static final int MAP_FRAGMENT = 1;
    private static final int GRAPH_FRAGMENT = 2;

    private int choosedDetailOption;

    private FragmentManager fragmentManager;
    private Fragment mapFragment;
    private Fragment graphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView ParamMeter = (TextView) findViewById(R.id.param_Meters);
        ParamMeter.setText("0000\nmeters");
        ParamMeter.setTextSize(17);

        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.main_HScrow_View) ;
        LayoutInflater li = LayoutInflater.from(getBaseContext());

        View v = li.inflate(R.layout.context_main_paramether,null,false);
        scrollView.addView(v);

        //----------- fragment set--------------------------
        mapFragment = new MainMapFragment();
        graphFragment = new MainGraphFragment();

    }

    public void showFragment(final Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (fragment.isAdded()) {
            if (fragment.isHidden()) {
                fragmentTransaction.show(fragment);
            }
        }else { // fragment needs to be added to frame container
            fragmentTransaction.add(R.id.details, fragment, "A");
        }
        fragmentTransaction.commit();
    }

    public void hideFragment(final Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (fragment.isAdded()) {
            //if (!fragment.isHidden()) {
                fragmentTransaction.hide(fragment);
            //}
        }
        fragmentTransaction.commit();
    }

    protected void displayFragment( final int fragmentNumber) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // TODO: for more fragments -> put at array and modify method...
        switch (fragmentNumber){

            case 0:
                hideFragment(mapFragment);
                hideFragment(graphFragment);
                break;
            case 1: // MapFragment
                showFragment(mapFragment);
                hideFragment(graphFragment);
                break;
            case 2: // GraphFragment
                showFragment(graphFragment);
                hideFragment(mapFragment);
                break;
            default:
                Log.d("displayFrg()", fragmentNumber + " Doesn't exist");
                return;
        }
        ft.commit();
    }

    // ScrollView child on Click
    public void ScrollItem(View view) {

        switch (view.getId()) {

            case R.id.param_1:
                choosedDetailOption = 1;

                displayFragment(GRAPH_FRAGMENT);

                break;
            case R.id.param_2:

                // TODO: Google map is maybe SurfaceView, for now Can't hide that fragment, and show others !!!

                displayFragment(MAP_FRAGMENT);

                break;
            case R.id.param_3:

                displayFragment(GRAPH_FRAGMENT);

                choosedDetailOption = 3;

                break;
            case R.id.param_4:

                choosedDetailOption = 4;
                displayFragment(WITHOUT_FRAGMENT);

                break;
            case R.id.param_5:

                displayFragment(WITHOUT_FRAGMENT);
                choosedDetailOption = 5;

                break;
            case R.id.param_6:

                displayFragment(WITHOUT_FRAGMENT);

                choosedDetailOption = 6;

                break;
        }
    }


    public void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (fragment.isAdded()) {
            //if (!fragment.isHidden()) {
            fragmentTransaction.remove(fragment);

            mapFragment.onDestroy();
            //}
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (choosedDetailOption == 2){

            removeFragment(mapFragment);
        }
       super.onBackPressed();
    }

    }
