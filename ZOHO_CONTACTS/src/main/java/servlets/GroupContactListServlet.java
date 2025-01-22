package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import dbpojo.Category;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class GroupContactList
 */

public class GroupContactListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation contactOperation;
	UserGroupOperation userGroupOperation;
	HttpSession session;
	UserOperation userOperation;
	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GroupContactListServlet() {
		super();

		contactOperation = new UserContactOperation();
		userOperation = new UserOperation();
		userGroupOperation = new UserGroupOperation();
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
		// TODO Auto-generated method stub

		try {

			System.out.println("hello im groupList contact");
			System.out.println("postpostpostpostpostpostpostpostpost" + request.getParameter("groupid"));

			if (request.getParameter("groupid") != null && !request.getParameter("groupid").isBlank()
					&& request.getParameter("method") != null && !request.getParameter("method").isBlank()) {
				String method = request.getParameter("method");
				int groupID = Integer.parseInt(request.getParameter("groupid"));
				SessionOperation so = new SessionOperation();
				CacheModel cachemodel = so.checkSessionAlive(so.getCustomSessionId(request.getCookies()));
				Userdata ud = cachemodel.getUserData();

				Category group = userGroupOperation.getSpecificGroup(groupID, ud.getID());

				request.setAttribute("group", group);
				
				logger.logInfo("UserSpecificContactRetrievalServlet", "doPost",
						"Retrieved specific contact info for Contact ID: " + groupID);

				if (method.equals("view")) {
					
					request.getRequestDispatcher("groupviewcontact.jsp").forward(request, response);
					
				}else {
					
					request.getRequestDispatcher("groupcontactadd.jsp").forward(request, response);	
				}
				
					
				
			} else {
				logger.logWarning("UserSpecificContactRetrievalServlet", "doPost", "Contact ID parameter is missing.");
				request.setAttribute("errorMessage", "Contact ID parameter is missing.");
				request.getRequestDispatcher("groups.jsp").forward(request, response);
			}
		} catch (

		Exception e) {
			logger.logError("UserSpecificContactRetrievalServlet", "doPost",
					"Exception occurred while retrieving specific contact.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("groups.jsp").forward(request, response);
		}
	}

}
