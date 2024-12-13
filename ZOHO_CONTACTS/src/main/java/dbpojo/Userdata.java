package dbpojo;

import java.util.ArrayList;

public class Userdata {
	int user_id;
	String Name;
	String password;
	String phone_no;
	String address;
	String timezone;
	ArrayList<Session> session = new ArrayList<Session>();
	ArrayList<EmailUser> email = new ArrayList<EmailUser>();
	LoginCredentials LoginCredentials;

	Userdata(int userid, String Name, String password, String Phoneno, String address, String timezone) {
		this.address = address;
		this.Name = Name;
		this.password = password;
		this.user_id = userid;
		this.phone_no = Phoneno;
		this.timezone = timezone;

	}

	public void setLoginCredentials(LoginCredentials login) {

		this.LoginCredentials = login;
	}

	public LoginCredentials getLoginCredentials() {
		return this.LoginCredentials;
	}

	public void setSession(Session session) {

		this.session.add(session);
	}

	public ArrayList<Session> getsession() {
		return this.session;
	}

	public void setEmail(EmailUser email) {

		this.email.add(email);
	}

	public ArrayList<EmailUser> getemail() {
		return this.email;
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
