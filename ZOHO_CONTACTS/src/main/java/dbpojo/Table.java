package dbpojo;

import java.util.List;
import java.util.Map;

public interface Table {

	public void setCreatedAt(long createdAt);

	public long getCreatedAt();

	public void setModifiedAt(long modifiedAt);

	public long getModifiedAt();

	public void setID(long id);

	public long getID();

	public String getPrimaryIDName();

	public String getTableName();

	public Map<String, Object> getSettedData();

	public List<String> getTableColumnNames();

	public Table getNewTable(Map<String, Object> tableData);

}
