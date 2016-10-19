/**
 * This is the main package for the provider chapter.
 * This chapter demonstrates 2 persistence options for storing
 * and retrieving domain object such as a Book object.
 * 
 * This package has the main driver application that kicks of 
 * 2 sub driver activities. All of the code that implements a 
 * content provider and the corresponding driver activity
 * is in this package.
 * 
 * All the other packages are there for using SQLite direclty.
 * 
 * com.androidbook.provider.directaccess: has the driver activity
 * for direct SQLite storage.
 * com.androidbook.provider.services: has the POJOs and interface
 * definitions.
 * com.androidbook.provider.services.impl.sqlite: has the 
 * implementation of persistence using SQLite
 * 
 * see the respective package documentation for more details.
 */
package com.androidbook.provider;
 