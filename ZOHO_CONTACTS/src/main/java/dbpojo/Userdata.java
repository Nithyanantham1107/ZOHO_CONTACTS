package dbpojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.tables;
import querybuilderconfig.TableSchema.user_data;

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
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private ArrayList<EmailUser> email = new ArrayList<EmailUser>();
	private LoginCredentials LoginCredentials;
	

	public Userdata() {

	}

	public Userdata(int userId, String Name, String password, String Phoneno, String address, String timezone,
			long createdAt, long modifiedAt) {
		settedData.clear();
	
		setName(Name);
		setPassword(password);
		setAddress(address);
		setID(userId);
		setPhoneno(Phoneno);
		setTimezone(timezone);
		setCreatedAt(createdAt);
		setModifiedAt(modifiedAt);

	}

	public String getTableName() {

		return tables.user_data.getTableName();
	}

	public String getPrimaryIDName() {

		return tables.user_data.getPrimaryKey();
	}

//	public void setRowKey(int rowKey) {
//
//		this.id = rowKey;
//		settedData.put(getTableName()+"."+user_data.user_id.toString(), getRowKey());
//	}
//
//	public int getRowKey() {
//
//		return this.id;
//	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;

		settedData.put( user_data.created_time.toString(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;

		settedData.put( user_data.modified_time.toString(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}



	public EmailUser getPrimaryEmail() {
		
		
		for(EmailUser data: email ) {
			
			
			if(data.getIsPrimary()) {
				return data;
			}
		}
		return null;
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
		settedData.put( user_data.user_id.toString(), getID());
	}

	public int getID() {
		return this.id;
	}

	public String getName() {
		return this.Name;
	}

	public void setName(String name) {
		this.Name = name;
		settedData.put( user_data.Name.toString(), getName());
	}

	public void setPassword(String Password) {
		this.password = Password;
		settedData.put( user_data.password.toString(), getPassword());
	}

	public String getPassword() {
		return this.password;
	}

	public void setPhoneno(String Phoneno) {
		this.phoneNo = Phoneno;
		settedData.put( user_data.phone_no.toString(), getPhoneno());
	}

	public String getPhoneno() {
		return this.phoneNo;
	}

	public void setTimezone(String Timezone) {
		this.timeZone = Timezone;

		settedData.put( user_data.timezone.toString(), getTimezone());
	}

	public String getTimezone() {
		return this.timeZone;
	}

	public void setAddress(String Address) {
		this.address = Address;
		settedData.put( user_data.address.toString(), getAddress());
	}

	public String getAddress() {
		return this.address;

	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

}
