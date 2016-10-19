package com.androidbook.asynctask;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

/*
 * What is this abstraction for?
 * 
 * To support Activity Dependent Objects (ADOs)
 * 
 * It will support a single retained root ADO object (root RADO)
 * 
 * if will give you a chance to create it one time 
 * for the lifecycle of the activity.
 * 
 * The root RADO will be maintained across config changes.
 * 
 * It will give an opportunity to create a RADO 
 * through an overrided method during the onStart.
 */
public class MonitoredActivityWithADOSupport 
extends MonitoredActivity
{
	private RetainedADO mRootRADO = null;
	
	//This will false when it is stopped
	//This will turn true on start
	private boolean bUIReady = true;
	
	public MonitoredActivityWithADOSupport(String intag) 
	{
		super(intag);
	}
	
	//use this method and cast the object
	//to your specific type.
	//This can be null if you have forgotten to set it up
	protected RetainedADO getRootRADO()
	{
		if (mRootRADO == null)
		{
			Log.w(tag,"you probably have forgotten to override onCreateRADO!");
		}
		return mRootRADO;
	}
	//The activity is going to be stopped
	//Doesn't mean it is going away
	//but again it might
	//
	//See if you can move it to onRetained method...
	//because, it might a stop but the actiity may come back
	//with its ui intact
	@Override
	protected void onStop() {
		//call super
		super.onStop();
		
		//Indicate that the UI is notready
		bUIReady = false;
		//indicate its unavailability
		if (mRootRADO != null)
		{
			//Make the activity null
			//mRootRADO.reset();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRootRADO = obtainRootRADO();
	}
	
	//Reattache the activity to the root RADO
	@Override
	protected void onStart() {
		//onStart. UI may be partially visible.
		super.onStart();
		bUIReady = true;
		if (mRootRADO == null)
		{
			Log.d(tag,"Root RADO is null");
		}
		else
		{
			Log.d(tag,"Root RADO exists. attaching the parent activity");
			mRootRADO.attach(this);
		}
	}
	
	private RetainedADO obtainRootRADO()
	{
		if (mRootRADO != null)
		{
			//this would be the case if the activity has restarted
			//and rootRADO stays intact
			return mRootRADO;
		}
		//rootRADO is null
		//see if is in the nonconfig instance
		Object rootRADO = this.getLastNonConfigurationInstance();
		if (rootRADO == null)
		{
			//The returned object is null
			//try to crate it
			rootRADO = onCreateRADO();
			return (RetainedADO)rootRADO;
		}
		
		//Last rootRADO is not null and retaiend
		if (!(rootRADO instanceof RetainedADO))
		{
			//it is not a retained ADO
			Log.e(tag,"You are returning non RetainedADO");
			return null;
		}
		//Last root RADO is there and it is the right type
		return (RetainedADO)rootRADO;
	}
	
	/**
	 * Override this method to create your dependent objects
	 * See obtainRootRADO to understand this
	 * This is called during onStart
	 * it may need to be moved to onCreate (or may be not)
	 * Probably because the rootRADO may hold things
	 * that are required to construct the UI as well
	 * so it is then better to call it in onCreate
	 * @return
	 */
	protected RetainedADO onCreateRADO()
	{
		//default is null
		return null;
	}
	

	//Called only when there is a configuration change
	//Called between onStop and onDestroy
	//You can return a local object pointer to 
	//retrieve it back through getConfigurationInstance.
	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d(tag,"onRetainNonConfigurationInstance.");
		if (mRootRADO != null)
		{
			Log.d(tag,"mRootRADO is not null. Resetting its activity");
			//Make the activity null
			mRootRADO.reset();

		}
		return mRootRADO;
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
		if (mRootRADO != null)
		{
			Log.d(tag, "Releasing root RADO resources");
			mRootRADO.releaseContracts();
		}
	}
	
	/**
	 * This will false when it is stopped.
	 * This will turn true on start.
	 * see bUIReady private variable.
	 */
	public boolean isUIReady()
	{
		return bUIReady;
	}
}//eof-class