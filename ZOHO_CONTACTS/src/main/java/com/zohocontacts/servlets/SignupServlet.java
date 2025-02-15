package com.zohocontacts.servlets;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.validation.UserValidation;

/**
 * Servlet implementation class SignupServlet
 */
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignupServlet() {
		super();

	
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * Handles POST requests for user sign-up.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ((request.getParameter("password") != null && !request.getParameter("password").isBlank())
					&& (request.getParameter("Name") != null && !request.getParameter("Name").isBlank())
					&& (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
					&& (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
					&& (request.getParameter("email") != null && !request.getParameter("email").isBlank())) {

				if (UserValidation.validateUserPassword(request.getParameter("password"))) {
					UserData userData = new UserData();
					LoginCredentials loginCredentials = new LoginCredentials();
					EmailUser emailUser = new EmailUser();
					userData.setCreatedAt(Instant.now().toEpochMilli());
					userData.setModifiedAt(userData.getCreatedAt());
					loginCredentials.setUserName(request.getParameter("username"));
					loginCredentials.setCreatedAt(userData.getCreatedAt());
					loginCredentials.setModifiedAt(userData.getModifiedAt());
					emailUser.setEmail(request.getParameter("email"));
					emailUser.setIsPrimary(true);
					emailUser.setCreatedAt(userData.getCreatedAt());
					emailUser.setModifiedAt(userData.getModifiedAt());
					userData.setName(request.getParameter("Name"));
					userData.setAddress(request.getParameter("Address"));

					userData.setPhoneno(request.getParameter("phone"));

					userData.setPassword(request.getParameter("password"));

					userData.setTimezone(request.getParameter("timezone"));
					userData.setEmail(emailUser);
					userData.setLoginCredentials(loginCredentials);

					userData = UserOperation.createUser(userData);

					if (userData != null) {

						String sessionID = SessionOperation.generateSessionId(userData.getID());
						Cookie sessionCookie = new Cookie("SESSIONID", sessionID);
						sessionCookie.setHttpOnly(true);
						response.addCookie(sessionCookie);
						CacheModel cachemodel = CacheData.getCache(sessionID);
				
						cachemodel.setUserData(userData);

						response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
						response.setHeader("Pragma", "no-cache");
						response.setDateHeader("Expires", 0);
						response.sendRedirect("home.jsp");

						LoggerSet.logInfo("SignupServlet", "doPost",
								"User signed up successfully: " + loginCredentials.getUserName());
					} else {
						LoggerSet.logWarning("SignupServlet", "doPost", "User creation failed.");
						request.setAttribute("errorMessage", "An error occurred while creating user");
						request.getRequestDispatcher("Signup.jsp").forward(request, response);
					}
				} else {
					LoggerSet.logWarning("SignupServlet", "doPost",
							"Password validation failed for user: " + request.getParameter("username"));
					request.setAttribute("errorMessage",
							"Password should contain at least one lower case, one upper case, and numbers.");
					request.getRequestDispatcher("Signup.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("SignupServlet", "doPost", "Required parameters are missing.");
				request.setAttribute("errorMessage", "Parameters should not be empty!");
				request.getRequestDispatcher("Signup.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("SignupServlet", "doPost", "Exception occurred during signup", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Signup.jsp").forward(request, response);
		}
	}
}
