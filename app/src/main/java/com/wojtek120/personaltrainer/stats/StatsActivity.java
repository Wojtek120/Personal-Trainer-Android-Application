package com.wojtek120.personaltrainer.stats;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;

/**
 * Activity with stats
 */
public class StatsActivity extends AppCompatActivity {
    private final static String TAG = "StatsActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.STATS_ACTIVITY;

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

        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, StatsActivity.this, ACTIVITY_NUMBER);
    }
}
