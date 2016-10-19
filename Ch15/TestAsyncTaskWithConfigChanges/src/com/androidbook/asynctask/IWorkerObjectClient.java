package com.androidbook.asynctask;

/**
 * 
 * works in conjunction with IWorkerObject
 * @author Satya
 *
 */
public interface IWorkerObjectClient {
	public void done(IWorkerObject wobj, int workerObjectPassthroughIdentifier);
}
