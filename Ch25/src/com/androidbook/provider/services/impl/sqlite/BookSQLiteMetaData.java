package com.androidbook.provider.services.impl.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.androidbook.provider.services.BaseEntity;
import com.androidbook.provider.services.Book;

/**
 * Implements the abstract contract required by AObjectMetaData
 * Extends BaseEntitySQLLiteMetaData with the fields of a book 
 * Defines the column names as they are known in the sqlite database
 * for a Book.java
 * Knows how to read values from a Book object
 * Knows how to write values to a Book object
 *  
 * @See BaseEntitySQLLiteMetaData 
 * @See BookSQLiteMetaData
 * @See AObjectMetadata
 * @See BaseEntity
 *
 */
public class BookSQLiteMetaData extends BaseEntitySQLiteMetaData 
{
	static public final String TABLE_NAME = "t_books";
	static public final String NAME = "name"; 
	static public final String AUTHOR = "author";
	static public final String ISBN = "isbn";
	protected void populateYourColumnNames(List<String> colNameList)
	{
		super.populateYourColumnNames(colNameList);
		colNameList.add(NAME);
		colNameList.add(AUTHOR);
		colNameList.add(ISBN);
	}
	
	public static BookSQLiteMetaData s_self = new BookSQLiteMetaData();
	
	protected void fillFields(Cursor c, BaseEntity be)
	{
		super.fillFields(c,be);
		Book b = (Book)be;
		b.setName(c.getString(c.getColumnIndex(NAME)));
		b.setAuthor(c.getString(c.getColumnIndex(AUTHOR)));
		b.setIsbn(c.getString(c.getColumnIndex(ISBN)));
	}
	public void fillUpdataleColumnValues(ContentValues cv, Book book)
	{
		super.fillUpdatableColumnValues(cv, book);
		fillMyColumnValues(cv, book);
	}
	public void fillAllColumnValues(ContentValues cv, Book book)
	{
		super.fillAllColumnValues(cv, book);
		fillMyColumnValues(cv, book);
	}
	private void fillMyColumnValues(ContentValues cv, Book book)
	{
		cv.put(BookSQLiteMetaData.NAME, book.getName());
		cv.put(BookSQLiteMetaData.ISBN, book.getIsbn());
		cv.put(BookSQLiteMetaData.AUTHOR, book.getAuthor());
	}
}
