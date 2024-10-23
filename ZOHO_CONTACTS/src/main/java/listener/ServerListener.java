package listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import loggerfiles.LoggerSet;
import querybuilder.SqlQueryBuilder;
import schedulers.SessionTableCleaner;
import schedulers.UpdateQueueSchedule;
import querybuilder.QueryBuilder;

public class ServerListener implements ServletContextListener {

	private ScheduledExecutorService updateCacheSchedule;
	private ScheduledExecutorService sessionDBCleaner;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("the context here started see !!..");
		LoggerSet.appLog();
		LoggerSet.accessLog();
		updateCacheSchedule = Executors.newScheduledThreadPool(1);
		updateCacheSchedule.scheduleAtFixedRate(new UpdateQueueSchedule(), 0, 1, TimeUnit.MINUTES);

		System.out.println("Update queue Scheduler started");
		sessionDBCleaner = Executors.newScheduledThreadPool(1);
		sessionDBCleaner.scheduleAtFixedRate(new SessionTableCleaner(), 0, 5, TimeUnit.MINUTES);
		System.out.println("Session DB cleaner Scheduler started");

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("the context ends here see...!!");

		if (updateCacheSchedule != null) {
			updateCacheSchedule.shutdown();
			System.out.println(" Update Cache Queue Scheduler shut down.");
		}
		if (sessionDBCleaner != null) {
			sessionDBCleaner.shutdown();
			System.out.println("session DB cleaner Scheduler shut down.");
		}

	}

}
