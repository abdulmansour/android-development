package com.molate.myprayertime;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class DisplayMasjidInformation extends AppCompatActivity {

    private Masjid masjid;
    private CurrentPrayerTime currentPrayerTime;
    private TextView masjidName;
    private TextView masjidAddress;
    private double latitude;
    private double longitude;
    private boolean allow = false;

    private TextView fajrTime;
    private TextView dhuhrTime;
    private TextView asrTime;
    private TextView maghribTime;
    private TextView ishaTime;
    private TextView nextPrayerPhrase;
    private TextView remainingPhrase;
    private TextView lastUpdate;
    private ImageView starButtonOff;
    private ImageView starButtonOn;

    private Button getDirections;
    private Button editButton;

    private DatabaseReference myRef;
    private int run = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_masjid_information);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DisplayMasjidInformation.this);
        final SharedPreferences.Editor editor = preferences.edit();

        masjidName = (TextView) findViewById(R.id.masjidName);
        masjidAddress = (TextView) findViewById(R.id.masjidAddress);
        fajrTime = (TextView) findViewById(R.id.fajrTime);
        dhuhrTime = (TextView) findViewById(R.id.dhuhrTime);
        asrTime = (TextView) findViewById(R.id.asrTime);
        maghribTime = (TextView) findViewById(R.id.maghribTime);
        ishaTime = (TextView) findViewById(R.id.ishaTime);
        nextPrayerPhrase = (TextView) findViewById(R.id.nextPrayer);
        remainingPhrase = (TextView) findViewById(R.id.remainingLabel);
        lastUpdate = (TextView) findViewById(R.id.lastUpdate);
        starButtonOff = (ImageView) findViewById(R.id.starButtonOff);
        starButtonOn = (ImageView) findViewById(R.id.starButtonOn);

        masjid = getIntent().getParcelableExtra("masjidObj");
        currentPrayerTime = getIntent().getParcelableExtra("currentPrayerTimeObj");

        masjidName.setText(masjid.getName());
        String mA = masjid.getAddress().substring(0, masjid.getAddress().indexOf(","));
        //masjidAddress.setText(mA);
        fajrTime.setText(currentPrayerTime.getFajrTime());
        dhuhrTime.setText(currentPrayerTime.getDhuhrTime());
        asrTime.setText(currentPrayerTime.getAsrTime());
        maghribTime.setText(currentPrayerTime.getMaghribTime());
        ishaTime.setText(currentPrayerTime.getIshaTime());
        nextPrayerPhrase.setText(currentPrayerTime.getNextPrayerPhrase());
        remainingPhrase.setText(currentPrayerTime.getRemainingPhrase());
        lastUpdate.setText(getIntent().getStringExtra("lastUpdate"));

        myRef = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

        builder1.setTitle("Subscribe");
        builder1.setMessage("By subscribing to " + masjid.getName() + ", you will start receiving push notifications 15 minutes before" +
                " each prayer &" +
                " you will be notified immediately when the masjid updates its prayer times");

        builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                starButtonOff.setVisibility(View.INVISIBLE);
                starButtonOn.setVisibility(View.VISIBLE);

                myRef.child("Subscription").child(masjid.getName()).setValue("true");

                Intent intent = new Intent(getApplicationContext(), MasjidsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(DisplayMasjidInformation.this, "Subscription Updated", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });

        builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder2.setTitle("Unsubscribe");
        builder2.setMessage("By unsubscribing to " + masjid.getName() + ", you will stop receiving push notifications 15 minutes before" +
                " each prayer time and" +
                " you will not be notified when the masjid updates its prayer times!");

        builder2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                starButtonOff.setVisibility(View.VISIBLE);
                starButtonOn.setVisibility(View.INVISIBLE);

                myRef.child("Subscription").child(masjid.getName()).setValue("false");

                Intent intent = new Intent(getApplicationContext(), MasjidsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(DisplayMasjidInformation.this, "Subscription Updated", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        getDirections = (Button) findViewById(R.id.getDirectionsButton);

        getDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String map = "http://maps.google.co.in/maps?q=" + masjid.getAddress();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);
            }
        });

        editButton = (Button) findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayMasjidInformation.this, LoginToAddMasjid.class);
                intent.putExtra("addoredit","edit");
                intent.putExtra("masjidid",masjid.getId());
                intent.putExtra("masjidname",masjid.getName());
                intent.putExtra("masjidaddress",masjid.getAddress());
                intent.putExtra("masjidlatitude",masjid.getLatitude());
                intent.putExtra("masjidlongitude",masjid.getLongitude());

                intent.putExtra("fajr",currentPrayerTime.getFajrTime());
                intent.putExtra("dhuhr",currentPrayerTime.getDhuhrTime());
                intent.putExtra("asr",currentPrayerTime.getAsrTime());
                intent.putExtra("maghrib",currentPrayerTime.getMaghribTime());
                intent.putExtra("isha",currentPrayerTime.getIshaTime());

                startActivity(intent);
            }
        });

        //make sure state of subsribed button is tracked when user opens app
        myRef.child("Subscription").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(masjid.getName())) {
                    if (snapshot.child(masjid.getName()).getValue().toString().equals("true")){
                        starButtonOff.setVisibility(View.INVISIBLE);
                        starButtonOn.setVisibility(View.VISIBLE);
                    }
                    else {
                        starButtonOff.setVisibility(View.VISIBLE);
                        starButtonOn.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    myRef.child("Subscription").child(masjid.getName()).setValue("false");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        starButtonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggle on
                AlertDialog alert = builder1.create();
                alert.show();

            }
        });

        starButtonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggle off
                AlertDialog alert = builder2.create();
                alert.show();
            }
        });

    }
}
