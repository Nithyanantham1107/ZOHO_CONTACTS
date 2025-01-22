package dbpojo;

import java.util.HashMap;
import java.util.Map;

public interface Table {
	public void setCreatedAt(long createdAt);

	public long getCreatedAt();

	public void setModifiedAt(long modifiedAt);

	public long getModifiedAt();

	public void setID(int id);

	public int getID();
	
	public  String getPrimaryIDName();

	public  String getTableName();
	
	public Map<String, Object> getSettedData();


}
