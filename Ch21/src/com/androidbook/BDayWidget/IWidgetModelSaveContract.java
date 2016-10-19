package com.androidbook.BDayWidget;

import java.util.Map;

/*
 * An interface designed to save and retrieve key/value pairs for a java object
 * in an Android Preferences file. This interface can be applied using
 * other persistence mechanisms as well.
 * 
 * This is a very preliminary design. Can be extended further to be clean
 * using delegation, reflection, and possibly separation of concerns.
 * 
 * @See APrefWidgetModel to see some of the methods that are implemented.
 * This derived class implements the preferences detail. For example
 * you can use ASQLWidgetModel to save these in a database if you coded it.
 * 
 * @See BDayWidgetModel is the most derived class that provides the 
 * necessary metadata and values to store and retrieve
 * 
 * Approach:
 * *********
 * 1. Asks a derived class to get key/value paris to save
 * 2. A derived class indicates a preference filename
 * 3. The abstract class will store the keys for each widget
 * instance id by doing key_iid and storing its value
 * 4. See the abstract class for details
 */
public interface IWidgetModelSaveContract 
{
	//Given a  key name and its value set the value
	//from the store or database to the object local variable.
	//So it is used by retrieval operations from the data store
	//A derived object knows given a key name what field to set
    public void setValueForPref(String key, String value);
    
    //you need to override this method
    //and give a filename that can be used to save preferences.
    //You need to override
	public String getPrefname();
	
	//A derived class will need declare the keys that needs to be saved
	//You need to override this method.
	//Map<Key, value>
	//A dervived class needs to give not only keys but also their values
	public Map<String,String> getPrefsToSave();

	//you have to implement. gets called after restore
	//You can use this do cross-field validations once
	//all the sets are called on all the fields.
    public void init();
}
