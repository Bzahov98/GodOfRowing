package com.bzahov.elsys.godofrowing.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bzahov.elsys.godofrowing.AuthenticationActivities.LogInActivity;
import com.bzahov.elsys.godofrowing.Models.MyLocation;
import com.bzahov.elsys.godofrowing.Models.ResourcesFromActivity;
import com.bzahov.elsys.godofrowing.R;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class ResultContentAnalysisFragment extends Fragment implements OnMapReadyCallback, ValueEventListener {
    private static final String TAG = "ResConttAnalysisFrg";
    private MapView mapView;
    private RelativeLayout analysisContainer;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser useraaa;
    private DatabaseReference usersActivitiesRef;
    private List<MyLocation> allLocations;
    MyLocation allLocations2;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_result_analysis, container, false);

        preparationOfView(v, savedInstanceState);
        checkUserInfo();

        usersActivitiesRef = database.getReference("users").child(mUser.getUid()).child("activities");
        Query query = usersActivitiesRef.orderByChild("currentTime").limitToLast(1);
        Log.e("Query getRef()",query.getRef().toString());
        query.addValueEventListener(this);
        Query locationsQuery = usersActivitiesRef.orderByChild("currentTime").limitToLast(1).getRef().child("myLocationsList");
        //locationsQuery.addValueEventListener(new ValueEventListener() {
        //}})
        return v;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot){
        ResourcesFromActivity receivedData = null;// = dataSnapshot.getValue(ResourcesFromActivity.class);
        for(DataSnapshot activitySnapShot: dataSnapshot.getChildren()){
            receivedData = activitySnapShot.getValue(ResourcesFromActivity.class);
        }
        if (receivedData == null) {
            return;
        }

        Log.e("Query Ref",dataSnapshot.getRef().toString());
        Log.e("Query DataS","\n "+dataSnapshot.toString() );
        Log.e("Query getVal()","\n "+ receivedData.toString() );
        Log.e("Query","\n "+ Long.toString(receivedData.getTotalMeters()));

        allLocations = receivedData.getMyLocationsList();
        /*ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GenericTypeIndicator<Map<String,ResourcesFromActivity>> indicator = new GenericTypeIndicator<Map<String, ResourcesFromActivity>>() {};
        MyLocation allLocations2 = mapper.convertValue(dataSnapshot.getValue(indicator), MyLocation.class);
        Log.e("Mapper",allLocations2.toString());*/
        //https://stackoverflow.com/questions/37547399/how-to-deserialise-a-subclass-in-firebase-using-getvaluesubclass-class/37548330#37548330

        addMarkersToMap();
        setAllValuesOfViews(receivedData);
    }

    private void addMarkersToMap() {
        if (allLocations != null) {
            mapView.setVisibility(View.VISIBLE);
            //Log.e("Map", "---"+allLocations.get(2).getTime());
            Log.e("Map2",allLocations.size()+"");
            if (allLocations.size() > 1) {
                MyLocation firstLocation = allLocations.get(0);
                Log.e("first Loc:", firstLocation.getLng() + " " + firstLocation.getLat());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstLocation.getLat(), firstLocation.getLng()), 17f));
            }
            for (MyLocation location : allLocations) {
                Log.e("::",location.toString()); //TODO: add more rowing data for each location!
                //TODO: Find a way to add title and subtitle of mark at v2 Google maps!! - DONE
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dialog_close_dark));
                markerOptions.position(new LatLng(location.getLat(), location.getLng()));
                markerOptions.title(
                        "Speed - " + Float.toString(round(location.getSpeed(), 2)) + " m/s \n");
                markerOptions.snippet("Speed - "+ Float.toString(round(location.getSpeed(), 2)) + " m/s \n" +
                        "Stroke Rate Ave: - "   + Float.toString(round(location.getAverageStrokeRate(), 2)) + " per minute\n" +
                        "Stroke Rate Min  - " + /* */"" + "\n"+
                        "Speed average    - " + Float.toString(round(location.getAverageSpeed(),2))+ "\n"+
                        "Stroke Rate Max  - " + /* */"" + "\n"+
                        "Total Meters - " + (location.getTotalMeters()) + "\n"+
                        "Traveled Meters for storke"+/*location.getTotalMeters()-oldLocation.getTotalMeters()*/ "\n");

                Marker as = googleMap.addMarker(markerOptions);
            }
        }else {
            mapView.setVisibility(View.GONE);
        }
    }

    private void setAllValuesOfViews(ResourcesFromActivity receivedData) {
        setParameters(R.id.res_analysis_meters_total, 0, null, Long.toString(receivedData.getTotalMeters()));
        setParameters(R.id.res_analysis_speed_average, 0, null, Float.toString(round(receivedData.getAverageSpeed(),2)));
        setParameters(R.id.res_analysis_speed_max, 0, null, Float.toString(round(receivedData.getMaxSpeed(),2)));
        setParameters(R.id.res_analysis_empty,R.drawable.icon_analysis,"Ave StrokePerMin",Float.toString(round(receivedData.getAverageStrokeRate(),2)));
        setParameters(R.id.res_analysis_elapsed_time, 0, null, receivedData.getElapsedTimeStr());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //mapView.setVisibility(View.VISIBLE);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.146168, 24.711175), 13.8f));

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        mapView.onResume();
    }

    public void onMapClickAnalysis(View view) {
    }

    private void preparationOfView(View v,Bundle savedInstanceState) {

        ScrollView a = (ScrollView) v.findViewById(R.id.res_analysis_scroll_view);
        analysisContainer = (RelativeLayout) a.findViewById(R.id.tab_content_analysis_layout);

        mapView = (MapView) v.findViewById(R.id.res_analysis_map);
        //mapView.setVisibility(View.VISIBLE); // TODO Review
        if (mapView!=null){
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }else {
            analysisContainer.addView(mapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
        //mapView.setVisibility(View.VISIBLE); // TODO Review
        analysisContainer = (RelativeLayout) a.findViewById(R.id.tab_content_analysis_layout);

        //set defaut values
        setParameters(R.id.res_analysis_meters_total ,R.drawable.icon_meters  , "Distance(m): " , "0000");
        setParameters(R.id.res_analysis_elapsed_time ,R.drawable.icon_timer   , "Duration: "    , "0:00:00");
        setParameters(R.id.res_analysis_empty        ,R.drawable.icon_analysis, "StrokePerMin"  , "0");
        setParameters(R.id.res_analysis_speed_average,R.drawable.icon_speed   , "Ave sec/500m"  , " 0:00");
        setParameters(R.id.res_analysis_speed_max,    R.drawable.icon_speed   , "Max Speed/500m", " 0:00");
    }

    private void checkUserInfo() {
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
                    startActivity(new Intent(getActivity(), LogInActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public static float round(float source, int positions) {
        long multiplier = (long) Math.pow(10, positions);
        return  ((float)((int) (source * multiplier)) / multiplier);
    }

    private void setParameters(int viewID, int imageID, @Nullable String name, String value){
        RelativeLayout viewById = ((RelativeLayout) analysisContainer.findViewById(viewID));
        if (imageID != 0) {
            ImageView imageView = ((ImageView) viewById.findViewById(R.id.list_item_head_header));
            imageView.setImageResource(imageID);
        }
        if (name != null) {
            TextView nameView = ((TextView) viewById.findViewById(R.id.res_layout_parameter_name));
            nameView.setText(name);
        }if (value != null){
            TextView  valueView = ((TextView) viewById.findViewById(R.id.list_item_head_text_workout));
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        addMarkersToMap();
        mAuth.addAuthStateListener(mAuthListener);

        if (mAuth.getCurrentUser() != null){
            mUser = mAuth.getCurrentUser();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        googleMap.clear();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        final Bundle mapViewSaveState = new Bundle(savedInstanceState);
        mapView.onSaveInstanceState(mapViewSaveState);
        savedInstanceState.putBundle("mapViewSaveState", mapViewSaveState);

        Bundle customBundle = new Bundle();
        // put custom objects if needed to customBundle

        super.onSaveInstanceState(savedInstanceState);
    }

}