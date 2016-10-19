package com.androidbook.provider.services;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.util.Log;

import com.androidbook.provider.services.impl.sqlite.BookPSSQLite;

/**
 * DBServicesProxyHandler
 * A class to externalize SQLite Transactions
 * It is a dynamic proxy.
 * @See Services.java to see how a reference to this is used
 * 
 * It is capable of hosting multiple interfaces
 * 
 * Each interface may represent persistence aspects of a 
 * particular entity or a domain object like a Book. 
 * Or the interface can be a composite interface dealing with
 * multiple entities.
 * 
 * It also uses ThreadLocals to pass the DatabaseContext
 * @See DatabaseContext
 * 
 * DatabaseContext provides the SQLiteDatabase reference to 
 * the implementation classes.
 * 
 * Related classes
 * ****************
 * Services.java : Client access to interfaces
 * IBookPS: Client interface to deal with persisting a Book
 * BookPSSQLite: SQLite Implementation of IBookPS
 * 
 * DBServicesProxyHandler: This class that is a dynamic proxu
 * DatabaseContext: Holds a db reference for BookPSSQlite implementation
 * DirectAccessBookDBHelper: Android DBHelper to construct the database
 *  
 */
public class DBServicesProxyHandler implements InvocationHandler
{
	private BookPSSQLite bookServiceImpl = new BookPSSQLite();
	private static String tag = "DBServicesProxyHandler";
	DBServicesProxyHandler(){}
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		logMethodSignature(method);
		String mname = method.getName();
		if (mname.startsWith("get")){
			return this.invokeForReads(method, args);
		}
		else {
			return this.invokeForWrites(method, args);
		}
	}
	private void logMethodSignature(Method method){
		String interfaceName = method.getDeclaringClass().getName();
		String mname = method.getName();
		Log.d(tag,String.format("%s : %s", interfaceName, mname));
	}
	
	private Object callDelegatedMethod(Method method, Object[] args)
	throws Throwable{
		return method.invoke(bookServiceImpl, args);
	}
	
	private Object invokeForReads(Method method, Object[] args) throws Throwable {
		if (DatabaseContext.isItAlreadyInsideATransaction() == true){
			//It is already bound
			return invokeForReadsWithoutATransactionalWrap(method, args);
		}
		else {
			//A new transaction
			return invokeForReadsWithATransactionalWrap(method, args);
		}
		
	}
	private Object invokeForReadsWithATransactionalWrap(Method method, Object[] args) 
	throws Throwable {
		try	{
			DatabaseContext.setReadableDatabaseContext();
			return callDelegatedMethod(method, args);
		}
		finally	{
			DatabaseContext.reset();
		}
	}
	private Object invokeForReadsWithoutATransactionalWrap(Method method, Object[] args) 
	throws Throwable {		
		return callDelegatedMethod(method, args);
	}
	private Object invokeForWrites(Method method, Object[] args) throws Throwable	{
		if (DatabaseContext.isItAlreadyInsideATransaction() == true) {
			//It is already bound
			return invokeForWritesWithoutATransactionalWrap(method, args);
		}
		else	{
			//A new transaction
			return invokeForWritesWithATransactionalWrap(method, args);
		}
	}
	private Object invokeForWritesWithATransactionalWrap(Method method, Object[] args) 
	throws Throwable	{
		try	{
			DatabaseContext.setWritableDatabaseContext();
			DatabaseContext.beginTransaction();
			Object rtnObject = callDelegatedMethod(method, args);
			DatabaseContext.setTransactionSuccessful();
			return rtnObject;
		}
		finally	{
			try {
				DatabaseContext.endTransaction();
			}
			finally {
				DatabaseContext.reset();
			}
		}
	}
	private Object invokeForWritesWithoutATransactionalWrap(Method method, Object[] args) 
	throws Throwable	{
		return callDelegatedMethod(method, args);
	}
}//eof-class
