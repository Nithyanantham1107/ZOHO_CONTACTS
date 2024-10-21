package querybuilder;

public class MysqlBuilder implements QueryBuilder {
	private StringBuilder query = new StringBuilder();
	private String TableName;

	public MysqlBuilder() {

		System.out.println("Query is  in mysql ");

	}

	@Override
	public QueryBuilder select(String tablename, String... columns) {

		this.TableName = tablename;
		this.query.append("SELECT");
		if (columns.length == 0) {
			this.query.append(" * ");
			this.query.append("FROM");
		} else {
			for (String col : columns) {
				query.append(" " + col + ",");

			}
			query.append(" FROM ");

		}
		query.append(" " + this.TableName);

		return this;
	}

	@Override
	public QueryBuilder Delete(String tablename) {
		this.TableName = tablename;
		this.query.append("DELETE FROM " + this.TableName + " ");

		return this;
	}

	@Override
	public QueryBuilder insert(String tablename, String... columns) {
		this.query.append("INSERT INTO " + tablename + " ");
		if (columns.length != 0) {
			this.query.append("(" + " ");
			for (int i = 0; i < columns.length; i++) {
				this.query.append(columns[i] + ",");

			}
			this.query.append(")"+" ");
		}

		return this;
	}

	@Override
	public QueryBuilder values(String ...values) {
		this.query.append("VALUES(" + " ");
		for (int i = 0; i < values.length; i++) {
			this.query.append(values[i] + ",");

		}
		this.query.append(")"+" ");
		return this;
	}

	@Override
	public QueryBuilder update(String tablename, String... condition) {
		this.TableName = tablename;
		this.query.append("UPDATE " + this.TableName + " " + "SET" + " ");
		for (int i = 0; i < condition.length; i++) {
			if (i == condition.length - 1) {
				this.query.append(condition[i] + " ");
			} else {

				this.query.append(condition[i] + ",");
			}

		}

		return this;
	}

	@Override
	public QueryBuilder create(String tableName, String... column) {
		this.TableName = tableName;
		this.query.append("CREATE TABLE " + tableName + " (");
		for (int i = 0; i < column.length; i++) {
			if (i == column.length - 1) {
				this.query.append(column[i]);

			} else {

				this.query.append(column[i] + ",");
			}

		}
		this.query.append(")");

		return this;
	}

	@Override
	public String column(String colName, String dataType) {

		return column(colName, dataType, null);
	}

	@Override
	public String column(String colName, String dataType, String constraints) {
		StringBuilder col = new StringBuilder();
		if (constraints == null) {
			col.append(colName + " " + dataType + " ");
		} else {
			col.append(colName + " " + dataType + " " + constraints);
		}

		return col.toString();
	}

	@Override
	public QueryBuilder where(String condition) {

		this.query.append("WHERE");
		this.query.append(" " + condition + " ");

		return this;
	}

	@Override
	public QueryBuilder and(String condition) {

		this.query.append("and");
		this.query.append(" " + condition + " ");

		return this;
	}

	@Override
	public QueryBuilder or(String condition) {

		this.query.append("or");
		this.query.append(" " + condition + " ");

		return this;
	}

	@Override
	public String build() {
		this.query.append(";");
		System.out.println("generated query upto select is :" + this.query);
		this.TableName = null;
		return this.query.toString();
	}

}

class SelectQuery {

	public SelectQuery() {

	}
}
