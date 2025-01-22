package audit;

import datahelper.JsonConverter;
import datahelper.PojoDataContainer;
import dbpojo.AuditLog;
import dbpojo.Table;
import dbpojo.Userdata;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.TableSchema.OpType;

public class AuditLogOperation {

	public static AuditLog audit(QueryBuilder qg, int rowKey, Table oldData, Table currentData, OpType opType,
			int userID) {

		int[] result = { -1, -1 };
		PojoDataContainer pj;
		StringBuilder json;

		AuditLog audit = new AuditLog();
		audit.setOperation(opType.getOpType());
		audit.setCreatedBy(userID);

		if (oldData != null) {
			audit.setAuditTableName(oldData.getTableName());
			audit.setCreatedAt(oldData.getModifiedAt());

			if (oldData instanceof Userdata) {

				audit.setCreatedBy(rowKey);
			}

		} else {
			audit.setAuditTableName(currentData.getTableName());
			audit.setCreatedAt(currentData.getModifiedAt());
			if (currentData instanceof Userdata) {

				audit.setCreatedBy(rowKey);
			}
		}

		if (opType.getOpType().equals(querybuilderconfig.TableSchema.OpType.INSERT.getOpType())) {
//			pj = new PojoDataContainer();
//			pj = PojoDataConversion.convertPojoData(currentData);
			json = JsonConverter.ConvertPojoToJson(currentData,userID);

			audit.setChangedState(json.toString());

			result = qg.insert(audit).execute(userID);

		} else if (opType.getOpType().equals(querybuilderconfig.TableSchema.OpType.UPDATE.getOpType())) {

			String[] table = JsonConverter.comparePojoJson(oldData, currentData,userID);

//			pj = new PojoDataContainer();
//			pj = PojoDataConversion.convertPojoData(oldData);
			audit.setPreviousState(table[0]);

//			System.out.println("note here from audit" + pj.getJson());
//			pj = PojoDataConversion.convertPojoData(currentData);

			audit.setChangedState(table[1]);

			result = qg.insert(audit).execute(userID);

		} else if (opType.getOpType().equals(querybuilderconfig.TableSchema.OpType.DELETE.getOpType())) {
			json = JsonConverter.ConvertPojoToJson(oldData,userID);

			audit.setPreviousState(json.toString());

			result = qg.insert(audit).execute(userID);
		}

		if (result[0] == -1) {

			return null;
		}

		audit.setID(result[1]);
		return audit;

	}

}
