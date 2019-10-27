package com.wojtek120.personaltrainer.config;

import android.app.Application;
import android.util.Log;

import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;

public class StartUp extends Application {

    private static final String TAG = "StartUp:";

    @Override
    public void onCreate(){

        Log.d(TAG, "running start up config method");

        super.onCreate();
        ImageLoaderSingleton.configure(this);
    }
}
