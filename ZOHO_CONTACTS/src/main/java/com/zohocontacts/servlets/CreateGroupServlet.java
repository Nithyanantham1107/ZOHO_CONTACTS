package com.zohocontacts.servlets;

import java.io.IOException;
import java.time.Instant;
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
 * Servlet implementation class CreateGroupServlet
 */
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateGroupServlet() {
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
	 * Handles POST requests to create or update a user group.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

//			if (request.getParameter("methodtype") != null && request.getParameter("methodtype").equals("create")) {
			if ((request.getParameter("groupName") != null && !request.getParameter("groupName").isBlank())
					&& request.getParameterValues("contact_ids") != null) {

				Category category = new Category();

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				category.setCategoryName(request.getParameter("groupName"));

				category.setModifiedAt(Instant.now().toEpochMilli());
				category.setCreatedAt(category.getModifiedAt());
				for (String i : request.getParameterValues("contact_ids")) {
					if (i != null) {
						CategoryRelation categoryRelation = new CategoryRelation();
						categoryRelation.setCreatedAt(category.getModifiedAt());
						categoryRelation.setContactIDtoJoin(Integer.parseInt(i));
						category.setCategoryRelation(categoryRelation);

					}
				}

				category.setCreatedBY(userData.getID());

				if (UserGroupOperation.createGroup(category, userData.getID())) {
					List<Category> usergroup = UserGroupOperation.viewAllGroup(userData.getID());
					if (usergroup != null) {

						LoggerSet.logInfo("CreateGroupServlet", "doPost",
								"Group created successfully: " + category.getCategoryName());
						response.sendRedirect("groups.jsp");
					} else {
						LoggerSet.logWarning("CreateGroupServlet", "doPost",
								"Failed to view all groups for user ID: " + userData.getID());
						request.setAttribute("errorMessage", "Failed to view all groups of the user");
						request.getRequestDispatcher("groups.jsp").forward(request, response);
					}
				} else {
					LoggerSet.logWarning("CreateGroupServlet", "doPost",
							"Failed to create group: " + category.getCategoryName());
					request.setAttribute("errorMessage", "Failed to create " + category.getCategoryName() + " group");
					request.getRequestDispatcher("groups.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("CreateGroupServlet", "doPost", "Group data is null");
				request.setAttribute("errorMessage", "Group data is null");
				request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
			}
//			} else {
//				if (request.getParameter("groupName") != null && request.getParameterValues("contact_ids") != null) {
//					Category category = new Category();
//					String sessionid = (String) request.getAttribute("sessionid");
//					CacheModel cachemodel = CacheData.getCache(sessionid);
//					UserData userData = cachemodel.getUserData();
//					category.setModifiedAt(Instant.now().toEpochMilli());
//					category.setCategoryName(request.getParameter("groupName"));
//					category.setID(Integer.parseInt(request.getParameter("groupdata")));
//					for (String values : request.getParameterValues("contact_ids")) {
//						if (values != null && !values.isBlank()) {
//							CategoryRelation categoryRelation = new CategoryRelation();
//							categoryRelation.setCategoryID(category.getID());
//							categoryRelation.setCreatedAt(category.getModifiedAt());
//							categoryRelation.setContactIDtoJoin(Integer.parseInt(values));
//							category.setCategoryRelation(categoryRelation);
//						}
//					}
//
//					category.setCreatedBY(userData.getID());
//
//
//					if (UserGroupOperation.updateUserGroup(category, userData.getID())) {
//						List<Category> usergroup = UserGroupOperation.viewAllGroup(userData.getID());
//						if (usergroup != null) {
//
//							logger.logInfo("CreateGroupServlet", "doPost",
//									"Group updated successfully: " + category.getCategoryName());
//							response.sendRedirect("Dashboard.jsp");
//						} else {
//							logger.logWarning("Create  <form action=\"/index.jsp\" method=\"get\">\n"
//									+ "            <input type=\"submit\" value=\"Back\" class=\"back-btn\" />\n"
//									+ "        </form>GroupServlet", "doPost",
//									"Failed to view all groups for user ID: " + userData.getID());
//							request.setAttribute("errorMessage", "Failed to view all groups of the user");
//							request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
//						}
//					} else {
//						logger.logWarning("CreateGroupServlet", "doPost", "Failed to update User Group Data");
//						request.setAttribute("errorMessage", "Failed to update User Group Data");
//						request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
//					}
//				} else {
//					logger.logWarning("CreateGroupServlet", "doPost", "Group data is null");
//					request.setAttribute("errorMessage", "Group data is null");
//					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
//				}
//			}
		} catch (DBOperationException e) {
			LoggerSet.logError("CreateGroupServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("groups.jsp").forward(request, response);
		}
	}
}
