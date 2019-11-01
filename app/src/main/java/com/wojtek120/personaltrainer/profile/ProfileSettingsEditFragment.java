package com.wojtek120.personaltrainer.profile;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;
import com.wojtek120.personaltrainer.utils.database.ProfileService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_profile_settings_edit)
public class ProfileSettingsEditFragment extends Fragment {

    private final String TAG = "ProfileSettingsEditFragment";

    @Bean
    ImageLoaderSingleton imageLoader;
    @Bean
    ProfileService profileService;

    @ViewById
    ProgressBar progressBar;

    @ViewById
    ImageView photo;
    @ViewById
    EditText usernameEditText;
    @ViewById
    EditText emailEditText;
    @ViewById
    EditText descriptionEditText;
    @ViewById
    EditText squatMaxEditText;
    @ViewById
    EditText benchMaxEditText;
    @ViewById
    EditText deadliftMaxEditText;



    @AfterViews
    void setupEditProfileSettings() {
        profileService.fillProfileEditInfo(usernameEditText, emailEditText, descriptionEditText, squatMaxEditText, benchMaxEditText, deadliftMaxEditText, photo, progressBar);
    }

    /**
     * Add on click listener to back arrow
     * It restarts current activity, so it navigates back to settings
     * it looks like you click back button
     */
    @Click(R.id.backIcon)
    void addOnClickListenerToBackArrow() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

}
