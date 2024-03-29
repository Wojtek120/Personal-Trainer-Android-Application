package com.wojtek120.personaltrainer.plans;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.DayDialog;
import com.wojtek120.personaltrainer.dialog.DayDialog_;
import com.wojtek120.personaltrainer.dialog.LongClickDayDialog;
import com.wojtek120.personaltrainer.dialog.PlanDialog;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.model.DayModel;
import com.wojtek120.personaltrainer.services.DaysService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_days)
public class DaysActivity extends AppCompatActivity implements DayDialog.OnConfirmAddEditDayListener, LongClickDayDialog.OnLongClickDayListener {
    private final static String TAG = "DaysActivity";
    private static final int ACTIVITY_NUMBER = ActivityNumbers.PLANS_ACTIVITY;

    @Extra
    String planId;
    @Extra
    String planName;

    @Bean
    DaysService daysService;

    @ViewById(R.id.daysListView)
    ListView listView;
    @ViewById
    ProgressBar progressBar;

    @AfterViews
    void setUpPlansActivity() {

        addDaysToListView();

    }


    /**
     * Add all user plans to ListView
     */
    private void addDaysToListView() {
        Log.d(TAG, "adding plans to ListView");

        daysService.setListViewWithDaysOfSelectedPlan(listView, planId, progressBar, this);

    }


    /**
     * Sets plan name to title on layout
     */
    @ViewById(R.id.daysTitle)
    void setPlanNameToTitle(TextView title) {
        title.setText(planName);
    }


    /**
     * Setup bottom navbar
     */
    @ViewById(R.id.bottomNavigationbar)
    void bottomNavbarSetup(BottomNavigationViewEx bottomNavigationViewEx) {
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, this, ACTIVITY_NUMBER);
    }


    @Click(R.id.backIcon)
    void backToPlans() {
        finish();
    }


    /**
     * Call dialog box to add new plan.
     * This dialog calls callback function to add plan
     */
    @Click(R.id.addIcon)
    void addDay() {

        DayDialog_ newDayDialog = new DayDialog_();
        newDayDialog.show(getSupportFragmentManager(), DayDialog.TAG);

    }

    /**
     * Callback function from dialog box
     *
     * @param day - model with new day data
     */
    @Override
    public void onConfirmAddDay(DayModel day) {
        Log.d(TAG, "Adding " + day.toString());

        daysService.addNewDay(day, progressBar, this);
    }


    /**
     * Callback function from dialog box
     *
     * @param day - model with new day data
     * @param dayId - edited day id
     */
    @Override
    public void onConfirmEditDay(DayModel day, String dayId) {
        Log.d(TAG, "Saving edited day " + day.toString());
        daysService.updateDay(day, dayId, progressBar, this);
    }


    @Override
    public void onEditDay(DayModel day, String dayId) {
        Log.d(TAG, "calling dialog to edit day " + day.toString());

        Bundle arguments = new Bundle();
        arguments.putSerializable("day", day);
        arguments.putString("dayId", dayId);

        DayDialog_ newDayDialog = new DayDialog_();
        newDayDialog.setArguments(arguments);
        newDayDialog.show(getSupportFragmentManager(), PlanDialog.TAG);
    }

    @Override
    public void onDeleteDay(String dayId) {
        Log.d(TAG, "onDeleteDay " + dayId);

        daysService.deleteDay(dayId, progressBar, this);
    }
}
