package com.zohocontacts.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.zohocontacts.dboperation.ServerRegistryOperation;
import com.zohocontacts.dbpojo.ServerRegistry;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.schedulers.AccessTokenRenewal;
import com.zohocontacts.schedulers.SessionTableCleaner;
import com.zohocontacts.schedulers.SyncGoogleContacts;
import com.zohocontacts.schedulers.UpdateSessionCache;
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.SessionCacheHandler;

public class ServerListener implements ServletContextListener {
	private ScheduledExecutorService AppScheduler;

	private static final String servletPath = "/servercache";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("The context here started see !!..");
		LoggerSet.appLog();
		LoggerSet.accessLog();
		ServletContext context = sce.getServletContext();
		String port = context.getInitParameter("serverPort");
		serverCacheSet(port);

		AppScheduler = Executors.newScheduledThreadPool(2);
		AppScheduler.scheduleAtFixedRate(new UpdateSessionCache(), 0, 2, TimeUnit.MINUTES);

		AppScheduler.scheduleAtFixedRate(new SessionTableCleaner(), 0, 10, TimeUnit.MINUTES);
		AppScheduler.scheduleAtFixedRate(new AccessTokenRenewal(), 0, 5, TimeUnit.MINUTES);
		AppScheduler.scheduleAtFixedRate(new SyncGoogleContacts(), 0, 10, TimeUnit.MINUTES);

		System.out.println("Update queue Scheduler started");
		System.out.println("Session Table Cleaner Scheduler started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("The context ends here see...!!");
		deleteServerCache();
		shutdownScheduler(AppScheduler);

		AbandonedConnectionCleanupThread.checkedShutdown();

		System.out.println("Scheduled tasks have been shut down successfully.");
	}

	private void shutdownScheduler(ScheduledExecutorService scheduler) {
		if (scheduler != null) {
			scheduler.shutdown();
			try {
				if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
					scheduler.shutdownNow();
				}
			} catch (InterruptedException e) {
				scheduler.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

	private void serverCacheSet(String port) {
		InetAddress inetAddress;
		String serverIP;
		try {
			inetAddress = InetAddress.getLocalHost();
			serverIP = inetAddress.getHostAddress();
			long portNumber = Long.valueOf(port);

			ServerRegistry server = ServerRegistryOperation.insertServerRegistry(serverIP, portNumber);
			if (server != null) {
				CacheData.setServerInfo(server);
				List<ServerRegistry> servers = ServerRegistryOperation.getServerRegistryExcept(server);
				CacheData.setServers(servers);

				boolean cacheDeleteSuccessful = SessionCacheHandler.sendServerCacheDeleteRequest(CacheData.getServers(),
						servletPath);
				if (cacheDeleteSuccessful) {
					System.out.println("Server registry cache in other server deleted successfully.");
				} else {
					System.out.println("Server registry cache in other server failed to delete.");
				}
			}

		} catch (DBOperationException | UnknownHostException e) {
			LoggerSet.logError("servletListener", "contextInitialized", "Exception occured", e);

			e.printStackTrace();
		}

	}

	private void deleteServerCache() {

		try {

			ServerRegistryOperation.deleteServerRegistry(CacheData.getServerInfo());

			boolean cacheDeleteSuccessful = SessionCacheHandler.sendServerCacheDeleteRequest(CacheData.getServers(),
					servletPath);
			if (cacheDeleteSuccessful) {
				System.out.println("Server registry cache in other server deleted successfully.");
			} else {
				System.out.println("Server registry cache in other server failed to delete.");
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("servletListener", "contextDestroy", "Exception occured", e);
			e.printStackTrace();
		}

	}
}
