package com.bzahov.elsys.godofrowing.Models;

import android.location.Location;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by bobo-pc on 2/10/2017.
 */
@IgnoreExtraProperties
public class MyLocation {
    //location information
    private long time;
    private double lat;
    private double lng;
    //training information at current moment
    private String elapsedTimeStr;
    private float averageSpeed;
    private float currentSpeed;
    private float maxSpeed;
    private float strokeRate;
    private float AverageStrokeRate;
    private long totalMeters;



    public MyLocation() {
        // Default constructor required for calls to DataSnapshot.getValue(MyLocation.class)
    }
    public MyLocation(double lat, double lng, @Nullable float speed,long time){
        this.lat = lat;
        this.lng = lng;
        this.currentSpeed = speed;
        this.time = time;

    }
    public MyLocation(Location inputLoc){
        this.lat = inputLoc.getLatitude();
        this.lng = inputLoc.getLongitude();
        this.currentSpeed = inputLoc.getSpeed();
        this.time = inputLoc.getTime();
    }

    public MyLocation(Location inputLoc, String elapsedTimeStr){//, float maxSpeed, float averageSpeed){//, float strokeRate,float averageStrokeRate,long totalMeters){
        this.lat = inputLoc.getLatitude();
        this.lng = inputLoc.getLongitude();
        this.time = inputLoc.getTime();
        this.currentSpeed = inputLoc.getSpeed();
        this.elapsedTimeStr = elapsedTimeStr;
        this.maxSpeed = maxSpeed;
        this.averageSpeed = averageSpeed;/*
        this.strokeRate = strokeRate;
        this.AverageStrokeRate = averageStrokeRate;
        this.totalMeters = totalMeters;*/
    }

    @Exclude
    public float distanceTo(MyLocation inputLocation){
        float[] result = new float[3];
        Location.distanceBetween(lat,lng,inputLocation.getLat(),inputLocation.getLng(), result );
        return result[0];
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public float getSpeed() {
        return currentSpeed;
    }

    public float getTime() {
        return time;
    }

    public String getElapsedTimeStr() {
        return elapsedTimeStr;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getStrokeRate() {
        return strokeRate;
    }

    public float getAverageStrokeRate() {
        return AverageStrokeRate;
    }

    public long getTotalMeters() {
        return totalMeters;
    }

}
