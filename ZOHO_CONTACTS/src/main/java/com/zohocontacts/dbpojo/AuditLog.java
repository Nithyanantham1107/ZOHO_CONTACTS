package com.zohocontacts.dbpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.AuditLogSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.ContactMailSchema;
import com.zohocontacts.dbpojo.tabledesign.Table;

public class AuditLog implements Table {

	private long ID = -1;
	private String previousState;
	private String changedState;
	private String tableName;
	private String operation;
	private long createdAt = -1;
	private long createdBy = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public AuditLog() {

	}

	public AuditLog(Map<String, Object> tableData) {
		if (tableData.get(AuditLogSchema.ID.getColumnName()) != null) {

			setID((long) tableData.get(AuditLogSchema.ID.getColumnName()));
		}

		if (tableData.get(AuditLogSchema.PREVIOUSSTATE.getColumnName()) != null) {

			setPreviousState((String) tableData.get(AuditLogSchema.PREVIOUSSTATE.getColumnName()));
		}

		if (tableData.get(AuditLogSchema.CHANGEDSTATE.getColumnName()) != null) {

			setChangedState((String) tableData.get(AuditLogSchema.CHANGEDSTATE.getColumnName()));
		}

		if (tableData.get(AuditLogSchema.TABLENAME.getColumnName()) != null) {

			setAuditTableName((String) tableData.get(AuditLogSchema.TABLENAME.getColumnName()));
		}

		if (tableData.get(AuditLogSchema.CREATEDAT.getColumnName()) != null) {

			setCreatedAt((long) tableData.get(AuditLogSchema.CREATEDAT.getColumnName()));
		}

		if (tableData.get(AuditLogSchema.CREATEDBY.getColumnName()) != null) {

			setCreatedBy((long) tableData.get(AuditLogSchema.CREATEDBY.getColumnName()));
		}

	}

	public long getID() {
		return ID;

	}

	public void setID(long iD) {
		ID = iD;
		settedData.put(AuditLogSchema.ID.getColumnName(), getID());
	}

	public String getPreviousState() {
		return previousState;
	}

	public String getAuditTableName() {
		return this.tableName;

	}

	public void setPreviousState(String previousState) {
		this.previousState = previousState;
		settedData.put(AuditLogSchema.PREVIOUSSTATE.getColumnName(), getPreviousState());
	}

	public String getChangedState() {
		return changedState;
	}

	public void setChangedState(String changedState) {
		this.changedState = changedState;

		settedData.put(AuditLogSchema.CHANGEDSTATE.getColumnName(), getChangedState());
	}

	public String getTableName() {
		return AuditLogSchema.ID.getTableName();
	}

	public String getPrimaryIDName() {

		return AuditLogSchema.ID.getPrimaryKey();
	}

	public void setAuditTableName(String tableName) {
		this.tableName = tableName;

		settedData.put(AuditLogSchema.TABLENAME.getColumnName(), getAuditTableName());
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
		settedData.put(AuditLogSchema.OPERATION.getColumnName(), getOperation());
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
		settedData.put(AuditLogSchema.CREATEDAT.getColumnName(), getCreatedAt());
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
		settedData.put(AuditLogSchema.CREATEDBY.getColumnName(), getCreatedBy());
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public long getModifiedAt() {

		return 0;
	}

	@Override
	public void setModifiedAt(long modifiedAt) {

	}

	@Override
	public List<String> getTableColumnNames() {

		return AuditLogSchema.ID.getColumns();
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new AuditLog(tableData);
	}
}
