package com.zohocontacts.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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

/**
 * Servlet implementation class GoogleOauthRedirectServlet
 */

public class GoogleOauthRedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoogleOauthRedirectServlet() {
		super();

	

		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

						ArrayList<ContactDetails> contacts = Oauth2handler.getContacts(oauth);
						for (ContactDetails contact : contacts) {
							ContactDetails contactDB = UserContactOperation
									.viewOauthSpecificUserContact(oauth.getUserID(), contact.getOauthContactID());
							if (contactDB == null) {
								UserContactOperation.addUserContact(contact);
							} else {
								contact.setID(contactDB.getID());
								UserContactOperation.updateSpecificUserContact(contact, oauth.getUserID());
							}
						}
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
