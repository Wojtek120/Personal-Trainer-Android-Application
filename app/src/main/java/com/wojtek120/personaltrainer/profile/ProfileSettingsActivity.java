package com.wojtek120.personaltrainer.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.utils.adapter.StatePageAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileSettingsActivity extends AppCompatActivity {
    private final String TAG = "ProfileSettingsActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.PROFILE_ACTIVITY;
    private Context context = this;
    private StatePageAdapter statePageAdapter;
    private ViewPager viewPager;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        viewPager = findViewById(R.id.pagesContainer);
        relativeLayout = findViewById(R.id.profileSettingsLayout1);

        configureAllOptionsToSettingList();
        addOnClickListenerToBackArrow();
        addAllFragments();
        bottomNavbarSetup();

        Log.d(TAG, "created settings");
    }

    /**
     * Add all options (like: edit profile, log out)
     * to ListView which is in profile settings
     */
    private void configureAllOptionsToSettingList() {
        Log.d(TAG, "adding options to profile setting");
        ListView listView = findViewById(R.id.profileOptionsListView);

        ArrayList<String> options = new ArrayList<>(Arrays.asList(
                getString(R.string.edit_profile),
                getString(R.string.log_out)
        ));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_expandable_list_item_1,
                options);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> navigateToFragment(position));
    }

    /**
     * Add on click listener to back arrow
     */
    private void addOnClickListenerToBackArrow() {
        ImageView imageView = findViewById(R.id.backIcon);
        imageView.setOnClickListener(v -> finish());
    }

    /**
     * Add all fragments to options in ListView
     */
    private void addAllFragments() {
        statePageAdapter = new StatePageAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        statePageAdapter.addFragment(new ProfileSettingsEditFragment_(), getString(R.string.edit_profile));
        statePageAdapter.addFragment(new ProfileSettingsLogOutFragment_(), getString(R.string.log_out));
    }

    /**
     * Method responsible for navigating to fragment
     */
    private void navigateToFragment(int fragmentNumber) {
        relativeLayout.setVisibility(View.GONE);
        viewPager.setAdapter(statePageAdapter);
        viewPager.setCurrentItem(fragmentNumber);
    }



    /**
     * Setup bottom navbar
     */
    private void bottomNavbarSetup(){
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationbar);

        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, context, ACTIVITY_NUMBER);
    }
}
