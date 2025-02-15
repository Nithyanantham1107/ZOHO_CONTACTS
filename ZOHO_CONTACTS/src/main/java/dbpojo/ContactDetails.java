package dbpojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import querybuilderconfig.TableSchema.ContactDetailsSchema;

public class ContactDetails implements TableWithChild {

	private long userId = -1;
	private long id = -1;
	private String Firstname;
	private String Middlename;
	private String Lastname;
	private String gender;
	private String oauthContactID;
	private String Address;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private List<Table> childTable = new ArrayList<Table>();
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private ContactMail contactmail;
	private ContactPhone contactPhone;
	private long oauth;

	public ContactDetails() {

	}

	public ContactDetails(Map<String, Object> tableData) {
		settedData.clear();

		if (tableData.get(ContactDetailsSchema.CONTACTID.getColumnName()) != null) {

			setID((long) tableData.get(ContactDetailsSchema.CONTACTID.getColumnName()));

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

	public void setContactPhone(ContactPhone phone) {

		this.contactPhone = phone;
	}

	public ContactPhone getContactphone() {
		return this.contactPhone;
	}

	public void setContactMail(ContactMail mail) {

		this.contactmail = mail;
	}

	public ContactMail getContactMail() {
		return this.contactmail;
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
//		System.out.println("boolean "+("male".equals(gender) || "M".equals(gender)));
//		
//		System.out.println(gender);
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
			setContactMail(contactMail);
		} else if (table instanceof ContactPhone) {
			ContactPhone contactPhone = (ContactPhone) table;

			setContactPhone(contactPhone);
		}

	}

	@Override
	public List<Table> getChildTables() {

		childTable.clear();
		if (getContactMail() != null) {
			childTable.add(getContactMail());

		}
		if (getContactphone() != null) {

			childTable.add(contactPhone);
		}
		return childTable;
	}

	@Override
	public List<Table> getDeleteChildTable() {

		return ContactDetailsSchema.CONTACTID.deleteChildTables();
	}

}
