package com.wojtek120.personaltrainer.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;

public class ProfileSettingsEditFragment extends Fragment {

    private final String TAG = "ProfileSettingsEditFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_settings_edit, container, false);

        addOnClickListenerToBackArrow(view);
        changeProfilePhoto(view);


        return view;
    }

    //TODO temporary - just for testing
    private void changeProfilePhoto(View view) {

        ImageView photo = view.findViewById(R.id.photo);
        String URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Björnsson_Arnold_Classic_2017.jpg/1200px-Björnsson_Arnold_Classic_2017.jpg";
        ImageLoaderSingleton.displayImage("", URL, photo, null);
    }

    /**
     * Add on click listener to back arrow
     * It restarts current activity, so it navigates back to settings
     * it looks like you click back button
     * @param view - current view
     */
    private void addOnClickListenerToBackArrow(View view) {
        ImageView imageView = view.findViewById(R.id.backIcon);
        imageView.setOnClickListener(v -> {
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        });
    }

}
