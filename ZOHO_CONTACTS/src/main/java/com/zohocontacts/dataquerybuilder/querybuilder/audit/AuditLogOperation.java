package com.zohocontacts.dataquerybuilder.querybuilder.audit;

import com.zohocontacts.dataquerybuilder.querybuilder.datahelper.JsonConverter;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OpType;
import com.zohocontacts.dbpojo.AuditLog;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.dbpojo.tabledesign.Table;

public class AuditLogOperation {

	public static AuditLog audit(QueryBuilder qg, long rowKey, Table oldData, Table currentData, OpType opType,
			long userID) {

		int[] result = { -1, -1 };

		StringBuilder json;

		AuditLog audit = new AuditLog();
		audit.setOperation(opType.getOpType());
		audit.setCreatedBy(userID);

		if (currentData != null) {

			audit.setAuditTableName(currentData.getTableName());
			audit.setCreatedAt(currentData.getModifiedAt());
			if (currentData instanceof UserData) {

				audit.setCreatedBy(rowKey);
			}

		} else {

			audit.setAuditTableName(oldData.getTableName());
			audit.setCreatedAt(oldData.getModifiedAt());

			if (oldData instanceof UserData) {

				audit.setCreatedBy(rowKey);
			}

		}

		if (opType.getOpType().equals(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OpType.INSERT.getOpType())) {
json = JsonConverter.ConvertPojoToJson(currentData, userID);

			audit.setChangedState(json.toString());

			result = qg.insert(audit).execute(userID);

		} else if (opType.getOpType().equals(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OpType.UPDATE.getOpType())) {

			String[] table = JsonConverter.comparePojoJson(oldData, currentData, userID);

		audit.setPreviousState(table[0]);


			audit.setChangedState(table[1]);

			result = qg.insert(audit).execute(userID);

		} else if (opType.getOpType().equals(com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OpType.DELETE.getOpType())) {
			json = JsonConverter.ConvertPojoToJson(oldData, userID);

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
