package com.molate.technicalassignment2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;

    public static final String PROFILE_PREF = "profile_preferences";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String ID = "id";

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PROFILE_PREF, context.MODE_PRIVATE);
    }

    public void saveProfile(Activity context, Profile profile) {
        boolean nameCheck = false, ageCheck = false, idCheck = false;
        String name = profile.getName();
        String age = profile.getAge();
        String id = profile.getId();

        //check if name is empty
        if(!name.isEmpty())
            nameCheck = true;
        else
            Toast.makeText(context,"Invalid Name.",Toast.LENGTH_SHORT).show();

        //check if age is empty
        if (!age.isEmpty()) {
            int iage = Integer.parseInt(age);

            //check if int value of age is between 18 & 99
            if (iage >= 18 && iage <= 99)
                ageCheck = true;
            else
                Toast.makeText(context, "Invalid Age - Enter Age between 18-99.", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "Invalid Age.", Toast.LENGTH_SHORT).show();

        //check if id is empty
        if (!id.isEmpty())
            idCheck = true;
        else
            Toast.makeText(context,"Invalid Student ID.",Toast.LENGTH_SHORT).show();

        //check that all 3 fields qualify before saving to sharedPreferences
        if (nameCheck && ageCheck && idCheck) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(NAME, name);
            editor.putString(AGE, age);
            editor.putString(ID, id);

            editor.apply();

            Toast.makeText(context, "Data Saved!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Data not Saved!",Toast.LENGTH_SHORT).show();
        }
    }
    //implemented invisible reset button to easily reset sharedPreferences content
    public void resetSharedPreferences(Activity context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(context,"Data has Reset!", Toast.LENGTH_SHORT).show();
    }
    //return new Profile object, with data(name, age, id) being passed from the EditTexts of our Profile Activity
    public Profile getProfile() {
        return new Profile(sharedPreferences.getString(NAME,null),sharedPreferences.getString(AGE,null),sharedPreferences.getString(ID,null));
    }
    //return value of key:"name" from sharedPreferences:"profile_preferences"
    public String getProfileName() {
        return sharedPreferences.getString(NAME,null);
    }
}
