package querybuilderconfig;

import java.util.ArrayList;

import querybuilderconfig.TableSchema.Statement;

public interface QueryBuilder{
	void openConnection();
	void closeConnection();
	void rollBackConnection();
	void commit();
//	QueryBuilder join( TableSchema.JoinType jointype,  Table tablename1,TableSchema.Operation op,Table tablename2);
	QueryBuilder select(dbpojo.Table table) ;
	QueryBuilder insert(dbpojo.Table table);
	QueryBuilder update(dbpojo.Table table);
	QueryBuilder delete(dbpojo.Table table);
	public ArrayList<dbpojo.Table> executeQuery();
    int[] execute(int userID);
    String make();
	
}
