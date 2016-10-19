package com.androidbook.asynctask;
import java.lang.ref.WeakReference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

/*
 * To test a progress dialog on flip
 * first make the activity references weak
 * see if the dialog is preserved on flip
 */
public class MyLongTask2 
extends AsyncTask<String,Integer,Integer>
implements OnCancelListener
{
	public String tag = null;
	
	WeakReference<IReportBack> r;
	WeakReference<Context> ctx;
	WeakReference<ProgressDialog> wpd = null;
	boolean bFinish = false;
	boolean bStart = false;
	
	MyLongTask2(IReportBack inr, Context inCtx, String inTag)
	{
		r = new WeakReference<IReportBack>(inr);
		ctx = new WeakReference<Context>(inCtx);
		tag = inTag;
	}
	protected void onPreExecute()
	{
		bStart = true;
		//Runs on the main ui thread
		Utils.logThreadSignature(this.tag);
		wpd = new WeakReference<ProgressDialog>(createProgressDialog());
	}
	
	private ProgressDialog createProgressDialog()
	{
		ProgressDialog pd = new ProgressDialog(ctx.get());
		pd.setTitle("title");
		pd.setMessage("In Progress...");
		pd.setCancelable(true);
		pd.setOnCancelListener(this);
		pd.setIndeterminate(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMax(5);
		return pd;
	}
    protected void onProgressUpdate(Integer... progress) 
    {
		//Runs on the main ui thread
		Utils.logThreadSignature(this.tag);
		this.reportThreadSignature();
		
		//will be called multiple times
		//triggered by onPostExecute
    	Integer i = progress[0];
    	
    	if (isMyActivityAvailable())
    	{
    		r.get().reportBack(tag, "Progress:" + i.toString());
    		//Activity is available
    		//Dialog is needed but may not be created
    		if (wpd.get() == null)
    		{
    			//Dialog is not there
    			//create it
    			Log.d(tag,"Creating a dialog during update");
    			wpd = new WeakReference<ProgressDialog>(createProgressDialog());
    		}
    		wpd.get().setProgress(i);
    	}
    	else
    	{
    		Log.d(tag,"Progress update not displayed:" + i);
    	}
    }
    
    private boolean isMyActivityAvailable()
    {
    	if (ctx.get() == null)
    	{
    		Log.d(tag,"Activity is null");
    		return false;
    	}
    	return true;
    }
    //should be called when the activity rotates and comes back
    public void resetActivity(Context inCtx, IReportBack inr)
    {
		r = new WeakReference<IReportBack>(inr);
		ctx = new WeakReference<Context>(inCtx);
		if (doINeedADialogOnRestart() == true)
		{
			Log.d(tag,"Creating a dialog during restart");
			wpd = new WeakReference<ProgressDialog>(createProgressDialog());
		}
    }
    
    private boolean doINeedADialogOnRestart()
    {
    	if ((bStart == true) && (bFinish == false))
    	{
    		return true;
    	}
    	return false;
    }
    protected void onPostExecute(Integer result) 
    {         
		//Runs on the main ui thread
		Utils.logThreadSignature(this.tag);
		conditionalReportBack("onPostExecute result:" + result);
    	if (isMyActivityAvailable())
    	{
    	    wpd.get().cancel();
    	}
    }
    protected Integer doInBackground(String...strings)
    {
		//Runs on a worker thread
    	//May even be a pool if there are 
    	//more tasks.
		Utils.logThreadSignature(this.tag);
		
    	for(String s :strings)
    	{
    		Log.d(tag, "Processing:" + s);
    		//r.reportTransient(tag, "Processing:" + s);
    	}
    	for (int i=0;i<5;i++)
    	{
    		Utils.sleepForInSecs(2);
    		publishProgress(i);
    	}
    	return 1;
    }
    protected void reportThreadSignature()
    {
    	String s = Utils.getThreadSignature();
   		conditionalReportBack(s);
    }
    public void onCancel(DialogInterface d)
    {
  		conditionalReportBack("Cancel Called");
    	this.cancel(true);
    }
    private void conditionalReportBack(String message)
    {
    	Log.d(tag,message);
    	if (isMyActivityAvailable())
    	{
    		r.get().reportBack(tag,"Cancel Called");
    	}
    }
}
