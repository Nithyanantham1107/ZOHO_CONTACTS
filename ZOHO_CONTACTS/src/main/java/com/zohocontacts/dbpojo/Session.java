package com.zohocontacts.dbpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.UserDataSchema;
import com.zohocontacts.dbpojo.tabledesign.Table;

public class Session implements Table {

	private String sessionID;
	private long lastAccessed = -1;
	private long userID = -1;
	private long id = -1;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public Session(Map<String, Object> tableData) {

		settedData.clear();

		if (tableData.get(SessionSchema.SESSIONID.getColumnName()) != null) {

			setSessionID((String) tableData.get(SessionSchema.SESSIONID.getColumnName()));
		}

		if (tableData.get(SessionSchema.LASTACCESSED.getColumnName()) != null) {

			setLastAccessed((Long) tableData.get(SessionSchema.LASTACCESSED.getColumnName()));
		}
		if (tableData.get(SessionSchema.ID.getColumnName()) != null) {

			setID((long) tableData.get(SessionSchema.ID.getColumnName()));
		}

		if (tableData.get(SessionSchema.USERID.getColumnName()) != null) {

			setUserId((long ) tableData.get(SessionSchema.USERID.getColumnName()));
		}
		if (tableData.get(SessionSchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((long) tableData.get(SessionSchema.CREATEDTIME.getColumnName()));
		}
		if (tableData.get(SessionSchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((long) tableData.get(SessionSchema.MODIFIEDTIME.getColumnName()));
		}

	}


	public Session() {

	}

	public String getTableName() {

		return com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema.ID.getTableName();
	}

	public String getPrimaryIDName() {

		return com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema.ID.getPrimaryKey();
	}

	public void setID(long id) {
		this.id = id;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema.ID.getColumnName(), getID());

	}

	public long getID() {

		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema.CREATEDTIME.getColumnName(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema.MODIFIEDTIME.getColumnName(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setUserId(long UserID) {
		this.userID = UserID;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema.USERID.getColumnName(), getUserId());
	}

	public long getUserId() {
		return this.userID;
	}

	public void setSessionID(String SessionID) {
		this.sessionID = SessionID;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema.SESSIONID.getColumnName(), getSessionId());
	}

	public String getSessionId() {
		return this.sessionID;
	}

	public void setLastAccessed(long lastAccessed) {
		this.lastAccessed = lastAccessed;

		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema.LASTACCESSED.getColumnName(), getLastAccessed());
	}

	public long getLastAccessed() {
		return this.lastAccessed;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new Session(tableData);
	}

	@Override
	public List<String> getTableColumnNames() {

		return SessionSchema.SESSIONID.getColumns();
	}

}
