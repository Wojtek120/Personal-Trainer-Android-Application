package com.wojtek120.personaltrainer.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.DayModel;
import com.wojtek120.personaltrainer.utils.ToastMessage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@EFragment(R.layout.dialog_day)
public class DayDialog extends DialogFragment {

    public static final String TAG = "DayDialog";

    private Context context = getActivity();

    @ViewById
    EditText dayDescriptionEt;
    @ViewById
    EditText dateEt;

    private DayModel day = null;
    private String dayId;

    OnConfirmAddEditDayListener onConfirmAddEditDayListener;

    public interface OnConfirmAddEditDayListener {
        void onConfirmAddDay(DayModel day);
        void onConfirmEditDay(DayModel day, String dayId);
    }

    @AfterViews
    void setUpDayDialog() {
        handleArguments();
    }

    /**
     * Fill EditTexts if argument is set
     */
    private void handleArguments() {

        Bundle arguments = getArguments();
        if (arguments != null) {
            day = (DayModel) getArguments().getSerializable("day");
            dayId = getArguments().getString("dayId");

            dayDescriptionEt.setText(day.getDescription());
            dateEt.setText(day.giveDateString());
        }

    }


    @Click(R.id.confirmButtonDialog)
    void onClickListenerToConfirmButton() {

        String dayDescription = dayDescriptionEt.getText().toString();
        String dateStr = dateEt.getText().toString();

        if (!dayDescription.isEmpty() && !dateStr.isEmpty()) {

            Date date = getDateFromString(dateStr);

            DayModel newDay = new DayModel(date, dayDescription);

            handleCallbackFunctions(newDay);

        } else {
            String errorMessage = getContext().getString(R.string.fill_all_fields);
            ToastMessage.showMessage(getActivity(), errorMessage);
        }

    }

    /**
     * Call proper callback function
     */
    private void handleCallbackFunctions(DayModel newDay) {

        if (checkIfAddingNewDay()) {
            onConfirmAddEditDayListener.onConfirmAddDay(newDay);
        } else {
            onConfirmAddEditDayListener.onConfirmEditDay(newDay, dayId);
        }

        getDialog().dismiss();
    }

    /**
     * Plan isn't null only if plan is edited
     *
     * @return true if dialog is responsible for adding new plan
     */
    private boolean checkIfAddingNewDay() {
        return day == null;
    }


    /**
     * Get Date from String with date text in format dd.MM.yyyy
     *
     * @param dateStr - date text in format dd.MM.yyyy
     * @return Date
     */
    private Date getDateFromString(String dateStr) {

        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        Date date = null;

        try {
            date = formatter.parse(dateStr);

        } catch (ParseException e) {
            ToastMessage.showMessage(context, context.getString(R.string.wrong_date_format));
        }


        return date;
    }


    /**
     * Calls pick up date dialog when EditText with date is touched
     *
     * @param v
     * @param motionEvent
     */
    @Touch(R.id.dateEt)
    void selectDate(View v, MotionEvent motionEvent) {
        Log.d(TAG, ":: date clicked");

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            DatePickerDialog picker = new DatePickerDialog(getActivity(),
                    (view, year1, monthOfYear, dayOfMonth) -> dateEt.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1), year, month, day);
            picker.show();
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
            onConfirmAddEditDayListener = (OnConfirmAddEditDayListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, ":: onAttach" + e.getMessage());
        }

    }

}
