package querybuilderconfig;

import java.util.List;



public interface Table {

	public String getColumnName();
	public  String getTableName();
	public String getPrimaryKey();
	public String getForiegnKey(String TableName);
	public List<dbpojo.Table> deleteChildTables();
	public List<String> getColumns();

}