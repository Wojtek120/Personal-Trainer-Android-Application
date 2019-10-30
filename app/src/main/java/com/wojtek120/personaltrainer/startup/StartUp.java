package com.wojtek120.personaltrainer.startup;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.wojtek120.personaltrainer.authentication.LoginActivity_;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;
import com.wojtek120.personaltrainer.utils.database.AuthenticationFacade;

public class StartUp extends Application {

    private static final String TAG = "StartUp:";

    @Override
    public void onCreate() {

        Log.d(TAG, "running start up config method");

        super.onCreate();

        ImageLoaderSingleton.configure(this);

        redirectToLoggingActivityIfIsNotSignIn();
    }

    /**
     * Redirects to logging activity if user isn't signed in
     */
    private void redirectToLoggingActivityIfIsNotSignIn() {

        if (!AuthenticationFacade.isSignedIn()) {
            Intent intent = new Intent(this, LoginActivity_.class);
            startActivity(intent);
        }

    }

}
