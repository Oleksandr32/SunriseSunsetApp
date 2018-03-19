package com.projects.sunrisesunset.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projects.sunrisesunset.R;
import com.projects.sunrisesunset.models.Sun;

/**
 * Created by Alex on 03.03.2018.
 * custom view
 */

public class SunInfoView extends LinearLayout {

    // default value for text view
    private static final String DEFAULT_VALUE = "Unknown";

    // widgets
    private TextView tvSunrise;
    private TextView tvDayLength;
    private TextView tvSunset;

    public SunInfoView(Context context, AttributeSet attrs) {
        super( context, attrs );

        setOrientation( LinearLayout.VERTICAL );
        inflate( context, R.layout.view_sun_info, this );

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
        tvSunrise = (TextView) findViewById( R.id.tvSunrise );
        tvDayLength = (TextView) findViewById( R.id.tvDayLength );
        tvSunset = (TextView) findViewById( R.id.tvSunset );

        // set text color
        tvSunrise.setTextColor( textColor );
        tvDayLength.setTextColor( textColor );
        tvSunset.setTextColor( textColor );

        // set text size
        tvSunrise.setTextSize( TypedValue.COMPLEX_UNIT_SP, textSize );
        tvDayLength.setTextSize( TypedValue.COMPLEX_UNIT_SP, textSize );
        tvSunset.setTextSize( TypedValue.COMPLEX_UNIT_SP, textSize );
    }

    public void setSunData(Sun sun) {
        if ( sun != null ) {
            tvSunrise.setText( sun.getSunrise() );
            tvDayLength.setText( sun.getDayLength() );
            tvSunset.setText( sun.getSunset() );
        } else {
            tvSunrise.setText( DEFAULT_VALUE );
            tvDayLength.setText( DEFAULT_VALUE );
            tvSunset.setText( DEFAULT_VALUE );
        }
    }
}
