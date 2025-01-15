package servlets;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dbmodel.UserGroup;
import dbmodel.UserData;
import dboperation.SessionOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class CreateGroupServlet
 */
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	UserGroupOperation ugo;
	HttpSession session;
	SessionOperation so;
	UserOperation user_op;
	LoggerSet logger; // LoggerSet instance

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateGroupServlet() {
		super();
		ugo = new UserGroupOperation();
		so = new SessionOperation();
		user_op = new UserOperation();
		logger = new LoggerSet(); // Initialize logger
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

			if (request.getParameter("methodtype") != null && request.getParameter("methodtype").equals("create")) {
				if ((request.getParameter("groupName") != null && !request.getParameter("groupName").isBlank())
						&& request.getParameterValues("contact_ids") != null) {

					int j = 0;
//                    ug = new UserGroup();
					Category category = new Category();
					String sessionid = (String) request.getAttribute("sessionid");
					CacheModel cachemodel = CacheData.getCache(sessionid);

					Userdata ud = cachemodel.getUserData();

//					int[] contactid = new int[request.getParameterValues("contact_ids").length];

					category.setCategoryName(request.getParameter("groupName"));
//                    ug.setGroupName(request.getParameter("groupName"));
//					category.setCreatedAt(Instant.now().toEpochMilli());
				
					
					category.setModifiedAt(Instant.now().toEpochMilli());
					category.setCreatedAt(category.getModifiedAt());
					for (String i : request.getParameterValues("contact_ids")) {
						if (i != null) {
							CategoryRelation categoryRelation = new CategoryRelation();
							categoryRelation.setCreatedAt(category.getModifiedAt());
							categoryRelation.setContactIDtoJoin(Integer.parseInt(i));
							category.setCategoryRelation(categoryRelation);
//                            contactid[j] = Integer.parseInt(i);

						}
					}

					
					category.setCreatedBY(ud.getUserId());
//                    ug.setUserid(ud.getUserId());

//                    ug.setcontactid(contactid);

					if (ugo.createGroup(category)) {
						ArrayList<Category> usergroup = ugo.viewAllGroup(ud.getUserId());
						if (usergroup != null) {
							cachemodel.setUserGroup(usergroup);

							logger.logInfo("CreateGroupServlet", "doPost",
									"Group created successfully: " + category.getCategoryName());
							response.sendRedirect("Dashboard.jsp");
						} else {
							logger.logWarning("CreateGroupServlet", "doPost",
									"Failed to view all groups for user ID: " + ud.getUserId());
							request.setAttribute("errorMessage", "Failed to view all groups of the user");
							request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
						}
					} else {
						logger.logWarning("CreateGroupServlet", "doPost",
								"Failed to create group: " + category.getCategoryName());
						request.setAttribute("errorMessage",
								"Failed to create " + category.getCategoryName() + " group");
						request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
					}
				} else {
					logger.logWarning("CreateGroupServlet", "doPost", "Group data is null");
					request.setAttribute("errorMessage", "Group data is null");
					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
				}
			} else {
				if (request.getParameter("groupName") != null && request.getParameterValues("contact_ids") != null) {
					int j = 0;
//					ug = new UserGroup();
					Category category = new Category();

					String sessionid = (String) request.getAttribute("sessionid");
					CacheModel cachemodel = CacheData.getCache(sessionid);

					Userdata ud = cachemodel.getUserData();
					category.setModifiedAt(Instant.now().toEpochMilli());
//					int[] contactid = new int[request.getParameterValues("contact_ids").length];

					category.setCategoryName(request.getParameter("groupName"));
					category.setCategoryID(Integer.parseInt(request.getParameter("groupdata")));
//					ug.setGroupName(request.getParameter("groupName"));
//					category.setCreatedAt(Instant.now().toEpochMilli());
					for (String i : request.getParameterValues("contact_ids")) {
						if (i != null && !i.isBlank()) {
//							contactid[j] = Integer.parseInt(i);
//							j++;

							CategoryRelation cr = new CategoryRelation();
                            
							cr.setCategoryID(category.getCategoryID());
							cr.setCreatedAt(category.getModifiedAt());
							cr.setContactIDtoJoin(Integer.parseInt(i));
							category.setCategoryRelation(cr);
						}
					}

					category.setCreatedBY(ud.getUserId());

//					ug.setUserid(ud.getUserId());
//					ug.setcontactid(contactid);

				
//					ug.set(Integer.parseInt(request.getParameter("groupdata")));

					if (ugo.updateUserGroup(category)) {
						ArrayList<Category> usergroup = ugo.viewAllGroup(ud.getUserId());
						if (usergroup != null) {
							cachemodel.setUserGroup(usergroup);

							logger.logInfo("CreateGroupServlet", "doPost",
									"Group updated successfully: " + category.getCategoryName());
							response.sendRedirect("Dashboard.jsp");
						} else {
							logger.logWarning("CreateGroupServlet", "doPost",
									"Failed to view all groups for user ID: " + ud.getUserId());
							request.setAttribute("errorMessage", "Failed to view all groups of the user");
							request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
						}
					} else {
						logger.logWarning("CreateGroupServlet", "doPost", "Failed to update User Group Data");
						request.setAttribute("errorMessage", "Failed to update User Group Data");
						request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
					}
				} else {
					logger.logWarning("CreateGroupServlet", "doPost", "Group data is null");
					request.setAttribute("errorMessage", "Group data is null");
					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
				}
			}
		} catch (Exception e) {
			logger.logError("CreateGroupServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
		}
	}
}
