package com.molate.myprayertime;

import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class CurrentPrayerTime implements Parcelable {

    private static final String TAG = CurrentPrayerTime.class.getSimpleName();
    private String fajrTime;
    private String sunriseTime;
    private String dhuhrTime;
    private String asrTime;
    private String maghribTime;
    private String ishaTime;
    private String nextPrayer;
    private String nextPrayerTime;
    private String nextPrayerPhrase;
    private int hoursRemaining;
    private int minutesRemaining;
    private String ampmNextPrayer;
    private String remainingPhrase;

    public CurrentPrayerTime(String fajrTime, String sunriseTime, String dhuhrTime, String asrTime,
                             String maghribTime, String ishaTime, String nextPrayer,
                             String nextPrayerTime, int hoursRemaining, int minutesRemaining, String ampmNextPrayer,
                             String remainingPhrase, String nextPrayerPhrase) {
        this.fajrTime = fajrTime;
        this.sunriseTime = sunriseTime;
        this.dhuhrTime = dhuhrTime;
        this.asrTime = asrTime;
        this.maghribTime = maghribTime;
        this.ishaTime = ishaTime;
        this.nextPrayer = nextPrayer;
        this.nextPrayerTime = nextPrayerTime;
        this.hoursRemaining = hoursRemaining;
        this.minutesRemaining = minutesRemaining;
        this.ampmNextPrayer = ampmNextPrayer;
        this.remainingPhrase = remainingPhrase;
        this.nextPrayerPhrase = nextPrayerPhrase;
    }

    public CurrentPrayerTime(String fajrTime, String dhuhrTime, String asrTime, String maghribTime, String ishaTime) {
        this.fajrTime = fajrTime;
        this.dhuhrTime = dhuhrTime;
        this.asrTime = asrTime;
        this.maghribTime = maghribTime;
        this.ishaTime = ishaTime;
    }

    public CurrentPrayerTime(String fajrTime,String sunriseTime, String dhuhrTime, String asrTime, String maghribTime, String ishaTime) {
        this.fajrTime = fajrTime;
        this.sunriseTime = sunriseTime;
        this.dhuhrTime = dhuhrTime;
        this.asrTime = asrTime;
        this.maghribTime = maghribTime;
        this.ishaTime = ishaTime;
    }

    public CurrentPrayerTime() {
    }

    protected CurrentPrayerTime(Parcel in) {
        fajrTime = in.readString();
        sunriseTime = in.readString();
        dhuhrTime = in.readString();
        asrTime = in.readString();
        maghribTime = in.readString();
        ishaTime = in.readString();
        nextPrayer = in.readString();
        nextPrayerTime = in.readString();
        nextPrayerPhrase = in.readString();
        hoursRemaining = in.readInt();
        minutesRemaining = in.readInt();
        ampmNextPrayer = in.readString();
        remainingPhrase = in.readString();
    }

    public static final Creator<CurrentPrayerTime> CREATOR = new Creator<CurrentPrayerTime>() {
        @Override
        public CurrentPrayerTime createFromParcel(Parcel in) {
            return new CurrentPrayerTime(in);
        }

        @Override
        public CurrentPrayerTime[] newArray(int size) {
            return new CurrentPrayerTime[size];
        }
    };

    public String getFajrTime() {
        return fajrTime;
    }

    public void setFajrTime(String fajrTime) {
        this.fajrTime = fajrTime;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getDhuhrTime() {
        return dhuhrTime;
    }

    public void setDhuhrTime(String dhuhrTime) {
        this.dhuhrTime = dhuhrTime;
    }

    public String getAsrTime() {
        return asrTime;
    }

    public void setAsrTime(String asrTime) {
        this.asrTime = asrTime;
    }

    public String getMaghribTime() {
        return maghribTime;
    }

    public void setMaghribTime(String maghribTime) {
        this.maghribTime = maghribTime;
    }

    public String getIshaTime() {
        return ishaTime;
    }

    public void setIshaTime(String ishaTime) {
        this.ishaTime = ishaTime;
    }

    public String getNextPrayer() {
        return nextPrayer;
    }

    public void calculateNextPrayer() {
        Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int min = c.get(Calendar.MINUTE);
        int i = 0;

        i = getFajrTime().indexOf(":");
        int fajrHour = Integer.parseInt(getFajrTime().substring(0,i));
        int fajrMin = Integer.parseInt(getFajrTime().substring(3,5));

        i = getSunriseTime().indexOf(":");
        int sunriseHour = Integer.parseInt(getSunriseTime().substring(0,i));
        int sunriseMin = Integer.parseInt(getSunriseTime().substring(3,5));

        i = getDhuhrTime().indexOf(":");
        int dhuhrHour = Integer.parseInt(getDhuhrTime().substring(0,i));
        int dhuhrMin = Integer.parseInt(getDhuhrTime().substring(3,5));

        i = getAsrTime().indexOf(":");
        int asrHour = Integer.parseInt(getAsrTime().substring(0,i));
        int asrMin = Integer.parseInt(getAsrTime().substring(3,5));

        i = getMaghribTime().indexOf(":");
        int maghribHour = Integer.parseInt(getMaghribTime().substring(0,i));
        int maghribMin = Integer.parseInt(getMaghribTime().substring(3,5));

        i = getIshaTime().indexOf(":");
        int ishaHour = Integer.parseInt(getIshaTime().substring(0,i));
        int ishaMin = Integer.parseInt(getIshaTime().substring(3,5));

        if (hour >= fajrHour && hour <= sunriseHour) {
            if(hour == fajrHour) {
                if(min > fajrMin) {
                    nextPrayer = "Sunrise";
                }
            }
            else if(hour == sunriseHour) {
                if (min < sunriseMin) {
                    nextPrayer = "Sunrise";
                }
            }
            else {
                nextPrayer = "Sunrise";
            }
            hoursRemaining = sunriseHour - hour;
            minutesRemaining = sunriseMin - min;
        }

        if (hour >= sunriseHour && hour <= dhuhrHour) {
            if(hour == sunriseHour) {
                if(min > sunriseMin) {
                    nextPrayer = "Dhuhr";
                }
            }
            else if(hour == dhuhrHour) {
                if (min < dhuhrMin) {
                    nextPrayer = "Dhuhr";
                }
            }
            else {
                nextPrayer = "Dhuhr";
            }
        }

        if (hour >= dhuhrHour && hour <= asrHour) {
            if(hour == dhuhrHour) {
                if(min > dhuhrMin) {
                    nextPrayer = "Asr";
                }
            }
            else if(hour == asrHour) {
                if (min < asrMin) {
                    nextPrayer = "Asr";
                }
            }
            else {
                nextPrayer = "Asr";
            }
        }

        if (hour >= asrHour && hour <= maghribHour) {
            if(hour == asrHour) {
                if(min > asrMin) {
                    nextPrayer = "Maghrib";
                }
            }
            else if(hour == maghribHour) {
                if (min < maghribMin) {
                    nextPrayer = "Maghrib";
                }
            }
            else {
                nextPrayer = "Maghrib";
            }
        }

        if (hour >= maghribHour && hour <= ishaHour) {
            if(hour == maghribHour) {
                if(min > maghribMin) {
                    nextPrayer = "Isha";
                }
            }
            else if(hour == ishaHour) {
                if (min < ishaMin) {
                    nextPrayer = "Isha";
                }
            }
            else {
                nextPrayer = "Isha";
            }
        }

        if (hour >= ishaHour || hour <= fajrHour) { //SOLVED: strictly or, can't be and
            if (hour == ishaHour) {
                if (min > ishaMin) {
                    nextPrayer = "Fajr";
                }
            } else if (hour == fajrHour) {
                if (min < fajrMin) {
                    nextPrayer = "Fajr";
                }
            } else {
                nextPrayer = "Fajr";
            }
        }

        if (nextPrayer == "Sunrise") {
            if (min > sunriseMin) {
                setMinutesRemaining(60 - min + sunriseMin);
                setHoursRemaining(sunriseHour - hour - 1);
            } else {
                setMinutesRemaining(sunriseMin - min);
                setHoursRemaining(sunriseHour - hour);
            }
        }
        if (nextPrayer == "Dhuhr") {
            if (min > dhuhrMin) {
                setMinutesRemaining(60 - min + dhuhrMin);
                setHoursRemaining(dhuhrHour - hour - 1);
            } else {
                setMinutesRemaining(dhuhrMin - min);
                setHoursRemaining(dhuhrHour - hour);
            }
        }
        if (nextPrayer == "Asr") {
            if (min > asrMin) {
                setMinutesRemaining(60 - min + asrMin);
                setHoursRemaining(asrHour - hour - 1);
            } else {
                setMinutesRemaining(asrMin - min);
                setHoursRemaining(asrHour - hour);
            }
        }
        if (nextPrayer == "Maghrib") {
            if (min > maghribMin) {
                setMinutesRemaining(60 - min + maghribMin);
                setHoursRemaining(maghribHour - hour - 1);
            } else {
                setMinutesRemaining(maghribMin - min);
                setHoursRemaining(maghribHour - hour);
            }
        }
        if (nextPrayer == "Isha") {
            if (min > ishaMin) {
                setMinutesRemaining(60 - min + ishaMin);
                setHoursRemaining(ishaHour - hour - 1);
            } else {
                setMinutesRemaining(ishaMin - min);
                setHoursRemaining(ishaHour - hour);
            }
        }
        if (nextPrayer == "Fajr") {
            if (min > fajrMin) {
                setMinutesRemaining(60 - min + fajrMin);
                setHoursRemaining(fajrHour - hour - 1);
            } else {
                setMinutesRemaining(fajrMin - min);
                setHoursRemaining(fajrHour - hour);
            }
            if(hour > fajrHour) {
                setHoursRemaining(fajrHour+24 - hour);
            }
        }
    }

    public String getNextPrayerTime() {
        return nextPrayerTime;
    }

    public void setNextPrayerTime(String nextPrayer) {
        if (nextPrayer == "Sunrise") {
            nextPrayerTime = getSunriseTime();
        }
        if (nextPrayer == "Dhuhr") {
            nextPrayerTime = getDhuhrTime();
        }
        if (nextPrayer == "Asr") {
            nextPrayerTime = getAsrTime();
        }
        if (nextPrayer == "Maghrib") {
            nextPrayerTime = getMaghribTime();
        }
        if (nextPrayer == "Isha") {
            nextPrayerTime = getIshaTime();
        }
        if (nextPrayer == "Fajr") {
            nextPrayerTime = getFajrTime();
        }
    }

    public int getHoursRemaining() {
        return hoursRemaining;
    }

    public void setHoursRemaining(int hoursRemaining) {
        this.hoursRemaining = hoursRemaining;
    }

    public int getMinutesRemaining() {
        return minutesRemaining;
    }

    public void setMinutesRemaining(int minutesRemaining) {
        this.minutesRemaining = minutesRemaining;
    }
    public void setAmpmNextPrayer(String nextPrayerTime) {
        int nextPrayerHour = Integer.parseInt(nextPrayerTime.substring(0,2));
        if (nextPrayerHour < 12 ) {
            ampmNextPrayer = "AM";
        }
        else {
            ampmNextPrayer = "PM";
        }
    }

    public String getAmpmNextPrayer() {
        return ampmNextPrayer;
    }

    public String getRemainingPhrase() {
        return remainingPhrase;
    }

    public void setRemainingPhrase() {
        String hoursLabel, minutesLabel, remainingLabel;
        remainingLabel =  " remaining";
        if (hoursRemaining == 1 || hoursRemaining == 01) {
            hoursLabel = " hour & ";
        }
        else {
            hoursLabel = " hours & ";
        }

        if(minutesRemaining == 1 || minutesRemaining == 01) {
            minutesLabel = " minute";
        }
        else {
            minutesLabel = " minutes";
        }
        if (hoursRemaining == 0) {
            remainingPhrase = minutesRemaining + minutesLabel + remainingLabel;
        }
        else if (hoursRemaining != 0 && minutesRemaining == 0) {
            remainingPhrase = hoursRemaining + hoursLabel + remainingLabel;
        }
        else {
            remainingPhrase = hoursRemaining + hoursLabel + minutesRemaining + minutesLabel + remainingLabel;
        }
    }

    public void setNextPrayerPhrase () {
        nextPrayerPhrase = nextPrayer + " " + formatTime(nextPrayerTime) + " " + ampmNextPrayer;
    }

    public String getNextPrayerPhrase() {
        return nextPrayerPhrase;
    }

    private String formatTime (String unformattedTime) {
        String leftPortion = unformattedTime.substring(0, 2);
        String rightPortion = unformattedTime.substring(3, 5);
        int stringToInt = Integer.parseInt(leftPortion);
        if (stringToInt > 12) {
            stringToInt -= 12;
            if (stringToInt < 10) {
                leftPortion = "0" + stringToInt;
            }
            else {
                leftPortion = stringToInt + "";
            }
        }
        return (leftPortion + ":" + rightPortion);
    }

    public void calculateNextPrayerService() {
        Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int min = c.get(Calendar.MINUTE);
        int i = 0;

        i = getFajrTime().indexOf(":");
        if(i == 1){
            setFajrTime("0" + getFajrTime());
            i += 1;
        }
        int fajrHour = Integer.parseInt(getFajrTime().substring(0,i));
        int fajrMin = Integer.parseInt(getFajrTime().substring(3,5));

        i = getSunriseTime().indexOf(":");
        if(i == 1){
            setSunriseTime("0" + getSunriseTime());
            i += 1;
        }
        int sunriseHour = Integer.parseInt(getSunriseTime().substring(0,i));
        int sunriseMin = Integer.parseInt(getSunriseTime().substring(3,5));

        i = getDhuhrTime().indexOf(":");
        if(i == 1){
            setDhuhrTime("0" + getDhuhrTime());
            i += 1;
        }
        int dhuhrHour = Integer.parseInt(getDhuhrTime().substring(0,i));
        int dhuhrMin = Integer.parseInt(getDhuhrTime().substring(3,5));

        i = getAsrTime().indexOf(":");
        if(i == 1){
            setAsrTime("0" + getAsrTime());
            i += 1;
        }
        int asrHour = Integer.parseInt(getAsrTime().substring(0,i));
        int asrMin = Integer.parseInt(getAsrTime().substring(3,5));

        i = getMaghribTime().indexOf(":");
        if(i == 1){
            setMaghribTime("0" + getMaghribTime());
            i += 1;
        }
        int maghribHour = Integer.parseInt(getMaghribTime().substring(0,i));
        int maghribMin = Integer.parseInt(getMaghribTime().substring(3,5));

        i = getIshaTime().indexOf(":");
        if(i == 1){
            setIshaTime("0" + getIshaTime());
            i += 1;
        }
        int ishaHour = Integer.parseInt(getIshaTime().substring(0,i));
        int ishaMin = Integer.parseInt(getIshaTime().substring(3,5));

        if (hour >= fajrHour && hour <= sunriseHour) {
            if(hour == fajrHour) {
                if(min > fajrMin) {
                    nextPrayer = "Sunrise";
                }
            }
            else if(hour == sunriseHour) {
                if (min < sunriseMin) {
                    nextPrayer = "Sunrise";
                }
            }
            else {
                nextPrayer = "Sunrise";
            }
            hoursRemaining = sunriseHour - hour;
            minutesRemaining = sunriseMin - min;
        }


        if (hour >= sunriseHour && hour <= dhuhrHour) {
            if(hour == sunriseHour) {
                if(min > sunriseMin) {
                    nextPrayer = "Dhuhr";
                }
            }
            else if(hour == dhuhrHour) {
                if (min < dhuhrMin) {
                    nextPrayer = "Dhuhr";
                }
            }
            else {
                nextPrayer = "Dhuhr";
            }
            hoursRemaining = dhuhrHour - hour;
            minutesRemaining = dhuhrMin - min;
        }

        if (hour >= dhuhrHour && hour <= asrHour) {
            if(hour == dhuhrHour) {
                if(min > dhuhrMin) {
                    nextPrayer = "Asr";
                }
            }
            else if(hour == asrHour) {
                if (min < asrMin) {
                    nextPrayer = "Asr";
                }
            }
            else {
                nextPrayer = "Asr";
            }
        }

        if (hour >= asrHour && hour <= maghribHour) {
            if(hour == asrHour) {
                if(min > asrMin) {
                    nextPrayer = "Maghrib";
                }
            }
            else if(hour == maghribHour) {
                if (min < maghribMin) {
                    nextPrayer = "Maghrib";
                }
            }
            else {
                nextPrayer = "Maghrib";
            }
        }

        if (hour >= maghribHour && hour <= ishaHour) {
            if(hour == maghribHour) {
                if(min > maghribMin) {
                    nextPrayer = "Isha";
                }
            }
            else if(hour == ishaHour) {
                if (min < ishaMin) {
                    nextPrayer = "Isha";
                }
            }
            else {
                nextPrayer = "Isha";
            }
        }

        if (hour >= ishaHour || hour <= fajrHour) { //SOLVED: strictly or, can't be and
            if(hour == ishaHour) {
                if(min > ishaMin) {
                    nextPrayer = "Fajr";
                }
            }
            else if(hour == fajrHour) {
                if (min < fajrMin) {
                    nextPrayer = "Fajr";
                }
            }
            else {
                nextPrayer = "Fajr";
            }
        }

        if (nextPrayer == "Dhuhr") {
            if (min > dhuhrMin) {
                setMinutesRemaining(60 - min + dhuhrMin);
                setHoursRemaining(dhuhrHour - hour - 1);
            } else {
                setMinutesRemaining(dhuhrMin - min);
                setHoursRemaining(dhuhrHour - hour);
            }
        }
        if (nextPrayer == "Asr") {
            if (min > asrMin) {
                setMinutesRemaining(60 - min + asrMin);
                setHoursRemaining(asrHour - hour - 1);
            } else {
                setMinutesRemaining(asrMin - min);
                setHoursRemaining(asrHour - hour);
            }
        }
        if (nextPrayer == "Maghrib") {
            if (min > maghribMin) {
                setMinutesRemaining(60 - min + maghribMin);
                setHoursRemaining(maghribHour - hour - 1);
            } else {
                setMinutesRemaining(maghribMin - min);
                setHoursRemaining(maghribHour - hour);
            }
        }
        if (nextPrayer == "Isha") {
            if (min > ishaMin) {
                setMinutesRemaining(60 - min + ishaMin);
                setHoursRemaining(ishaHour - hour - 1);
            } else {
                setMinutesRemaining(ishaMin - min);
                setHoursRemaining(ishaHour - hour);
            }
        }
        if (nextPrayer == "Fajr") {
            if (min > fajrMin) {
                setMinutesRemaining(60 - min + fajrMin);
                setHoursRemaining(fajrHour - hour - 1);
            } else {
                setMinutesRemaining(fajrMin - min);
                setHoursRemaining(fajrHour - hour);
            }
            if (hour > fajrHour) {
                setHoursRemaining(fajrHour+24 - hour);
            }
        }
        if (nextPrayer == "Sunrise") {
            if (min > sunriseMin) {
                setMinutesRemaining(60 - min + sunriseMin);
                setHoursRemaining(sunriseHour - hour - 1);
            } else {
                setMinutesRemaining(sunriseMin - min);
                setHoursRemaining(sunriseHour - hour);
            }
        }
    }

    public void calculateNextMasjidPrayer() {
        Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int min = c.get(Calendar.MINUTE);
        int i = 0;

        i = getFajrTime().indexOf(":");
        if (i == 1) {
            setFajrTime("0" + getFajrTime());
            i += 1;
        }
        int fajrHour = Integer.parseInt(getFajrTime().substring(0, i));
        int fajrMin = Integer.parseInt(getFajrTime().substring(3, 5));

        i = getDhuhrTime().indexOf(":");
        if (i == 1) {
            setDhuhrTime("0" + getDhuhrTime());
            i += 1;
        }
        int dhuhrHour = Integer.parseInt(getDhuhrTime().substring(0, i));
        int dhuhrMin = Integer.parseInt(getDhuhrTime().substring(3, 5));

        i = getAsrTime().indexOf(":");
        if (i == 1) {
            setAsrTime("0" + getAsrTime());
            i += 1;
        }
        int asrHour = Integer.parseInt(getAsrTime().substring(0, i));
        int asrMin = Integer.parseInt(getAsrTime().substring(3, 5));

        i = getMaghribTime().indexOf(":");
        if (i == 1) {
            setMaghribTime("0" + getMaghribTime());
            i += 1;
        }
        int maghribHour = Integer.parseInt(getMaghribTime().substring(0, i));
        int maghribMin = Integer.parseInt(getMaghribTime().substring(3, 5));

        i = getIshaTime().indexOf(":");
        if (i == 1) {
            setIshaTime("0" + getIshaTime());
            i += 1;
        }
        int ishaHour = Integer.parseInt(getIshaTime().substring(0, i));
        int ishaMin = Integer.parseInt(getIshaTime().substring(3, 5));

        if (hour >= fajrHour && hour <= dhuhrHour) {
            if (hour == fajrHour) {
                if (min > fajrMin) {
                    nextPrayer = "Dhuhr";
                }
            } else if (hour == dhuhrHour) {
                if (min < dhuhrMin) {
                    nextPrayer = "Dhuhr";
                }
            } else {
                nextPrayer = "Dhuhr";
            }
            hoursRemaining = dhuhrHour - hour;
            minutesRemaining = dhuhrMin - min;
        }

        if (hour >= dhuhrHour && hour <= asrHour) {
            if (hour == dhuhrHour) {
                if (min > dhuhrMin) {
                    nextPrayer = "Asr";
                }
            } else if (hour == asrHour) {
                if (min < asrMin) {
                    nextPrayer = "Asr";
                }
            } else {
                nextPrayer = "Asr";
            }
        }

        if (hour >= asrHour && hour <= maghribHour) {
            if (hour == asrHour) {
                if (min > asrMin) {
                    nextPrayer = "Maghrib";
                }
            } else if (hour == maghribHour) {
                if (min < maghribMin) {
                    nextPrayer = "Maghrib";
                }
            } else {
                nextPrayer = "Maghrib";
            }
        }

        if (hour >= maghribHour && hour <= ishaHour) {
            if (hour == maghribHour) {
                if (min > maghribMin) {
                    nextPrayer = "Isha";
                }
            } else if (hour == ishaHour) {
                if (min < ishaMin) {
                    nextPrayer = "Isha";
                }
            } else {
                nextPrayer = "Isha";
            }
        }

        if (hour >= ishaHour || hour <= fajrHour) { //SOLVED: strictly or, can't be and
            if (hour == ishaHour) {
                if (min > ishaMin) {
                    nextPrayer = "Fajr";
                }
            } else if (hour == fajrHour) {
                if (min < fajrMin) {
                    nextPrayer = "Fajr";
                }
            } else {
                nextPrayer = "Fajr";
            }
        }

        if (nextPrayer == "Dhuhr") {
            if (min > dhuhrMin) {
                setMinutesRemaining(60 - min + dhuhrMin);
                setHoursRemaining(dhuhrHour - hour - 1);
            } else {
                setMinutesRemaining(dhuhrMin - min);
                setHoursRemaining(dhuhrHour - hour);
            }
        }
        if (nextPrayer == "Asr") {
            if (min > asrMin) {
                setMinutesRemaining(60 - min + asrMin);
                setHoursRemaining(asrHour - hour - 1);
            } else {
                setMinutesRemaining(asrMin - min);
                setHoursRemaining(asrHour - hour);
            }
        }
        if (nextPrayer == "Maghrib") {
            if (min > maghribMin) {
                setMinutesRemaining(60 - min + maghribMin);
                setHoursRemaining(maghribHour - hour - 1);
            } else {
                setMinutesRemaining(maghribMin - min);
                setHoursRemaining(maghribHour - hour);
            }
        }
        if (nextPrayer == "Isha") {
            if (min > ishaMin) {
                setMinutesRemaining(60 - min + ishaMin);
                setHoursRemaining(ishaHour - hour - 1);
            } else {
                setMinutesRemaining(ishaMin - min);
                setHoursRemaining(ishaHour - hour);
            }
        }
        if (nextPrayer == "Fajr") {
            if (min > fajrMin) {
                setMinutesRemaining(60 - min + fajrMin);
                setHoursRemaining(fajrHour - hour - 1);
            } else {
                setMinutesRemaining(fajrMin - min);
                setHoursRemaining(fajrHour - hour);
            }
            if (hour > fajrHour) {
                setHoursRemaining(fajrHour + 24 - hour);
            }
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fajrTime);
        dest.writeString(sunriseTime);
        dest.writeString(dhuhrTime);
        dest.writeString(asrTime);
        dest.writeString(maghribTime);
        dest.writeString(ishaTime);
        dest.writeString(nextPrayer);
        dest.writeString(nextPrayerTime);
        dest.writeString(nextPrayerPhrase);
        dest.writeInt(hoursRemaining);
        dest.writeInt(minutesRemaining);
        dest.writeString(ampmNextPrayer);
        dest.writeString(remainingPhrase);
    }
}
