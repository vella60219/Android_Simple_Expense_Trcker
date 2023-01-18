package edu.dtd.test110919026;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("data",MODE_PRIVATE);
        if (!preferences.getAll().isEmpty()) {

            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {
            startActivity(new Intent(this, Activity2.class));
            finish();
        }
    }
}