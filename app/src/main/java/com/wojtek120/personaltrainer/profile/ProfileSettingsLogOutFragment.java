package com.wojtek120.personaltrainer.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wojtek120.personaltrainer.R;

public class ProfileSettingsLogOutFragment extends Fragment {

    private final String TAG = "ProfileSettingsLogOutFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile_settings_logout, container, false);
    }

    
}
