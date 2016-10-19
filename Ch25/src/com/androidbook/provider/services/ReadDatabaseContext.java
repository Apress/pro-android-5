package com.androidbook.provider.services;

import android.database.sqlite.SQLiteDatabase;

/**
 * Polymorphic implementation of a read database.
 * Assumes nothing to be done in terms of transactions.
 * So leaves those methods un implemented 
 *
 */
public class ReadDatabaseContext extends DatabaseContext
{
	public ReadDatabaseContext(SQLiteDatabase db)
	{
		super(db);
	}
}
