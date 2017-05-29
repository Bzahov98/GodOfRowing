package com.bzahov.elsys.godofrowing.Models;

import android.location.Location;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class ResourcesFromActivity {

    public String currentTime;
    public long elapsedTime;
    public String elapsedTimeStr;
    public float averageSpeed;
    public float maxSpeed;
    public float averageStrokeRate;
    public long totalMeters;
    public List<MyLocation> myLocationsList = new ArrayList<>();
    public List<Location> allLocations = new ArrayList<>();
    public List<Float> allStrokes = new ArrayList<>();

    public ResourcesFromActivity(){
        // Default constructor required for calls to DataSnapshot.getValue(ResourcesFromActivity.class)
    }

    public ResourcesFromActivity(String currentTime, long elapsedTime, String elapsedTimeStr, float averageSpeed, float maxSpeed, float averageStrokeRate, long totalMeters, List<MyLocation> myLocationsList, List<Location> allLocations, List<Float> allStrokes) {
        this.currentTime = currentTime;
        this.elapsedTime = elapsedTime;
        this.elapsedTimeStr = elapsedTimeStr;
        this.averageSpeed = averageSpeed;
        this.maxSpeed = maxSpeed;
        this.averageStrokeRate = averageStrokeRate;
        this.totalMeters = totalMeters;
        this.myLocationsList = myLocationsList;
        this.allLocations = allLocations;
        this.allStrokes = allStrokes;
    }

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

    public ResourcesFromActivity(@Nullable ArrayList<MyLocation> allLocations,
                                 /*@Nullable List<Float> allSpeeds,
                                 @Nullable List<Location> allLocations,*/
                                 long totalMeters,
                                 float averageStrokeRate,
                                 float maxSpeed,
                                 float averageSpeed,
                                 String elapsedTimeStr, String currentTime) {
        /*this.allStrokes = allStrokes;
        this.allSpeeds = allSpeeds;
        this.allLocations = allLocations;*/
        this.myLocationsList = allLocations;
        this.totalMeters = totalMeters;
        this.averageStrokeRate = averageStrokeRate;
        this.maxSpeed = maxSpeed;
        this.averageSpeed = averageSpeed;
        this.elapsedTimeStr = elapsedTimeStr;
        this.currentTime = currentTime;
    }

    public ResourcesFromActivity(long totalMeters, String elapsedTimeStr, float averageStrokeRate, String currentTime) {
        this.totalMeters = totalMeters;
        this.averageStrokeRate = averageStrokeRate;
        this.maxSpeed = 0;
        this.averageSpeed = 0;
        this.elapsedTimeStr = elapsedTimeStr;
        this.currentTime = currentTime;
    }

    @Exclude
    public static String splitToComponentTimes(long input) {
        int hours = (int) input / 3600;
        int remainder = (int) input - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        String result = hours + ":" + mins +":" + secs;
        return result;
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

    @Override
    public String toString() {
        return "ResourcesFromActivity{" +
                "currentTime='" + currentTime + '\'' +
                ", elapsedTime=" + elapsedTime +
                ", elapsedTimeStr='" + elapsedTimeStr + '\'' +
                ", averageSpeed=" + averageSpeed +
                ", maxSpeed=" + maxSpeed +
                ", averageStrokeRate=" + averageStrokeRate +
                ", totalMeters=" + totalMeters +
                ", myLocationsList=" + myLocationsList.toString() +
                ", allLocations=" + allLocations.toString() +
                ", allStrokes=" + allStrokes.toString() +
                '}';
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setElapsedTimeStr(String elapsedTimeStr) {
        this.elapsedTimeStr = elapsedTimeStr;
    }

    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setAverageStrokeRate(float averageStrokeRate) {
        this.averageStrokeRate = averageStrokeRate;
    }

    public void setTotalMeters(long totalMeters) {
        this.totalMeters = totalMeters;
    }

    public void setMyLocationsList(List<MyLocation> myLocationsList) {
        this.myLocationsList = myLocationsList;
    }

    public void setAllLocations(List<Location> allLocations) {
        this.allLocations = allLocations;
    }

    public void setAllStrokes(List<Float> allStrokes) {
        this.allStrokes = allStrokes;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public String getElapsedTimeStr() {
        return elapsedTimeStr;
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
    }

    public List<MyLocation> getMyLocationsList() {
        return myLocationsList;
    }

    public List<Location> getAllLocations() {
        return allLocations;
    }

    public List<Float> getAllStrokes() {
        return allStrokes;
    }
}
