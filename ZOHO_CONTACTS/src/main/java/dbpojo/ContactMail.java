package dbpojo;

import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.Contact_mail;
import querybuilderconfig.TableSchema.tables;

public class ContactMail implements Table {
	private int id = -1;
	private int contactID = -1;
	private String contactEmailID;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public ContactMail(int id, int contactID, String contactEmailID, long createdAt, long modifiedAt) {

		settedData.clear();

		setContactID(contactID);
		setContactMailID(contactEmailID);
		setID(id);
		setCreatedAt(createdAt);
		setModifiedAt(modifiedAt);

	}

	public ContactMail() {

	}

	public String getTableName() {

		return tables.Contact_mail.getTableName();
	}

	public String getPrimaryIDName() {

		return tables.Contact_mail.getPrimaryKey();
	}

	public void setID(int id) {

		this.id = id;
		settedData.put( Contact_mail.ID.toString(), getID());
	}

	public int getID() {
		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
		settedData.put( Contact_mail.created_time.toString(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
		settedData.put( Contact_mail.modified_time.toString(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setContactID(int ContactID) {
		this.contactID = ContactID;
		settedData.put( Contact_mail.contact_id.toString(), getContactID());
	}

	public int getContactID() {
		return this.contactID;
	}

	public void setContactMailID(String ContactMailID) {
		this.contactEmailID = ContactMailID;

		settedData.put( Contact_mail.Contact_email_id.toString(), getContactMailID());
	}

	public String getContactMailID() {
		return contactEmailID;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

}
