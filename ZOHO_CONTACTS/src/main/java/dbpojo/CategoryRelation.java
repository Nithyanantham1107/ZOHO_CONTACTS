package dbpojo;

import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.Category_relation;
import querybuilderconfig.TableSchema.tables;

public class CategoryRelation implements Table {
	private int id = -1;
	private int contactIdToJoin = -1;
	private int categoryID = -1;
	private long createdAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private long modifiedAt = -1;

	public CategoryRelation() {

	}

	public CategoryRelation(int id, int contactIdtoJoin, int CategoryID, long createdAt, long modifiedAt) {
		settedData.clear();

		setCategoryID(CategoryID);
		setContactIDtoJoin(contactIdtoJoin);
		setCreatedAt(createdAt);
		setModifiedAt(modifiedAt);
		setID(id);

	}

	public void setID(int id) {

		this.id = id;

		settedData.put( Category_relation.ID.toString(), getID());

	}

	public int getID() {

		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;

		settedData.put( Category_relation.created_time.toString(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;

		settedData.put( Category_relation.modified_time.toString(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public String getTableName() {

		return tables.Category_relation.getTableName();
	}

	public String getPrimaryIDName() {

		return tables.Category_relation.getPrimaryKey();
	}

	public void setCategoryID(int Categoryid) {
		this.categoryID = Categoryid;

		settedData.put( Category_relation.Category_id.toString(), getCategoryID());
	}

	public int getCategoryID() {
		return this.categoryID;
	}

	public void setContactIDtoJoin(int ContactIDtoJoin) {
		this.contactIdToJoin = ContactIDtoJoin;

		settedData.put( Category_relation.contact_id_to_join.toString(), getContactIDtoJoin());
	}

	public int getContactIDtoJoin() {
		return this.contactIdToJoin;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}
}
