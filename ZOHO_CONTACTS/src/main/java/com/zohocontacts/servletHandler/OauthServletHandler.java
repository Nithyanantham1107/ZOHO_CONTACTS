package com.zohocontacts.servletHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.OauthOperation;
import com.zohocontacts.dboperation.UserContactOperation;
import com.zohocontacts.dboperation.UserOperation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.Oauth;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.oauth2helper.Oauth2handler;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

public class OauthServletHandler {

	public static void oauthDeleteSyncStateRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			if ((request.getParameter("ID") != null && !request.getParameter("ID").isBlank())) {

				int oauthID = Integer.parseInt(request.getParameter("ID"));

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();

				OauthOperation.deleteOauth(oauthID, cacheModel.getUserData());
				System.out.println("here the use data Oauth Data is" + cacheModel.getUserData().getallOauth());

				System.out.println(
						"here the use data Oauth Data size is" + cacheModel.getUserData().getallOauth().size());

				LoggerSet.logWarning("OauthSyncStateServlet", "doPost",
						"successfully Oauth data modified for userID" + cacheModel.getUserData().getID());

				response.sendRedirect("linkedaccounts.jsp");

			} else {
				LoggerSet.logWarning("oauthDeleteSyncStateRequestHandler", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("oauthDeleteSyncStateRequestHandler", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
		}

	}

	public static void oauthSyncStateOnRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			if ((request.getParameter("ID") != null && !request.getParameter("ID").isBlank())) {

				int oauthID = Integer.parseInt(request.getParameter("ID"));

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();

				OauthOperation.setOauthSynncOn(oauthID, cacheModel.getUserData());

				LoggerSet.logWarning("OauthSyncStateServlet", "doPost",
						"successfully Oauth data modified for userID" + cacheModel.getUserData().getID());

				response.sendRedirect("linkedaccounts.jsp");

			} else {
				LoggerSet.logWarning("oauthSyncStateOnRequestHandler", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("oauthSyncStateOnRequestHandler", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
		}

	}

	public static void oauthSyncStateRequestOffHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			if ((request.getParameter("ID") != null && !request.getParameter("ID").isBlank())) {

				int oauthID = Integer.parseInt(request.getParameter("ID"));

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();

				OauthOperation.setOauthSynncOff(oauthID, cacheModel.getUserData());

				LoggerSet.logWarning("OauthSyncStateServlet", "doPost",
						"successfully Oauth data modified for userID" + cacheModel.getUserData().getID());

				response.sendRedirect("linkedaccounts.jsp");

			} else {
				LoggerSet.logWarning("oauthSyncStateRequestOffHandler", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("oauthSyncStateRequestOffHandler", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
		}

	}

	public static void googleOauthRedirectRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();
			String code = request.getParameter("code");
			System.out.println("hey hi hello man !!!");
			if (code == null || code.isEmpty()) {
				LoggerSet.logError("GoogleOauthRedirectServlet", "doGet", "Authorization code is Empty.", null);
				request.setAttribute("errorMessage", "Authorization code is empty");
				request.getRequestDispatcher("home.jsp").forward(request, response);

			} else {
				Oauth oauth = Oauth2handler.getAccessToken(code, response, userData.getID(), true);

				if (oauth != null) {

					Oauth OauthDB = OauthOperation.isOauthExist(oauth.getEmail(), oauth.getOauthProvider(),
							oauth.getUserID());
					if (OauthDB == null) {

						OauthOperation.addOauth(oauth, userData.getID());
						cacheModel.setUserData(UserOperation.getUserData(userData.getID()));

						oauthContactSync(oauth, userData.getID());
					}

				}

				response.sendRedirect("linkedaccounts.jsp");
			}

		} catch (DBOperationException e) {
			LoggerSet.logError("GoogleOauthRedirectServlet", "doGet", "Exception occurred while processing Oauth .", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
		}

	}

	public static void OauthContactSyncRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();
			if (request.getParameter("ID") != null && !request.getParameter("ID").isBlank()) {
				long oauthID = Long.parseLong(request.getParameter("ID"));

				Oauth oauth = new Oauth();
				oauth.setID(oauthID);
				oauth = OauthOperation.getOauth(oauth);
				if (oauth != null) {
					oauthContactSync(oauth, userData.getID());
					response.sendRedirect("linkedaccounts.jsp");
				} else {
					LoggerSet.logWarning("OauthContactSyncRequestHandler", "doPost", "Failed to sync contactts!");
					request.setAttribute("errorMessage", "Failed to sync Oauth Contact!");
					request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);

				}

			} else {

				LoggerSet.logWarning("OauthContactSyncRequestHandler", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);

			}

		} catch (DBOperationException e) {
			LoggerSet.logError("GoogleOauthRedirectServlet", "doGet", "Exception occurred while processing Oauth .", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
		}

	}

	private static void oauthContactSync(Oauth oauth, long userID) throws DBOperationException {

		List<ContactDetails> contacts = Oauth2handler.getContacts(oauth);
		for (ContactDetails contact : contacts) {
			ContactDetails contactDB = UserContactOperation.viewOauthSpecificUserContact(oauth.getUserID(),
					contact.getOauthContactID());
			if (contactDB == null) {
				contact.setCreatedAt(contact.getModifiedAt());
				UserContactOperation.addUserContact(contact);
			} else {
				contact.setID(contactDB.getID());
				UserContactOperation.updateSpecificUserContact(contact, oauth.getUserID());
			}
		}

		Oauth oauthDb = new Oauth();
		oauthDb.setID(oauth.getID());
		oauthDb.setModifiedAt(Instant.now().toEpochMilli());
		oauthDb.setOauthSyncTime(oauthDb.getModifiedAt());

		OauthOperation.updateOauth(oauthDb, userID);
	}

}
