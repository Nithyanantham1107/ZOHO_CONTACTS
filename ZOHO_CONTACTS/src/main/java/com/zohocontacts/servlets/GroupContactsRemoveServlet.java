package com.zohocontacts.servlets;

import java.io.IOException;

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
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class GroupContactsRemoveServlet
 */
public class GroupContactsRemoveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GroupContactsRemoveServlet() {
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

//	                int user_id = ud.getUserId();
				int contactID = Integer.parseInt(request.getParameter("contactID"));
				int groupID = Integer.parseInt(request.getParameter("groupID"));

				Category group = UserGroupOperation.getSpecificGroup(groupID, userData.getID());

				request.setAttribute("group", group);

				CategoryRelation categoryRelation = new CategoryRelation();

				categoryRelation.setCategoryID(groupID);
				categoryRelation.setContactIDtoJoin(contactID);

				if (UserGroupOperation.removeGroupContacts(categoryRelation, userData.getID())) {

					LoggerSet.logInfo("GroupContactsRemoveServlet", "doPost",
							"Contact removed successfully: " + contactID);
					request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);
				} else {
					LoggerSet.logWarning("GroupContactsRemoveServlet", "doPost",
							"Unable to remove contact: " + contactID);
					request.setAttribute("errorMessage", "Unable to delete contact");
					request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("GroupContactsRemoveServlet", "doPost",
						"Contact ID  and groupId param is null or empty");
				request.setAttribute("errorMessage",
						"Unable to remove contact because specified contact ID or GroupID is null");
				request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("GroupContactsRemoveServlet", "doPost", "Exception occurred while removing contact", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);
		}
	}

}
