package com.zohocontacts.servlets;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserGroupOperation;
import com.zohocontacts.dbpojo.Category;
import com.zohocontacts.dbpojo.CategoryRelation;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class GroupContactAddServlet
 */
public class GroupContactAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GroupContactAddServlet() {
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

			if (request.getParameter("contactID") != null && !request.getParameter("contactID").isBlank()
					&& request.getParameter("groupID") != null && !request.getParameter("groupID").isBlank()

			) {

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				int contactID = Integer.parseInt(request.getParameter("contactID"));
				int groupID = Integer.parseInt(request.getParameter("groupID"));

				Category group = UserGroupOperation.getSpecificGroup(groupID, userData.getID());

				request.setAttribute("group", group);

				CategoryRelation categoryRelation = new CategoryRelation();

				categoryRelation.setCategoryID(groupID);
				categoryRelation.setContactIDtoJoin(contactID);

				categoryRelation.setCreatedAt(Instant.now().toEpochMilli());
				categoryRelation.setModifiedAt(categoryRelation.getCreatedAt());

				if (UserGroupOperation.addGroupContacts(categoryRelation, userData.getID())) {

					LoggerSet.logInfo("GroupContactAddServlet", "doPost", "Contact added successfully: " + contactID);
					request.getRequestDispatcher("groupcontactadd.jsp").forward(request, response);
				} else {
					LoggerSet.logWarning("GroupContactAddServlet", "doPost", "Unable to add contact: " + contactID);
					request.setAttribute("errorMessage", "Unable to add contact");
					request.getRequestDispatcher("groupcontactadd.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("GroupContactAddServlet", "doPost", "Contact ID  and groupId param is null or empty");
				request.setAttribute("errorMessage",
						"Unable to remove contact because specified contact ID or GroupID is null");
				request.getRequestDispatcher("groupcontactadd.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("GroupContactAddServlet", "doPost", "Exception occurred while add contact", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("groupcontactadd.jsp").forward(request, response);
		}
	}

}
