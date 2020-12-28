package com.guk2zzada.runnerswar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.guk2zzada.runnerswar.PlayMultiActivity.RENEW_GPS;
import static com.guk2zzada.runnerswar.PlayMultiActivity.SEND_LATLNG;
import static com.guk2zzada.runnerswar.PlayMultiActivity.SEND_PRINT;

public class PlaySingleActivity extends AppCompatActivity implements OnMapReadyCallback {

    ArrayList<LatLngList> arrLL;
    ListAdapter adapter;

    Intent stepChecker;
    BroadcastReceiver receiver;
    GoogleMap gMap;
    MapFragment mapFrag;
    GPSTrackerOld gps = null;
    public Handler mHandler;

    Button btnStart;
    TextView txtNum;
    ListView list;

    boolean flag = true;
    String serviceData;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_single);

        btnStart = findViewById(R.id.btnStart);
        txtNum = findViewById(R.id.txtNum);
        list = findViewById(R.id.list);
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        arrLL = new ArrayList<>();
        stepChecker = new Intent(this, StepChecker.class);
        receiver = new PlayingReceiver();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == RENEW_GPS){
                    makeNewGpsService();
                }

                if(msg.what == SEND_PRINT) {
                    Sandwich.makeText(getApplicationContext(), (String)msg.obj, Sandwich.LENGTH_SHORT).show();
                }

                if(msg.what == SEND_LATLNG) {
                    getGPS(msg);
                }
            }
        };
        mHandler.sendEmptyMessage(RENEW_GPS);

        adapter = new ListAdapter(this, arrLL, R.layout.item_roomlist);
        list.setAdapter(adapter);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag) {
                    try {
                        IntentFilter mainFilter = new IntentFilter("com.guk2zzada.runnerswar");

                        registerReceiver(receiver, mainFilter);
                        startService(stepChecker);
                    } catch(Exception e) {
                        e.printStackTrace();
                        Sandwich.makeText(getApplicationContext(), e.getMessage(), Sandwich.LENGTH_SHORT).show();
                    }

                    if(gps == null) {
                        gps = new GPSTrackerOld(PlaySingleActivity.this, mHandler);
                    } else {
                        gps.Update();
                    }
                    btnStart.setText("중지");

                } else {
                    btnStart.setText("시작");

                    try {
                        unregisterReceiver(receiver);
                        stopService(stepChecker);
                        gps.stopUsingGPS();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }

                flag = !flag;
            }
        });
    }

    public void makeNewGpsService(){
        if(gps == null) {
            gps = new GPSTrackerOld(PlaySingleActivity.this, mHandler);
        } else {
            gps.Update();
        }
        //Sandwich.makeText(getApplicationContext(), "make GPS", Sandwich.LENGTH_SHORT).show();
        Log.e("GPS", "make GPS");
    }

    public void getGPS(Message msg) {
        // check if GPS enabled
        if(gps.canGetLocation()){
            Log.e("GPS", "getGPS");
            latitude = msg.getData().getDouble("lat");
            longitude = msg.getData().getDouble("lng");
            gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            arrLL.add(new LatLngList(latitude, longitude));
            adapter.notifyDataSetChanged();

            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(latitude, longitude));
            gMap.addMarker(marker);
            // \n is for new line
            double distance = gps.getDistance();
            //Sandwich.makeText(getApplicationContext(), "거리 = " + distance, Sandwich.LENGTH_SHORT).show();
            Sandwich.makeText(getApplicationContext(), "Lat: " + latitude + "\nLong: " + longitude, Sandwich.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
    }

    class PlayingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("PlayignReciever", "IN");
            serviceData = intent.getStringExtra("stepService");
            txtNum.setText(serviceData);
            //Sandwich.makeText(getApplicationContext(), "Playing Game", Sandwich.LENGTH_SHORT).show();
        }
    }

    private class PlaceHolder {
        TextView txtID;
        TextView txtEntry1;
        TextView txtEntry2;
        TextView txtScore;
    }

    private class ListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<LatLngList> array;

        int layout;

        public ListAdapter(Context context, ArrayList<LatLngList> array, int layout) {
            this.context = context;
            this.array = array;
            this.layout = layout;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final PlaceHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);

                holder = new PlaceHolder();
                holder.txtID = convertView.findViewById(R.id.txtID);
                holder.txtEntry1 = convertView.findViewById(R.id.txtEntry1);
                holder.txtEntry2 = convertView.findViewById(R.id.txtEntry2);
                holder.txtScore = convertView.findViewById(R.id.txtScore);

                convertView.setTag(holder);
            } else {
                holder = (PlaceHolder) convertView.getTag();
            }

            holder.txtID.setText("" + (position + 1));
            holder.txtEntry1.setText("" + array.get(position).latitude);
            holder.txtEntry2.setText("" + array.get(position).longitude);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(array.get(position).latitude, array.get(position).longitude)));
                }
            });

            return convertView;
        }
    }

    class LatLngList {
        double latitude;
        double longitude;

        public LatLngList(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

    }
}
