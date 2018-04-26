package com.bzahov.elsys.godofrowing.Support;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * God of Rowing
 * Created by B. Zahov on 24.04.18.
 */
public class DateFunctions {

    public static long convertDatetoMillis(String str, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date mDate = sdf.parse(str);
            return mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isDateBefore(int day,int month, int year, Calendar compDate) {
        if (year<=compDate.get(Calendar.YEAR)){
            if (month<=compDate.get(Calendar.MONTH)){
                if (day<= compDate.get(Calendar.DAY_OF_MONTH)){

                    Log.e("before true",day+":"+ month +":"+year+"\n"+compDate.toString());
                    return true;}
            }
        }Log.e("before false",day+":"+ month +":"+year+"\n"+compDate.get(Calendar.DAY_OF_MONTH)+":"+compDate.get(Calendar.MONTH)+":"+year);

        return false;
    }

    public static boolean isDateAfter(int day,int month, int year, Calendar compDate) {
        if (year>=compDate.get(Calendar.YEAR)){
            if (month>=compDate.get(Calendar.MONTH)){
                if (day>= compDate.get(Calendar.DAY_OF_MONTH)){
                    return true;
                }
            }
        }return false;
    }

    public static String formatToStr(int num){
        if (num<10){
            return "0"+ String.valueOf(num);
        }else
            return String.valueOf(num);
    }

}
