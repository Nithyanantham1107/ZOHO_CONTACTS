package mysqloperation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import audit.AuditLogOperation;
import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Table;
import dbpojo.Userdata;
import querybuilder.QueryExecuter;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.TableSchema.OpType;
import querybuilderconfig.TableSchema.tables;

public class DeleteOperation {
	private static int userId = -1;

	public static Table deleteTable(QueryBuilder qg, Connection con, dbpojo.Table table, StringBuilder query,
			Queue<Object> parameters) {

		ArrayList<Table> data = qg.select(table).executeQuery();
		query.append("DELETE FROM " + table.getTableName() + " ");

		if (data.size() > 0) {

			table = data.getFirst();

			WhereQueryGenerater.executeWhereBuilder(table, query, parameters);
			return table;

		} else {

			System.out.println("no value available for the table" + table.getTableName());
			return null;

		}

	}

	public static int[] execute(QueryBuilder qg, Connection con, String query, Queue<Object> parameters, Table oldData,
			int userID) {
		Queue<Object> values = new LinkedList<Object>();
		values.addAll(parameters);
		parameters.clear();

		DeleteOperation.deleteChildTable(qg, oldData, userID);
		System.out.println("here delete is " + query + "  then " + values);
		int[] data = QueryExecuter.mySqlExecuter(con, query, values, OpType.DELETE);

		if (data[0] == -1) {
			return null;
		}

		if (

		oldData != null && !oldData.getTableName().equals(tables.Audit_log.getTableName())) {

			if (AuditLogOperation.audit(qg, oldData.getID(), oldData, null, OpType.DELETE, userID) == null) {
				System.out.println("Table" + oldData.getTableName() + "  is not audited");
			}

		}

		return data;
	}

	public static void deleteChildTable(QueryBuilder qg, Table table, int userID) {
		userId = userID;

		System.out.println("here delete table name" + table.getTableName());
		if (table instanceof Userdata) {
			Userdata userData = (Userdata) table;
			EmailUser email = new EmailUser();
			ContactDetails contact = new ContactDetails();
			Category category = new Category();
			dbpojo.Session session = new dbpojo.Session();
			LoginCredentials login = new LoginCredentials();
			email.setEmailID(userID);
			login.setUserID(userData.getID());
			contact.setUserID(userData.getID());
			category.setCreatedBY(userData.getID());
			session.setUserId(userData.getID());
			auditOne(qg, login);
			auditall(qg, email);
			auditall(qg, category);
			auditall(qg, contact);
			auditall(qg, session);

		} else if (table instanceof Category) {
			Category category = (Category) table;
			CategoryRelation categoryRelation = new CategoryRelation();
			categoryRelation.setCategoryID(category.getID());
			auditall(qg, categoryRelation);

		} else if (table instanceof ContactDetails) {

			ContactDetails contactDetail = (ContactDetails) table;
			ContactMail contactMail = new ContactMail();
			ContactPhone contactPhone = new ContactPhone();
			CategoryRelation categoryRelation = new CategoryRelation();
			categoryRelation.setContactIDtoJoin(contactDetail.getID());
			contactMail.setContactID(contactDetail.getID());
			contactPhone.setContactID(contactDetail.getID());
			auditOne(qg, contactMail);
			auditOne(qg, contactPhone);
			auditall(qg, categoryRelation);
		}

	}

	private static void auditOne(QueryBuilder qg, Table table) {

		ArrayList<Table> tables = qg.select(table).executeQuery();
		if (tables.size() > 0) {
			AuditLogOperation.audit(qg, table.getID(), tables.getFirst(), null, OpType.DELETE, userId);
		}

	}

	private static void auditall(QueryBuilder qg, Table table) {

		ArrayList<Table> tables = qg.select(table).executeQuery();
		if (tables.size() > 0) {

			for (Table tableData : tables) {
				System.out.println("here group ID " + tableData.getTableName() + "  then ID" + tableData.getID());

				AuditLogOperation.audit(qg, tableData.getID(), tableData, null, OpType.DELETE, userId);

			}

		}

	}

}
