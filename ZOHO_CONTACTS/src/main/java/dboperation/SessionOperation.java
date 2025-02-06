package dboperation;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.mindrot.jbcrypt.BCrypt;

import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Table;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;
import querybuilderconfig.TableSchema.Email_user;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * This class handles session operations such as generating session IDs,
 * retrieving session IDs from cookies, deleting session data, and checking the
 * status of a session.
 */
public class SessionOperation {
	private static LoggerSet logger = new LoggerSet(); // LoggerSet instance
	final static int SESSIONTIMEOUT = 30 * 60 * 1000;

	public SessionOperation() {

	}

	/**
	 * Generates a unique session ID for a user and stores it in the database.
	 *
	 * @param userID the ID of the user
	 * @return the generated session ID or null if an error occurs
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static String generateSessionId(int userID) throws DBOperationException {

		int[] result = new int[2];
		CacheModel cachemodel = new CacheModel();
		dbpojo.Session session = new dbpojo.Session();
		Userdata ud = new Userdata();
		ud.setID(userID);
		String uuid = UUID.randomUUID().toString();

		long currentTime = Instant.now().toEpochMilli();

		String sessionplaintext = uuid + currentTime + userID;
		String sessionid = BCrypt.hashpw(sessionplaintext, BCrypt.gensalt());

		System.out.println("here the time data current" + currentTime + "timeoutdata" + SESSIONTIMEOUT
				+ "session expire" + currentTime + SESSIONTIMEOUT);

		session.setSessionID(sessionid);
		session.setUserId(userID);
		session.setLastAccessed(currentTime);
		session.setCreatedAt(currentTime);
		session.setModifiedAt(currentTime);
		cachemodel.setSession(session);
		cachemodel.setUserData(ud);

		logger.logInfo("SessionOperation", "generateSessionId", "Created session ID: " + sessionid);

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		try {

			qg.openConnection();

			result = qg.insert(session).execute(userID);
			if (result[0] == 0) {
				logger.logWarning("SessionOperation", "generateSessionId", "Error inserting into Session table");

				qg.rollBackConnection();
				return null;
			}

			qg.commit();
			addSessionCacheData(sessionid, cachemodel);

			return sessionid;
		} catch (Exception e) {
			logger.logError("SessionOperation", "generateSessionId", "Exception occurred", e);

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	/**
	 * Retrieves the session ID from the cookies.
	 *
	 * @param cookies the array of cookies from the HTTP request
	 * @return the session ID if found, or null if not found
	 */
	public static String getCustomSessionId(Cookie[] cookies) {
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
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static boolean DeleteSessionData(String sessionid) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		try {

			int[] result = { -1, -1 };
			qg.openConnection();
			if (sessionid != null) {
				CacheModel cachemodel = CacheData.getCache(sessionid);
				dbpojo.Session session = cachemodel.getsession(sessionid);
				int userID = session.getUserId();

				result = qg.delete(session).execute(userID);
			} else {
				logger.logWarning("SessionOperation", "DeleteSessionData", "Session ID is null");
				return false;
			}
			CacheData.deleteAllCache(sessionid);
			return true;
		} catch (Exception e) {
			logger.logError("SessionOperation", "DeleteSessionData", "Exception occurred", e);

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	/**
	 * Checks if a session is still alive and updates its expiration time.
	 *
	 * @param sessionid the session ID to check
	 * @return the user ID associated with the session if alive, 0 if not
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static CacheModel checkSessionAlive(String sessionid) throws DBOperationException {

		ArrayList<Table> result = new ArrayList<>();
		long currenttime = Instant.now().toEpochMilli();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {
			qg.openConnection();
			CacheModel cachemodel = CacheData.getCache(sessionid);
			if (cachemodel != null) {
				if (cachemodel.getsession(sessionid).getLastAccessed() + SESSIONTIMEOUT < currenttime) {

					CacheData.deleteAllCache(sessionid);
					return null;
				}
				System.out.println("last accessed " + cachemodel.getsession(sessionid).getLastAccessed());
				cachemodel.getsession(sessionid).setLastAccessed(Instant.now().toEpochMilli());
				if (!CacheData.checkCacheQueue(sessionid)) {

					CacheData.updateQueue(sessionid);

				}
				return cachemodel;

			} else {

				cachemodel = new CacheModel();
				dbpojo.Session session = new dbpojo.Session();
				session.setSessionID(sessionid);
				result = qg.select(session).executeQuery();
				if (result.size() > 0) {
					session = (dbpojo.Session) result.getFirst();
					long sessionTimeout = session.getLastAccessed() + SESSIONTIMEOUT;
					if (sessionTimeout < currenttime) {
						return null;
					}

					if (CacheData.getUsercache(session.getUserId()) != null) {
						cachemodel = CacheData.getUsercache(session.getUserId());
						cachemodel.setSession(session);
					} else {

						Userdata ud = new Userdata();
						EmailUser email = new EmailUser();
						LoginCredentials login = new LoginCredentials();
						ud.setLoginCredentials(login);
						ud.setEmail(email);
						ud.setID(session.getUserId());
						result = qg.select(ud).executeQuery();
						if (result.size() > 0) {
							ud = (Userdata) result.getFirst();
							cachemodel.setUserData(ud);

						} else {
							logger.logInfo("SessionOperation", "checkSessionAlive",
									"user not Found for userID " + session.getUserId());
							return null;
						}
						cachemodel.setSession(session);

					}
				} else {
					return null;
				}

				logger.logInfo("SessionOperation", "checkSessionAlive",
						"Updated session expiration time: " + new Timestamp(currenttime));

				return cachemodel;

			}

		} catch (Exception e) {
			logger.logError("SessionOperation", "checkSessionAlive", "Exception occurred", e);

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	public static void addSessionCacheData(String sessionid, CacheModel cachemodel) {
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		qg.openConnection();
		if (CacheData.getsessionMapper().size() >= 100) {
			long maxTime = 0;
			int userID = 0;
			long currentTime = Instant.now().toEpochMilli();
			dbpojo.Session maxSessionid = new dbpojo.Session();

			for (String sessionId : CacheData.getsessionMapper().keySet()) {
				CacheModel cache = CacheData.getCache(sessionId);
				if (currentTime - cache.getsession(sessionid).getLastAccessed() > maxTime) {
					maxTime = currentTime - cache.getsession(sessionid).getLastAccessed();
					maxSessionid = cache.getsession(sessionid);
					userID = maxSessionid.getUserId();
				}
			}

			qg.update(maxSessionid).execute(userID);

			CacheData.deleteAllCache(maxSessionid.getSessionId());

		}
		qg.closeConnection();
		CacheData.addViewCache(sessionid, cachemodel);

	}

}
