package com.guk2zzada.runnerswar;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DateManager {

    private Calendar cal;

    private int year;
    private int month;
    private int date;
    private int hour;
    private int min;
    private int sec;
    private int msec;
    private int dayOfWeek;
    private int week;

    DateManager() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        date = cal.get(Calendar.DATE);
        hour = cal.get(Calendar.HOUR);
        min = cal.get(Calendar.MINUTE);
        sec = cal.get(Calendar.SECOND);
        msec = cal.get(Calendar.MILLISECOND);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        week = cal.get(Calendar.WEEK_OF_MONTH);
    }

    public String getYear() {
        return String.format(Locale.KOREA, "%04d", year);
    }

    public String getYear(String day) {
        return day.substring(0, 4);
    }

    public String getMonth() {
        return String.format(Locale.KOREA, "%02d", month);
    }

    public String getMonth(String day) {
        return day.substring(4, 6);
    }

    public String getDate() {
        return String.format(Locale.KOREA, "%02d", date);
    }

    public String getDate(String day) {
        Log.e("getDate", day);
        return day.substring(6,8);
    }

    public String getHour() {
        return String.format(Locale.KOREA, "%02d", hour);
    }

    public String getMin() {
        return String.format(Locale.KOREA, "%02d", min);
    }

    public String getSec() {
        return String.format(Locale.KOREA, "%02d", sec);
    }

    public String getMsec() {
        return String.format(Locale.KOREA, "%02d", msec);
    }

    public String getDayOfWeek() {
        String[] array = {"일", "월", "화", "수", "목", "금", "토"};
        return array[dayOfWeek];
    }

    public int getWeek(){
        return week;

    }

    public int getWeekInMonths(String day) {

        int y = Integer.parseInt(day.substring(0, 4));
        int m = Integer.parseInt(day.substring(4, 6)) - 1;
        int d = Integer.parseInt(day.substring(6, 8));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DATE, d);

        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    // 특정 년,월,주 차에 일요일 구하기
    public String getSunday(String day){

        int y = Integer.parseInt(day.substring(0, 4));
        int m = Integer.parseInt(day.substring(4, 6)) - 1;
        int d = Integer.parseInt(day.substring(6, 8));
        int w = getWeekInMonths(day);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.WEEK_OF_MONTH, w);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(calendar.DATE,0);

        Log.e("Sunday", "" + m);
        Log.e("Sunday", "cal = " + (calendar.get(Calendar.MONTH) + 1));

        return String.format(Locale.KOREA, "%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));

    }

    public String getNextWeek(String day) {
        int y = Integer.parseInt(day.substring(0, 4));
        int m = Integer.parseInt(day.substring(4, 6)) - 1;
        int d = Integer.parseInt(day.substring(6, 8));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DATE, d);
        calendar.add(Calendar.DATE, +7);

        Log.e("Next Week", "day = " + day + " / m = " + m);
        Log.e("Next Week", "" + (calendar.get(Calendar.MONTH) + 1));

        return String.format(Locale.KOREA, "%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
    }

    public String getNextDay(String day, int amount) {
        int y = Integer.parseInt(day.substring(0, 4));
        int m = Integer.parseInt(day.substring(4, 6)) - 1;
        int d = Integer.parseInt(day.substring(6, 8));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DATE, d);
        calendar.add(Calendar.DATE, amount);

        Log.e("Next Week", "day = " + day + " / m = " + m);
        Log.e("Next Week", "" + (calendar.get(Calendar.MONTH) + 1));

        return String.format(Locale.KOREA, "%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
    }

    public String getSaturday(String day) {
        int y = Integer.parseInt(day.substring(0, 4));
        int m = Integer.parseInt(day.substring(4, 6)) - 1;
        int d = Integer.parseInt(day.substring(6, 8));

        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m, d);
        calendar.add(Calendar.DATE, +6);

        return String.format(Locale.KOREA, "%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
    }

    // 날짜 모두 구하기
    public ArrayList<DateList> getAllDate(String start, String end) {
        ArrayList<DateList> array = new ArrayList<>();

        String strStart = getSunday(start);
        String strEnd = getSunday(end);

        Log.e("Sunday", "strStart = " + strStart);

        while(Integer.parseInt(strStart) <= Integer.parseInt(strEnd)) {
            array.add(new DateList(strStart, getSaturday(strStart)));
            strStart = getNextWeek(strStart);
        }

        for(int i = 0; i < array.size(); i++) {
            Log.e("Week", "start = " + array.get(i).start + " / end = " + array.get(i).end);
        }

        return array;
    }

    public String getDateFormat(String day) {
        int y = Integer.parseInt(day.substring(0, 4));
        int m = Integer.parseInt(day.substring(4, 6));
        int d = Integer.parseInt(day.substring(6, 8));

        return String.format(Locale.KOREA, "%04d년 %d월 %d일", y, m, d);
    }
}
