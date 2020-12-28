package com.guk2zzada.runnerswar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

public class CreateActivity extends AppCompatActivity {

    View idCard;
    TextView txtCardID, txtCardInfo;
    Animation animSlideUp;

    String strID;
    String strHeight;
    String strWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        idCard = findViewById(R.id.idCard);
        txtCardID = idCard.findViewById(R.id.txtName);
        txtCardInfo = idCard.findViewById(R.id.txtInfo);
        animSlideUp = AnimationUtils.loadAnimation(getBaseContext(), R.anim.card_popup);

        strID = GlobalVar.id;
        strHeight = GlobalVar.height;
        strWeight = GlobalVar.weight;
        txtCardID.setText(strID);
        txtCardInfo.setText(strHeight + "cm/" + strWeight + "kg");

        idCard.startAnimation(animSlideUp);
        animSlideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                doAnimation();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Sandwich.makeText(getApplicationContext(), "회원가입이 완료되었습니다!", Sandwich.LENGTH_LONG).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }

    public void doAnimation() {
        ParticleSystem ps = new ParticleSystem(this, 500, R.drawable.star_yellow, 800);
        ps.setScaleRange(0.0f, 2.0f);
        ps.setSpeedRange(0.1f, 0.25f);
        ps.setAcceleration(0.0001f, 90);
        ps.setRotationSpeedRange(0, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(idCard, 500);
    }
}
