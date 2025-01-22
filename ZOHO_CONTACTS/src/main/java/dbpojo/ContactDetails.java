package dbpojo;

import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.Contact_details;
import querybuilderconfig.TableSchema.tables;
import querybuilderconfig.TableSchema.user_data;

public class ContactDetails implements Table {

	private int userId = -1;
	private int id = -1;
	private String Firstname;
	private String Middlename;
	private String Lastname;
	private String gender;
	private String Address;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private ContactMail contactmail;
	private ContactPhone contactPhone;

	public ContactDetails() {

	}

	public ContactDetails(int userid, int id, String Firstname, String middleName, String LastName, String gender,
			String Address, long CreatedAt, long modifiedAt) {

		settedData.clear();

		setUserID(userid);
		setID(id);
		setFirstName(Firstname);
		setMiddleName(middleName);
		setLastName(LastName);
		setGender(gender);
		setAddress(Address);
		setCreatedAt(CreatedAt);
		setModifiedAt(modifiedAt);

	}

	public String getTableName() {

		return tables.Contact_details.getTableName();
	}

	public String getPrimaryIDName() {

		return tables.Contact_details.getPrimaryKey();
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
		settedData.put(Contact_details.created_time.toString(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;

		settedData.put( Contact_details.modified_time.toString(), getModifiedAt());
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

	public void setUserID(int userid) {
		this.userId = userid;

		settedData.put( Contact_details.user_id.toString(), getUserID());
	}

	public int getUserID() {
		return this.userId;
	}

	public void setID(int id) {
		this.id = id;

		settedData.put( Contact_details.contact_id.toString(), getID());
	}

	public int getID() {
		return this.id;
	}

	public void setFirstName(String FirstName) {
		this.Firstname = FirstName;
		settedData.put( Contact_details.First_name.toString(), getFirstName());
	}

	public String getFirstName() {
		return this.Firstname;
	}

	public void setMiddleName(String MiddleName) {
		this.Middlename = MiddleName;
		settedData.put( Contact_details.Middle_name.toString(), getMiddleName());
	}

	public String getMiddleName() {
		return this.Middlename;
	}

	public void setLastName(String LastName) {
		this.Lastname = LastName;
		settedData.put( Contact_details.Last_name.toString(), getLastName());
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

		settedData.put( Contact_details.gender.toString(), getGender());

	}

	public String getGender() {
		return this.gender;
	}

	public void setAddress(String Address) {
		this.Address = Address;
		settedData.put( Contact_details.Address.toString(), getAddress());
	}

	public String getAddress() {
		return this.Address;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

}
