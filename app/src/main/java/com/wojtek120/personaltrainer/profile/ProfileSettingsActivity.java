package com.wojtek120.personaltrainer.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wojtek120.personaltrainer.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileSettingsActivity extends AppCompatActivity {
    private final String TAG = "ProfileSettingsActivity";
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        addAllOptionsToSettingList();
        addOnClickListenerToBackArrow();

        Log.d(TAG, "created settings");
    }

    /**
     * Add all options (like: edit profile, log out)
     * to ListView which is in profile settings
     */
    private void addAllOptionsToSettingList() {
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
    }

    private void addOnClickListenerToBackArrow() {
        ImageView imageView = findViewById(R.id.backIcon);
        imageView.setOnClickListener(v -> finish());
    }
}
