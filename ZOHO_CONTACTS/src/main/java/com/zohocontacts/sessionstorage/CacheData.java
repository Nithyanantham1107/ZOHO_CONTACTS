package com.zohocontacts.sessionstorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zohocontacts.dbpojo.ServerRegistry;
import com.zohocontacts.dbpojo.Session;

public class CacheData {

	private static final int SIZE = 100;
	private static Map<Long, CacheModel> userCache = new ConcurrentHashMap<Long, CacheModel>();
	private static Map<String, com.zohocontacts.dbpojo.Session> sessionMapper = new ConcurrentHashMap<String, com.zohocontacts.dbpojo.Session>();
	private static ServerRegistry serverInfo;
	private static List<ServerRegistry> servers = new ArrayList<ServerRegistry>();

	public static void setServerInfo(ServerRegistry server) {

		serverInfo = server;
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

		return userCache.get(userID);
	}

	public static void addUserCache(com.zohocontacts.dbpojo.Session sessionID, CacheModel cachedata) {
		if (userCache.get(cachedata.getUserData().getID()) == null) {

			if (userCache.size() >= SIZE) {
				long userID = -1;
				long userCacheLastaccessed = Long.MAX_VALUE;
				for (CacheModel userCache : userCache.values()) {

					if (userCache.getLastAccessed() < userCacheLastaccessed) {

						userID = userCache.getUserData().getID();
					}

				}

				userCache.remove(userID);
			}
			userCache.put(cachedata.getUserData().getID(), cachedata);
		}
		sessionID.setUserId(cachedata.getUserData().getID());
		sessionMapper.put(sessionID.getSessionId(), sessionID);
	}

	public static CacheModel getCache(String sessionid) {

		if (sessionMapper.get(sessionid) == null) {
			return null;
		}
		return userCache.get(sessionMapper.get(sessionid).getUserId());

	}

	public static void deleteAllCache(String sessionid) {

		if (sessionMapper.get(sessionid) != null) {

			long userId = sessionMapper.get(sessionid).getUserId();
			sessionMapper.remove(sessionid);
			boolean state = false;
			for (com.zohocontacts.dbpojo.Session session : sessionMapper.values()) {
				if (session.getUserId() == userId) {
					state = true;
					break;
				}

			}
			if (!state) {
				userCache.remove(userId);
			}

		}

	}

	public static void addSessionMapperCache(Session session) {
		if (userCache.get(session.getUserId()) != null) {
			sessionMapper.put(session.getSessionId(), session);

		}
	}

	public static Map<String, com.zohocontacts.dbpojo.Session> getsessionMapper() {
		return sessionMapper;
	}

}
