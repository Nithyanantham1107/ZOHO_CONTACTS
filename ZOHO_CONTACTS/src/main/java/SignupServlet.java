
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
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import validation.UserValidation;

/**
 * Servlet implementation class Signup_servlet
 */

public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserOperation user_op;
	UserData ud;
	UserGroupOperation ugo;
	UserContactOperation uco;
	UserValidation uservalidate;
	HttpSession session;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignupServlet() {
		super();

		user_op = new UserOperation();
		uservalidate = new UserValidation();
	    uco=new UserContactOperation();
	    ugo=new UserGroupOperation();
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
			if (    (request.getParameter("password") != null && ! request.getParameter("password").isBlank()) && 
					(request.getParameter("Name") != null  && ! request.getParameter("Name").isBlank()) &&
					(request.getParameter("phone") != null  && ! request.getParameter("phone").isBlank())&& 
					(request.getParameter("Address") != null &&  ! request.getParameter("Address").isBlank()) &&
					(request.getParameter("email") != null && ! request.getParameter("Name").isBlank())) {

				if (uservalidate.validateUserPassword(request.getParameter("password"))) {
					ud = new UserData();
					System.out.println(request.getParameter("username") + "and" + request.getParameter("password"));
					ud.setName(request.getParameter("Name"));
					ud.setAddress(request.getParameter("Address"));
					ud.setUserName(request.getParameter("username"));
					ud.setPhoneno(request.getParameter("phone"));
					ud.setPrimaryMail(request.getParameter("email"));
					ud.setPassword(request.getParameter("password"));
					ud.setCurrentEmail(request.getParameter("email"));

					ud = user_op.createUser(ud);
					if (ud != null) {

						session = request.getSession();
						session.setAttribute("user", ud);
						ArrayList<UserContacts> uc=uco.viewAllUserContacts(ud.getUserId());
						ArrayList<UserGroup> ug=ugo.viewAllGroup(ud.getUserId());
						session.setAttribute("usercontact", uc);
						session.setAttribute("usergroup", ug);

						request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
					} else {
						request.setAttribute("errorMessage", "An error occured while creating user");
			            request.getRequestDispatcher("Signup.jsp").forward(request, response);
						
					}

				} else {
					System.out.println("password should contain atleast one Lower case ,one Upper case and numbers");
					request.setAttribute("errorMessage", "password is too long or cases and number are");
		            request.getRequestDispatcher("Signup.jsp").forward(request, response);
					
				}
			} else {
				
				request.setAttribute("errorMessage", "Parameter should not be empty!!");
	            request.getRequestDispatcher("Signup.jsp").forward(request, response);
			
			}

//			PrintWriter out=response.getWriter();
//			out.print("<h1> hello hi i  post</h1>");

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Signup.jsp").forward(request, response);
			
		}
	}

}
