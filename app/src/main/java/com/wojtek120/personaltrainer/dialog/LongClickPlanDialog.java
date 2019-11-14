package com.wojtek120.personaltrainer.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.PlanModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.dialog_long_click)
public class LongClickPlanDialog extends DialogFragment {

    public static final String TAG = "LongClickPlanDialog";

    private PlanModel plan;
    private String planId;
    private OnLongClickPlanListener onLongClickPlanListener;

    public interface OnLongClickPlanListener {
        void onEditPlan(PlanModel plan, String planId);
        void onDeletePlan(String planId);
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
            plan = (PlanModel) getArguments().getSerializable("plan");
            planId = getArguments().getString("planId");
        }

    }

    @Click
    void editBtn() {
        Log.d(TAG, "Edited btn clicked");

        onLongClickPlanListener.onEditPlan(plan, planId);
        getDialog().dismiss();
    }

    @Click
    void deleteBtn() {
        Log.d(TAG, "Delete btn clicked");

        onLongClickPlanListener.onDeletePlan(planId);
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
            onLongClickPlanListener = (OnLongClickPlanListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, ":: onAttach" + e.getMessage());
        }

    }

}
