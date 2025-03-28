package dbpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import querybuilderconfig.TableSchema.ContactPhoneSchema;
import querybuilderconfig.TableSchema.LoginCredentialsSchema;

public class ContactPhone implements Table {
	private long id = -1;
	private long contactID = -1;
	private String ContactPhoneNo;
	private long createdAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private long modifiedAt = -1;

	public ContactPhone(Map<String, Object> tableData) {

		settedData.clear();

		if (tableData.get(ContactPhoneSchema.ID.getColumnName()) != null) {

			setID((long) tableData.get(ContactPhoneSchema.ID.getColumnName()));
		}

		if (tableData.get(ContactPhoneSchema.CONTACTPHONENO.getColumnName()) != null) {

			setContactPhone((String) tableData.get(ContactPhoneSchema.CONTACTPHONENO.getColumnName()));
		}

		if (tableData.get(ContactPhoneSchema.CONTACTID.getColumnName()) != null) {

			setContactID((long) tableData.get(ContactPhoneSchema.CONTACTID.getColumnName()));
		}

		if (tableData.get(ContactPhoneSchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((long) tableData.get(ContactPhoneSchema.CREATEDTIME.getColumnName()));
		}
		if (tableData.get(ContactPhoneSchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((long) tableData.get(ContactPhoneSchema.MODIFIEDTIME.getColumnName()));
		}

	}

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

		return ContactPhoneSchema.ID.getPrimaryKey();
	}

	public void setID(long id) {

		this.id = id;

		settedData.put(ContactPhoneSchema.ID.getColumnName(), getID());
	}

	public long getID() {

		return this.id;
	}

	public String getTableName() {

		return ContactPhoneSchema.ID.getTableName();
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;

		settedData.put(ContactPhoneSchema.CREATEDTIME.getColumnName(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;

		settedData.put(ContactPhoneSchema.MODIFIEDTIME.getColumnName(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setContactID(long ContactID) {
		this.contactID = ContactID;

		settedData.put(ContactPhoneSchema.CONTACTID.getColumnName(), getContactID());
	}

	public long getContactID() {
		return this.contactID;
	}

	public void setContactPhone(String ContactPhone) {
		this.ContactPhoneNo = ContactPhone;
		settedData.put(ContactPhoneSchema.CONTACTPHONENO.getColumnName(), getContactPhone());
	}

	public String getContactPhone() {
		return this.ContactPhoneNo;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public List<String> getTableColumnNames() {

		return ContactPhoneSchema.ID.getColumns();
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new ContactPhone(tableData);
	}

}
