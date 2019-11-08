package com.wojtek120.personaltrainer.profile.photo;

import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.profile.ProfileSettingsActivity_;
import com.wojtek120.personaltrainer.utils.Permissions;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

@EFragment
public class CameraFragment extends Fragment {

    private static final String TAG = "CameraFragment";
    private static final int REQUEST_CODE_FOR_CAMERA = 11;

    @Bean
    Permissions permissions;

    @Override
    public void onResume() {
        super.onResume();


        if(permissions.checkAllPermission()) {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, REQUEST_CODE_FOR_CAMERA);
        } else {
            ToastMessage.showMessage(getContext(), getString(R.string.lack_of_permissions));
            goBackToEditProfileSettings();
        }


    }

    /**
     * Goes back to edit profile settings
     * clears activity stack
     */
    private void goBackToEditProfileSettings() {
        Intent intent = new Intent(getActivity(), ProfileSettingsActivity_.class);
        intent.putExtra("startingFragment", getString(R.string.edit_profile));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_CAMERA) {
            Log.d(TAG, ":: getting photo");
        }
    }
}
