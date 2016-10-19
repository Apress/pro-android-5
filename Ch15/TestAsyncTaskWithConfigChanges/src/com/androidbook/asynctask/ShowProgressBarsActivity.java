package com.androidbook.asynctask;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Primary goal:
 * ***********************
 * 1. To show a series of progress bars and how their layouts look like
 * 2. Not a critical activity for the chapter
 * 
 * Stats
 * ********************
 * Activity name: ShowProgressBarsActivity
 * Layout file: show_progressbars_activity_layout (?)
 * Layout shortcut prefix for ids: spb_
 * Menu file: spb_menu.xml
 * 
 * >> Not required but does no harm 
 * Retained root object: AsyncTesterRADO
 * Retained Fragment: AsyncTesterFragment
 * 
 * Other fragments: None
 * Configuration change: allowed, and covered
 * Home and back: allowed, and covered
 * Dialogs: none
 * Asynctasks: yes, 1
 * 
 *
 */
public class ShowProgressBarsActivity 
extends MonitoredActivityWithADOSupport
implements IReportBack
{
	public static final String tag="ShowProgressBarsActivity";
	
	//Establish these pointers during onCreate
	//they being retaiend objects;
	AsyncTesterRADO asyncTester = null;
	AsyncTesterFragment asyncTesterFragment = null;	

	public ShowProgressBarsActivity()	{
		super(tag);
	}
	
	AsyncTesterRADO getAsyncTester()	{
		return (AsyncTesterRADO)getRootRADO();
	}
	
	/*
	 * This is currently called from onStart
	 * Move it to onCreate
	 * (non-Javadoc)
	 * @see com.androidbook.asynctask.MonitoredActivityWithADOSupport#onCreateRADO()
	 */
    @Override
    public RetainedADO onCreateRADO() 
    {
    	return new AsyncTesterRADO(this);
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //I should be guranteed to have my root RADO here!
        //I should be able to add my retained fragments here
        //Having my retained objects here ensures 
        //I can call my menus later and be assured their
        //executors are available.
    	asyncTester = getAsyncTester();
        
        asyncTesterFragment =
        	AsyncTesterFragment.establishRetainedAsyncTesterFragment(this);
        
        setContentView(R.layout.spb_show_progressbars_activity_layout);
    }
	public void reportBack(String tag, String message)
	{
	}
	public void reportTransient(String tag, String message)
	{
	}
    
}//eof-class