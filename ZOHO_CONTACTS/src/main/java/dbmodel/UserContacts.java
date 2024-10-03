package dbmodel;

public class UserContacts {
	
	private int user_id;
	private int contact_id;
	private String f_name;
	private String m_name;
	private String l_name;
	private String phoneno;
	private String address;
	private String gender;
	private String email;

	

	public void setUserid(int user_id) {
		this.user_id=user_id;
	}




	public int getUserid() {
		return this.user_id;
	}
	
	
	

	public void setContactid(int contact_id) {
		this.contact_id=contact_id;
	}
	public int getContactid() {
		return this.contact_id;
	}
	public void setFname(String f_name) {
		this.f_name=f_name;
	}

	public String getFname() {
		return this.f_name;
	}
	
	public void setMname(String m_name) {
		this.m_name=m_name;
	}

	public String getMname() {
		return this.m_name;
	}

	
	
	public void setLname(String l_name) {
		this.l_name=l_name;
	}

	public String getLname() {
		return this.l_name;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno=phoneno;
	}

	public String getPhoneno() {
		return this.phoneno;
	}

	public void setAddress(String address) {
		this.address=address;
	}


	public String getAddress() {
		return this.address;
	}

	public void setEmail(String email) {
		this.email=email;
	}


	public String getEmail() {
		return this.email;
	}
	
	
	public void setGender(String gender) {
		
		if("male".equals(gender) || "M".equals(gender)) {
		   this.gender="M";
		   
	   }
	  if("female".equals(gender) || "F".equals(gender)){
		  this.gender="F";
	   }
		
	}

	public String getGender() {
		return this.gender;
	}


}
