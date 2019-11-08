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
import com.wojtek120.personaltrainer.utils.files.ListPaths;
import com.wojtek120.personaltrainer.utils.files.Paths;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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

    @ViewById
    ImageView mainPhoto;
    @ViewById
    GridView galleryPhotos;
    @ViewById
    Spinner spinner;
    @ViewById
    ProgressBar progressBar;

    ArrayList<String> directories;

    @AfterViews
    void SetUpGalleryFragment() {
        initializeSpinner();
        setUpOnClickListenerToSpinner();
        setWidthOfColumnInGallery();
    }


    /**
     * Initialize spinner to show directories with photos
     */
    private void initializeSpinner() {

        directories = listPaths.getPathsToDirectoriesFromPath(paths.PICTURES);
        directories.add(0, paths.DCIM);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, directories);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
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
    }

    /**
     *  Set on click listener to spinner
     */
    private void setUpOnClickListenerToSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, ":: Spinner dir :: " + directories.get(i));
                putPhotosInGridView(directories.get(i));
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

        galleryPhotos.setOnItemClickListener((adapterView, view, i, l) -> setImageToMainPhoto(photosPath.get(i)));
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
     * @param imagePath
     */
    private void setImageToMainPhoto(String imagePath) {
        imageLoaderSingleton.displayImage(FILE_PREFIX,imagePath, mainPhoto, progressBar);
    }
}
