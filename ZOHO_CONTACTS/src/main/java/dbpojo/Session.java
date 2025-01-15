package dbpojo;

import querybuilderconfig.TableSchema.tables;

public class Session implements Table {

	private String sessionID;
	private long lastAccessed = -1;
	private int userID = -1;
	private int id = -1;
	private long createdAt = -1;
	private long modifiedAt = -1;

	public Session(int id, String sessionId, long lastAccessed, int userId, long createdAt, long modifiedAt) {

		this.sessionID = sessionId;
		this.lastAccessed = lastAccessed;
		this.id = id;
		this.userID = userId;

		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;

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

	public void setUserId(int UserID) {
		this.userID = UserID;
	}

	public int getUserId() {
		return this.userID;
	}

	public void setSessionID(String SessionID) {
		this.sessionID = SessionID;
	}

	public String getSessionId() {
		return this.sessionID;
	}

	public void setLastAccessed(long lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public long getLastAccessed() {
		return this.lastAccessed;
	}

}
