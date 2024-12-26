package sessionstorage;

import java.util.ArrayList;
import java.util.HashMap;

import dbpojo.Category;
import dbpojo.ContactDetails;
import dbpojo.Session;
import dbpojo.Userdata;


public class CacheModel {
//	private long sessionExpireTime;
//	private long lastAccessed;
	private Userdata ud;
    private HashMap<String,Session> session =new  HashMap<String, Session>();
	private ArrayList<Category> ug;
	private ArrayList<ContactDetails> uc;

//	public void setSessionExpire(long sessionExpireTime) {
//
//		this.sessionExpireTime = sessionExpireTime;
//	}
//
//	public long getSessionExpire() {
//		return this.sessionExpireTime;
//	}

//	public void setLastAccessed(long lastAccessed) {
//
//		this.lastAccessed = lastAccessed;
//	}
//
//	public long getLastAccessed() {
//		return this.lastAccessed;
//	}

	
	
	public void setSession(Session session) {

		this.session.put(session.getSessionId(),session);
	}

//	public ArrayList<Session> getallsession() {
//		return this.session;
//	}

	
	
	public Session getsession(String sessionId) {
		return this.session.get(sessionId);
	}
	
	
	public void setUserData(Userdata ud) {

		this.ud = ud;
	}

	public Userdata getUserData() {
		return this.ud;
	}

	public void setUserGroup(ArrayList<Category> ug) {

		this.ug = ug;
	}

	public ArrayList<Category> getUserGroup() {
		
		

		return this.ug;
	}

	public void setUserContact(ArrayList<ContactDetails> uc) {

		this.uc = uc;
	}

	public ArrayList<ContactDetails> getUserContact() {
		return this.uc;
	}

}
