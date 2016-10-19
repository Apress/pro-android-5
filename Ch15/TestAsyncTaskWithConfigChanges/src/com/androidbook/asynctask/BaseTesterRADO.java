package com.androidbook.asynctask;

import android.app.Activity;
import android.util.Log;

/*
 * Name: BaseTester RetainedADO
 * 1. Expected to be a member variable in an activity
 * 2. Encapsulates multiple functions in response to menu 
 * clicks.
 * 3. Act as a base class for other kinds of testers
 */
public class BaseTesterRADO 
extends RetainedADO
implements IReportBack
{
	public BaseTesterRADO(Activity act, String tag)	{
		super(act, tag);
		if (!(act instanceof IReportBack)) {
			throw new RuntimeException("Sorry, the activity should support IReportBack interface");
		}
	}
	public void reportBack(String tag, String message)	{
		if (!isActivityReady())		{
			Log.d(tag,"sorry activity is not ready during reportback");
			return;
		}
		//activity is good
		getReportBack().reportBack(tag,message);
	}
	
	private IReportBack getReportBack()	{
		return (IReportBack)getActivity();
	}

	public void reportTransient(String tag, String message)	{
		if (!isActivityReady())		{
			Log.d(tag,"sorry activity is not ready during reportback");
			return;
		}
		//activity is good
		getReportBack().reportBack(tag,message);
	}
}//eof-class
