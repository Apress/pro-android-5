package com.androidbook.asynctask;

import android.app.Activity;

/*
 * See IActivityDependentObject
 * See RecreatedADO
 * See RetainedADO
 * 
 * This acts as a place holder for common
 * member variables.
 * 
 * This class is abstract because the way 
 * an activity is passed depends on the derived class.
 * 
 */
public abstract class DefaultADO 
implements IActivityDependentObject
{
	//This variable should work well between
	//the same thread or between threads
	protected volatile MonitoredActivityWithADOSupport m_parent = null;
	
	//This is used for debug messages
	//you want direct access to child classes
	protected String tag = null;
	
	//this forces child classes to always provide a tag
	//Need for Default constructor is not needed
	//because this class is not going to be recreated by
	//the framework!
	//Implication is that you are not going to pass it 
	//through bundles!!
	public DefaultADO(String intag)
	{
		tag = intag;
	}
	
	public MonitoredActivityWithADOSupport getActivity()
	{
		return m_parent;
	}
	//The way an activity is set 
	//is through indirect means
	protected void setActivity(MonitoredActivityWithADOSupport a)
	{
		m_parent = a;
	}
}
