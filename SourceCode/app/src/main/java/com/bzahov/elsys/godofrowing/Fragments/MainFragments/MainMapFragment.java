package com.bzahov.elsys.godofrowing.Fragments.MainFragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bzahov.elsys.godofrowing.MainActivity;
import com.bzahov.elsys.godofrowing.R;
import com.bzahov.elsys.godofrowing.RowApplication;
import com.bzahov.elsys.godofrowing.Support.MathFunct;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;
//import static com.bzahov.elsys.godofrowing.Support.MathFunct.roundFloat;

/**
 * Created by bobo-pc on 12/28/2016.
 */
public class MainMapFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener {

    private static final String TAG = MainMapFragment.class.getSimpleName();
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5; //1 second
    private GoogleMap mMap;

    private LocationManager locationManager;
    private Location mLastLocation;
    private double latitude;
    private MapView mMapView;
    private double longitude;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private MapFrgCommunicationChannel mCommChListner;
    private MainActivity mainActivity;
    private boolean isSportActivityStarted;
    private int locationCounter;
    private boolean isFirst;
    private RowApplication app = RowApplication.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showSettingsAlert();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mainActivity = (MainActivity) getActivity();
        locationCounter =0;
        checkSportActivityStatus();

        mapSetUp(savedInstanceState, view);

        return view;
    }

    private void mapSetUp(Bundle savedInstanceState, View view) {

        mMapView = (MapView) view.findViewById(R.id.mapFragment);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
    }

    private boolean checkSportActivityStatus() { // check does sport activity at Main is at training mode (isStarted)
      return isSportActivityStarted = mainActivity.getIsStarted();
    }

    private synchronized void buildGoogleApiClient() {
       // Log.d(TAG, "buildGoogleApi");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private static final int MY_PERMISSION_REQUEST_LOCATION = 3;

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        alertDialog.setTitle(app.getString(R.string.dialog_gps_title)).setCancelable(false);

        alertDialog.setMessage(app.getString(R.string.dialog_gps_message_enable));

        alertDialog.setPositiveButton(app.getString(R.string.dialog_gps_button_positive_settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
              //  Log.d(TAG, "PossitiveButton");
            }
        });
        alertDialog.setNegativeButton(app.getString(R.string.dialog_gps_button_negative_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
           //     Log.d(TAG, "NegativeButton");
                //Toast.makeText(getContext(),"Cancaled", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private boolean checkLocationPermission() {
        String[] permStrArray = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
        };
        //Log.d(TAG, "CheckLocationPermissions");
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), permStrArray, MY_PERMISSION_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(), permStrArray,
                        MY_PERMISSION_REQUEST_LOCATION);
            }
            return false;
        } else {
        //    Log.d(TAG, "HavePermission");
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showSettingsAlert();
            }
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       // Log.d(TAG, "onMapReady");
        mMap = googleMap;
        LatLng plovdivCanal = new LatLng(42.1500, 24.7167);
        gotoLocation(plovdivCanal, 16);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setTrafficEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        /*Location MyLocation = getmLastLocation();
        if(MyLocation != null) {
            gotoLocation(MyLocation.getLatitude(), MyLocation.getLongitude(), 17.0f);
        }*/

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else checkLocationPermission();
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setCompassEnabled(true);
        //mMap.addMarker(new MarkerOptions().position(sofiaNDK).title("Marker in NDK"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sofiaNDK));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                 //       Log.d(TAG, "Location Permission Granted: " + MY_PERMISSION_REQUEST_LOCATION);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            showSettingsAlert();
                        }
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied
                //    Log.d(TAG, "Location Permission denied: " + MY_PERMISSION_REQUEST_LOCATION);
                    Toast.makeText(getContext(), app.getString(R.string.err_permission_location_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 404: {
                break;
            }
            default: {
               // Log.d(TAG, "Location Code Wrong: " + requestCode);
                break;
            }
        }

    }

    /*@Override
    public void sendDataFromMainToFragment(String data) {

    }
*/
    public interface MapFrgCommunicationChannel {
        void setMapCommunication(String msg);

        void sendNewLocation(Location location);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapFrgCommunicationChannel) {
            mCommChListner = (MapFrgCommunicationChannel) context;
        } else {
            //throw new ClassCastException();
        }
    }

    public void sendMessage(String msg) {
        mCommChListner.setMapCommunication(msg);
    }

    public void sendLocation(Location location) {
        mCommChListner.sendNewLocation(location);
    }

    public void gotoLocation(double lat, double lng, float zoom) {
       // Log.d("value", "gotoLocation called");
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);
    }

    public void gotoLocation(LatLng latLng, float zoom) {
        //Log.d("value", "gotoLocation called");
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        if (locationCounter == 0 || checkSportActivityStatus()) {
            locationCounter++;
            mMap.moveCamera(update);

        }
    }

    public double getLatitude() {
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
        }

        // return latitude
        return latitude;
    }

    public double getLongitude() {
        if (mLastLocation != null) {
            longitude = mLastLocation.getLongitude();
        }

        // return longitude
        return longitude;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        android.app.Fragment fragment = getActivity().getFragmentManager().findFragmentById(R.id.map);
        if (null != fragment) {
            android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
       // Log.d ("as", "onLocationChanged" + location.toString());
        if (checkSportActivityStatus()) {
            mLastLocation = location;
         //   Log.d(TAG, "marker Removed");
            //mCurrLocationMarker.remove();
            LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            gotoLocation(newLatLng, 16);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dialog_close_dark));
            markerOptions.position(newLatLng);
            markerOptions.title("Speed - " + Float.toString(MathFunct.roundFloat(location.getSpeed(), 2)) + " m/s");
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            sendLocation(location);
            /*if (location.hasSpeed()) {
                //sendLocation(location);
                //float currentSpeed = location.getSpeed();
                //Toast.makeText(getContext(),"Speed is " + currentSpeed + " km/h",Toast.LENGTH_SHORT ).show();
                //Log.d(TAG, "Speed: " + currentSpeed);
            } else {
                if (isFirst){
                    isFirst = false;
                    //sendLocation(location);
                }
                //Toast.makeText(getContext(),"Location Hasn't speed",Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Location has't Speed");
            }*/
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Log.d(TAG, "onConnected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);

        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Toast.makeText(getContext(), "Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), app.getString(R.string.err_toast_gps_connect_failed), Toast.LENGTH_SHORT).show();
        checkLocationPermission();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Toast.makeText(getContext(), "onPause", Toast.LENGTH_SHORT).show();
        //Log.d(TAG, "onPause");
        if (mGoogleApiClient != null) {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           if (mGoogleApiClient != null) {
               mGoogleApiClient.connect();
           }else {
               checkLocationPermission();
           }

            //  LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        mMapView.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
