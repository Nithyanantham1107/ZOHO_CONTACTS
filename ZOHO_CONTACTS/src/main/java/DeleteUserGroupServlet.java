
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
 * Servlet implementation class DeleteUserGroup
 */

public class DeleteUserGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserGroupOperation ugo;
	HttpSession session;
	SessionOperation so;
	UserOperation user_op;
	UserContactOperation uco;
	UserData ud;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteUserGroupServlet() {
		super();
		ugo = new UserGroupOperation();
		so = new SessionOperation();
		user_op = new UserOperation();
		uco = new UserContactOperation();
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
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			if ((request.getParameter("groupid") != null  && ! request.getParameter("groupid").isBlank())) {
				int groupid = Integer.parseInt(request.getParameter("groupid"));
				if (ugo.deleteUserGroup(groupid)) {

					
					ud = (UserData) session.getAttribute("user");

					ArrayList<UserGroup> ug = ugo.viewAllGroup(ud.getUserId());

					session.setAttribute("usergroup", ug);
					response.sendRedirect("Dashboard.jsp");

				} else {
					System.out.println("error in deleting the groups ");
					request.setAttribute("errorMessage", "error in deleting the groups");
	                request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
				
				}
				System.out
						.println("hello this is the post of delete user group here" + request.getParameter("groupid"));
			} else {

				System.out.println("group id is null in delete post request");
				request.setAttribute("errorMessage", "group id is null in delete post request");
                request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
			}
		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
		}
	}

}
