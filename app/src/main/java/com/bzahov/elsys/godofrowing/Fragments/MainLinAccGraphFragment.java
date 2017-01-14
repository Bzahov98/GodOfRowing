package com.bzahov.elsys.godofrowing.Fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;

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

    @Override
    protected void setSensor() {
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mLinAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor accSensor = sensorEvent.sensor;

        if (accSensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            x_accelerometer = sensorEvent.values[0];
            //
            y_accelerometer = sensorEvent.values[1];
            //     yVals.add(new Entry(i, y_accelerometer));
            z_accelerometer = sensorEvent.values[2];
            //   zVals.add(new Entry(i, z_accelerometer));
        }
        if (accSensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            x_lAccel = sensorEvent.values[0];
            y_lAccel = sensorEvent.values[1];
            z_lAccel = sensorEvent.values[2];

            addEntry(lineGraphChart, x_lAccel, 0);
            addEntry(lineGraphChart, y_lAccel, 2);
            addEntry(lineGraphChart, z_lAccel, 1);
            lineGraphChart.notifyDataSetChanged();
            lineGraphChart.invalidate();
        }

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
