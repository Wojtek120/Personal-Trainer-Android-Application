package com.wojtek120.personaltrainer.profile.photo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.Permissions;
import com.wojtek120.personaltrainer.utils.ToastMessage;
import com.wojtek120.personaltrainer.utils.adapter.StatePageAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_edit_profile_photo)
public class EditProfilePhotoActivity extends AppCompatActivity {

    private final String TAG = "EditProfilePhotoActivity";

    @ViewById(R.id.pagesContainer)
    ViewPager viewPager;
    @ViewById(R.id.editPhotoBottomTabs)
    TabLayout tabLayout;

    @Bean
    Permissions permissions;


    @AfterViews
    void setUpEditProfilePhoto() {

        if (!handlePermissions()) {
            ToastMessage.showMessage(this, getString(R.string.lack_of_permissions));
        }

        addAllFragmentsToBottomNavBar();

    }



    /**
     * Handle permissions, if not all are granted,
     * ask user to grant permission
     *
     * @return true if all permissions are granted, otherwise false
     */
    private boolean handlePermissions() {
        if (permissions.checkAllPermission()) {
            return true;
        } else {
            permissions.askUserToGrantPermissions(EditProfilePhotoActivity.this);
            return false;
        }
    }


    /**
     * Add all fragments to bottom navbar
     */
    private void addAllFragmentsToBottomNavBar() {
        String gallery = getString(R.string.gallery);
        String camera = getString(R.string.camera);

        StatePageAdapter statePageAdapter = new StatePageAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        statePageAdapter.addFragment(new GalleryFragment_(), gallery);
        statePageAdapter.addFragment(new CameraFragment_(), camera);

        viewPager.setAdapter(statePageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(gallery);
        tabLayout.getTabAt(1).setText(camera);

    }

}

