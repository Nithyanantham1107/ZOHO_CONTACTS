package querybuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import audit.AuditLogOperation;
import datahelper.PojoDataContainer;
import datahelper.PojoDataConversion;
import datahelper.changedStateContainer;
import dbconnect.DBconnection;
import mysqloperation.SelectJoinerOperation;
import mysqloperation.DeleteOperation;
import mysqloperation.insertOperation;
import mysqloperation.updateOperation;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.TableSchema.OpType;
import querybuilderconfig.TableSchema.Operation;
import querybuilderconfig.TableSchema.tables;

public class MysqlBuilder implements QueryBuilder {
	private StringBuilder query = new StringBuilder();
	private dbpojo.Table newData;
	private String TableName;
	private Connection con = null;
	private int id = -1;

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

		this.TableName = table.getTableName();
		this.newData = table;

		this.query.append("SELECT");

		this.query.append(" * ");
		this.query.append("FROM");

		query.append(" " + this.TableName);

		SelectJoinerOperation.joinTable(query, this.newData);

		return this;
	}

	@Override
	public QueryBuilder delete(dbpojo.Table table) {
		this.opType = OpType.DELETE;
		this.oldData = table;
		this.newData = null;
		this.id = table.getID();

		this.TableName = table.getTableName();

		this.query.append("DELETE FROM " + table.getTableName() + " ");

		return this;
	}

	@Override
	public QueryBuilder insert(dbpojo.Table table) {
		this.opType = OpType.INSERT;
		this.newData = table;
		this.oldData = null;
		this.TableName = this.newData.getTableName();
		this.query.append("INSERT INTO " + this.newData.getTableName() + " ");
		PojoDataContainer pojoDataContainer = PojoDataConversion.convertPojoData(this.newData);
		Queue<String> columns = pojoDataContainer.getPojoKey();
		this.parameters = pojoDataContainer.getPojoValue();
		int length = this.parameters.size();
		if (columns.size() != 0) {
			this.query.append("(" + " ");

			while (columns.size() > 0) {

				this.query.append(columns.poll());

				if (columns.size() != 0) {
					this.query.append(",");
				}

			}
			this.query.append(")" + " ");
		}

		this.query.append(" VALUES(" + " ");
		for (int i = 0; i < length; i++) {

			this.query.append("?");

			if (i != length - 1) {
				this.query.append(",");
			}

		}
		this.query.append(")" + " ");

		return this;
	}

	public QueryBuilder update(dbpojo.Table table) {
		this.opType = OpType.UPDATE;
		this.newData = table;

		this.TableName = this.newData.getTableName();

		if (newData.getID() == -1) {

			System.out.println("Need to throw Exception for non id data");
			return null;
		}

		ArrayList<dbpojo.Table> data = this.select(newData).executeQuery();

		if (data.size() == 0) {

			System.out.println("throw Exception for not finding data");
			return null;
		} else {

			this.oldData = (dbpojo.Table) data.getFirst();
		}

		changedStateContainer changedState = PojoDataConversion.getChangedPojoData(this.newData, this.oldData);
		this.id = changedState.getID();
		this.newData = changedState.getnewData();
		this.oldData = changedState.getOldData();

		this.query.append("UPDATE " + this.newData.getTableName() + " " + "SET" + " ");

		PojoDataContainer pojoDataContainer = PojoDataConversion.convertPojoData(this.newData);
		System.out.println(pojoDataContainer.getJson());
		Queue<String> columns = pojoDataContainer.getPojoKey();
		if (columns.size() == 0) {

			System.out.println("here i need to throw error for no data is changed so no updation");
			return null;
		}
		while (columns.size() > 0) {

			this.query.append(columns.poll() + "=?");

			if (columns.size() != 0) {
				this.query.append(",");
			}

		}

		this.parameters = pojoDataContainer.getPojoValue();

		return this;
	}

	public ArrayList<dbpojo.Table> executeQuery() {

		executeQueryWhereBuilder();

		ArrayList<dbpojo.Table> result = QueryExecuter.mySqlExecuteQuery(con, this.query, parameters, TableName);

		return result;

	}

	public int[] execute(int userID) {

		executeWhereBuilder();

		Queue<Object> values = this.parameters;

		dbpojo.Table newData = this.newData;
		dbpojo.Table oldData = this.oldData;
		int id = this.id;
		String query = this.query.toString();
		OpType opType = this.opType;
		this.query.setLength(0);

		if (opType.getOpType().equals(OpType.DELETE.getOpType())) {

			DeleteOperation.deleteChildTable(this, oldData, userID);
		}

		int[] data = QueryExecuter.mySqlExecuter(con, query, values, opType);
		if (opType.getOpType().equals(OpType.INSERT.getOpType())) {

			newData.setID(data[1]);
			id = data[1];

		}
		if (data[0] == -1) {
			return null;
		}

		if (opType.getOpType().equals(OpType.INSERT.getOpType()) && data[0] != -1) {

			insertOperation.insertChildTable(this, newData, userID);

		}

		if (opType.getOpType().equals(OpType.UPDATE.getOpType()) && data[0] != -1) {

			updateOperation.updateChildTable(this, newData, userID);
		}

		if ((newData != null && !newData.getTableName().equals(tables.Audit_log.getTableName())) ||

				(oldData != null && !oldData.getTableName().equals(tables.Audit_log.getTableName()))) {

			if (AuditLogOperation.audit(this, id, oldData, newData, opType, userID) == null) {
				System.out.println("Table" + newData.getTableName() + "  is not audited");
			}

		}

		return data;
	}

	private void executeQueryWhereBuilder() {
		dbpojo.Table newData = this.newData;

		this.query.append(" WHERE");

		if (newData.getID() != -1) {
			this.query.append(" " + newData.getTableName() + "." + newData.getPrimaryIDName() + " "
					+ Operation.Equal.getOperation() + "?");

			this.parameters.offer(newData.getID());
		} else {

			PojoDataContainer pojoDataContainer = PojoDataConversion.convertPojoData(newData);

			if (pojoDataContainer.getPojoKey().size() != 0) {

				while (pojoDataContainer.getPojoKey().size() > 0) {

					this.query.append(
							" " + pojoDataContainer.getPojoKey().poll() + " " + Operation.Equal.getOperation() + "?");

					if (pojoDataContainer.getPojoKey().size() != 0)
						this.query.append(" and");

				}
				this.parameters = pojoDataContainer.getPojoValue();

			} else {

				System.out.println("throw the Exception for empty value to use in where cluse");

			}

		}

		this.query.append(";");

	}

	private void executeWhereBuilder() {

		dbpojo.Table newData = this.newData;
		dbpojo.Table oldData = this.oldData;
		int id = this.id;
		OpType opType = this.opType;

		if (!opType.getOpType().equals(OpType.INSERT.getOpType())) {
			this.query.append(" WHERE");
			if (opType.getOpType().equals(OpType.DELETE.getOpType())) {
				this.query.append(" " + oldData.getTableName() + "." + oldData.getPrimaryIDName() + " "
						+ Operation.Equal.getOperation() + "?");
			} else {

				this.query.append(" " + newData.getTableName() + "." + newData.getPrimaryIDName() + " "
						+ Operation.Equal.getOperation() + "?");

			}

			this.parameters.offer(id);

		}

		this.query.append(";");

	}

	public String make() {
		this.query.append(";");
		System.out.println("generated query upto select is :" + this.query);

		this.TableName = null;
		this.parameters.clear();
		this.query.setLength(0);

		return this.query.toString();
	}

}
