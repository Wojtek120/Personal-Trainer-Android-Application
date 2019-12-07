package com.wojtek120.personaltrainer.profile.photo;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;
import com.wojtek120.personaltrainer.utils.adapter.GridViewAdapter;
import com.wojtek120.personaltrainer.services.ProfileService;
import com.wojtek120.personaltrainer.utils.files.ListPaths;
import com.wojtek120.personaltrainer.utils.files.Paths;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

@EFragment(R.layout.fragment_gallery)
public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    private static final int NUM_OF_COLUMNS_IN_GRID_VIEW = 4;
    private static final String FILE_PREFIX = "file:/";

    @Bean
    Paths paths;
    @Bean
    ListPaths listPaths;
    @Bean
    ImageLoaderSingleton imageLoaderSingleton;
    @Bean
    ProfileService profileService;

    @ViewById
    ImageView mainPhoto;
    @ViewById
    GridView galleryPhotos;
    @ViewById
    Spinner spinner;
    @ViewById
    ProgressBar progressBar;

    private ArrayList<String> directoriesPaths;
    private String pathToChosenImage;

    @AfterViews
    void SetUpGalleryFragment() {
        initializeSpinner();
        setUpOnClickListenerToSpinner();
        setWidthOfColumnInGallery();
    }


    /**
     * Initialize spinner to show directoriesPaths with photos
     */
    private void initializeSpinner() {

        directoriesPaths = listPaths.getPathsToDirectoriesFromPath(paths.PICTURES);
        directoriesPaths.add(0, paths.DCIM);

        ArrayList<String> directoriesName = getNamesOfDirectoriesFromPath();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, directoriesName);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }


    /**
     * Get name of directoriesPaths form directoriesPaths paths
     * used to display it in spinner
     *
     * @return name of directoriesPaths
     */
    private ArrayList<String> getNamesOfDirectoriesFromPath() {
        ArrayList<String> directoriesName = new ArrayList<>();

        for(String path : directoriesPaths) {
            directoriesName.add(new File(path).getName());
        }

        return directoriesName;
    }

    /**
     * Set on click listener to back icon,
     * which finishes activity
     */
    @Click(R.id.backIcon)
    void onClickListenerToBackIcon() {
        Log.d(TAG, "Back icon clicked. Going back to edit profile");
        getActivity().finish();
    }


    /**
     * Set on click listener to save photo icon
     */
    @Click(R.id.saveIcon)
    void onClickListenerToSaveIcon() {
        Log.d(TAG, "Saving profile photo");

        progressBar.setVisibility(View.VISIBLE);
        profileService.saveProfilePhoto(pathToChosenImage, progressBar);
    }

    /**
     *  Set on click listener to spinner
     */
    private void setUpOnClickListenerToSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, ":: Spinner dir :: " + directoriesPaths.get(i));
                putPhotosInGridView(directoriesPaths.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Puts all photos from path directory (@param) to GridView
     *
     * @param pathToDirectory - path to directory to take photos from
     */
    private void putPhotosInGridView(String pathToDirectory) {
        ArrayList<String> photosPath = listPaths.getPathsToFilesFromPath(pathToDirectory);

        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), photosPath, R.layout.layout_grid_view_image, FILE_PREFIX, imageLoaderSingleton);
        galleryPhotos.setAdapter(gridViewAdapter);

        setImageToMainPhoto(photosPath.get(0));
        pathToChosenImage = photosPath.get(0);

        galleryPhotos.setOnItemClickListener((adapterView, view, i, l) -> {
            setImageToMainPhoto(photosPath.get(i));
            pathToChosenImage = photosPath.get(i);
        });
    }


    /**
     * Sets width of column in gallery GridView
     * Takes width and divide it by number of columns
     */
    private void setWidthOfColumnInGallery() {
        int width = getResources().getDisplayMetrics().widthPixels;
        int photoWidth = width/NUM_OF_COLUMNS_IN_GRID_VIEW;
        galleryPhotos.setColumnWidth(photoWidth);
    }


    /**
     * Puts image from param to main ImageView
     *
     * @param imagePath - path to image to display
     */
    private void setImageToMainPhoto(String imagePath) {
        imageLoaderSingleton.displayImage(FILE_PREFIX,imagePath, mainPhoto, progressBar);
    }
}
