package com.wojtek120.personaltrainer.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.Utils.BottomNavigationbarHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUMBER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeBottomNavbarLook();
        addFragmentsToPagerAdapter();
    }

    /**
     * Add tabs
     */
    private void addFragmentsToPagerAdapter(){
        PagerAdapter sectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new MainFragment());
        //TODO tuatj dodaj wiecej tabow
        ViewPager viewPager = findViewById(R.id.pagesContainer);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_programs);
    }

    /**
     * Change bottom navigation bar animations
     */
    private void changeBottomNavbarLook(){
        Log.d(TAG, "changeBottomNavbarLook: starting");

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationbar);
        BottomNavigationbarHelper.changeBottomNavbarLook(bottomNavigationViewEx);
        BottomNavigationbarHelper.setupNavigationBetweenActivities(MainActivity.this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }
}
