package dbpojo;

import querybuilderconfig.TableSchema.tables;

public class ContactDetails implements Table{

	private int userId=-1;
	private int id=-1;
	private String Firstname;
	private String Middlename;
	private String Lastname;
	private String gender;
	private String Address;
	private long createdAt = -1;
	private long modifiedAt = -1;

	private ContactMail contactmail;
	private ContactPhone contactPhone;


	
	public ContactDetails() {
		
	}

	public ContactDetails(int userid, int id, String Firstname, String middleName, String LastName,
			String gender, String Address, long CreatedAt,long modifiedAt) {

		this.userId = userid;
		this.id =id;
		this.Firstname = Firstname;
		this.Middlename = middleName;
		this.Lastname = LastName;
		this.gender = gender;
		this.Address = Address;
		this.createdAt = CreatedAt;
		this.modifiedAt=modifiedAt;

	}
	

	public String getTableName() {
		
		return tables.Contact_details.getTableName();
	}
	

	public String getPrimaryIDName() {
		
		return tables.Contact_details.getPrimaryKey();
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
	}

	public int getUserID() {
		return this.userId;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public void setFirstName(String FirstName) {
		this.Firstname = FirstName;
	}

	public String getFirstName() {
		return this.Firstname;
	}

	public void setMiddleName(String MiddleName) {
		this.Middlename = MiddleName;
	}

	public String getMiddleName() {
		return this.Middlename;
	}

	public void setLastName(String LastName) {
		this.Lastname = LastName;
	}

	public String getLastName() {
		return this.Lastname;
	}

	public void setGender(String Gender) {
//		System.out.println("boolean "+("male".equals(gender) || "M".equals(gender)));
//		
//		System.out.println(gender);
		if ("male".equals(Gender) || "M".equals(Gender)) {
            this.gender = "M";
        } else  {
            this.gender = "F";
        }
	}

	public String getGender() {
		return this.gender;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}

	public String getAddress() {
		return this.Address;
	}

	

	

}
