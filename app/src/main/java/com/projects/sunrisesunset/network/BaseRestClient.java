package com.projects.sunrisesunset.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by Alex on 18.03.2018.
 */

public class BaseRestClient {

    // http client from api 'com.loopj.android:android-async-http:1.4.9'
    private static AsyncHttpClient client;

    // init client
    static { client = new AsyncHttpClient(); }

    // perform async HTTP GET request
    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get( url, responseHandler );
    }
}
