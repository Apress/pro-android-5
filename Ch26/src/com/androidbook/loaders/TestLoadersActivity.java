package com.androidbook.loaders;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;

public class TestLoadersActivity 
extends      MonitoredListActivity //very simple class to log activity call backs
implements   LoaderManager.LoaderCallbacks<Cursor> //Loader Manager call backs
             ,OnQueryTextListener //Search text call back to filter contacts 
{
	private static final String tag = "TestLoadersActivity";
	
    //Adapter for displaying the list's data
	//Initialized to null cursor in onCreate and set on the list
	//Use it in later call backs to swap cursor
	//This is reinitialized to null cursor when rotation occurs
    SimpleCursorAdapter mAdapter;
    
    //Search filter working with OnQueryTextListener
    String mCurFilter;    
	
    //These are the Contacts columns that we will retrieve
    static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};

    //This is the select criteria for the contacts URI
    static final String SELECTION = "((" + 
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";
    
    public TestLoadersActivity()  {
    	super(tag);
    }
    @Override     
    protected void onCreate(Bundle savedInstanceState)  {         
    	super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.test_loaders_activity_layout);
    	
    	//Initialize the adapter
    	this.mAdapter = createEmptyAdapter();
    	this.setListAdapter(mAdapter);

    	//Hide the listview and show the progress bar
    	this.showProgressbar();
    	
    	//Initialize a loader for an id of 0
        getLoaderManager().initLoader(0, null, this);
    }
    //Create a simple list adapter with a null cursor
    //The good cursor will come later in the loader call back
    private SimpleCursorAdapter createEmptyAdapter() {
    	// For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1
        //Return the cursor
    	return new SimpleCursorAdapter(this, 
    			android.R.layout.simple_list_item_1,
    			null, //cursor 
    			fromColumns, 
    			toViews);
    }
    //This is a LoaderManager call back. Return a properly constructed CursorLoader
    //This gets called only if the loader does not previously exist.
    //This means this method will not be called on rotation because
    //a previous loader with this ID is already available and initialized.
    //This also gets called when the loader is "restarted" by calling
    //LoaderManager.restartLoader()
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(tag,"onCreateLoader for loader id:" + id);
		Uri baseUri;
		if (mCurFilter != null) {
		    baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
		           Uri.encode(mCurFilter));
		} else {
		    baseUri = Contacts.CONTENT_URI;
		}		
		String[] selectionArgs = null;
		String sortOrder = null;
		return new CursorLoader(this, baseUri,
            PROJECTION, SELECTION, selectionArgs, sortOrder);
	}
    //This is a LoaderManager call back. Use the data here.
    //This gets called when he loader finishes. Called on the main thread.
    //Can be called multiple times as the data changes underneath.
    //Also gets called after rotation with out requerying the cursor.	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		 Log.d(tag,"onLoadFinished for loader id:" + loader.getId());
		 Log.d(tag,"Number of contacts found:" + cursor.getCount());
	     this.hideProgressbar();
	     this.mAdapter.swapCursor(cursor);
	}
    //This is a LoaderManager call back. Remove any references to this data.
    //This gets called when the loader is destroyed like when activity is done.
    //FYI - this does NOT get called because of loader "restart"
    //This can be seen as a "destructor" for the loader.	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(tag,"onLoaderReset for loader id:" + loader.getId());
		this.showProgressbar();
		this.mAdapter.swapCursor(null);
	}
    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
        return true;
    }
    //This is a Searchview call back. Restart the loader.
    //This gets called when user enters new search text.
    //Call LoaderManager.restartLoader to trigger the onCreateLoader
    @Override
    public boolean onQueryTextChange(String newText) {
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        Log.d(tag,"Restarting the loader");
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }
    @Override 
    public boolean onQueryTextSubmit(String query) {
        return true;
    }
    private void showProgressbar() {
    	//show progress bar
    	View pbar = this.getProgressbar();
    	pbar.setVisibility(View.VISIBLE);
    	//hide listview
    	this.getListView().setVisibility(View.GONE);
    	findViewById(android.R.id.empty).setVisibility(View.GONE);
    }
    private void hideProgressbar()  {
    	//show progress bar
    	View pbar = this.getProgressbar();
    	pbar.setVisibility(View.GONE);
    	//hide listview
    	this.getListView().setVisibility(View.VISIBLE);
    }
    private View getProgressbar()  {
    	return findViewById(R.id.tla_pbar);
    }
}//eof-class


