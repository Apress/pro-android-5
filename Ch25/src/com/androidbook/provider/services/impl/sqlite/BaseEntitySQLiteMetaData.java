package com.androidbook.provider.services.impl.sqlite;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.androidbook.provider.services.BaseEntity;
import com.androidbook.provider.services.Book;


/**
 * Implements the abstract contract required by AObjectMetaData
 * Defines the column names as they are known in the sqlite database
 * for the most basic columns as defined by BaseEntity.java
 * Knows how to read values from a BaseEntity object
 * Knows how to write values to a BaseEntity object
 *  
 * @See BaseEntitySQLLiteMetaData 
 * @See BookSQLiteMetaData
 * @See AObjectMetadata
 * @See BaseEntity
 *
 */
public abstract class BaseEntitySQLiteMetaData extends AObjectMetadata 
{
	static public final String OWNED_ACCOUNT_COLNAME = "owned_account"; 
	static public final String CREATED_BY_COLNAME = "created_by";
	static public final String CREATED_ON_COLNAME = "created_on";
	static public final String LAST_UPDATED_ON = "last_updated_on";
	static public final String LAST_UPDATED_BY = "last_updated_by";
	static public final String ID_COLNAME = "id";
	
	protected void populateYourColumnNames(List<String> colNameList)
	{
		colNameList.add(CREATED_BY_COLNAME);
		colNameList.add(CREATED_ON_COLNAME);
		colNameList.add(ID_COLNAME);
		colNameList.add(LAST_UPDATED_BY);
		colNameList.add(LAST_UPDATED_ON);
	}
	
	protected void fillFields(Cursor c, BaseEntity be)
	{
		be.setId(c.getInt(c.getColumnIndex(ID_COLNAME)));
		
		//creation
		be.setCreatedBy(c.getString(c.getColumnIndex(CREATED_BY_COLNAME)));
		be.setCreatedOn(getDate(c.getLong(c.getColumnIndex(CREATED_ON_COLNAME))));
		
		//update
		be.setLastUpdatedBy(c.getString(c.getColumnIndex(LAST_UPDATED_BY)));
		be.setLastUpdatedOn(getDate(c.getLong(c.getColumnIndex(LAST_UPDATED_ON))));
	}
	
	protected void fillUpdatableColumnValues(ContentValues cv, Book book)
	{
		this.fillMyCommonColumnValues(cv, book);
	}
	
	protected void fillAllColumnValues(ContentValues cv, Book book)
	{
		//cv.put(ID_COLNAME, book.getId());
		cv.put(CREATED_BY_COLNAME, book.getCreatedBy());
		cv.put(CREATED_ON_COLNAME, fromDate(book.getCreatedOn()));
		this.fillMyCommonColumnValues(cv, book);
	}
	private void fillMyCommonColumnValues(ContentValues cv, Book book)
	{
		cv.put(LAST_UPDATED_BY, book.getLastUpdatedBy());
		cv.put(LAST_UPDATED_ON, fromDate(book.getLastUpdatedOn()));
	}
	private Date getDate(long datenumber)
	{
		return new Date(datenumber);
	}
	private long fromDate(Date date)
	{
		return date.getTime();
	}
}
