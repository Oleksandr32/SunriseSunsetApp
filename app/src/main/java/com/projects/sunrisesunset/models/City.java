package com.projects.sunrisesunset.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


/**
 * Created by Alex on 25.01.2018.
 */

public class City implements Parcelable {

    // object tag
    public static final String OBJECT_TAG = City.class.getSimpleName();
    // tag for log filter
    private static final String LOG_TAG = "myLogs";

    // provider
    private static final String PROVIDER_NAME = "gps";

    // data fields
    private String address;
    private Location location;

    public City(String address, Location location) {
        this.address = address;
        this.location = location;
    }

    public City(String address, double longitude, double latitude) {
        this.address = address;

        location = new Location( PROVIDER_NAME );
        location.setLatitude( latitude );
        location.setLongitude( longitude );
    }

    // getters and setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format( "Address: %s\nLatitude: %.6f\nLongitude: %.6f",
                address, location.getLatitude(), location.getLatitude() );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // pack in Parcel
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d( LOG_TAG, OBJECT_TAG + " writeToParcel()" );
        parcel.writeString( address );
        parcel.writeParcelable( location, PARCELABLE_WRITE_RETURN_VALUE );
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        // unpack object from Parcel
        public City createFromParcel(Parcel in) {
            Log.d( LOG_TAG, OBJECT_TAG + " createFromParcel()" );
            return new City( in );
        }

        public City[] newArray(int size) {
            return new City[size];
        }
    };

    // constructor, which read date from Parcel
    private City(Parcel parcel) {
        Log.d(LOG_TAG, "City(Parcel parcel)");
        address = parcel.readString();
        location = parcel.readParcelable( null );
    }
}
