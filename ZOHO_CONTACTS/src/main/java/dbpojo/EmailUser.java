package dbpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import querybuilderconfig.TableSchema.EmailUserSchema;
import querybuilderconfig.TableSchema.LoginCredentialsSchema;

public class EmailUser implements Table {
	private long id = -1;
	private long emID = -1;
	private String email;
	private Boolean isPrimary;
	private long createdAt = -1;
	private long modifiedAt = -1;

	private Map<String, Object> settedData = new HashMap<String, Object>();

	public EmailUser(Map<String, Object> tableData) {

		settedData.clear();

		if (tableData.get(EmailUserSchema.ID.getColumnName()) != null) {

			setID((long) tableData.get(EmailUserSchema.ID.getColumnName()));
		}
		if (tableData.get(EmailUserSchema.EMAILID.getColumnName()) != null) {

			setEmailID((long) tableData.get(EmailUserSchema.EMAILID.getColumnName()));
		}
		if (tableData.get(EmailUserSchema.EMAIL.getColumnName()) != null) {

			setEmail((String) tableData.get(EmailUserSchema.EMAIL.getColumnName()));
		}

		if (tableData.get(EmailUserSchema.ISPRIMARY.getColumnName()) != null) {

			setIsPrimary((Boolean) tableData.get(EmailUserSchema.ISPRIMARY.getColumnName()));
		}

		if (tableData.get(EmailUserSchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((long) tableData.get(EmailUserSchema.CREATEDTIME.getColumnName()));
		}
		if (tableData.get(EmailUserSchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((long) tableData.get(EmailUserSchema.MODIFIEDTIME.getColumnName()));
		}

	}

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

	public void setID(long id) {

		this.id = id;

		settedData.put(EmailUserSchema.ID.getColumnName(), getID());
	}

	public long getID() {

		return this.id;
	}

	public String getTableName() {

		return EmailUserSchema.ID.getTableName();
	}

	public String getPrimaryIDName() {

		return EmailUserSchema.ID.getPrimaryKey();
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
		settedData.put(EmailUserSchema.CREATEDTIME.getColumnName(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
		settedData.put(EmailUserSchema.MODIFIEDTIME.getColumnName(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setEmailID(long EmailId) {
		this.emID = EmailId;

		settedData.put(EmailUserSchema.EMAILID.getColumnName(), getEmailId());
	}

	public long getEmailId() {
		return this.emID;
	}

	public void setEmail(String Email) {
		this.email = Email;
		settedData.put(EmailUserSchema.EMAIL.getColumnName(), getEmail());
	}

	public String getEmail() {
		return this.email;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;

		settedData.put(EmailUserSchema.ISPRIMARY.getColumnName(), getIsPrimary());
	}

	public Boolean getIsPrimary() {
		return this.isPrimary;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public List<String> getTableColumnNames() {

		return EmailUserSchema.ID.getColumns();
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new EmailUser(tableData);
	}

}
