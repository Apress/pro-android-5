package com.androidbook.fragments.dialogs;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment
    implements OnDateSetListener {

    private OnDialogDoneListener dialogClient;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        // See MainActivity.java for comments on bug 34833
        return new FixedDatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    	Log.d(MainActivity.LOGTAG, "in onDateSet()");
        // Do something with the date chosen by the user
    	String dateStr = String.format("%02d", month+1) + "/" 
                + String.format("%02d", day) + "/"
                + String.format("%04d", year);
	    dialogClient.onDialogDone(getTag(), false, dateStr);
    }
	
	@Override
	public void onAttach(Activity act) {
		// If the activity we're being attached to has
		// not implemented the OnDialogDoneListener
		// interface, the following line will throw a
		// ClassCastException. This is the earliest we
		// can test if we have a well-behaved activity.
		try {
            dialogClient = (OnDialogDoneListener)act;
		}
		catch(ClassCastException cce) {
			// Here is where we fail gracefully.
			Log.e(MainActivity.LOGTAG, "Activity is not listening");
		}
		super.onAttach(act);
	}
}