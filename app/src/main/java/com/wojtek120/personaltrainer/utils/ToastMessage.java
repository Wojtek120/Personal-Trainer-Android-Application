package com.wojtek120.personaltrainer.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastMessage {

    private ToastMessage() {

    }

    public static void showMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
