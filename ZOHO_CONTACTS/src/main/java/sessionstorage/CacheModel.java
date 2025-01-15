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
	private int currentPage;
	private HashMap<String, Session> session = new HashMap<String, Session>();
//	private HashMap<Integer, Category[]> categoryPage = new HashMap<Integer, Category[]>();
//	private HashMap<Integer, ContactDetails[]> contactPage = new HashMap<Integer, ContactDetails[]>();
	private ArrayList<Category> groups;
	private ArrayList<ContactDetails> contacts;

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

		this.session.put(session.getSessionId(), session);
	}

	public void setContactDetails(ArrayList<ContactDetails> contactDetails) {

	}

//	public ArrayList<Session> getallsession() {
//		return this.session;
//	}

	public void setCurrentPage(int currentPage) {

		this.currentPage = currentPage;
	}

	public int getCurrentPage() {
		return this.currentPage;
	}

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

		this.groups = ug;
	}

	public ArrayList<Category> getAllUserGroup() {

		return this.groups;
	}

	public Category getUserGroup(int categoryID) {
		for (Category group : this.groups) {

			if (group.getCategoryID() == categoryID) {
				return group;
			}
		}

		return null;

	}

	public void setUserContact(ArrayList<ContactDetails> uc) {

		this.contacts = uc;
	}

	public ArrayList<ContactDetails> getAllUserContact() {
		return this.contacts;
	}

	public ContactDetails getUserContact(int contactID) {

		for (ContactDetails contactsDetails : this.contacts) {

			if (contactsDetails.getContactID() == contactID) {
				return contactsDetails;
			}
		}

		return null;
	}

}
