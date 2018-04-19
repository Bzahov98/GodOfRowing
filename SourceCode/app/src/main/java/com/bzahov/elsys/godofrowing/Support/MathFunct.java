package com.bzahov.elsys.godofrowing.Support;

import com.bzahov.elsys.godofrowing.Models.MyLocation;

import java.util.ArrayList;

public class MathFunct {

    public static int calcSecondsPer500meters(float speed) {

        double seconds = 0;

        if (speed > 0) {
            seconds = 500 / speed;

            if (seconds > 1000) {
                seconds = 0;
            }
        }
        return (int) seconds;
    }

    public static int calcSecondsPerGivenMeters(float speed, int meters) {

        double seconds = 0;

        if (speed > 0) {
            seconds = meters / speed;

            if (seconds > 1000) {
                seconds = 0;
            }
        }
        return (int) seconds;
    }

    public static int[] splitToComponentTimes(long input) {
        int[] result = {0, 0, 0};

        if (input == 0) {
            return result;
        }

        int hours = result[0] = (int) input / 3600;
        int remainder = (int) input - hours * 3600;
        int mins = result[1] = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = result[2] = remainder;

        return result;
    }

    public static float roundFloat(float source, int positions) {
        long multiplier = (long) Math.pow(10, positions);
        return ((float) ((int) (source * multiplier)) / multiplier);
    }

    public static float calcDistanceBetween(ArrayList<MyLocation> list) {
        int size = list.size() - 1;
        return list.get(size - 1).distanceTo(list.get(size));
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
}