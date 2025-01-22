package dbpojo;

import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.Contact_phone;
import querybuilderconfig.TableSchema.tables;
import querybuilderconfig.TableSchema.user_data;

public class ContactPhone implements Table {
	private int id = -1;
	private int contactID = -1;
	private String ContactPhoneNo;
	private long createdAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private long modifiedAt = -1;

	public ContactPhone(int id, int contactID, String ContactPhoneNo, long createdAt, long modifiedAt) {

		settedData.clear();
		setContactID(contactID);
		setContactPhone(ContactPhoneNo);
		setID(id);
		setCreatedAt(createdAt);
		setModifiedAt(modifiedAt);

	}

	public ContactPhone() {

	}

	public String getPrimaryIDName() {

		return tables.Contact_phone.getPrimaryKey();
	}

	public void setID(int id) {

		this.id = id;

		settedData.put( Contact_phone.ID.toString(), getID());
	}

	public int getID() {

		return this.id;
	}

	public String getTableName() {

		return tables.Contact_phone.getTableName();
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;

		settedData.put( Contact_phone.created_time.toString(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;

		settedData.put( Contact_phone.modified_time.toString(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setContactID(int ContactID) {
		this.contactID = ContactID;

		settedData.put( Contact_phone.contact_id.toString(), getContactID());
	}

	public int getContactID() {
		return this.contactID;
	}

	public void setContactPhone(String ContactPhone) {
		this.ContactPhoneNo = ContactPhone;
		settedData.put( Contact_phone.Contact_phone_no.toString(), getContactPhone());
	}

	public String getContactPhone() {
		return this.ContactPhoneNo;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

}
