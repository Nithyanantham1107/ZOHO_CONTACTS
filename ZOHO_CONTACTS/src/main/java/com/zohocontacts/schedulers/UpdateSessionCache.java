package com.zohocontacts.schedulers;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.CacheModel;

public class UpdateSessionCache implements Runnable {

	public void run() {
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder()) {

			query.openConnection();

			updateSessionQueue(query);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void updateSessionQueue(QueryBuilder query) {

		int[] result = { -1, -1 };
		long userID = 0;
		System.out.println("hey here is the update queue");

		try {

			for (String sessionid : CacheData.getsessionMapper().keySet()) {
				CacheModel cachemodel = CacheData.getCache(sessionid);
				if (cachemodel != null) {

					System.out.println("here the session data" + sessionid + "  then last accessed stored"
							+ CacheData.getsessionMapper().get(sessionid).getLastAccessed());

					com.zohocontacts.dbpojo.Session session = new com.zohocontacts.dbpojo.Session();
					session.setID(CacheData.getsessionMapper().get(sessionid).getID());
					session.setLastAccessed(CacheData.getsessionMapper().get(sessionid).getLastAccessed());

					result = query.update(session).execute(userID);

					if (result[0] == -1) {
						System.out.println("Error in updating session data to the table ,session id" + sessionid);

					}

				} else {
					System.out.println("here the cache data is null for session id" + sessionid);
				}
				CacheData.deleteAllCache(sessionid);
			}

		} catch (Exception e) {
			System.out.println(e);
			LoggerSet.logError("UpdateSessionCache", "UpdateQueueSchedule", "Error updating the session table: ", e);
		}

	}

}
