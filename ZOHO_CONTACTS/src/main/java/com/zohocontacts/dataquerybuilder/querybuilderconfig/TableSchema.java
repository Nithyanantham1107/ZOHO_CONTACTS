package com.zohocontacts.dataquerybuilder.querybuilderconfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dbpojo.Category;
import com.zohocontacts.dbpojo.CategoryRelation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.ContactMail;
import com.zohocontacts.dbpojo.ContactPhone;
import com.zohocontacts.dbpojo.EmailUser;
import com.zohocontacts.dbpojo.LoginCredentials;
import com.zohocontacts.dbpojo.Oauth;
import com.zohocontacts.dbpojo.Session;

public class TableSchema {

	public enum OauthProvider {

		GOOGLE, FACEBOOK, MICROSOFT, LINKEDIN;

	}

	public enum SyncState {

		ENABLED, DISABLED;
	}

	public enum Statement {

		RETURNGENERATEDKEYS;

	}

	public enum OpType {

		INSERT(1, "INSERT"), UPDATE(2, "UPDATE"), DELETE(3, "DELETE");

		int id;
		String type;

		OpType(int id, String type) {

			this.id = id;
			this.type = type;

		}

		public int getID() {

			return this.id;
		}

		public String getOpType() {
			return this.type;
		}

	}

	public enum Operation {
		EQUAL("="), NOTEQUAL("!="), GREATERTHAN(">"), LESSERTHAN("<"), GREATEREQUAL(">="), LESSEREQUAL("<=");

		String value;

		Operation(String val) {

			this.value = val;
		}

		public String getOperation() {
			return this.value;
		}
	}

	public enum JoinType {
		LEFT("LEFT JOIN"), RIGHT("RIGHT JOIN"), INNER("INNER JOIN"), OUTER("OUTER JOIN");

		String value;

		JoinType(String val) {

			this.value = val;
		}

		public String getType() {
			return this.value;
		}
	}

	public enum CategoryRelationSchema implements Table {
		ID("ID"), CONTACTIDTOJOIN("contact_id_to_join"), CATEGORYID("Category_id"), CREATEDTIME("created_time"),
		MODIFIEDTIME("modified_time");

		private String columnName;

		private CategoryRelationSchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("ID", "contact_id_to_join", "Category_id",
				"created_time", "modified_time");

		private static final Map<String, String> FOREIGNKEYRELATION = new HashMap<String, String>();

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = new ArrayList<com.zohocontacts.dbpojo.tabledesign.Table>();

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Category_relation";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum ServerRegistrySchema implements Table {
		SERVERID("server_id"), SERVERIP("server_ip"), SERVERPORT("server_port"), CREATEDTIME("created_time"),
		MODIFIEDTIME("modified_time");

		private String columnName;

		private ServerRegistrySchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("server_id", "server_port", "server_ip",
				"created_time", "modified_time");

		private static final Map<String, String> FOREIGNKEYRELATION = new HashMap<String, String>();

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = new ArrayList<com.zohocontacts.dbpojo.tabledesign.Table>();

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Server_registry";
		}

		public String getPrimaryKey() {

			return "server_id";
		}

	}

	public enum CategorySchema implements Table {
		CATEGORYID("Category_id"), CATEGORYNAME("Category_name"), CREATEDBY("created_by"), CREATEDTIME("created_time"),
		MODIFIEDTIME("modified_time");

		private String columnName;

		private CategorySchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("Category_id", "Category_name", "created_by",
				"created_time", "modified_time");
		private static final Map<String, String> FOREIGNKEYRELATION = Map.of(

				CategoryRelationSchema.CATEGORYID.getTableName(), CategoryRelationSchema.CATEGORYID.getColumnName()

		);

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = Arrays
				.asList(new CategoryRelation());

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Category";
		}

		public String getPrimaryKey() {

			return "Category_id";
		}

	}

	public enum ContactDetailsSchema implements Table {
		USERID("user_id"), CONTACTID("contact_id"), FIRSTNAME("First_name"), MIDDLENAME("Middle_name"),
		LASTNAME("Last_name"), GENDER("gender"), ADDRESS("Address"), CREATEDTIME("created_time"),
		MODIFIEDTIME("modified_time"), OAUTHCONTACTID("Oauth_contactID"), OAUTHID("OauthID"),
		OAUTHCONTACTMODIFIEDTIME("Oauth_contact_modifiedtime");

		private String columnName;

		private ContactDetailsSchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("user_id", "contact_id", "First_name",
				"Middle_name", "Last_name", "gender", "Address", "created_time", "modified_time", "Oauth_contactID",
				"OauthID", "Oauth_contact_modifiedtime");

		private static final Map<String, String> FOREIGNKEYRELATION = Map.of(
				ContactPhoneSchema.CONTACTID.getTableName(), ContactPhoneSchema.CONTACTID.getColumnName(),
				CategoryRelationSchema.CONTACTIDTOJOIN.getTableName(),
				CategoryRelationSchema.CONTACTIDTOJOIN.getColumnName(),

				ContactMailSchema.CONTACTID.getTableName(), ContactMailSchema.CONTACTID.getColumnName()

		);
		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = Arrays
				.asList(new ContactMail(), new ContactPhone(), new CategoryRelation());

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Contact_details";
		}

		public String getPrimaryKey() {

			return "contact_id";
		}

	}

	public enum ContactMailSchema implements Table {
		ID("ID"), CONTACTID("contact_id"), LABELNAME("label_name"),CONTACTMAILID("Contact_email_id"), CREATEDTIME("created_time"),
		MODIFIEDTIME("modified_time");

		private String columnName;

		private ContactMailSchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("ID", "contact_id", "Contact_email_id","label_name",
				"created_time", "modified_time");
		private static final Map<String, String> FOREIGNKEYRELATION = new HashMap<String, String>();

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = new ArrayList<com.zohocontacts.dbpojo.tabledesign.Table>();

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Contact_mail";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum ContactPhoneSchema implements Table {
		ID("ID"), CONTACTID("contact_id"),LABELNAME("label_name"), CONTACTPHONENO("Contact_phone_no"), CREATEDTIME("created_time"),
		MODIFIEDTIME("modified_time");

		private String columnName;

		private ContactPhoneSchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("ID", "contact_id", "Contact_phone_no","label_name",
				"created_time", "modified_time");

		private static final Map<String, String> FOREIGNKEYRELATION = new HashMap<String, String>();

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = new ArrayList<com.zohocontacts.dbpojo.tabledesign.Table>();

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Contact_phone";
		}

		@Override
		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum EmailUserSchema implements Table {
		ID("ID"), EMAILID("em_id"), EMAIL("email"), ISPRIMARY("is_primary"), CREATEDTIME("created_time"),
		MODIFIEDTIME("modified_time");

		private String columnName;

		private EmailUserSchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("ID", "em_id", "email", "is_primary",
				"created_time", "modified_time");
		private static final Map<String, String> FOREIGNKEYRELATION = new HashMap<String, String>();

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = new ArrayList<com.zohocontacts.dbpojo.tabledesign.Table>();

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Email_user";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum LoginCredentialsSchema implements Table {
		ID("ID"), LOGID("log_id"), USERNAME("username"), CREATEDTIME("created_time"), MODIFIEDTIME("modified_time");

		private String columnName;

		private LoginCredentialsSchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("ID", "log_id", "username", "created_time",
				"modified_time");

		private static final Map<String, String> FOREIGNKEYRELATION = new HashMap<String, String>();

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = new ArrayList<com.zohocontacts.dbpojo.tabledesign.Table>();

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Login_credentials";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum SessionSchema implements Table {
		ID("ID"), SESSIONID("Session_id"), LASTACCESSED("last_accessed"), USERID("user_id"),
		CREATEDTIME("created_time"), MODIFIEDTIME("modified_time");

		private String columnName;

		private SessionSchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("ID", "Session_id", "last_accessed", "user_id",
				"created_time", "modified_time");

		private static final Map<String, String> FOREIGNKEYRELATION = new HashMap<String, String>();

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = new ArrayList<com.zohocontacts.dbpojo.tabledesign.Table>();

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Session";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum UserDataSchema implements Table {
		USERID("user_id"), NAME("Name"), PASSWORD("password"), PHONENO("phone_no"), ADDRESS("address"),
		TIMEZONE("timezone"), CREATEDTIME("created_time"), MODIFIEDTIME("modified_time");

		private String columnName;
		private static final List<String> COLUMNNAMES = Arrays.asList("user_id", "Name", "password", "phone_no",
				"address", "timezone", "created_time", "modified_time");
		private static final Map<String, String> foreignKeyRelation = Map.of(
				LoginCredentialsSchema.LOGID.getTableName(), LoginCredentialsSchema.LOGID.columnName,
				EmailUserSchema.EMAILID.getTableName(), EmailUserSchema.EMAILID.getColumnName(),
				OauthSchema.USERID.getTableName(), OauthSchema.USERID.getColumnName(),
				ContactDetailsSchema.CONTACTID.getTableName(), ContactDetailsSchema.USERID.getColumnName(),
				CategorySchema.CREATEDBY.getTableName(), CategorySchema.CREATEDBY.getColumnName(),
				SessionSchema.USERID.getTableName(), SessionSchema.USERID.getColumnName()

		);

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables = Arrays.asList(
				new EmailUser(), new LoginCredentials(), new ContactDetails(), new Category(), new Session(),
				new Oauth());

		private UserDataSchema(String columnName) {
			this.columnName = columnName;
		}

		public String getForiegnKey(String Name) {

			return foreignKeyRelation.get(Name);
		}

		public String getColumnName() {

			return columnName;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return deleteChildTables;
		}

		public String getTableName() {
			return "user_data";
		}

		public String getPrimaryKey() {

			return "user_id";
		}

	}

	public enum AuditLogSchema implements Table {

		ID("ID"), PREVIOUSSTATE("previous_state"), CHANGEDSTATE("changed_state"), TABLENAME("table_name"),
		OPERATION("operation"), ROWKEY("row_key"), CREATEDAT("created_at"), CREATEDBY("created_by");

		private String columnName;

		private AuditLogSchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("ID", "previous_state", "changed_state",
				"table_name", "operation", "row_key", "created_at", "created_by");

		private static final Map<String, String> FOREIGNKEYRELATION = new HashMap<String, String>();

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = new ArrayList<com.zohocontacts.dbpojo.tabledesign.Table>();

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Audit_log";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum OauthSchema implements Table {
		ID("ID"), USERID("userID"), OAUTHPROVIDER("Oauth_provider"), SYNCSTATE("sync_state"),
		REFRESHTOKEN("refresh_token"), ACCESSTOKEN("access_token"), OAUTHSYNCTIME("last_sync_time"), EMAIL("email"),
		EXPIRYTIME("expiry_time"), CREATEDTIME("created_time"), MODIFIEDTIME("modified_time");

		private String columnName;

		private OauthSchema(String columnName) {
			this.columnName = columnName;
		}

		private static final List<String> COLUMNNAMES = Arrays.asList("ID", "userID", "Oauth_provider", "sync_state",
				"refresh_token", "access_token", "email", "expiry_time", "last_sync_time", "created_time",
				"modified_time");

		private static final Map<String, String> FOREIGNKEYRELATION = new HashMap<String, String>();

		private static final List<com.zohocontacts.dbpojo.tabledesign.Table> DELETECHILDTABLES = new ArrayList<com.zohocontacts.dbpojo.tabledesign.Table>();

		public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables() {

			return DELETECHILDTABLES;
		}

		public List<String> getColumns() {

			return COLUMNNAMES;
		}

		public String getForiegnKey(String TableName) {
			return FOREIGNKEYRELATION.get(TableName);
		}

		public String getColumnName() {

			return columnName;
		}

		public String getTableName() {
			return "Oauth";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

}
