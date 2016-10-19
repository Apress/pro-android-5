package com.androidbook.asynctask;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/*
 * To test an asynctask with an embedded progress bar
 * Uses retained fragments as a solution to get to the activity
 * 
 * Key Approach:
 * 
 * 1. Use a retained fragment to connect the activity to the async task
 * 2. use a progressbar to show the progress
 * 3. Control the progressbar as much as possible from the asynctask
 * 4. Takeover the view state of the progressbar here
 * 5. Because the activity is not doing a good job progressbar state
 * 6. Remove the asynctask from the retained fragment once the task
 *    finishes.
 * 7. See MyLongTaskWithFragments to see how the samething is
 *    done using dialogs instead of progressbars
 * 8. Cancels the task on an activity back  
 * 
 * Key testcases:
 * ****************************
 * 1. Regular orientation (Do it 3 times) (success)
 * 2. Flip in the middle (success)
 * 3. Flip and back (success)
 * 4. home and back during
 * 5. home and back after finishing
 * 6. Phone call hiding the activity
 * 7. prevent a back during the task Or cancel task on back 
 * 9. Go back before the task
 * 10. Go back during the task
 * 11. Go back after the task
 * 
 * Key secondary test cases
 * **************************************
 * 1. Cleanup the self pointer (success)
 * 2  Vestigial Testing: Retest the menu (Subsequent invocations may show vestiges)
 * 3. Flip back after finish a few times back and forth (Success)
 *    
 * 1. Regular orientation (Do it 3 times)
 * **************************************
 * See the progress bar
 * See the progress
 * Finish the progress
 * Regain the space
 * Asynctask pointer removed
 * No exceptions in log file
 * 
 * Redo it 3 times
 * 
 * 2. Flip in the middle
 * ***************************************
 * See the progress bar
 * See the progress
 * Flip
 * Pick up the progress where left off
 * Finish the progress
 * Regain the space
 * Asynctask pointer removed
 * No exceptions in log file
 * 
 * Rerun in the new orientation
 * Finish well
 * Regain space
 * No exceptions
 *
 * 3. Flip and back
 * *******************
 * See the progress bar
 * See the progress
 * Flip
 * Pick up the progress where left off
 * Flip again
 * Pick up the progress where left off
 * Finish in the original orientation
 * 
 * Asynctask pointer removed
 * No exceptions in log file
 * 
 * 4. home and back during
 * ***************************
 * See the progress bar
 * See the progress
 * Go home
 * async should be proceeding
 * progress should be proceeding in the background
 * Revisit the app
 * Pick up the progress where left off
 * Finish the progress
 * Regain the space
 * Asynctask pointer removed
 * No exceptions in log file
 * Retest in the same orientation if it works still
 *   
 * 5. home and back after finishing
 * *************************************
 * See the progress bar
 * See the progress
 * Go home
 * async should be proceeding
 * progress should be proceeding in the background
 * wait long enough for the task to finish
 * 2) possibilities
 * 1 is finishes and removes itself
 * 2 is finishes but waits for the restart
 * 
 * I think I used approach 2
 * 
 * Revisit the activtity
 * bar closed
 * async pointer reclaimed
 * 
 * 9. Go back before the task
 * ***************************** 
 * Go back without invoking the activity
 * Make sure nothing is broken
 * Check debug messages
 * we do this because the plumbing may effect initial conditions
 * eventhough the task hasn't started.
 * 
 * 10. Go back during the task
 * ***************************** 
 * See the progress bar
 * See the progress
 * Go back
 * Should see in debug that the task is cancelled
 * The task pointer is freed
 * You can revisit the activity
 * reinvoke the task
 * Complete task successfully
 * 
 * 11. Go back during the task
 * ***************************** 
 * See the progress bar
 * See the progress
 * wait the progressbar to close
 * The task pointer is freed
 * Go back
 * Should see in debug that the right close messages are observed
 * You can revisit the activity
 * reinvoke the task
 * Complete task successfully
 * 
 */
public class MyLongTaskWithProgressBar 
extends AsyncTask<String,Integer,Integer>
implements IWorkerObject
{
	//Debug tag
	public String tag = null;
	//Reference to the retained fragment 
	private MonitoredFragment retainedFragment;
	//To track current progress
	int curProgress = 0;
	
	//To report back errors
	//Represents the BaseTester
	//This should track the activity status
	//and likely a RADO itself
	private IReportBack r;
	
	
	MyLongTaskWithProgressBar(MonitoredFragment parentFragment
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
		//show the dialog
		showProgressBar();
	}

	/*
	 * I am going to start the asynctask
	 * show the progress bar
	 * we assume the activity is available here
	 */
	private void showProgressBar()	{
		Activity act = retainedFragment.getActivity();
		//assume activity is available
		ProgressBar pb = (ProgressBar) act.findViewById(R.id.tpb_progressBar1);
		pb.setProgress(0);
		pb.setMax(15);
		pb.setVisibility(View.VISIBLE);
	}
	
	
	//this can be null if the activity is not available
	private ProgressBar getProgressBar()
	{
		Activity act = retainedFragment.getActivity();
		if (act == null)
		{
			Log.d(tag, "activity is null. shouldnt be. returning a null dialog");
			return null;
		}
		//Good dialog
		return (ProgressBar) act.findViewById(R.id.tpb_progressBar1);
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
    		setProgressOnProgressBar(i);
    	}
    	else
    	{
    		Log.d(tag,"Activity is not ready! Progress update not displayed:" + i);
    	}
    }

    private void setProgressOnProgressBar(int i)
    {
    	this.curProgress = i;
    	ProgressBar pbar = getProgressBar();
    	if (pbar == null)
    	{
    		Log.d(tag, "Activity is not available to set progress");
    		return;
    	}
    	r.reportBack(tag, "pbar:" + pbar.getProgress());
    	pbar.setProgress(i);
    	
    }
    private void closeProgressBar()
    {
    	ProgressBar pbar = getProgressBar();
    	if (pbar == null)
    	{
    		Log.d(tag, "Sorry progress bar is null to close it!");
    		return;
    	}
    	//Dismiss the dialog
    	Log.d(tag,"Progress bar is being dismissed");
    	pbar.setVisibility(View.GONE);
    	detachFromParent();
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
			closeProgressBar();
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
		//dismiss dialog if needed
		if (bDoneFlag == true)		{
			Log.d(tag,"On my start I notice I was done earlier");
			closeProgressBar();
			return;
		}
		Log.d(tag,"I am reattached. I am not done");
		setProgressBarRightOnReattach();
	}
	private void setProgressBarRightOnReattach()	{
		ProgressBar pb = getProgressBar();
		pb.setMax(15);
		pb.setProgress(curProgress);
		pb.setVisibility(View.VISIBLE);
	}
	//To tell the called that I have finished
	//The Activity or retained fragment act as a client
	//to this asynctask
	IWorkerObjectClient client = null;
	int workerObjectPassbackIdentifier = -1;
	public void registerClient(IWorkerObjectClient woc,
			int inWorkerObjectPassbackIdentifier) {
		Log.d(tag,"Registering a client for the asynctask");
		client = woc;
		this.workerObjectPassbackIdentifier = inWorkerObjectPassbackIdentifier;
	}
	public void releaseResources()	{
		//Cancel the task
		Log.d(tag,"Cancelling the task");
		cancel(true);
		//Remove myself
		detachFromParent();
	}
	private void detachFromParent()	{
		if (client == null)		{
			Log.e(tag,"You have failed to register a client.");
			return;
		}
		//client is available
		client.done(this,workerObjectPassbackIdentifier);
	}

}//eof-class
