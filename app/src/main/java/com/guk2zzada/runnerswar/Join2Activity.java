package com.guk2zzada.runnerswar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

/**
 *
 * 사용 안 하는 액티비티
 *
 */

public class Join2Activity extends AppCompatActivity {

    Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    ViewFlipper viewFlipper;
    Button btnPrev, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevPage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });

    }

    public void prevPage() {
        viewFlipper.setInAnimation(slide_in_left);
        viewFlipper.setOutAnimation(slide_out_right);
        viewFlipper.showPrevious();
        setButtonEnabled();
    }

    public void nextPage() {
        if(viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {
            finish();
        } else {
            viewFlipper.setInAnimation(slide_in_right);
            viewFlipper.setOutAnimation(slide_out_left);
            viewFlipper.showNext();
            setButtonEnabled();
        }
    }

    public void setButtonEnabled() {
        btnPrev.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setText("다음");

        if(viewFlipper.getDisplayedChild() == 0) {
            btnPrev.setVisibility(View.INVISIBLE);
        } else if(viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {;
            btnNext.setText("닫기");
        }
    }
}
