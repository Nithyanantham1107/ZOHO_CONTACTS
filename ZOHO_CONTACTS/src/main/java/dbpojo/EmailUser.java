package dbpojo;

public class EmailUser {

	int em_id;
	String email;
	boolean is_primary;
	
	
	
	EmailUser(int emID,String email,boolean IsPrimary){
		this.em_id=emID;
		this.email=email;
		this.is_primary=IsPrimary;
	}

	public void setEmailID(int EmailId) {
		this.em_id = EmailId;
	}

	public int getEmailId() {
		return this.em_id;
	}

	public void setEmail(String Email) {
		this.email = Email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setIsPrimary(Boolean IsPrimary) {
		this.is_primary = IsPrimary;
	}

	public boolean getIsPrimary() {
		return this.is_primary;
	}
	
	@Override
	public String toString() {
		return "email :" + getEmail()+"  userID:"+getEmailId()+"  is primary:"+getIsPrimary();
	}

}
