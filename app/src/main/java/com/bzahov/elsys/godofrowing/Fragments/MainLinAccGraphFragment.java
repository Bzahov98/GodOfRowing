package com.bzahov.elsys.godofrowing.Fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import static java.security.AccessController.getContext;

/**
 * Created by bobo-pc on 1/14/2017.
 */
public class MainLinAccGraphFragment extends AbstractChartFragment implements SensorEventListener {

    private int accelerometerAccuracy;
    private int linearAccelAccuracy;
    private Sensor mLinAcceleration;
    private float x_lAccel;
    private float y_lAccel;
    private float z_lAccel;
    private double[] gravity;
    private double[] linear_acceleration;
    private float x_gravity;
    private float y_gravity;
    private float z_gravity;
    private float x_linear_acceleration;
    private float y_linear_acceleration;
    private float z_linear_acceleration;
    private Date lastUpdate;
    private boolean hasLinAccel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, parent, savedInstanceState);
        lastUpdate = new Date(System.currentTimeMillis());
        return view;
    }

    @Override
    protected void setSensor() {
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

            mLinAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mSensorManager.registerListener(this, mLinAcceleration , SensorManager.SENSOR_DELAY_NORMAL);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor accSensor = sensorEvent.sensor;

        if (accSensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            Log.d("LinAccel","has ");
            x_linear_acceleration  = sensorEvent.values[0];
            x_linear_acceleration  = sensorEvent.values[1];
            z_linear_acceleration  = sensorEvent.values[2];

            if (x_linear_acceleration == 0 && y_linear_acceleration == 0 && z_linear_acceleration == 0){
                return;
            }
        }

        if (accSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x_accelerometer = sensorEvent.values[0];
            y_accelerometer = sensorEvent.values[1];
            z_accelerometer = sensorEvent.values[2];

            final float alpha = 0.8f; // TODO: t / (t + dT)'

            x_gravity = alpha * x_gravity + (1 - alpha) * x_accelerometer;
            y_gravity = alpha * y_gravity + (1 - alpha) * y_accelerometer;
            z_gravity = alpha * z_gravity + (1 - alpha) * z_accelerometer;

            x_linear_acceleration = x_accelerometer - x_gravity;
            y_linear_acceleration = y_accelerometer - y_gravity;
            z_linear_acceleration = z_accelerometer - z_gravity;
        }
            addEntry(lineGraphChart, x_linear_acceleration, 0);
            addEntry(lineGraphChart, y_linear_acceleration, 1);
            addEntry(lineGraphChart, z_linear_acceleration, 2);
            lineGraphChart.notifyDataSetChanged();
            lineGraphChart.invalidate();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerometerAccuracy = i;
        }
        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            linearAccelAccuracy = i;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {

        super.onPause();

        mSensorManager.unregisterListener(this);

    }

}
