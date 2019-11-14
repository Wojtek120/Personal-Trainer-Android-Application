package com.wojtek120.personaltrainer.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.ExerciseModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.dialog_long_click)
public class LongClickExerciseDialog extends DialogFragment {

    public static final String TAG = "LongClickExerciseDialog";

    private ExerciseModel exercise;
    private String exerciseId;
    private OnLongClickExerciseListener onLongClickExerciseListener;

    public interface OnLongClickExerciseListener {
        void onEditExercise(ExerciseModel exercise, String exerciseId);
        void onDeleteExercise(String exerciseId);
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
            exercise = (ExerciseModel) getArguments().getSerializable("exercise");
            exerciseId = getArguments().getString("exerciseId");

            Log.d(TAG, "Got exercise to edit " + exerciseId + " => " + exercise.toString());
        }

    }

    @Click
    void editBtn() {
        Log.d(TAG, "Edited btn clicked, editing " + exercise.toString());

        onLongClickExerciseListener.onEditExercise(exercise, exerciseId);
        getDialog().dismiss();
    }

    @Click
    void deleteBtn() {
        Log.d(TAG, "Delete btn clicked");

        onLongClickExerciseListener.onDeleteExercise(exerciseId);
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
            onLongClickExerciseListener = (OnLongClickExerciseListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, ":: onAttach" + e.getMessage());
        }

    }

}
