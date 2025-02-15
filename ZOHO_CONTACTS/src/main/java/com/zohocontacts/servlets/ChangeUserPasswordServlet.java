package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserOperation;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class ChangeUserPasswordServlet
 */

public class ChangeUserPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangeUserPasswordServlet() {
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
		
			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();
			if (((request.getParameter("password") != null && !request.getParameter("password").isBlank())
					&& (request.getParameter("Newpassword") != null
							&& !request.getParameter("Newpassword").isBlank()))) {

				UserData oldUser = userData;

				String oldPassword = request.getParameter("password");
				String newPassword = request.getParameter("Newpassword");

				if (UserOperation.userPasswordChange(oldUser, oldPassword, newPassword, userData.getID())) {

					response.sendRedirect("changePassword.jsp");
					LoggerSet.logInfo(" ChangeUserPasswordServlet", "doPost", "User Password Update successfully.");
				} else {
					LoggerSet.logWarning(" ChangeUserPasswordServlet", "doPost",
							"Failed to change Password for User ID: " + userData.getID());
					request.setAttribute("errorMessage",
							"Error while trying to update the User Password. Please try again.");
					request.getRequestDispatcher("changePassword.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning(" ChangeUserPasswordServlet", "doPost", "Input fields are empty.");
				request.setAttribute("errorMessage", "Input fields should not be empty!");
				request.getRequestDispatcher("changePassword.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError(" ChangeUserPasswordServlet", "doPost", "Exception occurred during Password Update", e);
			request.setAttribute("errorMessage", "An error occurred while processing your request.");
			request.getRequestDispatcher("changePassword.jsp").forward(request, response);
		}
	}

}
