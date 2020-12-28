package com.guk2zzada.runnerswar;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.lang.ref.WeakReference;

public class JoinActivity extends AppCompatActivity {

    SendAsyncTask sendAsyncTask;
    Animation animIn, animOut, animSlideOut, animSlideIn;

    Button btnSubmit;
    EditText edtID, edtPW1, edtPW2, edtBirth, edtHeight, edtWeight;
    RadioButton rdoMale, rdoFemale;
    RadioGroup rdoGrp;
    View cardJoin;

    private static String strID;
    private static String strPW;
    private static String strBirth;
    private static String strHeight;
    private static String strWeight;
    private static int gender;
    private static boolean boolSubmit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        cardJoin = findViewById(R.id.cardJoin);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        edtID = (EditText) findViewById(R.id.edtID);
        edtPW1 = (EditText) findViewById(R.id.edtPW1);
        edtPW2 = (EditText) findViewById(R.id.edtPW2);
        edtBirth = (EditText) findViewById(R.id.edtBirth);
        edtHeight = (EditText) findViewById(R.id.edtHeight);
        edtWeight = (EditText) findViewById(R.id.edtWeight);
        rdoMale = (RadioButton) findViewById(R.id.rdoMale);
        rdoFemale = (RadioButton) findViewById(R.id.rdoFemale);
        rdoGrp = (RadioGroup) findViewById(R.id.rdoGrp);

        animIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_in);
        animOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.card_out);
        animSlideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
        animSlideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);

        cardJoin.startAnimation(animIn);
        btnSubmit.startAnimation(animSlideIn);

        // 전송 버튼
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setData()) {
                    //sendAsyncTask = new SendAsyncTask(JoinActivity.this);
                    //sendAsyncTask.execute(strID, strPW, strBirth, strWeight, strHeight, String.valueOf(gender));
                    cardJoin.startAnimation(animOut);
                    btnSubmit.startAnimation(animSlideOut);
                    boolSubmit = true;
                }
            }
        });

        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(boolSubmit) {
                    Intent intent = new Intent(getBaseContext(), CreateActivity.class);
                    GlobalVar.id = strID;
                    GlobalVar.height = strHeight;
                    GlobalVar.weight = strWeight;
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //sendAsyncTask.cancel(true);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        cardJoin.startAnimation(animOut);
        btnSubmit.startAnimation(animSlideOut);
    }

    public boolean setData() {
        if(!edtID.getText().toString().equals("")) {
            strID = edtID.getText().toString();
        } else {
            Sandwich.makeText(getBaseContext(), "아이디를 입력해주세요.", Sandwich.LENGTH_SHORT).show();
            return false;
        }

        if(!edtPW1.getText().toString().equals("")) {
            strPW = edtPW1.getText().toString();
            if(!strPW.equals(edtPW2.getText().toString())) {
                Sandwich.makeText(getBaseContext(), "암호가 다릅니다.", Sandwich.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Sandwich.makeText(getBaseContext(), "암호를 입력해주세요.", Sandwich.LENGTH_SHORT).show();
            return false;
        }

        rdoGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdoMale:
                        gender = 0;
                        break;

                    case R.id.rdoFemale:
                        gender = 1;
                        break;
                }
            }
        });

        if(!edtBirth.getText().toString().equals("")) {
            strBirth = edtBirth.getText().toString();
        } else {
            Sandwich.makeText(getBaseContext(), "생년월일을 입력해주세요.", Sandwich.LENGTH_SHORT).show();
            return false;
        }

        if(!edtHeight.getText().toString().equals("")) {
            strHeight = edtHeight.getText().toString();
        } else {
            Sandwich.makeText(getBaseContext(), "키를 입력해주세요.", Sandwich.LENGTH_SHORT).show();
            return false;
        }

        if(!edtWeight.getText().toString().equals("")) {
            strWeight = edtWeight.getText().toString();
        } else {
            Sandwich.makeText(getBaseContext(), "몸무게를 입력해주세요.", Sandwich.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private static class SendAsyncTask extends AsyncTask<String, String, String> {

        private WeakReference<JoinActivity> activityWeakReference;
        Animation animOut;
        Animation animSlideOut;

        SendAsyncTask(JoinActivity context) {
            activityWeakReference = new WeakReference<>(context);
            animOut = AnimationUtils.loadAnimation(context, R.anim.card_out);
            animSlideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String pw = strings[1];
            String birth = strings[2];
            String height = strings[3];
            String weight = strings[4];
            int gender = Integer.parseInt(strings[5]);      // 0: 남자, 1: 여자

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JoinActivity activity = activityWeakReference.get();

            if(activity == null || activity.isFinishing())
                return;

            Button btnSubmit = (Button) activity.findViewById(R.id.btnSubmit);
            View cardJoin = activity.findViewById(R.id.cardJoin);

            /*
             *
             * code here
             *
             */

            cardJoin.startAnimation(animOut);
            btnSubmit.startAnimation(animSlideOut);
            boolSubmit = true;
        }
    }
}
