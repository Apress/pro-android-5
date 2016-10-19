package com.androidbook.provider.services;

/**
 * A test harness to test dynamic proxy with out android 
 * I have copied these files and tested in a plain java project. 
 * Once tested, this class is not needed and nor used.
 */
public class TestProxies {

	public static void main(String[] args) 
	{
		System.out.println("begin");
		Services.init();
		//Services.PersistenceServices.bookps.getAllBooks();
		System.out.println("end");
	}

}
