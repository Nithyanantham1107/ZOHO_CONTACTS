package com.zohocontacts.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserOperation;
import com.zohocontacts.dbpojo.EmailUser;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class DeleteUserEmailServlet
 */

public class DeleteUserEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;



	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteUserEmailServlet() {
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

			if (request.getParameter("emailID") != null && !request.getParameter("emailID").isBlank()) {
			

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				int emailID = Integer.parseInt(request.getParameter("emailID"));

				EmailUser email = new EmailUser();
				email.setID(emailID);
				email.setEmailID(userData.getID());

				if (UserOperation.deleteUserEmail(email, userData.getID())) {
					EmailUser emailDB = new EmailUser();
					emailDB.setEmailID(userData.getID());
					List<EmailUser> emails = UserOperation.getAllEmail(emailDB);
					if (emails != null) {

						userData.setAllEmails(emails);
					}

					LoggerSet.logInfo("DeleteUserEmailServlet", "doPost", "email deleted successfully: " + emailID);
					response.sendRedirect("changePassword.jsp");
				} else {
					LoggerSet.logWarning("DeleteUserEmailServlet", "doPost", "Unable to delete email: " + emailID);
					request.setAttribute("errorMessage", "Unable to delete email");
					request.getRequestDispatcher("changePassword.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("DeleteUserEmailServlet", "doPost", "Email ID is null or empty");
				request.setAttribute("errorMessage", "Unable to delete Email because specified Email ID is null");
				request.getRequestDispatcher("changePassword.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("DeleteUserEmailServlet", "doPost", "Exception occurred while deleting Email", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("changePassword.jsp").forward(request, response);
		}
	}

}
