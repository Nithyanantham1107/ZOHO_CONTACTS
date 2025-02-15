package com.zohocontacts.dataquerybuilder.querybuilderconfig;

import java.util.ArrayList;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;

public interface QueryBuilder extends AutoCloseable {
	void openConnection();

	void closeConnection();

	void rollBackConnection();

	void commit();

	QueryBuilder select(com.zohocontacts.dbpojo.tabledesign.Table table);

	QueryBuilder insert(com.zohocontacts.dbpojo.tabledesign.Table table);

	QueryBuilder update(com.zohocontacts.dbpojo.tabledesign.Table table);

	QueryBuilder delete(com.zohocontacts.dbpojo.tabledesign.Table table);
	QueryBuilder where(Table column, Operation operation, Object value) ;
	QueryBuilder and(Table column, Operation operation, Object value);
	QueryBuilder or(Table column, Operation operator, Object value) ;
	public ArrayList<com.zohocontacts.dbpojo.tabledesign.Table> executeQuery();

	int[] execute(long userID);

	String make();

}
