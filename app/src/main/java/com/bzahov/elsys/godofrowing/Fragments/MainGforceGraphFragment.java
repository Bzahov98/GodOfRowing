package com.bzahov.elsys.godofrowing.Fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;

/**
 * Created by bobo-pc on 1/14/2017.
 */
public class MainGforceGraphFragment extends AbstractChartFragment implements SensorEventListener{

    private int accelerometerAccuracy;
    private float x_gForce;
    private float y_gForce;
    private float z_gForce;

    @Override
    protected void setSensor() {
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor accSensor = sensorEvent.sensor;

        if (accSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x_accelerometer = sensorEvent.values[0];
            x_gForce = x_accelerometer / 9.81f;
            //
            y_accelerometer = sensorEvent.values[1];
            y_gForce = y_accelerometer / 9.81f;
            //     yVals.add(new Entry(i, y_accelerometer));
            z_accelerometer = sensorEvent.values[2];
            z_gForce = z_accelerometer / 9.81f;
            //   zVals.add(new Entry(i, z_accelerometer));

            //the pythagorean theorem to the different components of the acceleration:
            float gForce_total = (float)(Math.sqrt(x_gForce * x_gForce + y_gForce * y_gForce + z_gForce * z_gForce));
            addEntry(lineGraphChart, gForce_total, 0);
            //addEntr y(lineGraphChart, y_gForce, 1);
            //addEntry(lineGraphChart, z_gForce, 2);
            lineGraphChart.notifyDataSetChanged();
            lineGraphChart.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerometerAccuracy = i;
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
