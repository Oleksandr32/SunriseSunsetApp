package com.projects.sunrisesunset.network;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;

import com.projects.sunrisesunset.activities.MainActivity;
import com.projects.sunrisesunset.models.City;
import com.projects.sunrisesunset.utils.JSONParser;

import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Alex on 18.03.2018.
 * This is the object responsible for communicating with a REST API.
 * Used AsyncHttpClient
 */

public class CityRestClient {

    // client tag
    public static final String CLIENT_TAG = CityRestClient.class.getSimpleName();
    // tag for log filter
    public static final String LOG_TAG = "myLogs";

   /*   Google Geocode API
    *   The Geocode API accepts search filter parameters. If you know that you're looking for a
    *   city with a particular word or string in its name you can pass this information
    *   to the Geocode service for more accurate results.
    *   To search city, you can include the address parameter in your Geocode Search requests.
    */
    public static final String REST_URL_FOR_CITY_BY_NAME = "http://maps.googleapis.com/maps/api/geocode/json?address=%s&language=en";
    public static final String REST_URL_FOR_CITY_BY_LOCATION = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=true&language=en";

    // context (MainActivity)
    private Context context;
    // result object of GET request
    private City result;

    public CityRestClient(Context context) {
        this.context = context;
        result = null;
    }

    // perform async HTTP GET request, parse json response and return object of City.
    public void getCity(String name, Location loc) {
        String url;
        if ( name == null ) { // convert double to string and replace "," to "." for correct GET request
            url = String.format( REST_URL_FOR_CITY_BY_LOCATION, String.valueOf( loc.getLatitude() ).replace(',', '.'),
                    String.valueOf( loc.getLongitude() ).replace(',', '.') );
        } else
            url = String.format( REST_URL_FOR_CITY_BY_NAME, name );

        // that's async client
        BaseRestClient.get( url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e( LOG_TAG, CLIENT_TAG + ": getCity: onFailure: response = " + responseString, throwable );
                result = null;
                sendResult(); // send result to activity
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d( LOG_TAG, CLIENT_TAG + ": getCity: onSuccess: response = " + responseString );
                try {
                    result = JSONParser.parseJSONToCityObject( responseString );
                } catch (JSONException e) {
                    Log.e( LOG_TAG, CLIENT_TAG + ": getCity: onSuccess: parse json response", e );
                    result = null;
                }
                // send result to activity
                sendResult();
            }
        });
    }

    // send result to activity
    private void sendResult() { ((MainActivity) context).onLocationChange( result ); }

    // getter // get "previous" result of request
    public City getResult() { return result; }
}
