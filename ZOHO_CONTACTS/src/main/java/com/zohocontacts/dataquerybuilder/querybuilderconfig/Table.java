package com.zohocontacts.dataquerybuilder.querybuilderconfig;

import java.util.List;



public interface Table {

	public String getColumnName();
	public  String getTableName();
	public String getPrimaryKey();
	public String getForiegnKey(String TableName);
	public List<com.zohocontacts.dbpojo.tabledesign.Table> deleteChildTables();
	public List<String> getColumns();

}