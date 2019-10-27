package com.wojtek120.personaltrainer.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;

/**
 * Activity with settings
 */
public class ProfileActivity extends AppCompatActivity {
    private final static String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.PROFILE_ACTIVITY;
    private Context context = ProfileActivity.this;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressBar = findViewById(R.id.progressBarInProfileLayout);

        setToolbar();
        bottomNavbarSetup();
        changeProfilePhoto();

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
     * Setup bottom navbar
     */
    private void bottomNavbarSetup(){
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationbar);

        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, ProfileActivity.this, ACTIVITY_NUMBER);
    }

    //TODO temporary - just for testing
    private void changeProfilePhoto() {

        ImageView photo = findViewById(R.id.imageProfile);
        String URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Björnsson_Arnold_Classic_2017.jpg/1200px-Björnsson_Arnold_Classic_2017.jpg";
        ImageLoaderSingleton.displayImage("", URL, photo, progressBar);
    }

    /**
     * Make progress bar disappear
     */
    private void turnOffProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
