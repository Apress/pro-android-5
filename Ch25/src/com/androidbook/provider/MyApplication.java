package com.androidbook.provider;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

public class MyApplication extends Application
{
	public final static String tag="MyApplication";
	public static volatile Context m_appContext = null;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(tag,"configuration changed");
	}

	@Override
	public void onCreate() {
		super.onCreate();
        MyApplication.m_appContext = this.getApplicationContext();
		Log.d(tag,"oncreate. Application context is available");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.d(tag,"onLowMemory");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(tag,"onTerminate");
	}

}
