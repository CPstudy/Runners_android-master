package com.guk2zzada.runnerswar;

public class SingleList {
    int _id;
    String timeWeek;
    String timeDate;
    String timeStart;
    String timeEnd;
    int distance;
    String location;

    public SingleList(int _id, String timeWeek, String timeDate, String timeStart, String timeEnd, int distance, String location) {
        this._id = _id;
        this.timeWeek = timeWeek;
        this.timeDate = timeDate;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.distance = distance;
        this.location = location;
    }
}
