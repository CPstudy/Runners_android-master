package com.guk2zzada.runnerswar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.nearby.bootstrap.request.ConnectRequest;

import java.util.ArrayList;
import java.util.List;

public class TestmapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final static int MY_PERMISSION_REQUEST_CODE = 7171;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;

    public final static int RENEW_GPS = 1;

    GPSTracker gps;
    Handler handler;
    MarkerOptions marker;
    private Polyline line;
    private PolylineOptions polylineOptions;
    private ArrayList<LatLng> arrayPoints = new ArrayList<>();
    MapFragment mapFrag;
    GoogleMap gMap;
    TextView txtCoordinates;
    Button btnGetCoordinates, btnLocationUpdates;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    private static int UPDATE_INTERVAL = 1000;     // 이 시간 만큼 위치를 업데이트 하기 위해 노력한다.
    private static int FATEST_INTERVAL = 500;      // 이 시간보다 빠르게 업데이트 하지 않는다.
    private static int DISPLACEMENT = 1;           // Meters

    double latitude;
    double longitude;
    double[] arrLatitude;
    double[] arrLongitude;

    boolean mRequestingLocationUpdates = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        gps.buildGoogleApiClient();
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmap);

        txtCoordinates = findViewById(R.id.txtCoordinates);
        btnGetCoordinates = findViewById(R.id.btnGetCoordinates);
        btnLocationUpdates = findViewById(R.id.btnLocationUpdates);
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        marker = new MarkerOptions();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch(msg.what) {
                    case RENEW_GPS:
                        displayLocation(msg);
                        break;
                }
            }
        };
        handler.sendEmptyMessage(0);

        gps = new GPSTracker(TestmapActivity.this, handler);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                gps.buildGoogleApiClient();
                gps.createLocationRequest();
            }
        }

        btnGetCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps.displayLocation();
            }
        });

        btnLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tooglePeriodicLocationUpdates();
            }
        });
    }

    public void makeNewGpsService(){
        if(gps == null) {
            gps = new GPSTracker(TestmapActivity.this, handler);
        }
        //Sandwich.makeText(getApplicationContext(), "make GPS", Sandwich.LENGTH_SHORT).show();
        Log.e("GPS", "make GPS");
    }

    private void tooglePeriodicLocationUpdates() {
        if(!mRequestingLocationUpdates) {
            gps.clearRoute();
            arrayPoints.clear();
            line.remove();
            btnLocationUpdates.setText("Stop Location Update");
            mRequestingLocationUpdates = true;
            gps.startLocationUpdates();
        } else {
            btnLocationUpdates.setText("Start Location Update");
            mRequestingLocationUpdates = false;
            gps.stopLocationUpdates();
        }
    }

    private void displayLocation(Message msg) {
        List list = (List) msg.obj;
        arrayPoints.clear();

        if(line != null)
            line.remove();

        for(int i = 0; i < list.size(); i++) {
            arrayPoints.add((LatLng)list.get(i));

            Log.e("LatLng", latitude + " / " + longitude);
        }

        if(arrayPoints.size() > 0) {
            latitude = arrayPoints.get(arrayPoints.size() - 1).latitude;
            longitude = arrayPoints.get(arrayPoints.size() - 1).longitude;

            LatLng latLng = new LatLng(latitude, longitude);
            txtCoordinates.setText(latitude + " / " + longitude);
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(10);
            arrayPoints.add(latLng);
            polylineOptions.addAll(arrayPoints);
            line = gMap.addPolyline(polylineOptions);
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
