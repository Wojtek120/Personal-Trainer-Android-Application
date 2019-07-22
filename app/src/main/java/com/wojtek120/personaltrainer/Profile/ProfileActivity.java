package com.wojtek120.personaltrainer.Profile;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setToolbar();
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
        BottomNavigationbarHelper.setupNavigationBetweenActivities(ProfileActivity.this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.profileTabs);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "clicked " + item);

                switch (item.getItemId()){
                    case R.id.menuProfile:
                        Log.d(TAG, "test");
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        return true;
    }
}
