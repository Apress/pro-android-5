package com.androidbook.maps.whereami;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends SupportMapFragment
    implements OnMapReadyCallback {
	private GoogleMap mMap = null;
	
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
        setRetainInstance(true); 
	}
	
	public void gotoLocation(LatLng latlng, String locString) {
		if(mMap == null)
			return;
		// Add a marker for the given location
		MarkerOptions markerOpt = new MarkerOptions()
				.draggable(false)
				.flat(false)
				.position(latlng)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.title("You chose:")
				.snippet(locString);
		// See the onMarkerClicked callback for why we do this
		mMap.addMarker(markerOpt);

		// Move the camera to zoom in on our location
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
	}

	@Override
	public void onMapReady(GoogleMap arg0) {
		mMap = arg0;
	}
}
