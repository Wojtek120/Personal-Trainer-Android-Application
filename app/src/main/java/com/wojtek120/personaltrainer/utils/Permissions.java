package com.wojtek120.personaltrainer.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean(scope = EBean.Scope.Singleton)
public class Permissions {

    private static final String TAG = "Permissions";
    private static final int PERMISSIONS_REQUEST = 1;

    @RootContext
    Context context;

    private final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    /**
     * Asks user to grant permissions
     *
     * @param activity - activity
     */
    public void askUserToGrantPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSIONS_REQUEST);
    }

    /**
     * Checks permissions are granted,
     *
     * @return true if all are granted, false otherwise
     */
    public boolean checkAllPermission() {

        Log.d(TAG, ":: verifying permissions");

        for (String permission : PERMISSIONS) {
            if (!checkPermission(permission)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Checks single permission
     *
     * @param permission - permission to check
     * @return true is permission is granted, otherwise false
     */
    private boolean checkPermission(String permission) {

        Log.d(TAG, ":: checking permission " + permission);


        int permissionCheck = ActivityCompat.checkSelfPermission(context, permission);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        Log.d(TAG, ":: permission not granted: " + permission);

        return false;
    }


}
