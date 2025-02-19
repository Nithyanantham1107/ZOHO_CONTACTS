package com.zohocontacts.sessionstorage;

public class ThreadLocalStorage {

	private static final ThreadLocal<CacheModel> CURRENTUSERCACHE = ThreadLocal.withInitial(() -> new CacheModel());

	public static void setCurrentUserCache(CacheModel userCache) {
		CURRENTUSERCACHE.set(userCache);
	}

	public static CacheModel getCurrentUserCache() {
		return CURRENTUSERCACHE.get();
	}

	public static void remove() {
		CURRENTUSERCACHE.remove();

	}

}
