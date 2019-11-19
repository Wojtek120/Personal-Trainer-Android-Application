package com.wojtek120.personaltrainer.predefined;

import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.utils.database.PredefinedPlansService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity with predefined plans
 */
@EActivity(R.layout.activity_predefined_plans)
public class PredefinedPlansActivity extends AppCompatActivity {
    private final static String TAG = "PredefinedPlansActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.PREDEFINED_PLANS_ACTIVITY;

    @Bean
    PredefinedPlansService predefinedPlansService;

    @ViewById
    ProgressBar progressBar;

    private String addPlanTitle;
    private String addPlanMessage;
    private String confirmStr;
    private String cancelStr;

    @AfterViews
    void setupPredefinedPlans() {
        progressBar.setVisibility(View.GONE);
    }

    @AfterViews
    void setUpPlansActivity() {
        addPlanTitle = getString(R.string.add_predefined_plan_title);
        addPlanMessage = getString(R.string.add_predefined_plan_message);
        confirmStr = getString(R.string.confirm);
        cancelStr = getString(R.string.cancel);
    }


    /**
     * Add on click listener to add button of 5/3/1 program
     */
    @Click(R.id.addButton531)
    void add531Plan() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(addPlanTitle)
                .setMessage(addPlanMessage)
                .setPositiveButton(confirmStr, (dialogInterface, i) -> {
                    Log.d(TAG, "Adding 531");

                    pickDateAndCopyPlan("neRBR8PoW6KeesQJBv0b");


                })
                .setNegativeButton(cancelStr, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void pickDateAndCopyPlan(String documentId) {

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        DatePickerDialog picker = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) ->
                {
                    try {

                        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dayOfMonth + "." + (monthOfYear + 1) + "." + year1);
                        Log.d(TAG, "Picked date " + date);
                        predefinedPlansService.copyPlan(documentId, date, progressBar);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }, year, month, day);
        picker.show();

    }


    /**
     * Add on click listener to add button of Texas Method program
     */
    @Click(R.id.addButtonTexas)
    void addTexasPlan() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(addPlanTitle)
                .setMessage(addPlanMessage)
                .setPositiveButton(confirmStr, (dialogInterface, i) -> {
                    Log.d(TAG, "Adding texas method");

                    pickDateAndCopyPlan("texas_method");

                })
                .setNegativeButton(cancelStr, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }



    /**
     * Add on click listener to add button of Strong Lifts 5x5 program
     */
    @Click(R.id.addButtonStrongLifts)
    void addStrongLiftsPlan() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(addPlanTitle)
                .setMessage(addPlanMessage)
                .setPositiveButton(confirmStr, (dialogInterface, i) -> {
                    Log.d(TAG, "Adding dtrong lifts");
                    pickDateAndCopyPlan("lzBbgzUN6dYVu54A8NfQ");
                })
                .setNegativeButton(cancelStr, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    /**
     * Setup bottom navbar
     */
    @ViewById(R.id.bottomNavigationbar)
    void bottomNavbarSetup(BottomNavigationViewEx bottomNavigationViewEx) {
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, PredefinedPlansActivity.this, ACTIVITY_NUMBER);
    }
}
