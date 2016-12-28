package com.bzahov.elsys.godofrowing;

import android.*;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class RowMapFragment extends FragmentActivity implements  OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private GoogleMap mMap;

    private UiSettings mUiSettings;

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.details, container);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.d("Assert", "onCreate");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Assert", "onMapReady");
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sofiaNDK = new LatLng(42.684694, 23.318871);
        mMap.addMarker(new MarkerOptions().position(sofiaNDK).title("Marker in NDK"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sofiaNDK));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));


        if (!EasyPermissions.hasPermissions(this, LOCATION_SERVICE)) {
            Log.d("Debug", "requesting Permission");
            EasyPermissions.requestPermissions(this, "Give Location Permission, please!!!", 0, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
           // onBackPressed();
            Log.d("Assert", "ELSE AT requesting Permission");

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("Assert", "RequestPermission");

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "checking Permission " + ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION));
            //this.onBackPressed();
        }
        Log.d("Assert", "PermissionGranted\nsetMyLocation");
        mMap.setMyLocationEnabled(true);
        UiSettings  uiSettings = mMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setCompassEnabled(true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //this.onBackPressed();
        Log.d("Assert", "PermissionDenied");
    }
}
