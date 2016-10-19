package com.androidbook.provider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class ProviderTester extends BaseTester 
{
	private static String tag = "Provider Tester";
	ProviderTester(Context ctx, IReportBack target)
	{
		super(ctx, target,tag);
	}
	public void addBook()
	{
		Log.d(tag,"Adding a book");
		ContentValues cv = new ContentValues();
		cv.put(BookProviderMetaData.BookTableMetaData.BOOK_NAME, "book1");
		cv.put(BookProviderMetaData.BookTableMetaData.BOOK_ISBN, "isbn-1");
		cv.put(BookProviderMetaData.BookTableMetaData.BOOK_AUTHOR, "author-1");
		
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
		Log.d(tag,"book insert uri:" + uri);
		Uri insertedUri = cr.insert(uri, cv);
		Log.d(tag,"inserted uri:" + insertedUri);
		this.reportString("Inserted Uri:" + insertedUri);
	}
	public void removeBook()
	{
		int firstBookId = this.getFirstBookId();
		if (firstBookId == -1)
		{
			this.reportString("No books are available at this time. Add a book before removing");
			return;
		}
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(firstBookId));
		reportString("Del Uri:" + delUri);
		cr.delete(delUri, null, null);
		this.reportString("Newcount:" + getCount());
	}
	public void showBooks()
	{
		Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = null;
		try
		{
			c = a.getContentResolver().query(uri, 
					null, //projection
					null, //selection string
					null, //selection args array of strings
					null); //sort order
			int iid = c.getColumnIndex(BookProviderMetaData.BookTableMetaData._ID);
			int iname = c.getColumnIndex(BookProviderMetaData.BookTableMetaData.BOOK_NAME);
			int iisbn = c.getColumnIndex(BookProviderMetaData.BookTableMetaData.BOOK_ISBN);
			int iauthor = c.getColumnIndex(BookProviderMetaData.BookTableMetaData.BOOK_AUTHOR);
			
			//Report your indexes
			reportString(
					String.format("Indexes are => id:%s, name:%s, isbn:%%s,author:%s" 
							,iid, iname, iisbn, iauthor));
			
			//walk through the rows based on indexes
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				//Gather values
				String id = c.getString(iid);
				String name = c.getString(iname);
				String isbn = c.getString(iisbn);
				String author = c.getString(iauthor);
				
				//Report or log the row
				StringBuffer cbuf = new StringBuffer(id);
				cbuf.append(",").append(name);
				cbuf.append(",").append(isbn);
				cbuf.append(",").append(author);
				reportString(cbuf.toString());
			}
			
			//Report how many rows have been read
			int numberOfRecords = c.getCount();
			reportString("Num of Records:" + numberOfRecords);
		}
		finally 
		{
			//Close the cursor
			//ideally this should be done in 
			//a finally block.
			if (c!= null) c.close();
		}
	}
	private int getFirstBookId()
	{
		Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = null;
		try
		{
			c = a.getContentResolver().query(uri, 
					null, //projection
					null, //selection string
					null, //selection args array of strings
					null); //sort order
			int numberOfRecords = c.getCount();
			if (numberOfRecords == 0)
			{
				return -1;
			}
			c.moveToFirst();
			int id = c.getInt(c.getColumnIndex(BookProviderMetaData.BookTableMetaData._ID)); //id column
			return id;
		}
		finally
		{
			if (c!= null) c.close();
		}
		
	}
	private int getCount()
	{
		Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = null;
		try
		{
			c = a.getContentResolver().query(uri, 
					null, //projection
					null, //selection string
					null, //selection args array of strings
					null); //sort order
			int numberOfRecords = c.getCount();
			return numberOfRecords;
		}
		finally
		{
			if (c!= null) c.close();
		}
	}
}
