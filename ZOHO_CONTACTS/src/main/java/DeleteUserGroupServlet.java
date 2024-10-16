
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
     * Handles POST requests to delete a user group.
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {

			if ((request.getParameter("groupid") != null && !request.getParameter("groupid").isBlank())) {
				int groupid = Integer.parseInt(request.getParameter("groupid"));
				if (ugo.deleteUserGroup(groupid)) {
                    session=request.getSession(false);
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
