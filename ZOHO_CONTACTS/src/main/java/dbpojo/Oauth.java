package dbpojo;

import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.tables;

public class Oauth implements Table {

	private int ID = -1;
	private int userID = -1;
	private String oauthProvider;
	private String refreshToken;
	private String accessToken;
	private String email;
	private Boolean syncState;
	private long expiryTime = -1;
	private long createdTime = -1;
	private long modifiedTime = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public Oauth() {

	}

	public Oauth(int ID, int userID, String oauthProvider, String refreshToken, String accessToken, String email,
			Boolean syncState, long expiryTime, long createdTime, long modifiedTime) {

		setID(ID);
		setUserID(userID);
		setSyncState(syncState);
		setOauthProvider(oauthProvider);
		setRefreshToken(refreshToken);
		setAccessToken(accessToken);
		setEmail(email);
		setExpiryTime(expiryTime);
		setCreatedAt(createdTime);
		setModifiedAt(modifiedTime);

	}

	public Boolean getSyncState() {

		return this.syncState;
	}

	public void setSyncState(Boolean syncState) {

		this.syncState = syncState;
		settedData.put(querybuilderconfig.TableSchema.Oauth.sync_state.toString(), getSyncState());
	}

	public int getID() {

		return ID;

	}

	public void setID(int ID) {
		this.ID = ID;

		settedData.put(querybuilderconfig.TableSchema.Oauth.ID.toString(), ID);
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
		settedData.put(querybuilderconfig.TableSchema.Oauth.userID.toString(), userID);
	}

	public String getOauthProvider() {
		return oauthProvider;
	}

	public void setOauthProvider(String oauthProvider) {
		this.oauthProvider = oauthProvider;
		settedData.put(querybuilderconfig.TableSchema.Oauth.Oauth_provider.toString(), oauthProvider);
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		settedData.put(querybuilderconfig.TableSchema.Oauth.refresh_token.toString(), refreshToken);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		settedData.put(querybuilderconfig.TableSchema.Oauth.access_token.toString(), accessToken);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;

		settedData.put(querybuilderconfig.TableSchema.Oauth.email.toString(), email);
	}

	public long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;

		settedData.put(querybuilderconfig.TableSchema.Oauth.expiry_time.toString(), expiryTime);
	}

	public void setCreatedAt(long createdAt) {
		this.createdTime = createdAt;
		settedData.put(querybuilderconfig.TableSchema.Oauth.created_time.toString(), createdAt);

	}

	public long getCreatedAt() {

		return this.createdTime;
	}

	public void setModifiedAt(long modifiedAt) {
		this.modifiedTime = modifiedAt;

		settedData.put(querybuilderconfig.TableSchema.Oauth.modified_time.toString(), modifiedAt);

	}

	public long getModifiedAt() {

		return this.modifiedTime;
	}

	public String getPrimaryIDName() {

		return tables.Oauth.getPrimaryKey();
	}

	public String getTableName() {

		return tables.Oauth.getTableName();
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

}
