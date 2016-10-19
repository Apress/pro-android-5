package com.androidbook.asynctask;

import android.app.Activity;

/*
 * why?
 * 
 * I could have named it ActivityWorkerObject!
 * 
 * This object is dependent on an activity
 * it holds a pointer to the activity
 * 
 */
public interface IActivityDependentObject 
{
	public MonitoredActivityWithADOSupport getActivity();
}
