package com.projects.sunrisesunset.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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
import com.projects.sunrisesunset.fragments.NoGpsConnectionDialogFragment;
import com.projects.sunrisesunset.fragments.DatePickerDialogFragment;
import com.projects.sunrisesunset.fragments.PlaceFinderDialogFragment;
import com.projects.sunrisesunset.models.City;
import com.projects.sunrisesunset.network.CityRestClient;
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

        // start async task for determine current location
        startDetermineLocationTask();

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

        ( (Button) findViewById(R.id.btnFollow) ).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // start activity, which information about sun
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
        if (city == null) // show "city not found" message
            Toast.makeText(this, R.string.city_not_found_message, Toast.LENGTH_LONG).show();
        // show "unknown" text
        mLocationInfoView.setCityInfo( city );
    }

    // determine object of City for current location or desired(entered) city by using async http client
    public void getCityData(String name, Location location) {
        // performed asynchronously
        client.getCity( name, location );
    }

    // start async task for determine current location
    public void startDetermineLocationTask() {
        DetermineLocationTask mLocationTask = new DetermineLocationTask();

        if ( !mLocationTask.isProviderEnabled )
            new NoGpsConnectionDialogFragment().show(getSupportFragmentManager(), NoGpsConnectionDialogFragment.DIALOG_TAG);
        else // performed asynchronously
            mLocationTask.execute();
    }

    // async task for determine last known location
    private class DetermineLocationTask extends AsyncTask<Void, Void, Location> {
        // flag for GPS status
        boolean isProviderEnabled;

        // location manager
        LocationManager locationManager;

        public DetermineLocationTask() {
            locationManager = (LocationManager) MainActivity.this.getSystemService( Context.LOCATION_SERVICE );
            isProviderEnabled = locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
            locationManager.requestLocationUpdates("gps", 5000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {}
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {}
                @Override
                public void onProviderEnabled(String s) {}
                @Override
                public void onProviderDisabled(String s) {}
            });
        }

        //to get the user's current location // performed asynchronously
        @Override
        protected Location doInBackground(Void... voids) {
            // permission check
            if ( ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION )
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO
                return null;
            }
            // get last known location
            Location locationNet = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
            Location locationGPS = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
            Location locationProvider = locationManager.getLastKnownLocation( LocationManager.PASSIVE_PROVIDER );

            if ( locationNet != null ) return locationNet;
            else if ( locationGPS != null ) return locationGPS;
            else if ( locationProvider != null ) return locationProvider;
            else  return null;
        }

        @Override
        protected void onPostExecute(Location location) {
            super.onPostExecute( location );
            Log.d( LOG_TAG, ACTIVITY_TAG +  ": End DetermineLocationTask. Result: " + location );
            // show location
            mLocationInfoView.setLocation( location );
            // determine address of current location by using async http client
            getCityData( null, location );
        }
    } // end DetermineLocationTask class
}
