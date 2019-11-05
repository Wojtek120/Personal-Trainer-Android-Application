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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
     * @param activityArg      - activity
     * @param progressBarArg   - progress bar to set visibility to gone after registration
     */
    public void registerNewUser(String email, String username, String password, String repeatedPassword, Activity activityArg, ProgressBar progressBarArg) {

        progressBar = progressBarArg;
        activity = activityArg;

        if (validateRegistrationData(email, username, password, repeatedPassword)) {

            CollectionReference users = database.collection(DatabaseCollectionNames.USER_DETAILS);
            Query query = users.whereEqualTo("username", username);
            query.get().addOnCompleteListener(task -> {

                if (isUsernameUnique(task)) {

                    //add new authentication data if username doesn't exist
                    addNewUserDataAndSendVerificationEmail(email, password, username);

                    signOut();

                } else {
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
    public void sendVerificationEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentUser.sendEmailVerification()
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "SendingVerificationEmail::Success");

                            String emailVerificationMessage = context.getString(R.string.verification_email_send);
                            ToastMessage.showMessage(context, emailVerificationMessage);

                            if(progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }

                            if(activity != null) {
                                activity.finish();
                            }

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
     * Checks if field is unique,
     * gets result of task and checks if it isn't empty
     * if it isn't empty checks if it doesn't belong to current user
     *
     * @param task - result of query
     * @return true if username is unique, otherwise show message and return false
     */
    public boolean isUsernameUnique(Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Log.d(TAG, document.getId() + " => " + document.getData());

                if(!AuthenticationFacade.isSignedIn() || !document.getId().equals(AuthenticationFacade.getIdOfCurrentUser())) {

                    ToastMessage.showMessage(context, context.getString(R.string.username_exists));
                    return false;
                }

            }

            return true;
        } else {
            ToastMessage.showMessage(context, context.getString(R.string.something_went_wrong));
            return false;
        }
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
