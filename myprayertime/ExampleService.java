package com.molate.myprayertime;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.molate.myprayertime.App.CHANNEL_ID;

public class ExampleService extends Service {

    private CurrentPrayerTime currentPrayerTime;
    private int currentDay;
    Calendar c = Calendar.getInstance();
    final int day = c.get(Calendar.DAY_OF_MONTH);

    private Intent notificationIntent;
    private PendingIntent pendingIntent;
    private Notification notification;

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 15;
    int run = 1;
    float dist = 0;
    int timeElapsed = 0;

    final Handler handler = new Handler();

    private LocationManager locationManager;
    private Location location;
    private Location lastLocation;
    private double latitude;
    private double longitude;

    private DatabaseReference myRef;

    @Override
    public void onCreate() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String fajrTime = preferences.getString("fajrTime", "");
        String sunriseTime = preferences.getString("sunriseTime","");
        String dhuhrTime = preferences.getString("dhuhrTime", "");
        String asrTime = preferences.getString("asrTime", "");
        String maghribTime = preferences.getString("maghribTime", "");
        String ishaTime = preferences.getString("ishaTime", "");
        currentDay = preferences.getInt("day", 0);

        currentPrayerTime = new CurrentPrayerTime(fajrTime,sunriseTime, dhuhrTime, asrTime, maghribTime, ishaTime);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTimer();

        return START_NOT_STICKY;
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 1000, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void run() {

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ExampleService.this);
                        SharedPreferences.Editor editor = preferences.edit();

                        if (preferences.getBoolean("register",false) || preferences.getBoolean("login",false)) {

                            //TODO CALL NOTIFICATION FUNC

                            currentPrayerTime.calculateNextPrayerService();
                            currentPrayerTime.setNextPrayerTime(currentPrayerTime.getNextPrayer());
                            currentPrayerTime.setAmpmNextPrayer(currentPrayerTime.getNextPrayerTime());
                            currentPrayerTime.setRemainingPhrase();
                            currentPrayerTime.setNextPrayerPhrase();

                            if (currentDay != day) {
                                Intent dialogIntent = new Intent(ExampleService.this, MainActivity.class);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(dialogIntent);
                            }
                            notificationIntent = new Intent(ExampleService.this, MainActivity.class);
                            pendingIntent = PendingIntent.getActivity(ExampleService.this,
                                    0, notificationIntent, 0);
                            notification = new NotificationCompat.Builder(ExampleService.this, CHANNEL_ID)
                                    .setContentTitle("Upcoming: " + currentPrayerTime.getNextPrayerPhrase())
                                    .setContentText(currentPrayerTime.getRemainingPhrase())
                                    .setSmallIcon(R.drawable.ic_timelapse)
                                    .setContentIntent(pendingIntent)
                                    .build();

                            startForeground(1, notification);

                            Boolean fajrNotification = preferences.getBoolean("fajrNotification", false);
                            Boolean dhuhrNotification = preferences.getBoolean("dhuhrNotification", false);
                            Boolean asrNotification = preferences.getBoolean("asrNotification", false);
                            Boolean maghribNotification = preferences.getBoolean("maghribNotification", false);
                            Boolean ishaNotification = preferences.getBoolean("ishaNotification", false);

                            Calendar c = Calendar.getInstance();
                            final int hour = c.get(Calendar.HOUR_OF_DAY);
                            final int min = c.get(Calendar.MINUTE);

                            if (fajrNotification) {
                                int i = currentPrayerTime.getFajrTime().indexOf(":");
                                int fajrHour = Integer.parseInt(currentPrayerTime.getFajrTime().substring(0, i));
                                int fajrMin = Integer.parseInt(currentPrayerTime.getFajrTime().substring(3, 5));
                                if (hour == fajrHour && min == fajrMin) {
                                    Notification.Builder builder = new Notification.Builder(ExampleService.this);
                                    builder.setSmallIcon(R.drawable.ic_launcher)
                                            .setContentTitle("Time for Fajr Prayer")
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setOngoing(true);
                                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    manager.notify(1, builder.build());
                                }
                            }
                            if (dhuhrNotification) {
                                int i = currentPrayerTime.getDhuhrTime().indexOf(":");
                                int dhuhrHour = Integer.parseInt(currentPrayerTime.getDhuhrTime().substring(0, i));
                                int dhuhrMin = Integer.parseInt(currentPrayerTime.getDhuhrTime().substring(3, 5));
                                if (hour == dhuhrHour && min == dhuhrMin) {
                                    Notification.Builder builder = new Notification.Builder(ExampleService.this);
                                    builder.setSmallIcon(R.drawable.ic_launcher)
                                            .setContentTitle("Time for Dhuhr Prayer")
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setOngoing(true);
                                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    manager.notify(1, builder.build());
                                }
                            }
                            if (asrNotification) {
                                int i = currentPrayerTime.getAsrTime().indexOf(":");
                                int asrHour = Integer.parseInt(currentPrayerTime.getAsrTime().substring(0, i));
                                int asrMin = Integer.parseInt(currentPrayerTime.getAsrTime().substring(3, 5));
                                if (hour == asrHour && min == asrMin) {
                                    Notification.Builder builder = new Notification.Builder(ExampleService.this);
                                    builder.setSmallIcon(R.drawable.ic_launcher)
                                            .setContentTitle("Time for Asr Prayer")
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setOngoing(true);
                                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    manager.notify(1, builder.build());
                                }
                            }
                            if (maghribNotification) {
                                int i = currentPrayerTime.getMaghribTime().indexOf(":");
                                int maghribHour = Integer.parseInt(currentPrayerTime.getMaghribTime().substring(0, i));
                                int maghribMin = Integer.parseInt(currentPrayerTime.getMaghribTime().substring(3, 5));
                                if (hour == maghribHour && min == maghribMin) {
                                    Notification.Builder builder = new Notification.Builder(ExampleService.this);
                                    builder.setSmallIcon(R.drawable.ic_launcher)
                                            .setContentTitle("Time for Maghrib Prayer")
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setOngoing(true);
                                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    manager.notify(1, builder.build());
                                }
                            }
                            if (ishaNotification) {
                                int i = currentPrayerTime.getIshaTime().indexOf(":");
                                int ishaHour = Integer.parseInt(currentPrayerTime.getIshaTime().substring(0, i));
                                int ishaMin = Integer.parseInt(currentPrayerTime.getIshaTime().substring(3, 5));
                                if (hour == ishaHour && min == ishaMin) {
                                    Notification.Builder builder = new Notification.Builder(ExampleService.this);
                                    builder.setSmallIcon(R.drawable.ic_launcher)
                                            .setContentTitle("Time for Isha Prayer")
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setOngoing(true);
                                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    manager.notify(1, builder.build());
                                }
                            }

                            //record location every 15 mins && if location is 10meters apart last location
                            if (timeElapsed % 900000 == 0) {
                                getLocation();
                            }

                            timeElapsed = timeElapsed + 15;
                        }
                    }
                });
            }
        };
    }

    @Override
    public void onDestroy() {
        stoptimertask();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getDate(){
        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        String date=dfDate.format(Calendar.getInstance().getTime());
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        String time = dfTime.format(Calendar.getInstance().getTime());
        return date + " " + time;
    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //get best possible location provider
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, 1000, 0,
                    new LocationListener() {

                        public void onLocationChanged(Location location) {
                        }

                        public void onProviderDisabled(String provider) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {
                        }
                    });
            location = locationManager.getLastKnownLocation(provider);
            
            if (location != null ) {
                if (run == 1) {
                    lastLocation = location;
                }
                if (dist > 10 || run == 1) {
                    
                    run = 0;
                    
                    dist = location.distanceTo(lastLocation);
                    lastLocation = location;
                    
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    myRef = FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    String dateStamp = getDate();
                    myRef.child("LocationStamps").child(dateStamp).child("Latitude").setValue(latitude);
                    myRef.child("LocationStamps").child(dateStamp).child("Longitude").setValue(longitude);

                    Log.d(TAG, "Location from service: " + latitude + " & " + longitude);
                    
                    break;
                }
            }
        }
    }

}
