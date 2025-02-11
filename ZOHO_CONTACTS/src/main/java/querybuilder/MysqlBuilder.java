package querybuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import datahelper.changedStateContainer;
import dbconnect.DBconnection;
import mysqloperation.DeleteOperation;
import mysqloperation.SelectJoinerOperation;
import mysqloperation.WhereQueryGenerater;
import mysqloperation.insertOperation;
import mysqloperation.updateOperation;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.Table;
import querybuilderconfig.TableSchema.OpType;
import querybuilderconfig.TableSchema.Operation;

public class MysqlBuilder implements QueryBuilder {
	private StringBuilder query = new StringBuilder();
	private dbpojo.Table newData;
	private String tableName;
	private Connection con = null;
	private Boolean isValid = true;
	private Boolean isWhereAdded = false;
	private dbpojo.Table oldData;
	private OpType opType;
	private Queue<Object> parameters = new LinkedList<Object>();

	public MysqlBuilder() throws SQLException {
		con = DBconnection.getConnection();
//		con=DBconnection.getDriverConnection();
		System.out.println("Query is  in mysql ");

	};

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
	public QueryBuilder select(dbpojo.Table table) {

		query.setLength(0);
		newData=table;
		tableName = table.getTableName();
		SelectJoinerOperation.selectTable(table, con, this, query, parameters);

		return this;

	}

	@Override
	public QueryBuilder delete(dbpojo.Table table) {
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
	public QueryBuilder insert(dbpojo.Table table) {
		opType = OpType.INSERT;
		isValid = true;
		query.setLength(0);
		insertOperation.insert(table, query, parameters);
		newData = table;
		return this;
	}

	public QueryBuilder update(dbpojo.Table table) {

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

		query.append(column).append(" ").append(operation.getOperation()).append(" ").append(formatValue(value));

		isWhereAdded = true;
		return this;
	}

	public QueryBuilder and(Table column, Operation operation, Object value) {

		query.append(" AND ");

		query.append(column).append(" ").append(operation.getOperation()).append(" ").append(formatValue(value));

		return this;
	}

	public QueryBuilder or(Table column, Operation operator, Object value) {
		query.append(" OR ");
		query.append(column).append(" ").append(operator).append(" ").append(formatValue(value));

		return this;
	}

	private String formatValue(Object value) {
		if (value instanceof String) {
			return "'" + value + "'";
		} else {
			return value.toString();
		}
	}

	public ArrayList<dbpojo.Table> executeQuery() {

		ArrayList<dbpojo.Table> result = SelectJoinerOperation.executeQuery(con, query.toString(), parameters,
				newData);

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

			if (!isWhereAdded ) {

					isWhereAdded = false;

			} else {
				query.append(";");
			}

			result = insertOperation.execute(this, con, query.toString(), parameters, newData, userID);

		} else if (this.opType.getOpType().equals(OpType.UPDATE.getOpType())) {

			if (!isWhereAdded ) {

				WhereQueryGenerater.executeWhereBuilder(newData, query, parameters);
				isWhereAdded = false;

			} else {
				query.append(";");
			}

			result = updateOperation.execute(this, con, query.toString(), parameters, newData, oldData, userID);
		} else if (this.opType.getOpType().equals(OpType.DELETE.getOpType())) {

			if (!isWhereAdded ) {

				WhereQueryGenerater.executeWhereBuilder(oldData, query, parameters);
				isWhereAdded = false;

			} else {
				query.append(";");
			}

			result = DeleteOperation.execute(this, con, query.toString(), parameters, oldData, userID);
		}
		query.setLength(0);

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
