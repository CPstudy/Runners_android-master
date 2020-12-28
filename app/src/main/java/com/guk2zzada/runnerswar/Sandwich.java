package com.guk2zzada.runnerswar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2017. 8. 3..
 */

public class Sandwich {

    final static int LENGTH_SHORT = 0;
    final static int LENGTH_LONG = 1;

    private Context mContext;
    private View view;
    private int mDuration = Toast.LENGTH_LONG;

    public Sandwich(Context con, View v, int duration) {
        mContext = con;
        view = v;
        mDuration = duration;
    }

    public static Sandwich makeText(Context context, String message, int duration) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.toast_layout, null);
        TextView text = layout.findViewById(R.id.txt_toast);
        text.setText(message);

        return new Sandwich(context, layout, duration);

    }

    public void show() {
        Toast toast = new Toast(mContext);
        toast.setDuration(mDuration);
        toast.setView(view);
        toast.show();
    }

    // 좌표 설정 가능한 부분
    public void show(int gravity, int xOffset, int yOffset) {
        Toast toast = new Toast(mContext);
        toast.setDuration(mDuration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setView(view);
        toast.show();
    }
}
