package dbpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import querybuilderconfig.TableSchema.CategoryRelationSchema;
import querybuilderconfig.TableSchema.SessionSchema;

public class CategoryRelation implements Table {
	private long id = -1;
	private long contactIdToJoin = -1;
	private long categoryID = -1;
	private long createdAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private long modifiedAt = -1;

	public CategoryRelation() {

	}

	public CategoryRelation(Map<String, Object> tableData) {
		settedData.clear();

		if (tableData.get(CategoryRelationSchema.ID.getColumnName()) != null) {

			setID((long) tableData.get(CategoryRelationSchema.ID.getColumnName()));
		}

		if (tableData.get(CategoryRelationSchema.CONTACTIDTOJOIN.getColumnName()) != null) {

			setContactIDtoJoin((long) tableData.get(CategoryRelationSchema.CONTACTIDTOJOIN.getColumnName()));
		}

		if (tableData.get(CategoryRelationSchema.CATEGORYID.getColumnName()) != null) {

			setCategoryID((long) tableData.get(CategoryRelationSchema.CATEGORYID.getColumnName()));
		}

		if (tableData.get(CategoryRelationSchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((long) tableData.get(CategoryRelationSchema.CREATEDTIME.getColumnName()));
		}

		if (tableData.get(CategoryRelationSchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((long) tableData.get(CategoryRelationSchema.MODIFIEDTIME.getColumnName()));
		}

	}

	public CategoryRelation(int id, int contactIdtoJoin, int CategoryID, long createdAt, long modifiedAt) {
		settedData.clear();

		setCategoryID(CategoryID);
		setContactIDtoJoin(contactIdtoJoin);
		setCreatedAt(createdAt);
		setModifiedAt(modifiedAt);
		setID(id);

	}

	public void setID(long id) {

		this.id = id;

		settedData.put(CategoryRelationSchema.ID.getColumnName(), getID());

	}

	public long getID() {

		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;

		settedData.put(CategoryRelationSchema.CREATEDTIME.getColumnName(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;

		settedData.put(CategoryRelationSchema.MODIFIEDTIME.getColumnName(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public String getTableName() {

		return CategoryRelationSchema.ID.getTableName();
	}

	public String getPrimaryIDName() {

		return CategoryRelationSchema.ID.getPrimaryKey();
	}

	public void setCategoryID(long Categoryid) {
		this.categoryID = Categoryid;

		settedData.put(CategoryRelationSchema.CATEGORYID.getColumnName(), getCategoryID());
	}

	public long getCategoryID() {
		return this.categoryID;
	}

	public void setContactIDtoJoin(long ContactIDtoJoin) {
		this.contactIdToJoin = ContactIDtoJoin;

		settedData.put(CategoryRelationSchema.CONTACTIDTOJOIN.getColumnName(), getContactIDtoJoin());
	}

	public long getContactIDtoJoin() {
		return this.contactIdToJoin;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public List<String> getTableColumnNames() {
	
		return CategoryRelationSchema.ID.getColumns();
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {
		
		return new CategoryRelation(tableData);
	}
}
