package com.guk2zzada.runnerswar;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
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

public class TestviewpagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    SliderAdapter adapter;
    DatabaseManager dm;
    DateManager dateManager;

    ArrayList<DateList> arrayList = new ArrayList<>();
    ArrayList<SingleList> arrSingle = new ArrayList<>();

    Button btnDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testviewpager);

        viewPager = findViewById(R.id.viewPager);
        btnDB = findViewById(R.id.btnDB);

        dm = new DatabaseManager(getApplicationContext());
        dateManager = new DateManager();

        try {
            arrayList = dateManager.getAllDate(dm.getFirstDate(), dm.getLastDate());
        } catch(Exception e) {
            e.printStackTrace();
        }

        adapter = new SliderAdapter(this);
        viewPager.setAdapter(adapter);

        btnDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Move move = new Move(TestviewpagerActivity.this);
                move.startActivity(DataActivity.class);
            }
        });
    }

    private void setChartSettings(BarChart ...charts) {
        for(BarChart chart : charts) {
            chart.setTouchEnabled(false);
            chart.setDrawBarShadow(false);
            chart.setDrawValueAboveBar(false);
            chart.setMaxVisibleValueCount(60);
            chart.setPinchZoom(false);
            chart.animateY(1000);
            chart.setDoubleTapToZoomEnabled(false);
            chart.setScaleXEnabled(false);
            chart.setScaleYEnabled(false);
            chart.getDescription().setEnabled(false);

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setEnabled(false);

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setEnabled(false);

            XAxis xAxis = chart.getXAxis();
            xAxis.setEnabled(true);
            xAxis.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorText));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisLineColor(ContextCompat.getColor(getApplicationContext(), R.color.axisLine));

            Legend legend = chart.getLegend();
            legend.setEnabled(false);
        }
    }

    private void setData(BarChart chart, final int position) {

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        String today = arrSingle.get(position).timeDate;
        int startDate = Integer.parseInt(dateManager.getDate(today));

        yVals1.add(new BarEntry(startDate, dm.getSumOfDistance(dateManager.getNextDay(today, 0))));
        yVals1.add(new BarEntry(startDate + 1, dm.getSumOfDistance(dateManager.getNextDay(today, 1))));
        yVals1.add(new BarEntry(startDate + 2, dm.getSumOfDistance(dateManager.getNextDay(today, 2))));
        yVals1.add(new BarEntry(startDate + 3, dm.getSumOfDistance(dateManager.getNextDay(today, 3))));
        yVals1.add(new BarEntry(startDate + 4, dm.getSumOfDistance(dateManager.getNextDay(today, 4))));
        yVals1.add(new BarEntry(startDate + 5, dm.getSumOfDistance(dateManager.getNextDay(today, 5))));
        yVals1.add(new BarEntry(startDate + 6, dm.getSumOfDistance(dateManager.getNextDay(today, 6))));

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            set1.setDrawIcons(false);

            set1.setColors(Color.argb(125, 255, 255, 255));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorText));
            data.setDrawValues(false);
            data.setBarWidth(0.7f);

            chart.setData(data);
            if(chart.getData() != null) {
                chart.getData().setHighlightEnabled(false);
            }
        }
    }

    class SliderAdapter extends PagerAdapter {

        Context context;
        LayoutInflater inflater;

        public SliderAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (FrameLayout) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.slide_layout, container, false);

            TextView txtDate = view.findViewById(R.id.txtDate);

            BarChart chart = view.findViewById(R.id.barChart);

            String start = dateManager.getDateFormat(arrayList.get(position).start);
            String end = dateManager.getDateFormat(arrayList.get(position).end);

            Log.e("ViewPager", arrayList.get(position).start + " / " + arrayList.get(position).end);

            arrSingle = dm.selectDate(arrayList.get(position).start, arrayList.get(position).end);
            setChartSettings(chart);
            setData(chart, position);

            txtDate.setText(start + " ~ " + end);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((FrameLayout) object);

        }
    }
}
