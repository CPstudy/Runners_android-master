package com.guk2zzada.runnerswar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.Locale;

/**
 * Created by user on 2018-04-01.
 */

public class LineChart extends View {

    final static int STYLE_ORANGE = 0;
    final static int STYLE_BLUE = 1;

    Paint paint;
    DisplayMetrics dm;

    int viewWidth;
    int viewHeight;
    int paddingTop;
    int paddingBottom;
    int paddingLeft;
    int paddingRight;

    int num = 7;
    float chartWidth;
    float chartMargin;
    float ratio = 0.8f;

    String[] arrDate = {"4/1", "4/2", "4/3", "4/4", "4/5", "4/6", "4/7"};
    float[] size = {25.3f, 30.0f, 50.0f, 78.4f, 63.2f, 41.7f, 14.8f};
    float[] percent;
    float max;

    NinePatch sc;

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs) {

        Bitmap mBubbleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.high_chartbar_orange);
        sc = new NinePatch(mBubbleBitmap, mBubbleBitmap.getNinePatchChunk(), "");

        paint = new Paint();
        paint.setColor(Color.WHITE);

        setSize();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();

        viewWidth = w - paddingLeft - paddingRight;
        viewHeight = h - paddingTop - paddingBottom;

        chartWidth = (viewWidth / num) * ratio;
        chartMargin = (viewWidth - (chartWidth * num)) / (num - 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float m = (float)paddingLeft;
        float left;
        float height;

        Drawable drawable = getBackground();
        if(drawable != null) {
            drawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        for(int i = 0; i < num; i++) {
            left = m + chartWidth * i;
            height = viewHeight * percent[i];
            Rect rect = new Rect((int)left, (int)height + paddingTop, (int)(left + chartWidth), viewHeight + paddingBottom);
            sc.draw(canvas, rect, paint);
            m += chartMargin;
        }

        super.onDraw(canvas);
    }

    private void setSize() {
        percent = new float[num];

        max = size[0];

        for(int i = 1; i < num; i++) {
            if (max < size[i]) {
                max = size[i];
            }
        }

        for(int i = 0; i < num; i++) {
            size[i] = max - size[i];
            percent[i] = (size[i] / max);
        }
    }

    public void setData(float[] array) {
        size = array;
        setSize();
        invalidate();
    }

    public void setData(int[] array) {
        float[] farray = new float[7];
        farray[0] = (float)array[0];
        farray[1] = (float)array[1];
        farray[2] = (float)array[2];
        farray[3] = (float)array[3];
        farray[4] = (float)array[4];
        farray[5] = (float)array[5];
        farray[6] = (float)array[6];

        size = farray;
        setSize();
        invalidate();
    }

    public void setChartStyle(Context context, int style) {
        Bitmap bitmap;

        switch(style) {
            case STYLE_ORANGE:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.high_chartbar_orange);
                sc = new NinePatch(bitmap, bitmap.getNinePatchChunk(), "");
                break;

            case STYLE_BLUE:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.high_chartbar_blue);
                sc = new NinePatch(bitmap, bitmap.getNinePatchChunk(), "");
                break;

            default:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.high_chartbar_orange);
                sc = new NinePatch(bitmap, bitmap.getNinePatchChunk(), "");
                break;
        }

        invalidate();
    }

}
