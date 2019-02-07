package com.molate.technicalassignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class mainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private Button viewMyGradesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareViews();
    }

    private void prepareViews() {
        viewMyGradesButton = findViewById(R.id.viewMyGradesButton);
        viewMyGradesButton.setOnClickListener(viewGradesClickListener);
    }

    private Button.OnClickListener viewGradesClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG,"View My Grades Button Clicked!");
            goToGradeActivity();
        }
    };

    private void goToGradeActivity() {
        Intent intent = new Intent(this, gradeActivity.class);
        startActivity(intent);
    }

}
