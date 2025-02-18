package com.zohocontacts.dataquerybuilder.querybuilder.postgressqlbuilder;

import java.util.ArrayList;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;
import com.zohocontacts.dbpojo.tabledesign.Table;

public class PostgresBuilder implements QueryBuilder {

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollBackConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QueryBuilder select(Table table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder insert(Table table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder update(Table table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder delete(Table table) {
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
	public ArrayList<Table> executeQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] execute(long userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String make() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
