package dbpojo;

import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.Login_credentials;
import querybuilderconfig.TableSchema.tables;

public class LoginCredentials implements Table {
	private int id = -1;
	private int logID = -1;
	private String userName;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public LoginCredentials(int id, int logId, String userName, long createdAt, long modifiedAt) {
		
		settedData.clear();
		setID(id);
		setUserID(logId);
		setUserName(userName);
		setCreatedAt(createdAt);
		setModifiedAt(modifiedAt);

	}

	public LoginCredentials() {

	}

	public String getTableName() {

		return tables.Login_credentials.getTableName();
	}

	public String getPrimaryIDName() {

		return tables.Login_credentials.getPrimaryKey();
	}

	public void setID(int id) {
		this.id = id;
		settedData.put( Login_credentials.ID.toString(), getID());

	}

	public int getID() {

		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;

		settedData.put( Login_credentials.created_time.toString(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
		settedData.put( Login_credentials.modified_time.toString(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setUserID(int userId) {
		this.logID = userId;
		settedData.put( Login_credentials.log_id.toString(), getUserId());
	}

	public int getUserId() {
		return this.logID;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		settedData.put( Login_credentials.username.toString(), getUserName());
	}

	public String getUserName() {
		return this.userName;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

}
