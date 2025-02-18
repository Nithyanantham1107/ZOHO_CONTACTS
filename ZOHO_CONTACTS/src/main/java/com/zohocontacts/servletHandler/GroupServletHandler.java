package com.zohocontacts.servletHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
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

public class GroupServletHandler {

	public static void createGroupRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

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

		} catch (DBOperationException e) {
			LoggerSet.logError("CreateGroupServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("groups.jsp").forward(request, response);
		}
	}

	public static void deleteGroupRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (request.getParameter("groupid") != null && !request.getParameter("groupid").isBlank()) {

				int groupid = Integer.parseInt(request.getParameter("groupid"));
				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				Category category = new Category();
				category.setID(groupid);

				if (UserGroupOperation.deleteUserGroup(category, userData.getID())) {

					LoggerSet.logInfo("DeleteUserGroupServlet", "doPost",
							"User group deleted successfully: " + groupid);
					response.sendRedirect("groups.jsp");
				} else {
					LoggerSet.logWarning("DeleteUserGroupServlet", "doPost",
							"Error in deleting user group: " + groupid);
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

	public static void groupContactAddRequestHandler(HttpServletRequest request, HttpServletResponse response)
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
				LoggerSet.logWarning("GroupContactAddServlet", "doPost",
						"Contact ID  and groupId param is null or empty");
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
	
	
	public static void groupContactViewRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		try {

			System.out.println("hello im groupList contact");
			System.out.println("postpostpostpostpostpostpostpostpost" + request.getParameter("groupid"));

			if (request.getParameter("groupid") != null && !request.getParameter("groupid").isBlank()
					&& request.getParameter("method") != null && !request.getParameter("method").isBlank()) {
				String method = request.getParameter("method");
				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				int groupID = Integer.parseInt(request.getParameter("groupid"));

				Category group = UserGroupOperation.getSpecificGroup(groupID, userData.getID());

				request.setAttribute("group", group);

				LoggerSet.logInfo("UserSpecificContactRetrievalServlet", "doPost",
						"Retrieved specific contact info for Contact ID: " + groupID);

				if (method.equals("view")) {

					request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);

				} else {

					request.getRequestDispatcher("groupcontactadd.jsp").forward(request, response);
				}

			} else {
				LoggerSet.logWarning("UserSpecificContactRetrievalServlet", "doPost",
						"Contact ID parameter is missing.");
				request.setAttribute("errorMessage", "Contact ID parameter is missing.");
				request.getRequestDispatcher("groups.jsp").forward(request, response);
			}
		} catch (

		DBOperationException e) {
			LoggerSet.logError("UserSpecificContactRetrievalServlet", "doPost",
					"Exception occurred while retrieving specific contact.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("groups.jsp").forward(request, response);
		}
	}
	
	
	public static void groupContactRemoveRequestHandler(HttpServletRequest request, HttpServletResponse response)
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
	
	
	public static void updateGroupRequestHandler(HttpServletRequest request, HttpServletResponse response)
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
