package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.OauthOperation;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class OauthSyncStateServlet
 */

public class OauthSyncStateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OauthSyncStateServlet() {
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
			if ((request.getParameter("operation") != null && !request.getParameter("operation").isBlank()) &&

					(request.getParameter("ID") != null && !request.getParameter("ID").isBlank())) {

				String operation = request.getParameter("operation");
				int oauthID = Integer.parseInt(request.getParameter("ID"));

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				if (operation.equals("syncoff")) {
					OauthOperation.setOauthSynncOff(oauthID, userData);

				} else if (operation.equals("syncon")) {
					OauthOperation.setOauthSynncOn(oauthID, userData);
				} else if (operation.equals("deletesync")) {
					OauthOperation.deleteOauth(oauthID, userData);
				}

				LoggerSet.logWarning("OauthSyncStateServlet", "doPost",
						"successfully Oauth data modified for userID" + userData.getID());

				response.sendRedirect("linkedaccounts.jsp");

			} else {
				LoggerSet.logWarning("OauthSyncStateServlet", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("OauthSyncStateServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
		}
	}

}
