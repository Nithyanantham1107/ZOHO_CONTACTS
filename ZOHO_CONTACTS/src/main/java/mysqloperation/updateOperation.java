package mysqloperation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import audit.AuditLogOperation;
import datahelper.changedStateContainer;
import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.EmailUser;
import dbpojo.Oauth;
import dbpojo.Table;
import dbpojo.TableWithChild;
import dbpojo.Userdata;
import querybuilder.QueryExecuter;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.TableSchema.AuditLogSchema;
import querybuilderconfig.TableSchema.OpType;
import querybuilderconfig.TableSchema.SessionSchema;

public class updateOperation {

	public static changedStateContainer updateTable(QueryBuilder qg, Connection con, Table newData, StringBuilder query,
			Queue<Object> parameters) {

		Table oldData;
		Queue<String> columns = new LinkedList<String>();

		if (newData.getID() == -1) {

			System.out.println("Need to throw Exception for non id data");
			return null;
		}

		ArrayList<dbpojo.Table> data = qg.select(newData).executeQuery();

		if (data.size() == 0) {

			System.out.println("throw Exception for not finding data");
			return null;
		} else {

			oldData = (dbpojo.Table) data.getFirst();
		}

		changedStateContainer stateContainer = new changedStateContainer();

		stateContainer.setNewData(newData);
		stateContainer.setOldData(oldData);

//		newData = stateContainer.getnewData();
//		oldData = stateContainer.getOldData();
		query.append("UPDATE " + newData.getTableName() + " " + "SET" + " ");
//		PojoDataContainer pojoDataContainer = PojoDataConversion.convertPojoData(newData);
//		System.out.println(pojoDataContainer.getJson());
		for (Map.Entry<String, Object> table : newData.getSettedData().entrySet()) {

			if (!table.getKey().equals(newData.getPrimaryIDName())) {
				columns.add(table.getKey());

				parameters.add(table.getValue());

			}

		}

//		Queue<String> columns = pojoDataContainer.getPojoKey();
		if (columns.size() == 0) {

			System.out.println("here i need to throw error for no data is changed so no updation");
			return null;
		}
		while (columns.size() > 0) {

			query.append(columns.poll() + "=?");

			if (columns.size() != 0) {
				query.append(",");
			}

		}

//		parameters.addAll(pojoDataContainer.getPojoValue());

//		WhereQueryGenerater.executeWhereBuilder(newData, query, parameters);

		return stateContainer;

	}

	private static void updateChildTable(QueryBuilder qg, TableWithChild table, long userID) {

		for (Table childTable : table.getChildTables()) {

			qg.update(childTable).execute(userID);
		}
	}

//	private static Table updateChildTable(QueryBuilder qg, Table table, int userID) {
//
//		if (table instanceof Userdata) {
//
//			Userdata userData = (Userdata) table;
//			if (userData.getLoginCredentials() != null) {
//
//				qg.update(userData.getLoginCredentials()).execute(userID);
//
//			}
//			if (userData.getallemail() != null && userData.getallemail().size() != 0) {
//				for (EmailUser email : userData.getallemail()) {
//
//					qg.update(email).execute(userID);
//				}
//			}
//
//			if (userData.getallOauth() != null && userData.getallOauth().size() != 0) {
//				for (Oauth oauth : userData.getallOauth()) {
//
//					qg.update(oauth).execute(userID);
//				}
//			}
//
//		} else if (table instanceof Category) {
//			Category category = (Category) table;
//			if (category.getCategoryRelation() != null && category.getCategoryRelation().size() != 0) {
//
//				for (CategoryRelation categoryRelation : category.getCategoryRelation()) {
////					categoryRelation.setCategoryID(category.getID());
//					qg.update(categoryRelation).execute(userID);
//				}
//
//			}
//
//		} else if (table instanceof ContactDetails) {
//
//			ContactDetails contactDetails = (ContactDetails) table;
//			if (contactDetails.getContactMail() != null) {
//
//				contactDetails.getContactMail().setContactID(contactDetails.getID());
//				qg.update(contactDetails.getContactMail()).execute(userID);
//
//			}
//
//			if (contactDetails.getContactphone() != null) {
//
//				contactDetails.getContactphone().setContactID(contactDetails.getID());
//				qg.update(contactDetails.getContactphone()).execute(userID);
//			}
//
//		}
//		return null;
//	}

	public static int[] execute(QueryBuilder qg, Connection con, String query, Queue<Object> parameters, Table newData,
			Table oldData, long userID) {

		int[] data = QueryExecuter.mySqlExecuter(con, query, parameters, OpType.UPDATE);

		if (data[0] != -1) {
			if (newData instanceof TableWithChild) {

				TableWithChild parentTable = (TableWithChild) newData;
				updateOperation.updateChildTable(qg, parentTable, userID);
			}

		}
		if ((newData != null && !newData.getTableName().equals(AuditLogSchema.ID.getTableName())) &&

				(oldData != null && !oldData.getTableName().equals(AuditLogSchema.ID.getTableName()))

				&& (newData != null && !newData.getTableName().equals(SessionSchema.ID.getTableName())) &&

				(oldData != null && !oldData.getTableName().equals(SessionSchema.ID.getTableName()))

		) {

			if (AuditLogOperation.audit(qg, oldData.getID(), oldData, newData, OpType.UPDATE, userID) == null) {
				System.out.println("Table" + newData.getTableName() + "  is not audited");
			}

		}

		return data;
	}
}
