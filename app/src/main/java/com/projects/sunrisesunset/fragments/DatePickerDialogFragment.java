package com.projects.sunrisesunset.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import com.projects.sunrisesunset.activities.MainActivity;

import java.util.Calendar;

/**
 * Created by Alex on 01.03.2018.
 */

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    // dialog fragment tag
    public static final String DIALOG_TAG = DatePickerDialogFragment.class.getSimpleName();
    // tag for log filter
    public static final String LOG_TAG = "myLogs";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();

        return new DatePickerDialog( getActivity(), this,
                c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) );
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        ((MainActivity) getActivity()).onDateSelected( String.format( "%d-%d-%d", year, ++month, day ) );
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss( dialog );
        Log.d( LOG_TAG, DIALOG_TAG + " onDismiss: date was selected" );
    }
}
