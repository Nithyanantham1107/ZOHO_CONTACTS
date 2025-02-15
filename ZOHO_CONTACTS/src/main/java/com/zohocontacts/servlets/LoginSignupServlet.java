package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.SessionOperation;
import com.zohocontacts.dboperation.UserOperation;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.validation.UserValidation;

/**
 * Servlet implementation class LoginSignupServlet
 */
public class LoginSignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;



	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginSignupServlet() {
		super();


	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * Handles POST requests for user login.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ((request.getParameter("username") != null && !request.getParameter("username").isBlank())
					&& (request.getParameter("password") != null && !request.getParameter("password").isBlank())) {

				if (UserValidation.validateUserPassword(request.getParameter("password"))) {

				UserData	userData = new UserData();
					userData = UserOperation.isUser(request.getParameter("username"), request.getParameter("password"));
					if (userData != null) {
						String sessionid = SessionOperation.generateSessionId(userData.getID());
						Cookie sessionCookie = new Cookie("SESSIONID", sessionid);
						sessionCookie.setHttpOnly(true);
						response.addCookie(sessionCookie);
//						CacheModel cachemodel = CacheData.getCache(sessionid);
//						cachemodel.setUserData(userData);

						LoggerSet.logInfo("LoginSignupServlet", "doPost",
								"User logged in successfully: " + userData.getName());
						response.sendRedirect("home.jsp");
					} else {
						LoggerSet.logWarning("LoginSignupServlet", "doPost",
								"Invalid username or password for user: " + request.getParameter("username"));
						request.setAttribute("errorMessage", "Invalid username and password");
						request.getRequestDispatcher("Login.jsp").forward(request, response);
					}
				} else {
					LoggerSet.logWarning("LoginSignupServlet", "doPost",
							"Password validation failed for user: " + request.getParameter("username"));
					request.setAttribute("errorMessage", "Password is too long or missing cases and numbers");
					request.getRequestDispatcher("Login.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("LoginSignupServlet", "doPost", "Username or password is empty");
				request.setAttribute("errorMessage", "Username and password should not be empty");
				request.getRequestDispatcher("Login.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("LoginSignupServlet", "doPost", "Exception occurred during login", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		}
	}
}
