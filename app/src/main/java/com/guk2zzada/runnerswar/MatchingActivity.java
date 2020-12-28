package com.guk2zzada.runnerswar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.util.StringUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchingActivity extends AppCompatActivity {

    final int SCORE = 2000;

    RequestQueue queue;
    ServerManager sm;
    ArrayList<RoomList> arrRoom = new ArrayList<>();
    RoomList selRoom;
    ImageView imgShadow;

    boolean bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        queue = Volley.newRequestQueue(getApplicationContext());
        Animation animTransRight = AnimationUtils.loadAnimation(this, R.anim.zoom_exit);
        imgShadow = findViewById(R.id.imgShadow);

        imgShadow.startAnimation(animTransRight);

        sm = new ServerManager(MatchingActivity.this);

        //getList(getApplicationContext());

        Log.e("userID", GlobalVar.id);
        sm.getAllList(true);
        //myAsync.execute("");
        Log.e("get", "finish");

        //myAsync.execute("");

    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.hold, R.anim.fade_out);
    }
}
