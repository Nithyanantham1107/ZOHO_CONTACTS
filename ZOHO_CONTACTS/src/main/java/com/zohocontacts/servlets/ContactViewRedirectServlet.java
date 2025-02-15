package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserContactOperation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class ContactViewRedirectServlet
 */

public class ContactViewRedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ContactViewRedirectServlet() {
		super();
	
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

		
			System.out.println("hello im update contact");
			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();
			if (request.getParameter("contact_id") != null) {
				long userID = userData.getID();
				long contactID = Long.parseLong(request.getParameter("contact_id"));
				ContactDetails contact = UserContactOperation.viewSpecificUserContact(userID, contactID);
				if (contact != null) {

					request.setAttribute("user_spec_contact", contact);
					request.getRequestDispatcher("contactview.jsp").forward(request, response);
					LoggerSet.logInfo("ContactViewRedirectServlet", "doPost",
							"Retrieved specific contact info for Contact ID: " + contactID);
				} else {
					LoggerSet.logWarning("ContactViewRedirectServlet", "doPost",
							"Failed to retrieve info for Contact ID: " + contactID);
					request.setAttribute("errorMessage", "Can't retrieve info of specific user");
					request.getRequestDispatcher("home.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("ContactViewRedirectServlet", "doPost", "Contact ID parameter is missing.");
				request.setAttribute("errorMessage", "Contact ID parameter is missing.");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("ContactViewRedirectServlet", "doPost",
					"Exception occurred while retrieving specific contact.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}

	}

}
