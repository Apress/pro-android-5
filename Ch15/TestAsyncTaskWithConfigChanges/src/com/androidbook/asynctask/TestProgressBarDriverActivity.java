package com.androidbook.asynctask;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Stats
 * ********************
 * Activity name: TestProgressBarDriverActivity
 * Layout file: test_progressbar_activity_layout
 * Layout shortcut prefix for ids: tpb_
 * Menu file: tpb_menu.xml
 * Retained root object: AsyncTesterRADO
 * Retained Fragment: AsyncTesterFragment
 * Other fragments: None
 * Configuration change: allowed, and covered
 * Home and back: allowed, and covered
 * Dialongs: none
 * Asynctasks: yes, 1
 * 
 * 
 * Primary goal:
 * ***********************
 * 1. To test a variety of progress bars
 *
 */
public class TestProgressBarDriverActivity 
extends MonitoredActivityWithADOSupport
implements IReportBack
{
	public static final String tag="TestProgressBarDriverActivity";
	
	//Establish these pointers during onCreate
	//they being retaiend objects;
	AsyncTesterRADO asyncTester = null;
	AsyncTesterFragment asyncTesterFragment = null;	

	
	public TestProgressBarDriverActivity()
	{
		super(tag);
	}
	
	AsyncTesterRADO getAsyncTester()
	{
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
        
        setContentView(R.layout.test_progressbar_activity_layout);
		ProgressBar pb = (ProgressBar)findViewById(R.id.tpb_progressBar1);
		pb.setSaveEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){ 
    	super.onCreateOptionsMenu(menu);
 	   	MenuInflater inflater = getMenuInflater();
 	   	inflater.inflate(R.menu.tpb_menu, menu);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){ 
    	appendMenuItemText(item);
    	if (item.getItemId() == R.id.tpb_menu_test1){
    		asyncTesterFragment.testFragmentProgressBar();
    		return true;
    	}
       	return true;
    }
    private TextView getTextView(){
        return (TextView)this.findViewById(R.id.tpb_text1);
    }
    private void appendMenuItemText(MenuItem menuItem){
       String title = menuItem.getTitle().toString();
       TextView tv = getTextView(); 
       tv.setText(tv.getText() + "\n" + title);
    }
    private void emptyText(){
       TextView tv = getTextView();
       tv.setText("");
    }
    private void appendText(String s){
       TextView tv = getTextView(); 
       tv.setText(tv.getText() + "\n" + s);
       Log.d(tag,s);
    }
	public void reportBack(String tag, String message)
	{
		this.appendText(tag + ":" + message);
		Log.d(tag,message);
	}
	public void reportTransient(String tag, String message)
	{
		String s = tag + ":" + message;
		Toast mToast = 
		  Toast.makeText(this, s, Toast.LENGTH_SHORT);
		mToast.show();
		reportBack(tag,message);
		Log.d(tag,message);
	}
	
	private ProgressBar getProgressBar()
	{
		ProgressBar pb = (ProgressBar)findViewById(R.id.tpb_progressBar1);
		return pb;
	}
	/*
	//To test the progress bar flip state problem
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		ProgressBar pb = getProgressBar();
		if (pb!=null)
		{
			outState.putInt("curprogress", pb.getProgress());
			Log.d(tag,"Placing current progress:" + pb.getProgress());
		}
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		ProgressBar pb = getProgressBar();
		if (pb!=null)
		{
			pb.setMax(15);
			int curprogress = savedInstanceState.getInt("curprogress");
			Log.d(tag,"Placing current progress:" + curprogress);
			if (curprogress >= 0)
			{
				//pb.setProgress(curprogress);
			}
		}
	}
	*/
}//eof-class