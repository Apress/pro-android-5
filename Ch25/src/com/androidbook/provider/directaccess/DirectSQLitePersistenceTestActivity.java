package com.androidbook.provider.directaccess;

import android.util.Log;
import android.view.MenuItem;

import com.androidbook.provider.IReportBack;
import com.androidbook.provider.MonitoredDebugActivity;
import com.androidbook.provider.R;

/**
 * This is a driver activity with a menu to test persistence
 * features of sqlite. It delegates most of work SQLitePersistenceTester
 * 
 * Uses base class to load a standard debug based layout with a menu
 *
 * Menu: R.menu.test_book_persistence_menu
 * 
 */
public class DirectSQLitePersistenceTestActivity 
extends MonitoredDebugActivity
implements IReportBack
{
	public static final String tag="DirectSQLitePersistenceTestActivity";
	private SQLitePersistenceTester sqlitePeristenceTester = null;
	
	public DirectSQLitePersistenceTestActivity()
	{
		super(R.menu.test_book_persistence_menu,tag);
		this.retainState();
		sqlitePeristenceTester = new SQLitePersistenceTester(this,this);
	}
    protected boolean onMenuItemSelected(MenuItem item)
    {
    	Log.d(tag,item.getTitle().toString());
    	if (item.getItemId() == R.id.menu_add_book)
    	{
    		sqlitePeristenceTester.addBook();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_books)
    	{
    		sqlitePeristenceTester.showBooks();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_delete_book)
    	{
    		sqlitePeristenceTester.removeBook();
    		return true;
    	}
    	return true;
    }
}