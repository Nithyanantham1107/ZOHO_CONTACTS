package dbpojo;

import java.util.ArrayList;

import querybuilderconfig.TableSchema.tables;

public class Userdata implements Table {
	private int id = -1;
	private String Name;
	private String password;
	private String phoneNo;
	private String address;
	private String timeZone;
	private String CurrentEmail;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private ArrayList<EmailUser> email = new ArrayList<EmailUser>();
	private LoginCredentials LoginCredentials;

	public Userdata() {

	}

	public Userdata(int userId, String Name, String password, String Phoneno, String address, String timezone, long createdAt,
			long modifiedAt) {
		this.address = address;
		this.Name = Name;
		this.password = password;
		this.id = userId;
		this.phoneNo = Phoneno;
		this.timeZone = timezone;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;

	}

	public String getTableName() {

		return tables.user_data.getTableName();
	}

	

	public String getPrimaryIDName() {
		
		return tables.user_data.getPrimaryKey();
	}
	public void setRowKey(int rowKey) {

		this.id = rowKey;
	}

	public int getRowKey() {

		return this.id;
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

	public void setCurrentEmail(String current) {

		this.CurrentEmail = current;
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

	public EmailUser getemail(int ID) {

		for (EmailUser email : this.email) {

			if (email.getID() == ID) {
				return email;
			}
		}
		return null;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return this.id;
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
		this.phoneNo = Phoneno;
	}

	public String getPhoneno() {
		return this.phoneNo;
	}

	public void setTimezone(String Timezone) {
		this.timeZone = Timezone;
	}

	public String getTimezone() {
		return this.timeZone;
	}

	public void setAddress(String Address) {
		this.address = Address;
	}

	public String getAddress() {
		return this.address;
	}

}
