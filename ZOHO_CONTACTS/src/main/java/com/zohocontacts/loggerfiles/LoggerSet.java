package com.zohocontacts.loggerfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerSet {

	private static final Logger applog = Logger.getLogger("AppLogger");
	private static final Logger accesslog = Logger.getLogger("AccessLogger");
	private static Properties properties = new Properties();

	static {

		String currentWorkingDirectory = System.getProperty("user.dir");

		String logConfigPath = currentWorkingDirectory + "/git/ZOHO_CONTACTS/ZOHO_CONTACTS/config.properties";

		try (FileInputStream fis = new FileInputStream(logConfigPath)) {
			properties.load(fis);
		} catch (IOException e) {
			System.err.println("Failed to load config.properties: " + e.getMessage());
		}
	}

	public static void appLog() {
		try {
			String logFilePath = properties.getProperty("application.log.path", "/default/path/application.log");
			File logFile = new File(logFilePath);
			FileHandler fileHandler;

			if (logFile.exists()) {
				fileHandler = new FileHandler(logFilePath, true);
			} else {
				fileHandler = new FileHandler(logFilePath);
				System.out.println("Application log file created: " + logFilePath);
			}

			fileHandler.setFormatter(new SimpleFormatter());
			applog.addHandler(fileHandler);
			applog.setLevel(Level.ALL);

		} catch (IOException e) {
			applog.log(Level.SEVERE, "Failed to initialize file handler.", e);
		}
	}

	public static void accessLog() {
		try {
			String logFilePath = properties.getProperty("access.log.path", "/default/path/access.log");
			File logFile = new File(logFilePath);
			FileHandler fileHandler;

			if (logFile.exists()) {
				fileHandler = new FileHandler(logFilePath, true);
			} else {
				fileHandler = new FileHandler(logFilePath);
				System.out.println("Access log file created: " + logFilePath);
			}

			fileHandler.setFormatter(new SimpleFormatter());
			accesslog.addHandler(fileHandler);
			accesslog.setLevel(Level.ALL);

		} catch (IOException e) {
			accesslog.log(Level.SEVERE, "Failed to initialize file handler.", e);
		}
	}

	public static void logAccessSet(String clientIp, String resource, String httpMethod, int responseStatus,
			String userAgent) {
		String timestamp = java.time.LocalDateTime.now().toString();

		String logMessage = String.format(
				"%s | Client IP: %s | Resource: %s | Method: %s | Status: %d | User Agent: %s ", timestamp, clientIp,
				resource, httpMethod, responseStatus, userAgent);

		accesslog.info(logMessage);
	}

	public static String logAction(String className, String methodName, String Message) {
		long timestamp = System.currentTimeMillis();
		String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new java.util.Date(timestamp));

		String logEntry = String.format("%s | %s | %s | %s ", formattedDate, className, methodName, Message);

		System.out.println(logEntry);
		return logEntry;
	}

	public static void logInfo(String className, String methodName, String message) {
		applog.info(logAction(className, methodName, message));
	}

	public static void logError(String className, String methodName, String message, Throwable throwable) {
		applog.log(Level.SEVERE, logAction(className, methodName, message), throwable);
	}

	public static void logWarning(String className, String methodName, String message) {
		applog.log(Level.WARNING, logAction(className, methodName, message));
	}

}
