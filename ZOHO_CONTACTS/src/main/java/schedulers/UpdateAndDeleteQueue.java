package schedulers;

import java.sql.Connection;
import java.util.ArrayList;

import dbconnect.DBconnection;
import loggerfiles.LoggerSet;
import querybuilder.QueryBuilder;
import querybuilder.SqlQueryLayer;
import querybuilder.TableSchema.Operation;
import querybuilder.TableSchema.Session;
import querybuilder.TableSchema.tables;
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

					result = this.qg.update(tables.Session, Session.last_accessed)
							.valuesUpdate(cachemodel.getsession(sessionid).getLastAccessed())
							.where(Session.Session_id, Operation.Equal, sessionid).execute();

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

		int SESSIONTIMEOUT = 30*60;
		ArrayList<Object> result = new ArrayList<Object>();
		System.out.println("hello im Session Table cleaner");
//		Connection con = DBconnection.getConnection();

		long sessionExpire;
		long currentTime;
//		this.qg.openConnection();

		try {

			currentTime = System.currentTimeMillis() / 1000;

			result = this.qg.select(tables.Session).executeQuery();

//			PreparedStatement ps = con.prepareStatement("select Session_id,session_expire from Session;");
//			ResultSet val = ps.executeQuery();
			if (result != null) {

				for (Object data : result) {

					dbpojo.Session session = (dbpojo.Session) data;

					sessionExpire = session.getLastAccessed() + SESSIONTIMEOUT;
//					currentTime - sessionExpire < (5 * 60)
					if (currentTime - sessionExpire > 0) {
//						ps = con.prepareStatement("delete from Session where Session_id=?");
//						ps.setString(1, val.getString(1));
//						ps.executeUpdate();

						this.qg.delete(tables.Session).where(querybuilder.TableSchema.Session.Session_id,
								Operation.Equal, session.getSessionId()).execute();

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
