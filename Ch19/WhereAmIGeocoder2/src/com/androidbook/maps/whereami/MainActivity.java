package com.androidbook.maps.whereami;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity {
	
	private static final String MAPFRAGTAG = "MAPFRAGTAG";
	MyMapFragment myMapFrag = null;
	private Geocoder geocoder;
	List<Address> addressList = null;
    private ProgressDialog progDialog;

	@SuppressLint("NewApi")
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
		try {
		  if(!Geocoder.isPresent()) {
			// Should do something the user can see rather than this
			Log.e("WhereAmIGeocoder2", "Geocoder functionality is not present");
			return;
		  }
		} // If this is run pre-Gingerbread, ignore the exception because
		  // isPresent() won't be found.
		catch(Exception e) {}
        geocoder = new Geocoder(this);
        EditText loc = (EditText)findViewById(R.id.locationName);
        loc.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String locationName = v.getText().toString();

                    progDialog = ProgressDialog.show(MainActivity.this, 
                    		"Processing...", "Finding Location...", true, false);

                    findLocation(locationName);
                }
                return false;
            }
        });
	}

private void findLocation(final String locationName)
{
    Thread thrd = new Thread()
    {
        public void run()
        {
            try {
                // do background work
                addressList = geocoder.getFromLocationName(locationName, 5);
                //send message to handler to process results
                Message msg = new Message();
                msg.obj = locationName;
                uiCallback.sendMessage(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    thrd.start();
}

// ui thread callback handler
private Handler uiCallback = new Handler()
{
    @Override
    public void handleMessage(Message msg)
    {
    	// tear down dialog
        progDialog.dismiss();

        if(addressList!=null && addressList.size()>0)
        {
            myMapFrag.gotoLocation(new LatLng(
        		addressList.get(0).getLatitude(),
        		addressList.get(0).getLongitude()),
                (String)msg.obj);
        }
        else
        {
            Dialog foundNothingDlg = new 
               AlertDialog.Builder(MainActivity.this)
              .setIcon(0)
              .setTitle("Failed to Find Location")
              .setPositiveButton("Ok", null)
              .setMessage("Location Not Found...")
            .create();
            foundNothingDlg.show();
        }
    }
};
}
