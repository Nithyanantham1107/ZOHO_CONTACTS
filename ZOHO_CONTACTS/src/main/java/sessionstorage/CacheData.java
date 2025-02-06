package sessionstorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CacheData {

	private static final int SIZE = 100;
	private static ConcurrentHashMap<Integer, CacheModel> viewcache = new ConcurrentHashMap<Integer, CacheModel>();
	private static HashMap<String, Integer> sessionMapper = new HashMap<String, Integer>();
	private static BlockingQueue<String> primaryUpdateQueue = new ArrayBlockingQueue<String>(100);
	private static BlockingQueue<String> secondaryUpdateQueue = new ArrayBlockingQueue<String>(100);
	private static Boolean active = false;
	
	private static HashSet<String> deleteSet=new HashSet<String>();

	public void addDeleteContactID(String resourceName) {
		
		deleteSet.add(resourceName);
		
	}
	
	
	
	public static void setSecondaryActive() {
		active = true;
	}

	public static void setSecondaryInactive() {
		active = false;
	}

	public static void updateQueue(String sessionid) {
		if (active) {
			secondaryUpdateQueue.add(sessionid);
		} else {
			primaryUpdateQueue.add(sessionid);
		}

	}

	public static CacheModel getUsercache(Integer userID) {

		return viewcache.get(userID);
	}

	public static void addViewCache(String sessionid, CacheModel cachedata) {
		if (viewcache.get(cachedata.getUserData().getID()) == null) {

			if (viewcache.size() >= SIZE) {
				Integer userID = -1;
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

		if(sessionMapper.get(sessionid)==null) {
			return null;
		}
		return viewcache.get(sessionMapper.get(sessionid));

	}

	public static boolean checkCacheQueue(String sessionid) {

		if (primaryUpdateQueue.contains(sessionid) || secondaryUpdateQueue.contains(sessionid)) {
			return true;
		} else {
			return false;
		}
	}

	public static void deleteAllCache(String sessionid) {

		if (primaryUpdateQueue.contains(sessionid)) {
			primaryUpdateQueue.remove(sessionid);
		}
		if (secondaryUpdateQueue.contains(sessionid)) {
			secondaryUpdateQueue.remove(sessionid);
		}

		if (sessionMapper.get(sessionid) != null) {

			int userId = sessionMapper.get(sessionid);
			sessionMapper.remove(sessionid);
			boolean state = false;
			for (int i : sessionMapper.values()) {
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

	public static HashMap<String, Integer> getsessionMapper() {
		return sessionMapper;
	}

	public static BlockingQueue<String> getPrimaryUpdateQueue() {
		return primaryUpdateQueue;
	}

	public static BlockingQueue<String> getSecondaryUpdateQueue() {
		return secondaryUpdateQueue;
	}

	public static void removePrimaryUpdateQueue(String sessionid) {

		if (primaryUpdateQueue.contains(sessionid)) {
			primaryUpdateQueue.remove(sessionid);
		}
	}

	public static void transferSecondaryToPrimary() {
		for (String sessionid : secondaryUpdateQueue) {
			if (!primaryUpdateQueue.contains(sessionid)) {
				primaryUpdateQueue.add(sessionid);
			}
			secondaryUpdateQueue.remove(sessionid);
		}
	}

}
