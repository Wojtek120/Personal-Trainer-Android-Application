package com.wojtek120.personaltrainer.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wojtek120.personaltrainer.R;

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
     * Authenticate user
     * @param email - e-mail address
     * @param password - user password
     * @param activity - activity
     * @param progressBar - progress bar to hide when authentication is completed
     */
    public static void authenticate(String email, String password, Activity activity, ProgressBar progressBar) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "sign in success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                    } else {
                        Log.w(TAG, "sign in failed", task.getException());

                        String failedMessage = activity.getString(R.string.authentification_failed);
                        ToastMessage.showMessage(activity, failedMessage);
                    }

                    progressBar.setVisibility(View.GONE);
                });
    }
}
