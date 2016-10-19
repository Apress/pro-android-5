package com.androidbook.maps.whereami;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends FragmentActivity {
	
	private static final String MAPFRAGTAG = "MAPFRAGTAG";
	MyMapFragment myMapFrag = null;
	private Geocoder geocoder;

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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
        	Toast.makeText(this, "Geocoder is not available on this device", Toast.LENGTH_LONG).show();
        	finish();
        }
        geocoder = new Geocoder(this);
        EditText loc = (EditText)findViewById(R.id.locationName);
        loc.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String locationName = v.getText().toString();

                    try {
                        List<Address> addressList = 
                           geocoder.getFromLocationName(locationName, 5);
                        if(addressList!=null && addressList.size()>0)
                        {
//                       Log.v(TAG, "Address: " + addressList.get(0).toString());
                            myMapFrag.gotoLocation(new LatLng(
                        		addressList.get(0).getLatitude(),
                        		addressList.get(0).getLongitude()),
                                locationName);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
	}
}
