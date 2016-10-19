package com.androidbook.asynctask;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 1. Encapsulates Key Callbacks
 * 2. Documents them
 * 3. Monitors them
 * 4. Provides a pattern for what to override
 * 5. works as a sample for extending fragment
 * 
 */
public class MonitoredFragment 
extends Fragment
{
	public static String DEBUG_TAG_NAME="com.androidbook.asynctask.debugtag";
	private String tag = "No tag specified yet. Still being constructed";
	
	//Set it on start
	//Reset it on stop
	boolean bUIReady = false;
	
	/*
	 * Default constructor required by the framework for rebirth
	 */
	public MonitoredFragment(){};
	/**
	 * This is for derived classes to put restrictions on
	 * what needs to be in the args bundle.
	 */
	public void init(String tagname, Bundle argsBundle)
	{
		argsBundle.putString(this.DEBUG_TAG_NAME,tagname);
		this.setArguments(argsBundle);
	}

	/*
	 * Order: 1
	 * after: onCreate
	 * This is the first method that gets called
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		reConstructTagNameFromArgsBundle();
		Log.d(tag,"onAttach called. Very first call back");
	}
	
	private void reConstructTagNameFromArgsBundle()
	{
		Bundle args = this.getArguments();
		if (args == null)
		{
			Log.w(tag,"Sorry no args set to collect the tag name");
			return;
		}		
		
		String tagname = args.getString(this.DEBUG_TAG_NAME);
		if (tagname == null)
		{
			Log.w(tag,"Sorry no debug tag specified");
			return;
		}
		else
		{
			//tagname is available
			tag = tagname;
		}
	}
	
	/*
	 * Order: 2
	 * method before: onAttach
	 * method after: onCreateView
	 * 
	 * Key notes:
	 * 1. Activity hasn't finished creating
	 * 2. Activitys views are not ready
	 * 3. To know that the activitys views are available use
	 * onActivityCreated callback
	 * 4. You can read the argument bundle to do initializations.
	 * 5. Most likely actitivity has been attached to other fragments as well
	 * 6. What ever you can do here can do in onCreateView or
	 * onActivityCreated
	 * 7. However as onCreateView may not get called all the time you 
	 * may want to move that work to onActivityCreated
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(tag,"onCreate called. Activity creation hasn't finished yet.");
	}
	
	/*
	 * Order: 3
	 * method before: onCreate
	 * this method: onCreateView
	 * method after: onActivityCreated
	 * 
	 * Key notes:
	 * 1. Load the view
	 * 2. return the view
	 * 3. It may not be called if there is parent attached
	 * 4. Don't use the view group to directly attach
	 * 5. No need to implement for headless fragments
	 * 6. Any initialization work you put here is suspect as it
	 * may not get called.
	 * 7. So put that initialization code in onCreate or the 
	 * next method onActivityCreated
	 * 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	/*
	 * Order: 4
	 * method before: onCreateView
	 * this method: onActivityCreated
	 * method after: onStart
	 * 
	 * Key notes:
	 * 1. Activities views are good
	 * 2. Views of the fragment have been integrated
	 * 3. Do any initialization based on 1 and 2
	 * 4. This is an important equilibrium call
	 * 5. retained fragments should do most of their work here
	 * 
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		Log.d(tag,"onActivityCreated called. All views including activity are ready.");
	}


	/*
	 * Order: 5
	 * method before: onActivityCreated
	 * this method: onStart
	 * method after: onResume
	 * 
	 * Key notes:
	 * 1. follow the activity
	 * 
	 */
	@Override
	public void onStart() {
		super.onStart();
		Log.d(tag,"onstart");
		bUIReady = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(tag,"onResume");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(tag,"onConfigChanged");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(tag,"onDestroy");
		if (getActivity().isFinishing() == true){
			releaseResources();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(tag,"onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(tag,"onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(tag,"onPause");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(tag,"onSaveInstanceState");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(tag,"onstop");
		bUIReady = false;
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		Log.d(tag,"onTrimMemory. level:" + level);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	//***********************************************************
	//* Additional behavior
	//***********************************************************
	public boolean isActivityReady()
	{
		Activity act = getActivity();
		if(act != null) return true;
		return false;
	}
	
	public boolean isUIReady()
	{
		return bUIReady;
	}
	public void releaseResources()
	{
		Log.d(tag,"Fragment release resources");
	}
}
