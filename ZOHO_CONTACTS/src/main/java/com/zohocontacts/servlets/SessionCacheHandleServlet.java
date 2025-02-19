package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheData;

/**
 * Servlet implementation class SessionCacheHandleServlet
 */

public class SessionCacheHandleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SessionCacheHandleServlet() {
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
			if (request.getParameter("sessionID") != null && !request.getParameter("sessionID").isBlank()) {

				String sessionID = request.getParameter("sessionID");
				System.out.println("........here the cache data is deleted  for sesssionID" + sessionID);
				CacheData.deleteAllCache(sessionID);

			} else {
				System.out.println("here the recieved sessionID is empty");
				LoggerSet.logWarning("SessionCacheHandleServlet", "doPost",
						"Exception occurred while processing session ID.");

			}

		} catch (Exception e) {
			LoggerSet.logError("SessionCacheHandleServlet", "doPost",
					"Exception occurred while processing user profile.", e);
		}

	}

}
