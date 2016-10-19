package com.androidbook.maps.whereami;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends SupportMapFragment
    implements GoogleApiClient.ConnectionCallbacks,
               GoogleApiClient.OnConnectionFailedListener,
               OnMapReadyCallback {
	private Context mContext = null;
	private GoogleMap mMap = null;
	private GoogleApiClient mClient = null;
	private LatLng mLatLng = null;
	
	public static MyMapFragment newInstance() {
		MyMapFragment myMF = new MyMapFragment();
		return myMF;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getMapAsync(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(mClient == null) { // first time in, setup this fragment
            setRetainInstance(true);

            mContext = getActivity().getApplication();
		    mClient = new GoogleApiClient.Builder(mContext, this, this)
                .addApi(LocationServices.API)
                .build();
            mClient.connect();
		}
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(mContext, "Connection failed", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// Figure out where we are (lat, long) as best as we can
		// based on the user's selections for Location Settings
		FusedLocationProviderApi locator = LocationServices.FusedLocationApi;
		Location myLocation = locator.getLastLocation(mClient);
		// if the services are not available, could get a null location
		if(myLocation == null)
			return;
		double lat = myLocation.getLatitude();
		double lng = myLocation.getLongitude();
		mLatLng = new LatLng(lat, lng);
		doWhenEverythingIsReady();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Toast.makeText(mContext, "Connection suspended", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onMapReady(GoogleMap arg0) {
        mMap = arg0;
        doWhenEverythingIsReady();
	}

	private void doWhenEverythingIsReady() {
		if(mMap == null || mLatLng == null)
			return;
		// Add a marker
		MarkerOptions markerOpt = new MarkerOptions()
				.draggable(false)
				.flat(true)
				.position(mLatLng)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	    mMap.addMarker(markerOpt);
		
	    // Move the camera to zoom in on our location
	    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
	}
}
