package com.wojtek120.personaltrainer.utils.database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wojtek120.personaltrainer.model.UserDetails;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Locale;

/**
 * Data access object to Firebase Database - Cloud Firestone
 * Responsible for operations in profile activity
 * Written in singleton pattern.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ProfileService {

    @RootContext
    Context context;

    @Bean
    ImageLoaderSingleton imageLoader;

    private static final String TAG = "ProfileService";
    private FirebaseFirestore database;

    @AfterInject
    void setUserService() {
        Log.d(TAG, ":: initialize");
        database = FirebaseFirestore.getInstance();
    }


    /**
     * Fill user profile with data from database
     *
     * @param usernameTv       - TextView with username
     * @param usernameHeaderTv - TextView with username displayed in header
     * @param descriptionTv    - TextView with description of profile
     * @param squatTv          - TextView with squat max
     * @param benchTv          - TextView with bench max
     * @param deadliftTv       - TextView with deadlift max
     * @param profileImage     - ImageView with profile image
     * @param progressBar      - progress bar to turn off after loading
     */
    public void fillProfileInfo(TextView usernameTv, TextView usernameHeaderTv, TextView descriptionTv, TextView squatTv, TextView benchTv, TextView deadliftTv, ImageView profileImage, ProgressBar progressBar) {

        DocumentReference docRef = database.collection(DatabaseCollectionNames.USER_DETAILS).document(AuthenticationFacade.getIdOfCurrentUser());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            UserDetails user = documentSnapshot.toObject(UserDetails.class);

            if (user != null) {
                usernameTv.setText(user.getUsername());
                usernameHeaderTv.setText(user.getUsername());
                descriptionTv.setText(user.getDescription());
                squatTv.setText(String.format(Locale.ENGLISH, "%.2f", user.getSquatMax()));
                benchTv.setText(String.format(Locale.ENGLISH, "%.2f", user.getBenchMax()));
                deadliftTv.setText(String.format(Locale.ENGLISH, "%.2f", user.getDeadliftMax()));

                imageLoader.displayImage("", user.getProfilePhoto(), profileImage, null);
            }


            progressBar.setVisibility(View.GONE);
        });

    }


    /**
     * Fill user profile with data from database
     *
     * @param usernameEt    - EditText with username
     * @param emailEt       - EditText with email
     * @param descriptionEt - EditText with description of profile
     * @param squatEt       - EditText with squat max
     * @param benchEt       - EditText with bench max
     * @param deadliftEt    - EditText with deadlift max
     * @param profileImage  - ImageView with profile image
     * @param progressBar   - progress bar to turn off after loading
     */
    public void fillProfileEditInfo(EditText usernameEt, EditText emailEt, EditText descriptionEt, EditText squatEt, EditText benchEt, EditText deadliftEt, ImageView profileImage, ProgressBar progressBar) {

        DocumentReference docRef = database.collection(DatabaseCollectionNames.USER_DETAILS).document(AuthenticationFacade.getIdOfCurrentUser());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            UserDetails user = documentSnapshot.toObject(UserDetails.class);

            if (user != null) {
                usernameEt.setText(user.getUsername());
                descriptionEt.setText(user.getDescription());
                squatEt.setText(String.format(Locale.ENGLISH, "%.2f", user.getSquatMax()));
                benchEt.setText(String.format(Locale.ENGLISH, "%.2f", user.getBenchMax()));
                deadliftEt.setText(String.format(Locale.ENGLISH, "%.2f", user.getDeadliftMax()));

                emailEt.setText(AuthenticationFacade.getEmailOfCurrentUser());

                imageLoader.displayImage("", user.getProfilePhoto(), profileImage, null);
            }


            progressBar.setVisibility(View.GONE);
        });

    }


}
