package querybuilderconfig;

import java.util.ArrayList;

import querybuilderconfig.TableSchema.Operation;

public interface QueryBuilder {
	void openConnection();

	void closeConnection();

	void rollBackConnection();

	void commit();

	QueryBuilder select(dbpojo.Table table);

	QueryBuilder insert(dbpojo.Table table);

	QueryBuilder update(dbpojo.Table table);

	QueryBuilder delete(dbpojo.Table table);
	QueryBuilder where(Table column, Operation operation, Object value) ;
	QueryBuilder and(Table column, Operation operation, Object value);
	QueryBuilder or(Table column, Operation operator, Object value) ;
	public ArrayList<dbpojo.Table> executeQuery();

	int[] execute(long userID);

	String make();

}
