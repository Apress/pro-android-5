package com.androidbook.provider;

import android.util.Log;
import android.view.MenuItem;

public class ContentProviderTestActivity extends MonitoredDebugActivity
implements IReportBack
{
	public static final String tag="HelloWorld";
	private ProviderTester providerTester = null;
	
	public ContentProviderTestActivity()
	{
		super(R.menu.test_book_persistence_menu,tag);
		this.retainState();
		providerTester = new ProviderTester(this,this);
	}
    protected boolean onMenuItemSelected(MenuItem item)
    {
    	Log.d(tag,item.getTitle().toString());
    	if (item.getItemId() == R.id.menu_add_book)
    	{
    		providerTester.addBook();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_books)
    	{
    		providerTester.showBooks();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_delete_book)
    	{
    		providerTester.removeBook();
    		return true;
    	}
    	return true;
    }
}