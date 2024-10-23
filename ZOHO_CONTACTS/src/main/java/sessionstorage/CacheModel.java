package sessionstorage;

import java.util.ArrayList;

import dbmodel.UserContacts;
import dbmodel.UserData;
import dbmodel.UserGroup;

public class CacheModel {
	private long sessionExpireTime;
	private long lastAccessed;
	private UserData ud;
	private ArrayList<UserGroup> ug;
	private ArrayList<UserContacts> uc;

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

	public void setUserData(UserData ud) {

		this.ud = ud;
	}

	public UserData getUserData() {
		return this.ud;
	}

	public void setUserGroup(ArrayList<UserGroup> ug) {

		this.ug = ug;
	}

	public ArrayList<UserGroup> getUserGroup() {
		return this.ug;
	}

	public void setUserContact(ArrayList<UserContacts> uc) {

		this.uc = uc;
	}

	public ArrayList<UserContacts> getUserContact() {
		return this.uc;
	}

}
