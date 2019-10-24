package com.wojtek120.personaltrainer.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.Utils.BottomNavigationbarHelper;

/**
 * Activity with settings
 */
public class ProfileActivity extends AppCompatActivity {
    private final static String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUMBER = 4;
    private Context context = ProfileActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setToolbar();
        changeBottomNavbarLook();
        turnOffProgressBar();

        Log.d(TAG, "running");
    }

    /**
     * Set toolbar
     */
    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.profileTabs);
        setSupportActionBar(toolbar);

        ImageView menuImageView = findViewById(R.id.menuProfile);
        menuImageView.setOnClickListener(v -> {
            Log.d(TAG, "Profile settings opening");
            Intent intent = new Intent(context, ProfileSettingsActivity.class);
            startActivity(intent);
        });

    }

    /**
     * Change bottom navigation bar animations
     */
    private void changeBottomNavbarLook(){
        Log.d(TAG, "changeBottomNavbarLook: starting");

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationbar);
        BottomNavigationbarHelper.changeBottomNavbarLook(bottomNavigationViewEx);
        BottomNavigationbarHelper.setupNavigationBetweenActivities(ProfileActivity.this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

    /**
     * Make progress bar disappear
     */
    private void turnOffProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBarInProfileLayout);
        progressBar.setVisibility(View.GONE);
    }
}
