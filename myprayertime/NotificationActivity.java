package com.molate.myprayertime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = MasjidSearchActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter; //takes care of getting only the data we currently
    private RecyclerView.LayoutManager mLayoutManager; //takes care of properly alligning each item
    private ArrayList<ExampleItem> exampleList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        
        if (preferences.getBoolean("fajrNotification",false)) {
            editor.remove("fajrNotification");
            editor.putBoolean("fajrNotification",true);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny,"Fajr","Notification: On"));
        }
        else {
            editor.remove("fajrNotification");
            editor.putBoolean("fajrNotification",false);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny,"Fajr","Notification: Off"));
        }
        if (preferences.getBoolean("dhuhrNotification",false)) {
            editor.remove("dhuhrNotification");
            editor.putBoolean("dhuhrNotification",true);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny,"Dhuhr","Notification: On"));
        }
        else {
            editor.remove("fajrNotification");
            editor.putBoolean("fajrNotification", false);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny, "Dhuhr", "Notification: Off"));
        }
        if (preferences.getBoolean("asrNotification",false)) {
            editor.remove("asrNotification");
            editor.putBoolean("asrNotification",true);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny,"Asr","Notification: On"));
        }
        else {
            editor.remove("fajrNotification");
            editor.putBoolean("fajrNotification", false);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny, "Asr", "Notification: Off"));
        }
        if (preferences.getBoolean("maghribNotification",false)) {
            editor.remove("maghribNotification");
            editor.putBoolean("maghribNotification",true);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny,"Maghrib","Notification: On"));
        }
        else {
            editor.remove("fajrNotification");
            editor.putBoolean("fajrNotification", false);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny, "Maghrib", "Notification: Off"));
        }
        if (preferences.getBoolean("ishaNotification",false)) {
            editor.remove("ishaNotification");
            editor.putBoolean("ishaNotification",true);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny,"Isha","Notification: On"));
        }
        else {
            editor.remove("fajrNotification");
            editor.putBoolean("fajrNotification", false);
            editor.apply();
            exampleList.add(new ExampleItem(R.drawable.sunny, "Isha", "Notification: Off"));
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(NotificationActivity.this,
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NotificationActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                
                String prayer = exampleList.get(position).getmText1();
                
                if (prayer.equals("Fajr")) {
                    if(preferences.getBoolean("fajrNotification",false)) {
                        editor.remove("fajrNotification");
                        editor.putBoolean("fajrNotification", false);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: Off");
                        mAdapter.notifyItemChanged(position);
                    }
                    else {
                        editor.remove("fajrNotification");
                        editor.putBoolean("fajrNotification", true);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: On");
                        mAdapter.notifyItemChanged(position);
                    }
                }

                if (prayer.equals("Dhuhr")) {
                    if(preferences.getBoolean("dhuhrNotification",false)) {
                        editor.remove("dhuhrNotification");
                        editor.putBoolean("dhuhrNotification", false);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: Off");
                        mAdapter.notifyItemChanged(position);
                    }
                    else {
                        editor.remove("dhuhrNotification");
                        editor.putBoolean("dhuhrNotification", true);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: On");
                        mAdapter.notifyItemChanged(position);
                    }
                }

                if (prayer.equals("Asr")) {
                    if(preferences.getBoolean("asrNotification",false)) {
                        editor.remove("asrNotification");
                        editor.putBoolean("asrNotification", false);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: Off");
                        mAdapter.notifyItemChanged(position);
                    }
                    else {
                        editor.remove("asrNotification");
                        editor.putBoolean("asrNotification", true);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: On");
                        mAdapter.notifyItemChanged(position);
                    }
                }

                if (prayer.equals("Maghrib")) {
                    if(preferences.getBoolean("maghribNotification",false)) {
                        editor.remove("maghribNotification");
                        editor.putBoolean("maghribNotification", false);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: Off");
                        mAdapter.notifyItemChanged(position);
                    }
                    else {
                        editor.remove("maghribNotification");
                        editor.putBoolean("maghribNotification", true);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: On");
                        mAdapter.notifyItemChanged(position);
                    }
                }

                if (prayer.equals("Isha")) {
                    if(preferences.getBoolean("ishaNotification",false)) {
                        editor.remove("ishaNotification");
                        editor.putBoolean("ishaNotification", false);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: Off");
                        mAdapter.notifyItemChanged(position);
                    }
                    else {
                        editor.remove("ishaNotification");
                        editor.putBoolean("ishaNotification", true);
                        editor.apply();
                        exampleList.get(position).changeText2("Notification: On");
                        mAdapter.notifyItemChanged(position);
                    }
                }

            }
        });
    }
}
