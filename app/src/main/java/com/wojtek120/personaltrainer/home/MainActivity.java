package com.wojtek120.personaltrainer.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.ExerciseDialog;
import com.wojtek120.personaltrainer.dialog.ExerciseDialog_;
import com.wojtek120.personaltrainer.dialog.LongClickExerciseDialog;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.model.ExerciseModel;
import com.wojtek120.personaltrainer.utils.database.ExercisesService;
import com.wojtek120.personaltrainer.utils.database.HomeService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity  implements ExerciseDialog.OnConfirmAddEditExerciseListener, LongClickExerciseDialog.OnLongClickExerciseListener{

    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.MAIN_ACTIVITY;

    @Bean
    HomeService homeService;
    @Bean
    ExercisesService exercisesService;

    @ViewById(R.id.exercisesListView)
    ListView listView;
    @ViewById
    ProgressBar progressBar;



    @AfterViews
    void setupMain() {
        bottomNavbarSetup();

        homeService.setListViewWithExercisesByGivenDate(listView, new Date(), progressBar, MainActivity.this);
    }

    /**
     * Sets plan date to title on layout
     */
    @ViewById(R.id.exercisesDate)
    void setPlanDateToTitle(TextView dateTv) {

        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy");
        dateTv.setText(formatter.format(new Date()));
    }


    /**
     * Setup bottom navbar
     */
    private void bottomNavbarSetup(){
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationbar);

        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, MainActivity.this, ACTIVITY_NUMBER);
    }

    @Override
    public void onConfirmAddExercise(ExerciseModel exercise) {
        Log.d(TAG, "Adding " + exercise.toString());

        exercisesService.addNewExercise(exercise, progressBar, this);
    }

    /**
     * Callback function from dialog box
     *
     * @param exercise   - model with new exercise data
     * @param exerciseId - edited exercise id
     */
    @Override
    public void onConfirmEditExercise(ExerciseModel exercise, String exerciseId) {
        Log.d(TAG, "Saving edited exercise " + exercise.toString());
        exercisesService.updateExercise(exercise, exerciseId, progressBar, this);
    }

    @Override
    public void onEditExercise(ExerciseModel exercise, String exerciseId) {
        Log.d(TAG, "calling dialog to exercise day " + exercise.toString());

        Bundle arguments = new Bundle();
        arguments.putSerializable("exercise", exercise);
        arguments.putString("exerciseId", exerciseId);

        ExerciseDialog_ exerciseDialog = new ExerciseDialog_();
        exerciseDialog.setArguments(arguments);
        exerciseDialog.show(getSupportFragmentManager(), ExerciseDialog.TAG);
    }

    @Override
    public void onDeleteExercise(String exerciseId) {
        Log.d(TAG, "onDeleteExercise " + exerciseId);

        exercisesService.deleteExercise(exerciseId, progressBar, this);

    }
}
