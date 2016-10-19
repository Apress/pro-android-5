package com.androidbook.provider.services;

import java.util.Date;

/**
 * Acts as a base class for all POJO entities
 * Holds common values for all basic objects.
 * Reflects the typical most common fields in a database.
 * yet those should be applicable even if it is not a database objects. 
 *
 */
public class BaseEntity {
	
	private int id; //database identifier
	
	private String ownedAccount = null; //Multi-tenant if needed 
	private String createdBy;
	private Date createdOn;
	private String lastUpdatedBy;
	private Date lastUpdatedOn;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getOwnedAccount() {
		return ownedAccount;
	}
	public BaseEntity(String ownedAccount, String createdBy, Date createdOn,
			String lastUpdatedBy, Date lastUpdatedOn, int id) {
		super();
		this.ownedAccount = ownedAccount;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedOn = lastUpdatedOn;
		this.id = id;
	}
	//For persitence
	public BaseEntity(){}
	public void setOwnedAccount(String ownedAccount) {
		this.ownedAccount = ownedAccount;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
}
