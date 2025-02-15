package com.zohocontacts.sessionstorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zohocontacts.dbpojo.ServerRegistry;

public class CacheData {

	private static final int SIZE = 100;
	private static Map<Long, CacheModel> viewcache = new ConcurrentHashMap<Long, CacheModel>();
	private static Map<String, Long> sessionMapper = new ConcurrentHashMap<String, Long>();
	private static Map<String, Long> secondarySessionMapper = new HashMap<String, Long>();
	private static Boolean active = false;
	private static Map<String, Long> deleteContact = new ConcurrentHashMap<String, Long>();
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

	public static void addDeleteContactID(String resourceName, long OauthID) {
		deleteContact.put(resourceName, OauthID);
	}

	public static void clearDeletecontact() {
		deleteContact.clear();

	}

	public static Map<String, Long> getDeleteCache() {
		return deleteContact;
	}

	public static CacheModel getUsercache(long userID) {

		return viewcache.get(userID);
	}

	public static void addViewCache(String sessionid, CacheModel cachedata) {
		if (viewcache.get(cachedata.getUserData().getID()) == null) {

			if (viewcache.size() >= SIZE) {
				long userID = -1;
				long userCacheLastaccessed = Long.MAX_VALUE;
				for (CacheModel userCache : viewcache.values()) {

					if (userCache.getLastAccessed() < userCacheLastaccessed) {

						userID = userCache.getUserData().getID();
					}

				}

				viewcache.remove(userID);
			}
			viewcache.put(cachedata.getUserData().getID(), cachedata);
		}
		sessionMapper.put(sessionid, cachedata.getUserData().getID());
	}

	public static CacheModel getCache(String sessionid) {

		if (sessionMapper.get(sessionid) == null) {
			return null;
		}
		return viewcache.get(sessionMapper.get(sessionid));

	}

	public static void deleteAllCache(String sessionid) {

		if (sessionMapper.get(sessionid) != null) {

			long userId = sessionMapper.get(sessionid);
			sessionMapper.remove(sessionid);
			boolean state = false;
			for (long i : sessionMapper.values()) {
				if (i == userId) {
					state = true;
					break;
				}

			}
			if (!state) {
				viewcache.remove(userId);
			}

		}

	}

	public static Map<String, Long> getsessionMapper() {
		return sessionMapper;
	}

}
