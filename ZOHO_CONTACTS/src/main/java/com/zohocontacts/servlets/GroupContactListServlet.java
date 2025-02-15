package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserGroupOperation;
import com.zohocontacts.dbpojo.Category;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class GroupContactList
 */

public class GroupContactListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GroupContactListServlet() {
		super();

		logger = new LoggerSet();
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
		// TODO Auto-generated method stub

		try {

			System.out.println("hello im groupList contact");
			System.out.println("postpostpostpostpostpostpostpostpost" + request.getParameter("groupid"));

			if (request.getParameter("groupid") != null && !request.getParameter("groupid").isBlank()
					&& request.getParameter("method") != null && !request.getParameter("method").isBlank()) {
				String method = request.getParameter("method");
				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				int groupID = Integer.parseInt(request.getParameter("groupid"));

		
				Category group = UserGroupOperation.getSpecificGroup(groupID, userData.getID());

				request.setAttribute("group", group);

				logger.logInfo("UserSpecificContactRetrievalServlet", "doPost",
						"Retrieved specific contact info for Contact ID: " + groupID);

				if (method.equals("view")) {

					request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);

				} else {

					request.getRequestDispatcher("groupcontactadd.jsp").forward(request, response);
				}

			} else {
				logger.logWarning("UserSpecificContactRetrievalServlet", "doPost", "Contact ID parameter is missing.");
				request.setAttribute("errorMessage", "Contact ID parameter is missing.");
				request.getRequestDispatcher("groups.jsp").forward(request, response);
			}
		} catch (

		DBOperationException e) {
			logger.logError("UserSpecificContactRetrievalServlet", "doPost",
					"Exception occurred while retrieving specific contact.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("groups.jsp").forward(request, response);
		}
	}

}
