package com.bzahov.elsys.godofrowing;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.firebase.database.FirebaseDatabase;

/**
 * God of Rowing
 * Created by B. Zahov on 14.04.18.
 * md gouse, https://stackoverflow.com/questions/28672883/java-lang-illegalstateexception-fragment-not-attached-to-activity
 */
public class RowApplication extends Application {

    private static RowApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //offline data collection
    }

    public static synchronized RowApplication getInstance() {
        return mInstance;
    }

    @Override //https://stackoverflow.com/questions/33590009/how-to-use-multidex-with-a-custom-application-class
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
