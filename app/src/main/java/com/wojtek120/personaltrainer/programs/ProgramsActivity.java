package com.wojtek120.personaltrainer.programs;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;

/**
 * Activity with all training programs
 */
public class ProgramsActivity extends AppCompatActivity {
    private final static String TAG = "ProgramsActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.PROGRAMS_ACTIVITY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavbarSetup();
        Log.d(TAG, "running");
    }


    /**
     * Setup bottom navbar
     */
    private void bottomNavbarSetup(){
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationbar);

        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, ProgramsActivity.this, ACTIVITY_NUMBER);
    }
}
