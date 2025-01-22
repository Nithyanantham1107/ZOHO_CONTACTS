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
import mysqloperation.insertOperation;
import mysqloperation.updateOperation;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.TableSchema.OpType;

public class MysqlBuilder implements QueryBuilder {
	private StringBuilder query = new StringBuilder();
	private dbpojo.Table newData;
	private String tableName;
	private Connection con = null;
	private Boolean isValid = true;
	private dbpojo.Table oldData;
	private OpType opType;
	private Queue<Object> parameters = new LinkedList<Object>();

	public MysqlBuilder() throws SQLException {
//		con = DBconnection.getConnection();
		con = DBconnection.getDriverConnection();
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

	public ArrayList<dbpojo.Table> executeQuery() {

		ArrayList<dbpojo.Table> result = SelectJoinerOperation.executeQuery(con, query.toString(), parameters,
				tableName);

		query.setLength(0);
		return result;

	}
	public int[] execute(int userID) {
		int[] result = { -1, -1 };

		if (!isValid) {

			return result;
		}

		if (this.opType.getOpType().equals(OpType.INSERT.getOpType())) {

			result = insertOperation.execute(this, con, query.toString(), parameters, newData, userID);

		} else if (this.opType.getOpType().equals(OpType.UPDATE.getOpType())) {

			result = updateOperation.execute(this, con, query.toString(), parameters, newData, oldData, userID);
		} else if (this.opType.getOpType().equals(OpType.DELETE.getOpType())) {

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
