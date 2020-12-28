package com.guk2zzada.runnerswar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MatchedActivity extends AppCompatActivity {

    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayMultiActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.hold);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.hold, R.anim.fade_out);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
