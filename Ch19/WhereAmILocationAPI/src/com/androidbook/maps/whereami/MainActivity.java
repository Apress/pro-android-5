package com.androidbook.maps.whereami;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	private static final String MAPFRAGTAG = "MAPFRAGTAG";
	MyMapFragment myMapFrag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        if ((myMapFrag = (MyMapFragment) getSupportFragmentManager()
                .findFragmentByTag(MAPFRAGTAG)) == null) {
            myMapFrag = MyMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, myMapFrag, MAPFRAGTAG).commit();
        }

	    if(!isLocationEnabled(this)) {
    	    // no location service providers are enabled
    	    Toast.makeText(this, "Location Services are off. " +
    	        "Can't work without them. Please turn them on.",
    			Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(
    		    android.provider.Settings
    		        .ACTION_LOCATION_SOURCE_SETTINGS), 0);
        }
	}

	@SuppressWarnings("deprecation")
    public boolean isLocationEnabled(Context context) {
	    int locationMode = Settings.Secure.LOCATION_MODE_OFF;
	    String locationProviders;

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
	        try {
	            locationMode = Settings.Secure.getInt(
	            	context.getContentResolver(),
	            	Settings.Secure.LOCATION_MODE);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
	    }else{
	        locationProviders = Settings.Secure.getString(
	        	context.getContentResolver(), 
	        	Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	        return !TextUtils.isEmpty(locationProviders);
	    }
	}
}
