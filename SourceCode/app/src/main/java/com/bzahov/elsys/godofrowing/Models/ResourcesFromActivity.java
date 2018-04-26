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

    public long curTimeMillis;

    public TrainingOverview trainingOverview = new TrainingOverview();
    public List<MyLocation> allTrainLocations = new ArrayList<>();
    public List<Location> allLocations = new ArrayList<>();
    public List<Float> allStrokes = new ArrayList<>();

    public ResourcesFromActivity(){
        // Default constructor required for calls to DataSnapshot.getValue(ResourcesFromActivity.class)
    }

    public ResourcesFromActivity(String currentTime, long elapsedTime, String elapsedTimeStr, float averageSpeed, float maxSpeed, float averageStrokeRate, long totalMeters, List<MyLocation> allTrainLocations, List<Location> allLocations, List<Float> allStrokes) {
        this.trainingOverview.currentTime = currentTime;
        this.trainingOverview.elapsedTime = elapsedTime;
        this.trainingOverview.elapsedTimeStr = elapsedTimeStr;
        this.trainingOverview.averageSpeed = averageSpeed;
        this.trainingOverview.maxSpeed = maxSpeed;
        this.trainingOverview.averageStrokeRate = averageStrokeRate;
        this.trainingOverview.totalMeters = totalMeters;
        this.allTrainLocations = allTrainLocations;
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
        this.trainingOverview.totalMeters = totalMeters;
        this.trainingOverview.averageStrokeRate = averageStrokeRate;
        this.trainingOverview.maxSpeed = maxSpeed;
        this.trainingOverview.averageSpeed = averageSpeed;
        this.trainingOverview.elapsedTime = elapsedTime;

    }

    public ResourcesFromActivity(@Nullable ArrayList<MyLocation> allLocations,
                                 /*@Nullable List<Float> allSpeeds,
                                 @Nullable List<Location> allLocations,*/
                                 long totalMeters,
                                 float averageStrokeRate,
                                 float maxSpeed,
                                 float averageSpeed,
                                 String elapsedTimeStr,
                                 String currentTime,
                                 long curTimeMillis) {
        /*this.allStrokes = allStrokes;
        this.allSpeeds = allSpeeds;
        this.allLocations = allLocations;*/
        this.allTrainLocations = allLocations;
        this.trainingOverview.totalMeters = totalMeters;
        this.curTimeMillis= curTimeMillis;
        this.trainingOverview.setCurTimeMillis(curTimeMillis);
        this.trainingOverview.averageStrokeRate = averageStrokeRate;
        this.trainingOverview.maxSpeed = maxSpeed;
        this.trainingOverview.averageSpeed = averageSpeed;
        this.trainingOverview.elapsedTimeStr = elapsedTimeStr;
        this.trainingOverview.currentTime = currentTime;
    }

    public ResourcesFromActivity(long totalMeters, String elapsedTimeStr, float averageStrokeRate, String currentTime) {
        this.trainingOverview.totalMeters = totalMeters;
        this.trainingOverview.averageStrokeRate = averageStrokeRate;
        this.trainingOverview.maxSpeed = 0;
        this.trainingOverview.averageSpeed = 0;
        this.trainingOverview.elapsedTimeStr = elapsedTimeStr;
        this.trainingOverview.currentTime = currentTime;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("averageSpeed", trainingOverview.averageSpeed);
        result.put("maxSpeed", trainingOverview.maxSpeed);
        result.put("averageStrokeRate", trainingOverview.averageStrokeRate);
        result.put("totalMeters", trainingOverview.totalMeters);
        result.put("elapsedTime", trainingOverview.elapsedTime);
        result.put("currentTime", trainingOverview.currentTime);
        result.put("curTimeMillis",curTimeMillis);

        return result;
    }

    @Override
    public String toString() {
        return "ResourcesFromActivity{" +
                "curTimeMillis='" + curTimeMillis + '\'' +
                "currentTime='" + trainingOverview.currentTime + '\'' +
                ", elapsedTime=" + trainingOverview.elapsedTime +
                ", elapsedTimeStr='" + trainingOverview.elapsedTimeStr + '\'' +
                ", averageSpeed=" + trainingOverview.averageSpeed +
                ", maxSpeed=" + trainingOverview.maxSpeed +
                ", averageStrokeRate=" + trainingOverview.averageStrokeRate +
                ", totalMeters=" + trainingOverview.totalMeters +
                ", allTrainLocations=" + allTrainLocations.toString() +
                ", allLocations=" + allLocations.toString() +
                ", allStrokes=" + allStrokes.toString() +
                '}';
    }

    public void setTrainingOverview(TrainingOverview trainingOverview) {
        this.trainingOverview = trainingOverview;
    }

    public void setAllTrainLocations(List<MyLocation> allTrainLocations) {
        this.allTrainLocations = allTrainLocations;
    }

    public void setAllLocations(List<Location> allLocations) {
        this.allLocations = allLocations;
    }

    public void setAllStrokes(List<Float> allStrokes) {
        this.allStrokes = allStrokes;
    }

    public List<MyLocation> getAllTrainLocations() {
        return allTrainLocations;
    }

    public List<Location> getAllLocations() {
        return allLocations;
    }

    public List<Float> getAllStrokes() {
        return allStrokes;
    }

    public TrainingOverview getTrainingOverview() {
        return trainingOverview;
    }

    /*
    public void setCurrentTime(String currentTime) {
        this.trainingOverview.currentTime = currentTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.trainingOverview.elapsedTime = elapsedTime;
    }

    public void setElapsedTimeStr(String elapsedTimeStr) {
        this.trainingOverview.elapsedTimeStr = elapsedTimeStr;
    }

    public void setAverageSpeed(float averageSpeed) {
        this.trainingOverview.averageSpeed = averageSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.trainingOverview.maxSpeed = maxSpeed;
    }

    public void setAverageStrokeRate(float averageStrokeRate) {
        this.trainingOverview.averageStrokeRate = averageStrokeRate;
    }

    public void setTotalMeters(long totalMeters) {
        this.trainingOverview.totalMeters = totalMeters;
    }

/*
    public String getCurrentTime() {
        return trainingOverview.currentTime;
    }

    public long getElapsedTime() {
        return trainingOverview.elapsedTime;
    }

    public String getElapsedTimeStr() {
        return trainingOverview.elapsedTimeStr;
    }

    public float getAverageSpeed() {
        return trainingOverview.averageSpeed;
    }

    public float getMaxSpeed() {
        return trainingOverview.maxSpeed;
    }

    public float getAverageStrokeRate() {
        return trainingOverview.averageStrokeRate;
    }

    public long getTotalMeters() {
        return trainingOverview.totalMeters;
    }
*/

}
