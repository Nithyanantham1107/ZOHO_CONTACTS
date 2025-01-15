package dbpojo;

import java.util.ArrayList;

import querybuilderconfig.TableSchema.tables;

public class Category implements Table {

	private int id = -1;
	private String categoryName;
	private int createdBy = -1;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private ArrayList<CategoryRelation> categoryRelation = new ArrayList<CategoryRelation>();

	public Category(int id, String CategoryName, int CreatedBy, long createdAt, long modifiedAt) {
		this.id = id;
		this.categoryName = CategoryName;
		this.createdBy = CreatedBy;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;

	}

	public Category() {

	}
	

	public void setID(int id) {
		
		this.id=id;
	}
	
	public int getID() {
		return this.id;
	}

	public String getPrimaryIDName() {
		
		return tables.Category.getPrimaryKey();
	}

	public  String getTableName() {
		
		return tables.Category.getTableName();
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

	public void setCategoryRelationAll(ArrayList<CategoryRelation> categoryrelation) {

		this.categoryRelation = categoryrelation;
	}

	public void setCategoryRelation(CategoryRelation categoryrelation) {

		this.categoryRelation.add(categoryrelation);
	}

	public ArrayList<CategoryRelation> getCategoryRelation() {
		return this.categoryRelation;
	}



	public void setCategoryName(String CategoryName) {
		this.categoryName = CategoryName;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCreatedBY(int createdby) {
		this.createdBy = createdby;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public boolean isContactExist(int usercontactid) {
		if (this.categoryRelation != null) {

			for (CategoryRelation cr : this.categoryRelation) {

				if (cr.getContactIDtoJoin() == usercontactid) {

					return true;
				}
			}

		}

		return false;

	}

}
