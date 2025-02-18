package com.zohocontacts.sessionstorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserCache implements Map<Long, CacheModel>{

	
	private Map<Long,CacheModel> users;
	public UserCache() {
		
		
		users=new HashMap<Long, CacheModel>();
	}
	@Override
	public int size() {
		
		return users.size();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CacheModel get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CacheModel put(Long key, CacheModel value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CacheModel remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends Long, ? extends CacheModel> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Long> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CacheModel> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<Long, CacheModel>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
