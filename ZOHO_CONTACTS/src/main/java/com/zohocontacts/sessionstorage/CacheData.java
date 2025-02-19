package com.zohocontacts.sessionstorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zohocontacts.dbpojo.ServerRegistry;
import com.zohocontacts.dbpojo.Session;

public class CacheData {

	private static final int SIZE = 100;
	private static final Map<Long, CacheModel> USERCACHE = new ConcurrentHashMap<Long, CacheModel>();
	private static final Map<String, com.zohocontacts.dbpojo.Session> SESSIONMAPPER = new ConcurrentHashMap<String, com.zohocontacts.dbpojo.Session>();

	private static final Map<String, Integer> PAGENUMBER = new ConcurrentHashMap<String, Integer>();

	private static ServerRegistry serverInfo;
	private static List<ServerRegistry> servers = new ArrayList<ServerRegistry>();

	public static void setServerInfo(ServerRegistry server) {

		serverInfo = server;
	}

	public static void setPageNumber(String sessionID, int pageNumber) {
		PAGENUMBER.put(sessionID, pageNumber);

	}

	public static int getPageNumber(String sessionID) {

		return PAGENUMBER.get(sessionID);

	}

	public static ServerRegistry getServerInfo() {

		return serverInfo;
	}

	public static void setServers(List<ServerRegistry> server) {

		servers = server;
	}

	public static List<ServerRegistry> getServers() {
		return servers;
	}

	public static CacheModel getUsercache(long userID) {

		return USERCACHE.get(userID);
	}

	public static void addUserCache(com.zohocontacts.dbpojo.Session sessionID, CacheModel cachedata) {
		if (USERCACHE.get(cachedata.getUserData().getID()) == null) {

			if (USERCACHE.size() >= SIZE) {
				long userID = -1;
				long userCacheLastaccessed = Long.MAX_VALUE;
				for (CacheModel userCache : USERCACHE.values()) {

					if (userCache.getLastAccessed() < userCacheLastaccessed) {

						userID = userCache.getUserData().getID();
					}

				}

				USERCACHE.remove(userID);
			}
			USERCACHE.put(cachedata.getUserData().getID(), cachedata);
		}
		sessionID.setUserId(cachedata.getUserData().getID());

		SESSIONMAPPER.put(sessionID.getSessionId(), sessionID);

		PAGENUMBER.put(sessionID.getSessionId(), 1);
	}

	public static CacheModel getCache(String sessionid) {

		if (SESSIONMAPPER.get(sessionid) == null) {
			return null;
		}
		return USERCACHE.get(SESSIONMAPPER.get(sessionid).getUserId());

	}

	public static void deleteAllCache(String sessionid) {

		if (SESSIONMAPPER.get(sessionid) != null) {

			long userId = SESSIONMAPPER.get(sessionid).getUserId();
			PAGENUMBER.remove(sessionid);
			SESSIONMAPPER.remove(sessionid);
			boolean state = false;
			for (com.zohocontacts.dbpojo.Session session : SESSIONMAPPER.values()) {
				if (session.getUserId() == userId) {
					state = true;
					break;
				}

			}
			if (!state) {
				USERCACHE.remove(userId);
			}

		}

	}

	public static void addSessionMapperCache(Session session) {
		if (USERCACHE.get(session.getUserId()) != null) {
			SESSIONMAPPER.put(session.getSessionId(), session);
			PAGENUMBER.put(session.getSessionId(), 1);

		}
	}

	public static Map<String, com.zohocontacts.dbpojo.Session> getsessionMapper() {
		return SESSIONMAPPER;
	}

}
