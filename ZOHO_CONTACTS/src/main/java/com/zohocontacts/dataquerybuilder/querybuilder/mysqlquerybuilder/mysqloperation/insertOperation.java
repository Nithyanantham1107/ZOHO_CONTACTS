package com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation;

import java.sql.Connection;
import java.util.LinkedList;
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
import com.zohocontacts.exception.QueryBuilderException;

public class insertOperation {

	public static void insert(com.zohocontacts.dbpojo.tabledesign.Table table, StringBuilder query,
			Queue<Object> parameters) throws QueryBuilderException {

		try {

			Queue<String> columns = new LinkedList<String>();
			Table newData = table;
			query.append("INSERT INTO " + newData.getTableName() + " ");

			for (Map.Entry<String, Object> data : newData.getSettedData().entrySet()) {

				columns.add(data.getKey());
				parameters.add(data.getValue());

			}

			int length = parameters.size();

			System.out.println("here inside the insert method" + length + parameters);
			if (columns.size() != 0) {
				query.append("(" + " ");

				while (columns.size() > 0) {

					query.append(columns.poll());

					if (columns.size() != 0) {
						query.append(",");
					}

				}
				query.append(")" + " ");
			}

			query.append(" VALUES(" + " ");
			for (int i = 0; i < length; i++) {

				query.append("?");

				if (i != length - 1) {
					query.append(",");
				}

			}
			query.append(")" + " ;");

		} catch (Exception e) {
			throw new QueryBuilderException(e);
		}

	}

	public static int[] execute(QueryBuilder qg, Connection con, String query, Queue<Object> parameters, Table newData,
			long userID) throws QueryBuilderException {
		try {

			int id = -1;
			int[] data = QueryExecuter.mySqlExecuter(con, query, parameters, OpType.INSERT);

			newData.setID(data[1]);
			id = data[1];
			if (data[0] == -1) {
				return null;
			}

			if (data[0] != -1) {

				if (newData instanceof TableWithChild) {

					TableWithChild parentTable = (TableWithChild) newData;

					insertOperation.insertChildTable(qg, parentTable, userID);
				}

			}

			if (newData != null && (!newData.getTableName().equals(AuditLogSchema.ID.getTableName())
					&& !newData.getTableName().equals(SessionSchema.ID.getTableName()))) {

				if (AuditLogOperation.audit(qg, id, null, newData, OpType.INSERT, userID) == null) {
					System.out.println("Table" + newData.getTableName() + "  is not audited");
				}

			}

			return data;

		} catch (Exception e) {
			throw new QueryBuilderException(e);
		}

	}

	private static void insertChildTable(QueryBuilder qg, TableWithChild table, long userID) throws QueryBuilderException {
		for (Table childTable : table.getChildTables()) {
			qg.insert(childTable).execute(userID);

		}

	}

}
