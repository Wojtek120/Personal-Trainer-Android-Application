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

@EFragment(R.layout.dialog_changing_email)
public class ConfirmPasswordDialog extends DialogFragment {

    public static final String TAG = "ConfirmPasswordDialog";
    private String newEmail;

    @ViewById(R.id.passwordConfirmEt)
    EditText passwordEt;

    OnConfirmPasswordListener onConfirmPasswordListener;

    public interface OnConfirmPasswordListener {
        public void OnConfirmPassword(String password, String newEmail);
    }

    @Click(R.id.confirmButtonDialog)
    void onClickListenerToConfirmButton() {

        String password = passwordEt.getText().toString();

        if (password.length() > 5) {
            onConfirmPasswordListener.OnConfirmPassword(password, newEmail);
            getDialog().dismiss();
        } else {
            String passwordToShortMessage = getContext().getString(R.string.too_short_password);
            ToastMessage.showMessage(getActivity(), passwordToShortMessage);
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
            onConfirmPasswordListener = (OnConfirmPasswordListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, ":: onAttach" + e.getMessage());
        }

    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
