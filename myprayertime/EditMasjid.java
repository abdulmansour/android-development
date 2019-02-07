package com.molate.myprayertime;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMasjid extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private EditText masjidName;
    private EditText masjidAddress;
    private EditText masjidLatitude;
    private EditText masjidLongitude;
    private EditText masjidFajrTime;
    private EditText masjidDhuhrTime;
    private EditText masjidAsrTime;
    private EditText masjidMaghribTime;
    private EditText masjidIshaTime;
    private Button editMasjidButton;
    private String time;
    private int choice = 0;
    private DatabaseReference rootRef,myRef;
    private static final String TAG = AddMasjid.class.getSimpleName();
    private Masjid masjidForTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_masjid);

        masjidName = (EditText) findViewById(R.id.masjidName);
        masjidAddress = (EditText) findViewById(R.id.masjidAddress);
        masjidLatitude = (EditText) findViewById(R.id.masjidLatitude);
        masjidLongitude = (EditText) findViewById(R.id.masjidLongitude);
        masjidFajrTime = (EditText) findViewById(R.id.masjidFajrTime);
        masjidDhuhrTime = (EditText) findViewById(R.id.masjidDhuhrTime);
        masjidAsrTime = (EditText) findViewById(R.id.masjidAsrTime);
        masjidMaghribTime = (EditText) findViewById(R.id.masjidMaghribTime);
        masjidIshaTime = (EditText) findViewById(R.id.masjidIshaTime);

        masjidName.setText(getIntent().getStringExtra("masjidname"));
        masjidAddress.setText(getIntent().getStringExtra("masjidaddress"));
        masjidLatitude.setText(getIntent().getDoubleExtra("masjidlatitude",0)+"");
        masjidLongitude.setText(getIntent().getDoubleExtra("masjidlongitude",0)+"");

        masjidFajrTime.setText(getIntent().getStringExtra("fajr"));
        masjidDhuhrTime.setText(getIntent().getStringExtra("dhuhr"));
        masjidAsrTime.setText(getIntent().getStringExtra("asr"));
        masjidMaghribTime.setText(getIntent().getStringExtra("maghrib"));
        masjidIshaTime.setText(getIntent().getStringExtra("isha"));

        masjidFajrTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 1;
                android.support.v4.app.DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        masjidDhuhrTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 2;
                android.support.v4.app.DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        masjidAsrTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 3;
                android.support.v4.app.DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        masjidMaghribTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 4;
                android.support.v4.app.DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        masjidIshaTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 5;
                android.support.v4.app.DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });

        rootRef = FirebaseDatabase.getInstance().getReference();
        myRef = rootRef.child("masjids");

        editMasjidButton = (Button) findViewById(R.id.editMasjidButton);

        editMasjidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (masjidName.getText().toString().matches("") || masjidAddress.getText().toString().matches("") ||
                        masjidLatitude.getText().toString().trim().matches("") ||
                        masjidLongitude.getText().toString().trim().matches("") ||
                        masjidFajrTime.getText().toString().matches("") || masjidDhuhrTime.getText().toString().matches("") ||
                        masjidAsrTime.getText().toString().matches("") || masjidMaghribTime.getText().toString().matches("") ||
                        masjidIshaTime.getText().toString().matches(""))
                {

                    Toast.makeText(EditMasjid.this, "Please fill in all fields", Toast.LENGTH_LONG).show();

                }
                else {

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                String uid = getIntent().getStringExtra("masjidid");

                                String name = masjidName.getText().toString();
                                if (!ds.child("name").getValue(String.class).equals(name)) {
                                    myRef.child(uid).child("name").setValue(name);
                                }

                                String address = masjidAddress.getText().toString();
                                if (!ds.child("address").getValue(String.class).equals(address)) {
                                    myRef.child(uid).child("address").setValue(address);
                                }

                                String lat = masjidLatitude.getText().toString();
                                double latitude = Double.parseDouble(lat);
                                if (!ds.child("latitude").getValue(Double.class).equals(latitude)) {
                                    myRef.child(uid).child("latitude").setValue(latitude);
                                }

                                String lon = masjidLongitude.getText().toString();
                                double longitude = Double.parseDouble(lon);
                                if (!ds.child("longitude").getValue(Double.class).equals(longitude)) {
                                    myRef.child(uid).child("longitude").setValue(longitude);
                                }

                                String fajrTime = masjidFajrTime.getText().toString();
                                if (!ds.child("currentPrayerTime").child("fajrTime").getValue(String.class).equals(fajrTime)) {
                                    myRef.child(uid).child("currentPrayerTime").child("fajrTime").setValue(fajrTime);
                                }

                                String dhuhrTime = masjidDhuhrTime.getText().toString();
                                if (!ds.child("currentPrayerTime").child("dhuhrTime").getValue(String.class).equals(dhuhrTime)) {
                                    myRef.child(uid).child("currentPrayerTime").child("dhuhrTime").setValue(dhuhrTime);
                                }

                                String asrTime = masjidAsrTime.getText().toString();
                                if (!ds.child("currentPrayerTime").child("asrTime").getValue(String.class).equals(asrTime)) {
                                    myRef.child(uid).child("currentPrayerTime").child("asrTime").setValue(asrTime);
                                }

                                String maghribTime = masjidMaghribTime.getText().toString();
                                if (!ds.child("currentPrayerTime").child("maghribTime").getValue(String.class).equals(maghribTime)) {
                                    myRef.child(uid).child("currentPrayerTime").child("maghribTime").setValue(maghribTime);
                                }

                                String ishaTime = masjidIshaTime.getText().toString();
                                if (!ds.child("currentPrayerTime").child("ishaTime").getValue(String.class).equals(ishaTime)) {
                                    myRef.child(uid).child("currentPrayerTime").child("ishaTime").setValue(ishaTime);
                                }
                                masjidForTime = new Masjid();
                                myRef.child(uid).child("lastUpdate").setValue(masjidForTime.getLastUpdate());

                                Toast.makeText(EditMasjid.this, "Masjid Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditMasjid.this,MasjidsActivity.class);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String h = "" + hourOfDay;
        String m = "" + minute;
        if (h.length() == 1) {
            h = "0" + h;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        time = h + ":" + m;
        if (choice == 1) {
            masjidFajrTime.setText(time);
        }
        if (choice == 2) {
            masjidDhuhrTime.setText(time);
        }
        if (choice == 3) {
            masjidAsrTime.setText(time);
        }
        if (choice == 4) {
            masjidMaghribTime.setText(time);
        }
        if (choice == 5) {
            masjidIshaTime.setText(time);
        }
    }
}
