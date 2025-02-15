package com.zohocontacts.sessionstorage;

import java.util.HashMap;
import java.util.Map;

import com.zohocontacts.dbpojo.Session;
import com.zohocontacts.dbpojo.UserData;

public class CacheModel {
	private UserData ud;
	private long lastAccessedTime=Long.MAX_VALUE;
	private Map<String, Session> session = new HashMap<String, Session>();

	public void setSession(Session session) {

		this.session.put(session.getSessionId(), session);
		if(session.getLastAccessed()<lastAccessedTime) {
			
			lastAccessedTime=session.getLastAccessed();
		}
	}

	public Session getsession(String sessionId) {
		
		return this.session.get(sessionId);
	}
	
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
