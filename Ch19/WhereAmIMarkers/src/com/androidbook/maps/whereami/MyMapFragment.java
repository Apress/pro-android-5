package com.androidbook.maps.whereami;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends SupportMapFragment
    implements OnMapReadyCallback {
	
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

	@Override
	public void onMapReady(GoogleMap myMap) {
		LatLng disneyMagicKingdom = new LatLng(28.418971, -81.581436);
		LatLng disneySevenLagoon = new LatLng(28.410067, -81.583699);

		// Add a marker
		MarkerOptions markerOpt = new MarkerOptions()
				.draggable(false)
				.flat(false)
				.position(disneyMagicKingdom)
				.title("Magic Kingdom")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	    myMap.addMarker(markerOpt);
	    
	    markerOpt.position(disneySevenLagoon)
	             .title("Seven Seas Lagoon");
	    myMap.addMarker(markerOpt);
		
        // Derive a bounding box around the markers
		LatLngBounds latLngBox = LatLngBounds.builder()
				.include(disneyMagicKingdom)
				.include(disneySevenLagoon)
				.build();
		
	    // Move the camera to zoom in on our locations
	    myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBox, 200, 200, 0));
	}
}
