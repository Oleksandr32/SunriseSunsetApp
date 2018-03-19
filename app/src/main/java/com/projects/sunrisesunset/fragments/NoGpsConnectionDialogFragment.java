package com.projects.sunrisesunset.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.projects.sunrisesunset.R;

/**
 * Created by Alex on 02.03.2018.
 */

public class NoGpsConnectionDialogFragment extends DialogFragment implements  DialogInterface.OnClickListener {

    // dialog fragment tag
    public static final String DIALOG_TAG = NoGpsConnectionDialogFragment.class.getSimpleName();
    // tag for log filter
    public static final String LOG_TAG = "myLogs";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder( getActivity())
                .setTitle( R.string.dialog_no_gps_title )
                .setMessage( R.string.dialog_no_gps_message )
                .setCancelable( false )
                .setPositiveButton( R.string.btn_continue_title, this )
                .setNegativeButton( R.string.btn_cancel_title, this );
        return adb.create();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d( LOG_TAG, DIALOG_TAG + " onDismiss" );
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch ( which ) {
            case DialogInterface.BUTTON_POSITIVE:
                startActivity( new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS ) );
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dismiss();
                break;
        }
    }
}
