package schedulers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbconnect.DBconnection;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

public class UpdateQueueSchedule  implements Runnable{
	
	public void run() {
		System.out.println("hey here is the update queue");
		Connection con = DBconnection.getConnection();
		LoggerSet logger = new LoggerSet();
		try {
			
			int sessiontimeout=30*60;
			CacheData.setSecondaryActive();
			
			for(String sessionid : CacheData.getPrimaryUpdateQueue()) {
				 CacheModel cachemodel=CacheData.getCache(sessionid);
				 if(cachemodel !=null) {
					 
					 
					 
					 PreparedStatement ps = con.prepareStatement("UPDATE Session SET session_expire = ? WHERE session_id = ?");
			         ps.setLong(1, cachemodel.getlastAccessed()+ sessiontimeout);
			         ps.setString(2, sessionid);
			         int result = ps.executeUpdate();
			         if (result == 0) {
			        	 System.out.println("Error in updating session data to the table ,session id"+sessionid);
			             
			             
			             
			         }
			         CacheData.removePrimaryUpdateQueue(sessionid);
				 }else {
					 System.out.println("here the cache data is null for session id"+sessionid);
				 }
				 
			
			
		}
		CacheData.setSecondaryInactive();
		CacheData.transferSecondaryToPrimary();
			
		}catch(Exception e) {
			System.out.println(e);
			logger.logError("Servletlistener", "UpdateQueueSchedule", "Error updating the session table: " ,e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
			
			
		
		
		
		
		
	}

}
