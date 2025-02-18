package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.servletHandler.GroupServletHandler;
import com.zohocontacts.servletHandler.UserServletHandler;

/**
 * Servlet implementation class GroupServlet
 */

public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GroupServlet() {
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

			case "deletegroup":
				LoggerSet.logError(" GroupServlet", "doPost", "delete Group Hitted", null);

				GroupServletHandler.deleteGroupRequestHandler(request, response);
				break;

			case "groupview":
				LoggerSet.logError("GroupServlet", "doPost", "Hgroup View Hitted", null);

				GroupServletHandler.groupContactViewRequestHandler(request, response);
				break;

			case "groupadd":

				LoggerSet.logError("GroupServlet", "doPost", "Group Add Hitted", null);

				GroupServletHandler.groupContactAddRequestHandler(request, response);
				break;
			case "groupremove":

				LoggerSet.logError("GroupServlet", "doPost", "Group remove Hitted", null);

				GroupServletHandler.groupContactRemoveRequestHandler(request, response);
				break;
			case "groupcreate":

				LoggerSet.logError("GroupServlet", "doPost", "group Create is empty", null);

				GroupServletHandler.createGroupRequestHandler(request, response);
				break;

			default:

				LoggerSet.logError("GroupServlet", "doPost", "Action string is undefined", null);
				request.setAttribute("errorMessage", "the action is undefined");
				request.getRequestDispatcher("home.jsp").forward(request, response);
				System.out.println("Unknown action");

			}
		} else {

			LoggerSet.logError("GroupServlet", "doPost", "Action string is empty", null);
			request.setAttribute("errorMessage", "the action parameter not provided");
			request.getRequestDispatcher("home.jsp").forward(request, response);

		}

	}

}
