
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbmodel.UserData;
import dbmodel.UserGroup;
import dboperation.UserGroupOperation;

/**
 * Servlet implementation class UpdateUserGroupServlet
 */

public class UpdateUserGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserGroupOperation ugo;
	UserGroup ug;
	HttpSession session;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserGroupServlet() {
		super();
		ugo = new UserGroupOperation();
		ug = new UserGroup();
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

		System.out
				.println("hello this is the post of update user group servlet see!!" + request.getParameter("groupid"));

		try {
			if ((request.getParameter("groupid") != null && ! request.getParameter("groupid").isBlank()) &&
				(request.getParameter("groupName") != null && ! request.getParameter("groupName").isBlank()) 	
					
					) {
				session = request.getSession(false);
				UserData ud = (UserData) session.getAttribute("user");
				ug.setUserid(ud.getUserId());
				ug.setGroupid(Integer.parseInt(request.getParameter("groupid")));
				ug.setGroupName(request.getParameter("groupName"));
				int[] value=ugo.viewUserGroupContact(ug.getGroupid(), ug.getUserid());
				if(value !=null) {
					ug.setcontactid(value);
				}else {
					System.out.println("group contact is null");
//					request.setAttribute("errorMessage", "group contact is null");
//		            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
				}
						
						
				request.setAttribute("usergroupupdate", ug);
				request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

			} else {
				System.out.println("group should not empty ");
				request.setAttribute("errorMessage", "groupname should not be empty");
	            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
			}

		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
		}
	}

}
