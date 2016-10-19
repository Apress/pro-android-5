package com.androidbook.maps.geofencing;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends FragmentActivity
    implements GoogleApiClient.ConnectionCallbacks,
               GoogleApiClient.OnConnectionFailedListener,
               ResultCallback<Status>,
               DialogInterface.OnCancelListener {

	private static final String TAG = "GeofencingApiDemo";
	private static final int PLAY_SERVICES_RECOVERY_REQUEST = 777;
	private static final int LOCATION_SETTINGS_REQUEST = 888;
	private static final int
                   CONNECTION_FAILURE_RESOLUTION_REQUEST = 999;
	private GoogleApiClient mClient = null;
	private enum FIX {NO_FAIL, PLAY_SERVICES,
		LOCATION_SETTINGS, CONNECTION};
	private FIX lastFix = FIX.NO_FAIL;
	private GeofencingApi mFencer = 
			LocationServices.GeofencingApi;
	private List<Geofence> mGeofences = new ArrayList<Geofence>();
	private PendingIntent pIntent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        final float radius = 0.5f * 1609.0f; // half mile times 1609 meters per mile

		Geofence.Builder gb = new Geofence.Builder();
		// Make a half mile geofence around your home
		Geofence home = gb.setCircularRegion(28.389859,-81.383777, radius)
				.setTransitionTypes(
					Geofence.GEOFENCE_TRANSITION_ENTER |
					Geofence.GEOFENCE_TRANSITION_EXIT |
					Geofence.GEOFENCE_TRANSITION_DWELL )
				.setExpirationDuration(
					12 * 60 * 60 * 1000)  // 12 hours
				.setLoiteringDelay(300000)   // 5 minutes
				.setRequestId("home")
				.setNotificationResponsiveness(5000) // 5 secs
				.build();
        mGeofences.add(home);

        // Make another geofence around your work
        Geofence work = gb.setCircularRegion(28.36631, -81.52120, radius)
				.setRequestId("work")
				.build();
        mGeofences.add(work);
        Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);

        pIntent = PendingIntent.getService(getApplicationContext(), 0, intent,
        		PendingIntent.FLAG_UPDATE_CURRENT);

		mClient = new GoogleApiClient.Builder(this, this, this)
                .addApi(LocationServices.API)
                .build();
		
        Log.v(TAG, "Activity, client are created");
	}

	private void tryToConnect() {
		if(mClient.isConnected()) return;
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
                mClient.connect();
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
        Log.v(TAG, "Setting up geofences (onConnected)...");
	    PendingResult<Status> pResult = mFencer.addGeofences(mClient, mGeofences, pIntent);
        pResult.setResultCallback(this);  // ResultCallback<Status> interface
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
	public void onDestroy() {
		super.onDestroy();
        Log.v(TAG, "Destroying activity, GoogleApiClient...");
		if(mClient.isConnected()) {
	        Log.v(TAG, "Removing geofences, disconnecting...");
            mFencer.removeGeofences(mClient, pIntent);
            mClient.disconnect();
		}
		mClient = null;
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
	public void onResult(Status status) {
		Log.v(TAG, "Got a result from addGeofences("
			+ status.getStatusCode() + "): "
	        + status.getStatus().getStatusMessage());
	}
}
