package com.wojtek120.personaltrainer.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.PlanModel;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.dialog_plan)
public class PlanDialog extends DialogFragment {

    public static final String TAG = "PlanDialog";

    @ViewById
    EditText planNameEt;

    private PlanModel plan = null;
    private String planId;

    OnConfirmAddEditPlanListener onConfirmAddEditPlanListener;

    public interface OnConfirmAddEditPlanListener {
        void onConfirmAddPlan(String planName);
        void onConfirmEditPlan(PlanModel plan, String planId);
    }

    @AfterViews
    void setUpPlanDialog() {
        handleArguments();
    }

    /**
     * Fill EditTexts if argument is set
     */
    private void handleArguments() {

        Bundle arguments = getArguments();
        if (arguments != null) {
            plan = (PlanModel) getArguments().getSerializable("plan");
            planId = getArguments().getString("planId");
            planNameEt.setText(plan.getName());
        }

    }


    @Click(R.id.confirmButtonDialog)
    void onClickListenerToConfirmButton() {

        String planName = planNameEt.getText().toString();

        if (!planName.isEmpty()) {

            handleCallbackFunctions(planName);

        } else {
            String emptyPlanNameMessage = getContext().getString(R.string.lack_of_plan_name);
            ToastMessage.showMessage(getActivity(), emptyPlanNameMessage);
        }

    }


    /**
     * Call proper callback function
     */
    private void handleCallbackFunctions(String planName) {

        if (checkIfAddingNewPlan()) {
            onConfirmAddEditPlanListener.onConfirmAddPlan(planName);
        } else {
            plan.setName(planName);
            onConfirmAddEditPlanListener.onConfirmEditPlan(plan, planId);
        }

        getDialog().dismiss();
    }


    /**
     * Plan isn't null only if plan is edited
     *
     * @return true if dialog is responsible for adding new plan
     */
    private boolean checkIfAddingNewPlan() {
        return plan == null;
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
            onConfirmAddEditPlanListener = (OnConfirmAddEditPlanListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, ":: onAttach" + e.getMessage());
        }

    }

}
