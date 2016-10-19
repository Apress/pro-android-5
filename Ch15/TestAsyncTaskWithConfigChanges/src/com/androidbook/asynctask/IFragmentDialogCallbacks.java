package com.androidbook.asynctask;

import android.app.DialogFragment;
import android.content.DialogInterface;

public interface IFragmentDialogCallbacks 
{
	public void onCancel(DialogFragment df, DialogInterface di);
	public void onDismiss(DialogFragment df, DialogInterface di);
}
