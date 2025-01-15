package dbpojo;

import querybuilderconfig.TableSchema.tables;

public class ContactMail implements Table {
	private int id = -1;
	private int contactID = -1;
	private String contactEmailID;
	private long createdAt = -1;
	private long modifiedAt = -1;

	public ContactMail(int id, int contactID, String contactEmailID, long createdAt, long modifiedAt) {
		this.contactID = contactID;
		this.contactEmailID = contactEmailID;
		this.id = id;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;

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
		
		this.id=id;
	}

	public int getID() {
		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setContactID(int ContactID) {
		this.contactID = ContactID;
	}

	public int getContactID() {
		return this.contactID;
	}

	public void setContactMailID(String ContactMailID) {
		this.contactEmailID = ContactMailID;
	}

	public String getContactMailID() {
		return contactEmailID;
	}

}
