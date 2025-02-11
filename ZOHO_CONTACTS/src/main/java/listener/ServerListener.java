package listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import schedulers.SessionTableCleaner;
import schedulers.UpdateAndDeleteQueue;
import schedulers.DeleteOauthContactScheduler;

public class ServerListener implements ServletContextListener {
	QueryBuilder qg;
	private ScheduledExecutorService updateCacheSchedule;
	private ScheduledExecutorService sessionTableCleanSchedule;
	private ScheduledExecutorService deleteOauthContactSchedule;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("the context here started see !!..");
		LoggerSet.appLog();
		LoggerSet.accessLog();

		deleteOauthContactSchedule = Executors.newScheduledThreadPool(1);
		deleteOauthContactSchedule.scheduleAtFixedRate(new DeleteOauthContactScheduler(), 0, 30, TimeUnit.SECONDS);

		updateCacheSchedule = Executors.newScheduledThreadPool(1);
		updateCacheSchedule.scheduleAtFixedRate(new UpdateAndDeleteQueue(), 0, 1, TimeUnit.MINUTES);
		sessionTableCleanSchedule = Executors.newScheduledThreadPool(1);
		sessionTableCleanSchedule.scheduleAtFixedRate(new SessionTableCleaner(), 0, 5, TimeUnit.MINUTES);
		System.out.println("Update queue Scheduler started");
		System.out.println("Session Table Cleaner Scheduler started");

		System.out.println("Delete Oauth Contact  Scheduler started");
	}
	

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("the context ends here see...!!");
		if (deleteOauthContactSchedule != null) {
			deleteOauthContactSchedule.shutdown();
			try {
				if (!deleteOauthContactSchedule.awaitTermination(60, TimeUnit.SECONDS)) {
					deleteOauthContactSchedule.shutdownNow();
				}
			} catch (InterruptedException e) {
				deleteOauthContactSchedule.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}


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

		System.out.println("Scheduled tasks have been shut down successfully.");

		if (sessionTableCleanSchedule != null) {
			sessionTableCleanSchedule.shutdown();
			try {
				if (!sessionTableCleanSchedule.awaitTermination(60, TimeUnit.SECONDS)) {
					sessionTableCleanSchedule.shutdownNow();
				}
			} catch (InterruptedException e) {
				sessionTableCleanSchedule.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}

		System.out.println("Scheduled tasks Ses have been shut down successfully.");

	}

}
