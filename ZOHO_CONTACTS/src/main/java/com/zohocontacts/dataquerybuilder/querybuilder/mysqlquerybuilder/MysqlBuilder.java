package com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.zohocontacts.dataquerybuilder.querybuilder.datahelper.changedStateContainer;
import com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation.DeleteOperation;
import com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation.SelectJoinerOperation;
import com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation.WhereQueryGenerater;
import com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation.insertOperation;
import com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.mysqloperation.updateOperation;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.Table;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OpType;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;
import com.zohocontacts.dbconnect.DBconnection;
import com.zohocontacts.exception.DBOperationException;

public class MysqlBuilder implements QueryBuilder {
	private StringBuilder query = new StringBuilder();
	private com.zohocontacts.dbpojo.tabledesign.Table newData;
	private String tableName;
	private Connection con = null;
	private Boolean isValid = true;
	private Boolean isWhereAdded = false;
	private com.zohocontacts.dbpojo.tabledesign.Table oldData;
	private OpType opType;
	private Queue<Object> parameters = new LinkedList<Object>();

	public MysqlBuilder() throws SQLException {
		con = DBconnection.getConnection();
//		con=DBconnection.getDriverConnection();
		System.out.println("Query is  in mysql ");

	};

	@Override
	public void close() {
		closeConnection();

	}

	@Override
	public void openConnection() {
		try {
			this.con.setAutoCommit(false);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void closeConnection() {
		try {
			this.con.commit();
			this.con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void rollBackConnection() {
		try {
			this.con.rollback();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void commit() {
		try {
			this.con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public QueryBuilder select(com.zohocontacts.dbpojo.tabledesign.Table table) {

		query.setLength(0);
		newData = table;
		tableName = table.getTableName();
		SelectJoinerOperation.selectTable(table, con, this, query, parameters);

		return this;

	}

	@Override
	public QueryBuilder delete(com.zohocontacts.dbpojo.tabledesign.Table table) {
		opType = OpType.DELETE;

		query.setLength(0);

		oldData = DeleteOperation.deleteTable(this, con, table, query, parameters);

		if (oldData == null) {
			isValid = false;
		} else {
			isValid = true;
		}

		return this;
	}

	@Override
	public QueryBuilder insert(com.zohocontacts.dbpojo.tabledesign.Table table) {
		opType = OpType.INSERT;
		isValid = true;
		query.setLength(0);
		insertOperation.insert(table, query, parameters);
		newData = table;
		return this;
	}

	public QueryBuilder update(com.zohocontacts.dbpojo.tabledesign.Table table) {

		opType = OpType.UPDATE;
		query.setLength(0);

		changedStateContainer changedState = updateOperation.updateTable(this, con, table, query, parameters);
		if (changedState == null) {
			isValid = false;
		} else {
			newData = changedState.getnewData();
			oldData = changedState.getOldData();
			isValid = true;
		}

		return this;
	}

	public QueryBuilder where(Table column, Operation operation, Object value) {
		query.append(" WHERE ");

		query.append(column.getColumnName()).append(" ").append(operation.getOperation()).append(" ")
				.append(formatValue(value));

		isWhereAdded = true;
		return this;
	}

	public QueryBuilder and(Table column, Operation operation, Object value) {

		query.append(" AND ");

		query.append(column.getColumnName()).append(" ").append(operation.getOperation()).append(" ")
				.append(formatValue(value));

		return this;
	}

	public QueryBuilder or(Table column, Operation operator, Object value) {
		query.append(" OR ");
		query.append(column.getColumnName()).append(" ").append(operator).append(" ").append(formatValue(value));

		return this;
	}

	private String formatValue(Object value) {
		if (value instanceof String) {
			return "'" + value + "'";
		} else {
			return value.toString();
		}
	}

	public List<com.zohocontacts.dbpojo.tabledesign.Table> executeQuery() {
		if (!isWhereAdded) {

			WhereQueryGenerater.executeQueryWhereBuilder(newData, query, parameters);

		} else {
			query.append(";");
		}

		List<com.zohocontacts.dbpojo.tabledesign.Table> result = SelectJoinerOperation.executeQuery(con, query.toString(), parameters, newData);

		query.setLength(0);
		isWhereAdded = false;
		return result;

	}

	public int[] execute(long userID) {
		int[] result = { -1, -1 };
		if (!isValid) {

			return result;
		}

		if (this.opType.getOpType().equals(OpType.INSERT.getOpType())) {

			if (isWhereAdded) {

				query.append(";");
			}

			result = insertOperation.execute(this, con, query.toString(), parameters, newData, userID);

		} else if (this.opType.getOpType().equals(OpType.UPDATE.getOpType())) {

			if (!isWhereAdded) {

				WhereQueryGenerater.executeWhereBuilder(newData, query, parameters);

			} else {
				query.append(";");
			}

			result = updateOperation.execute(this, con, query.toString(), parameters, newData, oldData, userID);
		} else if (this.opType.getOpType().equals(OpType.DELETE.getOpType())) {

			if (!isWhereAdded) {

				WhereQueryGenerater.executeWhereBuilder(oldData, query, parameters);

			} else {
				query.append(";");
			}

			result = DeleteOperation.execute(this, con, query.toString(), parameters, oldData, userID);
		}
		query.setLength(0);
		isWhereAdded = false;
		return result;
	}

	public String make() {
		this.query.append(";");
		System.out.println("generated query upto select is :" + this.query);

		this.tableName = null;
		this.parameters.clear();
		this.query.setLength(0);

		return this.query.toString();
	}

}
