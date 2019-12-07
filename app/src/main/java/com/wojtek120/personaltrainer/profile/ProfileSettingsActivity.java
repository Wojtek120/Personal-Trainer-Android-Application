package com.wojtek120.personaltrainer.profile;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.ConfirmPasswordDialog;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.utils.adapter.StatePageAdapter;
import com.wojtek120.personaltrainer.services.ProfileService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;

@EActivity(R.layout.activity_profile_settings)
public class ProfileSettingsActivity extends AppCompatActivity implements ConfirmPasswordDialog.OnConfirmPasswordListener {
    private final String TAG = "ProfileSettingsActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.PROFILE_ACTIVITY;
    private Context context = this;
    private StatePageAdapter statePageAdapter;

    @Bean
    ProfileService profileService;

    @ViewById(R.id.pagesContainer)
    ViewPager viewPager;
    @ViewById(R.id.profileSettingsLayout1)
    RelativeLayout relativeLayout;
    @ViewById(R.id.profileOptionsListView)
    ListView listView;

    @Extra
    String startingFragment;

    @AfterViews
    void SetUpProfileSettingsActivity() {
        configureAllOptionsToSettingList();
        addAllFragments();

        redirectIfExtraExists();
    }


    /**
     * Add all options (like: edit profile, log out)
     * to ListView which is in profile settings
     */
    private void configureAllOptionsToSettingList() {
        Log.d(TAG, "adding options to profile setting");

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
     * Add all fragments to options in ListView
     */
    private void addAllFragments() {
        statePageAdapter = new StatePageAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        statePageAdapter.addFragment(new ProfileSettingsEditFragment_(), getString(R.string.edit_profile));
        statePageAdapter.addFragment(new ProfileSettingsLogOutFragment_(), getString(R.string.log_out));
    }

    /**
     * Checks if redirects to specified fragment
     * based on incoming extra
     */
    private void redirectIfExtraExists() {

        if (startingFragment == null) {
            return;
        }

        int fragmentNumber = statePageAdapter.getFragmentNumber(startingFragment);
        navigateToFragment(fragmentNumber);

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
     * Add on click listener to back arrow
     */
    @Click(R.id.backIcon)
    void addOnClickListenerToBackArrow() {
        finish();
    }

    /**
     * Setup bottom navbar
     */
    @ViewById(R.id.bottomNavigationbar)
    void bottomNavbarSetup(BottomNavigationViewEx bottomNavigationViewEx) {
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, context, ACTIVITY_NUMBER);
    }


    /**
     * Callback function of dialogbox in which user write his password in order to change email
     * Here reauthentication and change email address method is called
     *
     * @param password - user password
     * @param newEmail - new email
     */
    @Override
    public void OnConfirmPassword(String password, String newEmail) {
        Log.d(TAG, ":: OnConfirmPassword password: " + password + " email " + newEmail);
        profileService.changeEmail(newEmail, password);
    }
}
