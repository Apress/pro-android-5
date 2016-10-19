package com.androidbook.asynctask;

import android.app.Activity;

public class AsyncTesterRADO
extends BaseTesterRADO
{
	private final static String tag="AsyncTesterRADO"; 
	public AsyncTesterRADO(Activity act) {
		super(act, tag);
	}

	//Call this from a menu item
	public void testFragmentProgressDialog()
	{
		//Check status when you start
		logStatus();
		
		//Instantiate an asynctask
		//
		//it is also a RADO object
		//it knows its life cycle
		
		//The task will create a fragment dialog
		//report its progress
		//Close the dialog when it is done
		
		//tbd: May be remove itself from the registry as well!!
		MyLongTaskWithRADO longTaskWithFragmentDialog3 = 
			new MyLongTaskWithRADO(this,this,"Task With Dialogs");
		
		//Execute it
		longTaskWithFragmentDialog3.execute("String1","String2","String3");
	}
}
