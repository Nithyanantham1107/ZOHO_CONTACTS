package com.zohocontacts.sessionstorage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.zohocontacts.dbpojo.ServerRegistry;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;

public class SessionCacheHandler {

	
	public static Boolean sendCacheDeleteRequest(List<ServerRegistry> servers, String servletPath, String sessionID)
			throws DBOperationException {

		try {
			if (servers != null && servers.size() > 0) {
				for (ServerRegistry server : servers) {
					String session = "sessionID=" + sessionID;
					sendPostRequestToServer(server.getServerIP(), server.getServerPort(), servletPath, session);

				}
				LoggerSet.logInfo("SessionCacheHandler", "sendServerCacheDeleteRequest",
						"Clear Cache request sent successfully for sessinID" + sessionID);
				return true;
			} else {

				LoggerSet.logInfo("SessionCacheHandler", "sendServerCacheDeleteRequest", "Empty server as parameters");

				return false;
			}
		} catch (Exception e) {
			LoggerSet.logError("SessionCacheHandler", "sendServerCacheDeleteRequest",
					"Exception occurred: " + e.getMessage(), e);
			throw new DBOperationException(e.getMessage());

		}

	}

	public static Boolean sendServerCacheDeleteRequest(List<ServerRegistry> servers, String servletPath)
			throws DBOperationException {

		try {
			if (servers != null && servers.size() > 0) {

				for (ServerRegistry server : servers) {

					sendPostRequestToServer(server.getServerIP(), server.getServerPort(), servletPath, null);

				}
				LoggerSet.logInfo("SessionCacheHandler", "sendServerCacheDeleteRequest",
						"Clear Cache request sent successfully for server");
				return true;
			} else {

				LoggerSet.logInfo("SessionCacheHandler", "sendServerCacheDeleteRequest",
						"Cache is Empty for servers to delete Cache");

				return false;
			}

		} catch (Exception e) {
			LoggerSet.logError("SessionCacheHandler", "sendServerCacheDeleteRequest",
					"Exception occurred: " + e.getMessage(), e);
			throw new DBOperationException(e.getMessage());

		}

	}

	private static void sendPostRequestToServer(String IP, long port, String servletPath, String body) {

		try {
			URL url = new URL("http://" + IP + ":" + port + servletPath);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setReadTimeout(5000);
			connection.setConnectTimeout(5000);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if (body != null) {
				try (OutputStream os = connection.getOutputStream()) {
					byte[] input = body.getBytes("utf-8");
					os.write(input, 0, input.length);
				}

			}

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				System.out.println("Response from Server B: " + response.toString());
			} else {
				System.out.println("Failed to connect to Server B. Response Code: " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
