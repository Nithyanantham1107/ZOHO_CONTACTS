package com.zohocontacts.dbpojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategorySchema;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.dbpojo.tabledesign.TableWithChild;

public class Category implements TableWithChild {

	private long id = -1;
	private String categoryName;
	private long createdBy = -1;
	private long createdAt = -1;
	private long modifiedAt = -1;
	private List<Table> childTable = new ArrayList<Table>();
	private Map<String, Object> settedData = new HashMap<String, Object>();
	private List<CategoryRelation> categoryRelation = new ArrayList<CategoryRelation>();

	public Category(Map<String, Object> tableData) {
		settedData.clear();

		if (tableData.get(CategorySchema.CATEGORYID.getColumnName()) != null) {

			setID((long) tableData.get(CategorySchema.CATEGORYID.getColumnName()));

		}

		if (tableData.get(CategorySchema.CATEGORYNAME.getColumnName()) != null) {

			setCategoryName((String) tableData.get(CategorySchema.CATEGORYNAME.getColumnName()));

		}

		if (tableData.get(CategorySchema.CREATEDBY.getColumnName()) != null) {

			setCreatedBY((long) tableData.get(CategorySchema.CREATEDBY.getColumnName()));

		}

		if (tableData.get(CategorySchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((long) tableData.get(CategorySchema.CREATEDTIME.getColumnName()));

		}

		if (tableData.get(CategorySchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((long) tableData.get(CategorySchema.MODIFIEDTIME.getColumnName()));

		}

	}



	public Category() {

	}

	public void setID(long id) {

		this.id = id;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategorySchema.CATEGORYID.getColumnName(), getID());

		for (Table table : getChildTables()) {

			if (table instanceof CategoryRelation) {
				CategoryRelation categoryRelation = (CategoryRelation) table;
				categoryRelation.setCategoryID(id);

			}
		}
	}

	public long getID() {
		return this.id;
	}

	public String getPrimaryIDName() {

		return com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategorySchema.CATEGORYID.getPrimaryKey();
	}

	public String getTableName() {

		return com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategorySchema.CATEGORYID.getTableName();
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;

		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategorySchema.CREATEDTIME.getColumnName(), getCreatedAt());

	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategorySchema.MODIFIEDTIME.getColumnName(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setCategoryRelationAll(List<CategoryRelation> categoryrelation) {

		this.categoryRelation = categoryrelation;
	}

	public void setCategoryRelation(CategoryRelation categoryrelation) {

		this.categoryRelation.add(categoryrelation);
	}

	public CategoryRelation getSpecificCategorrRelation(long id) {

		for (CategoryRelation categoryRelation : getCategoryRelation()) {
			if (categoryRelation.getID() == id) {
				return categoryRelation;

			}
		}

		return null;
	}

	public List<CategoryRelation> getCategoryRelation() {
		return this.categoryRelation;
	}

	public void setCategoryName(String CategoryName) {
		this.categoryName = CategoryName;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategorySchema.CATEGORYNAME.getColumnName(), getCategoryName());
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCreatedBY(long createdby) {
		this.createdBy = createdby;
		settedData.put(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategorySchema.CREATEDBY.getColumnName(), getCreatedBy());
	}

	public long getCreatedBy() {
		return this.createdBy;
	}

	public boolean isContactExist(long userContactID) {
		if (this.categoryRelation != null) {

			for (CategoryRelation cr : this.categoryRelation) {

				if (cr.getContactIDtoJoin() == userContactID) {

					return true;
				}
			}

		}

		return false;

	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public List<String> getTableColumnNames() {

		return CategorySchema.CATEGORYID.getColumns();
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new Category(tableData);
	}

	@Override
	public String getForiegnkey(String TableName) {

		return CategorySchema.CATEGORYID.getForiegnKey(TableName);
	}

	@Override
	public void setChildTable(Table table) {

		if (table instanceof CategoryRelation) {
			CategoryRelation categoryRelation = (CategoryRelation) table;

			if (getSpecificCategorrRelation(categoryRelation.getID()) == null) {

				setCategoryRelation(categoryRelation);
			}

		}

	}

	@Override
	public List<Table> getChildTables() {
		childTable.clear();

		for (CategoryRelation categoryRelation : getCategoryRelation()) {

			childTable.add(categoryRelation);
		}
		return childTable;
	}

	@Override
	public List<Table> getDeleteChildTable() {

		return CategorySchema.CATEGORYID.deleteChildTables();
	}

}
