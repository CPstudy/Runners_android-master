package com.guk2zzada.runnerswar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    SQLiteDatabase sqlDB;

    DatabaseManager(Context context) {
        super(context, "runners.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tbl_single (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "time_week TEXT, " +
                "time_date TEXT," +
                "time_start TEXT," +
                "time_end TEXT," +
                "distance INTEGER," +
                "location TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_single");
        onCreate(db);
    }

    public ArrayList<SingleList> selectAllData() {
        ArrayList<SingleList> array = new ArrayList<>();

        sqlDB = this.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT _id, time_week, time_date, time_start, time_end, distance, location FROM tbl_single ORDER BY time_start DESC;", null);

        while(cursor.moveToNext()) {
            array.add(new SingleList(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)
            ));
        }

        cursor.close();

        return array;
    }

    public ArrayList<SingleList> selectDate(String start, String end) {
        sqlDB = this.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM tbl_single WHERE time_date BETWEEN '" + start + "' AND '" + end + "' ORDER BY time_start DESC;", null);

        ArrayList<SingleList> array = new ArrayList<>();

        while(cursor.moveToNext()) {
            array.add(new SingleList(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)
            ));
            Log.e("DataBase", cursor.getString(2));
        }

        cursor.close();

        return array;
    }

    public String getFirstDate() {
        sqlDB = this.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT MIN(time_date) FROM tbl_single", null);

        String str = "";
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }
        cursor.close();

        return str;
    }

    public String getLastDate() {
        sqlDB = this.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT MAX(time_date) FROM tbl_single", null);

        String str = "";
        while(cursor.moveToNext()) {
            str = cursor.getString(0);
        }
        cursor.close();

        return str;
    }

    public void insertData(String week, String date, String start, String end, int distance, String location) {
        sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO tbl_single (time_week, time_date, time_start, time_end, distance, location) VALUES ('" +
                week + "', '" + date + "', '" + start + "', '" + end + "', " + distance + ", '" + location + "');");
    }

    public void updateData(int id, String week, String date, String start, String end, int distance, String location) {
        sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("UPDATE tbl_single SET " +
                "time_week = '" + week + "', " +
                "time_date = '" + date + "', " +
                "time_start = '" + start + "', " +
                "time_end = '" + end + "', " +
                "distance = " + distance + " " +
                "WHERE _id = " + id + ";");
    }

    public void removeData(int id) {
        sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("DELETE FROM tbl_single WHERE _id = " + id + ";");
    }

    public void removeAll() {
        sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("DELETE FROM tbl_single");
    }

    public int getSumOfDistance(String day) {
        sqlDB = this.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT SUM(distance) FROM tbl_single WHERE time_start = '" + day + "';", null);

        int sum = 0;

        while(cursor.moveToNext()) {
            sum = cursor.getInt(0);
        }
        cursor.close();

        return sum;
    }
}
