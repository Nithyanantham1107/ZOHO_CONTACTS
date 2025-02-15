package com.zohocontacts.servlets;

import java.io.IOException;
import java.time.Instant;
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
 * Servlet implementation class AddUserEmailServlet
 */

public class AddUserEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;



	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddUserEmailServlet() {
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
		// TODO Auto-geneAddUserEmailServletrated method stub
		try {
			if ((request.getParameter("newemail") != null && !request.getParameter("newemail").isBlank())) {

			
				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				EmailUser email = new EmailUser();
				email.setEmailID(userData.getID());
				email.setEmail(request.getParameter("newemail"));
				email.setIsPrimary(false);
				email.setCreatedAt(Instant.now().toEpochMilli());
				email.setModifiedAt(email.getCreatedAt());
				email = UserOperation.addEmail(email, userData.getID());
				if (email != null) {
					EmailUser emailDB = new EmailUser();
					emailDB.setEmailID(userData.getID());
					List<EmailUser> emails = UserOperation.getAllEmail(emailDB);
					if (emails != null) {

						userData.setAllEmails(emails);
					}

					LoggerSet.logInfo("AddUserEmailServlet", "doPost",
							"Contact added successfully for user ID: " + userData.getID());
					response.sendRedirect("changePassword.jsp");
				} else {
					LoggerSet.logWarning("AddUserEmailServlet", "doPost",
							"Error in adding contact for user ID: " + userData.getID());
					request.setAttribute("errorMessage", "Error in adding contact");
					request.getRequestDispatcher("changePassword.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("AddUserEmailServlet", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("changePassword.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("AddUserEmailServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("changePassword.jsp").forward(request, response);
		}
	}

}
