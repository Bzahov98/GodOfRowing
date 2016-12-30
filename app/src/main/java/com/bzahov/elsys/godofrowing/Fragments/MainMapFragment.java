package com.bzahov.elsys.godofrowing.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzahov.elsys.godofrowing.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
/**
 * Created by bobo-pc on 12/28/2016.
 */
public class MainMapFragment extends Fragment implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private GoogleMap mMap;

    private UiSettings mUiSettings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.details, fragment);
        transaction.commit();

        fragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("value", "onMapReady");
        mMap = googleMap;
        //LatLng sofiaNDK = new LatLng(42.684694, 23.318871);
        gotoLocation(42.685, 23.319, 17.0f);
        //mMap.addMarker(new MarkerOptions().position(sofiaNDK).title("Marker in NDK"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sofiaNDK));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));


        if (!EasyPermissions.hasPermissions(getContext(), Context.LOCATION_SERVICE)) {
            Log.d("Debug", "requesting Permission");
            EasyPermissions.requestPermissions(this, "Give Location Permission, please!!!", 0, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            // onBackPressed();
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
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "checking Permission " + ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION));
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
    private void gotoLocation(double lat,double lng,float zoom) {
        Log.d("value","gotoLocation called");
        LatLng latLng=new LatLng(lat,lng);
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        mMap.moveCamera(update);
    }

}
