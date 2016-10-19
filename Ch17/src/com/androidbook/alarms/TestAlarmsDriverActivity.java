package com.androidbook.alarms;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class TestAlarmsDriverActivity extends Activity
implements IReportBack
{
	public static final String tag="TestAlarmsDriverActivity";
	//The following classes extend each other as they go down 
	//private SendAlarmOnceTester alarmTester = null;
	//private SendRepeatingAlarmTester alarmTester = null;
	//private CancelRepeatingAlarmTester alarmTester = null;
	//private ScheduleIntentMultipleTimesTester alarmTester = null;
	private AlarmIntentPrimacyTester alarmTester = null;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        alarmTester = new AlarmIntentPrimacyTester(this,this);
        setupTextViewScrolling();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	//call the parent to attach any system level menus
    	super.onCreateOptionsMenu(menu);
 	   	MenuInflater inflater = getMenuInflater(); //from activity
 	   	inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	appendMenuItemText(item);
    	if (item.getItemId() == R.id.menu_clear)
    	{
    		this.emptyText();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_alarm_once)
    	{
    		alarmTester.sendAlarmOnce();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_alarm_repeated)
    	{
    		alarmTester.sendRepeatingAlarm();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_alarm_cancel)
    	{
    		alarmTester.cancelRepeatingAlarm();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_alarm_multiple)
    	{
    		alarmTester.scheduleSameIntentMultipleTimes();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_alarm_distinct_intents)
    	{
    		alarmTester.scheduleDistinctIntents();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_alarm_intent_primacy)
    	{
    		alarmTester.alarmIntentPrimacy();
    		return true;
    	}
    	return true;
    }
    
    private TextView getTextView()
    {
        return (TextView)this.findViewById(R.id.text1);
    }
    
    private void appendMenuItemText(MenuItem menuItem)
    {
       String title = menuItem.getTitle().toString();
       TextView tv = getTextView(); 
       tv.setText(tv.getText() + "\n\nMenu Item:" + title + "\n****************************");
    }
    private void emptyText()
    {
          TextView tv = getTextView();
          tv.setText("");
    }
    private void appendText(String s)
    {
       TextView tv = getTextView(); 
       tv.setText(tv.getText() + "\n" + s);
       Log.d(tag,s);
    }
	public void reportBack(String tag, String message)
	{
		this.appendText(tag + ":" + message);
		Log.d(tag,message);
	}
	public void reportBack(String tag, int stringResourceId)
	{
		reportBack(tag,getString(stringResourceId));
	}
	public void reportBack(String tag, int stringResourceId, Object...objects)
	{
		getString(stringResourceId,objects);
		reportBack(tag,getString(stringResourceId,objects));
	}
	private void setupTextViewScrolling()
	{
		TextView tv = this.getTextView();
		tv.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
}