package com.molate.technicalassignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private TextView name;
    private TextView age;
    private TextView id;
    private Button saveButton;
    private Button resetButton;
    private MenuItem checkable;

    private SharedPreferencesHelper sharedPreferencesHelper;
    private static boolean isChecked;
    private static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prepareActivity();
        adjustViews(isChecked);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new profile object and pass it to controller
                sharedPreferencesHelper.saveProfile(ProfileActivity.this,new Profile(
                        name.getText().toString().trim(),
                        age.getText().toString().trim(),
                        id.getText().toString().trim()
                ));
                isChecked = false;
                adjustViews(isChecked);
                checkable.setChecked(isChecked);
            }

        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                //after 5 clicks of the invisible resetButton, reset the sharedPreferences
                if (counter >= 5) {
                    sharedPreferencesHelper.resetSharedPreferences(ProfileActivity.this);
                    adjustViews(isChecked);
                    counter = 0;
                }
            }
        });

    }
    //link variables with views, create sharedPreferencesHelper object
    // and make sure that on arriving to activity isChecked is false to be in display mode
    private void prepareActivity() {
        name = findViewById(R.id.edit_text_name);
        age = findViewById(R.id.edit_text_age);
        id = findViewById(R.id.edit_text_id);

        saveButton = findViewById(R.id.save_button);
        resetButton = findViewById(R.id.reset_button);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        isChecked = false;
    }
    //Will correlate with the state of the EditMode check box
    //isChecked == true >> EditMode; isChecked == false >> DisplayMode
    private void adjustViews(boolean isChecked) {
        name.setEnabled(isChecked);
        age.setEnabled(isChecked);
        id.setEnabled(isChecked);

        if (isChecked)
            saveButton.setVisibility(View.VISIBLE);
        else
            //GONE instead of INVISIBLE to not affect layout
            saveButton.setVisibility(View.GONE);

        setViews(sharedPreferencesHelper.getProfile());
    }

    //makes sure that correct profile fields are displayed; especially when toggling from edit mode to display mode
    private void setViews(Profile profile) {
        name.setText(profile.getName());
        if(profile.getAge() == null && profile.getId() == null) {
            age.setText("");
            id.setText("");
        }
        else {
            //since age and id are 'int' and we require Strings to store into EditText, call getStringAge/Id
            age.setText(profile.getAge());
            id.setText(profile.getId());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        checkable = menu.findItem(R.id.menu_id);
        checkable.setChecked(isChecked);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_id: {
                //display proper checkbox (checked or not checked)
                isChecked = !item.isChecked();
                item.setChecked(isChecked);

                //Perform your click operation
                adjustViews(isChecked);

                break;
            }
            case android.R.id.home: {
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
