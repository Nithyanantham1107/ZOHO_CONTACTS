package com.zohocontacts.schedulers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.loggerfiles.LoggerSet;

public class SessionTableCleaner implements Runnable {
	private static final int SESSIONTIMEOUT = 30 * 60 * 1000;

	private static final int DELAY = 5 * 60 * 1000;

	public void run() {
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder()) {

			query.openConnection();
			sessionTableCleaner(query);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void sessionTableCleaner(QueryBuilder query) {

		List<Table> result = new ArrayList<>();
		System.out.println("hello im Session Table cleaner");

		long sessionExpire;
		long currentTime;

		try {
			currentTime = Instant.now().toEpochMilli();
			com.zohocontacts.dbpojo.Session sessions = new com.zohocontacts.dbpojo.Session();

			result = query.select(sessions).executeQuery();

			if (result.size() > 0) {

				for (Table data : result) {

					com.zohocontacts.dbpojo.Session session = (com.zohocontacts.dbpojo.Session) data;

					sessionExpire = session.getLastAccessed() + SESSIONTIMEOUT + DELAY;

					if (currentTime - sessionExpire > 0) {
						long userID = session.getUserId();

						query.delete(session).execute(userID);

					}

				}

			}

		} catch (Exception e) {

			System.out.println(e);
			LoggerSet.logError("SessionTableCleaner", "SessionTableCleaner", "Error Deleting the session table: ", e);
		}

	}

}
