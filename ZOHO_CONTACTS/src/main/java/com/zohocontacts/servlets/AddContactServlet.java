package com.zohocontacts.servlets;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserContactOperation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.ContactMail;
import com.zohocontacts.dbpojo.ContactPhone;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class Add_contact_servlet
 */
public class AddContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddContactServlet() {
		super();


	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("hello I'm called at get method!!");
		response.sendRedirect("Dashboard.jsp");
	}

	/**
	 * Handles POST requests to add a new user contact
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			System.out.println("the add contact srevlet is hetted!!!!");
			if ((request.getParameter("f_name") != null && !request.getParameter("f_name").isBlank())
					&& (request.getParameter("gender") != null && !request.getParameter("gender").isBlank())
					&& (request.getParameterValues("phones") != null && request.getParameterValues("phones").length > 0)
					&& (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
					&& (request.getParameterValues("emails") != null
							&& request.getParameterValues("emails").length > 0)) {

				ContactDetails contact = new ContactDetails();
				String[] mails = request.getParameterValues("emails");
				String[] phones = request.getParameterValues("phones");

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				contact.setCreatedAt(Instant.now().toEpochMilli());
				contact.setModifiedAt(contact.getCreatedAt());

				contact.setFirstName(request.getParameter("f_name"));
				contact.setMiddleName(request.getParameter("m_name"));
				contact.setLastName(request.getParameter("l_name"));
				contact.setAddress(request.getParameter("Address"));
				contact.setGender(request.getParameter("gender"));
				contact.setUserID(userData.getID());

				for (String mail : mails) {

					ContactMail tempMail = new ContactMail();
					tempMail.setContactMailID(mail);
					tempMail.setCreatedAt(contact.getCreatedAt());
					tempMail.setModifiedAt(contact.getCreatedAt());
					contact.setContactMail(tempMail);

				}

				for (String phone : phones) {

					ContactPhone tempPhone = new ContactPhone();
					tempPhone.setContactPhone(phone);

					tempPhone.setCreatedAt(contact.getCreatedAt());
					tempPhone.setModifiedAt(contact.getCreatedAt());
					contact.setContactPhone(tempPhone);
				}

				contact.setCreatedAt(Instant.now().toEpochMilli());
				;
				contact = UserContactOperation.addUserContact(contact);
				if (contact != null) {

					LoggerSet.logInfo("AddContactServlet", "doPost",
							"Contact added successfully for user ID: " + userData.getID());
					request.getRequestDispatcher("home.jsp").forward(request, response);
				} else {
					LoggerSet.logWarning("AddContactServlet", "doPost",
							"Error in adding contact for user ID: " + userData.getID());
					request.setAttribute("errorMessage", "Error in adding contact");
					request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("AddContactServlet", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("AddContactServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);
		}
	}
}
