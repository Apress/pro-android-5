package com.androidbook.maps.whereami;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;

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

	@Override
	public void onResume() {
		super.onResume();
		doWhenMapIsReady();
	}

	@Override
	public void onPause() {
		super.onPause();
		if(mMap != null)
            mMap.setMyLocationEnabled(false);
	}

	@Override
	public void onMapReady(GoogleMap arg0) {
		mMap = arg0;
		doWhenMapIsReady();
	}
	
	/* We have a race condition where the fragment could resume
     * before or after the map is ready. So we put all our logic
	 * for initializing the map into a common method that is
	 * called when the fragment is resumed or resuming and the
	 * map is ready.
	 */
	void doWhenMapIsReady() {
		if(mMap != null && isResumed()) {
		    mMap.setMyLocationEnabled(true);
		//    UiSettings uis = mMap.getUiSettings();
		//    uis.setZoomControlsEnabled(true);
		}
	}
}
