package querybuilderconfig;

public class TableSchema {

	public enum Statement {

		RETURN_GENERATED_KEYS;
		
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

		public  String getOpType() {
			return this.type;
		}

	}

	public enum Operation {
		Equal("="), Notequal("!="), GreaterThan(">"), LesserThan("<"), GreaterEqual(">="), LesserEqual("<=");

		String value;

		Operation(String val) {

			this.value = val;
		}

		public String getOperation() {
			return this.value;
		}
	}

	public enum JoinType {
		left("LEFT JOIN"), right("RIGHT JOIN"), inner("INNER JOIN"), outer("OUTER JOIN");

		String value;

		JoinType(String val) {

			this.value = val;
		}

		public String getType() {
			return this.value;
		}
	}

	public enum Category_relation implements Table {
		ID, contact_id_to_join, Category_id, created_time, modified_time;

		public String getTableName() {
			return "Category_relation";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum Category implements Table {
		Category_id, Category_name, created_by, created_time, modified_time;

		public String getTableName() {
			return "Category";
		}

		public String getPrimaryKey() {

			return "Category_id";
		}

	}

	public enum Contact_details implements Table {
		user_id, contact_id, First_name, Middle_name, Last_name, gender, Address, created_time, modified_time;

		public String getTableName() {
			return "Contact_details";
		}

		public String getPrimaryKey() {

			return "contact_id";
		}

	}

	public enum Contact_mail implements Table {
		ID, contact_id, Contact_email_id, created_time, modified_time;

		public String getTableName() {
			return "Contact_mail";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum Contact_phone implements Table {
		ID, contact_id, Contact_phone_no, created_time, modified_time;

		public String getTableName() {
			return "Contact_phone";
		}

		@Override
		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum Email_user implements Table {
		ID, em_id, email, is_primary, created_time, modified_time;

		public String getTableName() {
			return "Email_user";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum Login_credentials implements Table {
		ID, log_id, username, created_time, modified_time;

		public String getTableName() {
			return "Login_credentials";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum Session implements Table {
		ID, Session_id, last_accessed, user_id, created_time, modified_time;

		public String getTableName() {
			return "Session";
		}

		public String getPrimaryKey() {

			return "ID";
		}

	}

	public enum user_data implements Table {
		user_id, Name, password, phone_no, address, timezone, created_time, modified_time;

		public String getTableName() {
			return "user_data";
		}

		public String getPrimaryKey() {

			return "user_id";
		}

	}

	public enum Audit_log implements Table {

		ID, previous_state, changed_state, table_name, operation, row_key, created_at, created_by;

		
		public String getTableName() {
			return "Audit_log";
		}

		public String getPrimaryKey() {

			return "ID";
		}
		
		
	}

	public enum tables implements Table {

		Category("Category", "Category_id"), Category_relation("Category_relation", "ID"),
		Contact_details("Contact_details", "contact_id"), Contact_mail("Contact_mail", "ID"),
		Contact_phone("Contact_phone", "ID"), Email_user("Email_user", "ID"),
		Login_credentials("Login_credentials", "ID"), Session("Session", "ID"),
		user_data("user_data", "user_id"),
		Audit_log("Audit_log", "ID");

		private final String tableName;
		private final String primarykey;

		private tables(String tableName, String primarykey) {
			this.tableName = tableName;
			this.primarykey = primarykey;
		}

		public String getTableName() {
			return tableName;
		}

		public String getPrimaryKey() {
			return primarykey;
		}

	}
}
