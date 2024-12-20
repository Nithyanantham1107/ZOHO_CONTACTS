package querybuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import dbconnect.DBconnection;
import dbpojo.PojoMapper;
import querybuilder.TableSchema.Statement;

public class MysqlBuilder implements QueryBuilder {
	private StringBuilder query = new StringBuilder();
	private ArrayList<String> TableName = new ArrayList<String>();
	private Connection con = null;
	private String primarykey;
	private Queue<Object> parameters = new LinkedList<Object>();
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

	public QueryBuilder join(TableSchema.JoinType jointype, Table table1, TableSchema.Operation op, Table table2) {

		this.query.append(" " + jointype.getType() + " " + table2.getTableName() + "  on  " + table1.getTableName()
				+ "." + table1 + "  " + op.getOperation() + " " + table2.getTableName() + "." + table2 + " ");
		this.TableName.add(table2.getTableName());

		return this;

	}

	@Override
	public QueryBuilder select(Table tablename, Table... columns) {

		this.TableName.add(tablename.getTableName());
		Boolean state = false;

		this.query.append("SELECT");
		if (columns.length == 0) {
			this.query.append(" * ");
			this.query.append("FROM");
		} else {
			this.primarykey = tablename.getTableName() + "." + tablename.getPrimaryKey();

			for (int i = 0; i < columns.length; i++) {

				if (this.primarykey.equals(columns[i].getTableName() + "." + columns[i])) {
					state = true;

				}

				query.append(" " + columns[i].getTableName() + "." + columns[i] + " ");

				if (i < columns.length - 1) {
					this.query.append(",");
				}

			}
			if (!state) {

				this.query.append("," + this.primarykey + " ");

			}
			query.append(" FROM ");

		}
		query.append(" " + this.TableName.getLast());

		return this;
	}

	@Override
	public QueryBuilder Delete(Table tablename) {

		this.TableName.add(tablename.getTableName());

		this.query.append("DELETE FROM " + this.TableName + " ");

		return this;
	}

	@Override
	public QueryBuilder insert(Table tablename, Table... columns) {

		this.TableName.add(tablename.getTableName());
		this.query.append("INSERT INTO " + tablename.getTableName() + " ");
		if (columns.length != 0) {
			this.query.append("(" + " ");

			for (int i = 0; i < columns.length; i++) {
				if (TableName.contains(columns[i].getTableName())) {
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

		this.TableName.add(tablename.getTableName());
		
		
//		System.out.println(this.TableName );

		this.query.append("UPDATE " + tablename.getTableName() + " " + "SET" + " ");
		for (int i = 0; i < columns.length; i++) {
			if (this.TableName.contains(columns[i].getTableName())) {

				if (i == columns.length - 1) {
					this.query.append(" " + columns[i].getTableName() + "." + columns[i] + "=?" + " ");
				} else {

					this.query.append(" " + columns[i].getTableName() + "." + columns[i] + "=?,");
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
	public QueryBuilder where(Table columns, TableSchema.Operation operation, Object data) {

		this.query.append(" WHERE");

		if (!this.TableName.contains(columns.getTableName())) {
			return null;
		}
		this.query.append(" " + columns.getTableName() + "." + columns + " " + operation.getOperation() + "?");
		this.parameters.offer(data);

		return this;
	}

	@Override
	public QueryBuilder and(Table columns, TableSchema.Operation operation, Object data) {

		this.query.append(" and");

		if (!this.TableName.contains(columns.getTableName())) {
			return null;
		}
		this.query.append(" " + columns.getTableName() + "." + columns + " " + operation.getOperation() + "?");
		this.parameters.offer(data);

		return this;
	}

	@Override
	public QueryBuilder or(Table columns, TableSchema.Operation operation, Object data) {

		this.query.append("or");
		if (!this.TableName.contains(columns.getTableName())) {
			return null;
		}
		this.query.append(" " + columns.getTableName() + "." + columns + " " + operation.getOperation() + "?");
		this.parameters.offer(data);

		return this;
	}

	@Override
	public ArrayList<Object> executeQuery() {
		this.query.append(";");

		System.out.println("generated query upto select is :" + this.query);

		try {
			int i = 1;
			PreparedStatement ps = con.prepareStatement(this.query.toString());

			while (!this.parameters.isEmpty()) {

				if (this.parameters.peek() instanceof String) {

					ps.setString(i, (String) this.parameters.peek());

				} else if (this.parameters.peek() instanceof Integer) {

					ps.setInt(i, (Integer) this.parameters.peek());

				} else if (this.parameters.peek() instanceof Long) {
					ps.setLong(i, (Long) this.parameters.peek());

				}
				this.parameters.poll();
				i++;

			}

			ResultSet result = ps.executeQuery();
			this.parameters.clear();

			int columnCount = result.getMetaData().getColumnCount();

			ArrayList<String> columnNames = new ArrayList<>();

			for (int j = 1; j <= columnCount; j++) {
				String columnName = result.getMetaData().getColumnName(j);
				String tablename = result.getMetaData().getTableName(j);
				columnNames.add(tablename + "." + columnName);
//				System.out.println(tablename + "." + columnName);

			}

			PojoMapper pm = new PojoMapper();

			return pm.PojoResultSetter(this.TableName.get(0), columnNames, result);

		} catch (Exception e) {
			System.out.println(e);
		} finally {

			this.TableName.clear();
			;
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
	

	public int[] execute(Statement... statements) {
		
		int[] data={-1,-1};
		try {
			ResultSet result;
			this.query.append(";");
			int i = 1;
			
			System.out.println("generated query upto select is :" + this.query);

			PreparedStatement ps = this.con.prepareStatement(this.query.toString());

			if (statements.length ==1 ) {

				if (statements[0].toString().equals("RETURN_GENERATED_KEYS")) {
					ps = this.con.prepareStatement(this.query.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
				}

			}

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
				
				
//				System.out.println("value"+this.parameters.peek());
				this.parameters.poll();
				i++;

			}
			this.parameters.clear();
			data[0]=ps.executeUpdate();
			System.out.println("execution of query"+data[0]);
			
			if (statements.length == 1) {

				if (statements[1].toString().equals("RETURN_GENERATED_KEYS")) {
					result=ps.getGeneratedKeys();
					if(result.next()) {
						data[1]=result.getInt(1);
					}else {
						System.out.println("generated Key are null!!!");
					}
				}

			}
			
			
			return data;

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			this.TableName.clear();
			;
			this.query.setLength(0);

		}
		return data;
	}

}
