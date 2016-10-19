package com.androidbook.contacts;

import android.util.Log;
import android.view.MenuItem;

public class TestContactsDriverActivity extends DebugActivity
implements IReportBack
{
	public static final String tag="Test Contacts";
	AccountsFunctionTester accountsFunctionTester = null;
	AggregatedContactFunctionTester aggregatedContactFunctionTester = null;
	RawContactFunctionTester rawContactFunctionTester = null;
	ContactDataFunctionTester contactDataFunctionTester = null;
	AddContactFunctionTester addContactFunctionTester = null;
	ProfileRawContactFunctionTester profileRawContactFunctionTester = null;
	AddProfileContactFunctionTester addProfileContactFunctionTester = null;
	ProfileContactDataFunctionTester proifleContactDataFunctionTester = null;
	
	public TestContactsDriverActivity()
	{
		super(R.menu.main_menu,tag);
		accountsFunctionTester = new AccountsFunctionTester(this,this);
		aggregatedContactFunctionTester = new AggregatedContactFunctionTester(this,this);
		rawContactFunctionTester = new RawContactFunctionTester(this,this);
		contactDataFunctionTester = new ContactDataFunctionTester(this,this);
		addContactFunctionTester = new AddContactFunctionTester(this,this);
		profileRawContactFunctionTester = new ProfileRawContactFunctionTester(this,this);
		addProfileContactFunctionTester = new AddProfileContactFunctionTester(this,this);
		proifleContactDataFunctionTester = new ProfileContactDataFunctionTester(this,this);
	}
    protected boolean onMenuItemSelected(MenuItem item)
    {
    	Log.d(tag,item.getTitle().toString());
    	if (item.getItemId() == R.id.menu_show_accounts)
    	{
    		accountsFunctionTester.testAccounts();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_contact_cursor)
    	{
    		aggregatedContactFunctionTester.listContactCursorFields();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_contacts)
    	{
    		aggregatedContactFunctionTester.listContacts();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_single_contact_cursor)
    	{
    		aggregatedContactFunctionTester.listLookupUriColumns();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_rc_all)
    	{
    		rawContactFunctionTester.showAllRawContacts();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_rc)
    	{
    		rawContactFunctionTester.showRawContactsForFirstAggregatedContact();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_rc_cursor)
    	{
    		rawContactFunctionTester.showRawContactsCursor();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_rce_cursor)
    	{
    		contactDataFunctionTester.showRawContactsEntityCursor();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_rce_data)
    	{
    		contactDataFunctionTester.showRawContactsData();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_add_contact)
    	{
    		addContactFunctionTester.addContact();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_profile_raw_contacts)
    	{
    		this.profileRawContactFunctionTester.showAllRawContacts();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_uris)
    	{
    		this.profileRawContactFunctionTester.printVariousUris();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_add_proifle_contact)
    	{
    		this.addProfileContactFunctionTester.addContact();
    		return true;
    	}
    	if (item.getItemId() == R.id.menu_show_prc_all)
    	{
    		this.proifleContactDataFunctionTester.showProfileRawContactsData();
    		return true;
    	}
    	return true;
    }
}