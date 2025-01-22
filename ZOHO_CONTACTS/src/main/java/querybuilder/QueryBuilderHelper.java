package querybuilder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbpojo.Table;

public class QueryBuilderHelper {
	
	
	public static boolean  queryParamSetter(PreparedStatement ps,Object param,int i) throws SQLException {
		System.out.println("param" + param);
		
		if(param ==null) {
			ps.setObject(i, param);
			return true;
			
			
		}else if ( param instanceof String) {

			ps.setString(i, (String) param);
			System.out.println(i);
			return true;

		} else if (param instanceof Integer) {

			ps.setInt(i, (Integer) param);
			System.out.println(i);
			return true;

		} else if (param instanceof Long) {
			ps.setLong(i, (Long) param);
			System.out.println(i);
			return true;
		} else if (param instanceof Boolean) {
			ps.setBoolean(i, (Boolean) param);
			System.out.println(i);
			return true;

		} else {
			System.out.println("error in data type!!!");
			return false;
		}
	}

	
	
	
	
	
	
}







