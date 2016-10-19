package com.androidbook.asynctask;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class TestAsyncTaskDriverActivity 
extends MonitoredActivityWithADOSupport
implements IReportBack
{
	public static final String tag="AsyncTaskDriverActivity";
	
	//Establish these pointers during onCreate
	//they being retaiend objects;
	AsyncTesterRADO asyncTester = null;
	AsyncTesterFragment asyncTesterFragment = null;	

	public TestAsyncTaskDriverActivity() {
		super(tag);
	}
	AsyncTesterRADO getAsyncTester() {
		return (AsyncTesterRADO)getRootRADO();
	}
	/*
	 * This is currently called from onStart
	 * Move it to onCreate
	 */
    @Override
    public RetainedADO onCreateRADO() {
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
        
        setContentView(R.layout.main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){ 
    	super.onCreateOptionsMenu(menu);
 	   	MenuInflater inflater = getMenuInflater();
 	   	inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){ 
    	appendMenuItemText(item);
    	if (item.getItemId() == R.id.menu_test_flip){
    		asyncTester.testFragmentProgressDialog();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_test_flip1){
    		asyncTesterFragment.testFragmentProgressDialog();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_test_pbars){
    		startTargetActivity(TestProgressBarDriverActivity.class);
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_pbars){
    		startTargetActivity(ShowProgressBarsActivity.class);
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_test_config_change) {
    		startTargetActivity(TestConfigChangeActivity.class);
    		return true;
    	}
       	return true;
    }
    private TextView getTextView(){
        return (TextView)this.findViewById(R.id.main_text1);
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
}//eof-class