package querybuilder;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import dbconnect.DBconnection;

public class MysqlBuilder implements QueryBuilder {
	private StringBuilder query = new StringBuilder();
	private String TableName;
	private Connection con = null;
	private Queue<Object> parameters = new LinkedList<Object>();
	private Map<String, Set<String>> tableColumn = new HashMap<>();
	String url = "jdbc:mysql://localhost:3306/ZOHO_CONTACTS";
	String username = "root";
	String password = "root";

	public MysqlBuilder() throws SQLException {

		this.con = DriverManager.getConnection(this.url, this.username, this.password);

//		this.con = DBconnection.getConnection();

		if (this.con == null) {
			System.out.println("Hello im null ");
		}

		System.out.println("Query is  in mysql ");

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
	public void rollBackConnectio() {
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

//	public QueryBuilder join(Table table1, String operation, Table table2) {
//
//		this.query.append(" join  " + " " + table2.getTableName() + "  on  " + table1 + " = " + table2);
//
//		return this;
//
//	}

	@Override
	public QueryBuilder select(Table tablename, Table... columns) {

		this.TableName = tablename.getTableName();

		this.query.append("SELECT");
		if (columns.length == 0) {
			this.query.append(" * ");
			this.query.append("FROM");
		} else {
			for (int i = 0; i < columns.length; i++) {
				if (this.TableName.equals(columns[i].getTableName())) {
					query.append(" " + columns[i] + " ");
					if (i < columns.length - 1) {
						this.query.append(",");
					}

				} else {
					System.out.println("Invalid columnName for table:" + this.TableName);
					return null;
				}

			}
			query.append(" FROM ");

		}
		query.append(" " + this.TableName);

		return this;
	}

	@Override
	public QueryBuilder Delete(Table tablename) {

		this.TableName = tablename.getTableName();

		this.query.append("DELETE FROM " + this.TableName + " ");

		return this;
	}

	@Override
	public QueryBuilder insert(Table tablename, Table... columns) {

		this.TableName = tablename.getTableName();
		this.query.append("INSERT INTO " + tablename + " ");
		if (columns.length != 0) {
			this.query.append("(" + " ");

			for (int i = 0; i < columns.length; i++) {
				if (TableName.equals(columns[i].getTableName())) {
					this.query.append(columns[i]);

				} else {
					System.out.println("Invalid columnName for table:" + this.TableName);
					return null;
				}
				if (i < columns.length - 1) {
					this.query.append(",");
				}

			}
			this.query.append(")" + " ");
		}

		return this;
	}

	@Override
	public QueryBuilder valuesInsert(Object... values) {
		this.query.append(" VALUES(" + " ");
		for (int i = 0; i < values.length; i++) {

			this.parameters.offer(values[i]);

			this.query.append("?");

			if (i < values.length - 1) {
				this.query.append(",");
			}

		}
		this.query.append(")" + " ");
		return this;
	}

	@Override
	public QueryBuilder update(Table tablename, Table... columns) {

		this.TableName = tablename.getTableName();

		this.query.append("UPDATE " + this.TableName + " " + "SET" + " ");
		for (int i = 0; i < columns.length; i++) {
			if (this.TableName.equals(columns[i].getTableName())) {

				if (i == columns.length - 1) {
					this.query.append(columns[i] + "=?" + " ");
				} else {

					this.query.append(columns[i] + "=?,");
				}

			} else {
				System.out.println("Invalid columnName for table:" + this.TableName);
				return null;
			}

		}

		return this;
	}

	@Override
	public QueryBuilder valuesUpdate(Object... values) {

		for (int i = 0; i < values.length; i++) {

			this.parameters.offer(values[i]);

		}

		return this;
	}

	@Override
	public QueryBuilder where(Table columns, String operation, Object data) {

		this.query.append(" WHERE");

		if (!this.TableName.equals(columns.getTableName())) {
			return null;
		}
		this.query.append(" " + columns + " " + operation + "?");
		this.parameters.offer(data);

		return this;
	}

	@Override
	public QueryBuilder and(Table columns, String operation, Object data) {

		this.query.append(" and");

		if (!this.TableName.equals(columns.getTableName())) {
			return null;
		}
		this.query.append(" " + columns + " " + operation + "?");
		this.parameters.offer(data);

		return this;
	}

	@Override
	public QueryBuilder or(Table columns, String operation, Object data) {

		this.query.append("or");
		if (!this.TableName.equals(columns.getTableName())) {
			return null;
		}
		this.query.append(" " + columns + " " + operation + "?");
		this.parameters.offer(data);

		return this;
	}

	@Override
	public ArrayList<Map<String, Object>> buildQuery() {
		this.query.append(";");
		System.out.println("generated query upto select is :" + this.query);

		try {
			int i = 1;
			PreparedStatement ps = con.prepareStatement(this.query.toString());

			while (!this.parameters.isEmpty()) {

				if (this.parameters.peek() instanceof String) {

					ps.setString(i, (String) this.parameters.peek());
					System.out.println(i);

				} else if (this.parameters.peek() instanceof Integer) {

					ps.setInt(i, (Integer) this.parameters.peek());
					System.out.println(i);

				} else if (this.parameters.peek() instanceof Long) {
					ps.setLong(i, (Long) this.parameters.peek());
					System.out.println(i);
				}
				this.parameters.poll();
				i++;

			}

			ResultSet result = ps.executeQuery();
			this.parameters.clear();
			ArrayList<Map<String, Object>> resultList = new ArrayList<>();
			int columnCount = result.getMetaData().getColumnCount();

			while (result.next()) {
				Map<String, Object> row = new HashMap<>();
				for (i = 1; i <= columnCount; i++) {
					String columnName = result.getMetaData().getColumnName(i);
					Object value = result.getObject(i);
					row.put(columnName, value);
				}
				resultList.add(row);
			}
			return resultList;

		} catch (Exception e) {
			System.out.println(e);
		} finally {

			this.TableName = null;
			this.query.setLength(0);

		}
		return null;

	}

	public String make() {
		this.query.append(";");
		System.out.println("generated query upto select is :" + this.query);

		this.TableName = null;
		this.parameters.clear();
		this.query.setLength(0);

		return this.query.toString();
	}

	public int build() {
		try {
			this.query.append(";");
			int i = 1;
			System.out.println("generated query upto select is :" + this.query);
			PreparedStatement ps = this.con.prepareStatement(this.query.toString());

			while (!this.parameters.isEmpty()) {

				if (this.parameters.peek() instanceof String) {

					ps.setString(i, (String) this.parameters.peek());
					System.out.println(i);

				} else if (this.parameters.peek() instanceof Integer) {

					ps.setInt(i, (Integer) this.parameters.peek());
					System.out.println(i);

				} else if (this.parameters.peek() instanceof Long) {
					ps.setLong(i, (Long) this.parameters.peek());
					System.out.println(i);
				}
				this.parameters.poll();
				i++;

			}
			this.parameters.clear();
			return ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			this.TableName = null;
			this.query.setLength(0);

		}
		return -1;
	}

}
