package com.bzahov.elsys.godofrowing.Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by bobo-pc on 2/14/2017.
 */

@IgnoreExtraProperties
public class Clubs {

    public int id;
    public String name;
    public String country;

    public Clubs(){

    }
    public Clubs(String name,String country,int id){
        this.name = name;
        this.country = country;
        this.id  = id;
    }

    public String getMovieName() {
        return name;
    }

    public void setMovieName(String movieName) {
        this.name = movieName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}