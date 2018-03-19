package com.projects.sunrisesunset.network;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;

import com.projects.sunrisesunset.activities.ResultActivity;
import com.projects.sunrisesunset.models.Sun;
import com.projects.sunrisesunset.utils.JSONParser;

import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Alex on 18.03.2018.
 * This is the object responsible for communicating with a REST API.
 * Used AsyncHttpClient
 */

public class SunRestClient {

    // client tag
    public static final String CLIENT_TAG = SunRestClient.class.getSimpleName();
    // tag for log filter
    public static final String LOG_TAG = "myLogs";

   /*   Sunset and sunrise times API
    *   We offer a free API that provides sunset and sunrise times for a given latitude and longitude.
    *   Please note that attribution is required if you use our API. Check "Usage limits and attribution"
    *   section below for more information.
    *   API documentation
    *   Ours is a very simple REST api, you only have to do a GET request to https://api.sunrise-sunset.org/json.
    *   Parameters
    *   lat (float): Latitude in decimal degrees. Required.
    *   lng (float): Longitude in decimal degrees. Required.
    *   date (string): Date in YYYY-MM-DD format. If not present, date defaults to current date. Optional.
    */
    public static final String GET_REQUEST_FOR_SUN_INFO = "https://api.sunrise-sunset.org/json?lat=%f&lng=%f&date=%s";

    // context (ResultActivity)
    private Context context;
    // result object of GET request
    private Sun result;

    public SunRestClient(Context context) {
        this.context = context;
        result = null;
    }

    // perform async HTTP GET request, parse json response and return object of Sun.
    public Sun getSun(String date, double lat, double lng) {
        String url = String.format( GET_REQUEST_FOR_SUN_INFO, lat, lng, date );

        // that's async client
        BaseRestClient.get( url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e( LOG_TAG, CLIENT_TAG + ": getSun: onFailure: response = " + responseString, throwable );
                result = null;
                sendResult(); // send result to activity
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d( LOG_TAG, CLIENT_TAG + ": getSun: onSuccess: response = " + responseString );
                try {
                    result = JSONParser.parseJSONToSunObject( responseString );
                } catch (JSONException e) {
                    Log.e( LOG_TAG, CLIENT_TAG + ": getSun: onSuccess: parse json response", e );
                    result = null;
                }
                // send result to activity
                sendResult();
            }
        });

        return result;
    }

    // send result to activity
    private void sendResult() { ((ResultActivity) context).setSunData( result ); }
}
