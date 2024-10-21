package sessionstorage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CacheData {
	 private static HashMap<String, CacheModel> viewcache=new HashMap<String, CacheModel>();
	 private static BlockingQueue<String> primaryUpdateQueue=new ArrayBlockingQueue<String>(100);
	 private static BlockingQueue<String> secondaryUpdateQueue=new ArrayBlockingQueue<String>(100);
	 private static Boolean active=false;
	 
	 
	 
	 public static void updateQueue(String sessionid) {
		 if( active) {
			secondaryUpdateQueue.add(sessionid);
		 }else {
			 primaryUpdateQueue.add(sessionid);
		 }
		 
	 }
	 
	 public static void addViewCache(String sessionid,CacheModel cachedata) {
		 viewcache.put(sessionid, cachedata);
	 }
	
	
	

}
