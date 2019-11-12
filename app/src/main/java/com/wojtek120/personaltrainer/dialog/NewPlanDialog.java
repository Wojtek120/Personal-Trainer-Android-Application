package com.wojtek120.personaltrainer.dialog;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.dialog_add_plan)
public class NewPlanDialog extends DialogFragment {

    public static final String TAG = "NewPlanDialog";

    @ViewById
    EditText planNameEt;

    OnConfirmAddPlanListener onConfirmAddPlanListener;

    public interface OnConfirmAddPlanListener {
        void onConfirmAddPlan(String planName);
    }

    @Click(R.id.confirmButtonDialog)
    void onClickListenerToConfirmButton() {

        String planName = planNameEt.getText().toString();

        if (!planName.isEmpty()) {
            onConfirmAddPlanListener.onConfirmAddPlan(planName);
            getDialog().dismiss();
        } else {
            String emptyPlanNameMessage = getContext().getString(R.string.lack_of_plan_name);
            ToastMessage.showMessage(getActivity(), emptyPlanNameMessage);
        }

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
            onConfirmAddPlanListener = (OnConfirmAddPlanListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, ":: onAttach" + e.getMessage());
        }

    }

}
