package com.androidbook.provider.services;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.androidbook.provider.services.impl.sqlite.BookPSSQLite;

/**
 * A test interceptor for multiple interfaces
 * Tests to see how to route a method call
 * to a suitable implementation
 * 
 *  Eventually will be used for Android SQLite 
 *  transaction support.
 *  
 */
public class TestInterceptor implements InvocationHandler
{
	private BookPSSQLite bookServiceImpl;
	TestInterceptor()
	{
		this.bookServiceImpl = new BookPSSQLite();
	}
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		logMethodSignature(method);
		return null;
	}
	
	private void logMethodSignature(Method method)
	{
		String interfaceName = method.getDeclaringClass().getName();
		String mname = method.getName();
		System.out.println(String.format("%s : %s", interfaceName, mname));
	}
}
