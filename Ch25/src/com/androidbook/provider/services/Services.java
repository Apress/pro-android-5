package com.androidbook.provider.services;

import java.lang.reflect.Proxy;

import com.androidbook.provider.services.impl.sqlite.Database;

import android.util.Log;

/**
 * Allow a name space for clients to discover various services
 * Usage:
 *   Services.persistenceServices.bookps.addBook(); etc.
 * Dynamic proxy will take care of transactions.
 * Dynamic proxy will take care of mock data.
 * Dynamic Proxy will allow more than one interface 
 *   to apply the above aspects. 
 */
public class Services {
	public static String tag = "Services";
	public static class PersistenceServices	{
		////se this pointer during initialization
		public static IBookPS bookps = null;
		static {
			Services.init();
		}
	}
	//Although this method is empty, calling it
	//will trigger all static initialization code for this class
	public static void init() {}
	private static Object mainProxy;
	static 
	{
		//Initialize the database
		Database.initialize();
		
		//set up bookps
		ClassLoader cl = IBookPS.class.getClassLoader();
		//Add more interfaces as available
		Class[] variousServiceInterfaces = new Class[] { IBookPS.class };
		
		//Create a big object that can proxy all the related interfaces
		//for which similar common aspects are applied
		//In this cases it is android sqllite transactions
		mainProxy = 
			Proxy.newProxyInstance(cl, 
					variousServiceInterfaces, 
					new DBServicesProxyHandler());
		Log.d(Services.tag,"Main Services Proxy Initialized");
		//Preset the namespace for easy discovery
		PersistenceServices.bookps = (IBookPS)mainProxy;
	}
}
