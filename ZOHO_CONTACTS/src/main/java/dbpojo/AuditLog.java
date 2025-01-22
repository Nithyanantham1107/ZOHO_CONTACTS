package dbpojo;

import java.util.HashMap;
import java.util.Map;

import querybuilderconfig.TableSchema.Audit_log;
import querybuilderconfig.TableSchema.tables;

public class AuditLog implements Table {

	private int ID = -1;
	private String previousState;
	private String changedState;
	private String tableName;
	private String operation;
	private long createdAt = -1;
	private int createdBy = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public int getID() {
		return ID;

	}

	public void setID(int iD) {
		ID = iD;
		settedData.put( Audit_log.ID.toString(), getID());
	}

	public String getPreviousState() {
		return previousState;
	}

	public String getAuditTableName() {
		return this.tableName;

	}

	public void setPreviousState(String previousState) {
		this.previousState = previousState;
		settedData.put( Audit_log.previous_state.toString(), getPreviousState());
	}

	public String getChangedState() {
		return changedState;
	}

	public void setChangedState(String changedState) {
		this.changedState = changedState;

		settedData.put( Audit_log.changed_state.toString(), getChangedState());
	}

	public String getTableName() {
		return tables.Audit_log.getTableName();
	}

	public String getPrimaryIDName() {

		return tables.Audit_log.getPrimaryKey();
	}

	public void setAuditTableName(String tableName) {
		this.tableName = tableName;

		settedData.put( Audit_log.table_name.toString(), getAuditTableName());
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
		settedData.put( Audit_log.operation.toString(), getOperation());
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
		settedData.put( Audit_log.created_at.toString(), getCreatedAt());
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
		settedData.put( Audit_log.created_by.toString(), getCreatedBy());
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
}
