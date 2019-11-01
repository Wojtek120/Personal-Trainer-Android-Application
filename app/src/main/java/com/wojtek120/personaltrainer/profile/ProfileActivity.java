package com.wojtek120.personaltrainer.profile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;
import com.wojtek120.personaltrainer.utils.database.ProfileService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Activity with settings
 */
@EActivity(R.layout.activity_profile)
public class ProfileActivity extends AppCompatActivity {
    private final static String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.PROFILE_ACTIVITY;

    Context context;

    @Bean
    ImageLoaderSingleton imageLoader;
    @Bean
    ProfileService profileService;

    @ViewById(R.id.progressBarInProfileLayout)
    ProgressBar progressBar;
    @ViewById(R.id.imageProfile)
    ImageView photo;
    @ViewById(R.id.profileNameDisplayUnderPhoto)
    TextView usernameTv;
    @ViewById(R.id.profileDescriptionDisplayUnderPhoto)
    TextView descriptionTv;
    @ViewById
    TextView usernameHeaderTv;
    @ViewById
    TextView squatMaxTv;
    @ViewById
    TextView benchMaxTv;
    @ViewById
    TextView deadliftMaxTv;


    @AfterViews
    void setUpProfileActivity() {

        context = this;

        setProfileInformation();
    }

    /**
     * Set toolbar
     */
    @ViewById(R.id.profileTabs)
    void setToolbar(Toolbar toolbar){
        Log.d(TAG, ":: setting toolbar");
        setSupportActionBar(toolbar);
    }


    /**
     * Setup bottom navbar
     */
    @ViewById(R.id.bottomNavigationbar)
    void bottomNavbarSetup(BottomNavigationViewEx bottomNavigationViewEx){
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, ProfileActivity.this, ACTIVITY_NUMBER);
    }


    /**
     * Add on click listener to dots to open menu
     */
    @Click(R.id.menuProfile)
    void setOnClickListenerToMenu() {
        Log.d(TAG, "Profile settings opening");
        Intent intent = new Intent(context, ProfileSettingsActivity_.class);
        startActivity(intent);
    }

    /**
     * Add on click listener to edit profile button
     * redirects to edit profile fragment
     */
    @Click(R.id.textEditProfile)
    void addOnClickListenerToEditProfileBtn() {
        Intent intent = new Intent(context, ProfileSettingsActivity_.class);
        intent.putExtra("startingFragment", getString(R.string.edit_profile));
        startActivity(intent);
    }

    /**
     * Set up profile info with data from database
     */
    private void setProfileInformation() {
        profileService.fillProfileInfo(usernameTv, usernameHeaderTv, descriptionTv, squatMaxTv, benchMaxTv, deadliftMaxTv, photo, progressBar);
    }

}
