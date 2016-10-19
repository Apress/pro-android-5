package com.androidbook.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class MonitoredActivity extends Activity
{
	protected String tag = null;
	MonitoredActivity(String intag) {tag = intag;}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(tag,"onCreate.");
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void onPause() {
		Log.d(tag,"onpause. I may be partially or fully invisible");
		if (isFinishing() == true)
		{
			Log.d(tag,"The activity is closing by expectation. isFinishing() is true");
		}
		else
		{
			Log.d(tag,"The activity is closing by force or on a config change. isFinishing() is false");
		}
		super.onPause();
	}
	@Override
	protected void onStop() {
		Log.d(tag,"onstop. I am fully invisible");
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		Log.d(tag,"ondestroy. about to be removed");
		if (isFinishing() == true)
		{
			Log.d(tag,"The activity is closing by expectation. isFinishing() is true, like on a back");
			releaseResources();
		}
		super.onDestroy();
	}
	
	//Only called when an activity is restarted
	//without getting destroyed first. 
	//So this will be called when your activity is 
	//hidden and brought back such as going to to home
	//and coming back.
	//
	//onRestart also means, hence, UI is not disturbed
	//
	@Override
	protected void onRestart() {
		Log.d(tag,"onRestart. UI controls are there.");
		super.onRestart();
	}
	@Override
	protected void onStart() {
		Log.d(tag,"onStart. UI may be partially visible.");
		super.onStart();
	}
	@Override
	protected void onResume() {
		Log.d(tag,"onResume. UI fully visible.");
		super.onResume();
	}
	//Called between onStart and onResume
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(tag,"onRestoreInstanceState. You should restore state");
		super.onRestoreInstanceState(savedInstanceState);
	}

	//Called only when there is a configuration change
	//Called between onStop and onDestroy
	//You can return a local object pointer to 
	//retrieve it back through getConfigurationInstance.
	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d(tag,"onRetainNonConfigurationInstance.");
		return super.onRetainNonConfigurationInstance();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(tag,"onSaveInstanceState. You should load up the bundle");
		super.onSaveInstanceState(outState);
	}
	
	//This method is called when you disable a configuration 
	//change in the manifest file for this activity.
	//Instead the activity stays as is and this method is called.
	//
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(tag,"onConfigurationChanged. No rotation occurs.");
		super.onConfigurationChanged(newConfig);
	}

	//You should call this if an activity is 
	//known to be legitly closing.
	//ex:
	//	a) explicit finish
	//	b) implied finish such as back
	//
	//An activity may also close for
	//	a) a config change to reborn
	//	b) low memory process is getting terminated
	protected void releaseResources()
	{
		//default is nothing
		//override this method to do more if needed.
		Log.d(tag,"Release Resources called");
	}
	
	public void startTargetActivity(Class<? extends Activity> targetActivityClass)
	{
		Intent i = new Intent(this,targetActivityClass);
		startActivity(i);		
	}
}

/*
 * One possible order when screen flips
 * ***************************************
//Begining...
onCreate
onStart
onRestoreInstanceState
onResume


//configuration changed
onSaveInstanceState
onPause : partially visible ui
onStop: ui invisible
onRetainNonConfigurationInstance: an local objects you want to remeber
onDestroy

//Back to begining
 * 
 * onRestart and Some other observation
 * *****************************************
 * The sequence of calls will be different
 * for going to home and revisiting the activity.
 * 
 * 1. onRetart will be called instead of onCreate
 * 2. onStart will be called from onRestart
 * 3. See the activity diagram
 * 4. See satyakomatineni.com/item/3528
 * 
 */