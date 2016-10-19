package com.androidbook.provider;

import android.content.Context;

public class BaseTester 
{
	protected IReportBack mReportTo;
	protected Context mContext;
	private String tag;
	public BaseTester(Context ctx, IReportBack target, String debugtag)
	{
		mReportTo = target;
		mContext = ctx;
		tag = debugtag;
	}
	protected void report(int stringid)
	{
		this.mReportTo.reportBack(tag,this.mContext.getString(stringid));
	}
	protected void reportString(String s)
	{
		this.mReportTo.reportBack(tag,s);
	}
	protected void reportString(String s, int stringid)
	{
		this.mReportTo.reportBack(tag,s);
		report(stringid);
	}
	
}
