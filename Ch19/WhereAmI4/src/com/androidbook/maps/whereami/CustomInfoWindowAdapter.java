package com.androidbook.maps.whereami;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;


public class CustomInfoWindowAdapter implements InfoWindowAdapter {
	private static final String TAG = "WhereAmI";
	Context mContext = null;
	String mText = null;
	
	public CustomInfoWindowAdapter(Context context, String text2display) {
	    	mContext = context;
	    	mText = text2display;
	}
	
	@Override
	public View getInfoContents(Marker arg0) {
		Log.v(TAG, "in getInfoContents. showing is "+arg0.isInfoWindowShown());
		TextView tv = new TextView(mContext);
		tv.setMinLines(2);
		tv.setText(mText);
		return tv;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		Log.v(TAG, "in getInfoWindow. showing is "+arg0.isInfoWindowShown());
		// Return null here since we're not providing a complete window
		return null;
	}

}
