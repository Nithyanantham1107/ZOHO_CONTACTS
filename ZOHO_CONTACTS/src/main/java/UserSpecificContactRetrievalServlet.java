
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
     * Handles POST requests for retrieving a specific user's contact information.
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
			
			// upto this session check is implemented
			session=request.getSession(false);
			UserData user = (UserData) session.getAttribute("user");
			System.out.println("hello this is post of update user specific servlet servlet");
			if (request.getParameter("contact_id") != null) {
				int user_id = user.getUserId();
				int contact_id = Integer.parseInt(request.getParameter("contact_id"));
				UserContacts uc = co.viewSpecificUserContact(user_id, contact_id);

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
