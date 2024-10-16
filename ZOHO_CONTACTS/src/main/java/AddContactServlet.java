
import java.io.IOException;
import java.time.Instant;
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
import validation.UserValidation;

/**
 * Servlet implementation class Add_contact_servlet
 */

public class AddContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation co;
	UserContacts uc;
	HttpSession session;
	SessionOperation so;

	UserOperation user_op;
	UserGroupOperation ugo;
	UserContactOperation uco;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddContactServlet() {
		super();

		co = new UserContactOperation();
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
		System.out.println("hello im called at get  method!!");
		response.sendRedirect("Dashboard.jsp");
	}

	
	
	
	  /**
    * Handles POST requests to add a new user contact
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
   
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			if ((request.getParameter("f_name") != null && !request.getParameter("f_name").isBlank())
					&& (request.getParameter("gender") != null && !request.getParameter("gender").isBlank())
					&& (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
					&& (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
					&& (request.getParameter("email") != null && !request.getParameter("email").isBlank())) {
				uc = new UserContacts();
				session = request.getSession(false);
				UserData ud = (UserData) session.getAttribute("user");
				System.out.println("check 1" + request.getParameter("localDateTime"));
				System.out.println("check 2" + request.getParameter("timeZoneId"));
				System.out.println("check 3" + request.getParameter("utcDateTime"));

				uc.setFname(request.getParameter("f_name"));
				uc.setMname(request.getParameter("m_name"));
				uc.setLname(request.getParameter("l_name"));
				uc.setAddress(request.getParameter("Address"));
				uc.setGender(request.getParameter("gender"));
				uc.setPhoneno(request.getParameter("phone"));
				uc.setUserid(ud.getUserId());
				uc.setEmail(request.getParameter("email"));

				uc.setCreatedAt();
				uc = co.addUserContact(uc);
				if (uc != null) {

					ArrayList<UserContacts> uc = co.viewAllUserContacts(ud.getUserId());

					session.setAttribute("usercontact", uc);

					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
				} else {
					request.setAttribute("errorMessage", "Error in adding contact");
					request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);

				}

			} else {

				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);
			}

		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);

		}

	}

}
