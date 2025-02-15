package dbpojo;

import java.util.List;

public interface TableWithChild extends Table {
	

	public String getForiegnkey(String TableName);

	public void setChildTable(Table table);
	public List<Table> getChildTables();
	
	public List<Table> getDeleteChildTable();

}
