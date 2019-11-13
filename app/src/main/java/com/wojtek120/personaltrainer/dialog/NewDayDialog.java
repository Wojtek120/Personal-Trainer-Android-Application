package com.wojtek120.personaltrainer.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.model.DayModel;
import com.wojtek120.personaltrainer.utils.ToastMessage;

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


@EFragment(R.layout.dialog_add_day)
public class NewDayDialog extends DialogFragment {

    public static final String TAG = "NewDayDialog";

    private Context context = getActivity();

    @ViewById
    EditText dayDescriptionEt;
    @ViewById
    EditText dateEt;

    OnConfirmAddDayListener onConfirmAddDayListener;

    public interface OnConfirmAddDayListener {
        void onConfirmAddDay(DayModel day);
    }

    @Click(R.id.confirmButtonDialog)
    void onClickListenerToConfirmButton() {

        String dayDescription = dayDescriptionEt.getText().toString();
        String dateStr = dateEt.getText().toString();

        if (!dayDescription.isEmpty() && !dateStr.isEmpty()) {

            Date date = getDateFromString(dateStr);

            DayModel day = new DayModel(date, dayDescription);

            onConfirmAddDayListener.onConfirmAddDay(day);
            getDialog().dismiss();
        } else {
            String errorMessage = getContext().getString(R.string.fill_all_fields);
            ToastMessage.showMessage(getActivity(), errorMessage);
        }

    }

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
            onConfirmAddDayListener = (OnConfirmAddDayListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, ":: onAttach" + e.getMessage());
        }

    }

}
