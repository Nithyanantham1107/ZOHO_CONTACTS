package querybuilder;

import java.util.ArrayList;
import java.util.Map;

import querybuilder.TableSchema.Statement;

public interface QueryBuilder{
	void openConnection();
	void closeConnection();
	void rollBackConnection();
	void commit();
	QueryBuilder join( TableSchema.JoinType jointype,  Table tablename1,TableSchema.Operation op,Table tablename2);
	QueryBuilder select(Table tablename,Table ...columns) ;
	QueryBuilder insert(Table tablename,Table  ...columns);
	QueryBuilder valuesInsert(Object ...values);
	QueryBuilder valuesUpdate(Object ...values);
	QueryBuilder update(Table tablename,Table ...columns);
	QueryBuilder Delete(Table tablename);
	QueryBuilder where(Table columns,TableSchema.Operation op,Object data);
	QueryBuilder and(Table columns,TableSchema.Operation op,Object data);
	QueryBuilder or(Table columns,TableSchema.Operation op,Object data);
	public ArrayList<Object> executeQuery();
    int[] execute(Statement ...statements );
    String make();
	
}
