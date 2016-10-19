package com.androidbook.provider.services.impl.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androidbook.provider.services.DatabaseContext;

/**
 * Provides some utility methods to derived implementation
 * of persistence services.
 * 
 * it is made abstract to prevent direct instantiation.
 * provides database instances for derived classes using 
 * simple method signatures.
 *
 */
public abstract class ASQLitePS 
{
	/**
	 * Give me the current database on the thread
	 * it can be read or write database.
	 * Transactions are automatically handled
	 * @return SQLiteDatabase
	 */
	protected SQLiteDatabase getDb()
	{
		return DatabaseContext.getDb();
	}
	/**
	 * Use these if you need to.
	 * Use getDb() instead as that returns DB on the current thread
	 * @return SQLiteDatabase
	 */
	protected SQLiteDatabase getWriteDb()
	{
		return getDb();
	}
	/**
	 * Use these if you need to.
	 * Use getDb() instead as that returns DB on the current thread
	 * @return SQLiteDatabase
	 */
	protected SQLiteDatabase getReadDb()
	{
		return getDb();
	}
}
