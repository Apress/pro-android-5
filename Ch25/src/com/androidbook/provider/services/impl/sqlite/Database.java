package com.androidbook.provider.services.impl.sqlite;

import android.util.Log;

import com.androidbook.provider.services.DatabaseContext;

/**
 * In android a DBHelper is a standby object for a real database.
 * There is a unique instance of this object somewhere in the process.
 * In our case the DirectAccessBookDBHelper is not only one such database,
 * but also holds to a static pointer of that object.
 * 
 * This class Database is a static utility class that knows how to 
 * initialize all database related objects from one place. 
 *  
 * The database is initialized when the first method of a Service is called. 
 * The initialize() method below is called from the Service.java class
 * as part of that class's static initialization.
 * 
 */
public class Database 
{
	private static final String tag = "Database"; 
	public static void initialize()
	{
		DatabaseContext.initialize(DirectAccessBookDBHelper.m_self);
		Log.d(tag, "Database initialized");
	}
	public static DirectAccessBookDBHelper getDBHelper()
	{
		return DirectAccessBookDBHelper.m_self;
	}
}
