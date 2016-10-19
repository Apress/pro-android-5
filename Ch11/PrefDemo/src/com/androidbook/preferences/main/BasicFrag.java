package com.androidbook.preferences.main;
// This file is BasicFrag.java

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class BasicFrag extends PreferenceFragment {

	// You don't have to do much to create a fragment to hold
	// a PreferenceScreen. This fragment class was created as
	// a separate class to show that you can, as opposed to
	// having to create inner classes of PreferenceFragment
	// inside a PreferenceActivity. Either way works.
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.nested_screen_basicfrag);
    }
}
