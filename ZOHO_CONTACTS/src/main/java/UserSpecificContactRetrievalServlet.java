
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
 * Servlet implementation class UserSpecificContactRetrieval
 */

public class UserSpecificContactRetrievalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation co;
	UserContacts uc;
	HttpSession session;
	UserGroup ug;
	UserGroupOperation ugo;
	SessionOperation so;
	UserOperation user_op;

	UserContactOperation uco;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserSpecificContactRetrievalServlet() {
		super();
		co = new UserContactOperation();
		uc = new UserContacts();
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

			UserData user = (UserData) session.getAttribute("user");
			System.out.println("hello this is post of update user specific servlet servlet");
			if (request.getParameter("contact_id") != null) {
				int user_id = user.getUserId();
				int contact_id = Integer.parseInt(request.getParameter("contact_id"));
				UserContacts uc = co.viewSpecificUserContact(user_id, contact_id);
				System.out.println(uc.getFname() + uc.getMname());
				if (uc != null) {

					System.out.println("hello this is the data of user updation");
					request.setAttribute("user_spec_contact", uc);
					request.getRequestDispatcher("Update_contact.jsp").forward(request, response);

				} else {
					request.setAttribute("errorMessage", "cant retrieve info of specific user");
					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

				}
			}

		} catch (Exception e) {

			System.out.println(e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		}
	}

}
