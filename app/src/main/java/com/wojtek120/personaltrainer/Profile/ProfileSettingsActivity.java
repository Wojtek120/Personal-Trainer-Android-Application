package com.wojtek120.personaltrainer.Profile;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wojtek120.personaltrainer.R;

public class ProfileSettingsActivity extends AppCompatActivity {
    private final String TAG = "ProfileSettingsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Log.d(TAG, "created settings");
    }
}
