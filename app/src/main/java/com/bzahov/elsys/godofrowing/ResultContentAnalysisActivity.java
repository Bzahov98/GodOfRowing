package com.bzahov.elsys.godofrowing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
import com.bzahov.elsys.godofrowing.Model.ResourcesFromActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bobo-pc on 2/8/2017.
 */
public class ResultContentAnalysisActivity extends Activity implements OnMapReadyCallback, ValueEventListener {

    private static final String TAG = "ResConttAnalysisAct-y";
    private MapView mapView;
    private RelativeLayout analysisContainer;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser useraaa;
    private DatabaseReference myUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_analysis);

        ScrollView a = (ScrollView) findViewById(R.id.res_analysis_scroll_view);
        mapView = (MapView) findViewById(R.id.res_analysis_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        analysisContainer = (RelativeLayout) a.findViewById(R.id.tab_content_analysis_layout);

        setParameters(R.id.res_analysis_meters_total, R.drawable.icon_meters, "Distance(m): ", "0000");
        setParameters(R.id.res_analysis_elapsed_time, R.drawable.icon_timer, "Duration: ", "0:00:00");
        setParameters(R.id.res_analysis_empty,R.drawable.icon_analysis,"StrokePerMin","0");
        setParameters(R.id.res_analysis_speed_average, R.drawable.icon_speed, "Ave sec/500m", " 0:00");
        setParameters(R.id.res_analysis_speed_max, R.drawable.icon_speed, "Max Speed/500m", " 0:00");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUser = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    startActivity(new Intent(ResultContentAnalysisActivity.this, LogInActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        /*mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mAuth.getCurrentUser().reload();
                    mUser = firebaseAuth.getCurrentUser();

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    startActivity(new Intent(ResultContentAnalysisActivity.this, LogInActivity.class));
                    Toast.makeText(getBaseContext(),"Welcome ",Toast.LENGTH_LONG).show();
                    Log.d("SED", "signed_out: ");
                }
            }
        };*/

        if (mAuth.getCurrentUser() == null) {
            mAuth.getCurrentUser().reload();
             Toast.makeText(getBaseContext(),"WTF",Toast.LENGTH_SHORT).show();
        }
        else{
             mUser = FirebaseAuth.getInstance().getCurrentUser();
            //myUserRef = database.getReference("users").child(mUser.getUid()).child("activities") ;
            myUserRef = database.getReference("message");

            Toast.makeText(getBaseContext(), mUser.getEmail(),Toast.LENGTH_SHORT).show();

            myUserRef.addValueEventListener(this);
        }

        DatabaseReference myRef = database.getReference("lastActivity");



        // Read from the database
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.146168, 24.711175), 13.8f));
        mapView.onResume();
    }

    public void onMapClickAnalysis(View view) {
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot){
        ResourcesFromActivity receivedData = dataSnapshot.getValue(ResourcesFromActivity.class);

        setParameters(R.id.res_analysis_meters_total, 0, null, Long.toString(receivedData.getTotalMeters()));
        setParameters(R.id.res_analysis_speed_average, 0, null, Float.toString(round(receivedData.getAverageSpeed(),2)));
        setParameters(R.id.res_analysis_speed_max, 0, null, Float.toString(round(receivedData.getMaxSpeed(),2)));
        setParameters(R.id.res_analysis_empty,R.drawable.icon_analysis,"Ave StrokePerMin",Float.toString(receivedData.getAverageStrokeRate()));
        setParameters(R.id.res_analysis_elapsed_time, 0, null, receivedData.getElapsedTimeString());

    }

    public static float round(float source, int positions) {
        long multiplier = (long) Math.pow(10, positions);
        return  ((float)((int) (source * multiplier)) / multiplier);
    }

    private void setParameters(int viewID, int imageID, @Nullable String name, String value){
        RelativeLayout viewById = ((RelativeLayout) analysisContainer.findViewById(viewID));
        if (imageID != 0) {
            ImageView imageView = ((ImageView) viewById.findViewById(R.id.res_layout_parameter_image));
            imageView.setImageResource(imageID);
        }
        if (name != null) {
            TextView nameView = ((TextView) viewById.findViewById(R.id.res_layout_parameter_name));
            nameView.setText(name);
        }if (value != null){
            TextView  valueView = ((TextView) viewById.findViewById(R.id.res_layout_parameter_value));
            valueView.setText(value);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mAuth.getCurrentUser() != null){
            mUser = mAuth.getCurrentUser();
        }else Toast.makeText(getBaseContext(),"WTF >>>>>>",Toast.LENGTH_SHORT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
        if (mAuth.getCurrentUser() != null){
            mUser = mAuth.getCurrentUser();
        }else Toast.makeText(getBaseContext(),"WTF >>>>>>",Toast.LENGTH_SHORT);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
