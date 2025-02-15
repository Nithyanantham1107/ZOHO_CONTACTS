package oauth2helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.Oauth;
import querybuilderconfig.TableSchema.OauthProvider;

public class Oauth2handler {

	public static String getAuthorizationURl() throws IOException {

		String authorizationUrl = Outh2Credentials.getAuthorizationURL() + "?response_type=code" + "&client_id="
				+ Outh2Credentials.getClientID() + "&redirect_uri="
				+ URLEncoder.encode(Outh2Credentials.getRedirectURI(), "UTF-8") + "&scope="
				+ URLEncoder.encode(Outh2Credentials.getScope(), "UTF-8") + "&access_type=offline&prompt=consent";

		return authorizationUrl;
	}

	public static Oauth getAccessToken(String AuthorizationCode, HttpServletResponse response, long userID,
			Boolean state) throws IOException {
		String postData;

		postData = "code=" + AuthorizationCode + "&client_id=" + Outh2Credentials.getClientID() + "&client_secret="
				+ Outh2Credentials.getClientSecret() + "&redirect_uri="
				+ URLEncoder.encode(Outh2Credentials.getRedirectURI(), "UTF-8")
				+ "&grant_type=authorization_code&prompt=consent";
//		if (state) {
//
//			postData = "code=" + AuthorizationCode + "&client_id=" + Outh2Credentials.getClientID() + "&client_secret="
//					+ Outh2Credentials.getClientSecret() + "&redirect_uri="x
//					+ URLEncoder.encode(Outh2Credentials.getRedirectURI(), "UTF-8") + "&grant_type=authorization_code";
//
//		} else {
//
//			postData = "code=" + AuthorizationCode + "&client_id=" + Outh2Credentials.getClientID() + "&client_secret="
//					+ Outh2Credentials.getClientSecret() + "&redirect_uri="
//					+ URLEncoder.encode(Outh2Credentials.getRedirectURI(), "UTF-8")
//					+ "&grant_type=authorization_code&prompt=consent";
//
//		}
		try {

			System.out.println("the Authorization code is:" + AuthorizationCode);

			URL url = new URL(Outh2Credentials.getToken());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoOutput(true);

			try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
				outputStream.writeBytes(postData);
				outputStream.flush();
			}

			int responseCode = connection.getResponseCode();
			StringBuilder responseBody = new StringBuilder();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					responseBody.append(line);
				}
			}
			if (responseCode == HttpURLConnection.HTTP_OK) {

				String responseJson = responseBody.toString();
				System.out.println("Token Response: " + responseJson);
				JsonObject jsonAccess = JsonParser.parseString(responseJson).getAsJsonObject();
				Oauth oauth = new Oauth();
				oauth.setAccessToken(jsonAccess.get("access_token").getAsString());
				oauth.setUserID(userID);
				oauth.setSyncState(false);
				oauth.setCreatedAt(Instant.now().toEpochMilli());
				oauth.setModifiedAt(oauth.getCreatedAt());
				oauth.setOauthProvider(OauthProvider.GOOGLE.toString());
				if (jsonAccess.get("refresh_token") != null) {
					oauth.setRefreshToken(jsonAccess.get("refresh_token").getAsString());
				}
				oauth.setExpiryTime(oauth.getCreatedAt() + jsonAccess.get("expires_in").getAsLong() * 1000);
				String userURL = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + oauth.getAccessToken();
				JsonObject jsonUser = requestOauth(userURL, oauth.getAccessToken());
				oauth.setEmail(jsonUser.get("email").getAsString());

				return oauth;

			} else {
				System.out.println("Error: Unable to fetch contacts. HTTP Response Code: " + responseCode);
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				StringBuilder errorResponse = new StringBuilder();
				String errorLine;
				while ((errorLine = errorReader.readLine()) != null) {
					errorResponse.append(errorLine);
				}
				System.out.println("Error Response: " + errorResponse.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("Error: " + e.getMessage());
		}

		return null;

	}

	public static ArrayList<ContactDetails> getContacts(Oauth oauth) {

		String apiUrl = "https://people.googleapis.com/v1/people/me/connections?personFields=names,emailAddresses,phoneNumbers,addresses,metadata";

		JsonObject json = requestOauth(apiUrl, oauth.getAccessToken());

		JsonArray connections = json.getAsJsonArray("connections");

		ArrayList<ContactDetails> contacts = new ArrayList<ContactDetails>();
		if (connections != null) {

			for (int i = 0; i < connections.size(); i++) {
				ContactDetails contact = new ContactDetails();
				contact.setUserID(oauth.getUserID());
				contact.setCreatedAt(Instant.now().toEpochMilli());
				contact.setModifiedAt(contact.getCreatedAt());
				JsonObject contactJson = connections.get(i).getAsJsonObject();

				JsonArray names = contactJson.getAsJsonArray("names");
				System.out.println("name" + names);
				if (names.size() > 0) {

					if (names.get(0).getAsJsonObject().get("givenName") != null) {
						contact.setFirstName(names.get(0).getAsJsonObject().get("givenName").getAsString());

					}
					if (names.get(0).getAsJsonObject().get("middleName") != null) {
						contact.setMiddleName(names.get(0).getAsJsonObject().get("middleName").getAsString());

					}

					if (names.get(0).getAsJsonObject().get("familyName") != null) {
						contact.setLastName(names.get(0).getAsJsonObject().get("familyName").getAsString());

					}

				}

				if (contactJson.has("emailAddresses")) {

					JsonArray emails = contactJson.getAsJsonArray("emailAddresses");

					for (int j = 0; j < emails.size(); j++) {

						ContactMail contactMail = new ContactMail();
						contactMail.setContactMailID(emails.get(j).getAsJsonObject().get("value").getAsString());
						contactMail.setCreatedAt(contact.getCreatedAt());
						contactMail.setModifiedAt(contact.getModifiedAt());
						contact.setContactMail(contactMail);

					}

				}

				if (contactJson.has("phoneNumbers")) {

					JsonArray phone = contactJson.getAsJsonArray("phoneNumbers");

					for (int j = 0; j < phone.size(); j++) {
						ContactPhone contactPhone = new ContactPhone();
						contactPhone.setContactPhone(phone.get(j).getAsJsonObject().get("value").getAsString());
						contactPhone.setCreatedAt(contact.getCreatedAt());
						contactPhone.setModifiedAt(contact.getModifiedAt());
						contact.setContactPhone(contactPhone);
					}

				}
				if (contactJson.has("addresses")) {

					JsonArray addressesArray = contactJson.getAsJsonArray("addresses");
					System.out.println("here the address" + addressesArray);
					for (JsonElement addressElement : addressesArray) {
						JsonObject addressObject = addressElement.getAsJsonObject();
//						String formattedValue = addressObject.get("formattedValue").getAsString();
						String streetAddress = addressObject.get("streetAddress").getAsString();
						String city = addressObject.get("city").getAsString();
						String region = addressObject.get("region").getAsString();
//						String postalCode = addressObject.get("postalCode").getAsString();
						String country = addressObject.get("country").getAsString();
						String countryCode = addressObject.get("countryCode").getAsString();
						String address = streetAddress + city + region + country + countryCode;

						address = address.trim();

						System.out.println("here the address is" + address);

						contact.setAddress(address);

					}

				}

				if (contactJson.has("resourceName")) {

					String oauthContactProvider = contactJson.get("resourceName").getAsString();

					if (oauthContactProvider != null) {

						contact.setOauthContactID(oauthContactProvider);
					}

				}
				contact.setOauthID(oauth.getID());

				contacts.add(contact);

			}
		}
		return contacts;
	}

	public static JsonObject getUserProfile(String accessToken) {

		String apiUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;

		JsonObject userJson = requestOauth(apiUrl, accessToken);
		return userJson;

	}

	private static JsonObject requestOauth(String apiUrl, String accessToken) {

		HttpURLConnection connection = null;
		BufferedReader in = null;
		try {
			URL url = new URL(apiUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			connection.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {

				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder responseBuffer = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					responseBuffer.append(inputLine);
				}

				System.out.println("API Response: " + responseBuffer.toString());

				JsonObject json = JsonParser.parseString(responseBuffer.toString()).getAsJsonObject();

				return json;
			} else {
				System.out.println("Error: Unable to fetch contacts. HTTP Response Code: " + responseCode);
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				StringBuilder errorResponse = new StringBuilder();
				String errorLine;
				while ((errorLine = errorReader.readLine()) != null) {
					errorResponse.append(errorLine);
				}
				System.out.println("Error Response: " + errorResponse.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (in != null) {
					in.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;

	}

	public static Oauth refreshAccessToken(Oauth oauth) {

		String postData = "refresh_token=" + oauth.getRefreshToken() + "&client_id=" + Outh2Credentials.getClientID()
				+ "&client_secret=" + Outh2Credentials.getClientSecret() + "&grant_type=refresh_token";

		try {

			System.out.println("the refresh Token is:" + oauth.getRefreshToken());

			URL url = new URL(Outh2Credentials.getToken());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoOutput(true);

			try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
				outputStream.writeBytes(postData);
				outputStream.flush();
			}

			int responseCode = connection.getResponseCode();
			StringBuilder responseBody = new StringBuilder();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					responseBody.append(line);
				}
			}
			if (responseCode == HttpURLConnection.HTTP_OK) {

				String responseJson = responseBody.toString();
				System.out.println("Token Response: " + responseJson);
				JsonObject jsonAccess = JsonParser.parseString(responseJson).getAsJsonObject();
				Oauth oauthData = new Oauth();
				oauthData.setID(oauth.getID());
				oauthData.setAccessToken(jsonAccess.get("access_token").getAsString());
				oauthData.setModifiedAt(Instant.now().toEpochMilli());
				oauthData.setExpiryTime(oauthData.getModifiedAt() + jsonAccess.get("expires_in").getAsLong() * 1000);

				return oauthData;

			} else {
				System.out.println("Error: Unable to fetch contacts. HTTP Response Code: " + responseCode);
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				StringBuilder errorResponse = new StringBuilder();
				String errorLine;
				while ((errorLine = errorReader.readLine()) != null) {
					errorResponse.append(errorLine);
				}
				System.out.println("Error Response: " + errorResponse.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;

	}

//	private static JsonObject requestOauthWithJson(String apiUrl, String accessToken, JsonObject jsonBody) throws Exception {
//URL url = new URL(apiUrl);
//	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//connection.setRequestMethod("PUT");
//	
//	    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
//	    connection.setRequestProperty("Content-Type", "application/json");
//
//
//	    connection.setDoOutput(true);
//
//	  
//	    try (OutputStream os = connection.getOutputStream()) {
//	        byte[] input = jsonBody.toString().getBytes("UTF_8");
//	        os.write(input, 0, input.length); 	    }
// int responseCode = connection.getResponseCode();
//
//	
//	    InputStream inputStream;
//	    if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
//	        inputStream = connection.getInputStream();
//	    } else {
//	        inputStream = connection.getErrorStream();
//	    }
//
//
//	    StringBuilder response = new StringBuilder();
//	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//	        String line;
//	        while ((line = reader.readLine()) != null) {
//	            response.append(line);
//	        }
//	    }
//
//	    JsonObject jsonResponse = new JsonObject();
//	    if (responseCode == HttpURLConnection.HTTP_OK) {
//	        jsonResponse = new JsonObject(response.toString());
//	    } else {
//	        throw new Exception("Request failed with status code: " + responseCode + ", response: " + response.toString());
//	    }
//
//	    return jsonResponse;
//	}

	public static void deleteOauthContact(String resourceName, String accessToken) throws IOException {
		String urlString = "https://people.googleapis.com/v1/" + resourceName + ":deleteContact";
		URL url = new URL(urlString);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");
		connection.setRequestProperty("Authorization", "Bearer " + accessToken);
		int responseCode = connection.getResponseCode();
		System.out.println("DELETE Response Code: " + responseCode);
		try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			System.out.println("DELETE Response: " + response.toString());
		}
	}

}
