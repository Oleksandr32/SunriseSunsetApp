package com.projects.sunrisesunset.utils;

import com.projects.sunrisesunset.models.City;
import com.projects.sunrisesunset.models.Sun;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Alex on 23.01.2018.
 */

public class JSONParser {

   /*
    * for Sunset and sunrise times API(the result data is formatted using JSON)
    * parse JSON string to Sun object
    */
    public static Sun parseJSONToSunObject(String jsonStr) throws JSONException {
        JSONObject reader = new JSONObject( jsonStr );

        // get JSON Object called "results"
        JSONObject objectResults = reader.getJSONObject( "results" );

        // get values with JSON object
        String sunrise = objectResults.getString( "sunrise" );
        String sunset = objectResults.getString( "sunset" );
        String dayLength = objectResults.getString( "day_length" );

        return new Sun( sunrise, sunset, dayLength );
    }

    /*
    * for Google Geocode API (the result data is formatted using JSON)
    * parse JSON string to City object
    */
    public static City parseJSONToCityObject(String jsonStr) throws JSONException {
        JSONObject reader = new JSONObject(jsonStr);

        // get JSON Array called "results"
        JSONArray arrayResults = reader.getJSONArray("results");
        if (arrayResults.length() > 0) {
            // get first JSON Object with  array
            JSONObject objectResults = arrayResults.getJSONObject(0);

            // get value with JSON array of address components
            String address = objectResults.getString( "formatted_address" );

            // get JSON Object called "geometry" with objectResults
            JSONObject objectGeometry = objectResults.getJSONObject("geometry");
            // get JSON Object called "location" with objectGeometry
            JSONObject objectLocation = objectGeometry.getJSONObject("location");
            // get values with JSON object of location
            double latitude = objectLocation.getDouble("lat");
            double longitude = objectLocation.getDouble("lng");

            return new City( address, longitude, latitude);
        } else {
            return  null;
        }
    }
}
