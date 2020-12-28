package com.guk2zzada.runnerswar;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class GPSTracker extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static int MY_PERMISSION_REQUEST_CODE = 7171;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;

    private ArrayList<LatLng> arrayPoints = new ArrayList<>();

    Context context;
    Handler handler;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    private static int UPDATE_INTERVAL = 1000;     // 이 시간 만큼 위치를 업데이트 하기 위해 노력한다.
    private static int FATEST_INTERVAL = 500;      // 이 시간보다 빠르게 업데이트 하지 않는다.
    private static int DISPLACEMENT = 1;           // Meters

    double latitude;
    double longitude;

    boolean mRequestingLocationUpdates = false;

    public GPSTracker(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void connect() {
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    private void tooglePeriodicLocationUpdates() {
        if(!mRequestingLocationUpdates) {
            arrayPoints.clear();
            startLocationUpdates();
        } else {
            stopLocationUpdates();
        }
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void displayLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            arrayPoints.add(latLng);
            sendLatLng(latitude, longitude);
        } else {

        }
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void clearRoute() {
        arrayPoints.clear();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if(mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    private void sendLatLng(double lat, double lng) {
        Bundle data = new Bundle();
        data.putDouble("lat", lat);
        data.putDouble("lng", lng);
        Message msg = handler.obtainMessage();
        msg.what = TestmapActivity.RENEW_GPS;
        msg.obj = arrayPoints;
        msg.setData(data);
        handler.sendMessage(msg);

    }
}
