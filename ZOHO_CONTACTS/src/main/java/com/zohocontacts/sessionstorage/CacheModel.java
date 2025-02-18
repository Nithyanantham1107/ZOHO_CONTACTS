package com.zohocontacts.sessionstorage;

import com.zohocontacts.dbpojo.UserData;

public class CacheModel {
	private UserData ud;
	private long lastAccessedTime = Long.MAX_VALUE;


	public long getLastAccessed() {
		return lastAccessedTime;
	}

	public void setUserData(UserData table) {

		this.ud = table;
	}

	public UserData getUserData() {
		return this.ud;
	}

}
