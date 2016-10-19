package com.androidbook.asynctask;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class AsyncTesterFragment
extends BaseTesterFragment
implements IWorkerObjectClient
{
	private final static String tag="AsyncTesterFragment";
	private final static String FRAGMENT_TAG="RetainedAsyncTesterFragment";

	//This pointer is there to coordinate/manage the lifetime
	//Starts when the menu is clicked
	//Sets to null when the activity dialog closes 
	//after the task is done!
	//
	MyLongTaskWithFragment longTaskWithFragment = null;
	
	//invoked by a menu
	//You need to hold on to it because you may need it
	//to tell it that the activity is up
	//during home and back.
	//You needed this for a dialog
	//but you may not need this for a bar!!
	MyLongTaskWithProgressBar longTaskWithProgressBar = null;
	
	int tryCount = 1;
	
	/*
	 * Default constructor required by the framework for rebirth
	 */
	public AsyncTesterFragment(){};
	
	public void init()
	{
		Bundle b = new Bundle();
		super.init(tag, b);
	}	//Call this from a menu item
	
	public static AsyncTesterFragment newInstance()
	{
		AsyncTesterFragment i = new AsyncTesterFragment();
		i.init();
		return i;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		MonitoredActivityWithADOSupport act = (MonitoredActivityWithADOSupport)getActivity();
		if (longTaskWithFragment != null)
		{
			longTaskWithFragment.onStart(act);
		}
		if (longTaskWithProgressBar != null)
		{
			longTaskWithProgressBar.onStart(act);
		}
	}
	
	
	
	
	public void testFragmentProgressDialog()
	{
		//Construction args:
		//arg1: parent fragment
		//	so that the asynctask can get to the activity
		//	or to the dialog fragment
		//arg2: reportback
		//	so that the asynctask can report back errors etc
		//	this test fragment can play that role as well
		//arg3: Just a tag
		longTaskWithFragment = 
			new MyLongTaskWithFragment(this,this,"Task With Dialogs");
			//new MyLongTaskWithFragment(this,this,"Task With Dialogs " + tryCount++);
		
		longTaskWithFragment.registerClient(this, 0);
		//Execute it
		longTaskWithFragment.execute("String1","String2","String3");
	}
	
	public void testFragmentProgressBar()
	{
		//Construction args:
		//arg1: parent fragment
		//	so that the asynctask can get to the activity
		//	or to the dialog fragment
		//arg2: reportback
		//	so that the asynctask can report back errors etc
		//	this test fragment can play that role as well
		//arg3: Just a tag
		longTaskWithProgressBar = 
			new MyLongTaskWithProgressBar(this,this,"Task With Progress bar");
		
		longTaskWithProgressBar.registerClient(this, 1);
		//Execute it
		longTaskWithProgressBar.execute("String1","String2","String3");
	}
	/*
	 * All this does is to create a fragment that I can use to test
	 * functionality.
	 * this fragment will be retained.
	 * I can locate it anytime I want
	 * 
	 * I can use menus to invoke functions on it
	 * 
	 */
	public static AsyncTesterFragment createRetainedAsyncTesterFragment(Activity act)
	{
		Log.d(tag,"Creatign the async tester fragment");
		AsyncTesterFragment frag = AsyncTesterFragment.newInstance();
		frag.setRetainInstance(true);
		//set args if there are any
		FragmentManager fm = act.getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(frag, AsyncTesterFragment.FRAGMENT_TAG);
		ft.commit();
		return frag;
	}
	public static AsyncTesterFragment establishRetainedAsyncTesterFragment(Activity act)
	{
		AsyncTesterFragment frag = getRetainedAsyncTesterFragment(act);
		if (frag == null)
		{
			return createRetainedAsyncTesterFragment(act);
		}
		Log.d(tag,"Fragment is already there");
		return frag;
	}
	public static AsyncTesterFragment getRetainedAsyncTesterFragment(Activity act)
	{
		Fragment frag = act.getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
		if (frag == null)
		{
			//sorry that frag doesn't exist
			Log.d(tag,"Fragment doesn't exist");
			return null;
		}
		return (AsyncTesterFragment)frag;
	}
	//asynctask callback
	public void done(IWorkerObject wobj, int workerObjectPassthroughIdentifier)
	{
		if (workerObjectPassthroughIdentifier == 0)
		{
			Log.d(tag, "The asynctask has finished. the dialog is closed. release the pointer");
			this.longTaskWithFragment = null;
		}
		else if (workerObjectPassthroughIdentifier == 1)
		{
			Log.d(tag, "The asynctask with pbar has finished. release the pointer");
			this.longTaskWithProgressBar = null;
		}
	}
	public void releaseResources()
	{
		Log.d(tag,"Fragment release resources");
		if (longTaskWithProgressBar != null)
		{
			longTaskWithProgressBar.releaseResources();
		}
	}
	
}//eof-class

