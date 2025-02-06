package mysqloperation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import dbpojo.Category;
import dbpojo.ContactDetails;
import dbpojo.Table;
import dbpojo.Userdata;
import querybuilder.QueryExecuter;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.TableSchema;
import querybuilderconfig.TableSchema.Category_relation;
import querybuilderconfig.TableSchema.Contact_details;
import querybuilderconfig.TableSchema.Contact_mail;
import querybuilderconfig.TableSchema.Contact_phone;
import querybuilderconfig.TableSchema.Email_user;
import querybuilderconfig.TableSchema.JoinType;
import querybuilderconfig.TableSchema.Login_credentials;
import querybuilderconfig.TableSchema.Oauth;
import querybuilderconfig.TableSchema.Operation;
import querybuilderconfig.TableSchema.user_data;

public class SelectJoinerOperation {

	public static QueryBuilder selectTable(Table table, Connection con, QueryBuilder qg, StringBuilder query,
			Queue<Object> parameters) {
//		StringBuilder query = new StringBuilder();
//		Queue<Object> parameters = new LinkedList<Object>();

//		String TableName = table.getTableName();

		query.append("SELECT");

		query.append(" * ");
		query.append("FROM");

		query.append(" " + table.getTableName());

		SelectJoinerOperation.joinTable(query, table);
		WhereQueryGenerater.executeQueryWhereBuilder(table, query, parameters);
//		ArrayList<dbpojo.Table> result =   executeQuery( con, query.toString(), parameters, TableName);

		return qg;

	}

	public static ArrayList<dbpojo.Table> executeQuery(Connection con, String query, Queue<Object> parameters,
			String tableName) {

		ArrayList<dbpojo.Table> result = QueryExecuter.mySqlExecuteQuery(con, query, parameters, tableName);

		return result;

	}

	private static StringBuilder joinTable(StringBuilder query, Table table) {

		if (table instanceof Userdata) {

			Userdata userData = (Userdata) table;
			if (userData.getLoginCredentials() != null && userData.getLoginCredentials() != null) {

				join(query, JoinType.left, user_data.user_id, Operation.Equal, Login_credentials.log_id);

			}
			if (userData.getallemail() != null && userData.getallemail().size() != 0) {
				join(query, JoinType.left, user_data.user_id, Operation.Equal, Email_user.em_id);
			}
			if (userData.getallOauth() != null && userData.getallOauth().size() != 0) {
				join(query, JoinType.left, user_data.user_id, Operation.Equal, Oauth.userID);
			}

		} else if (table instanceof Category) {
			Category category = (Category) table;
			if (category.getCategoryRelation() != null && category.getCategoryRelation().size() != 0) {

				join(query, JoinType.left, querybuilderconfig.TableSchema.Category.Category_id, Operation.Equal,
						Category_relation.Category_id);

			}

		} else if (table instanceof ContactDetails) {

			ContactDetails contactDetails = (ContactDetails) table;
			if (contactDetails.getContactMail() != null) {
				join(query, JoinType.left, Contact_details.contact_id, Operation.Equal, Contact_mail.contact_id);

			}

			if (contactDetails.getContactphone() != null) {

				join(query, JoinType.left, Contact_details.contact_id, Operation.Equal, Contact_phone.contact_id);
			}

		}

		return null;

	}

	private static void join(StringBuilder query, TableSchema.JoinType jointype, querybuilderconfig.Table table1,
			TableSchema.Operation op, querybuilderconfig.Table table2) {
		query.append(" " + jointype.getType() + " " + table2.getTableName() + "  on  " + table1.getTableName() + "."
				+ table1 + "  " + op.getOperation() + " " + table2.getTableName() + "." + table2 + " ");

	}

}
