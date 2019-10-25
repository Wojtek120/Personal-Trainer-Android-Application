package com.wojtek120.personaltrainer.search;
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
 * Activity with search options
 */
public class SearchActivity extends AppCompatActivity {
    private final static String TAG = "SearchActivity";
    private static final int ACTIVITY_NUMBER = 3;

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
        BottomNavigationbarHelper.setupNavigationBetweenActivities(SearchActivity.this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }
}
