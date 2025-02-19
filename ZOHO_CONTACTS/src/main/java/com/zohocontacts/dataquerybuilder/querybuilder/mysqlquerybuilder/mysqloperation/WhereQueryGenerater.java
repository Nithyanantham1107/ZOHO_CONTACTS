package com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.exception.QueryBuilderException;

public class WhereQueryGenerater {

	public static void executeQueryWhereBuilder(Table newData, StringBuilder query, Queue<Object> parameters)
			throws QueryBuilderException {
		try {

			if (newData.getID() != -1) {
				query.append(" WHERE");
				query.append(" " + newData.getTableName() + "." + newData.getPrimaryIDName() + " "
						+ Operation.EQUAL.getOperation() + "?");

				parameters.offer(newData.getID());
			} else {

				Queue<String> column = new LinkedList<String>();

				for (Map.Entry<String, Object> data : newData.getSettedData().entrySet()) {
					column.add(data.getKey());
					parameters.add(data.getValue());

				}

				if (column.size() != 0) {
					query.append(" WHERE");

					while (column.size() > 0) {

						query.append(" " + column.poll() + " " + Operation.EQUAL.getOperation() + "?");

						if (column.size() != 0)
							query.append(" and");

					}

				} else {

					System.out.println("throw the Exception for empty value to use in where cluse");

				}

			}

			query.append(";");

		} catch (Exception e) {
			throw new QueryBuilderException(e);
		}

	}

	public static void executeWhereBuilder(Table oldData, StringBuilder query, Queue<Object> parameters) {

		query.append(" WHERE");

		query.append(" " + oldData.getTableName() + "." + oldData.getPrimaryIDName() + " "
				+ Operation.EQUAL.getOperation() + "?");
		parameters.offer(oldData.getID());

		query.append(";");

	}

}
