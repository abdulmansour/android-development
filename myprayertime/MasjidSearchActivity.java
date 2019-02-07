package com.molate.myprayertime;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MasjidSearchActivity extends AppCompatActivity {

    private static final String TAG = MasjidSearchActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter; //takes care of getting only the data we currently
    private RecyclerView.LayoutManager mLayoutManager; //takes care of properly alligning each item
    private DatabaseReference myRef;
    private ArrayList<ExampleItem> exampleList = new ArrayList<>();
    private int size;

    private String masjidName,masjidAddress;
    private boolean subscribed = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masjid_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Search");

        myRef = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //verify if is a subscribed mosque
        myRef.child("Subscription")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Intent intent = getIntent();
                        final ArrayList<Masjid> masjids = intent.getParcelableArrayListExtra("masjids");
                        final ArrayList<CurrentPrayerTime> currentPrayerTimes = intent.getParcelableArrayListExtra("currentPrayerTimes");
                        final ArrayList<String> subbedMasjidsDist = intent.getStringArrayListExtra("subbedMasjidsDist");
                        final ArrayList<String> nonsubbedMasjidsDist = intent.getStringArrayListExtra("nonsubbedMasjidsDist");
                        int index = 0;
                        int index2 = 0;

                        size = masjids.size();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (int i = 0; i < size; i++) {
                                masjidName = masjids.get(i).getName();
                                masjidAddress = masjids.get(i).getAddress();

                                if (masjidName.equals(snapshot.getKey()) && snapshot.getValue().equals("true")) {
                                    subscribed = true;
                                    break;
                                } else {
                                    subscribed = false;
                                }
                            }

                            if (subscribed) {
                                exampleList.add(new ExampleItem(R.drawable.ic_mosque_subscribed, masjidName, subbedMasjidsDist.get(index)));
                                index++;
                            }
                        }
                        for (int i = 0; i < size; i++) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                masjidName = masjids.get(i).getName();
                                masjidAddress = masjids.get(i).getName();

                                if (masjidName.equals(snapshot.getKey()) && snapshot.getValue().equals("true")) {
                                    subscribed = true;
                                    break;
                                } else {
                                    subscribed = false;
                                }
                            }

                            if (!subscribed) {
                                exampleList.add(new ExampleItem(R.drawable.ic_mosque, masjidName, nonsubbedMasjidsDist.get(index2)));
                                index2++;
                            }
                        }

                        mRecyclerView = findViewById(R.id.recyclerView);
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(MasjidSearchActivity.this,
                                DividerItemDecoration.VERTICAL));
                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(MasjidSearchActivity.this);
                        mAdapter = new ExampleAdapter(exampleList);

                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                for (int i = 0; i < size; i++) {
                                    if (masjids.get(i).getName().equals(exampleList.get(position).getmText1())) {
                                        Log.d(TAG,"Masjid Clicked: " + masjids.get(i).getName());
                                        Toast.makeText(MasjidSearchActivity.this, "Masjid: "+masjids.get(i).getName(), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MasjidSearchActivity.this,DisplayMasjidInformation.class);
                                        intent.putExtra("masjidObj", masjids.get(i));
                                        intent.putExtra("currentPrayerTimeObj",currentPrayerTimes.get(i));
                                        intent.putExtra("lastUpdate",masjids.get(i).getLastUpdate());
                                        startActivity(intent);
                                    }
                                }


                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}