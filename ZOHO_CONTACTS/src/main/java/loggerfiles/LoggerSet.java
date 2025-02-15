package loggerfiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerSet {

	private static final Logger applog = Logger.getLogger("AppLogger");

	public static void appLog() {
		try {
			String logFilePath = "/home/nithya-pt7676/git/ZOHO_CONTACTS/ZOHO_CONTACTS/application.log";
			File logFile = new File(logFilePath);
			FileHandler fileHandler;
			if (logFile.exists()) {
				fileHandler = new FileHandler(logFilePath, true); // Append mode
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

	private static final Logger accesslog = Logger.getLogger("AccessLogger");

	public static void accessLog() {
		try {

			String logFilePath = "/home/nithya-pt7676/git/ZOHO_CONTACTS/ZOHO_CONTACTS/access.log";
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

	public void logAccessSet(String clientIp, String resource, String httpMethod, int responseStatus,
			String userAgent) {

		String timestamp = java.time.LocalDateTime.now().toString();

		String logMessage = String.format(
				"%s | Client IP: %s | Resource: %s | Method: %s | Status: %d | User Agent: %s ", timestamp, clientIp,
				resource, httpMethod, responseStatus, userAgent);

		accesslog.info(logMessage);
	}

	public String logAction(String className, String methodName, String Message) {

		long timestamp = System.currentTimeMillis();
		String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new java.util.Date(timestamp));

		String logEntry = String.format("%s | %s | %s | %s ", formattedDate, className, methodName, Message);

		System.out.println(logEntry);
		return logEntry;
	}

	public void logInfo(String className, String methodName, String message) {
		applog.info(logAction(className, methodName, message));
	}

	public void logError(String className, String methodName, String message, Throwable throwable) {
		applog.log(Level.SEVERE, logAction(className, methodName, message), throwable);
	}

	public void logWarning(String className, String methodName, String message) {
		applog.log(Level.WARNING, logAction(className, methodName, message));
	}

}
