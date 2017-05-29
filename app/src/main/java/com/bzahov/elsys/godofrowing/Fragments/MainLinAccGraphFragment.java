package com.bzahov.elsys.godofrowing.Fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bobo-pc on 1/14/2017.
 */
public class MainLinAccGraphFragment extends BaseChartFragment implements SensorEventListener {

    private static final float CONST_FOR_POSSIBLE_WRONG = 0.1f;
    private static final int CONST_POSITIONS_FOR_INCREASE = 10;
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
    private float alpha = 0.7f;
    private float dt = 0;

    // Timestamps for the low-pass filter
    private float timestamp = System.nanoTime();
    private float timestampOld = System.nanoTime();
    private int count;
    private float currentAcceleration;
    private List<float[]> allAccelData = new ArrayList<>();
    private HashMap<Float, Float> allLinAccelData = new HashMap<>();
    private LinAccelFrgCommunicationChannel mCommChListner;


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

        //    mLinAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //    mSensorManager.registerListener(this, mLinAcceleration , SensorManager.SENSOR_DELAY_NORMAL);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_FASTEST);
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

            if (!(x_linear_acceleration == 0 && y_linear_acceleration == 0 && z_linear_acceleration == 0)){
                // does not have Sensor.TYPE_LINEAR_ACCELERATION

            currentAcceleration = (((float) Math.pow(x_linear_acceleration, 2)) + (float)Math.pow(y_linear_acceleration, 2)+ (float)Math.pow(z_linear_acceleration, 2));

            //addEntry(lineGraphChart, x_linear_acceleration, 0);
            //addEntry(lineGraphChart, y_linear_acceleration, 1);
            addEntry(lineGraphChart, currentAcceleration, 2);
            }
        }

        if (accSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, this.accelerationOnAxis, 0, sensorEvent.values.length);

            x_accelerometer = sensorEvent.values[0];
            y_accelerometer = sensorEvent.values[1];
            z_accelerometer = sensorEvent.values[2];
            //timestampOld = timestamp;
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

            gravityOnAxis = new float[]{x_gravity, y_gravity, z_gravity};
            linearAcceleration = new float[]{x_linear_acceleration, y_linear_acceleration, z_linear_acceleration};

            int length = linearAcceleration.length - 1;
            Log.d("LinearAccelOnAxes", "on x-" + x_linear_acceleration +
                    "on y-" + y_linear_acceleration +
                    "on z-" + z_linear_acceleration);

            allAccelData.add(linearAcceleration);
///     }
        }
        currentAcceleration = (float) (Math.pow(x_linear_acceleration, 2) + Math.pow(y_linear_acceleration, 2)+ Math.pow(z_linear_acceleration, 2));
        allLinAccelData.put(timestamp,currentAcceleration);

        //Toast.makeText(getContext(),Float.toString(currentAcceleration),Toast.LENGTH_SHORT).show();
        Log.d("LinearAccel","Alpha:"+ alpha + " "+"NormalAccel" + listToString(allAccelData) + "\nLinearAcc: " + allLinAccelData.get(allAccelData.size()-1)+ "\nCurrent Lin Accel "+Float.toString(currentAcceleration));

        findEachStroke();

        addEntry(lineGraphChart, x_linear_acceleration, 0);
        //addEntry(lineGraphChart, x_linear_acceleration, 0);
        //addEntry(lineGraphChart, y_linear_acceleration, 1);
        //addEntry(lineGraphChart, zurrentAcceleration, 2);

        lineGraphChart.notifyDataSetChanged();
        lineGraphChart.invalidate();

    }

    private void findEachStroke() {
        if (currentAcceleration <= (0 - CONST_FOR_POSSIBLE_WRONG)){
           // if (last10Increasing()){                    setnotifyForNewStroke();}
            someAlgorithm();
        }
    }

    private void someAlgorithm() {
        int size = allLinAccelData.size() - 1;

        for (Float time: allLinAccelData.keySet()) {
            allLinAccelData.get(0);

        }
    }
    private boolean last10Increasing() {
        int size = allLinAccelData.size()-1;
        int startPos = size - CONST_POSITIONS_FOR_INCREASE;
        int possibleNegatives = 0;
        float base = 0.0f;
        if (size<1) return false;
        else if (size< 10) return false;

        for (int i = startPos; i == size;++i){
            if (allLinAccelData.get(i) == startPos ){
                base = allLinAccelData.get(i);
            }else {
                if (allLinAccelData.get(i) < base){
                    possibleNegatives++;
                    if (possibleNegatives>4){ // possible wrong
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void setnotifyForNewStroke() {
        //communication with activity
        mCommChListner.notifyForNewStroke();
        Toast.makeText(getContext(),"New Stroke",Toast.LENGTH_SHORT).show();
    }

    private String listToString(List<float[]> input){
        int size = input.size()-1;
        String result = "X: " + Float.toString(x_accelerometer) +  "-" + Float.toString(x_gravity)+ " Y: "
                +Float.toString(input.get(size)[1]) + " Z:\n"
                + Float.toString(input.get(size)[2]);
        return result;
    }
    public void sendNewLinAccel() {
        //communication with activity
        mCommChListner.sendNewLimAccel(currentAcceleration);
        Toast.makeText(getContext(),"New Stroke",Toast.LENGTH_SHORT).show();
    }


    protected float[] LowPassFilter(float[] input, float[] output, float alpha) {
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
    public interface LinAccelFrgCommunicationChannel {
        void notifyForNewStroke();

        void sendNewLimAccel(float l);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LinAccelFrgCommunicationChannel) {
            mCommChListner = (MainLinAccGraphFragment.LinAccelFrgCommunicationChannel) context;
        } else {
//            throw new ClassCastException();
        }
    }

}
