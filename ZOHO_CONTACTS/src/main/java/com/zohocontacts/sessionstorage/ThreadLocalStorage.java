package com.zohocontacts.sessionstorage;

public class ThreadLocalStorage {

	private static final ThreadLocal<CacheModel> currentUserCache = ThreadLocal.withInitial(() -> new CacheModel());

	public static void setCurrentUserCache(CacheModel userCache) {
		currentUserCache.set(userCache);
	}

	public static CacheModel getCurrentUserCache() {
		return currentUserCache.get();
	}

	public static void remove() {
		currentUserCache.remove();
	}
}
