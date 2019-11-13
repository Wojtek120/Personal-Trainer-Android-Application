package com.wojtek120.personaltrainer.plans;

import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.NewExerciseDialog;
import com.wojtek120.personaltrainer.dialog.NewExerciseDialog_;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.model.ExerciseModel;
import com.wojtek120.personaltrainer.utils.database.ExercisesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_exercises)
public class ExercisesActivity extends AppCompatActivity implements NewExerciseDialog.OnConfirmAddExerciseListener {
    private final static String TAG = "ExercisesActivity";

    private static final int ACTIVITY_NUMBER = ActivityNumbers.PLANS_ACTIVITY;

    @Bean
    ExercisesService exercisesService;

    @ViewById(R.id.exercisesListView)
    ListView listView;
    @ViewById
    ProgressBar progressBar;

    @Extra
    String planId;
    @Extra
    String dayId;
    @Extra
    String date;
    @Extra
    String planName;

    @AfterViews
    void setUpExercisesActivity() {

        addPlansToListView();

    }


    /**
     * Add all user plans to ListView
     */
    private void addPlansToListView() {
        Log.d(TAG, "adding plans to ListView");

        exercisesService.setListViewWithExercises(listView, planId, dayId, progressBar);

    }


    /**
     * Setup bottom navbar
     */
    @ViewById(R.id.bottomNavigationbar)
    void bottomNavbarSetup(BottomNavigationViewEx bottomNavigationViewEx){
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, ExercisesActivity.this, ACTIVITY_NUMBER);
    }


    /**
     * Sets plan name to title on layout
     */
    @ViewById(R.id.exercisesTitle)
    void setPlanNameToTitle(TextView title) {
        title.setText(planName);
    }

    /**
     * Sets plan date to title on layout
     */
    @ViewById(R.id.exercisesDate)
    void setPlanDateToTitle(TextView dateTv) {
        dateTv.setText(date);
    }


    /**
     * Back icon - finishes activity
     */
    @Click(R.id.backIcon)
    void backToPlans() {
        finish();
    }


    /**
     * Call dialog box to add new exercise.
     * This dialog calls callback function to add plan
     */
    @Click(R.id.addIcon)
    void addDay() {

        NewExerciseDialog_ newExerciseDialog = new NewExerciseDialog_();
        newExerciseDialog.show(getSupportFragmentManager(), NewExerciseDialog.TAG);

    }


    @Override
    public void onConfirmAddExercise(ExerciseModel exercise) {
        Log.d(TAG, "Adding " + exercise.toString());

        exercisesService.addNewExercise(exercise, progressBar, this);
    }
}
