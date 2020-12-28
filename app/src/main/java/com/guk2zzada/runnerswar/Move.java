package com.guk2zzada.runnerswar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Move extends Dialog {

    private Move moveActivity;
    private Context context;
    private Class<?> cls;

    Move(@NonNull Context context) {
        super(context);
        this.context = context;
        this.moveActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Animation animSpinLeft = AnimationUtils.loadAnimation(context, R.anim.spin_anim_left);
        Animation animSpinRignt = AnimationUtils.loadAnimation(context, R.anim.spin_anim_right);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_loading);

        ImageView imgLoading1 = this.findViewById(R.id.imgLoading1);
        ImageView imgLoading2 = this.findViewById(R.id.imgLoading2);

        imgLoading1.setAnimation(animSpinLeft);
        imgLoading2.setAnimation(animSpinRignt);
    }

    public void startActivity(Class<?> cls) {
        this.cls = cls;

        moveActivity.setCancelable(false);
        moveActivity.show();

        new MoveAsync().execute();
    }

    class MoveAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            moveActivity.dismiss();
            context.startActivity(new Intent(context, cls));
        }
    }
}
