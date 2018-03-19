package com.projects.sunrisesunset.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.location.Location;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projects.sunrisesunset.R;
import com.projects.sunrisesunset.models.City;

/**
 * Created by Alex on 02.03.2018.
 * custom view
 */

public class LocationInfoView extends LinearLayout {

    // default value for location and address
    private static final String DEFAULT_VALUE = "Unknown";

    // widgets
    private TextView tvDate;
    private TextView tvAddress;
    private TextView tvLocation;

    public LocationInfoView(Context context, AttributeSet attrs) {
        super( context, attrs );

        setOrientation( LinearLayout.VERTICAL );
        inflate( context, R.layout.view_location_info, this );

        int textColor;
        float textSize;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.InfoView,
                0, 0);

        try {
            textColor = a.getColor( R.styleable.InfoView_textColor,
                    getResources().getColor( R.color.icons ) );
            textSize = a.getDimension(R.styleable.InfoView_textSize,
                    getResources().getDimension( R.dimen.custom_view_text_size ) );
        } finally {
            a.recycle();
        }

        initWidgets( textColor, textSize );
    }

    private void initWidgets(int textColor, float textSize) {
        tvDate = (TextView) findViewById( R.id.tvDate );
        tvAddress = (TextView) findViewById( R.id.tvAddress );
        tvLocation = (TextView) findViewById( R.id.tvLocation );

        // set text color
        tvDate.setTextColor( textColor );
        tvAddress.setTextColor( textColor );
        tvLocation.setTextColor( textColor );

        // set text size
        tvDate.setTextSize( TypedValue.COMPLEX_UNIT_SP, textSize );
        tvAddress.setTextSize( TypedValue.COMPLEX_UNIT_SP, textSize );
        tvLocation.setTextSize( TypedValue.COMPLEX_UNIT_SP, textSize );
    }

    public void setDate(String date) {
        tvDate.setText( date );
    }

    public String getDate() {
        return tvDate.getText().toString();
    }

    public void setCityInfo(City city) {
        if ( city != null ) {
            tvAddress.setText( city.getAddress() );
            setLocation( city.getLocation() );
        }
        else
            tvAddress.setText( DEFAULT_VALUE );
    }

    public void setLocation(Location location) {
        if ( location != null ) {
            tvLocation.setText(String.format( "lat = %.6f\nlng = %.6f",
                    location.getLatitude(), location.getLongitude()));
        } else
            tvLocation.setText( DEFAULT_VALUE );
    }
}
