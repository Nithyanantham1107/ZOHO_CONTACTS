package querybuilder;

import java.io.FileInputStream;
import java.lang.classfile.TypeAnnotation.ThrowsTarget;
import java.util.HashMap;
import java.util.Properties;

public class SqlQueryBuilder {

	private String tableName;

	private static String getDBType() {

		Properties property = new Properties();
		System.out.println("check1");
		try (FileInputStream fis = new FileInputStream(
				"/home/nithya-pt7676/git/ZOHO_CONTACTS/ZOHO_CONTACTS/database.properties")) {
			property.load(fis);
			System.out.println("check2");

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
