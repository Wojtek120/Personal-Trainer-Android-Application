package com.wojtek120.personaltrainer.utils.database;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.ConfirmPasswordDialog;
import com.wojtek120.personaltrainer.dialog.ConfirmPasswordDialog_;
import com.wojtek120.personaltrainer.model.UserDetails;
import com.wojtek120.personaltrainer.profile.ProfileActivity_;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

/**
 * Data access object to Firebase Database - Cloud Firestone
 * Responsible for operations in profile activity
 * Written in singleton pattern.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ProfileService {
    private static final String TAG = "ProfileService";
    private static final String PROFILE_PHOTO_PATH_IN_DB = "profile_photos/";

    @RootContext
    Context context;

    @Bean
    ImageLoaderSingleton imageLoader;
    @Bean
    UserService userService;

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


    /**
     * Saves data to database, if username is unique
     *
     * @param user        - structure with user details to save
     * @param email       - email to save
     * @param progressBar - ProgressBar to hide
     * @param activity    - activity, used to get fragment manager
     */
    public void save(UserDetails user, String email, ProgressBar progressBar, AppCompatActivity activity) {

        CollectionReference users = database.collection(DatabaseCollectionNames.USER_DETAILS);
        Query query = users.whereEqualTo("username", user.getUsername());
        query.get().addOnCompleteListener(task -> {

            if (userService.isUsernameUnique(task)) {

                checkIfEmailChangedAndUpdate(user, email, progressBar, activity);

            } else {
                progressBar.setVisibility(View.GONE);
            }


        });
    }

    /**
     * Checks if email changed and update
     * Also get profile photolink from db and put it in structure
     *
     * @param user        - structure with user details to save
     * @param email       - email to save
     * @param progressBar - ProgressBar to hide
     * @param activity    - AppCompatActivity, used to get fragment manager
     */
    private void checkIfEmailChangedAndUpdate(UserDetails user, String email, ProgressBar progressBar, AppCompatActivity activity) {
        DocumentReference docRef = database.collection(DatabaseCollectionNames.USER_DETAILS).document(AuthenticationFacade.getIdOfCurrentUser());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            UserDetails loadedUser = documentSnapshot.toObject(UserDetails.class);
            user.setProfilePhoto(loadedUser.getProfilePhoto());


            if (doesEmailChanged(email)) {
                updateUserDetails(docRef, user, progressBar);
                updateUserEmail(email, activity);
            } else {
                updateUserDetails(docRef, user, progressBar);
            }

        });
    }


    /**
     * Call dialog box with where user should write password.
     * This dialog calls callback function to reauthenticate
     * and save new email address
     *
     * @param email    - email to save
     * @param activity - activity, used to get fragment manager
     */
    private void updateUserEmail(String email, AppCompatActivity activity) {

        ConfirmPasswordDialog_ confirmPasswordDialog = new ConfirmPasswordDialog_();
        confirmPasswordDialog.setNewEmail(email);
        confirmPasswordDialog.show(activity.getSupportFragmentManager(), ConfirmPasswordDialog.TAG);

    }


    /**
     * Update user details in database
     *
     * @param docRef      - DocumentReference to user details document
     * @param user        - structure with user details to save
     * @param progressBar - ProgressBar to hide
     */
    private void updateUserDetails(DocumentReference docRef, UserDetails user, ProgressBar progressBar) {

        docRef.set(user);

        progressBar.setVisibility(View.GONE);
        String confirmationMessage = context.getString(R.string.changes_saved);
        ToastMessage.showMessage(context, confirmationMessage);
    }


    /**
     * Checks if email changed
     *
     * @param email - user email to check
     * @return true if email changed, if not return false
     */
    private boolean doesEmailChanged(String email) {
        return !email.equals(AuthenticationFacade.getEmailOfCurrentUser());
    }

    /**
     * Reauthenticate user and change email
     *
     * @param newEmail - new email address
     * @param password - password to reauthentication
     */
    public void changeEmail(String newEmail, String password) {

        FirebaseUser user = AuthenticationFacade.getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(AuthenticationFacade.getEmailOfCurrentUser(), password);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Log.d(TAG, "User re-authenticated.");

                        checkIfEmailExists(newEmail);

                    } else {
                        Log.d(TAG, "User re-authenticated failed.");
                        String errorMessage = context.getString(R.string.wrong_password);
                        ToastMessage.showMessage(context, errorMessage);
                    }
                });

    }

    /**
     * Checks if email exists.
     * If not updates user email.
     *
     * @param newEmail - new email
     */
    private void checkIfEmailExists(String newEmail) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.fetchSignInMethodsForEmail(newEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().getSignInMethods().size() == 0) {
                        updateEmail(newEmail);
                    }

                    if (task.getResult().getSignInMethods().size() > 0) {
                        Log.d(TAG, "Email already exists");
                        ToastMessage.showMessage(context, context.getString(R.string.email_in_use));
                    }


                }
            }
        });
    }


    /**
     * Updates email in database
     *
     * @param newEmail - new email
     */
    private void updateEmail(String newEmail) {
        AuthenticationFacade.getCurrentUser().updateEmail(newEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email updated");


                        String emailUpdatedMessage = context.getString(R.string.email_updated);
                        ToastMessage.showMessage(context, emailUpdatedMessage);

                        userService.sendVerificationEmail();
                    } else {
                        String errorMessage = context.getString(R.string.something_went_wrong);
                        ToastMessage.showMessage(context, errorMessage);
                    }
                });

    }


    /**
     * Save profile photography to database and change profile photo
     * Photo is named as userid
     *
     * @param pathToPhoto - path to photo to save
     * @param progressBar - progressBar to hide when uploaded
     */
    public void saveProfilePhoto(String pathToPhoto, ProgressBar progressBar) {

        byte[] image = getByteImageFromPath(pathToPhoto);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(PROFILE_PHOTO_PATH_IN_DB + AuthenticationFacade.getIdOfCurrentUser());

        UploadTask uploadTask = storageReference.putBytes(image);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);

                throw task.getException();
            }
            // Continue with the task to get the download URL
            return storageReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {


                Uri downloadUri = task.getResult();

                Log.d(TAG, "image uploaded :: " + downloadUri.toString());
                changeProfilePhotoLinkInDb(downloadUri.toString(), progressBar);


            } else {
                ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                progressBar.setVisibility(View.GONE);
            }


        });

    }

    /**
     * Change link in db to new one from param
     *
     * @param newPhotoLink - new link to photo
     * @param progressBar - progrssbar to hide
     */
    private void changeProfilePhotoLinkInDb(String newPhotoLink, ProgressBar progressBar) {

        database.collection(DatabaseCollectionNames.USER_DETAILS).document(AuthenticationFacade.getIdOfCurrentUser())
                .update("profilePhoto", newPhotoLink)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        ToastMessage.showMessage(context, context.getString(R.string.image_changed));

                        Intent intent = new Intent(context, ProfileActivity_.class);
                        context.startActivity(intent);

                    } else {
                        ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
                    }

                    progressBar.setVisibility(View.GONE);
                });

    }


    /**
     * Get compressed to jpeg byte[] from path to image
     *
     * @param path - path to image
     * @return byte[] with image from path
     */
    private byte[] getByteImageFromPath(String path) {

        Bitmap bitmap = getBitmapFromPath(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        return baos.toByteArray();
    }


    /**
     * Get bitmap from path to image
     *
     * @param path - path to image
     * @return bitmap with image from path
     */
    private Bitmap getBitmapFromPath(String path) {
        Bitmap bitmap = null;
        File image = new File(path);

        try (FileInputStream fileInputStream = new FileInputStream(image)) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
