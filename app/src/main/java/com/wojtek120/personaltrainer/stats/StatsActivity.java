package com.wojtek120.personaltrainer.stats;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.BottomNavigationbarHelper;

/**
 * Activity with stats
 */
public class StatsActivity extends AppCompatActivity {
    private final static String TAG = "StatsActivity";
    private static final int ACTIVITY_NUMBER = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeBottomNavbarLook();
        Log.d(TAG, "running");
    }

    /**
     * Change bottom navigation bar animations
     */
    private void changeBottomNavbarLook(){
        Log.d(TAG, "changeBottomNavbarLook: starting");

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationbar);
        BottomNavigationbarHelper.changeBottomNavbarLook(bottomNavigationViewEx);
        BottomNavigationbarHelper.setupNavigationBetweenActivities(StatsActivity.this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }
}
