package listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import loggerfiles.LoggerSet;
import schedulers.SessionTableCleaner;
import schedulers.UpdateAndDeleteQueue;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

public class ServerListener implements ServletContextListener {
 QueryBuilder qg;
	private ScheduledExecutorService updateCacheSchedule;
//	private ScheduledExecutorService sessionDBCleaner;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("the context here started see !!..");
		LoggerSet.appLog();
		LoggerSet.accessLog();
//		qg=new SqlQueryLayer().createQueryBuilder();
//		qg.openConnection();
		
//		 if(new SqlQueryLayer().createQueryBuilder() ==null) {
//			 
//			 System.out.println("connection here in listner is null");
//		 }
		updateCacheSchedule = Executors.newScheduledThreadPool(1);
		updateCacheSchedule.scheduleAtFixedRate(new UpdateAndDeleteQueue(), 0, 1,
				TimeUnit.MINUTES);

		System.out.println("Update queue Scheduler started");
//		sessionDBCleaner = Executors.newScheduledThreadPool(1);
//		sessionDBCleaner.scheduleAtFixedRate(new SessionTableCleaner(qg), 0, 5,
//				TimeUnit.MINUTES);
//		System.out.println("Session DB cleaner Scheduler started");


	
	}
	

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("the context ends here see...!!");
// qg.closeConnection();
		
		if (updateCacheSchedule != null) {
			updateCacheSchedule.shutdown();
			try {
				if (!updateCacheSchedule.awaitTermination(60, TimeUnit.SECONDS)) {
					updateCacheSchedule.shutdownNow(); 
				}
			} catch (InterruptedException e) {
				updateCacheSchedule.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}

//		if (sessionDBCleaner != null) {
//			sessionDBCleaner.shutdown(); // Initiates an orderly shutdown
//			try {
//				if (!sessionDBCleaner.awaitTermination(60, TimeUnit.SECONDS)) {
//					sessionDBCleaner.shutdownNow(); 
//				}
//			} catch (InterruptedException e) {
//				sessionDBCleaner.shutdownNow();
//				Thread.currentThread().interrupt();
//			}
//		}

		System.out.println("Scheduled tasks have been shut down successfully.");

	}

}
