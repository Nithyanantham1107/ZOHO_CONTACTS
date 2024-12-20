package dbpojo;

public class ContactMail {

	int contact_id;
	String Contact_email_id;

	
	public ContactMail(int contactId,String ContactEmailID) {
		this.contact_id=contactId;
		this.Contact_email_id=ContactEmailID;
		
		
	}
	
	
	public ContactMail() {
		
	}
	
	public void setContactID(int Contactid) {
		this.contact_id = Contactid;
	}

	public int getContactId() {
		return this.contact_id;
	}

	public void setContactMailID(String ContactMailId) {
		this.Contact_email_id = ContactMailId;
	}

	public String getContactMailID() {
		return Contact_email_id;
	}

}
