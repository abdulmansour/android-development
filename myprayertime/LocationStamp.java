package com.molate.myprayertime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationStamp {

    private String date;
    private double latitude;
    private double longitude;

    public void setDate() {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        date = format.format(today);
    }
    public String getDate() {
        return date;
    }
    public LocationStamp(double latitude, double longitude) {
        setDate();
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
