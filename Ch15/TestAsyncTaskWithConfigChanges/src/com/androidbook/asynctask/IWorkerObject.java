package com.androidbook.asynctask;

import android.app.Activity;

/**
 * Meant for something like an asynctask
 * This can probably merged with IActivityDepedentObject
 * @author satya
 *
 */
public interface IWorkerObject {
	public void registerClient(IWorkerObjectClient woc, int workerObjectPassbackIdentifier);
	public void onStart(Activity act);
	public void releaseResources();
}
