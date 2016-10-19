package com.androidbook.maps.geofencing;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
               ResultCallback<Status> {

	private static final String TAG = "GeofencingApiDemo";
	private GoogleApiClient mClient = null;
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
        mClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed, no resolutions available, "+
              GooglePlayServicesUtil.getErrorString(
       	      connectionResult.getErrorCode() ));
	}

	@Override
	public void onConnected(Bundle arg0) {
		// Setup geofences
        Log.v(TAG, "Setting up geofences (onConnected)...");
	    PendingResult<Status> pResult = mFencer.addGeofences(mClient, mGeofences, pIntent);
        pResult.setResultCallback(this);  // ResultCallback<Status> interface
	}

	@Override
	public void onConnectionSuspended(int arg0) {
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
	public void onResult(Status status) {
		Log.v(TAG, "Got a result from addGeofences("
			+ status.getStatusCode() + "): "
	        + status.getStatus().getStatusMessage());
	}
}
