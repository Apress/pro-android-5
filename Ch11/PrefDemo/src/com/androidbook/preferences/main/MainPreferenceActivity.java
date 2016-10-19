package com.androidbook.preferences.main;

import java.util.List;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class MainPreferenceActivity extends PreferenceActivity
{
    @Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.headers, target);
	}
	
    public static class Frag1 extends PreferenceFragment
        implements OnPreferenceChangeListener, 
                   OnSharedPreferenceChangeListener {
    	// Technically we would do one or the other of the above
    	// interfaces, and most likely the OnPreferenceChangeListener

    	// If this weren't a static inner class, these wouldn't
    	// need to be static either.
	    private static EditTextPreference pkgPref;
		private static EditTextPreference emailPref;
		private static ListPreference listPref;
		private static MultiSelectListPreference pizzaPref;

		@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main);

            // These are the ones we want to work with in the change
            // callback, so we get references to them here.
            listPref = (ListPreference)findPreference("flight_sort_option");
            pkgPref = (EditTextPreference)
                          findPreference("package_name_preference");
            pkgPref.setSummary(pkgPref.getText());
            emailPref = (EditTextPreference)
                          findPreference("alert_email_address");
            emailPref.setSummary(emailPref.getText());
            pizzaPref = (MultiSelectListPreference)findPreference("pizza_toppings");
        }
		
		@Override
		public boolean onPreferenceChange(Preference preference,
				                          Object newValue) {
			// This callback is invoked before the change occurs,
			// giving you the option to return false to prevent the
			// change from happening. In our example, we let the
			// change occur and update the summaries as needed.
	        final String key = preference.getKey();
	        if ("package_name_preference".equals(key)) {
	        	pkgPref.setSummary(newValue.toString());
	        }
	        else if ("alert_email_address".equals(key)) {
	        	emailPref.setSummary(newValue.toString());
	        }
	        else if ("flight_sort_option".equals(key)) {
	        	setFlightOptionsSummary(newValue.toString());
	        }
	        // For pizza toppings, we set a limit of 4, and
			// prevent the user from selecting more than that.
	        else if ("pizza_toppings".equals(key)) {
	        	if(((Set<String>) newValue).size() > 4) {
	        		Toast.makeText(getActivity(), 
	        			"Too many toppings. No more than 4 please",
	        			Toast.LENGTH_LONG)
	        			.show();
	        		return false;
	        	}
	        }
	        // true indicates it's okay to update the preference
	        return true;  
		}
		
		public void onResume() {
			super.onResume();
			// If we wanted to be notified of all preference
			// changes within SharedPreferences, we'd use the
			// following commented out lines:
			// getPreferenceManager().getSharedPreferences()
			//        .registerOnSharedPreferenceChangeListener(this);
			// If you uncomment the line above, comment out all
			// of the following lines:
			listPref.setOnPreferenceChangeListener(this);
			setFlightOptionsSummary(null);
			pkgPref.setOnPreferenceChangeListener(this);
			pkgPref.setSummary(pkgPref.getText());
			emailPref.setOnPreferenceChangeListener(this);
			emailPref.setSummary(emailPref.getText());
			pizzaPref.setOnPreferenceChangeListener(this);
		}
		
		public void onPause() {
			super.onPause();
			// getPreferenceManager().getSharedPreferences()
			//        .unregisterOnSharedPreferenceChangeListener(this);
			listPref.setOnPreferenceChangeListener(null);
			pkgPref.setOnPreferenceChangeListener(null);
			emailPref.setOnPreferenceChangeListener(null);
			pizzaPref.setOnPreferenceChangeListener(null);
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			// This callback is invoked after the preference has changed.
			// There is no way here to prevent too many pizza toppings from
			// being selected so we don't even try.
			if("package_name_preference".equals(key)) {
				pkgPref.setSummary(pkgPref.getText());
			}
			else if("alert_email_address".equals(key)) {
				emailPref.setSummary(emailPref.getText());
			}
			else if("flight_sort_option".equals(key)) {
				setFlightOptionsSummary(listPref.getValue());
			}
		}

		private void setFlightOptionsSummary(String newValue) {
			String setTo = newValue;
			if(setTo == null)
				setTo = listPref.getValue();
	    	String[] optionEntries = this.getResources().getStringArray(R.array.flight_sort_options);
	    	try { 
	    		// Within a PreferenceFragment, it's much easier to lookup the display text.
	    		// The findIndexOfValue() method is very handy when you have access to the Preference.
			    listPref.setSummary("Currently set to " +
			    	optionEntries[listPref.findIndexOfValue(setTo)]);
	    	}
	    	catch(Exception e) {
	    		listPref.setSummary("Preference error: unknown value of listPref: " + setTo);
	    	}
		}
    }
    
    // This is just a simple fragment with basic preferences in it.
    public static class Frag2 extends PreferenceFragment {
		@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.frag2);
		}
    }
}
