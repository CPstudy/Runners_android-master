package com.guk2zzada.runnerswar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class ModeMultiActivity extends AppCompatActivity {

    Animation animIn, animOut, animSlideOut, animSlideIn;

    Button btnStart;
    View card;

    boolean boolStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_multi);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        btnStart = (Button) findViewById(R.id.btnStart);
        card = findViewById(R.id.card);

        animIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_in);
        animIn.setStartOffset(500);
        animOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(boolStart) {
                    Move move = new Move(ModeMultiActivity.this);
                    move.startActivity(MatchingActivity.class);
                    overridePendingTransition(R.anim.fade_in, 0);
                } else {
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animSlideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
        animSlideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolStart = true;
                card.startAnimation(animOut);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            card.startAnimation(animIn);
        }
    }

    @Override
    public void onBackPressed() {
        boolStart = false;
        card.startAnimation(animOut);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }
}
