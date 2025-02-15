package com.zohocontacts.dbpojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.ContactDetailsSchema;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.dbpojo.tabledesign.TableWithChild;

public class ContactDetails implements TableWithChild {

	private long userId = -1;
	private long id = -1;
	private String Firstname;
	private String Middlename;
	private String Lastname;
	private String gender;
	private String oauthContactID;
	private String Address;
	private long oauthContactModifiedTime = -1;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private List<Table> childTable = new ArrayList<Table>();
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private List<ContactMail> contactMails = new ArrayList<ContactMail>();
	private List<ContactPhone> contactPhones = new ArrayList<ContactPhone>();
	private long oauth;

	public ContactDetails() {

	}

	public ContactDetails(Map<String, Object> tableData) {
		settedData.clear();

		if (tableData.get(ContactDetailsSchema.CONTACTID.getColumnName()) != null) {

			setID((long) tableData.get(ContactDetailsSchema.CONTACTID.getColumnName()));

		}

		if (tableData.get(ContactDetailsSchema.OAUTHCONTACTMODIFIEDTIME.getColumnName()) != null) {
			setOauthcContactModifiedTime(
					(long) tableData.get(ContactDetailsSchema.OAUTHCONTACTMODIFIEDTIME.getColumnName()));

		}

		if (tableData.get(ContactDetailsSchema.USERID.getColumnName()) != null) {

			setUserID((long) tableData.get(ContactDetailsSchema.USERID.getColumnName()));

		}

		if (tableData.get(ContactDetailsSchema.OAUTHCONTACTID.getColumnName()) != null) {

			setOauthContactID((String) tableData.get(ContactDetailsSchema.OAUTHCONTACTID.getColumnName()));

		}
		if (tableData.get(ContactDetailsSchema.FIRSTNAME.getColumnName()) != null) {

			setFirstName((String) tableData.get(ContactDetailsSchema.FIRSTNAME.getColumnName()));

		}

		if (tableData.get(ContactDetailsSchema.MIDDLENAME.getColumnName()) != null) {

			setMiddleName((String) tableData.get(ContactDetailsSchema.MIDDLENAME.getColumnName()));

		}

		if (tableData.get(ContactDetailsSchema.LASTNAME.getColumnName()) != null) {

			setLastName((String) tableData.get(ContactDetailsSchema.LASTNAME.getColumnName()));

		}
		if (tableData.get(ContactDetailsSchema.GENDER.getColumnName()) != null) {

			setGender((String) tableData.get(ContactDetailsSchema.GENDER.getColumnName()));

		}

		if (tableData.get(ContactDetailsSchema.ADDRESS.getColumnName()) != null) {

			setAddress((String) tableData.get(ContactDetailsSchema.ADDRESS.getColumnName()));

		}

		if (tableData.get(ContactDetailsSchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((long) tableData.get(ContactDetailsSchema.CREATEDTIME.getColumnName()));

		}

		if (tableData.get(ContactDetailsSchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((long) tableData.get(ContactDetailsSchema.MODIFIEDTIME.getColumnName()));

		}
		if (tableData.get(ContactDetailsSchema.OAUTHID.getColumnName()) != null) {

			setOauthID((long) tableData.get(ContactDetailsSchema.OAUTHID.getColumnName()));

		}

	}

	public ContactDetails(int userid, int id, int oauthID, String oauthContactID, String Firstname, String middleName,
			String LastName, String gender, String Address, long CreatedAt, long modifiedAt) {

		settedData.clear();

		setUserID(userid);
		setID(id);
		setOauthContactID(oauthContactID);
		setFirstName(Firstname);
		setMiddleName(middleName);
		setLastName(LastName);
		setGender(gender);
		setAddress(Address);
		setCreatedAt(CreatedAt);
		setModifiedAt(modifiedAt);
		setOauthID(oauthID);
	}

	public void setOauthID(long oauth) {
		this.oauth = oauth;
		settedData.put(ContactDetailsSchema.OAUTHID.getColumnName(), getOauthID());
	}

	public long getOauthID() {
		return this.oauth;
	}

	public void setOauthcContactModifiedTime(long oauthContactModifiedTime) {
		this.oauthContactModifiedTime = oauthContactModifiedTime;
		settedData.put(ContactDetailsSchema.OAUTHCONTACTMODIFIEDTIME.getColumnName(), getOauthContactModifiedTime());
	}

	public long getOauthContactModifiedTime() {
		return this.oauthContactModifiedTime;
	}

	public String getTableName() {

		return ContactDetailsSchema.CONTACTID.getTableName();
	}

	public String getPrimaryIDName() {

		return ContactDetailsSchema.CONTACTID.getPrimaryKey();
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
		settedData.put(ContactDetailsSchema.CREATEDTIME.getColumnName(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;

		settedData.put(ContactDetailsSchema.MODIFIEDTIME.getColumnName(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setContactPhone(ContactPhone contactPhone) {

		contactPhones.add(contactPhone);
	}

	public void setAllContactPhone(ArrayList<ContactPhone> phone) {

		contactPhones = phone;
	}

	public List<ContactPhone> getAllContactphone() {
		return contactPhones;
	}

	public ContactPhone getContactphone(long ID) {
		for (ContactPhone contact : getAllContactphone()) {

			if (contact.getID() == ID) {

				return contact;
			}
		}

		return null;
	}

	public void setAllContactMail(ArrayList<ContactMail> mail) {

		contactMails = mail;
	}

	public void setContactMail(ContactMail contactMail) {

		contactMails.add(contactMail);
	}

	public ContactMail getContactMail(long ID) {
		for (ContactMail contact : contactMails) {

			if (contact.getID() == ID) {

				return contact;
			}
		}
		return null;
	}

	public List<ContactMail> getAllContactMail() {

		return contactMails;
	}

	public void setUserID(long userid) {
		this.userId = userid;

		settedData.put(ContactDetailsSchema.USERID.getColumnName(), getUserID());
	}

	public long getUserID() {
		return this.userId;
	}

	public void setID(long id) {
		this.id = id;

		settedData.put(ContactDetailsSchema.CONTACTID.getColumnName(), getID());

		for (Table table : getChildTables()) {

			if (table instanceof ContactPhone) {
				ContactPhone contactPhone = (ContactPhone) table;
				contactPhone.setContactID(id);

			} else if (table instanceof ContactMail) {

				ContactMail contactMail = (ContactMail) table;
				contactMail.setContactID(id);

			}
		}

	}

	public long getID() {
		return this.id;
	}

	public void setOauthContactID(String oauthContactID) {
		this.oauthContactID = oauthContactID;
		settedData.put(ContactDetailsSchema.OAUTHCONTACTID.getColumnName(), getOauthContactID());
	}

	public String getOauthContactID() {
		return this.oauthContactID;
	}

	public void setFirstName(String FirstName) {
		this.Firstname = FirstName;
		settedData.put(ContactDetailsSchema.FIRSTNAME.getColumnName(), getFirstName());
	}

	public String getFirstName() {
		return this.Firstname;
	}

	public void setMiddleName(String MiddleName) {
		this.Middlename = MiddleName;
		settedData.put(ContactDetailsSchema.MIDDLENAME.getColumnName(), getMiddleName());
	}

	public String getMiddleName() {
		return this.Middlename;
	}

	public void setLastName(String LastName) {
		this.Lastname = LastName;
		settedData.put(ContactDetailsSchema.LASTNAME.getColumnName(), getLastName());
	}

	public String getLastName() {
		return this.Lastname;
	}

	public void setGender(String gender) {

		if ("male".equals(gender) || "M".equals(gender)) {
			this.gender = "M";
		} else {
			this.gender = "F";
		}
		if (gender == null) {
			this.gender = null;
		}

		settedData.put(ContactDetailsSchema.GENDER.getColumnName(), getGender());

	}

	public String getGender() {
		return this.gender;
	}

	public void setAddress(String Address) {
		this.Address = Address;
		settedData.put(ContactDetailsSchema.ADDRESS.getColumnName(), getAddress());
	}

	public String getAddress() {
		return this.Address;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public List<String> getTableColumnNames() {

		return ContactDetailsSchema.CONTACTID.getColumns();
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new ContactDetails(tableData);
	}

	@Override
	public String getForiegnkey(String TableName) {

		return ContactDetailsSchema.CONTACTID.getForiegnKey(TableName);
	}

	@Override
	public void setChildTable(Table table) {

		if (table instanceof ContactMail) {
			ContactMail contactMail = (ContactMail) table;
			if (getContactMail(contactMail.getID()) == null) {

				setContactMail(contactMail);

			}

		} else if (table instanceof ContactPhone) {
			ContactPhone contactPhone = (ContactPhone) table;
			if (getContactphone(contactPhone.getID()) == null) {

				setContactPhone(contactPhone);
			}

		}

	}

	@Override
	public List<Table> getChildTables() {

		childTable.clear();
		if (getAllContactMail() != null && getAllContactMail().size() > 0) {
			for (Table table : getAllContactMail()) {

				childTable.add(table);
			}

		}
		if (getAllContactphone() != null && getAllContactphone().size() > 0) {

			for (Table table : getAllContactphone()) {

				childTable.add(table);
			}

		}
		return childTable;
	}

	@Override
	public List<Table> getDeleteChildTable() {

		return ContactDetailsSchema.CONTACTID.deleteChildTables();
	}

}
