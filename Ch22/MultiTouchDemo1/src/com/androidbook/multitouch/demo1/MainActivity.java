package com.androidbook.multitouch.demo1;

// This file is MainActivity.java
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnTouchListener {
    private int os;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.layout1);
        layout1.setOnTouchListener(this);

        try { os = Build.VERSION.SDK_INT; }
        catch(Exception e) {
        	Log.v("Demo", "Exception is: " + e);
        	os = 3;
        }
    }

	public boolean onTouch(View v, MotionEvent event) {
		String myTag = v.getTag().toString();
		Log.v(myTag, "-----------------------------");
		Log.v(myTag, "Got view " + myTag + " in onTouch");
		Log.v(myTag, describeEvent(event));
		logAction(event);
		if( "true".equals(myTag.substring(0, 4))) {
			return true;
		}
		else {
			return false;
		}
	}

	protected String describeEvent(MotionEvent event) {
		StringBuilder result = new StringBuilder(500);
		result.append("Action: ").append(event.getAction()).append("\n");
		int numPointers = event.getPointerCount();
		result.append("Number of pointers: ").append(numPointers).append("\n");
		if( os >= 9 ) // Introduced in Android 2.3
    		result.append("Event source: ").append(event.getSource()).append("\n");
        int ptrIdx = 0;
		while (ptrIdx < numPointers) {
		    int ptrId = event.getPointerId(ptrIdx);
            result.append("Pointer Index: ").append(ptrIdx);
            result.append(", Pointer Id: ").append(ptrId).append("\n");
            result.append("   Location: ").append(event.getX(ptrIdx));
            result.append(" x ").append(event.getY(ptrIdx)).append("\n");
            result.append("   Pressure: ").append(event.getPressure(ptrIdx));
            result.append("   Size: ").append(event.getSize(ptrIdx)).append("\n");

            ptrIdx++;
        }
        result.append("Downtime: ").append(event.getDownTime()).append("ms\n");
        result.append("Event time: ").append(event.getEventTime()).append("ms");
        result.append("  Elapsed: ");
        result.append(event.getEventTime()-event.getDownTime());
        result.append(" ms\n");
        return result.toString();
    }

	private void logAction(MotionEvent event) {
		int action = event.getActionMasked();
    	int ptrIndex = event.getActionIndex();
	    int ptrId = event.getPointerId(ptrIndex);

	    if(action == 5 || action == 6)
            action = action - 5;

		Log.v("Action", "Pointer index: " + ptrIndex);
		Log.v("Action", "Pointer Id: " + ptrId);
		Log.v("Action", "True action value: " + action);
	}
}