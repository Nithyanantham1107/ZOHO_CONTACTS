package com.zohocontacts.dboperation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.ServerRegistrySchema;
import com.zohocontacts.dbpojo.ServerRegistry;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;

public class ServerRegistryOperation {

	
	public static ServerRegistry insertServerRegistry(String serverIP, long serverPort) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			ServerRegistry server = new ServerRegistry();
			server.setServerIP(serverIP);
			server.setServerPort(serverPort);
			server.setCreatedAt(Instant.now().toEpochMilli());
			server.setModifiedAt(server.getCreatedAt());
			query.openConnection();

			result = query.insert(server).execute(0);

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("ServerRegistryOperation", "insertServerRegistry", "Failed to insert Server Data",
						null);
				return null;
			}
			query.commit();
			LoggerSet.logInfo("ServerRegistryOperation", "insertServerRegistry",
					"Server Data added successfully: for ID " + server.getID());

			return server;

		} catch (Exception e) {
			LoggerSet.logError("ServerRegistryOperation", "insertServerRegistry", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static Boolean deleteServerRegistry(ServerRegistry server) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			result = query.delete(server).execute(0);

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("ServerRegistryOperation", "deleteServerRegistry", "Failed to delete Server Data",
						null);
				return false;
			}
			query.commit();
			LoggerSet.logInfo("ServerRegistryOperation", "deleteServerRegistry",
					"Server Data deleted successfully: for ID " + server.getID());

			return true;

		} catch (Exception e) {
			LoggerSet.logError("ServerRegistryOperation", "deleteServerRegistry", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static List<ServerRegistry> getServerRegistryExcept(ServerRegistry server) throws DBOperationException {
		List<Table> result = new ArrayList<Table>();
		List<ServerRegistry> servers = new ArrayList<ServerRegistry>();
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			result = query.select(server).where(ServerRegistrySchema.SERVERID, Operation.NOTEQUAL, server.getID())
					.executeQuery();

			if (result.size() <= 0) {

				query.rollBackConnection();
				LoggerSet.logError("ServerRegistryOperation", "getServerRegistryExcept", "Failed to get Server Data",
						null);
				return servers;
			}

			for (Table table : result) {
				servers.add((ServerRegistry) table);

			}
			LoggerSet.logInfo("ServerRegistryOperation", "getServerRegistryExcept",
					"Server Data retrieved successfully: for ID " + server.getID());

			return servers;

		} catch (Exception e) {
			LoggerSet.logError("ServerRegistryOperation", "getServerRegistryExcept",
					"Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

}
