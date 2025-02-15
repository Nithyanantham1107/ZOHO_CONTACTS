package com.zohocontacts.schedulers;

import java.time.Instant;
import java.util.ArrayList;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.loggerfiles.LoggerSet;

public class SessionTableCleaner implements Runnable {

	QueryBuilder qg = null;

	public void run() {

		this.qg = new SqlQueryLayer().createQueryBuilder();

		this.qg.openConnection();

		sessionTableCleaner();
		this.qg.closeConnection();
	}

	private void sessionTableCleaner() {

		int SESSIONTIMEOUT = 30 * 60 * 1000;
		ArrayList<Table> result = new ArrayList<>();
		System.out.println("hello im Session Table cleaner");

		long sessionExpire;
		long currentTime;

		try {

			currentTime = Instant.now().toEpochMilli();
			com.zohocontacts.dbpojo.Session sessions = new com.zohocontacts.dbpojo.Session();

			result = this.qg.select(sessions).executeQuery();

			if (result.size() > 0) {

				for (Table data : result) {

					com.zohocontacts.dbpojo.Session session = (com.zohocontacts.dbpojo.Session) data;

					sessionExpire = session.getLastAccessed() + SESSIONTIMEOUT;

					if (currentTime - sessionExpire > 0) {
						long userID = session.getUserId();

						this.qg.delete(session).execute(userID);

					}

				}

			}

		} catch (Exception e) {

			System.out.println(e);
			LoggerSet.logError("UpdateAndDeleteQueue", "SessionTableCleaner", "Error Deleting the session table: ", e);
		}

	}

}
