package com.androidbook.asynctask;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

/*
 * To test a progress dialog on flip
 * first make the activity references weak
 * see if the dialog is preserved on flip
 * 
 * Key notes:
 * 1. I am a RADO
 * 2. I have pointers to other RADO to which I am not a parent
 * 3. I have no child RADOs 
 */
public class MyLongTaskWithRADO 
extends AsyncTask<String,Integer,Integer>
implements IRetainedADO, IFragmentDialogCallbacks
{
	public String tag = null;
	public static String PROGRESS_DIALOG_FRAGMENT_TAG_NAME="AsyncDialog";

	//To support multiple inheritance
	//Transfer all calls to the implementation
	private IRetainedADO retainedADOImpl;
	
	//To report back errors
	//Represents the BaseTester
	//This should track the activity status
	//and likely a RADO itself
	private IReportBack r;
	
	MyLongTaskWithRADO(IRetainedADO parentRADO
			,IReportBack inr
			, String inTag)
	{
		tag = inTag;
		retainedADOImpl = new RetainedADO(parentRADO, inTag, this);
		r = inr;
	}
	
	//Gets executed in response to task.execute()
	protected void onPreExecute()	{
		//Runs on the main ui thread
		Utils.logThreadSignature(this.tag);
		//show the dialog
		showDialogFragment();
	}

	/*
	 * I am going to start the asynctask
	 * show the dialog fragment.
	 * Name the fragment.
	 */
	private void showDialogFragment()	{
		Activity act = this.getActivity();
		ProgressDialogFragment pdf = new ProgressDialogFragment();
		pdf.show(act.getFragmentManager(), this.PROGRESS_DIALOG_FRAGMENT_TAG_NAME);
	}
	
	//this can be null
	private ProgressDialogFragment getDialog()	{
		Activity act = getActivity();
		if (act == null)		{
			Log.d(tag, "activity is null. shouldnt be. returning a null dialog");
			return null;
		}
		//Good dialog
		return (ProgressDialogFragment)
		    act.getFragmentManager()
		       .findFragmentByTag(this.PROGRESS_DIALOG_FRAGMENT_TAG_NAME);
	}
	
    protected void onProgressUpdate(Integer... progress)   {
		//Runs on the main ui thread
		Utils.logThreadSignature(this.tag);
		this.reportThreadSignature();
		
		//will be called multiple times
		//triggered by publishProgress
    	Integer i = progress[0];
    	
    	//Because this is called on the main thread
    	//The following check may not be needed
    	//if the activity is being re created
    	//I could assume this method is delayed
    	//until the activity is ready to receive
    	//its messages.
    	if (isActivityReady())    	{
    		r.reportBack(tag, "Progress:" + i.toString());
    		//set progress
    		//wpd.get().setProgress(i);
    		setProgressOnProgressDialog(i);
    	}
    	else  	{
    		Log.d(tag,"Activity is not ready! Progress update not displayed:" + i);
    	}
    }

    private void setCallbacksOnProgressDialog()    {
    	ProgressDialogFragment dialog = getDialog();
    	if (dialog == null)    	{
    		Log.d(tag, "Dialog is not available to set callbacks");
    		return;
    	}
    	dialog.setDialogFragmentCallbacks(this);
    }
    
    private void setProgressOnProgressDialog(int i)    {
    	ProgressDialogFragment dialog = getDialog();
    	if (dialog == null)    	{
    		Log.d(tag, "Dialog is not available to set progress");
    		return;
    	}
    	dialog.setProgress(i);
    }
    private void closeProgressDialog()    {
    	DialogFragment dialog = getDialog();
    	if (dialog == null)    	{
    		Log.d(tag, "Sorry dialog fragment is null to close it!");
    		return;
    	}
    	//Dismiss the dialog
    	Log.d(tag,"Dialog is being dismissed");
    	dialog.dismiss();
    }
    
    //when you start say you are not done
    //Mark it done on postExecute
    private boolean bDoneFlag = false;
    protected void onPostExecute(Integer result) 
    {         
		//Rember it to fix it when coming back up
		bDoneFlag = true;
		
		//Runs on the main ui thread
		Utils.logThreadSignature(this.tag);
		conditionalReportBack("onPostExecute result:" + result);
		
		//we need to close the dialog. However....
		//closeProgressDialog()
		
		//Conditions I ahve to take into account
		//1. activity not there
		//2. Activity is there but stopped
		//3. Activity is UI ready
		if (isUIReady())
		{
			Log.d(tag,"UI is ready and running. dismiss is pemissible");
			closeProgressDialog();
			return;
		}
		//either not there or stopped
		Log.d(tag,"UI is not ready. Do this on start again");
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
    	for (int i=0;i<15;i++)
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
    	r.reportBack(tag,message);
    }
    
    //*****************************************************
    //Redirect work for multiple inheritance
    //*****************************************************
	public MonitoredActivityWithADOSupport getActivity() {
		// TODO Auto-generated method stub
		return retainedADOImpl.getActivity();
	}
	public void reset() {
		retainedADOImpl.reset();
	}
	public void attach(MonitoredActivityWithADOSupport act) {
		retainedADOImpl.attach(act);
		//set this object to respond to callbacks
		setCallbacksOnProgressDialog();
		
		//dismiss dialog if needed
		if (bDoneFlag == true)		{
			Log.d(tag,"On my start I notice I was done earlier");
			closeProgressDialog();
		}
	}
	public void releaseContracts() {
		retainedADOImpl.releaseContracts();
	}
	public boolean isActivityReady() {
		return retainedADOImpl.isActivityReady();
	}
	public void addChildRetainedADO(IRetainedADO childRetainedADO) {
		retainedADOImpl.addChildRetainedADO(childRetainedADO);
	}
	public boolean isUIReady() {
		return retainedADOImpl.isUIReady();
	}
	public boolean isConfigurationChanging()	{
		return retainedADOImpl.isConfigurationChanging();
	}
	/**
	 * You may need to remove this worker ADO once its job is done
	 * @param childRetainedADO
	 */
	public void removeChildRetainedADO(IRetainedADO childRetainedADO)
	{
		retainedADOImpl.removeChildRetainedADO(childRetainedADO);
	}
	/**
	 * Not sure when you need to call this
	 */
	public void removeAllChildRetainedADOs()	{
		retainedADOImpl.removeAllChildRetainedADOs();
	}
	
	/**
	 * To support Named ADOs. These are useful
	 * for repeated menu commands.
	 *  
	 * @return The name of this ADO if available or null otherwise
	 */
	public String getName()	{
		return retainedADOImpl.getName();
	}
	public void detachFromParent()	{
		retainedADOImpl.detachFromParent();
	}
	public void logStatus()	{
		retainedADOImpl.logStatus();
	}
	public void addChildRetainedADOOnly(IRetainedADO childRetainedADO)	{
		retainedADOImpl.addChildRetainedADOOnly(childRetainedADO);
	}
    //*****************************************************
    //DialogFragment callback methods
    //*****************************************************
	public void onCancel(DialogFragment df, DialogInterface di) 	{
		Log.d(tag,"onCancel called");
	}

	public void onDismiss(DialogFragment df, DialogInterface di) 	{
		// TODO Auto-generated method stub
		Log.d(tag,"onDismiss called");
		Log.d(tag,"Remove myself from my parent");
		detachFromParent();
	}
}//eof-class
