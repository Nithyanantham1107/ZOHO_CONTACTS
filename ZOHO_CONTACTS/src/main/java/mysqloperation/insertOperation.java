package mysqloperation;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import audit.AuditLogOperation;
import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.EmailUser;
import dbpojo.Table;
import dbpojo.Userdata;
import querybuilder.QueryExecuter;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.TableSchema.OpType;
import querybuilderconfig.TableSchema.tables;

public class insertOperation {

	public static void insert(dbpojo.Table table, StringBuilder query, Queue<Object> parameters) {
//		this.opType = OpType.INSERT;
		Queue<String> columns=new LinkedList<String>();
		Table newData = table;
		query.append("INSERT INTO " + newData.getTableName() + " ");
//		PojoDataContainer pojoDataContainer = PojoDataConversion.convertPojoData(newData);
		
		
		for(Map.Entry<String,Object>  data: newData.getSettedData().entrySet()) {
			
			
			columns.add(data.getKey());
			parameters.add(data.getValue());
		
			
		}
	
		

//		parameters.addAll(pojoDataContainer.getPojoValue());
//		parameters = pojoDataContainer.getPojoValue();
		int length = parameters.size();

		System.out.println("here inside the insert method" + length + parameters);
		if (columns.size() != 0) {
			query.append("(" + " ");

			while (columns.size() > 0) {

				query.append(columns.poll());

				if (columns.size() != 0) {
					query.append(",");
				}

			}
			query.append(")" + " ");
		}

		query.append(" VALUES(" + " ");
		for (int i = 0; i < length; i++) {

			query.append("?");

			if (i != length - 1) {
				query.append(",");
			}

		}
		query.append(")" + " ;");

//		int[] result = execute(qg, con, query.toString(), parameters, newData, userId);

//		return qg;
	}

	public static int[] execute(QueryBuilder qg, Connection con, String query, Queue<Object> parameters, Table newData,
			int userID) {

		int id = -1;
		int[] data = QueryExecuter.mySqlExecuter(con, query, parameters, OpType.INSERT);

		newData.setID(data[1]);
		id = data[1];
		if (data[0] == -1) {
			return null;
		}

		if (data[0] != -1) {

			insertOperation.insertChildTable(qg, newData, userID);

		}

		if ((newData != null && !newData.getTableName().equals(tables.Audit_log.getTableName()))) {

			if (AuditLogOperation.audit(qg, id, null, newData, OpType.INSERT, userID) == null) {
				System.out.println("Table" + newData.getTableName() + "  is not audited");
			}

		}

		return data;
	}

	private static Table insertChildTable(QueryBuilder qg, Table table, int userID) {

		if (table instanceof Userdata) {

			Userdata userData = (Userdata) table;

			if (userData.getLoginCredentials() != null) {
				userData.getLoginCredentials().setUserID(userData.getID());

				qg.insert(userData.getLoginCredentials()).execute(userData.getID());

			}
			if ( userData.getallemail()!=null &&  userData.getallemail().size() != 0) {
				for (EmailUser email : userData.getallemail()) {
					email.setEmailID(userData.getID());
					qg.insert(email).execute(userData.getID());
				}
			}

		} else if (table instanceof Category) {
			Category category = (Category) table;
			if (  category.getCategoryRelation()!=null &&  category.getCategoryRelation().size() != 0) {

				for (CategoryRelation categoryRelation : category.getCategoryRelation()) {

					categoryRelation.setCategoryID(category.getID());

					qg.insert(categoryRelation).execute(userID);
				}

			}

		} else if (table instanceof ContactDetails) {

			ContactDetails contactDetails = (ContactDetails) table;
			if (contactDetails.getContactMail() != null) {

				contactDetails.getContactMail().setContactID(contactDetails.getID());

				qg.insert(contactDetails.getContactMail()).execute(userID);

			}

			if (contactDetails.getContactphone() != null) {

				contactDetails.getContactphone().setContactID(contactDetails.getID());

				qg.insert(contactDetails.getContactphone()).execute(userID);
			}

		}

		return null;
	}

}
