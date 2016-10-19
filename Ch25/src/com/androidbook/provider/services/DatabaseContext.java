package com.androidbook.provider.services;

import android.database.sqlite.SQLiteDatabase;

/**
 * This is a hybrid class with 3 behaviors: 1)instance 2)static factory 3) thereadlocal
 * It is specific to sqlite. May be it should be in that package!
 * 
 * General Idea
 * ****************
 * Allows any client an easy path to establish and make calls on a polymorphic database 
 * object that is local to a thread.
 * Static methods are provided on this class
 * to get to the threadlocal instances of the database that is on this thread.
 * 
 * This allows a dynamic proxy to control transactions on a local thread
 * without explicitly passing the database reference to all the calls.
 * 
 * Calling approach from a client
 * *******************************
 * beginning of thread:
 * DatabaseContext.setWritableDatabaseContext();
 * 
 * later:
 * DatabaseContext dbc = DatabaseContext.getDatabaseContext();
 * dbc.begin, cancel, or commit transaction
 * 
 * end of thread or call:
 * DatabaseContext.reset();
 * 
 * 
 * Inheritance:
 * **********************************
 * DatabaseContext
 *   ReadDatabaseContext
 *   WriteDatabaseContext
 *
 * What is its non-static behavior?
 * **********************************
 * It is an SQLiteDatabase that can be a read or write
 * Knows how to apply transactions accordingly
 * 
 * What is its static behavior?
 * **********************************
 * it has a static factory that can create a read or writable database instance
 * A caller uses this static interface to create these objects
 * 
 * It also has threadlocal behavior
 * *******************************
 * Translates the static calls to an the database object that is local to 
 * this thread.  
 * 
 * Related classes
 * **********************
 * DirectAccessBookDBHelper (Acts like a factory for database contexts)
 * ReadDatabaseContext
 * WriteDatabaseContext
 *   
 */
public class DatabaseContext {
	public enum ReadWriteType
	{
		Read, Write;
	}
	protected SQLiteDatabase db = null;
	
	//This method is used by a factory object
	//It should be private other than to the factory object
	public DatabaseContext(SQLiteDatabase db)
	{
		this.db = db;
	}
	//The following code is used initially to 
	//setup a factory
	public static interface IFactory
	{
		ReadDatabaseContext createReadableDatabase();
		WriteDatabaseContext createWritableDatabase();
	}
	public static ReadDatabaseContext createReadDatabaseContext()
	{
		return dbfactory.createReadableDatabase();
	}
	public static WriteDatabaseContext createWriteDatabaseContext()
	{
		return dbfactory.createWritableDatabase();
	}
	private static ThreadLocal<DatabaseContext> tl_DatabaseContext 
			= new ThreadLocal<DatabaseContext>();
	
	public static void setWritableDatabaseContext()
	{
		DatabaseContext dc = createWriteDatabaseContext();
		tl_DatabaseContext.set(dc);
	}
	public static void setReadableDatabaseContext()
	{
		DatabaseContext dc = createReadDatabaseContext();
		tl_DatabaseContext.set(dc);
	}
	public static DatabaseContext getCurrentDatabaseContext()
	{
		return (DatabaseContext)tl_DatabaseContext.get();
	}
	public static boolean isItAlreadyInsideATransaction()
	{
		DatabaseContext dc = getCurrentDatabaseContext();
		if (dc != null) return true;
		return false;
	}
	
	public static SQLiteDatabase getDb()
	{
		return getCurrentDatabaseContext().db;
	}
	public static void reset()
	{
		//you have to call this at the end of the thread
		//Makes sure you do this in the final
		tl_DatabaseContext.set(null);
	}
	public static void beginTransaction(){
		getCurrentDatabaseContext().internalBeginTransaction();
	}
	public static void setTransactionSuccessful(){
		getCurrentDatabaseContext().internalSetTransactionSuccessful();
	}
	public static void endTransaction() {
		getCurrentDatabaseContext().internalEndTransaction();
	}
	
	//protected functions
	//These are implemented by read and write database differently
	//Write database will have implementations
	//Read database will not implement them
	protected void internalBeginTransaction(){}
	protected void internalSetTransactionSuccessful(){}
	protected void internalEndTransaction() {}
	
	private static IFactory dbfactory = null;
	/**
	 * Used only once during the setup of the database.
	 * Called by the SQLiteHelper derived class during the 
	 * database setup. 
	 * @param factory
	 */
	public static void initialize(DatabaseContext.IFactory factory)
	{
		DatabaseContext.dbfactory = factory;
	}
}//eof-class
