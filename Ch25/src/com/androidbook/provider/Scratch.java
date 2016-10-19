package com.androidbook.provider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.androidbook.provider.services.Book;
import com.androidbook.provider.services.DatabaseContext;
import com.androidbook.provider.services.impl.sqlite.AObjectMetadata;
import com.androidbook.provider.services.impl.sqlite.BaseEntitySQLiteMetaData;
import com.androidbook.provider.services.impl.sqlite.BookSQLiteMetaData;


/*
 * Just a text file i need for formatting
 */
public class Scratch 
{
}

/*
private long createBook(Book book)
{
	//Get access to a read database
	SQLiteDatabase db = DirectAccessBookDBHelper.m_self.getReadableDatabase();
	
	//Fill fields from the book object into the content values

	ContentValues bcv = new ContentValues();
	//.... fillo ther fields
	cv.put(BookSQLLiteMetaData.NAME, book.getName());
	cv.put(BookSQLLiteMetaData.ISBN, book.getIsbn());
	cv.put(BookSQLLiteMetaData.AUTHOR, book.getAuthor());
	//.... fill other fields 
	
	//I don't need to insert an empty row
	//usually any nullable column name goes here if I want to insert an empty row
	String nullColumnNameHack = null;
	
    long rowId = db.insertOrThrow(BookSQLLiteMetaData.TABLE_NAME, nullColumnNameHack, bcv);
    return rowId;
}


public class BaseEntitySQLLiteMetaData  
{
	static public final String OWNED_ACCOUNT_COLNAME = "owned_account"; 
	static public final String CREATED_BY_COLNAME = "created_by";
	static public final String CREATED_ON_COLNAME = "created_on";
	static public final String LAST_UPDATED_ON = "last_updated_on";
	static public final String LAST_UPDATED_BY = "last_updated_by";
	static public final String ID_COLNAME = "id";
}

public class BookSQLLiteMetaData extends BaseEntitySQLLiteMetaData 
{
	static public final String TABLE_NAME = "t_books";
	static public final String NAME = "name"; 
	static public final String AUTHOR = "author";
	static public final String ISBN = "isbn";
}

public void updateBook(Book book) {
	if (book.getId() < 0) {
		throw new SQLException("Book id is less than 0");
	}
	//Get access to a writable database
	SQLiteDatabase db = DirectAccessBookDBHelper.m_self.getWritableDatabase();
	
	//Fill fields from the book object into the content values
	ContentValues bcv = new ContentValues();
	//.... fillo ther fields
	cv.put(BookSQLLiteMetaData.NAME, book.getName());
	cv.put(BookSQLLiteMetaData.ISBN, book.getIsbn());
	cv.put(BookSQLLiteMetaData.AUTHOR, book.getAuthor());
	//.... fill other fields 
	
	//You can do this
	String whereClause = String.format("%1 = %2",BookSQLLiteMetaData.ID_COLNAME,book.getId());
	String whereClauseArgs = null;
	//Or the next 4 lines
	String whereClause2 = "? = ?";
	String[] whereClause2Args = new String[2];
	whereClause2Args[0] = BookSQLLiteMetaData.ID_COLNAME;
	whereClause2Args[1] = Integer.toString(book.getId());
	
	int count = db.update(BookSQLLiteMetaData.TABLE_NAME, bcv, whereClause, whereClauseArgs);
	if (count == 0)	{
		throw new SQLException(
				String.format("Failed ot update book for book id:%s",book.getId()));
	}
}

public void deleteBook(int bookid){
	//Get access to a writable database
	SQLiteDatabase db = DirectAccessBookDBHelper.m_self.getWritableDatabase();
	
	String tname = BookSQLLiteMetaData.TABLE_NAME;
	String whereClause = 
		String.format("%s = %s;",
			BookSQLLiteMetaData.ID_COLNAME,
			bookid);
	String[] whereClauseargs = null;
	int i = db.delete(tname,whereClause, whereClauseargs);
	if (i != 1)	{
		throw new RuntimeException("The number of deleted books is not 1 but:" + i);
	}
}


public List<Book> getAllBooks()	{
	SQLiteDatabase db = getReadDb();
	String tname = BookSQLLiteMetaData.TABLE_NAME;
	String[] colnames = BookSQLLiteMetaData.s_self.getColumnNames();
	
	//Selection
	String selection = null; //all rows. Usually a where clause. exclude where part
	String[] selectionArgs = null; //use ?s if you need it
	
	String groupBy = null; //sql group by clause: exclude group by part
	String having = null; //similar
	String orderby = null;
	String limitClause = null; //max number of rows
	//db.query(tname, colnames)
	Cursor c = null;
	
	try {
		c = db.query(tname,colnames,selection,selectionArgs,groupBy,having,orderby,limitClause);
		//This may not be the optimal way to read data through a list
		//Directly pass the cursor back if your intent is to read these one row at a time
		List<Book> bookList = new ArrayList<Book>();
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			Log.d(tag,"There are books");
			Book b = new Book();
			
			//..fill base fields the same way
			b.setName(c.getString(c.getColumnIndex(BookSQLiteMetaData.NAME)));
			b.setAuthor(c.getString(c.getColumnIndex(BookSQLiteMetaData.NAME)));
			b.setIsbn(c.getString(c.getColumnIndex(BookSQLiteMetaData.NAME)));
			//..fill other fileds
			//Or you could delegate this work to the BookSQLiteMetaData object
			//as we have done in the sample downlodable project

			bookList.add(b);
		}
		return bookList;
	}
	finally {
		if (c!= null) c.close();
	}
}

private void doSomethingTransactionally(...some args...) throws Throwable
{
	try
	{
		//Get access to a writable database
		SQLiteDatabase db = DirectAccessBookDBHelper.m_self.getWritableDatabase();
		
		db.beginTransaction();
		//Call other database related methods
		db.setTransactionSuccessful();
	}
	finally
	{
		try {
			db.endTransaction();
		}
		finally {
			DatabaseContext.reset();
		}
	}
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
		int iname = c.getColumnIndex(BookProviderMetaData.BookTableMetaData.BOOK_NAME);
		int iisbn = c.getColumnIndex(BookProviderMetaData.BookTableMetaData.BOOK_ISBN);
		int iauthor = c.getColumnIndex(BookProviderMetaData.BookTableMetaData.BOOK_AUTHOR);
		
		//Report your indexes
		reportString("name,isbn,author:" + iname + iisbn + iauthor);
		
		//walk through the rows based on indexes
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			//Gather values
			String id = c.getString(1);
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

private int getCount() {
	Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
	Activity a = (Activity)this.mContext;
	Cursor c = null;
	try {
		c = a.getContentResolver().query(uri, 
				null, //projection
				null, //selection string
				null, //selection args array of strings
				null); //sort order
		int numberOfRecords = c.getCount();
		return numberOfRecords;
	}
	finally	{
		if (c!= null) c.close();
	}
}

public void removeBook() {
	int firstBookId = this.getFirstBookId();
	if (firstBookId == -1) throw new SQLException("Book id is less than 0");
 	ContentResolver cr = this.mContext.getContentResolver();
	Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
	Uri delUri = Uri.withAppendedPath(uri, Integer.toString(firstBookId));
	reportString("Del Uri:" + delUri);
	cr.delete(delUri, null, null);
	this.reportString("Newcount:" + getCount());
}

private int getFirstBookId() {
	Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
	Activity a = (Activity)this.mContext;
	Cursor c = null;
	try	{
		c = a.getContentResolver().query(uri, 
				null, //projection
				null, //selection string
				null, //selection args array of strings
				null); //sort order
		int numberOfRecords = c.getCount();
		if (numberOfRecords == 0) {
			return -1;
		}
		c.moveToFirst();
		int id = c.getInt(1); //id column
		return id;
	}
	finally	{
		if (c!= null) c.close();
	}
}


*/