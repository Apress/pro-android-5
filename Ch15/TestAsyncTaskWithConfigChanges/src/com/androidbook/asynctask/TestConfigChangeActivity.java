package com.androidbook.asynctask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Primary goal:
 * ***********************
 * 1. To test if an activity retains its state in an edit control on config change
 * 2. Not a critical activity for the chapter
 * 
 * Stats
 * ********************
 * Activity name: TestConfigChangeActivity
 * Layout file: test_config_change_activity_layout
 * Layout shortcut prefix for ids: tcc_
 * Menu file: tcc_menu.xml
 * Retained root object: none
 * Retained Fragment: none
 * Other fragments: None
 * Configuration change: allowed, and covered
 * Home and back: allowed, and covered
 * Dialongs: none
 * Asynctasks: none
 * 
 */
public class TestConfigChangeActivity extends Activity 
{
	public static final String tag="TestConfigChangeActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tcc_test_config_change_activity_layout);
		Log.d(tag,"onCreate called");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){ 
    	super.onCreateOptionsMenu(menu);
 	   	MenuInflater inflater = getMenuInflater();
 	   	inflater.inflate(R.menu.tcc_menu, menu);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){ 
    	appendMenuItemText(item);
    	if (item.getItemId() == R.id.tcc_menu_test1){
    		//startTargetActivity(TestProgressBarDriverActivity.class);
    		reportBack(tag, "item created");
    		return true;
    	}
       	return true;
    }
    private TextView getTextView(){
        return (TextView)this.findViewById(R.id.tcc_text1);
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
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(tag,"onSave called");
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d(tag,"onRestore called");
	}
}//eof-class