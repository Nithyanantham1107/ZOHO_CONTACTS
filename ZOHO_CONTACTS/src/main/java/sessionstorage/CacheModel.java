package sessionstorage;

import java.util.ArrayList;


import dbpojo.Userdata;
import dbpojo.Category;
import dbpojo.ContactDetails;


public class CacheModel {
	private long sessionExpireTime;
	private long lastAccessed;
	private Userdata ud;
	private ArrayList<Category> ug;
	private ArrayList<ContactDetails> uc;

	public void setSessionExpire(long sessionExpireTime) {

		this.sessionExpireTime = sessionExpireTime;
	}

	public long getSessionExpire() {
		return this.sessionExpireTime;
	}

	public void setLastAccessed(long lastAccessed) {

		this.lastAccessed = lastAccessed;
	}

	public long getlastAccessed() {
		return this.lastAccessed;
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
