package com.androidbook.maps.whereami;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends FragmentActivity
    implements GoogleApiClient.ConnectionCallbacks,
               GoogleApiClient.OnConnectionFailedListener,
               LocationListener,
               DialogInterface.OnCancelListener {

	private static final String TAG = "FusedLocationApiUpdates";
	private static final int PLAY_SERVICES_RECOVERY_REQUEST = 777;
	private static final int LOCATION_SETTINGS_REQUEST = 888;
	private static final int
                   CONNECTION_FAILURE_RESOLUTION_REQUEST = 999;
	private LocationRequest locReq = null;
	private GoogleApiClient client = null;
	private enum FIX {NO_FAIL, PLAY_SERVICES,
		LOCATION_SETTINGS, CONNECTION};
	private FIX lastFix = FIX.NO_FAIL;
	private FusedLocationProviderApi locator = 
			LocationServices.FusedLocationApi;
	private TextView modeStr = null;
	private TextView priorityStr = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		locReq = LocationRequest.create()
				.setPriority(
					LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
				.setInterval(10000)
				.setFastestInterval(5000);

		client = new GoogleApiClient.Builder(this, this, this)
                .addApi(LocationServices.API)
                .build();
		
		modeStr = (TextView)findViewById(R.id.mode);
		priorityStr = (TextView)findViewById(R.id.priority);

        Log.v(TAG, "Activity, client are created");
	}

	private void tryToConnect() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(this);
        // If Google Play services is available, then we're good
        if (resultCode == ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google Play services is available.");
    	    if(!isLocationEnabled(this)) {
            	if(lastFix == FIX.LOCATION_SETTINGS) {
            		// Since we're coming through again, it means
            		// recovery didn't happen. Time to bail out.
            		Log.e(TAG, "Location settings didn't work");
            		finish();
            	}
            	else {
        	        // no location service providers are enabled
        	        Toast.makeText(this, "Location Services are off. " +
        	            "Can't work without them. Please turn them on.",
        			    Toast.LENGTH_LONG).show();
        	        Log.i(TAG, "Location Services need to be on. " +
        			    "Launching the Settings screen");
        	        startActivityForResult(new Intent(
        		        android.provider.Settings
        		            .ACTION_LOCATION_SOURCE_SETTINGS),
        		        LOCATION_SETTINGS_REQUEST);
                	lastFix = FIX.LOCATION_SETTINGS;
            	}
            }
    	    else {
                client.connect();
                Log.v(TAG, "Connecting to GoogleApiClient...");
    	    }
        }
        // Google Play services was not available for some reason
        // See if the user can do something about it
        else if(GooglePlayServicesUtil
        		    .isUserRecoverableError(resultCode)) {
        	if(lastFix == FIX.PLAY_SERVICES) {
        		// Since we're coming through again, it means
        		// recovery didn't happen. Time to bail out.
        		Log.e(TAG, "Recovery doesn't seem to work");
        		finish();
        	}
        	else {
                Log.d(TAG, "Google Play services may be available. " +
        		    "Asking user for help");
                // This form of the dialog call will result in either a
                // callback to onActivityResult, or a dialog onCancel.
        	    GooglePlayServicesUtil.showErrorDialogFragment(resultCode,
        			this, PLAY_SERVICES_RECOVERY_REQUEST, this);
        	    lastFix = FIX.PLAY_SERVICES;
        	}
        } else {
        	// No hope left.
            Log.e(TAG, "Google Play Services is/are not available." +
        	      " No point in continuing");
            finish();
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
	            /* Note: locationMode will be one of:
	             * LOCATION_MODE_OFF (0)
	             * LOCATION_MODE_SENSORS_ONLY (1)
	             * LOCATION_MODE_BATTERY_SAVING  (2)
	             * LOCATION_MODE_HIGH_ACCURACY (3)
	             */
	            String MODES[] = {"OFF", "SENSORS_ONLY", 
	            		 "BATTERY_SAVING", "HIGH_ACCURACY"};
	            Log.v(TAG, "locationMode is " + MODES[locationMode]);
	            modeStr.setText("Current mode is " + MODES[locationMode]);
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

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        /* Decide what to do based on the original request code.
         * Note that our activity got paused to launch the other
         * activity, so after this callback runs, our activity's
         * onResume() will run.
         */
        switch (requestCode) {
        case PLAY_SERVICES_RECOVERY_REQUEST :
        	Log.v(TAG, "Got a result for Play Services Recovery");
            break;
        case LOCATION_SETTINGS_REQUEST :
        	Log.v(TAG, "Got a result for Location Settings");
        	break;
        case CONNECTION_FAILURE_RESOLUTION_REQUEST :
        	Log.v(TAG, "Got a result for connection failure");
            break;
        }
        Log.v(TAG, "resultCode was " + resultCode);
   	    Log.v(TAG, "End of onActivityResult");
    }

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * the error.
         */
        if (connectionResult.hasResolution()) {
            Log.i(TAG, "Connection failed, trying to resolve it...");
        	if(lastFix == FIX.CONNECTION) {
        		// Since we're coming through again, it means
        		// recovery didn't happen. Time to bail out.
        		Log.e(TAG, "Connection retry didn't work");
        		finish();
        	}
            try {
                // Start an Activity that tries to resolve the error
            	lastFix = FIX.CONNECTION;
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                Log.e(TAG, "Could not resolve connection failure");
                e.printStackTrace();
                finish();
            }
        } else {
            /*
             * If no resolution is available, display error to the
             * user.
             */
            Log.e(TAG, "Connection failed, no resolutions available, "+
                    GooglePlayServicesUtil.getErrorString(
                    		connectionResult.getErrorCode() ));
    		Toast.makeText(this, "Connection failed. Cannot continue",
    				Toast.LENGTH_LONG).show();
    		finish();
        }
	}

	@Override
	public void onConnected(Bundle arg0) {
		// Setup location updates
        Log.v(TAG, "Connected!");
        lastFix = FIX.NO_FAIL;
	    locator.requestLocationUpdates(client, locReq, this);
        Log.v(TAG, "Requesting location updates (onConnected)...");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Toast.makeText(this, "Connection suspended", Toast.LENGTH_LONG).show();
        Log.v(TAG, "Connection suspended");
	}
	
	@Override
	public void onResume() {
		super.onResume();
        Log.v(TAG, "Resuming activity");
		tryToConnect();
	}

	@Override
	public void onPause() {
		super.onPause();
        Log.v(TAG, "Pausing activity, removing location updates, disconnecting...");
		if(client.isConnected()) {
            locator.removeLocationUpdates(client, this);
            client.disconnect();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
        Log.v(TAG, "Destroying activity, GoogleApiClient...");
		client = null;
	}

	@Override
	public void onLocationChanged(Location location) {
        Log.v(TAG, "Got a location change, showing Toast msg");
		Toast.makeText(this, "New location latitude ["
			+ location.getLatitude() + "] longitude ["
			+ location.getLongitude() + "] accuracy ["
			+ location.getAccuracy() + " meters]", 
			Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		/* If we're here, then user cancelled the dialog
		 * instead of taking action to correct the Google
		 * Play Services problem.
		 */
		Log.d(TAG, "User does not want to fix the problem. Bailing out");
		finish();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu){ 
    	super.onCreateOptionsMenu(menu);
 	   	MenuInflater inflater = getMenuInflater();
 	   	inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
    	case R.id.menu_no_power :
    		locReq.setPriority(LocationRequest.PRIORITY_NO_POWER);
    		priorityStr.setText("Current priority is NO_POWER");
    		break;
    	case R.id.menu_low_power :
    		locReq.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    		priorityStr.setText("Current priority is LOW_POWER");
    		break;
    	case R.id.menu_balanced :
    		locReq.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    		priorityStr.setText("Current priority is BALANCED");
    		break;
    	case R.id.menu_high :
    		locReq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    		priorityStr.setText("Current priority is HIGH_ACCURACY");
    		break;
    	}
    	if(client.isConnected())
            locator.requestLocationUpdates(client, locReq, this);
       	return true;
    }
}
