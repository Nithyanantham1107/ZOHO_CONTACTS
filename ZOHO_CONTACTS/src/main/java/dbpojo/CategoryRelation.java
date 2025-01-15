package dbpojo;

import querybuilderconfig.TableSchema.tables;

public class CategoryRelation implements Table {
	private int id = -1;
	private int contactIdToJoin = -1;
	private int categoryID = -1;
	private long createdAt = -1;
	private long modifiedAt = -1;

	public CategoryRelation() {

	}

	public CategoryRelation(int id, int contactIdtoJoin, int CategoryID, long createdAt,long modifiedAt) {

		this.contactIdToJoin = contactIdtoJoin;
		this.categoryID = CategoryID;
		this.createdAt = createdAt;
		
		this.modifiedAt=modifiedAt;
		this.id = id;

	}

	public void setID(int id) {

		this.id = id;
	}

	public int getID() {

		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
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
	}

	public int getCategoryID() {
		return this.categoryID;
	}

	public void setContactIDtoJoin(int ContactIDtoJoin) {
		this.contactIdToJoin = ContactIDtoJoin;
	}

	public int getContactIDtoJoin() {
		return this.contactIdToJoin;
	}

}
