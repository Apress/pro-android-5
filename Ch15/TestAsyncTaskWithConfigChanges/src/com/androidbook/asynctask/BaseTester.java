package com.androidbook.asynctask;

import java.lang.ref.WeakReference;

import android.content.Context;

public class BaseTester 
{
	protected WeakReference<IReportBack> mReportTo;
	protected WeakReference<Context> mContext;
	//protected IReportBack mReportTo;
	//protected Context mContext;
	public BaseTester(Context ctx, IReportBack target)
	{
		mReportTo = new WeakReference<IReportBack>(target);
		mContext = new WeakReference<Context>(ctx);
	}
	
	protected void resetActivityInAsyncTester(Context inCtx, IReportBack inR)
	{
		mReportTo = new WeakReference<IReportBack>(inR);
		mContext = new WeakReference<Context>(inCtx);
	}
}
