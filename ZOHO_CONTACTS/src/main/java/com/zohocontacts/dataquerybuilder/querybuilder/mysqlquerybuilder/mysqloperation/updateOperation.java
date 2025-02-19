package com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.zohocontacts.dataquerybuilder.querybuilder.QueryExecuter;
import com.zohocontacts.dataquerybuilder.querybuilder.audit.AuditLogOperation;
import com.zohocontacts.dataquerybuilder.querybuilder.datahelper.changedStateContainer;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.AuditLogSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OpType;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.dbpojo.tabledesign.TableWithChild;
import com.zohocontacts.exception.QueryBuilderException;

public class updateOperation {

	public static changedStateContainer updateTable(QueryBuilder qg, Connection con, Table newData, StringBuilder query,
			Queue<Object> parameters) throws QueryBuilderException {

		try {

			Table oldData;
			Queue<String> columns = new LinkedList<String>();

			if (newData.getID() == -1) {

				System.out.println("Need to throw Exception for non id data");
				return null;
			}

			List<com.zohocontacts.dbpojo.tabledesign.Table> data = qg.select(newData).executeQuery();

			if (data.size() == 0) {

				System.out.println("throw Exception for not finding data");
				return null;
			} else {

				oldData = (com.zohocontacts.dbpojo.tabledesign.Table) data.getFirst();
			}
			changedStateContainer stateContainer = new changedStateContainer();
			stateContainer.setNewData(newData);
			stateContainer.setOldData(oldData);
			query.append("UPDATE " + newData.getTableName() + " " + "SET" + " ");
			for (Map.Entry<String, Object> table : newData.getSettedData().entrySet()) {

				if (!table.getKey().equals(newData.getPrimaryIDName())) {
					columns.add(table.getKey());

					parameters.add(table.getValue());

				}

			}

			if (columns.size() == 0) {

				System.out.println("here i need to throw error for no data is changed so no updation");
				return null;
			}
			while (columns.size() > 0) {

				query.append(columns.poll() + "=?");

				if (columns.size() != 0) {
					query.append(",");
				}

			}

			return stateContainer;

		} catch (Exception e) {
			throw new QueryBuilderException(e);
		}

	}

	private static void updateChildTable(QueryBuilder qg, TableWithChild table, long userID)
			throws QueryBuilderException {

		try {
			for (Table childTable : table.getChildTables()) {

				qg.update(childTable).execute(userID);
			}

		} catch (Exception e) {
			throw new QueryBuilderException(e);
		}

	}

	public static int[] execute(QueryBuilder qg, Connection con, String query, Queue<Object> parameters, Table newData,
			Table oldData, long userID) throws QueryBuilderException {

		try {

			int[] data = QueryExecuter.mySqlExecuter(con, query, parameters, OpType.UPDATE);

			if (data[0] != -1) {
				if (newData instanceof TableWithChild) {

					TableWithChild parentTable = (TableWithChild) newData;
					updateOperation.updateChildTable(qg, parentTable, userID);
				}

			}
			if ((newData != null && !newData.getTableName().equals(AuditLogSchema.ID.getTableName())) &&

					(oldData != null && !oldData.getTableName().equals(AuditLogSchema.ID.getTableName()))

					&& (newData != null && !newData.getTableName().equals(SessionSchema.ID.getTableName())) &&

					(oldData != null && !oldData.getTableName().equals(SessionSchema.ID.getTableName()))

			) {

				if (AuditLogOperation.audit(qg, oldData.getID(), oldData, newData, OpType.UPDATE, userID) == null) {
					System.out.println("Table" + newData.getTableName() + "  is not audited");
				}

			}

			return data;

		} catch (Exception e) {
			throw new QueryBuilderException(e);
		}

	}
}
