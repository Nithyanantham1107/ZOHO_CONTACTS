package com.zohocontacts.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet implementation class UpdateUserGroupServlet
 */
public class UpdateUserGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserGroupServlet() {
		super();

	}

	/**
	 * Handles POST requests for updating user group information.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ((request.getParameter("groupid") != null && !request.getParameter("groupid").isBlank())
					&& (request.getParameter("groupName") != null && !request.getParameter("groupName").isBlank())) {
				List<CategoryRelation> categoryRelation = new ArrayList<CategoryRelation>();

				Category userGroup = new Category();

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();

				userGroup.setCreatedBY(userData.getID());
				userGroup.setID(Integer.parseInt(request.getParameter("groupid")));
				userGroup.setCategoryName(request.getParameter("groupName"));
				categoryRelation = UserGroupOperation.viewUserGroupContact(userGroup.getID(), userGroup.getCreatedBy());
				if (categoryRelation != null) {

					userGroup.setCategoryRelationAll(categoryRelation);
				} else {
					LoggerSet.logWarning("UpdateUserGroupServlet", "doPost",
							"Group contact is null for Group ID: " + userGroup.getID());
				}

				request.setAttribute("usergroupupdate", userGroup);
				request.getRequestDispatcher("groups.jsp").forward(request, response);
				LoggerSet.logInfo("UpdateUserGroupServlet", "doPost",
						"User group updated successfully: " + userGroup.getCategoryName());

			} else {
				LoggerSet.logWarning("UpdateUserGroupServlet", "doPost", "Group name should not be empty.");
				request.setAttribute("errorMessage", "Group name should not be empty.");
				request.getRequestDispatcher("groups.jsp").forward(request, response);
			}

		} catch (DBOperationException e) {
			LoggerSet.logError("UpdateUserGroupServlet", "doPost", "Exception occurred while updating user group.", e);
			request.setAttribute("errorMessage", "An error occurred while processing your request.");
			request.getRequestDispatcher("groups.jsp").forward(request, response);
		}
	}
}
