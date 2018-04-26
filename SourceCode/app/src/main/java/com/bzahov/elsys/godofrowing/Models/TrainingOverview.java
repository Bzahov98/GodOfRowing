package com.bzahov.elsys.godofrowing.Models;

public class TrainingOverview {

    public long curTimeMillis;
    public String currentTime;
    public long elapsedTime;
    public String elapsedTimeStr;
    public float averageSpeed;
    public float maxSpeed;
    public float averageStrokeRate;
    public long totalMeters;

    public TrainingOverview() {

    }

    public TrainingOverview(long curTimeMillis, String currentTime, long elapsedTime, String elapsedTimeStr, float averageSpeed, float maxSpeed, float averageStrokeRate, long totalMeters) {
        this.curTimeMillis = curTimeMillis;
        this.currentTime = currentTime;
        this.elapsedTime = elapsedTime;
        this.elapsedTimeStr = elapsedTimeStr;
        this.averageSpeed = averageSpeed;
        this.maxSpeed = maxSpeed;
        this.averageStrokeRate = averageStrokeRate;
        this.totalMeters = totalMeters;
    }

    @Override
    public String toString() {
        return "TrainingOverview{" +
                "\ncurTimeMillis=" + curTimeMillis +
                ", curTime='" + currentTime + '\'' +
                ", elTime=" + elapsedTime +
                ", elTimeStr='" + elapsedTimeStr + '\'' +
                ", aveSpeed=" + averageSpeed +
                ", maxSpeed=" + maxSpeed +
                ", aveSRate=" + averageStrokeRate +
                ", totMeters=" + totalMeters +
                "\n}";
    }

    public void setAverageStrokeRate(float averageStrokeRate) {
        this.averageStrokeRate = averageStrokeRate;
    }
    public void setTotalMeters(long totalMeters) {
        this.totalMeters = totalMeters;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    public void setElapsedTimeStr(String elapsedTimeStr) {
        this.elapsedTimeStr = elapsedTimeStr;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setCurTimeMillis(long curTimeMillis) {
        this.curTimeMillis = curTimeMillis;
    }

    public long getCurTimeMillis() {
        return curTimeMillis;
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

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getAverageStrokeRate() {
        return averageStrokeRate;
    }



    public long getTotalMeters() {
        return totalMeters;
    }
    }