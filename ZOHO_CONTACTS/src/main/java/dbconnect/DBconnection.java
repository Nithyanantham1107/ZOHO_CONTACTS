package dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * This DBconnection class used to perform Database connection related 
 * operations
 * 
 */

public class DBconnection {
	

	public Connection con;
	
	/**
	 * the method used to lookup the connection
	 * @return connection object of DB
	 */
	public static Connection getConnection() {
		try {
		Context context=new InitialContext();
		DataSource data=(DataSource) context.lookup("java:/comp/env/jdbc/test");
		return data.getConnection();
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	
	
	public static Connection getDriverConnection() {
		try {
			String url = "jdbc:mysql://localhost:3306/ZOHO_CONTACTS";
			String username = "root";
			String password = "root";
		return DriverManager.getConnection(url, username, password);
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
		return null;
		
		
	}

}
