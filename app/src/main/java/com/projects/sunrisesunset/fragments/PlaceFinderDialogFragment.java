package com.projects.sunrisesunset.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.projects.sunrisesunset.R;
import com.projects.sunrisesunset.activities.MainActivity;

/**
 * Created by Alex on 02.03.2018.
 */

public class PlaceFinderDialogFragment extends DialogFragment
        implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    // dialog fragment tag
    public static final String DIALOG_TAG = PlaceFinderDialogFragment.class.getSimpleName();
    // tag for log filter
    public static final String LOG_TAG = "myLogs";

    // widgets
    private RadioGroup rgMode;
    private EditText etPlace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // init main view
        getDialog().setTitle( R.string.fragment_place_finder_title );
        View view = inflater.inflate( R.layout.dialog_place_finder, container, false );

        // init widgets
        rgMode = view.findViewById( R.id.rgMode );
        rgMode.setOnCheckedChangeListener( this );

        etPlace = view.findViewById( R.id.etPlace );

        view.findViewById( R.id.btnContinue ).setOnClickListener( this );
        view.findViewById( R.id.btnCancel ).setOnClickListener( this );

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch ( id ) {
            case R.id.rbCurrentPlace:
                getView().findViewById( R.id.llCurrentPlace ).setVisibility( View.VISIBLE );
                getView().findViewById( R.id.llFindPlace ).setVisibility( View.GONE );
                etPlace.setText("");
                break;
            case R.id.rbFindPlace:
                getView().findViewById( R.id.llCurrentPlace ).setVisibility( View.GONE );
                getView().findViewById( R.id.llFindPlace ).setVisibility( View.VISIBLE );
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if ( view.getId() == R.id.btnContinue ) {
            String cityName = etPlace.getText().toString();
            MainActivity context = (MainActivity) getActivity();

            if ( !cityName.isEmpty() )
                context.getCityData( cityName, null );
            else
                context.startDetermineLocationTask();
        }

        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss( dialog );
        Log.d( LOG_TAG, DIALOG_TAG + " onDismiss" );
    }
}
