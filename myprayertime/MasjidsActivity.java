package com.molate.myprayertime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MasjidsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MasjidsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private double myLatitude;
    private double myLongitude;
    private Masjid[] masjid;
    private int masjidCount = 0;
    private int i = 0;
    private DatabaseReference rootRef, myRef;
    private boolean useSubscribeIcon = false;
    private Button searchButton;
    private List<Masjid> masjids = new ArrayList<>();
    private List<CurrentPrayerTime> currentPrayerTimes = new ArrayList<>();
    private List<String> subbedMasjidsDist = new ArrayList<>();
    private List<String> nonsubbedMasjidsDist = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");
    private RadioButton rbmap;
    private RadioButton rbathan;

    private BitmapDescriptor bitmapDescriptorFromVector(ComponentName context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(this, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth()*2, vectorDrawable.getIntrinsicHeight()*2);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth()*2, vectorDrawable.getIntrinsicHeight()*2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        Log.d(TAG,"Width: " + vectorDrawable.getIntrinsicWidth() + " Height: " + vectorDrawable.getIntrinsicHeight());
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //loop

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masjids);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Masjids Map");

        rbathan = (RadioButton) findViewById(R.id.athan);
        rbathan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);
        //to avoid keyboard pop as activity opens
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        myLatitude = intent.getDoubleExtra("latitude", 45.4996990);
        myLongitude = intent.getDoubleExtra("longitude", -73.7842400);

        rootRef = FirebaseDatabase.getInstance().getReference();
        myRef = rootRef.child("masjids");

        fetchAllMasjidsFromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(MasjidsActivity.this,MasjidSearchActivity.class);
                intent.putParcelableArrayListExtra("masjids", (ArrayList<Masjid>) masjids);
                intent.putParcelableArrayListExtra("currentPrayerTimes", (ArrayList<CurrentPrayerTime>) currentPrayerTimes);
                intent.putStringArrayListExtra("subbedMasjidsDist",(ArrayList<String>) subbedMasjidsDist);
                intent.putStringArrayListExtra("nonsubbedMasjidsDist",(ArrayList<String>) nonsubbedMasjidsDist);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                return true;

            case R.id.item2:
                Intent intent2 = new Intent(this, LoginToAddMasjid.class);
                intent2.putExtra("addoredit","add");
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchAllMasjidsFromDatabase() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    masjidCount++;
                }

                masjid = new Masjid[masjidCount];

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    masjid[i] = new Masjid();
                    String id = ds.child("id").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    String address = ds.child("address").getValue(String.class);
                    double latitude = ds.child("latitude").getValue(Double.class);
                    double longitude = ds.child("longitude").getValue(Double.class);
                    String fajrTime = ds.child("currentPrayerTime").child("fajrTime").getValue(String.class);
                    String dhuhrTime = ds.child("currentPrayerTime").child("dhuhrTime").getValue(String.class);
                    String asrTime = ds.child("currentPrayerTime").child("asrTime").getValue(String.class);
                    String maghribTime = ds.child("currentPrayerTime").child("maghribTime").getValue(String.class);
                    String ishaTime = ds.child("currentPrayerTime").child("ishaTime").getValue(String.class);
                    String lastUpdate = ds.child("lastUpdate").getValue(String.class);

                    Log.d(TAG, masjidCount + ": " + name + " marker added");

                    masjid[i] = new Masjid(id,name,address,latitude,longitude,fajrTime,dhuhrTime,asrTime,maghribTime,ishaTime,lastUpdate);
                    addNewMarker(masjid[i]);

                    float[] dist = new float[1];
                    Location.distanceBetween(masjid[i].getLatitude(),masjid[i].getLongitude(),myLatitude,myLongitude,dist);
                    if(dist[0]/(1000*50) < 1){
                        //here your code or alert box for outside 1Km*50 radius area
                        masjids.add(masjid[i]);
                        currentPrayerTimes.add(masjid[i].getCurrentPrayerTime());

                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //make search edit text that locates masjid by typing name and tells distance from user location and can click to get directions

        mMap = googleMap;

        LatLng currentPosition = new LatLng(myLatitude, myLongitude);
        mMap.addMarker(new MarkerOptions().position(currentPosition).title("You are here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,11));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker) {
                displayMasjid(marker.getTitle().toString());
            }

        });
    }

    private void addNewMarker(final Masjid masjid) {

        final float[] dist = new float[1];
        Location.distanceBetween(masjid.getLatitude(),masjid.getLongitude(),myLatitude,myLongitude,dist);

        myRef = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //verify if is a subscribed mosque
        myRef.child("Subscription")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //if is subscribed
                            if(masjid.getName().equals(snapshot.getKey()) && snapshot.getValue().equals("true")) {
                                useSubscribeIcon = true;
                                break;
                            }
                            //if not subscribed
                            else {
                                useSubscribeIcon = false;

                            }
                        }
                        if (useSubscribeIcon) {
                            String distance = String.valueOf(df.format(dist[0]/1000))+" km";
                            subbedMasjidsDist.add(distance);
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(masjid.getLatitude(), masjid.getLongitude()))
                                    .title(masjid.getName())
                                    .snippet(distance)
                                    .icon(bitmapDescriptorFromVector(getCallingActivity(), R.drawable.ic_mosque_subscribed)));
                        }
                        else {
                            String distance = String.valueOf(df.format(dist[0]/1000))+" km";
                            nonsubbedMasjidsDist.add(distance);
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(masjid.getLatitude(), masjid.getLongitude()))
                                    .title(masjid.getName())
                                    .snippet(distance)
                                    .icon(bitmapDescriptorFromVector(getCallingActivity(), R.drawable.ic_mosque)));
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }


                });
    }

    public void displayMasjid(String masjidName) {
        for (int i = 0; i < masjidCount; i++) {
            if (masjid[i].getName().equals(masjidName)) {
                Intent intent = new Intent(MasjidsActivity.this,DisplayMasjidInformation.class);
                intent.putExtra("masjidObj", masjid[i]);
                intent.putExtra("currentPrayerTimeObj",masjid[i].getCurrentPrayerTime());
                intent.putExtra("lastUpdate",masjid[i].getLastUpdate());
                startActivity(intent);
            }
        }

    }
}
