package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.sql.Timestamp;
import javax.servlet.http.Cookie;
import org.mindrot.jbcrypt.BCrypt;
import dbconnect.DBconnection;
import dbmodel.UserData;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import querybuilder.QueryBuilder;
import querybuilder.SqlQueryLayer;
import querybuilder.TableSchema.Operation;
import querybuilder.TableSchema.Session;
import querybuilder.TableSchema.tables;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * This class handles session operations such as generating session IDs,
 * retrieving session IDs from cookies, deleting session data, and checking the
 * status of a session.
 */
public class SessionOperation {
	private LoggerSet logger; // LoggerSet instance

	public SessionOperation() {
		logger = new LoggerSet(); // Initialize logger
	}

	/**
	 * Generates a unique session ID for a user and stores it in the database.
	 *
	 * @param user_id the ID of the user
	 * @return the generated session ID or null if an error occurs
	 * @throws SQLException if a database access error occurs
	 */
	public String generateSessionId(int user_id) throws SQLException {
		int sessiontimeout = 30 * 60; 
		int[] val=new int[2];
		CacheModel cachemodel = new CacheModel();
		String uuid = UUID.randomUUID().toString();

		long timestamp = System.currentTimeMillis() / 1000;

		String sessionplaintext = uuid + timestamp + user_id;
		String sessionid = BCrypt.hashpw(sessionplaintext, BCrypt.gensalt());
		long sessionexpire = timestamp + sessiontimeout;
		System.out.println("here the time data current" + timestamp + "timeoutdata" + sessiontimeout + "session expire"
				+ sessionexpire);
		cachemodel.setSessionExpire(sessionexpire);
		cachemodel.setLastAccessed(timestamp);
		logger.logInfo("SessionOperation", "generateSessionId", "Created session ID: " + sessionid);
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		try {
//			con.setAutoCommit(false);
			qg.openConnection();
//			PreparedStatement ps = con.prepareStatement("INSERT INTO Session VALUES (?, ?, ?);");
//			ps.setString(1, sessionid);
//			ps.setLong(2, sessionexpire);
//			ps.setInt(3, user_id);
//			int val = ps.executeUpdate();
			
			
			 val=qg.insert(tables.Session)
					.valuesInsert(sessionid,sessionexpire,user_id)
					.execute();
			if (val[0] == 0) {
				logger.logWarning("SessionOperation", "generateSessionId", "Error inserting into Session table");
//				con.rollback();
				qg.rollBackConnection();
				return null;
			}

//			con.commit();
			qg.commit();
			addSessionCacheData(sessionid, cachemodel);

			return sessionid;
		} catch (Exception e) {
			logger.logError("SessionOperation", "generateSessionId", "Exception occurred", e);
		} finally {
//			con.close();
			qg.closeConnection();
		}

		return null;
	}

	/**
	 * Retrieves the session ID from the cookies.
	 *
	 * @param cookies the array of cookies from the HTTP request
	 * @return the session ID if found, or null if not found
	 */
	public String getCustomSessionId(Cookie[] cookies) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("SESSIONID".equals(cookie.getName())) {
					logger.logInfo("SessionOperation", "getCustomSessionId",
							"Cookie value found: " + cookie.getValue());
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Deletes the session data associated with the given session ID.
	 *
	 * @param sessionid the session ID to delete
	 * @return true if the session was deleted successfully, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
	public boolean DeleteSessionData(String sessionid) throws SQLException {
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val=new int[2];
		try {
			if (sessionid != null) {
//				PreparedStatement ps = con.prepareStatement("DELETE FROM Session WHERE session_id = ?;");
//				ps.setString(1, sessionid);
//				int val = ps.executeUpdate();
//                if (val == 0) {
//                    logger.logWarning("SessionOperation", "DeleteSessionData", "Error deleting the session");
//                    return false;
//                }
				 val=qg.Delete(tables.Session)
						.where(Session.Session_id, Operation.Equal, sessionid)
						.execute();
			} else {
				logger.logWarning("SessionOperation", "DeleteSessionData", "Session ID is null");
				return false;
			}
			CacheData.deleteAllCache(sessionid);
			return true;
		} catch (Exception e) {
			logger.logError("SessionOperation", "DeleteSessionData", "Exception occurred", e);
		} finally {
//			con.close();
			qg.closeConnection();
		}
		return false;
	}

	/**
	 * Checks if a session is still alive and updates its expiration time.
	 *
	 * @param sessionid the session ID to check
	 * @return the user ID associated with the session if alive, 0 if not
	 * @throws SQLException if a database access error occurs
	 */
	public CacheModel checkSessionAlive(String sessionid) throws SQLException {
		int userid = 0;
		Connection con = DBconnection.getConnection();
		long currenttime = System.currentTimeMillis() / 1000;
		try {
			CacheModel cachemodel = CacheData.getCache(sessionid);
			if (cachemodel != null) {
				
				if(cachemodel.getSessionExpire()<currenttime) {
					
					CacheData.deleteAllCache(sessionid);
					return null;
				}
				
				cachemodel.setLastAccessed(System.currentTimeMillis() / 1000);
				if (!CacheData.checkCacheQueue(sessionid)) {

					CacheData.updateQueue(sessionid);

				}
				return cachemodel;

			} else {
				cachemodel = new CacheModel();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM Session WHERE session_id = ?");
				ps.setString(1, sessionid);
				ResultSet val = ps.executeQuery();
				
				cachemodel.setLastAccessed(currenttime);
				if (val.next()) {
					userid = val.getInt(3);
					Userdata ud=new Userdata();
					ud.setUserId(userid);
					cachemodel.setUserData(ud);

					long timestatus = currenttime - val.getLong(2);
					if (timestatus > 0) {
						return null;
					}
				} else {
					return null;
				}

				con.setAutoCommit(false);
				currenttime = currenttime + (30 * 60);
				cachemodel.setSessionExpire(currenttime);
				logger.logInfo("SessionOperation", "checkSessionAlive",
						"Updated session expiration time: " + new Timestamp(currenttime));
				ps = con.prepareStatement("UPDATE Session SET session_expire = ? WHERE session_id = ?");
				ps.setLong(1, currenttime);
				ps.setString(2, sessionid);
				int result = ps.executeUpdate();
				if (result == 0) {
					logger.logWarning("SessionOperation", "checkSessionAlive",
							"Error updating the session table: " + sessionid);
					con.rollback();
					return null;
				}

				return cachemodel;

			}

		} catch (Exception e) {
			logger.logError("SessionOperation", "checkSessionAlive", "Exception occurred", e);
		} finally {
			con.close();
		}
		return null;
	}

	public void addSessionCacheData(String sessionid, CacheModel cachemodel) throws SQLException {
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		qg.openConnection();
		if (CacheData.getcache().size() >= 100) {
			int sessiontimeout = 30 * 60;
//			Connection con = DBconnection.getConnection();
			
			long maxTime = 0;
			long currentTime = System.currentTimeMillis() / 1000;
			String maxSessionid = null;
			for (String id : CacheData.getcache().keySet()) {
				CacheModel cache = CacheData.getCache(id);
				if (currentTime - cache.getlastAccessed() > maxTime) {
					maxTime = currentTime - cache.getlastAccessed();
					maxSessionid = id;
				}
			}
//			PreparedStatement ps = con.prepareStatement("UPDATE Session SET session_expire = ? WHERE session_id = ?");
//			ps.setLong(1, cachemodel.getlastAccessed() + sessiontimeout);
//			ps.setString(2, maxSessionid);
//			ps.executeUpdate();
			qg.update(tables.Session, Session.session_expire)
			.valuesUpdate(cachemodel.getlastAccessed() + sessiontimeout)
			.where(Session.Session_id, Operation.Equal,maxSessionid )
			.execute();

			CacheData.deleteAllCache(maxSessionid);

		}
		qg.closeConnection();
		CacheData.addViewCache(sessionid, cachemodel);

	}

}
