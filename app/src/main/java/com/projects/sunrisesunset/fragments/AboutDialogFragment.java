package com.projects.sunrisesunset.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import  android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.projects.sunrisesunset.R;

/**
 * Created by Alex on 01.03.2018.
 */

public class AboutDialogFragment extends DialogFragment {

    // dialog fragment tag
    public static final String DIALOG_TAG = AboutDialogFragment.class.getSimpleName();
    // tag for log filter
    public static final String LOG_TAG = "myLogs";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle( R.string.dialog_about_title ) // set the dialog title
                .setPositiveButton( R.string.btn_continue_title, null )
                .setMessage( R.string.dialog_about_message );
        return adb.create();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d( LOG_TAG, DIALOG_TAG + " onDismiss" );
    }
}
