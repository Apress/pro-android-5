package com.androidbook.asynctask;

import android.app.Activity;

/*
 * This object is recreated along with the Activity
 * in its onCreate.
 * 
 * It lives and perishes with the activity object
 * 
 * useful in delegating tasks from activity
 * 
 * keeps the activity code clean
 * 
 * Key notes
 * ***************
 * 1. An activity can host any number of RecreatedADOs
 * 2. An activity can host only one RetainedADO root
 * 3. Because it is orchestrated through onNonConfiguration callback
 * 4. (how this will change if fragments are introduced is tbd)
 * 
 * Caution:
 * **************
 * 1. Do not pass this object to anyone that retains it
 * 2. Because it will be stale on activity restart
 * 3. There will be a loss of the activity object
 * 4. Use in that case RetainedADO
 */
public class RecreatedADO 
extends DefaultADO
{
	public RecreatedADO(MonitoredActivityWithADOSupport in, String intag)
	{
		super(intag);
		setActivity(in);
	}
	/*
	 * This activity will never be null
	 * because this object is not retained across
	 * invocations of the activity restarts.
	 */
	public MonitoredActivityWithADOSupport getActivity()
	{
		return super.getActivity();
	}
}
