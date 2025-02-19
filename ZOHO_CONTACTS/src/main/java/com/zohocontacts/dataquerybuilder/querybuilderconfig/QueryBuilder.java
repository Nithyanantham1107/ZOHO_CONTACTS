package com.zohocontacts.dataquerybuilder.querybuilderconfig;

import java.util.List;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;
import com.zohocontacts.exception.QueryBuilderException;

public interface QueryBuilder extends AutoCloseable {
	void openConnection() throws QueryBuilderException;

	void close() throws QueryBuilderException;

	void rollBackConnection() throws QueryBuilderException;

	void commit() throws QueryBuilderException;

	QueryBuilder select(com.zohocontacts.dbpojo.tabledesign.Table table) throws QueryBuilderException;

	QueryBuilder insert(com.zohocontacts.dbpojo.tabledesign.Table table) throws QueryBuilderException;

	QueryBuilder update(com.zohocontacts.dbpojo.tabledesign.Table table) throws QueryBuilderException;

	QueryBuilder delete(com.zohocontacts.dbpojo.tabledesign.Table table) throws QueryBuilderException;

	QueryBuilder where(Table column, Operation operation, Object value);

	QueryBuilder offset(int offsetValue);

	QueryBuilder limit(int limit);

	QueryBuilder and(Table column, Operation operation, Object value);

	QueryBuilder or(Table column, Operation operator, Object value);

	public List<com.zohocontacts.dbpojo.tabledesign.Table> executeQuery() throws QueryBuilderException;

	int[] execute(long userID) throws QueryBuilderException;

	String make();

}
