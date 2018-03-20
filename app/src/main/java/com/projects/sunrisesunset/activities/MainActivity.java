package com.projects.sunrisesunset.activities;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.projects.sunrisesunset.R;
import com.projects.sunrisesunset.fragments.AboutDialogFragment;
import com.projects.sunrisesunset.fragments.InfoDialogFragment;
import com.projects.sunrisesunset.fragments.DatePickerDialogFragment;
import com.projects.sunrisesunset.fragments.PlaceFinderDialogFragment;
import com.projects.sunrisesunset.models.City;
import com.projects.sunrisesunset.network.CityRestClient;
import com.projects.sunrisesunset.utils.LocationHelper;
import com.projects.sunrisesunset.views.LocationInfoView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // activity tag
    public static final String ACTIVITY_TAG = MainActivity.class.getSimpleName();
    // tag for log filter
    public static final String LOG_TAG = "myLogs";

    // dialog fragments
    private InfoDialogFragment mInfoDialogFragment;
    private AboutDialogFragment mAboutDialogFragment;
    private DatePickerDialogFragment mDatePickerDialogFragment;
    private PlaceFinderDialogFragment mPlaceFinderDialogFragment;

    // custom view
    private LocationInfoView mLocationInfoView;

    // async http client for determine address of current location or desired city
    private CityRestClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // init async client
        client = new CityRestClient( this );

        // init widgets
        initWidgets();

        /*
         *  determine current location using util LocationHelper
         *  THIS IS DEFAULT MODE OF APP
         */
        determineCurrentLocation();

        // init dialog fragments
        mInfoDialogFragment = new InfoDialogFragment();
        mAboutDialogFragment = new AboutDialogFragment();
        mDatePickerDialogFragment = new DatePickerDialogFragment();
        mPlaceFinderDialogFragment = new PlaceFinderDialogFragment();
    }

    // init widgets
    private void initWidgets() {
        // init toolbar
        setSupportActionBar( (Toolbar) findViewById (R.id.mToolbar ) );
        // init custom LocationInfoView
        mLocationInfoView = findViewById( R.id.mLocationInfoView );
        // set current date on LocationInfoView
        onDateSelected( new SimpleDateFormat("yyyy-MM-dd" ).format( new Date() ) );
        // set listener
        ( (Button) findViewById(R.id.btnFollow) ).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // start activity, which contains information about sun
        startActivity( new Intent(this, ResultActivity.class)
                .putExtra( ResultActivity.EXTRA_PARAM_DATE, mLocationInfoView.getDate() )
                .putExtra( ResultActivity.EXTRA_PARAM_CITY, client.getResult() ) );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the toolbar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here. the toolbar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        switch ( id ) {
            case R.id.action_location:
                mPlaceFinderDialogFragment.show( getSupportFragmentManager(), PlaceFinderDialogFragment.DIALOG_TAG );
                break;
            case R.id.action_date:
                mDatePickerDialogFragment.show( getSupportFragmentManager(), DatePickerDialogFragment.DIALOG_TAG );
                break;
            case R.id.action_info:
                mInfoDialogFragment.show( getSupportFragmentManager(), InfoDialogFragment.DIALOG_TAG );
                break;
            case R.id.action_about:
                mAboutDialogFragment.show( getSupportFragmentManager(), AboutDialogFragment.DIALOG_TAG );
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // update date on LocationInfoView
    public void onDateSelected(String date) {
        mLocationInfoView.setDate( date );
    }

    // update current location or location of desired city on LocationInfoView
    public void onLocationChange(City city) {
        if ( city == null ) // show "city not found" message
            Toast.makeText(this, R.string.city_not_found_message, Toast.LENGTH_LONG).show();
        // show "unknown" text
        mLocationInfoView.setCityInfo( city );
    }

    // determine object of City for current location or desired(entered) city by using async http client
    public void getCityData(String name, Location location) {
        // performed asynchronously
        client.getCity( name, location );
    }

    //  determine current location using LocationHelper instance
    public void determineCurrentLocation() {
        LocationHelper locationHelper = LocationHelper.getInstance();
        locationHelper.determineLocation( this );
        Location location = locationHelper.getLocation();
        // debug location
        Log.d( LOG_TAG, ACTIVITY_TAG + ": location = " + location );
        // show current coordinates
        mLocationInfoView.setLocation( location );
        // determine object of City for current location
        if ( location != null )
            getCityData( null, location );
        else {
            onLocationChange( null ); // show "unknown" text
        }
    }
}
