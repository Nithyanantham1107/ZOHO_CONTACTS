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
import com.zohocontacts.servletHandler.GroupServletHandler;
import com.zohocontacts.servletHandler.OauthServletHandler;
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

		if (request.getParameter("action") != null && !request.getParameter("action").isBlank()) {

			String action = request.getParameter("action");
			switch (action) {

			case "deletesync":
				LoggerSet.logError(" OauthSyncStateServlet", "doPost", "delete Sync State Hitted", null);

				OauthServletHandler.oauthDeleteSyncStateRequestHandler(request, response);
				break;

			case "syncon":
				LoggerSet.logError("OauthSyncStateServlet", "doPost", "Sync state on Hitted", null);

				OauthServletHandler.oauthSyncStateOnRequestHandler(request, response);
				break;

			case "syncoff":

				LoggerSet.logError("OauthSyncStateServlet", "doPost", "Sync state off Hitted", null);

				OauthServletHandler.oauthSyncStateRequestOffHandler(request, response);
				break;
			case "syncnow":

				LoggerSet.logError("OauthSyncStateServlet", "doPost", "Sync now contact is Hitted", null);

				OauthServletHandler.OauthContactSyncRequestHandler(request, response);
				break;

			default:

				LoggerSet.logError("GroupServlet", "doPost", "Action string is undefined", null);
				request.setAttribute("errorMessage", "the action is undefined");
				request.getRequestDispatcher("home.jsp").forward(request, response);
				System.out.println("Unknown action");

			}
		} else {

			LoggerSet.logError("OauthSyncStateServlet", "doPost", "Action string is empty", null);
			request.setAttribute("errorMessage", "the action parameter not provided");
			request.getRequestDispatcher("home.jsp").forward(request, response);

		}

	}

}
