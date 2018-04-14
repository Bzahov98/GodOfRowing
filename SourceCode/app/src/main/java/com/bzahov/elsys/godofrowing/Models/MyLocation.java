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

    public long currentTime;
    public double lat;
    public double lng;
    //training information at current moment
    public String elapsedTimeStr;
    public float averageSpeed;
    public float currentSpeed;
    public float maxSpeed;
    public float strokeRate;
    public float averageStrokeRate;
    public long totalMeters;



    public MyLocation() {
        // Default constructor required for calls to DataSnapshot.getValue(MyLocation.class)
    }

    public MyLocation(Location location, String elapsedTimeStr,
                      float averageSpeed, float strokeRate,
                      float averageStrokeRate, long totalMeters)
    {
        this.elapsedTimeStr = elapsedTimeStr;
        this.averageSpeed = averageSpeed;
        this.strokeRate = strokeRate;
        this.averageStrokeRate = averageStrokeRate;
        this.totalMeters = totalMeters;

        this.currentSpeed = location.getSpeed();
        this.currentTime = location.getTime();
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();
    }

    public MyLocation(double lat, double lng, @Nullable float speed,long currentTime){
        this.lat = lat;
        this.lng = lng;
        this.currentSpeed = speed;
        this.currentTime = currentTime;

    }

    public MyLocation(Location inputLoc){
        this.lat = inputLoc.getLatitude();
        this.lng = inputLoc.getLongitude();
        this.currentSpeed = inputLoc.getSpeed();
        this.currentTime = inputLoc.getTime();
    }

    public MyLocation(Location inputLoc, float strokeRate,float averageStrokeRate,long totalMeters){
        this.lat = inputLoc.getLatitude();
        this.lng = inputLoc.getLongitude();
        this.currentSpeed = inputLoc.getSpeed();
        this.currentTime = inputLoc.getTime();
        this.strokeRate = strokeRate;
        this.averageStrokeRate = averageStrokeRate;
        this.totalMeters = totalMeters;
    }

    public MyLocation(Location inputLoc, String elapsedTimeStr){//, float maxSpeed, float averageSpeed){//, float strokeRate,float averageStrokeRate,long totalMeters){
        this.lat = inputLoc.getLatitude();
        this.lng = inputLoc.getLongitude();
        this.currentTime = inputLoc.getTime();
        this.currentSpeed = inputLoc.getSpeed();
        this.elapsedTimeStr = elapsedTimeStr;
        this.maxSpeed = maxSpeed;
        this.averageSpeed = averageSpeed;/*
        this.strokeRate = strokeRate;
        this.averageStrokeRate = averageStrokeRate;
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
        return currentTime;
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
        return averageStrokeRate;
    }

    public long getTotalMeters() {
        return totalMeters;
    }

    @Override
    public String toString() {
        return "AllLocations{" +
                " currentTime='" + getTime() + '\'' +
                ", Lat=" + getLat() +
                ", lng='" + getLng() + '\'' +
                ", averageSpeed=" + averageSpeed +
                ", maxSpeed=" + maxSpeed +
                ", currentSpeed=" + currentSpeed +
                ", AverageStrokeRate=" + averageStrokeRate +
                ", totalMeters*=" + totalMeters +
                '}';
    }
}
