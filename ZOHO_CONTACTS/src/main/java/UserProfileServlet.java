
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbmodel.UserData;
import dboperation.UserOperation;

/**
 * Servlet implementation class User_profile_servlet
 */

public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserData ud;
	UserOperation uo;
	HttpSession session;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserProfileServlet() {
		super();
		uo = new UserOperation();
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
			UserData user_session_data = (UserData) session.getAttribute("user");
			
			
			System.out.println(request.getParameter("method")+"hey see here");
			if(request.getParameter("method").equals("update")){
				 for(String value : request.getParameterValues("email")) {
	            	   System.out.println("here data is"+value);
	            	   
	               }
				if ((request.getParameter("Name") != null && ! request.getParameter("Name").isBlank()) 
						&& (request.getParameter("username") != null && ! request.getParameter("username").isBlank()) 
						&& (request.getParameter("phone") != null && ! request.getParameter("phone").isBlank()) 
						&& (request.getParameter("Address") != null  && ! request.getParameter("Address").isBlank()) 
						&& (request.getParameterValues("email") != null) && ! request.getParameter("email").isBlank()) {

					

					if ((request.getParameter("Newpassword") != null && ! request.getParameter("email").isBlank()) &&
						(  ! request.getParameter("password").isBlank()	&& user_session_data.getPassword().equals(request.getParameter("password")))    ) {

						System.out.println("hello this is update in user data profile");
						ud = new UserData();
						ud.setUserId(user_session_data.getUserId());
						ud.setName(request.getParameter("Name"));
						ud.setAddress(request.getParameter("Address"));
						ud.setUserName(request.getParameter("username"));
						ud.setPhoneno(request.getParameter("phone"));
						ud.setPassword(request.getParameter("Newpassword"));
						ud.setEmail(request.getParameterValues("email"));
						ud.setCurrentEmail(user_session_data.getCurrentEmail());
	                    ud.setPrimaryMail(request.getParameter("primaryemail"));
						if (uo.userDataUpdate(ud)) {
							
							session.setAttribute("user", ud);
							response.sendRedirect("Dashboard.jsp");
						} else {
							 request.setAttribute("errorMessage", "Error in updating profile Data");
			                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
						}

					} else {

						System.out.println("hello this is update in user data profile");
						ud = new UserData();
						ud.setUserId(user_session_data.getUserId());
						ud.setName(request.getParameter("Name"));
						ud.setAddress(request.getParameter("Address"));
						ud.setUserName(request.getParameter("username"));
						ud.setPhoneno(request.getParameter("phone"));
						ud.setPassword(user_session_data.getPassword());
						ud.setEmail(request.getParameterValues("email"));
						
	                    ud.setPrimaryMail(request.getParameter("primaryemail"));
	                    ud.setCurrentEmail(user_session_data.getCurrentEmail());
						if (uo.userDataUpdate(ud)) {
							
							session = request.getSession(false);
							session.setAttribute("user", ud);
							response.sendRedirect("Dashboard.jsp");
						} else {
							request.setAttribute("errorMessage", "Error in updating profile Data");
		                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
						}
					}

				} else {
					 request.setAttribute("errorMessage", "Parameter cannot be empty.");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
					
				}
			   
			}else {
				if(uo.deleteUserProfile(user_session_data.getUserId())) {
					System.out.println(" successful Delete of user profile");
					session.invalidate();
					response.sendRedirect("index.jsp");
					
					
				}else {
					
					System.out.println("Error in Delete of user profile");
					
					request.setAttribute("errorMessage", "unable to delete  user profile data");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
					

				}
				
				System.out.println("Delete of user profile");
			}
			

		} catch (Exception e) {

			System.out.println(e);
			request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
		}

	}

}
