package schedulers;

import java.time.Instant;
import java.util.ArrayList;

import dbpojo.Table;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

public class SessionTableCleaner implements Runnable {

	QueryBuilder qg = null;
	LoggerSet logger = new LoggerSet();

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
			dbpojo.Session sessions = new dbpojo.Session();

			result = this.qg.select(sessions).executeQuery();

			if (result.size() > 0) {

				for (Table data : result) {

					dbpojo.Session session = (dbpojo.Session) data;

					sessionExpire = session.getLastAccessed() + SESSIONTIMEOUT;

					if (currentTime - sessionExpire > 0) {
						long userID = session.getUserId();

						this.qg.delete(session).execute(userID);

					}

				}

			}

		} catch (Exception e) {

			System.out.println(e);
			logger.logError("UpdateAndDeleteQueue", "SessionTableCleaner", "Error Deleting the session table: ", e);
		}

	}

}
