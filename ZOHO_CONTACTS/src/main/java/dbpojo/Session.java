package dbpojo;

import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.tables;
import querybuilderconfig.TableSchema.user_data;

public class Session implements Table {

	private String sessionID;
	private long lastAccessed = -1;
	private int userID = -1;
	private int id = -1;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public Session(int id, String sessionId, long lastAccessed, int userId, long createdAt, long modifiedAt) {

		settedData.clear();

		setSessionID(sessionId);
		setLastAccessed(lastAccessed);
		setID(id);
		setUserId(userId);
		setCreatedAt(createdAt);
		setModifiedAt(modifiedAt);

	}

	public Session() {

	}

	public String getTableName() {

		return tables.Session.getTableName();
	}

	public String getPrimaryIDName() {

		return tables.Session.getPrimaryKey();
	}

	public void setID(int id) {
		this.id = id;
		settedData.put( querybuilderconfig.TableSchema.Session.ID.toString(), getID());

	}

	public int getID() {

		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
		settedData.put( querybuilderconfig.TableSchema.Session.created_time.toString(),
				getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
		settedData.put( querybuilderconfig.TableSchema.Session.modified_time.toString(),
				getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setUserId(int UserID) {
		this.userID = UserID;
		settedData.put( querybuilderconfig.TableSchema.Session.user_id.toString(), getUserId());
	}

	public int getUserId() {
		return this.userID;
	}

	public void setSessionID(String SessionID) {
		this.sessionID = SessionID;
		settedData.put( querybuilderconfig.TableSchema.Session.Session_id.toString(),
				getSessionId());
	}

	public String getSessionId() {
		return this.sessionID;
	}

	public void setLastAccessed(long lastAccessed) {
		this.lastAccessed = lastAccessed;

		settedData.put( querybuilderconfig.TableSchema.Session.last_accessed.toString(),
				getLastAccessed());
	}

	public long getLastAccessed() {
		return this.lastAccessed;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

}
