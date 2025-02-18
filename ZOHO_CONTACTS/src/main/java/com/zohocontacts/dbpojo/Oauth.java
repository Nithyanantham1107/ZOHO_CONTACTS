package com.zohocontacts.dbpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema;
import com.zohocontacts.dbpojo.tabledesign.Table;

public class Oauth implements Table {

	private long ID = -1;
	private long userID = -1;
	private String oauthProvider;
	private String refreshToken;
	private String accessToken;
	private String email;
	private Boolean syncState;
	private long expiryTime = -1;
	private long createdTime = -1;
	private long oauthSyncTime = -1;
	private long modifiedTime = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public Oauth() {

	}

	public Oauth(Map<String, Object> tableData) {

		settedData.clear();

		if (tableData.get(OauthSchema.ID.getColumnName()) != null) {

			setID((Long) tableData.get(OauthSchema.ID.getColumnName()));
		}

		if (tableData.get(OauthSchema.SYNCSTATE.getColumnName()) != null) {

			setSyncState((Boolean) tableData.get(OauthSchema.SYNCSTATE.getColumnName()));
		} else {

			setSyncState(false);
		}

		if (tableData.get(OauthSchema.OAUTHPROVIDER.getColumnName()) != null) {

			setOauthProvider((String) tableData.get(OauthSchema.OAUTHPROVIDER.getColumnName()));

		}

		if (tableData.get(OauthSchema.REFRESHTOKEN.getColumnName()) != null) {
			setRefreshToken((String) tableData.get(OauthSchema.REFRESHTOKEN.getColumnName()));
		}
		if (tableData.get(OauthSchema.ACCESSTOKEN.getColumnName()) != null) {

			setAccessToken((String) tableData.get(OauthSchema.ACCESSTOKEN.getColumnName()));

		}
		if (tableData.get(OauthSchema.EMAIL.getColumnName()) != null) {

			setEmail((String) tableData.get(OauthSchema.EMAIL.getColumnName()));

		}

		if (tableData.get(OauthSchema.EXPIRYTIME.getColumnName()) != null) {

			setExpiryTime((Long) tableData.get(OauthSchema.EXPIRYTIME.getColumnName()));

		}

		if (tableData.get(OauthSchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((Long) tableData.get(OauthSchema.CREATEDTIME.getColumnName()));

		}

		if (tableData.get(OauthSchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((Long) tableData.get(OauthSchema.MODIFIEDTIME.getColumnName()));

		}
		if (tableData.get(OauthSchema.USERID.getColumnName()) != null) {

			setUserID((long) tableData.get(OauthSchema.USERID.getColumnName()));

		}

		if (tableData.get(OauthSchema.OAUTHSYNCTIME.getColumnName()) != null) {
			setOauthSyncTime((long) tableData.get(OauthSchema.OAUTHSYNCTIME.getColumnName()));

		}

	}

	

	public Boolean getSyncState() {

		return this.syncState;
	}

	public void setSyncState(Boolean syncState) {

		this.syncState = syncState;
		settedData.put(
				com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.SYNCSTATE.getColumnName(),
				getSyncState());
	}

	public long getID() {

		return ID;

	}

	public void setID(long ID) {
		this.ID = ID;

		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.ID.getColumnName(),
				ID);
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
		settedData.put(
				com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.USERID.getColumnName(),
				userID);
	}

	public String getOauthProvider() {
		return oauthProvider;
	}

	public void setOauthProvider(String oauthProvider) {
		this.oauthProvider = oauthProvider;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.OAUTHPROVIDER
				.getColumnName(), oauthProvider);
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.REFRESHTOKEN
				.getColumnName(), refreshToken);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.ACCESSTOKEN
				.getColumnName(), accessToken);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;

		settedData.put(
				com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.EMAIL.getColumnName(),
				email);
	}

	public long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;

		settedData.put(
				com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.EXPIRYTIME.getColumnName(),
				expiryTime);
	}

	public void setCreatedAt(long createdAt) {
		this.createdTime = createdAt;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.CREATEDTIME
				.getColumnName(), createdAt);

	}

	public long getCreatedAt() {

		return this.createdTime;
	}

	public void setModifiedAt(long modifiedAt) {
		this.modifiedTime = modifiedAt;

		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.MODIFIEDTIME
				.getColumnName(), modifiedAt);

	}

	public long getModifiedAt() {

		return this.modifiedTime;
	}

	public String getPrimaryIDName() {

		return com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.ID.getPrimaryKey();
	}

	public String getTableName() {

		return com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.ID.getTableName();
	}

	public void setOauthSyncTime(long oauthSyncTime) {

		this.oauthSyncTime = oauthSyncTime;
		
		settedData.put(
				com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema.OAUTHSYNCTIME.getColumnName(),
	getOauthSyncTime());
	}

	public long getOauthSyncTime() {

		return this.oauthSyncTime;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new Oauth(tableData);
	}

	@Override
	public List<String> getTableColumnNames() {

		return OauthSchema.ID.getColumns();
	}

}
