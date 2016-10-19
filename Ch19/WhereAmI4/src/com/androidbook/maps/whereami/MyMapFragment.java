package com.androidbook.maps.whereami;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends SupportMapFragment
    implements GoogleApiClient.ConnectionCallbacks,
               GoogleApiClient.OnConnectionFailedListener,
               OnMarkerClickListener,
               OnMapReadyCallback {
	private Context mContext = null;
	private GoogleMap mMap = null;
	private GoogleApiClient mClient = null;
	private String mLocString = null;
	private Marker mLastClicked;
	private float mAccuracy = 0;
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
		mContext = getActivity();
		mClient = new GoogleApiClient.Builder(mContext, this, this)
            .addApi(LocationServices.API)
            .build();
        mClient.connect();
        setRetainInstance(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		doWhenEverythingIsReady();
	}
	
	private void doWhenEverythingIsReady() {
		if(mMap == null || mLocString == null)
			return;
		mMap.clear();
		// Setup the info window for the marker
		CustomInfoWindowAdapter ciwm = new CustomInfoWindowAdapter(mContext, mLocString);
		mMap.setInfoWindowAdapter(ciwm);
		
		// Add the marker to the map
		MarkerOptions markerOpt = new MarkerOptions()
			.draggable(false)
			.flat(true)
			.position(mLatLng)
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		mLastClicked = mMap.addMarker(markerOpt);
		mLastClicked.showInfoWindow();
		
		// Add the circle showing accuracy of the location result
		mMap.addCircle(new CircleOptions()
		    .center(mLatLng)
		    .radius(mAccuracy)
		    .fillColor(0x330000ff)
		    .strokeWidth(5));
		
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 16));
		
		mMap.setOnMarkerClickListener(this);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(mContext, "Connection failed", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		List<Address> here = null;
		Toast.makeText(mContext, "Got connected", Toast.LENGTH_LONG).show();
		FusedLocationProviderApi locator = LocationServices.FusedLocationApi;
		Location myLocation = locator.getLastLocation(mClient);
		// Toast.makeText(context, "My location is "+myLocation, Toast.LENGTH_LONG).show();
		double lat = myLocation.getLatitude();
		double lng = myLocation.getLongitude();
		mAccuracy = myLocation.getAccuracy();
		
		mLatLng = new LatLng(lat, lng);
		Geocoder geo = new Geocoder(mContext);
		try {
			here = geo.getFromLocation(lat, lng, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mLocString = "Your current location:"
			+ "\n" + here.get(0).getAddressLine(0)
			+ "\n" + here.get(0).getAddressLine(1);

		doWhenEverythingIsReady();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Toast.makeText(mContext, "Connection suspended", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// isInfoWindowShown() is broken so we need to keep track of what is
		// showing ourselves. See this issue report for details:
		// https://code.google.com/p/gmaps-api-issues/issues/detail?id=5408
        if (mLastClicked != null && mLastClicked.equals(marker)) {
            mLastClicked = null;
            marker.hideInfoWindow();
            return true;
        } else {
            mLastClicked = marker;
            return false;
        }
	}

	@Override
	public void onMapReady(GoogleMap arg0) {
		Toast.makeText(mContext, "Got a map", Toast.LENGTH_LONG).show();
		mMap = arg0;
		doWhenEverythingIsReady();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mClient.disconnect();
		if(mMap != null)
			mMap.clear();
	}
}
