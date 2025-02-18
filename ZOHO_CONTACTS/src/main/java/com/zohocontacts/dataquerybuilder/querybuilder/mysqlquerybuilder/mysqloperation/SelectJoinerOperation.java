package com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.zohocontacts.dataquerybuilder.querybuilder.QueryExecuter;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.JoinType;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.dbpojo.tabledesign.TableWithChild;
import com.zohocontacts.exception.DBOperationException;

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

	public static List<com.zohocontacts.dbpojo.tabledesign.Table> executeQuery(Connection con, String query,
			Queue<Object> parameters, Table table)  {

		List<com.zohocontacts.dbpojo.tabledesign.Table> result = QueryExecuter.mySqlExecuteQuery(con, query, parameters,
				table);

		return result;

	}

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

	private static void joinTable(StringBuilder query, TableSchema.JoinType jointype, String parentTableName,
			String parentTableKEy, TableSchema.Operation op, String childTableName, String ChildTableKEy) {
		query.append(" " + jointype.getType() + " " + childTableName + "  on  " + parentTableName + "." + parentTableKEy
				+ "  " + op.getOperation() + " " + childTableName + "." + ChildTableKEy + " ");

	}

}
