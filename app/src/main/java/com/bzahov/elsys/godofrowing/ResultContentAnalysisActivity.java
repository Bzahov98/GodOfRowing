package com.bzahov.elsys.godofrowing;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by bobo-pc on 2/8/2017.
 */
public class ResultContentAnalysisActivity extends Activity implements OnMapReadyCallback {

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
        setParameters(R.id.res_analysis_speed_average, R.drawable.icon_speed, "Max Speed/500m", " 0:00");
        setParameters(R.id.res_analysis_speed_max, R.drawable.icon_speed, "Ave Speed/500m", " 0:00");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.146168, 24.711175), 13.8f));
        mapView.onResume();
    }

    public void onMapClickAnalysis(View view) {
    }

    private void setParameters(int viewID, int imageID, String name, String value){
        RelativeLayout viewById = ((RelativeLayout) analysisContainer.findViewById(viewID));

        ImageView imageView = ((ImageView) viewById.findViewById(R.id.res_layout_parameter_image));
         imageView.setImageResource(imageID);
        TextView  nameView = ((TextView) viewById.findViewById(R.id.res_layout_parameter_name));
         nameView.setText(name);
        TextView  valueView = ((TextView) viewById.findViewById(R.id.res_layout_parameter_value));
         valueView.setText(value);
    }
}
