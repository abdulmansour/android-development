package com.molate.technicalassignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    protected SharedPreferencesHelper sharedPreferencesHelper;
    protected Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareActivity();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfileActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //start here and decide based on profile name whether to stay in main or go to ProfileActivity
        String name = sharedPreferencesHelper.getProfileName();
        if(name == null) {
            goToProfileActivity();
        }
        else {
            button.setText(name);
        }

    }
    //go to ProfileActivity
    private void goToProfileActivity() {
        Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(intent);
    }
    //Prepare views and sharedPreferenceHelper object
    private void prepareActivity() {
        button = findViewById(R.id.button);
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
    }

}


