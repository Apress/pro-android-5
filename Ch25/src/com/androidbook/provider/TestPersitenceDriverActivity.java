package com.androidbook.provider;

import android.view.View;

import com.androidbook.provider.directaccess.DirectSQLitePersistenceTestActivity;

/**
 * Basics
 * ********************
 * Activity name: TestPersitenceDriverActivity
 * Layout file: test_persistence_driver_activity_layout.xml
 * Layout shortcut prefix for ids: tpda_
 * Menu file: No menu file
 * 
 * Stats
 * ********************
 * Retained root object: none
 * Retained Fragment: none
 * Other fragments: None
 * Configuration change: n/a
 * Home and back: n/a
 * Dialongs: none
 * Asynctasks: none
 * 
 * 
 * Primary goal:
 * ***********************
 * 1. Home page
 * 2. Invoke the other activities
 *
 */
public class TestPersitenceDriverActivity extends BaseActivity 
{
	public static String tag = "TestPersitenceDriverActivity";
	public TestPersitenceDriverActivity()
	{
		super(R.layout.test_persistence_driver_activity_layout,
				-1, //no menu file
				tag);
	}
	public void startTestContentProviderActivity(View btn1)
	{
		gotoActivty(ContentProviderTestActivity.class);
	}	
	public void startTestDirectSQLiteStorageActivity(View btn1)
	{
		gotoActivty(DirectSQLitePersistenceTestActivity.class);
	}	
}//eof-class
