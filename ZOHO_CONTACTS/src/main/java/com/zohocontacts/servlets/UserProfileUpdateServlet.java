package com.zohocontacts.servlets;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserOperation;
import com.zohocontacts.dbpojo.EmailUser;
import com.zohocontacts.dbpojo.LoginCredentials;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class UserProfileUpdateServlet
 */

public class UserProfileUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserProfileUpdateServlet() {
		super();

		// TODO Auto-generated constructor stub
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
			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();

			Boolean isSuccess = false;

			if ((request.getParameter("name") != null && !request.getParameter("name").isBlank())
					&& (request.getParameter("username") != null && !request.getParameter("username").isBlank())
					&& (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
					&& (request.getParameter("address") != null && !request.getParameter("address").isBlank())
					&& (request.getParameter("primaryemail") != null) && !request.getParameter("primaryemail").isBlank()
					&& (request.getParameter("timezone") != null) && !request.getParameter("timezone").isBlank()
					&& (request.getParameter("emailID") != null) && !request.getParameter("emailID").isBlank()
					&& (request.getParameter("logID") != null) && !request.getParameter("logID").isBlank()) {
				long userId = userData.getID();

				UserData user = new UserData();
				LoginCredentials loginCredentials = new LoginCredentials();
				EmailUser emailUser = new EmailUser();
				emailUser.setID(Integer.parseInt(request.getParameter("emailID")));

				emailUser.setEmail(request.getParameter("primaryemail"));
				emailUser.setModifiedAt(user.getModifiedAt());
				emailUser.setIsPrimary(true);

				loginCredentials.setID(Integer.parseInt(request.getParameter("logID")));
				loginCredentials.setUserID(userData.getID());
				loginCredentials.setUserName(request.getParameter("username"));
				loginCredentials.setModifiedAt(user.getModifiedAt());
				user.setLoginCredentials(loginCredentials);
				user.setModifiedAt(Instant.now().toEpochMilli());
				user.setID(userData.getID());
				user.setName(request.getParameter("name"));
				user.setAddress(request.getParameter("address"));
				user.setPhoneno(request.getParameter("phone"));
				user.setTimezone(request.getParameter("timezone"));

				isSuccess = UserOperation.userprofileUpdate(user, emailUser);

				if (isSuccess) {
					cacheModel.setUserData(UserOperation.getUserData(userId));

					response.sendRedirect("profile.jsp");
					LoggerSet.logInfo("UserProfileServlet", "doPost",
							"User profile updated successfully for User ID: " + user.getID());
				} else {
					LoggerSet.logWarning("UserProfileServlet", "doPost",
							"Error in updating profile data for User ID: " + user.getID());
					request.setAttribute("errorMessage", "Error in updating profile Data");
					request.getRequestDispatcher("profile.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("UserProfileServlet", "doPost", "Parameter cannot be empty.");
				request.setAttribute("errorMessage", "Parameter cannot be empty.");
				request.getRequestDispatcher("profile.jsp").forward(request, response);
			}

		} catch (

		DBOperationException e) {
			LoggerSet.logError("UserProfileServlet", "doPost", "Exception occurred while processing user profile.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("profile.jsp").forward(request, response);
		}
	}

}
