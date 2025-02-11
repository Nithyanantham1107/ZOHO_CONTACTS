package dbpojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import querybuilderconfig.TableSchema.UserDataSchema;

public class Userdata implements TableWithChild {
	private long id = -1;
	private String Name;
	private String password;
	private String phoneNo;
	private String address;
	private String timeZone;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private List<Table> childTable = new ArrayList<Table>();
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private ArrayList<EmailUser> email = new ArrayList<EmailUser>();
	private LoginCredentials LoginCredentials;
	private ArrayList<Oauth> oauths = new ArrayList<Oauth>();

	public Userdata() {

	}

	public Userdata(Map<String, Object> tableData) {

		settedData.clear();

		if (tableData.get(UserDataSchema.NAME.getColumnName()) != null) {

			setName((String) tableData.get(UserDataSchema.NAME.getColumnName()));

		}

		if (tableData.get(UserDataSchema.PASSWORD.getColumnName()) != null) {

			setPassword((String) tableData.get(UserDataSchema.PASSWORD.getColumnName()));

		}
		if (tableData.get(UserDataSchema.ADDRESS.getColumnName()) != null) {

			setAddress((String) tableData.get(UserDataSchema.ADDRESS.getColumnName()));

		}
		if (tableData.get(UserDataSchema.USERID.getColumnName()) != null) {

			setID((long) tableData.get(UserDataSchema.USERID.getColumnName()));

		}
		if (tableData.get(UserDataSchema.PHONENO.getColumnName()) != null) {

			setPhoneno((String) tableData.get(UserDataSchema.PHONENO.getColumnName()));

		}

		if (tableData.get(UserDataSchema.TIMEZONE.getColumnName()) != null) {

			setTimezone((String) tableData.get(UserDataSchema.TIMEZONE.getColumnName()));

		}

		if (tableData.get(UserDataSchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((Long) tableData.get(UserDataSchema.CREATEDTIME.getColumnName()));

		}

		if (tableData.get(UserDataSchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((Long) tableData.get(UserDataSchema.MODIFIEDTIME.getColumnName()));

		}

	}

	public void setChildTable(Table table) {
		if (table instanceof LoginCredentials) {

			setLoginCredentials((LoginCredentials) table);
		} else if (table instanceof EmailUser) {

			EmailUser email = (EmailUser) table;
			if (getemail(email.getID()) == null) {
				setEmail(email);

			}

		} else if (table instanceof Oauth) {

			Oauth oauth = (Oauth) table;
			if (getOauth(oauth.getID()) == null) {
				setOauth(oauth);

			}

		}

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

	public Table getNewTable(Map<String, Object> tableData) {

		return new Userdata(tableData);
	}

	public String getForiegnkey(String TableName) {

		return UserDataSchema.USERID.getForiegnKey(TableName);
	}

	public List<Table> getChildTables() {

		childTable.clear();
		if (LoginCredentials != null) {

			childTable.add(LoginCredentials);
		}
		if (oauths.size() > 0) {
			for (Table table : oauths) {
				childTable.add(table);
			}

		}
		if (email.size() > 0) {

			for (Table table : email) {

				childTable.add(table);
			}
		}
		return childTable;
	}

	public List<String> getTableColumnNames() {

		return UserDataSchema.USERID.getColumns();
	}

	public String getTableName() {

		return UserDataSchema.USERID.getTableName();
	}

	public String getPrimaryIDName() {

		return UserDataSchema.USERID.getPrimaryKey();
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;

		settedData.put(UserDataSchema.CREATEDTIME.getColumnName(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;

		settedData.put(UserDataSchema.MODIFIEDTIME.getColumnName(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public EmailUser getPrimaryEmail() {

		for (EmailUser data : email) {

			if (data.getIsPrimary()) {
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

	public EmailUser getemail(long ID) {

		for (EmailUser email : this.email) {

			if (email.getID() == ID) {
				return email;
			}
		}
		return null;
	}

	public void setAllOauth(ArrayList<Oauth> oauths) {
		this.oauths.clear();
		this.oauths = oauths;

	}

	public void setOauth(Oauth oauth) {

		this.oauths.add(oauth);
	}

	public ArrayList<Oauth> getallOauth() {
		return this.oauths;
	}

	public Oauth getOauth(long ID) {

		for (Oauth oauth : this.oauths) {

			if (oauth.getID() == ID) {
				return oauth;
			}
		}
		return null;
	}

	public void setID(long id) {
		this.id = id;
		settedData.put(UserDataSchema.USERID.getColumnName(), getID());

		for (Table table : getChildTables()) {

			if (table instanceof LoginCredentials) {
				LoginCredentials login = (LoginCredentials) table;
				login.setUserID(id);

			} else if (table instanceof Oauth) {

				Oauth oauth = (Oauth) table;
				oauth.setUserID(id);

			} else if (table instanceof EmailUser) {

				EmailUser email = (EmailUser) table;
				email.setEmailID(id);
			}
		}
	}

	public List<Table> getDeleteChildTable() {

		return UserDataSchema.USERID.deleteChildTables();
	}

	public long getID() {
		return this.id;
	}

	public String getName() {
		return this.Name;
	}

	public void setName(String name) {
		this.Name = name;
		settedData.put(UserDataSchema.NAME.getColumnName(), getName());
	}

	public void setPassword(String Password) {
		this.password = Password;
		settedData.put(UserDataSchema.PASSWORD.getColumnName(), getPassword());
	}

	public String getPassword() {
		return this.password;
	}

	public void setPhoneno(String Phoneno) {
		this.phoneNo = Phoneno;
		settedData.put(UserDataSchema.PHONENO.getColumnName(), getPhoneno());
	}

	public String getPhoneno() {
		return this.phoneNo;
	}

	public void setTimezone(String Timezone) {
		this.timeZone = Timezone;

		settedData.put(UserDataSchema.TIMEZONE.getColumnName(), getTimezone());
	}

	public String getTimezone() {
		return this.timeZone;
	}

	public void setAddress(String Address) {
		this.address = Address;
		settedData.put(UserDataSchema.ADDRESS.getColumnName(), getAddress());
	}

	public String getAddress() {
		return this.address;

	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

}
