package com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Queue;

import com.zohocontacts.dataquerybuilder.querybuilder.QueryExecuter;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.JoinType;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.dbpojo.tabledesign.TableWithChild;

public class SelectJoinerOperation {

	public static QueryBuilder selectTable(Table table, Connection con, QueryBuilder qg, StringBuilder query,
			Queue<Object> parameters) {

		query.append("SELECT");

		query.append(" * ");
		query.append("FROM");

		query.append(" " + table.getTableName());

		if (table instanceof TableWithChild) {

			TableWithChild parentTable = (TableWithChild) table;
			SelectJoinerOperation.joinTable(query, parentTable);
		}

		return qg;

	}

	public static ArrayList<com.zohocontacts.dbpojo.tabledesign.Table> executeQuery(Connection con, String query, Queue<Object> parameters,
			Table table) {

		ArrayList<com.zohocontacts.dbpojo.tabledesign.Table> result = QueryExecuter.mySqlExecuteQuery(con, query, parameters, table);

		return result;

	}

//	public static ArrayList<dbpojo.Table> executeQuery(Connection con, String query, Queue<Object> parameters,
//			String tableName) {
//
//		ArrayList<dbpojo.Table> result = QueryExecuter.mySqlExecuteQuery(con, query, parameters, tableName);
//
//		return result;
//
//	}

	private static void joinTable(StringBuilder query, TableWithChild table) {
		ArrayList<Table> uniqueChildList = new ArrayList<Table>();

		for (Table child : table.getChildTables()) {
			Boolean isExist = false;
			for (Table tables : uniqueChildList) {

				if (child.getTableName().equals(tables.getTableName())) {
					isExist = true;
					break;
				}
			}

			if (!isExist) {

				uniqueChildList.add(child);
			}

		}

		for (Table childTable : uniqueChildList) {

			joinTable(query, JoinType.LEFT, table.getTableName(), table.getPrimaryIDName(), Operation.EQUAL,
					childTable.getTableName(), table.getForiegnkey(childTable.getTableName()));
		}

	}

//	private static StringBuilder joinTable(StringBuilder query, Table table) {
//
//		if (table instanceof Userdata) {
//
//			Userdata userData = (Userdata) table;
//			if (userData.getLoginCredentials() != null && userData.getLoginCredentials() != null) {
//
//				join(query, JoinType.LEFT, UserDataSchema.USERID, Operation.EQUAL, LoginCredentialsSchema.LOGID);
//
//			}
//			if (userData.getallemail() != null && userData.getallemail().size() != 0) {
//				join(query, JoinType.LEFT, UserDataSchema.USERID, Operation.EQUAL, EmailUserSchema.EMAILID);
//			}
//			if (userData.getallOauth() != null && userData.getallOauth().size() != 0) {
//				join(query, JoinType.LEFT, UserDataSchema.USERID, Operation.EQUAL, OauthSchema.USERID);
//			}
//
//		} else if (table instanceof Category) {
//			Category category = (Category) table;
//			if (category.getCategoryRelation() != null && category.getCategoryRelation().size() != 0) {
//
//				join(query, JoinType.LEFT, querybuilderconfig.TableSchema.CategorySchema.CATEGORYID, Operation.EQUAL,
//						CategoryRelationSchema.CATEGORYID);
//
//			}
//
//		} else if (table instanceof ContactDetails) {
//
//			ContactDetails contactDetails = (ContactDetails) table;
//			if (contactDetails.getContactMail() != null) {
//				join(query, JoinType.LEFT, ContactDetailsSchema.CONTACTID, Operation.EQUAL, ContactMailSchema.CONTACTID);
//
//			}
//
//			if (contactDetails.getContactphone() != null) {
//
//				join(query, JoinType.LEFT, ContactDetailsSchema.CONTACTID, Operation.EQUAL, ContactPhoneSchema.CONTACTID);
//			}
//
//		}
//
//		return null;
//
//	}

	private static void joinTable(StringBuilder query, TableSchema.JoinType jointype, String parentTableName,
			String parentTableKEy, TableSchema.Operation op, String childTableName, String ChildTableKEy) {
		query.append(" " + jointype.getType() + " " + childTableName + "  on  " + parentTableName + "." + parentTableKEy
				+ "  " + op.getOperation() + " " + childTableName + "." + ChildTableKEy + " ");

	}

//	private static void join(StringBuilder query, TableSchema.JoinType jointype, querybuilderconfig.Table table1,
//			TableSchema.Operation op, querybuilderconfig.Table table2) {
//		query.append(" " + jointype.getType() + " " + table2.getTableName() + "  on  " + table1.getTableName() + "."
//				+ table1 + "  " + op.getOperation() + " " + table2.getTableName() + "." + table2 + " ");
//
//	}

}
