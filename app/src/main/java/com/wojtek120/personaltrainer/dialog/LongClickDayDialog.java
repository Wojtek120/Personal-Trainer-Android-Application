package com.wojtek120.personaltrainer.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.DayModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.dialog_long_click)
public class LongClickDayDialog extends DialogFragment {

    public static final String TAG = "LongClickDayDialog";

    private DayModel day;
    private String dayId;
    private OnLongClickDayListener onLongClickDayListener;

    public interface OnLongClickDayListener {
        void onEditDay(DayModel day, String dayId);
        void onDeleteDay(String dayId);
    }

    @AfterViews
    void setUp() {
        handleArguments();
    }

    /**
     * Get model from args
     */
    private void handleArguments() {

        Bundle arguments = getArguments();
        if (arguments != null) {
            day = (DayModel) getArguments().getSerializable("day");
            dayId = getArguments().getString("dayId");
        }

    }

    @Click
    void editBtn() {
        Log.d(TAG, "Edited btn clicked");

        onLongClickDayListener.onEditDay(day, dayId);
        getDialog().dismiss();
    }

    @Click
    void deleteBtn() {
        Log.d(TAG, "Delete btn clicked");

        onLongClickDayListener.onDeleteDay(dayId);
        getDialog().dismiss();
    }

    @Click
    void cancelBtn() {
        getDialog().dismiss();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Log.d(TAG, ":: onAttach started");

        try {
            onLongClickDayListener = (OnLongClickDayListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, ":: onAttach" + e.getMessage());
        }

    }

}
