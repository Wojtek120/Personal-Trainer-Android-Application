package com.wojtek120.personaltrainer.utils.database;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.home.MainActivity;
import com.wojtek120.personaltrainer.utils.ToastMessage;

public class AuthenticationFacade {

    private static final String TAG = "AuthenticationFacade";

    private AuthenticationFacade() {
    }

    /**
     * Check if the user is currently signed in
     * @return - true if user is logged, otherwise false
     */
    public static boolean isSignedIn() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null ) {
            Log.d(TAG, "user isn't logged in");
            return false;
        } else {
            Log.d(TAG, "Logged user: " + currentUser.getEmail());
            return true;
        }
    }

    /**
     * Authenticate user and redirect to main page if success
     * @param email - e-mail address
     * @param password - user password
     * @param activity - activity
     * @param progressBar - progress bar to hide when authentication is completed
     */
    public static void authenticateAndRedirectOnSuccess(String email, String password, Activity activity, ProgressBar progressBar) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if(isEmailVerified()) {

                            Log.d(TAG, "sign in success");
                            redirectToMainActivity(activity);

                        } else {

                            Log.d(TAG, "sign in failed, email not verified");
                            String emailNotVerifiedMessage = activity.getString(R.string.email_not_verified);
                            ToastMessage.showMessage(activity, emailNotVerifiedMessage);

                            firebaseAuth.signOut();
                        }

                    } else {
                        Log.w(TAG, "sign in failed", task.getException());

                        String failedMessage = activity.getString(R.string.authentification_failed);
                        ToastMessage.showMessage(activity, failedMessage);
                    }

                    progressBar.setVisibility(View.GONE);
                });
    }


    /**
     * Get current user
     * @return current user or null if isn't logged
     */
    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }



    /**
     * Get current user id
     * @return current user id or null if isn't logged
     */
    public static String getIdOfCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    /**
     * Check if e-mail is verified
     * @return true if e-mail is verified else false
     */
    public static boolean isEmailVerified() {

        if(getCurrentUser() == null) {
            return false;
        }

        return getCurrentUser().isEmailVerified();
    }


    /**
     * Redirects to main activity if user is logged in
     * @param activity - current activity
     */
    private static void redirectToMainActivity(Activity activity){
        if(AuthenticationFacade.getCurrentUser() != null) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
