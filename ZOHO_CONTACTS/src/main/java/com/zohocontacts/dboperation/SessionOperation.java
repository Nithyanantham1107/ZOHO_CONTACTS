package com.zohocontacts.dboperation;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.mindrot.jbcrypt.BCrypt;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.CacheModel;

/**
 * This class handles session operations such as generating session IDs,
 * retrieving session IDs from cookies, deleting session data, and checking the
 * status of a session.
 */
public class SessionOperation {
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
	public static String generateSessionId(long userID) throws DBOperationException {

		int[] result = new int[2];
		CacheModel cachemodel = new CacheModel();
		com.zohocontacts.dbpojo.Session session = new com.zohocontacts.dbpojo.Session();
		UserData userData = new UserData();
		userData.setID(userID);
		String uniqueID = UUID.randomUUID().toString();

		long currentTime = Instant.now().toEpochMilli();

		String sessionplaintext = uniqueID + currentTime + userID;
		String sessionid = BCrypt.hashpw(sessionplaintext, BCrypt.gensalt());

		session.setSessionID(sessionid);
		session.setUserId(userID);
		session.setLastAccessed(currentTime);
		session.setCreatedAt(currentTime);
		session.setModifiedAt(currentTime);
		cachemodel.setSession(session);
		cachemodel.setUserData(userData);

		LoggerSet.logInfo("SessionOperation", "generateSessionId", "Created session ID: " + sessionid);

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			result = query.insert(session).execute(userID);
			if (result[0] == 0) {
				LoggerSet.logWarning("SessionOperation", "generateSessionId", "Error inserting into Session table");

				query.rollBackConnection();
				return null;
			}

			query.commit();
			addSessionCacheData(sessionid, cachemodel);

			return sessionid;
		} catch (Exception e) {
			LoggerSet.logError("SessionOperation", "generateSessionId", "Exception occurred", e);

			throw new DBOperationException(e.getMessage());

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
					LoggerSet.logInfo("SessionOperation", "getCustomSessionId",
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
	 * @param sessionID the session ID to delete
	 * @return true if the session was deleted successfully, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static boolean DeleteSessionData(String sessionID) throws DBOperationException {

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();
			if (sessionID != null) {
				int[] result = { -1, -1 };
				CacheModel cachemodel = CacheData.getCache(sessionID);
				com.zohocontacts.dbpojo.Session session = cachemodel.getsession(sessionID);
				long userID = session.getUserId();

				result = query.delete(session).execute(userID);
				if (result[0] != -1) {
					LoggerSet.logWarning("SessionOperation", "DeleteSessionData", "Session deleted successfully");

				} else {
					LoggerSet.logWarning("SessionOperation", "DeleteSessionData", "Failed to delete Session");

				}
			} else {
				LoggerSet.logWarning("SessionOperation", "DeleteSessionData", "Session ID is null");
				return false;
			}
			CacheData.deleteAllCache(sessionID);
			return true;
		} catch (Exception e) {
			LoggerSet.logError("SessionOperation", "DeleteSessionData", "Exception occurred", e);

			throw new DBOperationException(e.getMessage());

		}

	}

	/**
	 * Checks if a session is still alive and updates its expiration time.
	 *
	 * @param sessionID the session ID to check
	 * @return the user ID associated with the session if alive, 0 if not
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static CacheModel checkSessionAlive(String sessionID) throws DBOperationException {

		List<Table> resultList = new ArrayList<>();
		long currentTime = Instant.now().toEpochMilli();
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();
			CacheModel cacheModel = CacheData.getCache(sessionID);
			if (cacheModel != null && cacheModel.getsession(sessionID) != null) {

				if (cacheModel.getsession(sessionID).getLastAccessed() + SESSIONTIMEOUT < currentTime) {

					CacheData.deleteAllCache(sessionID);
					return null;
				}
				cacheModel.getsession(sessionID).setLastAccessed(Instant.now().toEpochMilli());
				return cacheModel;

			} else {

				cacheModel = new CacheModel();
				com.zohocontacts.dbpojo.Session session = new com.zohocontacts.dbpojo.Session();
				session.setSessionID(sessionID);
				resultList = query.select(session).executeQuery();
				if (resultList.size() > 0) {
					session = (com.zohocontacts.dbpojo.Session) resultList.getFirst();
					long sessionTimeout = session.getLastAccessed() + SESSIONTIMEOUT;
					if (sessionTimeout < currentTime) {
						return null;
					}

					if (CacheData.getUsercache(session.getUserId()) != null) {
						cacheModel = CacheData.getUsercache(session.getUserId());
						cacheModel.setSession(session);
					} else {

//						
						UserData user = UserOperation.getUserData(session.getUserId());
						if (user != null) {
//							
							cacheModel.setUserData(user);

						} else {
							LoggerSet.logInfo("SessionOperation", "checkSessionAlive",
									"user not Found for userID " + session.getUserId());
							return null;
						}
						cacheModel.setSession(session);

						SessionOperation.addSessionCacheData(sessionID, cacheModel);

					}
				} else {
					return null;
				}

				LoggerSet.logInfo("SessionOperation", "checkSessionAlive",
						"Updated session expiration time: " + new Timestamp(currentTime));

				return cacheModel;

			}

		} catch (Exception e) {
			LoggerSet.logError("SessionOperation", "checkSessionAlive", "Exception occurred", e);

			throw new DBOperationException(e.getMessage());

		}
	}

	private static void addSessionCacheData(String sessionid, CacheModel cachemodel) throws DBOperationException {

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();
			if (CacheData.getsessionMapper().size() >= 100) {
				long maxTime = 0;
				long userID = 0;
				long currentTime = Instant.now().toEpochMilli();
				com.zohocontacts.dbpojo.Session maxSessionid = new com.zohocontacts.dbpojo.Session();

				for (String sessionID : CacheData.getsessionMapper().keySet()) {
					CacheModel cacheModel = CacheData.getCache(sessionID);
					if (currentTime - cacheModel.getsession(sessionid).getLastAccessed() > maxTime) {
						maxTime = currentTime - cacheModel.getsession(sessionid).getLastAccessed();
						maxSessionid = cacheModel.getsession(sessionid);
						userID = maxSessionid.getUserId();
					}
				}

				query.update(maxSessionid).execute(userID);

				CacheData.deleteAllCache(maxSessionid.getSessionId());

			}

			CacheData.addViewCache(sessionid, cachemodel);

		} catch (Exception e) {
			LoggerSet.logError("SessionOperation", "addSessionCacheData(", "Exception occurred", e);

			throw new DBOperationException(e.getMessage());

		}

	}

}
