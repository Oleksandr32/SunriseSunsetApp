package com.projects.sunrisesunset.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Alex on 25.01.2018.
 */

public class Sun  implements Parcelable {

    // object tag
    public static final String OBJECT_TAG = City.class.getSimpleName();
    // tag for log filter
    private static final String LOG_TAG = "myLogs";

    // data fields
    private String sunrise;
    private String sunset;
    private String dayLength;

    public Sun(String sunrise, String sunset, String dayLength) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.dayLength = dayLength;
    }

    // getters and setters
    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getDayLength() {
        return dayLength;
    }

    public void setDayLength(String dayLength) {
        this.dayLength = dayLength;
    }

    @Override
    public String toString() {
        return "Sunrise: " + sunrise +
                "\n\nDay length:  " + dayLength +
                "\n\nSunset:  " + sunset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // pack in Parcel
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d( LOG_TAG, OBJECT_TAG + " writeToParcel()" );
        parcel.writeString( sunrise );
        parcel.writeString( sunset );
        parcel.writeString( dayLength );
    }

    public static final Parcelable.Creator<Sun> CREATOR = new Parcelable.Creator<Sun>() {
        // unpack object from Parcel
        public Sun createFromParcel(Parcel in) {
            Log.d( LOG_TAG, OBJECT_TAG + " createFromParcel()" );
            return new Sun( in );
        }

        public Sun[] newArray(int size) {
            return new Sun[size];
        }
    };

    // constructor, which read date from Parcel
    private Sun(Parcel parcel) {
        Log.d(LOG_TAG, "City(Parcel parcel)");
        sunrise = parcel.readString();
        sunset = parcel.readString();
        dayLength = parcel.readString();
    }
}
