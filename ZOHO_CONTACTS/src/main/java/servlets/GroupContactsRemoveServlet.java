package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class GroupContactsRemoveServlet
 */
public class GroupContactsRemoveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation userContactOperation;
	UserGroupOperation userGroupOperation;
	SessionOperation ssessionOperation;
	UserOperation userOperation;
	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GroupContactsRemoveServlet() {
		super();

		userContactOperation = new UserContactOperation();
		userGroupOperation = new UserGroupOperation();
		ssessionOperation = new SessionOperation();
		userOperation = new UserOperation();
		logger = new LoggerSet();
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
				String sessionid = (String) request.getAttribute("sessionid");
				CacheModel cachemodel = CacheData.getCache(sessionid);

				
				Userdata userData = cachemodel.getUserData();

//	                int user_id = ud.getUserId();
				int contactID = Integer.parseInt(request.getParameter("contactID"));
				int groupID = Integer.parseInt(request.getParameter("groupID"));

				
				
				Category group = userGroupOperation.getSpecificGroup(groupID, userData.getID());

				request.setAttribute("group", group);
				
				CategoryRelation categoryRelation = new CategoryRelation();

				categoryRelation.setCategoryID(groupID);
				categoryRelation.setContactIDtoJoin(contactID);

				if (userGroupOperation.removeGroupContacts(categoryRelation, userData.getID())) {

					logger.logInfo("GroupContactsRemoveServlet", "doPost",
							"Contact removed successfully: " + contactID);
					request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);
				} else {
					logger.logWarning("GroupContactsRemoveServlet", "doPost", "Unable to remove contact: " + contactID);
					request.setAttribute("errorMessage", "Unable to delete contact");
					request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);
				}
			} else {
				logger.logWarning("GroupContactsRemoveServlet", "doPost",
						"Contact ID  and groupId param is null or empty");
				request.setAttribute("errorMessage",
						"Unable to remove contact because specified contact ID or GroupID is null");
				request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);
			}
		} catch (Exception e) {
			logger.logError("GroupContactsRemoveServlet", "doPost", "Exception occurred while removing contact", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);
		}
	}

}
