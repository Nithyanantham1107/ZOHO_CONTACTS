package dbpojo;

import querybuilderconfig.TableSchema.tables;

public class LoginCredentials implements Table {
	private int id = -1;
	private int logID = -1;
	private String userName;
	private long createdAt = -1;
	private long modifiedAt = -1;

	public LoginCredentials(int id, int logId, String userName, long createdAt, long modifiedAt) {

		this.id = id;
		this.logID = logId;
		this.userName = userName;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;

	}

	public LoginCredentials() {

	}

	public String getTableName() {

		return tables.Login_credentials.getTableName();
	}
	

	public String getPrimaryIDName() {
		
		return  tables.Login_credentials.getPrimaryKey();
	}

	
	

	public void setID(int id) {
		this.id=id;
		
	}
	
	
	public int getID() {

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

	public void setUserID(int userId) {
		this.logID = userId;
	}

	public int getUserId() {
		return this.logID;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}

}
