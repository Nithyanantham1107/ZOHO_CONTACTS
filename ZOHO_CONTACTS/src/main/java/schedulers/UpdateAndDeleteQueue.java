package schedulers;

import java.util.ArrayList;

import dbpojo.Table;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

public class UpdateAndDeleteQueue implements Runnable {
	QueryBuilder qg = null;
	LoggerSet logger = new LoggerSet();

	public UpdateAndDeleteQueue() {
//		Connection con=DBconnection.getDriverConnection();

//		Connection con = DBconnection.getConnection();

	}

	public void run() {

		this.qg = new SqlQueryLayer().createQueryBuilder();

		this.qg.openConnection();
		updateSessionQueue();
		sessionTableCleaner();

		this.qg.closeConnection();

	}

	private void updateSessionQueue() {

		int[] result = { -1, -1 };
		int userID = 0;
		System.out.println("hey here is the update queue");
//		Connection con = DBconnection.getConnection();
//     this.qg.openConnection();
		try {
//
//			if (qg == null) {
//				System.out.println("connection here in listner is null");
//
//			} else {
//				System.out.println("connection here in listner is not  null");

//			}

//			int sessiontimeout=30*60;
			CacheData.setSecondaryActive();

			for (String sessionid : CacheData.getPrimaryUpdateQueue()) {
				CacheModel cachemodel = CacheData.getCache(sessionid);
				if (cachemodel != null) {

					System.out.println("here the session data" + sessionid + "  then last accessed stored"
							+ cachemodel.getsession(sessionid).getLastAccessed());

					dbpojo.Session session = new dbpojo.Session();
					session.setID(cachemodel.getsession(sessionid).getID());
					session.setLastAccessed(cachemodel.getsession(sessionid).getLastAccessed());
					userID = cachemodel.getsession(sessionid).getUserId();
					session.setUserId(userID);
//					result = this.qg.update(tables.Session, Session.last_accessed)
//							.valuesUpdate(cachemodel.getsession(sessionid).getLastAccessed())
//							.where(Session.Session_id, Operation.Equal, sessionid).execute();

					result = qg.update(session).execute(userID);

//					 
//					 PreparedStatement ps = con.prepareStatement("UPDATE Session SET session_expire = ? WHERE session_id = ?");
//			         ps.setLong(1, cachemodel.getlastAccessed()+ sessiontimeout);
//			         ps.setString(2, sessionid);
//			         int result = ps.executeUpdate();
					if (result[0] == -1) {
						System.out.println("Error in updating session data to the table ,session id" + sessionid);

					}
					CacheData.removePrimaryUpdateQueue(sessionid);
				} else {
					System.out.println("here the cache data is null for session id" + sessionid);
				}

			}
			CacheData.setSecondaryInactive();
			CacheData.transferSecondaryToPrimary();

		} catch (Exception e) {
			System.out.println(e);
			logger.logError("Servletlistener", "UpdateQueueSchedule", "Error updating the session table: ", e);
		} finally {

//			this.qg.closeConnection();
		}

	}

	private void sessionTableCleaner() {

		int SESSIONTIMEOUT = 30 * 60;
		ArrayList<Table> result = new ArrayList<>();
		System.out.println("hello im Session Table cleaner");
//		Connection con = DBconnection.getConnection();

		long sessionExpire;
		long currentTime;
//		this.qg.openConnection();

		try {

			currentTime = System.currentTimeMillis() / 1000;
			dbpojo.Session sessions = new dbpojo.Session();

			result = this.qg.select(sessions).executeQuery();

//			PreparedStatement ps = con.prepareStatement("select Session_id,session_expire from Session;");
//			ResultSet val = ps.executeQuery();
			if (result.size() > 0) {

				for (Table data : result) {

					dbpojo.Session session = (dbpojo.Session) data;

					sessionExpire = session.getLastAccessed() + SESSIONTIMEOUT;
//					currentTime - sessionExpire < (5 * 60)
					if (currentTime - sessionExpire > 0) {
						int userID = session.getUserId();
//						ps = con.prepareStatement("delete from Session where Session_id=?");
//						ps.setString(1, val.getString(1));
//						ps.executeUpdate();

//						this.qg.delete(tables.Session).where(querybuilderconfig.TableSchema.Session.Session_id,
//								Operation.Equal, session.getSessionId()).execute();

						this.qg.delete(session).execute(userID);

					}

				}

			}

		} catch (Exception e) {

			System.out.println(e);
			logger.logError("Servletlistener", "SessionTableCleaner", "Error updating the session table: ", e);
		} finally {

//			qg.closeConnection();
		}

	}

}
