package com.zohocontacts.dataquerybuilder.querybuilder.postgressqlbuilder;

import java.util.ArrayList;
import java.util.List;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.exception.QueryBuilderException;

public class PostgresBuilder implements QueryBuilder {

	@Override
	public void openConnection() throws QueryBuilderException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws QueryBuilderException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollBackConnection() throws QueryBuilderException {
		// TODO Auto-generated method stub

	}

	@Override
	public void commit() throws QueryBuilderException {
		// TODO Auto-generated method stub

	}

	@Override
	public QueryBuilder select(Table table) throws QueryBuilderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder insert(Table table) throws QueryBuilderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder update(Table table) throws QueryBuilderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder delete(Table table) throws QueryBuilderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder where(com.zohocontacts.dataquerybuilder.querybuilderconfig.Table column, Operation operation,
			Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder offset(int offsetValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder limit(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder and(com.zohocontacts.dataquerybuilder.querybuilderconfig.Table column, Operation operation,
			Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder or(com.zohocontacts.dataquerybuilder.querybuilderconfig.Table column, Operation operator,
			Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Table> executeQuery() throws QueryBuilderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] execute(long userID) throws QueryBuilderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String make() {
		// TODO Auto-generated method stub
		return null;
	}

}
