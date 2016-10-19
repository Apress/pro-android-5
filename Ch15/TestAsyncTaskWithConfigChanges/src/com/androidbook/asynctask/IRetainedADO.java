package com.androidbook.asynctask;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

/**
 * So what are its properties.
 * In addition to being an ADO it also implements the protocol of 
 * what it means to be retained.
 * 
 * See the RetainedADO class for more details
 * This is here to solve multiple-inheritance where previously
 * built Android SDK objects can behave like RetainedADOs.
 * Ex: An asynctask can be extended to behave like a RetainedADO
 * 
 * Implemented by: RetainedADO
 *  
 */
public interface IRetainedADO 
extends IActivityDependentObject
{
	public MonitoredActivityWithADOSupport getActivity();

	// Usually called on stop.
	// should set the activity to null
	// Call child ados reset
	public void reset();
	
	//Called when the activity is new
	//or reborn.
	//set the activity pointer
	//call any child ados
	public void attach(MonitoredActivityWithADOSupport act);
	
	//Called on destroy
	//Call children as well
	public void releaseContracts();
	
	//utility function
	//this will be true if activity is available
	//refactor later for additional activity statuses
	public boolean isActivityReady();
	
	//For this to be true
	//the activity must be there
	//the activity must not be stopped
	public boolean isUIReady();

	/**
	 * Redirected from the underlying activity
	 * @return true if the activity is being tagged for configuration change 
	 */
	public boolean isConfigurationChanging();

	/**
	 * Create a hierarchy of RADO objects
	 * @param childRetainedADO
	 */
	public void addChildRetainedADO(IRetainedADO childRetainedADO);

	/**
	 * Better method to call from a constructor.
	 * otherwise the previous method is invoking a virtual method
	 * @param childRetainedADO
	 */
	public void addChildRetainedADOOnly(IRetainedADO childRetainedADO);

	
	/**
	 * You may need to remove this worker ADO once its job is done
	 * @param childRetainedADO
	 */
	public void removeChildRetainedADO(IRetainedADO childRetainedADO);
	
	/**
	 * Not sure when you need to call this
	 */
	public void removeAllChildRetainedADOs();
	
	/**
	 * To support Named ADOs. These are useful
	 * for repeated menu commands.
	 *  
	 * @return The name of this ADO if available or null otherwise
	 */
	public String getName();
	
	/**
	 * Unregister from the parent
	 */
	public void detachFromParent();
	
	/**
	 * It is important to know overtime how the 
	 * RADO objects are accumulating
	 */
	public void logStatus();
	
}//eof-interface
