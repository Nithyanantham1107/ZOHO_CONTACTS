package dbpojo;

import java.util.ArrayList;

public class Category {

	int Category_id;
	String Category_name;
	int created_by;
ArrayList<CategoryRelation> categoryRelation=new ArrayList<CategoryRelation>();

	Category(int CategoryId, String CategoryName, int CreatedBy) {
		this.Category_id = CategoryId;
		this.Category_name = CategoryName;
		this.created_by = CategoryId;

	}
	
	
	
	
	public void setCategoryRelation(CategoryRelation categoryrelation) {

		this.categoryRelation.add(categoryrelation);
	}

	public ArrayList<CategoryRelation> getCategoryRelation() {
		return this.categoryRelation;
	}


	public void setCategoryID(int Categoryid) {
		this.Category_id = Categoryid;
	}

	public int getCategoryID() {
		return this.Category_id;
	}

	public void setCategoryName(String CategoryName) {
		this.Category_name = CategoryName;
	}

	public String getCategoryName() {
		return this.Category_name;
	}

	public void setCreatedBY(int createdby) {
		this.created_by = createdby;
	}

	public int getCreatedBy() {
		return this.created_by;
	}

}
