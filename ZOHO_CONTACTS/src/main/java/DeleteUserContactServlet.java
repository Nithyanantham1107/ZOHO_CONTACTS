
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class Delete_user_contact
 */

public class DeleteUserContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation uco;

	UserContacts uc;
	HttpSession session;

	UserGroup ug;
	UserGroupOperation ugo;

	SessionOperation so;
	UserOperation user_op;
	UserData ud;

	// TODO Auto-generated method stub

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteUserContactServlet() {
		super();
		uco = new UserContactOperation();
		ugo = new UserGroupOperation();
		so = new SessionOperation();
		user_op = new UserOperation();
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
		System.out.println("this is called in delete in get method" + request.getParameter("user_id"));

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

			if ((request.getParameter("contact_id") != null && !request.getParameter("contact_id").isBlank())) {

				ud = (UserData) session.getAttribute("user");

				int user_id = ud.getUserId();
				int contact_id = Integer.parseInt(request.getParameter("contact_id"));
				if (uco.deleteContact(user_id, contact_id)) {
					ArrayList<UserContacts> uc = uco.viewAllUserContacts(ud.getUserId());

					session.setAttribute("usercontact", uc);

					response.sendRedirect("Dashboard.jsp");

				} else {
					request.setAttribute("errorMessage", "unable to delete contact");
					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

				}
			} else {
				request.setAttribute("errorMessage", "unable to delete contact because specified contact id is null");
				request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

			}

		} catch (Exception e) {

			System.out.println(e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
		}

	}

}
