package com.androidbook.provider.services;

import android.database.sqlite.SQLiteDatabase;

/**
 * Polymorphic implementation of a write database.
 * Redefines what it means to apply transactions.
 * 
 * @See DatabaseContext for documentation
 */
public class WriteDatabaseContext extends DatabaseContext
{
	public WriteDatabaseContext(SQLiteDatabase db)
	{
		super(db);
	}	//protected functions
	@Override
	protected void internalBeginTransaction(){
		db.beginTransaction();
	}
	@Override
	protected void internalSetTransactionSuccessful(){
		db.setTransactionSuccessful();
	}
	@Override
	protected void internalEndTransaction() {
		db.endTransaction();
	}	
}
