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
 * Servlet implementation class DeleteUserContactServlet
 */
public class DeleteUserContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteUserContactServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("This is called in delete in GET method: " + request.getParameter("user_id"));
	}

	/**
	 * Handles POST requests to delete a user contact.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			if (request.getParameter("contact_id") != null && !request.getParameter("contact_id").isBlank()) {

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				int contactID = Integer.parseInt(request.getParameter("contact_id"));

				ContactDetails contact = new ContactDetails();
				contact.setID(contactID);
				if (UserContactOperation.deleteContact(contact, userData.getID())) {

					LoggerSet.logInfo("DeleteUserContactServlet", "doPost",
							"Contact deleted successfully: " + contactID);
					response.sendRedirect("home.jsp");
				} else {
					LoggerSet.logWarning("DeleteUserContactServlet", "doPost",
							"Unable to delete contact: " + contactID);
					request.setAttribute("errorMessage", "Unable to delete contact");
					request.getRequestDispatcher("home.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("DeleteUserContactServlet", "doPost", "Contact ID is null or empty");
				request.setAttribute("errorMessage", "Unable to delete contact because specified contact ID is null");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("DeleteUserContactServlet", "doPost", "Exception occurred while deleting contact", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
	}
}
