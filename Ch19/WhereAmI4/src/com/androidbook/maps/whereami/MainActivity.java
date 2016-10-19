package com.androidbook.maps.whereami;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
	}
}
