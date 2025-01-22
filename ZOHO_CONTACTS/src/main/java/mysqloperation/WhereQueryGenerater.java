package mysqloperation;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import dbpojo.Table;
import querybuilderconfig.TableSchema.Operation;

public class WhereQueryGenerater {

	public static void executeQueryWhereBuilder(Table newData, StringBuilder query, Queue<Object> parameters) {

		

		if (newData.getID() != -1) {
			query.append(" WHERE");
			query.append(" " + newData.getTableName() + "." + newData.getPrimaryIDName() + " "
					+ Operation.Equal.getOperation() + "?");

			parameters.offer(newData.getID());
		} else {
			
			
			Queue<String> column=new LinkedList<String>();

			for(Map.Entry<String, Object> data: newData.getSettedData().entrySet()) {
				column.add(data.getKey());
				parameters.add(data.getValue());
				
				
			}
			
			
//			PojoDataContainer pojoDataContainer = PojoDataConversion.convertPojoData(newData);

			if (column.size() != 0) {
				query.append(" WHERE");

				while (column.size() > 0) {

					query.append(
							" " + column.poll() + " " + Operation.Equal.getOperation() + "?");

					if (column.size() != 0)
						query.append(" and");

				}
//				parameters = pojoDataContainer.getPojoValue();
//				parameters.addAll(pojoDataContainer.getPojoValue());

			} else {

				System.out.println("throw the Exception for empty value to use in where cluse");

			}

		}

		query.append(";");

	}

	public static void executeWhereBuilder(Table oldData, StringBuilder query, Queue<Object> parameters) {

		query.append(" WHERE");

		query.append(" " + oldData.getTableName() + "." + oldData.getPrimaryIDName() + " "
				+ Operation.Equal.getOperation() + "?");
		parameters.offer(oldData.getID());

		query.append(";");

	}

}
