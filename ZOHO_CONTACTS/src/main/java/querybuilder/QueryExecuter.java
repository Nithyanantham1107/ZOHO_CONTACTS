package querybuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Queue;

import datahelper.PojoMapper;
import dbpojo.Table;
import querybuilderconfig.TableSchema.OpType;

public class QueryExecuter {
	
	

	public static int[] mySqlExecuter( Connection con, 
			String query, 
			Queue<Object> parameters,
			OpType opType) {


		int[] data = { -1, -1 };
		try {
			ResultSet result;

			int i = 1;

			System.out.println("generated query is :" + query);

			PreparedStatement ps = con.prepareStatement(query);



				if (opType.getOpType().equals(OpType.INSERT.getOpType())) {
					ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
				}

			

			while (!parameters.isEmpty()) {

				if (!QueryBuilderHelper.queryParamSetter(ps, parameters.peek(), i)) {

					System.out.println("Error in setting parameter on query");
				}


				parameters.poll();
				i++;

			}
			parameters.clear();
		
			data[0] = ps.executeUpdate();
	
			System.out.println("execution of  query state:" + data[0]);

			

				if (opType.getOpType().equals(OpType.INSERT.getOpType())) {
					result = ps.getGeneratedKeys();
					if (result.next()) {
						data[1] = result.getInt(1);
					} else {
						System.out.println("generated Key are null!!!");
					}
				}

			
			

//			ps.close();

			return data;

		} catch (Exception e) {
			System.out.println(e);
		} finally {
//			this.TableName.clear();

			query=null;

		}
		return data;

	}

	public static ArrayList<Table> mySqlExecuteQuery(Connection con, String query, Queue<Object> parameters,
			String tableName) {

		PreparedStatement ps;
		ResultSet result;

		System.out.println("generated query upto select is :" + query);

		try {
			int i = 1;
			ps = con.prepareStatement(query.toString());

			while (!parameters.isEmpty()) {

				if (!QueryBuilderHelper.queryParamSetter(ps, parameters.peek(), i)) {

					System.out.println("Error in setting parameter on query");
				}

				

				parameters.poll();
				i++;

			}

			
			
			
			result = ps.executeQuery();
			
			
			parameters.clear();

			int columnCount = result.getMetaData().getColumnCount();

			ArrayList<String> columnNames = new ArrayList<>();

			for (int j = 1; j <= columnCount; j++) {
				String columnName = result.getMetaData().getColumnName(j);
				String tablename = result.getMetaData().getTableName(j);
				columnNames.add(tablename + "." + columnName);
				

			}

			PojoMapper pm = new PojoMapper();

			return pm.PojoResultSetter(tableName, columnNames, result);

		} catch (Exception e) {
			System.out.println(e);
		} finally {

//			tableName.clear();

			query=null;

		}
		return null;

	}

}
