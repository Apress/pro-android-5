package com.androidbook.fragments.dialogs;

// This file is MainActivity.java
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

// Bug 34833 (https://code.google.com/p/android/issues/detail?id=34833)
// was filed on July 12, 2012 and reports that as of Android 4.1, the
// DatePickerDialog and the TimePickerDialog are buggy. The UI for
// each only shows a Done button with no Cancel option other than the
// Back button. However, due to the way that onStop() is implemented,
// the onDateSet() or onTimeSet() callbacks will be called either once
// if Back is pressed, or twice if Done is pressed. For this sample
// application, FixedDatePickerDialog was created to show a workaround,
// but TimePickerDialog was left alone so you could see the problem
// behavior. However, there probably isn't a lot of harm in the way
// that it calls the callback twice. So it might be easier to simply
// live with the double-calling. Check the bug report for the latest
// developments and workaround ideas.

public class MainActivity extends Activity
implements OnDialogDoneListener
{
	public static final String
    LOGTAG = "PickerDialogFragDemo";

	public static String 
	TIME_DIALOG_TAG = "TIME_DIALOG_TAG";

	public static String 
    DATE_DIALOG_TAG = "DATE_DIALOG_TAG";

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // FragmentManager.enableDebugLogging(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ 
    	super.onCreateOptionsMenu(menu);
 	   	MenuInflater inflater = getMenuInflater();
 	   	inflater.inflate(R.menu.menu, menu);
    	return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId()) {
    	case R.id.menu_show_time_dialog:
    		this.testTimePickerDialog();
    		break;
    	case R.id.menu_show_date_dialog:
    		this.testDatePickerDialog();
    		break;
    	}
    	return true;
    }

    private void testTimePickerDialog()
    {
    	FragmentTransaction ft = 
    		getFragmentManager().beginTransaction();
    	
    	TimePickerFragment tpf = new TimePickerFragment();
    	
    	tpf.show(ft, TIME_DIALOG_TAG);
    }

	private void testDatePickerDialog()
    {
    	FragmentTransaction ft = 
    		getFragmentManager().beginTransaction();
    	
    	DatePickerFragment dpf = new DatePickerFragment();
    	
    	dpf.show(ft, DATE_DIALOG_TAG);
    }

	public void onDialogDone(String tag, boolean cancelled, CharSequence message) {
		String s = tag + " responds with: " + message;
		if(cancelled)
			s = tag + " was cancelled by the user";
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
		Log.v(LOGTAG, s);
	}
}