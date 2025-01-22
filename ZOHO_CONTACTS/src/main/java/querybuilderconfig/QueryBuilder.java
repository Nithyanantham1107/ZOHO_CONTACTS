package querybuilderconfig;

import java.util.ArrayList;

public interface QueryBuilder {
	void openConnection();

	void closeConnection();

	void rollBackConnection();

	void commit();

	QueryBuilder select(dbpojo.Table table);

	QueryBuilder insert(dbpojo.Table table);

	QueryBuilder update(dbpojo.Table table);

	QueryBuilder delete(dbpojo.Table table);

	public ArrayList<dbpojo.Table> executeQuery();

	int[] execute(int userID);

	String make();

}
