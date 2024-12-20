package dbpojo;

public class ContactPhone {

	int contact_id;
	String Contact_phone_no ;

	public ContactPhone(int contactId,String contactPhoneNo) {
		this.contact_id=contactId;
		this.Contact_phone_no=contactPhoneNo;
		
	}
	
	public ContactPhone() {
		
	}
	
	
	public void setContactID(int Contactid) {
		this.contact_id = Contactid;
	}

	public int getContactId() {
		return this.contact_id;
	}

	public void setContactPhone(String ContactPhone) {
		this.Contact_phone_no = ContactPhone;
	}

	public String getContactPhone() {
		return this.Contact_phone_no;
	}

}
