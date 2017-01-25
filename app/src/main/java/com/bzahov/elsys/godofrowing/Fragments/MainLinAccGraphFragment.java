package com.bzahov.elsys.godofrowing.Fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import static java.security.AccessController.getContext;

/**
 * Created by bobo-pc on 1/14/2017.
 */
public class MainLinAccGraphFragment extends AbstractChartFragment implements SensorEventListener {

    private int accelerometerAccuracy;
    private int linearAccelAccuracy;
    private Sensor mLinAcceleration;

    private float[] accelerationOnAxis = new float[] {0,0,0} ;
    //gravities on axis
    private float[] gravityOnAxis;
    private float x_gravity;
    private float y_gravity;
    private float z_gravity;
    //Linear accelerations
    private float[] linearAcceleration;
    private float x_linear_acceleration;
    private float y_linear_acceleration;
    private float z_linear_acceleration;
    private Date lastUpdate;

    // LPFilter
    private float timeConstant = 0.18f;
    private float alpha = 0.9f;
    private float dt = 0;

    // Timestamps for the low-pass filter
    private float timestamp = System.nanoTime();
    private float timestampOld = System.nanoTime();
    private int count;
    private float currentAcceleration;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, parent, savedInstanceState);
        // lineGraphChart.getLineData().getDataSetByIndex(3).setVisible(true);
        lastUpdate = null;
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

    //    deltaT = mAccelerometer.getD;
        if (accSensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            Log.d("LinAccel","has ");
            x_linear_acceleration  = sensorEvent.values[0];
            x_linear_acceleration  = sensorEvent.values[1];
            z_linear_acceleration  = sensorEvent.values[2];

            if (x_linear_acceleration == 0 && y_linear_acceleration == 0 && z_linear_acceleration == 0){
                return; // does not have Sensor.TYPE_LINEAR_ACCELERATION
            }
            addEntry(lineGraphChart, x_linear_acceleration, 0);
            addEntry(lineGraphChart, y_linear_acceleration, 1);
            addEntry(lineGraphChart, z_linear_acceleration, 2);
        }

        if (accSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, this.accelerationOnAxis, 0, sensorEvent.values.length);

            x_accelerometer = sensorEvent.values[0];
            y_accelerometer = sensorEvent.values[1];
            z_accelerometer = sensorEvent.values[2];

            timestamp = System.nanoTime();

            float deltaTime = timestamp - timestampOld;

            dt = 1 / (count / ((deltaTime) / 1000000000.0f));

            count++;

            alpha = timeConstant / (timeConstant + dt);
              // long tDelta = currentUpdate.getTime() - lastUpdate.getTime();
               // long t = 1 / (2 * Math.PI * fc);
                //alpha = t / (t + tDelta);

            x_gravity = alpha * x_gravity + (1 - alpha) * x_accelerometer;
            y_gravity = alpha * y_gravity + (1 - alpha) * y_accelerometer;
            z_gravity = alpha * z_gravity + (1 - alpha) * z_accelerometer;

            x_linear_acceleration = x_accelerometer - x_gravity;
            y_linear_acceleration = y_accelerometer - y_gravity;
            z_linear_acceleration = z_accelerometer - z_gravity;

            gravityOnAxis      = new float[] { x_gravity , y_gravity , z_gravity };
            linearAcceleration = new float[] { x_linear_acceleration , y_linear_acceleration , z_linear_acceleration };
        }

           currentAcceleration = (((float) Math.pow(x_linear_acceleration, 2)) + (float)Math.pow(y_linear_acceleration, 2)+ (float)Math.pow(z_linear_acceleration, 2));

            addEntry(lineGraphChart, x_linear_acceleration, 0);
            addEntry(lineGraphChart, y_linear_acceleration, 1);
            addEntry(lineGraphChart, z_linear_acceleration, 2);
            addEntry(lineGraphChart, currentAcceleration, 3);

        lineGraphChart.notifyDataSetChanged();
        lineGraphChart.invalidate();

    }
    protected float[] filter(float[] input, float[] output, float alpha) {
        if (output == null) return input;
        if (Float.isNaN(alpha)){
            alpha = 0.8f; //low pass
        }
        for (int i=0; i<input.length; i++) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
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
