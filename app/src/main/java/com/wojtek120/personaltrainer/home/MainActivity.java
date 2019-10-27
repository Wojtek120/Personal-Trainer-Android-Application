package com.wojtek120.personaltrainer.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.utils.adapter.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.MAIN_ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavbarSetup();
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
     * Setup bottom navbar
     */
    private void bottomNavbarSetup(){
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationbar);

        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, MainActivity.this, ACTIVITY_NUMBER);
    }
}
