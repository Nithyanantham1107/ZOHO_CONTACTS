package querybuilder;




public class PostgresBuilder implements QueryBuilder {
	public PostgresBuilder() {

		System.out.println("Query is in postgresql  ");
	}
 @Override
public void openConnection() {
	// TODO Auto-generated method stub
	
}
 @Override
public void closeConnection() {
	// TODO Auto-generated method stub
	
}
 @Override
public void rollBackConnectio() {
	// TODO Auto-generated method stub
	
}
 @Override
public void commit() {
	// TODO Auto-generated method stub
	
}
	@Override
	public QueryBuilder select(String tableName, String... columns) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder create(String tablename, String... column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String column(String colName, String dataType) {
		// TODO Auto-generated method stub
		return column(colName, dataType, null);
	}

	@Override
	public String column(String colName, String dataType, String constraints) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder Delete(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public QueryBuilder insert(String tablename, String... columns) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public QueryBuilder values(String ...values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder update(String tableName, String... column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder where(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder and(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder or(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String build() {
		// TODO Auto-generated method stub
		return null;
	}

}
