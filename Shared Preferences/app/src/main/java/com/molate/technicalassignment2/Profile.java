package com.molate.technicalassignment2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Profile {

    private String name;
    //better to store 'age' & 'id' as String than int, to avoid default value issues when storing in sharedPreferences
    private String age;
    private String id;

    Profile() {

    }

    Profile(String name, String age, String id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




}
