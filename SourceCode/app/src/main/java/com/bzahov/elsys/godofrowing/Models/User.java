package com.bzahov.elsys.godofrowing.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by bobo-pc on 2/11/2017.
 */

// [START blog_user_class]
@IgnoreExtraProperties
public class User { // Data Class for Send information
    private int uId;
    private String username;
    private String email;
    private String pass; // for test
    private List<ResourcesFromActivity> resourcesFromActivityList;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String Uid, String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.pass = password;
    }

    public List<ResourcesFromActivity> getResourcesFromActivityList() {
        return resourcesFromActivityList;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
    public int getuId() {
        return uId;
    }

}
