package com.zohocontacts.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.servletHandler.ContactServletHandler;
import com.zohocontacts.servletHandler.UserServletHandler;

/**
 * Servlet implementation class UserServlet
 */

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
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

			case "addemail":

				UserServletHandler.addUserEmailRequestHandler(request, response);

				break;

			case "deleteemail":
				UserServletHandler.deleteUserEmailRequestHandler(request, response);
				break;

			case "changepassword":

				UserServletHandler.changeUserPasswordRequestHandler(request, response);
				break;
			case "updateuser":
				UserServletHandler.userUpdateRequestHandler(request, response);
				break;

			default:
				System.out.println("Unknown action");

			}
		} else {

			LoggerSet.logError(" UserServlet", "doPost", "Action string is empty", null);
			request.setAttribute("errorMessage", "the action is undefined");
			request.getRequestDispatcher("home.jsp").forward(request, response);

		}

	}

}
