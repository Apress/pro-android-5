package com.androidbook.preferences.main;
// This file is MainActivity.java

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Resources resources;
	private TextView tv = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // The following line sets the default values in the
        // XML data file under /data/data, but you'd be better
        // off using a resource for the default value, and then
        // reference that when getting the preference's value.
        PreferenceManager.setDefaultValues(this, R.xml.main, false);

        // The following is used to get to the resources.
        resources = this.getResources();

        tv = (TextView)findViewById(R.id.text1);
        setOptionText();
    }

    // Add a menu to the application in order to launch the
    // top-level preferences screen.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.mainmenu, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
       if (item.getItemId() == R.id.menu_prefs)
       {
           Intent intent = new Intent()
           		.setClass(this,
                com.androidbook.preferences.main.MainPreferenceActivity.class);
           this.startActivityForResult(intent, 0);
       }
       return true;
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data)
    {
    	super.onActivityResult(reqCode, resCode, data);
    	// Re-read the preference values and show in the main view
    	// of the application's activity.
    	setOptionText();
    }
    
    private void setOptionText()
    {
    	String valuesText;
        SharedPreferences prefs =
        	    PreferenceManager.getDefaultSharedPreferences(this);
//      This is the other way to get to the shared preferences:
//    	SharedPreferences prefs = getSharedPreferences(
//    			"com.androidbook.preferences.main_preferences", 0);

        // Here we use a resource string as the name of the preference,
        // and we use a resource string as the default value. This is
        // the safest way to reliably get a preference's value or the
        // default value if it hasn't been set yet.
    	String flight_option = prefs.getString(
    	        resources.getString(R.string.flight_sort_option),
    	        resources.getString(R.string.flight_sort_option_default_value));

    	// This gets the entries array for flight_sort_options, which contains
    	// the strings that are displayed to the user in the preferences screens.
    	String[] optionEntries = resources.getStringArray(R.array.flight_sort_options);

    	// String together a selection of preferences to display
        valuesText = "option value is " + flight_option + " (" + 
        		optionEntries[Integer.parseInt(flight_option)] + ")";
        
        // Here is the other way to get the display text for a ListPreference
        // but notice how we can't easily get to the ListPreference to easily access
        // the values and entries. We need to know the arrays that make up the
        // ListPreference. In reality we probably just want to use the value and
        // not care about the display text (i.e., string from the entries array).
        String[] optionValues = resources.getStringArray(R.array.flight_sort_options_values);

        int index = 0;
        for (; index < optionValues.length; index++) {
        	if (optionValues[index].equals(flight_option)) {
        	    break;
        	}
        }
        if(index < optionValues.length){
        	valuesText += "\n   ...or the other way to get it (" + optionEntries[index] + ")";
        }

        // The rest of these are different types of preferences and how to
        // get their respective values from SharedPreferences.
        valuesText += "\nShow Airline: " +
        		prefs.getBoolean("show_airline_column_pref", false);

        valuesText += "\nAlert email address: " +
        		prefs.getString("alert_email_address", "");
        
        valuesText += "\nFavorite pizza toppings: " +
        		prefs.getStringSet("pizza_toppings", null);
        
        // Now that we've built a long text message, display it
        tv.setText(valuesText);
        
        
    }
}