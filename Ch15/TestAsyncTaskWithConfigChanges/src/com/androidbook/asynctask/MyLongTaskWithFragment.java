package com.androidbook.asynctask;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

/*
 * To test a progress dialog on flip
 * Uses retained fragments as a solution.
 * 
 * Key Approach:
 * 1. This task wants access to the activity to report its progress
 * 2. But activity is not retained all the time
 * 3. But a fragment does
 * 4. So, the logic then says, it should hold a pointer to the 
 * retained fragment!
 * 
 */
public class MyLongTaskWithFragment 
extends AsyncTask<String,Integer,Integer>
implements IFragmentDialogCallbacks, IWorkerObject
{
	public String tag = null;
	public static String PROGRESS_DIALOG_FRAGMENT_TAG_NAME="AsyncDialog";

	private MonitoredFragment retainedFragment;
	
	//To report back errors
	//Represents the BaseTester
	//This should track the activity status
	//and likely a RADO itself
	private IReportBack r;
	
	MyLongTaskWithFragment(MonitoredFragment parentFragment
			,IReportBack inr
			, String inTag)
	{
		tag = inTag;
		retainedFragment = parentFragment;
		r = inr;
	}
	
	//Gets executed in response to task.execute()
	protected void onPreExecute()
	{
		//Runs on the main ui thread
		Utils.logThreadSignature(this.tag);
		Log.d(tag,"bDoneFlag:" + bDoneFlag);
		//bDoneFlag = false;
		//show the dialog
		showDialogFragment();
	}

	/*
	 * I am going to start the asynctask
	 * show the dialog fragment.
	 * Name the fragment.
	 */
	private void showDialogFragment()
	{
		Activity act = retainedFragment.getActivity();
		ProgressDialogFragment pdf = new ProgressDialogFragment();
		pdf.show(act.getFragmentManager(), this.PROGRESS_DIALOG_FRAGMENT_TAG_NAME);
	}
	
	//this can be null
	private ProgressDialogFragment getDialog()
	{
		Activity act = retainedFragment.getActivity();
		if (act == null)
		{
			Log.d(tag, "activity is null. shouldnt be. returning a null dialog");
			return null;
		}
		//Good dialog
		return (ProgressDialogFragment)
		    act.getFragmentManager()
		       .findFragmentByTag(this.PROGRESS_DIALOG_FRAGMENT_TAG_NAME);
	}
	
    protected void onProgressUpdate(Integer... progress) 
    {
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
    	if (retainedFragment.isUIReady())
    	{
    		r.reportBack(tag, "Progress:" + i.toString());
    		//set progress
    		//wpd.get().setProgress(i);
    		setProgressOnProgressDialog(i);
    	}
    	else
    	{
    		Log.d(tag,"Activity is not ready! Progress update not displayed:" + i);
    	}
    }

    private void setCallbacksOnProgressDialog()
    {
    	ProgressDialogFragment dialog = getDialog();
    	if (dialog == null)
    	{
    		Log.d(tag, "Dialog is not available to set callbacks");
    		return;
    	}
    	dialog.setDialogFragmentCallbacks(this);
    }
    
    private void setProgressOnProgressDialog(int i)
    {
    	ProgressDialogFragment dialog = getDialog();
    	if (dialog == null)
    	{
    		Log.d(tag, "Dialog is not available to set progress");
    		return;
    	}
    	dialog.setProgress(i);
    }
    private void closeProgressDialog()
    {
    	DialogFragment dialog = getDialog();
    	if (dialog == null)
    	{
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
		if (retainedFragment.isUIReady())
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
    private void conditionalReportBack(String message)
    {
    	Log.d(tag,message);
    	r.reportBack(tag,message);
    }
    
    /**
     * Call this from your onStart
     * @param act
     */
	public void onStart(Activity act) {
		setCallbacksOnProgressDialog();
		
		//dismiss dialog if needed
		if (bDoneFlag == true)
		{
			Log.d(tag,"On my start I notice I was done earlier");
			closeProgressDialog();
		}
	}
    
    
    //*****************************************************
    //DialogFragment callback methods
    //*****************************************************
	public void onCancel(DialogFragment df, DialogInterface di) 
	{
		Log.d(tag,"onCancel called");
	}

	public void onDismiss(DialogFragment df, DialogInterface di) 
	{
		// TODO Auto-generated method stub
		Log.d(tag,"onDismiss called");
		
		//Nothing to do here
		//This object is not held by anyone other than the async task manager
		//that should be release on postexecute anyway
		
		Log.d(tag,"Remove myself from my parent");
		detachFromParent();
	}

	private void detachFromParent()
	{
		if (client == null)
		{
			Log.e(tag,"You have failed to register a client.");
			return;
		}
		//client is available
		client.done(this,workerObjectPassbackIdentifier);
	}
	//To tell the called that I have finished
	IWorkerObjectClient client = null;
	int workerObjectPassbackIdentifier = -1;
	
	public void registerClient(IWorkerObjectClient woc,
			int inWorkerObjectPassbackIdentifier) {
		Log.d(tag,"Registering a client for the asynctask");
		client = woc;
		this.workerObjectPassbackIdentifier = inWorkerObjectPassbackIdentifier;
	}
	public void releaseResources()
	{
		//Cancel the task
		Log.d(tag,"Cancelling the task called");
		cancel(true);
		//Remove myself
		detachFromParent();
	}
}//eof-class
