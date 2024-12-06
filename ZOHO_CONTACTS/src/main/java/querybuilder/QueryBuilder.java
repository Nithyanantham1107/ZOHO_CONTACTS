package querybuilder;

import java.util.ArrayList;
import java.util.Map;

public interface QueryBuilder{
	void openConnection();
	void closeConnection();
	void rollBackConnectio();
	void commit();
	QueryBuilder select(Table tablename,Table ...columns) ;
	QueryBuilder insert(Table tablename,Table  ...columns);
	QueryBuilder valuesInsert(Object ...values);
	QueryBuilder valuesUpdate(Object ...values);
	QueryBuilder update(Table tablename,Table ...columns);
	QueryBuilder Delete(Table tablename);
	QueryBuilder where(Table columns,String operation,Object data);
	QueryBuilder and(Table columns,String operation,Object data);
	QueryBuilder or(Table columns,String operation,Object data);
	public ArrayList<Map<String,Object>> buildQuery();
    int build();
    String make();
	
}
