package sessionstorage;

import java.util.HashMap;
import java.util.Map;

import dbpojo.Session;
import dbpojo.Userdata;

public class CacheModel {
	private Userdata ud;
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

	public void setUserData(Userdata table) {

		this.ud = table;
	}

	public Userdata getUserData() {
		return this.ud;
	}

}
