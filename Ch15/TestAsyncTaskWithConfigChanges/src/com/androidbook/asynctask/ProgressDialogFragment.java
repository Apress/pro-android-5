package com.androidbook.asynctask;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * A DiglogFragment that encapsulates a ProgressDialog
 * This is not expected to be a retained fragment dialog. 
 * Gets recreated as activity rotates.
 */
public class ProgressDialogFragment extends DialogFragment  {
	private static String tag = "ProgressDialogFragment";
	ProgressDialog pd; //Will be set by onCreateDialog
	
	//This gets called from ADOs such as retained fragments
	//typically done when acitivty is attached back to the asynctask
	private IFragmentDialogCallbacks fdc;
	public void setDialogFragmentCallbacks(IFragmentDialogCallbacks infdc)
	{
		Log.d(tag, "attaching dialog callbacks");
		fdc = infdc;
	}
	
	//This is a default constructor. Called by the framework all the time
	//for re-introduction.
	public ProgressDialogFragment()	{
		//Should be safe for me to set cancelable as false;
		//wonder if that is carried through rebirth?
		this.setCancelable(false);
	}
	//One way for the client to attach in the begining
	//when the fragment is reborn. 
	//The reattachment is done through setFragmentDialogCallbacks
	//This is a shortcut. You may want to use the newInstance pattern
	public ProgressDialogFragment(IFragmentDialogCallbacks infdc)	{
		this.fdc = infdc;
		this.setCancelable(false);
	}
	/**
	 * This can get called multiple times each time the fragment is 
	 * recreated. So storing the pointer should be safe and it will 
	 * be a new one every time this fragment is recreated.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)	{
		Log.d(tag,"In onCreateDialog");
		pd = new ProgressDialog(getActivity());
		pd.setTitle("title");
		pd.setMessage("In Progress...");
		pd.setIndeterminate(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMax(15);
		return pd;
	}
	//Called when the dialog is dismissed.I should tell my corresponding task
	//to close or do the right thing!
	@Override
	public void onDismiss(DialogInterface dialog)	{
		super.onDismiss(dialog);
		Log.d(tag,"Dialog dismissed");
		if (fdc != null)		{
			fdc.onDismiss(this, dialog);
		}
	}
	@Override
	public void onCancel(DialogInterface dialog)	{
		super.onDismiss(dialog);
		Log.d(tag,"Dialog cancelled");
		if (fdc != null)	{
			fdc.onCancel(this, dialog);
		}
	}
	//will be called by a client
	public void setProgress(int value)	{
		pd.setProgress(value);
	}
}
