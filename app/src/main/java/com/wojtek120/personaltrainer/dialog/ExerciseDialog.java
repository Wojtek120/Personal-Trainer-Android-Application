package com.wojtek120.personaltrainer.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.ExerciseModel;
import com.wojtek120.personaltrainer.utils.Exercises;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.dialog_add_exercise)
public class ExerciseDialog extends DialogFragment {

    public static final String TAG = "ExerciseDialog";

    private Context context = getActivity();

    @Bean
    Exercises exercises;

    @ViewById
    Spinner exerciseSpinner;
    @ViewById
    EditText setsEt;
    @ViewById
    EditText repsEt;
    @ViewById
    EditText intensityEt;


    private ExerciseModel exercise = null;
    private String exerciseId;


    OnConfirmAddEditExerciseListener onConfirmAddEditExerciseListener;

    public interface OnConfirmAddEditExerciseListener {
        void onConfirmAddExercise(ExerciseModel exercise);
        void onConfirmEditExercise(ExerciseModel exercise, String exerciseId);
    }

    @AfterViews
    void setUpExerciseDialog() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, exercises.getEXERCISES_LIST(getActivity()));
        exerciseSpinner.setAdapter(adapter);

        handleArguments();

    }

    /**
     * Fill EditTexts if argument is set
     */
    private void handleArguments() {

        Bundle arguments = getArguments();
        if (arguments != null) {
            exercise = (ExerciseModel) getArguments().getSerializable("exercise");
            exerciseId = getArguments().getString("exerciseId");

            int exerciseSpinnerIndex = exercises.getEXERCISES_LIST(getActivity()).indexOf(exercise.getName());

            exerciseSpinner.setSelection(exerciseSpinnerIndex);
            setsEt.setText(Integer.toString(exercise.getSets()));
            repsEt.setText(Integer.toString(exercise.getReps()));
            intensityEt.setText(exercise.getIntensity());
        }

    }

    @Click(R.id.confirmButtonDialog)
    void onClickListenerToConfirmButton() {

        String exercise = exerciseSpinner.getSelectedItem().toString();
        String setsStr = setsEt.getText().toString();
        String repsStr = repsEt.getText().toString();
        String intensity = intensityEt.getText().toString();

        if (dataIsValid(exercise, setsStr, repsStr, intensity)) {

            int sets = Integer.parseInt(setsStr);
            int reps = Integer.parseInt(repsStr);

            ExerciseModel exerciseModel = new ExerciseModel(exercise, 0, intensity, reps, sets, 0);

            handleCallbackFunctions(exerciseModel);

        }

    }

    /**
     * Call proper callback function
     */
    private void handleCallbackFunctions(ExerciseModel exerciseModel) {

        if (checkIfAddingNewExercise()) {
            onConfirmAddEditExerciseListener.onConfirmAddExercise(exerciseModel);
        } else {

            exerciseModel.setOrder(exercise.getOrder());
            exerciseModel.setSetsDone(exercise.getSetsDone());

            onConfirmAddEditExerciseListener.onConfirmEditExercise(exerciseModel, exerciseId);
        }

        getDialog().dismiss();
    }


    /**
     * Plan isn't null only if plan is edited
     *
     * @return true if dialog is responsible for adding new plan
     */
    private boolean checkIfAddingNewExercise() {
        return exercise == null;
    }

    /**
     * Validate strings
     *
     * @param exercise
     * @param setsStr
     * @param repsStr
     * @param intensity
     * @return true if data is valid
     */
    private boolean dataIsValid(String exercise, String setsStr, String repsStr, String intensity) {

        if (exercise.isEmpty() || setsStr.isEmpty() || repsStr.isEmpty() || intensity.isEmpty()) {
            ToastMessage.showMessage(getActivity(), getActivity().getString(R.string.fill_all_fields));

            return false;
        }

        return true;

    }


    @Click(R.id.cancelButtonDialog)
    void onClickListenerToCancelButton() {
        getDialog().dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Log.d(TAG, ":: onAttach started");

        try {
            onConfirmAddEditExerciseListener = (OnConfirmAddEditExerciseListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, ":: onAttach" + e.getMessage());
        }

    }

}
