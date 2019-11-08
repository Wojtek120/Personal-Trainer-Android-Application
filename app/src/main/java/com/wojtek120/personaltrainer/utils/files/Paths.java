package com.wojtek120.personaltrainer.utils.files;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import lombok.Getter;

@EBean(scope = EBean.Scope.Singleton)
@Getter
public class Paths {
    private static final String TAG = "Paths";

    @RootContext
    Context context;

    public final String DCIM;
    public final String PICTURES;

    private final String CAMERA_APPEND = "/CAMERA";


    public Paths() {
        DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + CAMERA_APPEND;
        PICTURES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();

        Log.d(TAG, "DCIM:: " + DCIM);
        Log.d(TAG, "PICTURES:: " + PICTURES);
    }
}
