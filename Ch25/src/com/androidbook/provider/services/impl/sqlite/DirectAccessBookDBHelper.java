package com.androidbook.provider.services.impl.sqlite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.androidbook.provider.services.*;
import com.androidbook.provider.MyApplication;

/**
 * In android this is a standby class representing an SQLite database.
 * This class provides callbacks to
 * 1. create the database
 * 2. load it with data if new
 * 3. A callback to upgrade the database if the versions don't match
 * 
 * This class also supplies the read and wrtie databases to 
 * persistence services proxy so that the persistence services
 * can use the right database.
 * 
 * In this example you will also see how to load the create scripts
 * from an asset file and run them during the first time creation of the
 * database.
 *
 */
public class DirectAccessBookDBHelper extends SQLiteOpenHelper 
implements DatabaseContext.IFactory
{
	//there is one and only one of these database helpers
    public static DirectAccessBookDBHelper m_self = 
    	new DirectAccessBookDBHelper(MyApplication.m_appContext); 

    public static final String DATABASE_NAME = "booksqllite.db";
    public static final String CREATE_DATABASE_FILENAME = "create-book-db.sql";
    public static final int DATABASE_VERSION = 1;

    public static final String TAG = "DirectAccessBookDBHelper";
    
    DirectAccessBookDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        DatabaseContext.initialize(this);
    }
    @Override
    public void onCreate(SQLiteDatabase db)  {
    	try  	{
	    	Log.d(TAG,"Database is being created");
	    	//load it from assets
	    	loadSQLFrom(this.CREATE_DATABASE_FILENAME,db);
    	}
    	catch(Throwable t)    	{
    		Log.d(TAG,"Problem creating database",t);
    		throw new RuntimeException(t);
    	}
    }
    private void loadSQLFrom(String assetFilename, SQLiteDatabase db)    {
    	List<String> statements = getDDLStatementsFrom(assetFilename);
    	for(String stmt: statements){
    		Log.d(TAG,"Executing Statement:" + stmt);
    		db.execSQL(stmt);
    	}
    }
    //Optimize this function later
    //For now it assumes there are no comments in the file
    //the statements are separated by a semi colon
    private List<String> getDDLStatementsFrom(String assetFilename)  {
    	ArrayList<String> l = new ArrayList<String>();
    	String s = getStringFromAssetFile(assetFilename);
    	for (String stmt: s.split(";"))	{
    		//Add the stmt if it is a valid statement
    		if (isValid(stmt)) {
    			Log.d(TAG,"Adding:" + stmt);
    			l.add(stmt);
    		}
    	}
    	return l;
    }
    private boolean isValid(String s)    {
    	if (s == null) return false;
    	if (s.trim().equals("")) return false;
    	if (s.startsWith("//"))    	{
    		//this is a comment. discard
    		return false;
    	}
    	return true;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)   {
        Log.w(TAG, "Upgrading database from version " 
        		+ oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        //It can get hairy to do this
        //This gets called when you instantiate this class with a 
        //version number that is different
        //You may need to incrementally upgrade the database
        //using drop, alter etc
        //migrate the data etc
    }
    
    private String getStringFromAssetFile(String filename)   {
       Context ctx = MyApplication.m_appContext; 
       if ( ctx == null)   {
          throw new RuntimeException("Sorry your app context is null");
       }
       try   {
         AssetManager am = ctx.getAssets();
         InputStream is = am.open(filename);
         String s = convertStreamToString(is);
         is.close();
         return s;
       }
       catch (IOException x)    {
          throw new RuntimeException("Sorry not able to read filename:" + filename,x);
       }
    }
    //Optimize later. This may not be an efficient read
    private String convertStreamToString(InputStream is)  throws IOException   {   
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       int i = is.read();
       while (i != -1)  {
          baos.write(i);
          i = is.read();
       }
       return baos.toString();
    }
	public ReadDatabaseContext createReadableDatabase() {
		return new ReadDatabaseContext(this.getReadableDatabase());
	}
	public WriteDatabaseContext createWritableDatabase() {
		return new WriteDatabaseContext(this.getWritableDatabase());
	}    
}//eof-class DatabaseHelper
