package com.wojtek120.personaltrainer.startup;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.wojtek120.personaltrainer.authentication.LoginActivity_;
import com.wojtek120.personaltrainer.services.AuthenticationFacade;

public class StartUp extends Application {

    private static final String TAG = "StartUp:";

    @Override
    public void onCreate() {

        Log.d(TAG, "running start up config method");

        super.onCreate();

        redirectToLoggingActivityIfIsNotSignInOrVerified();
    }

    /**
     * Redirects to logging activity if user isn't signed in or email isn't verified
     */
    private void redirectToLoggingActivityIfIsNotSignInOrVerified() {

        if (AuthenticationFacade.getCurrentUser() == null || !AuthenticationFacade.isSignedIn() || !AuthenticationFacade.isEmailVerified()) {
            Intent intent = new Intent(this, LoginActivity_.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

}
