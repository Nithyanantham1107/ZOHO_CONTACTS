
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbmodel.UserContacts;
import dbmodel.UserData;
import dbmodel.UserGroup;
import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;

/**
 * Servlet implementation class CreateGroupServlet
 */

public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserGroup ug;
	UserGroupOperation ugo;
	HttpSession session;
	SessionOperation so;
	UserOperation user_op;

	UserContactOperation uco;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateGroupServlet() {
		super();
		ugo = new UserGroupOperation();
		so = new SessionOperation();
		user_op = new UserOperation();
		uco = new UserContactOperation();
		ugo = new UserGroupOperation();

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

			session = request.getSession(false);
			UserData ud = (UserData) session.getAttribute("user");
			String sessionid = so.getCustomSessionId(request.getCookies());
			int userid = so.checkSessionAlive(sessionid);
			if (userid != 0) {
				if (ud == null) {

					session = request.getSession();
					ud = user_op.getUserData(userid);

					ArrayList<UserContacts> uc = uco.viewAllUserContacts(userid);
					ArrayList<UserGroup> ug = ugo.viewAllGroup(userid);

					session.setAttribute("user", ud);
					session.setAttribute("usercontact", uc);
					session.setAttribute("usergroup", ug);
				}

			} else {

				so.DeleteSessionData(sessionid);
				if (session != null) {

					session.invalidate();
				}

				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				response.sendRedirect("index.jsp");
				return;

			}

			
			
			// upto this session check is implemented
			
			
			
			
			
			if (request.getParameter("methodtype") != null && request.getParameter("methodtype").equals("create")) {
				System.out.println("1St check");
				if ((request.getParameter("groupName") != null && !request.getParameter("groupName").isBlank())
						&& request.getParameterValues("contact_ids") != null) {

					int j = 0;
					ug = new UserGroup();

					ud = (UserData) session.getAttribute("user");
					int[] contactid = new int[request.getParameterValues("contact_ids").length];
					ug.setGroupName(request.getParameter("groupName"));

					System.out.println("chey here update is here see");
					for (String i : request.getParameterValues("contact_ids")) {
						if (i != null) {

							contactid[j] = Integer.parseInt(i);
							j++;

						}

					}
					ug.setUserid(ud.getUserId());
					ug.setcontactid(contactid);
					System.out.println("here anothe set of update here");
					if (ugo.createGroup(ug)) {
						System.out.println("creation of group is successfull");
						ArrayList<UserGroup> usergroup = ugo.viewAllGroup(ud.getUserId());
						if (usergroup != null) {
							session.setAttribute("usergroup", usergroup);
							response.sendRedirect("Dashboard.jsp");

						} else {
							System.out.println("failed to view all groups of the user");

							request.setAttribute("errorMessage", "failed to view all groups of the user");
							request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

						}

					} else {

						request.setAttribute("errorMessage",
								"failed to create" + request.getParameter("groupName") + " group");
						request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

					}
				} else {
					System.out.println("Group data is null");
					request.setAttribute("errorMessage", "Group data is null");
					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

				}

			} else {

				System.out.println("here updation of group is performed");

				if (request.getParameter("groupName") != null && request.getParameterValues("contact_ids") != null) {
					int j = 0;
					ug = new UserGroup();
					System.out.println("here updation of group is performed" + request.getParameter("groupName")
							+ "then" + request.getParameter("methodtype") + "then" + request.getParameter("groupdata"));

					session = request.getSession(false);
					ud = (UserData) session.getAttribute("user");
					int[] contactid = new int[request.getParameterValues("contact_ids").length];

					ug.setGroupName(request.getParameter("groupName"));

					for (String i : request.getParameterValues("contact_ids")) {
						if (i != null && !i.isBlank()) {
							contactid[j] = Integer.parseInt(i);

							j++;
						}

					}

					ug.setUserid(ud.getUserId());
					ug.setcontactid(contactid);

					ug.setGroupid(Integer.parseInt(request.getParameter("groupdata")));

					if (ugo.updateUserGroup(ug)) {
						System.out.println("updation  of group is successfull");
						ArrayList<UserGroup> usergroup = ugo.viewAllGroup(ud.getUserId());
						if (usergroup != null) {
							session.setAttribute("usergroup", usergroup);
							response.sendRedirect("Dashboard.jsp");

						} else {
							System.out.println("failed to view all groups of the user");

							request.setAttribute("errorMessage", "failed to view all groups of the user");
							request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
						}

					} else {
						request.setAttribute("errorMessage", "failed to update User Group Data");
						request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

					}
				} else {
					System.out.println("Group data is null");
					request.setAttribute("errorMessage", "Group data is null");
					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

				}

			}

		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
		}
	}

}
