package com.zohocontacts.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.ServerRegistryOperation;
import com.zohocontacts.dbpojo.ServerRegistry;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheData;

/**
 * Servlet implementation class ServerCacheDeleteServlet
 */

public class ServerCacheDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServerCacheDeleteServlet() {
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

		try {

			List<ServerRegistry> servers=ServerRegistryOperation.getServerRegistryExcept(CacheData.getServerInfo());
			CacheData.setServers(servers);
		} catch (DBOperationException e) {
			LoggerSet.logError("UserProfileServlet", "doPost", "Exception occurred while processing user profile.", e);
				}

	}

}
