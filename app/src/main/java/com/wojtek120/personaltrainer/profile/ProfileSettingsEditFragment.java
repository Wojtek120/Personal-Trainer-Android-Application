package com.wojtek120.personaltrainer.profile;

import android.content.Intent;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_profile_settings_edit)
public class ProfileSettingsEditFragment extends Fragment {

    private final String TAG = "ProfileSettingsEditFragment";

    @ViewById(R.id.photo)
    ImageView photo;


    @AfterViews
    void setupEditProfileSettings() {
        changeProfilePhoto();
    }

    //TODO temporary - just for testing
    private void changeProfilePhoto() {
        String URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Björnsson_Arnold_Classic_2017.jpg/1200px-Björnsson_Arnold_Classic_2017.jpg";
        ImageLoaderSingleton.displayImage("", URL, photo, null);
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
