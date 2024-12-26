package dbpojo;

import java.util.ArrayList;
import java.util.HashMap;

public class Userdata {
	int user_id;
	String Name;
	String password;
	String phone_no;
	String address;
	String timezone;
	String CurrentEmail;
//	HashMap<String,Session> session =new  HashMap<String, Session>();
	ArrayList<EmailUser> email = new ArrayList<EmailUser>();
	LoginCredentials LoginCredentials;

	
	public Userdata() {
		
	}
	
	
	Userdata(int userid, String Name, String password, String Phoneno, String address, String timezone) {
		this.address = address;
		this.Name = Name;
		this.password = password;
		this.user_id = userid;
		this.phone_no = Phoneno;
		this.timezone = timezone;

	}
	
	
	
	public void setCurrentEmail(String current ) {

		this.CurrentEmail=current;
	}

	public String getCurrentemail() {
		return this.CurrentEmail;
	}

	public void setLoginCredentials(LoginCredentials login) {

		this.LoginCredentials = login;
	}

	public LoginCredentials getLoginCredentials() {
		return this.LoginCredentials;
	}

	

	
	public void setEmail(EmailUser email) {

		this.email.add(email);
	}

	public ArrayList<EmailUser> getallemail() {
		return this.email;
	}
	public EmailUser getemail(int i) {
		return this.email.get(i);
	}

	public void setUserId(int user_id) {
		this.user_id = user_id;
	}

	public int getUserId() {
		return this.user_id;
	}

	public String getName() {
		return this.Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public void setPassword(String Password) {
		this.password = Password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPhoneno(String Phoneno) {
		this.phone_no = Phoneno;
	}

	public String getPhoneno() {
		return this.phone_no;
	}

	public void setTimezone(String Timezone) {
		this.timezone = Timezone;
	}

	public String getTimezone() {
		return this.timezone;
	}

	public void setAddress(String Address) {
		this.address = Address;
	}

	public String getAddress() {
		return this.address;
	}

}
