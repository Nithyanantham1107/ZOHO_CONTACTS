package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserGroupOperation;
import com.zohocontacts.dbpojo.Category;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

/**
 * Servlet implementation class DeleteUserGroupServlet
 */
public class DeleteUserGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteUserGroupServlet() {
		super();

}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * Handles POST requests to delete a user group.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (request.getParameter("groupid") != null && !request.getParameter("groupid").isBlank()) {

				
				int groupid = Integer.parseInt(request.getParameter("groupid"));
				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				Category category = new Category();
				category.setID(groupid);
				
				if (UserGroupOperation.deleteUserGroup(category, userData.getID())) {

					LoggerSet.logInfo("DeleteUserGroupServlet", "doPost", "User group deleted successfully: " + groupid);
					response.sendRedirect("groups.jsp");
				} else {
					LoggerSet.logWarning("DeleteUserGroupServlet", "doPost", "Error in deleting user group: " + groupid);
					request.setAttribute("errorMessage", "Error in deleting the group");
					request.getRequestDispatcher("groups.jsp").forward(request, response);
				}
				request.setAttribute("errorMessage", "Error in deleting the group");

			} else {
				LoggerSet.logWarning("DeleteUserGroupServlet", "doPost", "Group ID is null in delete request");
				request.setAttribute("errorMessage", "Group ID is null in delete post request");
				request.getRequestDispatcher("groups.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("DeleteUserGroupServlet", "doPost", "Exception occurred while deleting user group", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("groups.jsp").forward(request, response);
		}
	}
}
