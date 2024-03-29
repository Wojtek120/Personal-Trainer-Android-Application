package com.wojtek120.personaltrainer.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.home.MainActivity_;
import com.wojtek120.personaltrainer.plans.PlansActivity_;
import com.wojtek120.personaltrainer.predefined.PredefinedPlansActivity_;
import com.wojtek120.personaltrainer.profile.ProfileActivity_;
import com.wojtek120.personaltrainer.stats.StatsActivity_;

/**
 * Bottom navigation bar helper
 */
public class BottomNavigationbarHelper {

    private static final String TAG = "BottomNavigationHelper";

    /**
     * Change bottom navigation bar animations
     */
    public static void changeBottomNavbarLook(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setIconsMarginTop(30);
        bottomNavigationViewEx.setTextVisibility(false);

        Log.d(TAG, "changeBottomNavbarLook: changed");
    }

    /**
     * Setup how it is navigated between activities
     */
    public static void setupNavigationBetweenActivities(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;

                switch (item.getItemId()){
                    case R.id.iconHome:
                        intent = new Intent(context, MainActivity_.class);
                        context.startActivity(intent);
                        break;

                    case R.id.iconStats:
                        intent = new Intent(context, StatsActivity_.class);
                        context.startActivity(intent);
                        break;

                    case R.id.iconPrograms:
                        intent = new Intent(context, PlansActivity_.class);
                        context.startActivity(intent);
                        break;

                    case R.id.iconSearch:
                        intent = new Intent(context, PredefinedPlansActivity_.class);
                        context.startActivity(intent);
                        break;

                    case R.id.iconProfile:
                        intent = new Intent(context, ProfileActivity_.class);
                        context.startActivity(intent);
                        break;
                }

                return false;
            }
        });
    }
}
