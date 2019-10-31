package com.wojtek120.personaltrainer.utils.database;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.UserDetails;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Data access object to Firebase Database - Cloud Firestone
 * Used for connection to database to register/sign out user
 * Written in singleton pattern.
 */
@EBean(scope = EBean.Scope.Singleton)
public class UserService {

    @RootContext
    Context context;

    private static final String TAG = "UserService";
    private FirebaseFirestore database;
    private ProgressBar progressBar;
    private Activity activity;


    @AfterInject
    void setUserService() {
        Log.d(TAG, ":: initialize");
        database = FirebaseFirestore.getInstance();
    }


    /**
     * Register new user to database
     * First checks if all data are written and if are valid
     * then checks if username already exists
     * then register new authentication data
     * then add user details
     * at the end send verification e-mail
     *
     * @param email            - e-mail address
     * @param username         - username
     * @param password         - password
     * @param repeatedPassword - repeated password
     * @param activityArg         - activity
     * @param progressBarArg      - progress bar to set visibility to gone after registration
     */
    public void registerNewUser(String email, String username, String password, String repeatedPassword, Activity activityArg, ProgressBar progressBarArg) {

        progressBar = progressBarArg;
        activity = activityArg;

        if (validateRegistrationData(email, username, password, repeatedPassword)) {

            database.collection(DatabaseCollectionNames.USER_DETAILS)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            if (isUsernameNew(task.getResult(), username)) {

                                //add new authentication data if username doesn't exist
                                addNewUserDataAndSendVerificationEmail(email, password, username);

                                signOut();

                            } else {
                                String usernameExistsMessage = activity.getString(R.string.username_exists);
                                ToastMessage.showMessage(activity, usernameExistsMessage);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            progressBar.setVisibility(View.GONE);
                        }

                    });
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }

    /**
     * Sign out currently logged user
     */
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Add authentication data to database
     * send verification email
     * after all call method to add user details
     *
     * @param email    - e-mail address
     * @param password - password
     * @param username - username
     */
    private void addNewUserDataAndSendVerificationEmail(String email, String password, String username) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Task<AuthResult> authTask = firebaseAuth.createUserWithEmailAndPassword(email, password);
        authTask.addOnCompleteListener(activity, task -> {

            if (task.isSuccessful()) {
                Log.d(TAG, "create user success");

                FirebaseUser user = firebaseAuth.getCurrentUser();

                //send verification email
                sendVerificationEmail();

                //add user details
                addUserDetailsToDatabase(user.getUid(), username);


            } else {
                Log.w(TAG, "create user failure", task.getException());
                progressBar.setVisibility(View.GONE);

                String failMessage = activity.getString(R.string.registration_fail);
                ToastMessage.showMessage(activity, failMessage);
            }
        });
    }

    /**
     * Send verification e-mail to new registered user
     */
    private void sendVerificationEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentUser.sendEmailVerification()
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "SendingVerificationEmail::Success");

                            String emailVerificationMessage = context.getString(R.string.verification_email_send);
                            ToastMessage.showMessage(context, emailVerificationMessage);
                            progressBar.setVisibility(View.GONE);

//                            Intent intent = new Intent(context, RegisterActivity_.class);
//                            context.startActivity(intent);

                            activity.finish();

                        } else {
                            Log.d(TAG, "SendingVerificationEmail::Error");

                            String emailVerificationErrorMessage = context.getString(R.string.verification_email_error);
                            ToastMessage.showMessage(context, emailVerificationErrorMessage);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    /**
     * Add registered user details to database
     * Id of document in firestore is the same as id of the user
     *
     * @param userId   - id of user used in firestore
     * @param username - e-mail address
     */
    private void addUserDetailsToDatabase(String userId, String username) {

        UserDetails user = new UserDetails(userId, username, "", "", 0., 0., 0.);

        database.collection(DatabaseCollectionNames.USER_DETAILS)
                .document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    /**
     * Check if in result from database username exists
     * ignores upper/lower case
     *
     * @param resultOfTask - result from db
     * @param username     - username to check
     * @return true if username is valid (doesn't exists), otherwise false
     */
    private boolean isUsernameNew(QuerySnapshot resultOfTask, String username) {

        for (QueryDocumentSnapshot document : resultOfTask) {
            Log.d(TAG, document.getId() + " => " + document.getData());

            String currentUsername = document.toObject(UserDetails.class).getUsername();
            if (currentUsername.toLowerCase().equals(username.toLowerCase())) {
                return false;
            }
        }

        return true;
    }


    /**
     * Checks if string with email or password is empty,
     * checks if passwords are the same
     * checks if password is min 6 character long
     * and shows error message if is
     *
     * @param email            - string with email
     * @param password         - string with password
     * @param repeatedPassword - string with repeated password
     * @return - false if data is valid, otherwise true
     */
    private boolean validateRegistrationData(String email, String username, String password, String repeatedPassword) {

        if (email.isEmpty()) {
            String emailEmptyMessage = context.getString(R.string.fill_email);
            ToastMessage.showMessage(context, emailEmptyMessage);
            return false;
        }

        if (username.isEmpty()) {
            String usernameEmptyMessage = context.getString(R.string.fill_username);
            ToastMessage.showMessage(context, usernameEmptyMessage);
            return false;
        }

        if (password.isEmpty() || repeatedPassword.isEmpty()) {
            String passwordEmptyMessage = context.getString(R.string.fill_password);
            ToastMessage.showMessage(context, passwordEmptyMessage);
            return false;
        }

        if (!password.equals(repeatedPassword)) {
            String differentPasswordMessage = context.getString(R.string.different_password);
            ToastMessage.showMessage(context, differentPasswordMessage);
            return false;
        }

        if (password.length() < 6) {
            String tooShortPassword = context.getString(R.string.too_short_password);
            ToastMessage.showMessage(context, tooShortPassword);
        }

        return true;
    }


}