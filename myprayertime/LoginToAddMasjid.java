package com.molate.myprayertime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginToAddMasjid extends AppCompatActivity {

    private String username = "add_masjid";
    private String password = "masjid_123";

    private EditText usernameEditText;
    private EditText passwordEditText;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_to_add_masjid);

        usernameEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = usernameEditText.getText().toString().trim();
                String p = passwordEditText.getText().toString().trim();
                if (u.equals(username)) {
                    if (p.equals(password)) {
                        Toast.makeText(LoginToAddMasjid.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        String addoredit = getIntent().getStringExtra("addoredit");

                        if (addoredit.equals("add")) {
                            Intent intent = new Intent(LoginToAddMasjid.this, AddMasjid.class);
                            startActivity(intent);
                            finish();
                        }
                        else if (addoredit.equals("edit")) {
                            Intent intent = new Intent(LoginToAddMasjid.this, EditMasjid.class);

                            intent.putExtra("addoredit","edit");

                            intent.putExtra("masjidid",getIntent().getStringExtra("masjidid"));
                            intent.putExtra("masjidname",getIntent().getStringExtra("masjidname"));
                            intent.putExtra("masjidaddress",getIntent().getStringExtra("masjidaddress"));
                            intent.putExtra("masjidlatitude",getIntent().getDoubleExtra("masjidlatitude",0));
                            intent.putExtra("masjidlongitude",getIntent().getDoubleExtra("masjidlongitude",0));

                            intent.putExtra("fajr",getIntent().getStringExtra("fajr"));
                            intent.putExtra("dhuhr",getIntent().getStringExtra("dhuhr"));
                            intent.putExtra("asr",getIntent().getStringExtra("asr"));
                            intent.putExtra("maghrib",getIntent().getStringExtra("maghrib"));
                            intent.putExtra("isha",getIntent().getStringExtra("isha"));
                            intent.putExtra("masjidObj",getIntent().getParcelableExtra("masjidObj"));
                            intent.putExtra("currentPrayerTimeObj",getIntent().getParcelableExtra("currentPrayerTimeObj"));
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(LoginToAddMasjid.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(LoginToAddMasjid.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
