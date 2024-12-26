package querybuilder;



public class TableSchema {
	
	

	
	public enum Statement{
		
		
		
		RETURN_GENERATED_KEYS;
	}
	
	

	public enum Operation {
		Equal("="), Notequal("!="), GreaterThan(">"), LesserThan("<"), GreaterEqual(">="), LesserEqual("<=");

		String value;

		Operation(String val) {

			this.value = val;
		}

		String getOperation() {
			return this.value;
		}
	}
	
	
	
	public enum JoinType {
		left("LEFT JOIN"), right("RIGHT JOIN"), inner("INNER JOIN"), outer("OUTER JOIN");

		String value;

		JoinType(String val) {

			this.value = val;
		}

		String getType() {
			return this.value;
		}
	}

	public enum Category_relation implements Table {
		contact_id_to_join, Category_id;

		public String getTableName() {
			return "Category_relation";
		}
	
		public String getPrimaryKey() {
		
			return "Category_id";
		}
		

	}

	public enum Category implements Table {
		Category_id, Category_name, created_by;

		public String getTableName() {
			return "Category";
		}
		
		public String getPrimaryKey() {
			
			return "Category_id";
		}

	}

	public enum Contact_details implements Table {
		user_id, contact_id, First_name, Middle_name, Last_name, gender, Address, created_At;

		public String getTableName() {
			return "Contact_details";
		}
	
		public String getPrimaryKey() {
			
			return "contact_id";
		}

	}

	public enum Contact_mail implements Table {
		contact_id, Contact_email_id;

		public String getTableName() {
			return "Contact_mail";
		}
	
		public String getPrimaryKey() {
			
			return "contact_id";
		}

	}

	public enum Contact_phone implements Table {
		contact_id, Contact_phone_no;

		public String getTableName() {
			return "Contact_phone";
		}
		@Override
		public String getPrimaryKey() {
		
			return "contact_id";
		}

	}

	public enum Email_user implements Table {
		em_id, email, is_primary;

		public String getTableName() {
			return "Email_user";
		}
	
		public String getPrimaryKey() {
			
			return "email";
		}

	}

	public enum Login_credentials implements Table {
		id, username;

		public String getTableName() {
			return "Login_credentials";
		}
	
		public String getPrimaryKey() {
			
			return "username";
		}

	}

	public enum Session implements Table {
		Session_id, last_accessed, user_id;

		public String getTableName() {
			return "Session";
		}
		
		public String getPrimaryKey() {
		
			return "Session_id";
		}

	}

	public enum user_data implements Table {
		user_id, Name, password, phone_no, address, timezone;

		public String getTableName() {
			return "user_data";
		}
		
	
		public String getPrimaryKey() {
			
			return "user_id";
		}

	}

	public enum tables implements Table {

		Category("Category","Category_id"), Category_relation("Category_relation"," Category_id"), Contact_details("Contact_details","contact_id"),
		Contact_mail("Contact_mail","contact_id"), Contact_phone("Contact_phone","contact_id"), Email_user("Email_user","email"),
		Login_credentials("Login_credentials","username"), Session("Session","Session_id"), user_data("user_data","user_id");

		private final String tableName;
		private final String primarykey;

		private tables(String tableName,String primarykey) {
			this.tableName = tableName;
			this.primarykey=primarykey;
		}

		public String getTableName() {
			return tableName;
		}
		public String getPrimaryKey() {
			return primarykey;
		}

	}
}


