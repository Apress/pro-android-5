package com.androidbook.asynctask;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

/**
 * So what are its properties.
 * 
 * 1. this object, like a fragment, can stick around.
 * 
 * 2. the activity will be reset to null on stop.
 * 
 * 3. this should have the ability to traverse its child
 * dependent objects
 * 
 * 4. This ado should exist as long as the activity is
 * around including its rebirth. But it should release
 * its contracts and resources when the activity is
 * no longer there and destroyed
 * 
 * 5. there is a big assumption that no one holds a pointer
 * to this object than the activity framework
 * 
 * 6. So when the activity is destroyed it is expected that
 * this ado becomes available for garbage collection
 * 
 * 7. To construct a retainedADO you need either an activity
 * or another RetainedADO
 * 
 * Some design choices
 * **********************
 * 1. May be there are 2 kinds of retained ADOs
 * 2. A singular RADO hosted by an activity
 * 3. And multiple RADOs that can be hoseted by other RADOs
 * 4. For now I will work with what I have and refine the model later
 */
public class RetainedADO 
extends DefaultADO
implements IRetainedADO
{
	private String mName = null;
	
	//these are child ADOs
	//This parent ADO should call life cycle methods
	//on the child ADOs
	private ArrayList<IRetainedADO> childRetainedADOs = 
		new ArrayList<IRetainedADO>();
	
	//To provide support for detachFromParent
	IRetainedADO parentRADO = null;
	
	//
	public RetainedADO(Activity parent, String tag)
	{
		super(tag);
		//The parent activity is used as a merely guideline
		//to force a constructor that requires it.
		//This avoids the mistakes of creating RetainedADO
		//without a parent!
		//So in short the activity parent is ignored here.
		//In the future i may consider using it
		//but if I do I need to keep track of the activity states
		//to answer if the activity is ready.
	}
	
	//Because I am a reetainedADO I need to 
	//attach myself to a parent so that parent can call me
	//usually this parent is the activity
	//however other retainedADOs can act as parents
	public RetainedADO(IRetainedADO parent, String tag)
	{
		super(tag);
		parent.addChildRetainedADO(this);
		parentRADO = parent;
	}
	
	/**
	 * This is used for multiple inheritance simulation needs.
	 * Parent registration is lacking.
	 * This becomes a delegate.
	 * It works for the host.
	 * 
	 * parent is the same for both the
	 * 	a) delegate
	 * 	b) and the host
	 * 
	 * TBD: may be I should create another RADO called DelegatedRADO
	 * @param tag
	 */
	private IRetainedADO host = null;
	public RetainedADO(IRetainedADO parent, String tag, IRetainedADO inHost)
	{
		super(tag);
		parentRADO = parent;
		host = inHost;
		//Add the host for all the callbacks
		parent.addChildRetainedADOOnly(inHost);
		
		//Set the activity if available
		MonitoredActivityWithADOSupport act = parent.getActivity();
		if (act != null)
		{
			this.setActivity(act);
		}
	}
	
	/*
	 * This object can be null
	 * it will be null between stop and start
	 */
	public MonitoredActivityWithADOSupport getActivity()
	{
		MonitoredActivityWithADOSupport a = super.getActivity();
		if (a == null)
		{
			Log.w(tag,"Activity is being asked when it is null");
		}
		return a;
	}
	
	//Usually called on stop
	public void reset()
	{
		Log.d(tag,"Activity is being set to null. It is being stopped");
		setActivity(null);
		resetChildADOs();
	}
	
	private void resetChildADOs()
	{
		for(IRetainedADO rado: this.childRetainedADOs)
		{
			rado.reset();
		}
	}
	
	//Called when the activity is new
	//or reborn
	public void attach(MonitoredActivityWithADOSupport act)
	{
		//Usually called on start
		//the activity is ready
		Log.d(tag,"Activity is being attached. called from onstart");
		setActivity(act);
		attachToChildADOs(act);
	}
	
	private void attachToChildADOs(MonitoredActivityWithADOSupport act)
	{
		for(IRetainedADO rado: this.childRetainedADOs)
		{
			rado.attach(act);
		}
	}
	//Called on destroy
	public void releaseContracts()
	{
		//nothing in the default
		Log.d(tag,"Most likely activity is getting destroyed. release resources");
		releaseChildADOContracts();
	}
	
	private void releaseChildADOContracts()
	{
		for(IRetainedADO rado: this.childRetainedADOs)
		{
			rado.releaseContracts();
		}
	}
	
	//utility function
	public boolean isActivityReady()
	{
		return (getActivity() != null) ? true : false;
	}
	
	public boolean isUIReady()
	{
		if (isActivityReady() == false)
		{
			return false;
		}
		//activity is there
		//Ask the activity if it is ready
		MonitoredActivityWithADOSupport act = getActivity();
		return act.isUIReady();
	}

	/**
	 * Be wary to call this method from a constructor
	 */
	public void addChildRetainedADO(IRetainedADO childRetainedADO)
	{
		this.childRetainedADOs.add(childRetainedADO);
		//Add the activity as this might happen out of sync with sets
		if (isActivityReady())
		{
			//activity is available
			childRetainedADO.attach(getActivity());
		}
	}
	/**
	 * Better method to call from a constructor.
	 * otherwise the previous method is invoking a virtual method
	 * @param childRetainedADO
	 */
	public void addChildRetainedADOOnly(IRetainedADO childRetainedADO)
	{
		this.childRetainedADOs.add(childRetainedADO);
	}
	public boolean isConfigurationChanging()
	{
		if (isActivityReady() == false)
		{
			throw new RuntimeException("Wrong state. Activity is not available");
		}
		return getActivity().isChangingConfigurations();
	}
	/**
	 * You may need to remove this worker ADO once its job is done
	 * @param childRetainedADO
	 */
	public void removeChildRetainedADO(IRetainedADO childRetainedADO)
	{
		Log.d(tag,"Removing a childRetainedADO");
		this.childRetainedADOs.remove(childRetainedADO);
	}
	
	/**
	 * Not sure when you need to call this
	 */
	public void removeAllChildRetainedADOs()
	{
		Log.w(tag,"Removing all child RADOs. Beware of the side affects");
		this.childRetainedADOs.clear();
	}
	
	/**
	 * To support Named ADOs. These are useful
	 * for repeated menu commands.
	 *  
	 * @return The name of this ADO if available or null otherwise
	 */
	public String getName()
	{
		return mName;
	}
	/**
	 * Unregister from the parent.
	 * If the parent is a  RADo it will work.
	 * It has no impact if the parent is an activity
	 * or if the current RADO is a root RADO.
	 * 
	 * In the future see if this behavior should apply to the root RADO as well.
	 */
	public void detachFromParent()
	{
		if (parentRADO != null)
		{
			Log.d(tag,"Removing a child RADO from a parent");
			if (host != null)
			{
				Log.d(tag,"Removing the host child RADO from a parent");
				parentRADO.removeChildRetainedADO(host);
			}
			else
			{
				//host is null. this is the primary
				parentRADO.removeChildRetainedADO(this);
			}
		}
		else
		{
			Log.w(tag,"No parent RADO detected. This is likely a root RADO");
		}
	}
	public void logStatus()
	{
		Log.d(tag,"Number of Child RADO objects:" + this.childRetainedADOs.size());
		for(IRetainedADO rado: this.childRetainedADOs)
		{
			rado.logStatus();
		}
	}
}//eof-class
