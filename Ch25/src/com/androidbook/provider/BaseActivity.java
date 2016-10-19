package com.androidbook.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public abstract class BaseActivity extends MonitoredActivity
{
	private int menuid = 0;
	private int layoutid = 0;
	
	protected BaseActivity(int defaultLayoutId, int defaultMenuId, String intag)
	{
		super(intag);
		menuid = defaultMenuId;
		layoutid = defaultLayoutId;
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 	   	setContentView(layoutid);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){ 
    	if (menuid > 0)
    	{
	 	   	MenuInflater inflater = getMenuInflater();
	 	   	inflater.inflate(menuid, menu);
	    	return true;
    	}
    	else
    	{
        	return super.onCreateOptionsMenu(menu);
    	}
    }
    //override this to deal with menus
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	return super.onOptionsItemSelected(item);
    }
    
    protected void gotoActivty(Class<? extends Activity> activityClass)
    {
      Intent intent = new Intent(this,activityClass);
      startActivity(intent);
    }    
}
