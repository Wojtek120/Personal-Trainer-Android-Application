package com.wojtek120.personaltrainer.plans;

import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.dialog.NewPlanDialog;
import com.wojtek120.personaltrainer.dialog.NewPlanDialog_;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.utils.database.PlansService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Activity with all training programs
 */
@EActivity(R.layout.activity_plans)
public class PlansActivity extends AppCompatActivity implements NewPlanDialog.OnConfirmAddPlanListener {
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

        plansService.setListViewWithUserPlans(listView, progressBar);

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

        NewPlanDialog_ newPlanDialog = new NewPlanDialog_();
        newPlanDialog.show(getSupportFragmentManager(), NewPlanDialog.TAG);

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
}
