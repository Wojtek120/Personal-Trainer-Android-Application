package com.wojtek120.personaltrainer.profile;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.authentication.LoginActivity_;
import com.wojtek120.personaltrainer.utils.database.UserService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_profile_settings_logout)
public class ProfileSettingsLogOutFragment extends Fragment {

    private final String TAG = "ProfileSettingsLogOutFragment";

    @Bean
    UserService userService;

    /**
     * Set on click listener to sign out button
     * When button is clicked, user is logged out,
     * flags are set in order to couldn't go back (on back button) to activity
     * and user is redirect to logging page
     */
    @Click(R.id.logoutConfirmButton)
    void setOnClickListenerToSignOutButton() {

        userService.signOut();

        Intent intent = new Intent(getActivity(), LoginActivity_.class);
        getActivity().finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Set on click listener to cancel button
     * After hitting cancel button user is taking back to settings
     */
    @Click(R.id.logoutCancelButton)
    void setOnClickListenerToCancelButton() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}
