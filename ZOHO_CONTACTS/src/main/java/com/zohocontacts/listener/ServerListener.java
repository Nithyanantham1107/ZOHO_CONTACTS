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

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dboperation.ServerRegistryOperation;
import com.zohocontacts.dbpojo.ServerRegistry;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.schedulers.SessionTableCleaner;
import com.zohocontacts.schedulers.UpdateAndDeleteQueue;
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.SessionCacheHandler;

public class ServerListener implements ServletContextListener {
	QueryBuilder qg;
	private ScheduledExecutorService updateCacheSchedule;
	private ScheduledExecutorService sessionTableCleanSchedule;

	private String servletPath = "/servercache";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("the context here started see !!..");
		InetAddress inetAddress;
		String serverIP;

		try {

			inetAddress = InetAddress.getLocalHost();
			serverIP = inetAddress.getHostAddress();
			ServletContext context = sce.getServletContext();
			String port = context.getInitParameter("serverPort");
			long portNumber = Long.valueOf(port);
			LoggerSet.appLog();
			LoggerSet.accessLog();

			ServerRegistry server = ServerRegistryOperation.insertServerRegistry(serverIP, portNumber);
			if (server != null) {

				CacheData.setServerInfo(server);
				List<ServerRegistry> servers = ServerRegistryOperation.getServerRegistryExcept(server);
				CacheData.setServers(servers);

				SessionCacheHandler.sendServerCacheDeleteRequest(CacheData.getServers(), servletPath);

			}

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (DBOperationException e) {

			e.printStackTrace();
		}

		updateCacheSchedule = Executors.newScheduledThreadPool(1);
		updateCacheSchedule.scheduleAtFixedRate(new UpdateAndDeleteQueue(), 0, 5, TimeUnit.MINUTES);
		sessionTableCleanSchedule = Executors.newScheduledThreadPool(1);
		sessionTableCleanSchedule.scheduleAtFixedRate(new SessionTableCleaner(), 0, 5, TimeUnit.MINUTES);
		System.out.println("Update queue Scheduler started");
		System.out.println("Session Table Cleaner Scheduler started");
		System.out.println("Delete Oauth Contact  Scheduler started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

		System.out.println("the context ends here see...!!");

		try {
			ServerRegistryOperation.deleteServerRegistry(CacheData.getServerInfo());

			if (SessionCacheHandler.sendServerCacheDeleteRequest(CacheData.getServers(), servletPath)) {

				System.out.println("Server registry cache in other server deleted  successfully.");

			} else {

				System.out.println("server registry cache in other server failed to delete .");

			}
		} catch (DBOperationException e) {
			e.printStackTrace();
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
