package com.zohocontacts.servlets;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.SessionOperation;
import com.zohocontacts.dboperation.UserOperation;
import com.zohocontacts.dbpojo.EmailUser;
import com.zohocontacts.dbpojo.LoginCredentials;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class UserProfileServlet
 */
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserProfileServlet() {
		super();

	}

	/**
	 * Handles GET requests for user profile.
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
	 * Handles POST requests for updating user profile information or deleting a
	 * user account.
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
			Boolean isSuccess = false;

			String sessionID = (String) request.getAttribute("sessionid");

			if (request.getParameter("method").equals("update")) {
				if ((request.getParameter("Name") != null && !request.getParameter("Name").isBlank())
						&& (request.getParameter("username") != null && !request.getParameter("username").isBlank())
						&& (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
						&& (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
						&& (request.getParameterValues("email") != null) && !request.getParameter("email").isBlank()
						&& (request.getParameterValues("timezone") != null)
						&& !request.getParameter("timezone").isBlank()) {

					UserData user = new UserData();
					LoginCredentials loginCredentials = new LoginCredentials();

					user.setModifiedAt(Instant.now().toEpochMilli());
					loginCredentials.setModifiedAt(user.getModifiedAt());

					for (String email : request.getParameterValues("email")) {
						EmailUser emailUser = new EmailUser();
						emailUser.setEmail(email);
						emailUser.setCreatedAt(user.getModifiedAt());
						emailUser.setModifiedAt(user.getModifiedAt());

						if (email.equals(request.getParameter("primaryemail"))) {

							emailUser.setIsPrimary(true);
						} else {
							emailUser.setIsPrimary(false);
						}
						user.setEmail(emailUser);
					}

					loginCredentials.setUserName(request.getParameter("username"));

					user.setLoginCredentials(loginCredentials);

					user.setID(userData.getID());
					user.setName(request.getParameter("Name"));
					user.setAddress(request.getParameter("Address"));
					user.setPhoneno(request.getParameter("phone"));
					user.setTimezone(request.getParameter("timezone"));

					if (request.getParameter("password") == null || request.getParameter("password").isBlank()
							|| request.getParameter("Newpassword") == null
							|| request.getParameter("Newpassword").isBlank()) {

						isSuccess = UserOperation.userDataUpdate(user, null);

					} else {
						user.setPassword(request.getParameter("password"));

						isSuccess = UserOperation.userDataUpdate(user, request.getParameter("Newpassword"));

					}
					if (isSuccess) {
						cacheModel.setUserData(user);

						response.sendRedirect("profile.jsp");
						LoggerSet.logInfo("UserProfileServlet", "doPost",
								"User profile updated successfully for User ID: " + user.getID());
					} else {
						LoggerSet.logWarning("UserProfileServlet", "doPost",
								"Error in updating profile data for User ID: " + user.getID());
						request.setAttribute("errorMessage", "Error in updating profile Data");
						request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
					}
				} else {
					LoggerSet.logWarning("UserProfileServlet", "doPost", "Parameter cannot be empty.");
					request.setAttribute("errorMessage", "Parameter cannot be empty.");
					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
				}
			} else {
				if (UserOperation.deleteUserProfile(userData)) {
					LoggerSet.logInfo("UserProfileServlet", "doPost",
							"Successfully deleted user profile for User ID: " + userData.getID());

					SessionOperation.DeleteSessionData(sessionID);

					response.sendRedirect("index.jsp");
				} else {
					LoggerSet.logWarning("UserProfileServlet", "doPost",
							"Error in deleting user profile for User ID: " + userData.getID());
					request.setAttribute("errorMessage", "Unable to delete user profile data");
					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
				}
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("UserProfileServlet", "doPost", "Exception occurred while processing user profile.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
		}
	}
}
