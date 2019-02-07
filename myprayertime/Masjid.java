package com.molate.myprayertime;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Masjid implements Parcelable {
    private String id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private Calendar c;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private int sec;
    private String lastUpdate;
    private CurrentPrayerTime currentPrayerTime;

    public Masjid(String id, String name, String address, double latitude, double longitude,
                  String fajrTime, String dhuhrTime, String asrTime, String maghribTime, String ishaTime,String lastUpdate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        setCurrentPrayerTime(fajrTime,dhuhrTime,asrTime,maghribTime,ishaTime);
        this.lastUpdate = lastUpdate;

    }

    public Masjid() {

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        sec = c.get(Calendar.SECOND);

        String d = day + "";
        String mo = month + "";
        String h = hour + "";
        String m = min + "";
        String s = sec + "";

        if (d.length() == 1) {
            d = "0" + d;
        }
        if (mo.length() == 1) {
            mo = "0" + mo;
        }
        if (h.length() == 1) {
            h = "0" + h;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        if (s.length() == 1) {
            s = "0" + s;
        }

        lastUpdate = "Last Update: " + d + "-" + mo + "-" + year + " at " + h + ":" + m + ":" + s;

    }

    protected Masjid(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Masjid> CREATOR = new Creator<Masjid>() {
        @Override
        public Masjid createFromParcel(Parcel in) {
            return new Masjid(in);
        }

        @Override
        public Masjid[] newArray(int size) {
            return new Masjid[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public CurrentPrayerTime getCurrentPrayerTime() {
        return currentPrayerTime;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setCurrentPrayerTime(String fajrTime, String dhuhrTime, String asrTime, String maghribTime, String ishaTime) {
        currentPrayerTime = new CurrentPrayerTime(fajrTime,dhuhrTime,asrTime,maghribTime,ishaTime);
        currentPrayerTime.calculateNextMasjidPrayer();
        currentPrayerTime.setNextPrayerTime(currentPrayerTime.getNextPrayer());
        currentPrayerTime.setAmpmNextPrayer(currentPrayerTime.getNextPrayerTime());
        currentPrayerTime.setRemainingPhrase();
        currentPrayerTime.setNextPrayerPhrase();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
