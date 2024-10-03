
import java.io.IOException;
import java.io.PrintWriter;
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
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import validation.UserValidation;

/**
 * Servlet implementation class Login_signup_servlet
 */

public class LoginSignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserOperation user_op;
	UserGroupOperation ugo;
	UserContactOperation uco;
	UserData ud;
	UserValidation uservalidate;
	HttpSession session;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginSignupServlet() {

		super();

		user_op = new UserOperation();
		uservalidate=new UserValidation();
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
		session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", 0);
		response.sendRedirect("index.jsp");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			if ((request.getParameter("username") != null && ! request.getParameter("username").isBlank() )&& 
					(request.getParameter("password") != null && ! request.getParameter("password").isBlank())   ) {
				if (uservalidate.validateUserPassword(request.getParameter("password"))) {
					System.out.println(request.getParameter("username") + "and" + request.getParameter("password"));
					ud = new UserData();
					ud.setUserName(request.getParameter("username"));
					ud.setPassword(request.getParameter("password"));
					System.out.println("hello im new");
					ud = user_op.isUser(ud);
					if (ud != null) {
                        for(String i : ud.getEmail()) {
                        	System.out.println(i);
                        }
						session = request.getSession();
						session.setAttribute("user", ud);
						ArrayList<UserContacts> uc=uco.viewAllUserContacts(ud.getUserId());
						ArrayList<UserGroup> ug=ugo.viewAllGroup(ud.getUserId());
						session.setAttribute("usercontact", uc);
						session.setAttribute("usergroup", ug);
						request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
					} else {
						request.setAttribute("errorMessage", "invalid username and password");
			            request.getRequestDispatcher("Login.jsp").forward(request, response);

			
					}
				} else {
					System.out.println("password is too long or cases and numbers are missing");
					
					request.setAttribute("errorMessage", "password is too long or cases and numbers are missing");
		            request.getRequestDispatcher("Login.jsp").forward(request, response);

				}
			} else {
				request.setAttribute("errorMessage", "username and password should not be empty");
	            request.getRequestDispatcher("Login.jsp").forward(request, response);


				

			}



		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Login.jsp").forward(request, response);
		}
	}
}
