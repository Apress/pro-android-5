package com.androidbook.provider.services.impl.sqlite;

import java.util.ArrayList;
import java.util.List;

/**
 * It is often necessary to translate data between
 * java objects and SQLite through the android sqlite api.
 * 
 * This process needs to know what type of fields each object
 * supports and what their column names are.
 * 
 * Throush some defined contracts via abstract methods this
 * class uses a hook and template pattern to provide public 
 * methods that can be called ot get column names of derived objects.
 * 
 * Each domain object like a Book will have its metadata counterpart
 * that defines its metadata like columns and being able to set and get
 * from name value pairs. These metadata objects will use this base
 * class.
 * 
 * @See BaseEntitySQLLiteMetaData 
 * @See BookSQLiteMetaData
 * @See AObjectMetadata
 *
 */
public abstract class AObjectMetadata 
{
	protected abstract void populateYourColumnNames(List<String> colNameList);
	public String[] getColumnNames()
	{
		List<String> cols = new ArrayList<String>();
		populateYourColumnNames(cols);
		int count = cols.size();
		String[] columnNamesArray = new String[count];
		return cols.toArray(columnNamesArray);
	}
}
