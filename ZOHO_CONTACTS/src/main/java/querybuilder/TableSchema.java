package querybuilder;

import java.lang.classfile.Interfaces;

public class TableSchema {
	public void care() {
		
		System.out.print("hjgdcu");
	}


	public enum Category_relation implements Table {
		contact_id_to_join, Category_id;

		public String getTableName() {
			return "Category_relation";
		}

	}

	public enum Category implements Table {
		Category_id, Category_name, created_by;

		public String getTableName() {
			return "Category";
		}

	}

	public enum Contact_details implements Table {
		user_id, contact_id, First_name, Middle_name, Last_name, gender, Address, created_At;

		public String getTableName() {
			return "Contact_details";
		}

	}

	public enum Contact_mail implements Table {
		contact_id, Contact_email_id;

		public String getTableName() {
			return "Contact_mail";
		}

	}

	public enum Contact_phone implements Table {
		contact_id, contact_phone_no;

		public String getTableName() {
			return "Contact_phone";
		}

	}

	public enum Email_user implements Table {
		em_id, email, is_primary;

		public String getTableName() {
			return "Email_user";
		}

	}

	public enum Login_credentials implements Table {
		id, username;

		public String getTableName() {
			return "Login_credentials";
		}

	}

	public enum Session implements Table {
		Session_id, session_expire, user_id;

		public String getTableName() {
			return "Session";
		}

	}

	public enum user_data implements Table {
		user_id, Name, password, phone_no, address, timezone;

		public String getTableName() {
			return "user_data";
		}

	}

	public enum tables implements Table {

		Category("Category"), Category_relation("Category_relation"), Contact_details("Contact_details"),
		Contact_mail("Contact_mail"), Contact_phone("Contact_phone"), Email_user("Email_user"),
		Login_credentials("Login_credentials"), Session("Session"), user_data("user_data");

		private final String tableName;

		private tables(String tableName) {
			this.tableName = tableName;
		}

		public String getTableName() {
			return tableName;
		}

	}
}

//import java.util.*;
//
//public class TableSchema {
//	
//	private static  Map<String, Set<String>> tableColumn = new HashMap<String, Set<String>>();
//	static Map<String, Set<String>> dbTable() {
//		
//		
//	
//		
//		Set<String> Category = new HashSet<>(Arrays.asList("Category_id", "Category_name", "created_by"));
//		Set<String> Category_relation = new HashSet<>(Arrays.asList("contact_id_to_join", "Category_id"));
//		Set<String> Contact_details = new HashSet<>(Arrays.asList("user_id", "contact_id", "First_name", "Middle_name",
//				"Last_name", "gender", "Address", "created_At"));
//		Set<String> Contact_mail = new HashSet<>(Arrays.asList("contact_id", "Contact_email_id"));
//		Set<String> Contact_phone = new HashSet<>(Arrays.asList("contact_id", "contact_phone_no"));
//		Set<String> Email_user = new HashSet<>(Arrays.asList("em_id", "email", "is_primary"));
//		Set<String> Login_credentials = new HashSet<>(Arrays.asList("id", "username"));
//		Set<String> Session = new HashSet<>(Arrays.asList("Session_id", "session_expire", "user_id"));
//		Set<String> user_data = new HashSet<>(
//				Arrays.asList("user_id", "Name", "password", "phone_no", "address", "timezone"));
//
//		tableColumn.put("Category", Category);
//		tableColumn.put("Category_relation", Category_relation);
//		tableColumn.put("Contact_details", Contact_details);
//		tableColumn.put("Contact_mail", Contact_mail);
//		tableColumn.put("Contact_phone", Contact_phone);
//		tableColumn.put("Email_user", Email_user);
//		tableColumn.put("Login_credentials", Login_credentials);
//		tableColumn.put("Session", Session);
//		tableColumn.put("user_data", user_data);
//		
//		
//		return tableColumn;
//	}
//
//}
