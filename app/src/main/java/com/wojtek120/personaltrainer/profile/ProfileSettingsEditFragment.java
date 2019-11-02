package com.wojtek120.personaltrainer.profile;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.filter.DecimalInputFilter;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;
import com.wojtek120.personaltrainer.utils.NumberUtils;
import com.wojtek120.personaltrainer.utils.ToastMessage;
import com.wojtek120.personaltrainer.utils.database.ProfileService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_profile_settings_edit)
public class ProfileSettingsEditFragment extends Fragment {

    private final String TAG = "ProfileSettingsEditFragment";

    private Context context;

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
        context = getContext();

        addFiltersToEditTexts();
        fillProfileEditTextsInInforFromDatabase();
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

        progressBar.setVisibility(View.GONE);
    }

    /**
     * Calls function to fill EditTexts in data downloaded from database
     */
    private void fillProfileEditTextsInInforFromDatabase() {
        profileService.fillProfileEditInfo(usernameEditText, emailEditText, descriptionEditText, squatMaxEditText, benchMaxEditText, deadliftMaxEditText, photo, progressBar);
    }

    /**
     * Add filters to maxes EditText
     */
    private void addFiltersToEditTexts() {
        squatMaxEditText.setFilters(new InputFilter[]{new DecimalInputFilter()});
        benchMaxEditText.setFilters(new InputFilter[]{new DecimalInputFilter()});
        deadliftMaxEditText.setFilters(new InputFilter[]{new DecimalInputFilter()});
    }

    /**
     * On click listener to save icon
     * Saves data from EditTexts to database
     */
    @Click(R.id.saveIcon)
    void onClickSaveIcon() {

        progressBar.setVisibility(View.VISIBLE);

        String squat = squatMaxEditText.getText().toString();
        String bench = benchMaxEditText.getText().toString();
        String deadlift = deadliftMaxEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if (!validateValues(squat, bench, deadlift, username, email, description)) {
            progressBar.setVisibility(View.GONE);
            return;
        }

    }


    /**
     * Validates values from EditBoxes
     *
     * @param max1    - string with max from EditText
     * @param max2    - string with max from EditText
     * @param max3    - string with max from EditText
     * @param strings - rest of strings to validate
     * @return true if values are valid, otherwise false
     */
    private boolean validateValues(String max1, String max2, String max3, String... strings) {

        if (!areMaxesAreNumeric(max1, max2, max3)) {
            String errorMessage = getString(R.string.type_number_in_maxes);
            ToastMessage.showMessage(context, errorMessage);

            return false;
        }

        if(!areStringsFilled(strings)) {
            String errorMessage = getString(R.string.fill_all_fields);
            ToastMessage.showMessage(context, errorMessage);

            return false;
        }

        return  true;
    }

    /**
     * Checks if maxes from EditTexts are numeric
     *
     * @param strings - strings to validate
     * @return true if maxes are numeric, otherwise false
     */
    private boolean areMaxesAreNumeric(String... strings) {
        for (String str : strings) {
            if (!NumberUtils.isNumeric(str)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Checks if strings aren't empty
     *
     * @param strings - strings to validate
     * @return false if any string is empty, otherwise true
     */
    private boolean areStringsFilled(String... strings) {
        for (String str : strings) {
            if (str.isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
