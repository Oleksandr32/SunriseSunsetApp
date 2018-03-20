package com.projects.sunrisesunset.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.projects.sunrisesunset.R;
import com.projects.sunrisesunset.activities.MainActivity;
import com.projects.sunrisesunset.fragments.NoGpsConnectionDialogFragment;

/**
 * Created by Alex on 20.03.2018.
 * Singleton
 * Util for determine current location
 */

public class LocationHelper implements LocationListener {

    // util tag
    public static final String UTIL_TAG = LocationHelper.class.getSimpleName();
    // tag for log filter
    public static final String LOG_TAG = "myLogs";

    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    //The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;

    // instance
    private static LocationHelper instance = null;
    // data fields
    private Location location;


    // Singleton implementation
    public static LocationHelper getInstance()     {
        if ( instance == null ) {
            instance = new LocationHelper();
        }

        return instance;
    }

    // Local constructor
    private LocationHelper()     {
        Log.d( LOG_TAG, UTIL_TAG + " LocationHelper created" );
    }


    // Sets up location after permissions is granted
    @TargetApi(23)
    public void determineLocation(Context context) {
        // check permission
        if ( Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText( context, R.string.permission_not_granted_message, Toast.LENGTH_LONG ).show();
            return;
        }

        try   {
            LocationManager locationManager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

            // Get GPS and network status
            boolean isGPSEnabled = locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
            boolean isNetworkEnabled = locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );

            // cannot get location
            if ( !isNetworkEnabled && !isGPSEnabled )    {
                location = null;
                // show dialog fragment for Location Settings
                new NoGpsConnectionDialogFragment().show( ((MainActivity) context).getSupportFragmentManager(),
                        NoGpsConnectionDialogFragment.DIALOG_TAG );
            } else {
                // Network Provider
                if ( isNetworkEnabled ) {
                    locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this );
                    if ( locationManager != null )   {
                        location = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
                    }
                }
                // GPS Provider
                if ( isGPSEnabled )  {
                    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this );

                    if ( locationManager != null )  {
                        location = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
                    }
                }
            } // end else
        } catch (Exception e)  {
            Log.e( LOG_TAG, UTIL_TAG + ": initLocationHelper() : Error determining location : ", e );
            location = null;
        }
    }

    // getter
    public Location getLocation() {
        return location;
    }

    // for LocationListener
    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }
}
