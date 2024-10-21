package querybuilder;

public interface QueryBuilder{
	QueryBuilder select(String tablename,String ...columns);
	QueryBuilder insert(String tablename,String ...columns);
	QueryBuilder values(String ...values);
	QueryBuilder update(String tablename,String ...valueSetter);
	QueryBuilder Delete(String tablename);
	QueryBuilder create(String tablename,String ...column);
	String column(String colName,String dataType);
	String column(String colName,String dataType,String constraints);
	QueryBuilder where(String condition);
	QueryBuilder and(String condition);
	QueryBuilder or(String condition);
	String build();
	
}
