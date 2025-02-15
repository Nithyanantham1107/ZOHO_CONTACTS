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
 * Servlet implementation class UserSpecificContactRetrievalServlet
 */
public class UserSpecificContactRetrievalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserSpecificContactRetrievalServlet() {
		super();

}

	/**
	 * Handles GET requests for the servlet.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * Handles POST requests for retrieving a specific user's contact information.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();
            
			if (request.getParameter("contact_id") != null) {
				long user_id = userData.getID();
				long contact_id = Long.parseLong(request.getParameter("contact_id"));
				ContactDetails uc = UserContactOperation.viewSpecificUserContact(user_id, contact_id);
				if (uc != null) {

					request.setAttribute("user_spec_contact", uc);
					request.getRequestDispatcher("Update_contact.jsp").forward(request, response);
					LoggerSet.logInfo("UserSpecificContactRetrievalServlet", "doPost",
							"Retrieved specific contact info for Contact ID: " + contact_id);
				} else {
					LoggerSet.logWarning("UserSpecificContactRetrievalServlet", "doPost",
							"Failed to retrieve info for Contact ID: " + contact_id);
					request.setAttribute("errorMessage", "Can't retrieve info of specific user");
					request.getRequestDispatcher("home.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("UserSpecificContactRetrievalServlet", "doPost", "Contact ID parameter is missing.");
				request.setAttribute("errorMessage", "Contact ID parameter is missing.");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("UserSpecificContactRetrievalServlet", "doPost",
					"Exception occurred while retrieving specific contact.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
	}
}
