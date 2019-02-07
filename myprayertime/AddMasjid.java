package com.molate.myprayertime;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMasjid extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private EditText masjidName;
    private EditText masjidAddress;
    private EditText masjidLatitude;
    private EditText masjidLongitude;
    private EditText masjidFajrTime;
    private EditText masjidDhuhrTime;
    private EditText masjidAsrTime;
    private EditText masjidMaghribTime;
    private EditText masjidIshaTime;
    private Button addMasjidButton;
    private String time;
    private int choice = 0;
    private DatabaseReference databaseMasjids;
    private static final String TAG = AddMasjid.class.getSimpleName();
    Masjid masjidForTime = new Masjid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_masjid);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseMasjids = database.getReference("masjids");

        masjidName = (EditText) findViewById(R.id.masjidName);
        masjidAddress = (EditText) findViewById(R.id.masjidAddress);
        masjidLatitude = (EditText) findViewById(R.id.masjidLatitude);
        masjidLongitude = (EditText) findViewById(R.id.masjidLongitude);
        masjidFajrTime = (EditText) findViewById(R.id.masjidFajrTime);
        masjidDhuhrTime = (EditText) findViewById(R.id.masjidDhuhrTime);
        masjidAsrTime = (EditText) findViewById(R.id.masjidAsrTime);
        masjidMaghribTime = (EditText) findViewById(R.id.masjidMaghribTime);
        masjidIshaTime = (EditText) findViewById(R.id.masjidIshaTime);

        addMasjidButton = (Button) findViewById(R.id.addMasjidButton);

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

        addMasjidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Basic Masjid Info: " + masjidName.getText().toString().trim() + " " +
                        masjidLatitude.getText().toString().trim());
                addMasjid();
            }
        });
    }

    public void addMasjid() {
        String mName = masjidName.getText().toString().trim();
        String mAddress = masjidAddress.getText().toString().trim();
        String mFajrTime = masjidFajrTime.getText().toString().trim();
        String mDhuhrTime = masjidDhuhrTime.getText().toString().trim();
        String mAsrTime = masjidAsrTime.getText().toString().trim();
        String mMaghribTime = masjidMaghribTime.getText().toString().trim();
        String mIshaTime = masjidIshaTime.getText().toString().trim();

        if (mName.matches("") || mAddress.matches("") || masjidLatitude.getText().toString().trim().matches("") ||
                masjidLongitude.getText().toString().trim().matches("") || mFajrTime.matches("") || mDhuhrTime.matches("") ||
                mAsrTime.matches("") || mMaghribTime.matches("") || mIshaTime.matches(""))
        {

            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();

        }
        else {
            double mLatitude = Double.parseDouble(masjidLatitude.getText().toString().trim());
            double mLongitutde = Double.parseDouble(masjidLongitude.getText().toString().trim());
            String id = databaseMasjids.push().getKey();
            Masjid masjid = new Masjid(id,mName,mAddress,mLatitude,mLongitutde,mFajrTime,mDhuhrTime,mAsrTime,mMaghribTime,mIshaTime,"");
            //unique id generation, which will create a new node
            databaseMasjids.child(id).setValue(masjid);
            databaseMasjids.child(id).child("lastUpdate").setValue(masjidForTime.getLastUpdate());
            Toast.makeText(this, "Masjid added to database", Toast.LENGTH_LONG).show();
            AddMasjid.this.finish();
        }

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
