package com.androidbook.asynctask;

import android.os.Bundle;
import android.util.Log;

/*
 * Name: BaseTesterFragment
 *
 * 1. will be a retained non-ui fragment
 * 2. acts as a conduit to the parent activity as well
 * 3. provides access to the activity
 * 
 * Notes;
 * Because it is a headless fragment onCreateView won't be called 
 */
public class BaseTesterFragment 
extends MonitoredFragment
implements IReportBack
{
	/*
	 * Default constructor required by the framework for rebirth
	 */
	public BaseTesterFragment(){};
	
	public void init(String tagname, Bundle argsBundle)
	{
		super.init(tagname, argsBundle);
	}
	

	public void reportBack(String tag, String message) 
	{
		if (!isUIReady())
		{
			Log.d(tag,"sorry activity is not ready during reportback");
			return;
		}
		//activity is good
		getReportBack().reportBack(tag,message);
	}
	
	private IReportBack getReportBack()
	{
		return (IReportBack)getActivity();
	}

	public void reportTransient(String tag, String message) 
	{
		if (!isUIReady())
		{
			Log.d(tag,"sorry activity is not ready during reportback");
			return;
		}
		//activity is good
		getReportBack().reportBack(tag,message);
	}
	
}//eof-class
