package dbconnect;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBconnection {
	 private String url = "jdbc:mysql://localhost:3306/your_database_name";
	 private  String user = "root";
	 private String password = "root";
	public Connection con;
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

}
