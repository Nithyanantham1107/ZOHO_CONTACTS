package dbpojo;

import querybuilderconfig.TableSchema.tables;

public class EmailUser implements Table {
	private int id = -1;
	private int emID = -1;
	private String email;
	private Boolean isPrimary;
	private long createdAt = -1;
	private long modifiedAt = -1;

public	EmailUser(int id, int emID, String email, boolean isPrimary, long createdAt, long modifiedAt) {

		this.id = id;
		this.emID = emID;
		this.email = email;
		this.isPrimary = isPrimary;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public EmailUser() {

	}

	public void setID(int id) {

		this.id = id;
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

	public void setEmailID(int EmailId) {
		this.emID = EmailId;
	}

	public int getEmailId() {
		return this.emID;
	}

	public void setEmail(String Email) {
		this.email = Email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Boolean getIsPrimary() {
		return this.isPrimary;
	}

	@Override
	public String toString() {
		return "email :" + getEmail() + "  userID:" + getEmailId() + "  is primary:" + getIsPrimary();
	}

}
