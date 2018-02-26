package com.bzahov.elsys.godofrowing.Fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bobo-pc on 1/14/2017.
 */
public class MainLinAccGraphFragment extends BaseChartFragment implements SensorEventListener {

    private static final float CONST_FOR_POSSIBLE_WRONG = 0.1f;
    private static final int CONST_POSITIONS_FOR_INCREASE = 10;

    private static final float STATE_FOR_LOW_ACCEL_DATA = -1.1f;
    private static final float STATE_FOR_HIGH_ACCEL_DATA = 1.5f; //HERE

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
    private float currentLinAcceleration;
    private List<float[]> allAccelData = new ArrayList<>();
    private List<Float> allLinAccelData = new ArrayList<>();
    private LinAccelFrgCommunicationChannel mCommChListner;
    private int currentState;  // 0 - neutral , 1 -
    private boolean sendNotify;

    private int i;
    private boolean withTestData;
    private float currentNormAcceleration;
    private int j;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        withTestData = true; // TRUE for Test data FALSE for data from sensors
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, parent, savedInstanceState);
        // lineGraphChart.getLineData().getDataSetByIndex(3).setVisible(true);
        lastUpdate = null;
        i = j = 0;
        withTestData = true; // true for test data false for real accelerometer data
        sendNotify = true;
        return view;
    }

    @Override
    protected void setSensor() {
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        //    mLinAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //    mSensorManager.registerListener(this, mLinAcceleration , SensorManager.SENSOR_DELAY_NORMAL);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (withTestData) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL); //HERE
        }else {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor accSensor = sensorEvent.sensor;

        //    deltaT = mAccelerometer.getD;
        /*if (accSensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) { // I haven't linear accel sensor not tested!!!!!
            Log.d("LinAccel","has ");
            x_linear_acceleration  = sensorEvent.values[0];
            x_linear_acceleration  = sensorEvent.values[1];
            z_linear_acceleration  = sensorEvent.values[2];

            if (!(x_linear_acceleration == 0 && y_linear_acceleration == 0 && z_linear_acceleration == 0)){
                // does not have Sensor.TYPE_LINEAR_ACCELERATION

                //currentLinAcceleration = (((float) Math.pow(x_linear_acceleration, 2)) + (float)Math.pow(y_linear_acceleration, 2)+ (float)Math.pow(z_linear_acceleration, 2));

                //addEntry(lineGraphChart, x_linear_acceleration, 0);
                //addEntry(lineGraphChart, y_linear_acceleration, 1);
                //addEntry(lineGraphChart, currentLinAcceleration, 2);
            }
        }*/

        if (accSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, this.accelerationOnAxis, 0, sensorEvent.values.length);

            if (withTestData) {
                //Toast.makeText(getContext(),"Cleared",Toast.LENGTH_SHORT).show();
                if (i > 1425){
                    i = 0;
                    //Toast.makeText(getContext(),"Repeat",Toast.LENGTH_SHORT).show();
                }

                x_accelerometer = xFloatTestVector[i];
                y_accelerometer = yFloatTestVector[i];
                z_accelerometer = zFloatTestVector[i];
                i++;
            } else {
                x_accelerometer = sensorEvent.values[0];
                y_accelerometer = sensorEvent.values[1];
                z_accelerometer = sensorEvent.values[2];
            }

            if (j > 1200) {
                //removeDataSet(chartGraphData, 0);
                clearDataSet(chartGraphData, 1);
                //Log.d("Graph", "Cleared" );
                j = 0;
                //removeDataSet(chartGraphData, 2);
                //removeDataSet(chartGraphData, 3);
            }
            j++;
            timestampOld = timestamp;
            timestamp = System.nanoTime();

            float deltaTime = timestamp - timestampOld;

            dt = 1 / (count / ((deltaTime) / 100000000.0f));

            count++;

            alpha = timeConstant / (timeConstant + dt);  //HERE
            //alpha = 0.8f;
            // long tDelta = currentUpdate.getTime() - lastUpdate.getTime();
            // long t = 1 / (2 * Math.PI * fc);
            //alpha = t / (t + tDelta);

            x_gravity = alpha * x_gravity + (1 - alpha) * x_accelerometer;
            y_gravity = alpha * y_gravity + (1 - alpha) * y_accelerometer;
            z_gravity = alpha * z_gravity + (1 - alpha) * z_accelerometer;

            x_linear_acceleration = x_accelerometer - x_gravity;
            y_linear_acceleration = y_accelerometer - y_gravity;
            z_linear_acceleration = z_accelerometer - z_gravity;

            //gravityOnAxis = new float[]{x_gravity, y_gravity, z_gravity};
            //linearAcceleration = new float[]{x_linear_acceleration, y_linear_acceleration, z_linear_acceleration};

            //int length = linearAcceleration.length - 1;
            //Log.e("LinearAccel", "on x-" + x_linear_acceleration +"on y-" + y_linear_acceleration + "on z-" + z_linear_acceleration);

            //allAccelData.add(linearAcceleration);
///     }   -- -- if linear sensor is avaiable

            currentLinAcceleration = (float) (Math.pow(x_linear_acceleration, 2) + Math.pow(y_linear_acceleration, 2) + Math.pow(z_linear_acceleration, 2));

            //Log.e("AccelLin", Float.toString(currentLinAcceleration));
            //Log.e("AccelTotal", "\n" +Float.toString(z_linear_acceleration));
            //allLinAccelData.add(currentLinAcceleration);
            //Log.e("LinearAccel", "Alpha:" + alpha + " " + "\nNormalAccel " + listToString() + "\nCurrent Lin Accel " + Float.toString(currentLinAcceleration));

            findEachStroke();
            addEntry(lineGraphChart, z_linear_acceleration, 1);
            lineGraphChart.notifyDataSetChanged();
            lineGraphChart.invalidate();

            //addEntry(lineGraphChart, z_linear_acceleration, 4);
            //addEntry(lineGraphChart, x_accelerometer, 0);
            //addEntry(lineGraphChart, y_accelerometer, 1);
            //addEntry(lineGraphChart, z_accelerometer, 2);

        } // -- -- remove for lin accel eration from sensor
    }
    private void findEachStroke() { //HERE
        if (z_linear_acceleration >= STATE_FOR_HIGH_ACCEL_DATA){
            currentState = 1;
            if (sendNotify) {
                setNotifyForNewStroke();
                if(j< 600) {
                    //Toast.makeText(getContext(),"NewStroke",Toast.LENGTH_SHORT).show(); // For Debug TODO:
                }
            }
            sendNotify = false;
        }else if (z_linear_acceleration <= (STATE_FOR_LOW_ACCEL_DATA)) {
            currentState = -1;
            sendNotify = true;

        }else currentState = 0;
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

    private void setNotifyForNewStroke() {
        //communication with activity
        mCommChListner.notifyForNewStroke();
        //Toast.makeText(getContext(),"New Stroke",Toast.LENGTH_SHORT).show();
    }

    private String listToString(){
        String result = "X accel : " + Float.toString(x_accelerometer) +  " X: gravity" + Float.toString(x_gravity)+ "\nY: "
                +Float.toString(y_accelerometer) +  " Y gravity" + Float.toString(y_gravity)+ "\nZ acc:"
                + Float.toString(z_accelerometer) + " Z gravity: " + Float.toString(z_gravity)+ "\n"
                + "\n Linear Accel: \n"
                + " X linear" + Float.toString(x_linear_acceleration)
                + "\nY Lin acc: " + Float.toString(y_linear_acceleration)
                + "\nZ Lin acc: " + Float.toString(z_linear_acceleration) + "\n";
        return result;
    }
    public void sendNewLinAccel() {
        //communication with activity
        mCommChListner.sendNewLimAccel(currentLinAcceleration);
        //  Toast.makeText(getContext(),"New Stroke",Toast.LENGTH_SHORT).show();
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
       /* if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            linearAccelAccuracy = i;
        }*/
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
            throw new ClassCastException();
        }
    }
}
