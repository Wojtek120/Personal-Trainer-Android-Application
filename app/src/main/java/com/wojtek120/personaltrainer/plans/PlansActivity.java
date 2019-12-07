package com.wojtek120.personaltrainer.plans;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.LongClickPlanDialog;
import com.wojtek120.personaltrainer.dialog.PlanDialog;
import com.wojtek120.personaltrainer.dialog.PlanDialog_;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.model.PlanModel;
import com.wojtek120.personaltrainer.services.PlansService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Activity with all training programs
 */
@EActivity(R.layout.activity_plans)
public class PlansActivity extends AppCompatActivity implements PlanDialog.OnConfirmAddEditPlanListener, LongClickPlanDialog.OnLongClickPlanListener {
    private final static String TAG = "PlansActivity";

    private static final int ACTIVITY_NUMBER = ActivityNumbers.PLANS_ACTIVITY;

    @Bean
    PlansService plansService;

    @ViewById(R.id.plansListView)
    ListView listView;
    @ViewById
    ProgressBar progressBar;

    @AfterViews
    void setUpPlansActivity() {

        addPlansToListView();

    }


    /**
     * Add all user plans to ListView
     */
    private void addPlansToListView() {
        Log.d(TAG, "adding plans to ListView");

        plansService.setListViewWithUserPlans(listView, progressBar, this);

    }


    /**
     * Setup bottom navbar
     */
    @ViewById(R.id.bottomNavigationbar)
    void bottomNavbarSetup(BottomNavigationViewEx bottomNavigationViewEx) {
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, PlansActivity.this, ACTIVITY_NUMBER);
    }

    /**
     * Call dialog box to add new plan.
     * This dialog calls callback function to add plan
     */
    @Click(R.id.addIcon)
    void addPlan() {

        PlanDialog_ newPlanDialog = new PlanDialog_();
        newPlanDialog.show(getSupportFragmentManager(), PlanDialog.TAG);

    }

    /**
     * Callback function from dialog box
     *
     * @param planName - plan name
     */
    @Override
    public void onConfirmAddPlan(String planName) {
        plansService.addNewPlan(planName, progressBar, this);
    }

    @Override
    public void onConfirmEditPlan(PlanModel plan, String planId) {
        Log.d(TAG, "Saving edited plan " + plan.toString());
        plansService.updatePlan(plan, planId, progressBar, this);
    }

    /**
     * Called from long click dialog
     *
     * @param plan - model with clicked data
     */
    @Override
    public void onEditPlan(PlanModel plan, String planId) {
        Log.d(TAG, "onEditDay " + plan.toString());

        Bundle arguments = new Bundle();
        arguments.putSerializable("plan", plan);
        arguments.putString("planId", planId);

        PlanDialog_ newPlanDialog = new PlanDialog_();
        newPlanDialog.setArguments(arguments);
        newPlanDialog.show(getSupportFragmentManager(), PlanDialog.TAG);
    }

    @Override
    public void onDeletePlan(String planId) {
        Log.d(TAG, "onDeleteDay " + planId);

        plansService.deletePlan(planId, progressBar, this);
    }
}
