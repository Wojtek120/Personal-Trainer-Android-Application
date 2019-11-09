package com.wojtek120.personaltrainer.plans;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.general.ActivityNumbers;
import com.wojtek120.personaltrainer.general.BottomNavigationBarSetup;
import com.wojtek120.personaltrainer.utils.database.PlansService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Activity with all training programs
 */
@EActivity(R.layout.activity_plans)
public class PlansActivity extends AppCompatActivity {
    private final static String TAG = "PlansActivity";

    private static final int ACTIVITY_NUMBER = ActivityNumbers.PLANS_ACTIVITY;
    private Context context;

    @Bean
    PlansService plansService;

    @ViewById(R.id.plansListView)
    ListView listView;
    @ViewById
    ProgressBar progressBar;

    @AfterViews
    void setUpPlansActivity() {
        context = this;

        addPlansToListView();

//        addAllFragments();

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
    void bottomNavbarSetup(BottomNavigationViewEx bottomNavigationViewEx){
        BottomNavigationBarSetup bottomNavigationBarSetup = new BottomNavigationBarSetup();
        bottomNavigationBarSetup.setupNavigationBar(bottomNavigationViewEx, PlansActivity.this, ACTIVITY_NUMBER);
    }
}
