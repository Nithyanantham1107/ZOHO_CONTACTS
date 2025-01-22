package dbpojo;

import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.Email_user;
import querybuilderconfig.TableSchema.tables;
import querybuilderconfig.TableSchema.user_data;

public class EmailUser implements Table {
	private int id = -1;
	private int emID = -1;
	private String email;
	private Boolean isPrimary;
	private long createdAt = -1;
	private long modifiedAt = -1;

	private Map<String, Object> settedData = new HashMap<String, Object>();

	public EmailUser(int id, int emID, String email, boolean isPrimary, long createdAt, long modifiedAt) {

		settedData.clear();

		setID(id);
		setEmailID(emID);
		setEmail(email);
		setIsPrimary(isPrimary);
		setCreatedAt(createdAt);
		setModifiedAt(modifiedAt);
	}

	public EmailUser() {

	}

	public void setID(int id) {

		this.id = id;

		settedData.put( Email_user.ID.toString(), getID());
	}

	public int getID() {

		return this.id;
	}

	public String getTableName() {

		return tables.Email_user.getTableName();
	}

	public String getPrimaryIDName() {

		return tables.Email_user.getPrimaryKey();
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
		settedData.put( Email_user.created_time.toString(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
		settedData.put( Email_user.modified_time.toString(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setEmailID(int EmailId) {
		this.emID = EmailId;

		settedData.put( Email_user.em_id.toString(), getEmailId());
	}

	public int getEmailId() {
		return this.emID;
	}

	public void setEmail(String Email) {
		this.email = Email;
		settedData.put( Email_user.email.toString(), getEmail());
	}

	public String getEmail() {
		return this.email;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;

		settedData.put( Email_user.is_primary.toString(), getIsPrimary());
	}

	public Boolean getIsPrimary() {
		return this.isPrimary;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

}
