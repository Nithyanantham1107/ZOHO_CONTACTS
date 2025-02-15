package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dboperation.SessionOperation;
import dboperation.UserGroupOperation;
import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class UpdateUserGroupServlet
 */
public class UpdateUserGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserGroupOperation userGroupOperation;

	HttpSession session;
	SessionOperation sessionOperation;
	LoggerSet logger; // LoggerSet instance

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserGroupServlet() {
		super();
		userGroupOperation = new UserGroupOperation();

		sessionOperation = new SessionOperation();
		logger = new LoggerSet(); // Initialize logger
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
                ArrayList<CategoryRelation> categoryRelation=new ArrayList<CategoryRelation>();
				String sessionID = (String) request.getAttribute("sessionid");
				CacheModel cachemodel = CacheData.getCache(sessionID);
				Category userGroup = new Category();

				Userdata userData = cachemodel.getUserData();
//                ug.setUserid(ud.getUserId());
//                ug.setGroupid(Integer.parseInt(request.getParameter("groupid")));
//                ug.setGroupName(request.getParameter("groupName"));

				userGroup.setCreatedBY(userData.getID());
				userGroup.setID(Integer.parseInt(request.getParameter("groupid")));
				userGroup.setCategoryName(request.getParameter("groupName"));
				  categoryRelation = UserGroupOperation.viewUserGroupContact(userGroup.getID(), userGroup.getCreatedBy());
				if (categoryRelation != null) {
//					ug.setcontactid(value);
					userGroup.setCategoryRelationAll(categoryRelation);
				} else {
					logger.logWarning("UpdateUserGroupServlet", "doPost",
							"Group contact is null for Group ID: " + userGroup.getID());
				}

				request.setAttribute("usergroupupdate", userGroup);
				request.getRequestDispatcher("groups.jsp").forward(request, response);
				logger.logInfo("UpdateUserGroupServlet", "doPost",
						"User group updated successfully: " + userGroup.getCategoryName());

			} else {
				logger.logWarning("UpdateUserGroupServlet", "doPost", "Group name should not be empty.");
				request.setAttribute("errorMessage", "Group name should not be empty.");
				request.getRequestDispatcher("groups.jsp").forward(request, response);
			}

		} catch (DBOperationException  e) {
			logger.logError("UpdateUserGroupServlet", "doPost", "Exception occurred while updating user group.", e);
			request.setAttribute("errorMessage", "An error occurred while processing your request.");
			request.getRequestDispatcher("groups.jsp").forward(request, response);
		}
	}
}
