package dbpojo;

import java.util.ArrayList;

public class ContactDetails {

	int user_id;
	int contact_id;
	String First_name;
	String Middle_name;
	String Last_name;
	String gender;
	String Address;

	ContactMail contactmail;
	ContactPhone contactPhone;

	long created_At;

	public ContactDetails(int userid, int contactid, String Firstname, String middleName, String LastName,
			String gender, String Address, long CreatedAt) {

		this.user_id = userid;
		this.contact_id = contactid;
		this.First_name = Firstname;
		this.Middle_name = middleName;
		this.Last_name = LastName;
		this.gender = gender;
		this.Address = Address;
		this.created_At = CreatedAt;

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
		this.user_id = userid;
	}

	public int getUserID() {
		return this.user_id;
	}

	public void setContactID(int Contactid) {
		this.contact_id = Contactid;
	}

	public int getContactID() {
		return this.contact_id;
	}

	public void setFirstName(String FirstName) {
		this.First_name = FirstName;
	}

	public String getFirstName() {
		return this.First_name;
	}

	public void setMiddleName(String MiddleName) {
		this.Middle_name = MiddleName;
	}

	public String getMiddleName() {
		return this.Middle_name;
	}

	public void setLastName(String LastName) {
		this.Last_name = LastName;
	}

	public String getLastName() {
		return this.Last_name;
	}

	public void setGender(String Gender) {
		this.gender = Gender;
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

	public void setCreatedAt(int CreatedAt) {
		this.created_At = CreatedAt;
	}

	public long getCreatedAt() {
		return this.created_At;
	}

}
