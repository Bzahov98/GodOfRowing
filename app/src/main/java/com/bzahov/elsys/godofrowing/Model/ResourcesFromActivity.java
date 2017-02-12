package com.bzahov.elsys.godofrowing.Model;

import android.location.Location;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bobo-pc on 2/10/2017.
 */
@IgnoreExtraProperties
public class ResourcesFromActivity {

    private String currentTime;
    private long elapsedTime;
    private String elapsedTimeStr;
    private float averageSpeed;
    private float maxSpeed;
    private float averageStrokeRate;
    private long totalMeters;
    //private List<Location> allLocations = new ArrayList<>();
    //private List<Float> allSpeeds = new ArrayList<>();
    //private List<Float> allStrokes = new ArrayList<>();

    public ResourcesFromActivity(){}

    public ResourcesFromActivity(/*@Nullable List<Float> allStrokes,
                                 @Nullable List<Float> allSpeeds,
                                 @Nullable List<Location> allLocations,*/
                                 long totalMeters,
                                 float averageStrokeRate,
                                 float maxSpeed,
                                 float averageSpeed,
                                 long elapsedTime) {
        /*this.allStrokes = allStrokes;
        this.allSpeeds = allSpeeds;
        this.allLocations = allLocations;*/
        this.totalMeters = totalMeters;
        this.averageStrokeRate = averageStrokeRate;
        this.maxSpeed = maxSpeed;
        this.averageSpeed = averageSpeed;
        this.elapsedTime = elapsedTime;

    }

    //TODO: implement own Location method, because Location haven't empty constructor
    public ResourcesFromActivity(/*@Nullable List<Float> allStrokes,
                                 @Nullable List<Float> allSpeeds,
                                 @Nullable List<Location> allLocations,*/
                                 long totalMeters,
                                 float averageStrokeRate,
                                 float maxSpeed,
                                 float averageSpeed,
                                 String elapsedTimeStr, String currentTime) {
        /*this.allStrokes = allStrokes;
        this.allSpeeds = allSpeeds;
        this.allLocations = allLocations;*/
        this.totalMeters = totalMeters;
        this.averageStrokeRate = averageStrokeRate;
        this.maxSpeed = maxSpeed;
        this.averageSpeed = averageSpeed;
        this.elapsedTimeStr = elapsedTimeStr;
        this.currentTime = currentTime;
    }

    public long getElapsedTimeLong() {
        return elapsedTime;
    }

    public String getElapsedTimeString() {
        return elapsedTimeStr;
    }

    public static String splitToComponentTimes(long input) {
        int hours = (int) input / 3600;
        int remainder = (int) input - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        String result = hours + ":" + mins +":" + secs;
        return result;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getAverageStrokeRate() {
        return averageStrokeRate;
    }

    public long getTotalMeters() {
        return totalMeters;
    }/*

    public List<Location> getAllLocations() {
        return allLocations;
    }

    public List<Float> getAllSpeeds() {
        return allSpeeds;
    }

    public List<Float> getAllStrokes() {
        return allStrokes;
    }
*/
    @Override
    public String toString() {
        return "ResourcesFromActivity{" +
                "averageSpeed=" + Float.toString(averageSpeed) +
                ", maxSpeed=" + Float.toString(maxSpeed) +
                ", averageStrokeRate=" + Float.toString(averageStrokeRate) +
                ", totalMeters=" + Long.toString(totalMeters) +'}';/*
                ", allLocations=" + allLocations.toString() +
                ", allSpeeds=" + allSpeeds.toString() +
                ", allStrokes=" + allStrokes.toString() +
                '}';*/
    }

    public String getCurrentTime() {
        return currentTime;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("averageSpeed", averageSpeed);
        result.put("maxSpeed", maxSpeed);
        result.put("averageStrokeRate", averageStrokeRate);
        result.put("totalMeters", getTotalMeters());
        result.put("elapsedTime", elapsedTime);
        result.put("currentTime",currentTime);

        return result;
    }

}
