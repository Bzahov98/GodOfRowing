package com.bzahov.elsys.godofrowing.Models;

import android.support.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by bobo-pc on 2/10/2017.
 */
@IgnoreExtraProperties
public class MyLocation {

    private long lat;
    private long lng;
    private long speed;
    public MyLocation() {
        // Default constructor required for calls to DataSnapshot.getValue(MyLocation.class)
    }
    public MyLocation(long lat, long lng, @Nullable long speed){
        this.lat = lat;
        this.lng = lng;
        this.speed = speed;
    }
    public long getLat() {
        return lat;
    }

    public long getLng() {
        return lng;
    }

    public long getSpeed() {
        return speed;
    }

}
