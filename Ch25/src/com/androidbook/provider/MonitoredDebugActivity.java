package com.androidbook.provider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public abstract class MonitoredDebugActivity extends BaseActivity
implements IReportBack
{
	//Derived classes needs first
    protected abstract boolean onMenuItemSelected(MenuItem item);
    
    //private variables set by constructor
	private static String tag=null;
	private boolean m_retainState = false;
	
	public MonitoredDebugActivity(int inMenuId, String inTag)
	{
		super(R.layout.debug_activity_layout,
				R.menu.test_book_persistence_menu,
				inTag);
		tag = inTag;
	}
	public void retainState()
	{
		m_retainState = true;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){ 
    	appendMenuItemText(item);
    	if (item.getItemId() == R.id.menu_da_clear){
    		this.emptyText();
    		return true;
    	}
    	return onMenuItemSelected(item);
    }
    private TextView getTextView(){
        return (TextView)this.findViewById(R.id.text1);
    }
    protected void appendMenuItemText(MenuItem menuItem){
       String title = menuItem.getTitle().toString();
       TextView tv = getTextView(); 
       tv.setText(tv.getText() + "\n" + title);
    }
    protected void emptyText(){
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
	
	//Implement save/restore
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		String st = savedInstanceState.getString("debugViewText");
		if (st == null) return;
        TextView tv = getTextView();
        tv.setText(st);
        Log.d(tag,"Restored state");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (this.m_retainState == false) return;
		
	   //save state
       TextView tv = getTextView(); 
       String t = tv.getText().toString();
       outState.putString("debugViewText",t);
       Log.d(tag,"Saved state");
	}
}