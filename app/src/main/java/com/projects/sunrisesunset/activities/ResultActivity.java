package com.projects.sunrisesunset.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.projects.sunrisesunset.R;
import com.projects.sunrisesunset.models.City;
import com.projects.sunrisesunset.models.Sun;
import com.projects.sunrisesunset.network.SunRestClient;
import com.projects.sunrisesunset.views.SunInfoView;

public class ResultActivity extends AppCompatActivity {

    // intent extra parameters
    public static final String EXTRA_PARAM_DATE = "com.projects.sunrisesunset.activities.extra.DATE";
    public static final String EXTRA_PARAM_CITY = "com.projects.sunrisesunset.activities.extra.CITY";

    // widget
    private SunInfoView mSunInfoView;

    // async http client for determine info about sun
    private SunRestClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_result );

        // init client
        client = new SunRestClient( this );

        //init widget
        initWidget();
    }

    // init SunInfoView
    private void initWidget() {
        // init custom SunInfoView
        mSunInfoView = findViewById( R.id.mSunInfoView );

        // get data for determine info about sun
        Intent intent = getIntent();
        String date = intent.getStringExtra( EXTRA_PARAM_DATE );
        City city = intent.getParcelableExtra( EXTRA_PARAM_CITY );

        if ( city == null ) {
            setSunData( null );
            Toast.makeText(this, R.string.sun_info_not_found_message, Toast.LENGTH_LONG ).show();
        }
        else // performed asynchronously
            client.getSun(date, city.getLocation().getLatitude(), city.getLocation().getLongitude());
    }

    // set data about sun on SunInfoView
    public void setSunData(Sun sun) {
        mSunInfoView.setSunData( sun );
    }

}
