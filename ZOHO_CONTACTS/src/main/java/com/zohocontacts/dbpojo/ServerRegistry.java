package com.zohocontacts.dbpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.ServerRegistrySchema;
import com.zohocontacts.dbpojo.tabledesign.Table;

public class ServerRegistry implements Table {

	private long ID = -1;
	private String serverIp;
	private long serverPort=-1;
	private long createdTime = -1;
	private long modifiedTime = -1;

	private Map<String, Object> settedData = new HashMap<String, Object>();

	public ServerRegistry() {

	}

	public ServerRegistry(Map<String, Object> tableData) {

		settedData.clear();
		if (tableData.get(ServerRegistrySchema.SERVERID.getColumnName()) != null) {

			setID((long) tableData.get(ServerRegistrySchema.SERVERID.getColumnName()));

		}

		if (tableData.get(ServerRegistrySchema.SERVERIP.getColumnName()) != null) {

			setServerIP((String) tableData.get(ServerRegistrySchema.SERVERIP.getColumnName()));

		}
		
		if (tableData.get(ServerRegistrySchema.SERVERPORT.getColumnName()) != null) {

			setServerPort((Long) tableData.get(ServerRegistrySchema.SERVERPORT.getColumnName()));

		}

		if (tableData.get(ServerRegistrySchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((Long) tableData.get(ServerRegistrySchema.CREATEDTIME.getColumnName()));

		}

		if (tableData.get(ServerRegistrySchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((Long) tableData.get(ServerRegistrySchema.MODIFIEDTIME.getColumnName()));

		}

	}

	public void setServerIP(String serverIP) {

		this.serverIp = serverIP;

		settedData.put(ServerRegistrySchema.SERVERIP.getColumnName(), getServerIP());

	}
	
	
	

	public String getServerIP() {

		return this.serverIp;
	}
	
	
	
	public void setServerPort(long serverPort) {

		this.serverPort=serverPort;

		settedData.put(ServerRegistrySchema.SERVERPORT.getColumnName(), getServerPort());

	}
	
	
	

	public long getServerPort() {

		return this.serverPort;
	}

	@Override
	public long getID() {

		return this.ID;
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new ServerRegistry(tableData);
	}

	@Override
	public String getPrimaryIDName() {

		return ServerRegistrySchema.SERVERID.getPrimaryKey();
	}

	@Override
	public Map<String, Object> getSettedData() {
		return this.settedData;
	}

	@Override
	public List<String> getTableColumnNames() {

		return ServerRegistrySchema.SERVERID.getColumns();
	}

	@Override
	public String getTableName() {

		return ServerRegistrySchema.SERVERID.getTableName();
	}

	@Override
	public void setID(long id) {
		this.ID = id;

		settedData.put(ServerRegistrySchema.SERVERID.getColumnName(), getID());

	}

	@Override
	public void setModifiedAt(long modifiedAt) {
		this.modifiedTime = modifiedAt;

		settedData.put(ServerRegistrySchema.MODIFIEDTIME.getColumnName(), getModifiedAt());

	}

	public void setCreatedAt(long createdAt) {
		this.createdTime = createdAt;

		settedData.put(ServerRegistrySchema.CREATEDTIME.getColumnName(), getCreatedAt());

	}

	@Override
	public long getCreatedAt() {

		return createdTime;
	}

	@Override
	public long getModifiedAt() {
		return modifiedTime;
	}

}
