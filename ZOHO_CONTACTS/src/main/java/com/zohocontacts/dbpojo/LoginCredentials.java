package com.zohocontacts.dbpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.LoginCredentialsSchema;
import com.zohocontacts.dbpojo.tabledesign.Table;

public class LoginCredentials implements Table {
	private long id = -1;
	private long logID = -1;
	private String userName;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public LoginCredentials(Map<String, Object> tableData) {

		settedData.clear();

		if (tableData.get(LoginCredentialsSchema.ID.getColumnName()) != null) {

			setID((long) tableData.get(LoginCredentialsSchema.ID.getColumnName()));
		}
		if (tableData.get(LoginCredentialsSchema.LOGID.getColumnName()) != null) {

			setUserID((long) tableData.get(LoginCredentialsSchema.LOGID.getColumnName()));
		}

		if (tableData.get(LoginCredentialsSchema.USERNAME.getColumnName()) != null) {

			setUserName((String) tableData.get(LoginCredentialsSchema.USERNAME.getColumnName()));
		}
		if (tableData.get(LoginCredentialsSchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((long) tableData.get(LoginCredentialsSchema.CREATEDTIME.getColumnName()));
		}

		if (tableData.get(LoginCredentialsSchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((long) tableData.get(LoginCredentialsSchema.MODIFIEDTIME.getColumnName()));
		}

	}

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

		return LoginCredentialsSchema.ID.getTableName();
	}

	public String getPrimaryIDName() {

		return LoginCredentialsSchema.ID.getPrimaryKey();
	}

	public void setID(long id) {
		this.id = id;
		settedData.put(LoginCredentialsSchema.ID.getColumnName(), getID());

	}

	public long getID() {

		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;

		settedData.put(LoginCredentialsSchema.CREATEDTIME.getColumnName(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
		settedData.put(LoginCredentialsSchema.MODIFIEDTIME.getColumnName(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setUserID(long userId) {
		this.logID = userId;
		settedData.put(LoginCredentialsSchema.LOGID.getColumnName(), getUserId());
	}

	public long getUserId() {
		return this.logID;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		settedData.put(LoginCredentialsSchema.USERNAME.getColumnName(), getUserName());
	}

	public String getUserName() {
		return this.userName;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public List<String> getTableColumnNames() {

		return LoginCredentialsSchema.ID.getColumns();
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new LoginCredentials(tableData);
	}

}
