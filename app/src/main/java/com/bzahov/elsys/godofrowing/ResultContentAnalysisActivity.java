package com.bzahov.elsys.godofrowing;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bzahov.elsys.godofrowing.Model.ResourcesFromActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bobo-pc on 2/8/2017.
 */
public class ResultContentAnalysisActivity extends Activity implements OnMapReadyCallback, ValueEventListener {

    private MapView mapView;
    private RelativeLayout analysisContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_analysis);
        ScrollView a = (ScrollView) findViewById(R.id.res_analysis_scroll_view);
        a.setVisibility(View.VISIBLE);
        // a.setBackground(getDrawable(R.drawable.icon_settings));
        mapView = (MapView) findViewById(R.id.res_analysis_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        analysisContainer = (RelativeLayout) a.findViewById(R.id.tab_content_analysis_layout);

        setParameters(R.id.res_analysis_meters_total, R.drawable.icon_meters, "Distance(m): ", "0000");
        setParameters(R.id.res_analysis_elapsed_time, R.drawable.icon_timer, "Duration: ", "0:00:00");
        setParameters(R.id.res_analysis_empty,R.drawable.icon_analysis,"StrokePerMin","0");
        setParameters(R.id.res_analysis_speed_average, R.drawable.icon_speed, "Ave sec/500m", " 0:00");
        setParameters(R.id.res_analysis_speed_max, R.drawable.icon_speed, "Max Speed/500m", " 0:00");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.addListenerForSingleValueEvent(this);
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
}
