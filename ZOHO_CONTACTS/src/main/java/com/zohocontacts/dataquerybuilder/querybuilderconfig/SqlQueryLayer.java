package com.zohocontacts.dataquerybuilder.querybuilderconfig;

import java.io.FileInputStream;
import java.util.Properties;

import com.zohocontacts.dataquerybuilder.querybuilder.mysqlquerybuilder.MysqlBuilder;
import com.zohocontacts.dataquerybuilder.querybuilder.postgressqlbuilder.PostgresBuilder;

public class SqlQueryLayer {

	

	private static String getDBType() {

		Properties property = new Properties();
		System.out.println("check1");
		
		
		    String currentWorkingDirectory = System.getProperty("user.dir");  
		    
		    String dbConfigPath = currentWorkingDirectory + "/git/ZOHO_CONTACTS/ZOHO_CONTACTS/database.properties";


		    try (FileInputStream fis = new FileInputStream(
					dbConfigPath)) {
			property.load(fis);
			

			return property.getProperty("database.type");

		} catch (Exception e) {
			System.out.println(e);
			return null;

		}

	}

	public QueryBuilder createQueryBuilder() {
		try {

			String DBtype = getDBType();
			if (DBtype == null) {
				throw new NullPointerException("the DB type is not set");
			}
			switch (DBtype.toLowerCase()) {

			case "mysql":
				return new MysqlBuilder();

			case "postgresql":
				return new PostgresBuilder();
			default:
				throw new Exception("Unsupported Database...");
			}

		} catch (Exception e) {
			System.out.println(e);

		}
		return null;

	}

}
