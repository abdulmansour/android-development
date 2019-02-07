package com.molate.myprayertime;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//Designed in Dec 2017

public class MainActivity extends AppCompatActivity  {

    private static final int PERMISSION_REQUEST_CODE = 11;

    private static final String TAG = MainActivity.class.getSimpleName();
    private LocationManager locationManager;
    private Location location;
    private FirebaseAuth mAuth;

    private CurrentPrayerTime currentPrayerTime;
    private CurrentPrayerTime displayPrayerTime;

    private Button masjidsButton;
    private RadioButton rbmap;
    private RadioButton rbathan;
    private ImageView refreshOnClick;
    private ImageView notificationBell;
    private TextView nextPrayer;
    private TextView nextPrayerTime;
    private TextView nextPrayerAMPM;
    private TextView dayOfW;
    private TextView dayOfMonth;
    private TextView month3l;
    private TextView rPhrase;
    private TextView fTime;
    private TextView sTime;
    private TextView dTime;
    private TextView aTime;
    private TextView mTime;
    private TextView iTime;
    private TextView toolbarName;
    private TextView toolbarEmail;

    private CountDownTimer countDownTimer;
    boolean b = false;
    boolean update = false;
    Calendar c = Calendar.getInstance();
    final int year = c.get(Calendar.YEAR);
    final int month = c.get(Calendar.MONTH) + 1;
    final int day = c.get(Calendar.DAY_OF_MONTH);
    private Date d = new Date();
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE");
    private String dayOfWeek = sdf.format(d);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("MMM");
    private String month3letters = sdf2.format(d);
    private SimpleDateFormat sdf3 = new SimpleDateFormat("dd");
    private String daydd = sdf3.format(d);
    final int hour = c.get(Calendar.HOUR_OF_DAY);
    final int min = c.get(Calendar.MINUTE);
    final int sec = c.get(Calendar.SECOND);
    private String jsonData;
    private double latitude;
    private double longitude;

    //to be strored in shared preferences and used in service notification
    private String fajrTime;
    private String sunriseTime;
    private String dhuhrTime;
    private String asrTime;
    private String maghribTime;
    private String ishaTime;

    private String nextPrayerPhrase;
    private String remainingPhrase;

    private DrawerLayout drawer;

    private Toolbar toolbar;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDisplayPrayerTime(); // your code
                Toast.makeText(getApplicationContext(), "Prayer times refreshed", Toast.LENGTH_SHORT).show();
                pullToRefresh.setRefreshing(false);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        nextPrayer = (TextView) findViewById(R.id.nextPrayer);
        nextPrayerTime = (TextView) findViewById(R.id.nextPrayerTime);
        nextPrayerAMPM = (TextView) findViewById(R.id.nextPrayerAMPM);
        dayOfW = (TextView) findViewById(R.id.dayOfWeek);
        dayOfMonth = (TextView) findViewById(R.id.dayOfMonth);
        month3l = (TextView) findViewById(R.id.month3l);
        rPhrase = (TextView) findViewById(R.id.remainingLabel);
        fTime = (TextView) findViewById(R.id.fajrTime);
        sTime = (TextView) findViewById(R.id.sunriseTime);
        dTime = (TextView) findViewById(R.id.dhuhrTime);
        aTime = (TextView) findViewById(R.id.asrTime);
        mTime = (TextView) findViewById(R.id.maghribTime);
        iTime = (TextView) findViewById(R.id.ishaTime);

        try {
            checkLocationPermission();
        } catch (IOException e) {
            e.printStackTrace();
        }
        authenticateUser();
        getPrayerTime();
        //startService();
        refreshPrayerTimeLoop();

        rbmap = findViewById(R.id.map);
        rbmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, MasjidsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_athan,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item2:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //will only work if user is registered (gotta fix bug)
    public void startService() {
        Intent serviceIntent = new Intent(this,ExampleService.class);
        serviceIntent.putExtra("nextPrayerPhrase",nextPrayerPhrase);
        serviceIntent.putExtra("remainingPhrase",remainingPhrase);
        ContextCompat.startForegroundService(this,serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this,ExampleService.class);
        stopService(serviceIntent);
    }

    private void authenticateUser() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if(preferences.contains("email") && preferences.contains("password") ) {
            String email = preferences.getString("email", "");
            String password = preferences.getString("password", "");
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        getPrayerTime();

                    }
                }
            });
        }
        else {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void checkLocationPermission() throws IOException {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //check if older than marshmallow, where permissions where already enabled during download
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //check if persmission is disabled
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //if this is true, our user has already denied our request
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Location Permission")
                            .setMessage("We cannot provide you with Prayer Times without accessing your location. Click 'Yes' to continue.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, ":(", Toast.LENGTH_SHORT).show();
                                    finish();
                                    System.exit(0);
                                }
                            }).show();
                }
                else {
                    //will request a permission by passing PERMISSION_REQUEST_CODE to onRequestPermissionsResult
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                }
            } else {
                getLocation();
            }
        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    getLocation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(MainActivity.this, ":(", Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
            }
        }
    }

    private void getLocation() throws IOException {
        if (b == false) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryCode();
                toolbar.setTitle(city + ", " + state + ", " + country);

                Log.d(TAG, "Location Established at: " + latitude + " & " + longitude);
                b = true;
                break;
            }
        }
        }
    }

    private void getPrayerTime() {
        Log.d(TAG, "Year: " + year + " Month: " + month + " Day: " + day + " Hour: " + hour + " Minute: " + min + " Second: " + sec);
        String prayerTimeURL = "http://api.aladhan.com/v1/calendar?latitude=" + latitude +
                "&longitude=" + longitude + "&method=2&month=" + month + "&year=" + year;
        Log.d(TAG,prayerTimeURL);
        //Network Connection begins at this point
        if (isNetworkAvailable()) {
            //building our client
            OkHttpClient client = new OkHttpClient();

            // build a request to send to the server
            Request request = new Request.Builder().url(prayerTimeURL).build();

            //call the request using the client and created request
            Call call = client.newCall(request);

            //make the call asynchronous to not block the main thread if error is caught; ie making the call on the queue
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        //result of the response can be stored as a String (jsonData)
                        jsonData = response.body().string();
                        //will log the entire JSON file into a single string
                        Log.v(TAG, jsonData);
                        //if the response is sucessfull execute the following
                        if (response.isSuccessful()) {
                            currentPrayerTime = getCurrentDetails(jsonData, day);

                            displayPrayerTime = new CurrentPrayerTime(
                                    formatTime(currentPrayerTime.getFajrTime()), formatTime(currentPrayerTime.getSunriseTime()),
                                    formatTime(currentPrayerTime.getDhuhrTime()), formatTime(currentPrayerTime.getAsrTime()),
                                    formatTime(currentPrayerTime.getMaghribTime()), formatTime(currentPrayerTime.getIshaTime()),
                                    currentPrayerTime.getNextPrayer(),formatTime(currentPrayerTime.getNextPrayerTime()),
                                    currentPrayerTime.getHoursRemaining(), currentPrayerTime.getMinutesRemaining(),
                                    currentPrayerTime.getAmpmNextPrayer(), currentPrayerTime.getRemainingPhrase(),
                                    currentPrayerTime.getNextPrayerPhrase()
                            );
                            //displayed on main screen ui
                            updateDisplayPrayerTime();
                            //enable ability to refresh only after the call from api to get prayer is succesfull
                            update = true;
                            //method initializing variables used to display prayer times on service notifaction
                            sendPrayerTimeToService();

                    } else {
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Exception caught: ", e);
                    }
                }
            });
        }
    }

    private void sendPrayerTimeToService() {
        fajrTime = currentPrayerTime.getFajrTime();
        sunriseTime = currentPrayerTime.getSunriseTime();
        dhuhrTime = currentPrayerTime.getDhuhrTime();
        asrTime = currentPrayerTime.getAsrTime();
        maghribTime = currentPrayerTime.getMaghribTime();
        ishaTime = currentPrayerTime.getIshaTime();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("fajrTime");
        editor.remove("sunriseTime");
        editor.remove("dhuhrTime");
        editor.remove("asrTime");
        editor.remove("maghribTime");
        editor.remove("ishaTime");
        editor.putInt("day",day);

        editor.putString("fajrTime",fajrTime);
        editor.putString("sunriseTime",sunriseTime);
        editor.putString("dhuhrTime",dhuhrTime);
        editor.putString("asrTime",asrTime);
        editor.putString("maghribTime",maghribTime);
        editor.putString("ishaTime",ishaTime);
        editor.putInt("day",day);
        editor.apply();
    }

    private CurrentPrayerTime getCurrentDetails(String jsonData, int day) throws JSONException {
        //the String jsonData can be converted a JSONObject and parsed to retrieve key information
        JSONObject prayerTimes = new JSONObject(jsonData);

        JSONArray jsonarray = prayerTimes.getJSONArray("data");
        JSONObject dayIndexBlock = jsonarray.getJSONObject(day - 1);
        JSONObject timings = dayIndexBlock.getJSONObject("timings");

        CurrentPrayerTime currentPrayerTime = new CurrentPrayerTime();

        String unformattedTime;

        unformattedTime = timings.getString("Fajr");
        currentPrayerTime.setFajrTime(unformattedTime);

        unformattedTime = timings.getString("Sunrise");
        currentPrayerTime.setSunriseTime(unformattedTime);

        unformattedTime = timings.getString("Dhuhr");
        currentPrayerTime.setDhuhrTime(unformattedTime);

        unformattedTime = timings.getString("Asr");
        currentPrayerTime.setAsrTime(unformattedTime);

        unformattedTime = timings.getString("Maghrib");
        currentPrayerTime.setMaghribTime(unformattedTime);

        unformattedTime = timings.getString("Isha");
        currentPrayerTime.setIshaTime(unformattedTime);

        currentPrayerTime.calculateNextPrayer();
        currentPrayerTime.setNextPrayerTime(currentPrayerTime.getNextPrayer());
        currentPrayerTime.setAmpmNextPrayer(currentPrayerTime.getNextPrayerTime());
        currentPrayerTime.setRemainingPhrase();
        currentPrayerTime.setNextPrayerPhrase();


        return currentPrayerTime;
    }

    // verifies that network available is on or not
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        } else {
            alertUserAboutError();
            Toast.makeText(this, R.string.network_unavailble_message, Toast.LENGTH_SHORT).show();
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private String formatTime(String unformattedTime) {
        String leftPortion = unformattedTime.substring(0, 2);
        String rightPortion = unformattedTime.substring(3, 5);
        int stringToInt = Integer.parseInt(leftPortion);
        if (stringToInt > 12) {
            stringToInt -= 12;
            if (stringToInt < 10) {
                leftPortion = "0" + stringToInt;
            } else {
                leftPortion = stringToInt + "";
            }
        }
        return (leftPortion + ":" + rightPortion);
    }

    public void refreshPrayerTimeLoop() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 15000) {

            // This is called after every 60 sec interval.
            public void onTick(long millisUntilFinished) {
                if (update == true) {
                    updateDisplayPrayerTime();
                }
            }

            public void onFinish() {
                start();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            countDownTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private void updateDisplayPrayerTime() {
        currentPrayerTime.calculateNextPrayer();
        currentPrayerTime.setNextPrayerTime(currentPrayerTime.getNextPrayer());
        currentPrayerTime.setAmpmNextPrayer(currentPrayerTime.getNextPrayerTime());
        currentPrayerTime.setRemainingPhrase();
        currentPrayerTime.setNextPrayerPhrase();

        displayPrayerTime = new CurrentPrayerTime(
                formatTime(currentPrayerTime.getFajrTime()), formatTime(currentPrayerTime.getSunriseTime()),
                formatTime(currentPrayerTime.getDhuhrTime()), formatTime(currentPrayerTime.getAsrTime()),
                formatTime(currentPrayerTime.getMaghribTime()), formatTime(currentPrayerTime.getIshaTime()),
                currentPrayerTime.getNextPrayer(), formatTime(currentPrayerTime.getNextPrayerTime()),
                currentPrayerTime.getHoursRemaining(), currentPrayerTime.getMinutesRemaining(),
                currentPrayerTime.getAmpmNextPrayer(), currentPrayerTime.getRemainingPhrase(),
                currentPrayerTime.getNextPrayerPhrase()
        );

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                nextPrayer.setText(displayPrayerTime.getNextPrayer());
                nextPrayerTime.setText(displayPrayerTime.getNextPrayerTime());
                nextPrayerAMPM.setText(displayPrayerTime.getAmpmNextPrayer());

                dayOfW.setText(dayOfWeek);
                dayOfMonth.setText(daydd);
                month3l.setText(month3letters);

                rPhrase.setText(displayPrayerTime.getRemainingPhrase());

                fTime.setText(displayPrayerTime.getFajrTime());
                sTime.setText(displayPrayerTime.getSunriseTime());
                dTime.setText(displayPrayerTime.getDhuhrTime());
                aTime.setText(displayPrayerTime.getAsrTime());
                mTime.setText(displayPrayerTime.getMaghribTime());
                iTime.setText(displayPrayerTime.getIshaTime());

            }
        });

    }
}









