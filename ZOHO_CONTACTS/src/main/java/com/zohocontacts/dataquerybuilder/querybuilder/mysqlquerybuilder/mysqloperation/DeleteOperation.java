package com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.zohocontacts.dataquerybuilder.querybuilder.QueryExecuter;
import com.zohocontacts.dataquerybuilder.querybuilder.audit.AuditLogOperation;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.AuditLogSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OpType;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.dbpojo.tabledesign.TableWithChild;
import com.zohocontacts.exception.DBOperationException;

public class DeleteOperation {
	private static long userId = -1;

	public static Table deleteTable(QueryBuilder qg, Connection con, com.zohocontacts.dbpojo.tabledesign.Table table,
			StringBuilder query, Queue<Object> parameters)  {

		List<Table> data = qg.select(table).executeQuery();
		query.append("DELETE FROM " + table.getTableName() + " ");

		if (data.size() > 0) {

			table = data.getFirst();

			return table;

		} else {

			System.out.println("no value available for the table" + table.getTableName());
			return null;

		}

	}

	public static int[] execute(QueryBuilder qg, Connection con, String query, Queue<Object> parameters, Table oldData,
			long userID) {
		Queue<Object> values = new LinkedList<Object>();
		values.addAll(parameters);
		parameters.clear();
		if (oldData instanceof TableWithChild) {

			TableWithChild parentTable = (TableWithChild) oldData;
			DeleteOperation.deleteChildTable(qg, parentTable, userID);
		}

		System.out.println("here delete is " + query + "  then " + values);
		int[] data = QueryExecuter.mySqlExecuter(con, query, values, OpType.DELETE);

		if (data[0] == -1) {
			return null;
		}

		if (

		oldData != null && (!oldData.getTableName().equals(AuditLogSchema.ID.getTableName())
				&& !oldData.getTableName().equals(SessionSchema.ID.getTableName()))) {

			if (AuditLogOperation.audit(qg, oldData.getID(), oldData, null, OpType.DELETE, userID) == null) {
				System.out.println("Table" + oldData.getTableName() + "  is not audited");
			}

		}

		return data;
	}

	public static void deleteChildTable(QueryBuilder qg, TableWithChild table, long userID) {
		userId = userID;

		System.out.println("here delete table name" + table.getTableName());

		for (Table childTable : table.getDeleteChildTable()) {
			Map<String, Object> tableData = new HashMap<String, Object>();
			tableData.put(table.getForiegnkey(childTable.getTableName()), table.getID());
			Table deleteTable = childTable.getNewTable(tableData);
			auditall(qg, deleteTable);
		}

	}

	private static void auditall(QueryBuilder qg, Table table) {

		List<Table> tables = qg.select(table).executeQuery();
		if (tables.size() > 0) {

			for (Table tableData : tables) {
				System.out.println("here group ID " + tableData.getTableName() + "  then ID" + tableData.getID());

				AuditLogOperation.audit(qg, tableData.getID(), tableData, null, OpType.DELETE, userId);

			}

		}

	}

}
