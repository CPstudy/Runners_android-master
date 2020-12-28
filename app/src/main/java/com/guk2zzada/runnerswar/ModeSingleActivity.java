package com.guk2zzada.runnerswar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class ModeSingleActivity extends AppCompatActivity {


    Animation animIn, animOut, animSlideOut, animSlideIn;

    View card;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_single);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        card = findViewById(R.id.card);
        btnStart = findViewById(R.id.btnStart);

        animIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_in);
        animIn.setStartOffset(500);
        animOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animSlideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
        animSlideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);

        card.startAnimation(animIn);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaySingleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        card.startAnimation(animOut);
    }
}
