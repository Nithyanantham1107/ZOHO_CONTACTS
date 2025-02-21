package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.servletHandler.ContactServletHandler;

/**
 * Servlet implementation class ContactServlet
 */

public class ContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ContactServlet() {
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

			case "addcontact":

				ContactServletHandler.AddContactRequestHandler(request, response);
				break;

			case "viewcontact":
				ContactServletHandler.ContactViewRequestHandler(request, response);
				break;

			case "deletecontact":
				ContactServletHandler.DeleteContactRequestHandler(request, response);
				break;
			case "specificContact":
				ContactServletHandler.specificContactRetrievalRequestHandler(request, response);
				break;
			case "updateContact":
				ContactServletHandler.contactUpdateRequestHandler(request, response);

				break;
				
			case "mergecontact":
				ContactServletHandler.mergeContactRequestHandler(request, response);

				break;
			default:
				System.out.println("Unknown action");

			}
		} else {

			LoggerSet.logError(" ContactServlet", "doPost", "Action string is empty", null);
			request.setAttribute("errorMessage", "the action is undefined");
			request.getRequestDispatcher("home.jsp").forward(request, response);

		}
		
		
		
		}

	}

