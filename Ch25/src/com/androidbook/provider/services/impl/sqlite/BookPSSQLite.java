package com.androidbook.provider.services.impl.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.androidbook.provider.services.Book;
import com.androidbook.provider.services.IBookPS;

/**
 * Implements the interface stipulated b IBookPS
 * Use ASQLitePS for helper methods to get access to the database
 *
 * This is the class that finally calls the direct API provided by
 * Android in terms of SQLIteDatabase. It also relies on the 
 * metadata classes to map relational data to and from objects
 * 
 * This class is incredibly simple for the reasons
 * 
 * 1. Transactions are handled by the dynamic proxy. This means
 * individual methods don't have to write that code at all.
 * 2. Mapping is handled by respective metadata classes
 * 
 */
public class BookPSSQLite extends ASQLitePS 
implements IBookPS 
{
	private static String tag = "BookPSSQLite";
	public int saveBook(Book book)
	{
		//get the database
		//id does not exist in the book object
		if (book.getId() == -1)
		{
			//id of the book doesn't exist
			//so create it
			return (int)createBook(book);
		}
		//id exists in book object
		updateBook(book);
		return book.getId();
	}
	public void deleteBook(int bookid){
		SQLiteDatabase db = getWriteDb();
		String tname = BookSQLiteMetaData.TABLE_NAME;
		String whereClause = 
			String.format("%s = %s;",
				BookSQLiteMetaData.ID_COLNAME,
				bookid);
		String[] whereClauseargs = null;
		int i = db.delete(tname,whereClause, whereClauseargs);
		if (i != 1)
		{
			throw new RuntimeException("The number of deleted books is not 1 but:" + i);
		}
	}
	private long createBook(Book book)
	{
		//book doesn't exist
		//create it
		SQLiteDatabase db = getWriteDb();
		
		ContentValues bcv = this.getBookAsContentValuesForCreate(book);
		
		//I don't need ot insert an empty row
		//usually any nullable column name goes here
		//if I want to insert an empty row
		String nullColumnNameHack = null;
		//Construct values from the Book object
		//SQLException is a runtime exception
        long rowId = db.insertOrThrow(BookSQLiteMetaData.TABLE_NAME, nullColumnNameHack, bcv);
        return rowId;
	}
	public void updateBook(Book book)
	{
		if (book.getId() < 0) {
			throw new SQLException("Book id is less than 0");
		}
		SQLiteDatabase db = getWriteDb();
		ContentValues bcv = this.getBookAsContentValuesForUpdate(book);
		String whereClause = String.format("%1 = %2",BookSQLiteMetaData.ID_COLNAME,book.getId());
		//Or
		String whereClause2 = "? = ?";
		String[] whereArgs = new String[2];
		whereArgs[0] = BookSQLiteMetaData.ID_COLNAME;
		whereArgs[1] = Integer.toString(book.getId());
		
		int count = db.update(BookSQLiteMetaData.TABLE_NAME, bcv, whereClause, null);
		if (count == 0)	{
			throw new SQLException(
					String.format("Failed ot update book for book id:%s",book.getId()));
		}
	}
    private ContentValues getBookAsContentValuesForUpdate(Book book)
    {
		ContentValues cv = new ContentValues();
		BookSQLiteMetaData.s_self.fillUpdatableColumnValues(cv, book);
		return cv;
    }
    private ContentValues getBookAsContentValuesForCreate(Book book)
    {
		ContentValues cv = new ContentValues();
		BookSQLiteMetaData.s_self.fillAllColumnValues(cv, book);
		return cv;
    }
	public List<Book> getAllBooks()
	{
		SQLiteDatabase db = getReadDb();
		String tname = BookSQLiteMetaData.TABLE_NAME;
		String[] colnames = BookSQLiteMetaData.s_self.getColumnNames();
		
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
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				Log.d(tag,"There are books");
				Book b = new Book();
				BookSQLiteMetaData.s_self.fillFields(c,b);
				bookList.add(b);
			}
			return bookList;
		}
		finally {
			if (c!= null) c.close();
		}
	}
	public Book getBook(int bookid) {
		SQLiteDatabase db = getReadDb();
		String tname = BookSQLiteMetaData.TABLE_NAME;
		String[] colnames = BookSQLiteMetaData.s_self.getColumnNames();
		
		//Selection
		String selection = 
			String.format("%s = %s", 
					BookSQLiteMetaData.ID_COLNAME,
					bookid);
		//all rows. Usually a where clause. exclude where part
		String[] selectionArgs = null; //use ?s if you need it
		
		String groupBy = null; //sql group by clause: exclude group by part
		String having = null; //similar
		String orderby = null;
		String limitClause = null; //max number of rows
		//db.query(tname, colnames)
		Cursor c = db.query(tname,colnames,selection,selectionArgs,groupBy,having,orderby,limitClause);
		
		try
		{
			if (c.isAfterLast())
			{
				Log.d(tag,"No rows for id" + bookid);
				return null;
			}
			Book b = new Book();
			BookSQLiteMetaData.s_self.fillFields(c, b);
			return b;
		}
		finally {
			c.close();
		}
	}
}//eof-class
