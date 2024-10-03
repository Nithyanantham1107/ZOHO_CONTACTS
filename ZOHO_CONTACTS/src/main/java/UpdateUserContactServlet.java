
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

/**
 * Servlet implementation class Update_user_contact
 */

public class UpdateUserContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation co;
	UserContacts uc;
	
	HttpSession session;

	// TODO Auto-generated method stub

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserContactServlet() {
		super();
		co = new UserContactOperation();
		uc = new UserContacts();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		System.out.println("this is called in update in get method"+request.getParameter("user_id"));

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub System.out.println("1");
		try {
           
            	System.out.println(request.getParameter("method"));
			System.out.println("hello this is post of update servlet" +request.getParameter("contactid"));
        
			if ((request.getParameter("f_name") != null && ! request.getParameter("f_name").isBlank() )&& 
					(request.getParameter("gender") != null && ! request.getParameter("gender").isBlank()) && 
					(request.getParameter("phone") != null && ! request.getParameter("phone").isBlank()) && 
					(request.getParameter("Address") != null && ! request.getParameter("Address").isBlank()) && 
					(request.getParameter("email") != null && ! request.getParameter("email").isBlank())) {

				session = request.getSession(false);
				UserData ud = (UserData) session.getAttribute("user");
				uc.setContactid(Integer.parseInt(request.getParameter("contactid")));
				uc.setFname(request.getParameter("f_name"));
				uc.setMname(request.getParameter("m_name"));
				uc.setLname(request.getParameter("l_name"));
				// Get close button
				uc.setAddress(request.getParameter("Address"));
				uc.setGender(request.getParameter("gender"));
				uc.setPhoneno(request.getParameter("phone"));
				uc.setUserid(ud.getUserId());
				uc.setEmail(request.getParameter("email"));
				System.out.println("check 1");
				if (co.updateSpecificUserContact(uc)) {
					System.out.println("check 2");
					ArrayList<UserContacts> uc=co.viewAllUserContacts(ud.getUserId());

					session.setAttribute("usercontact", uc);
					
			

					request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
				}else {
					request.setAttribute("errorMessage", "Error while trying update the contact ,Try again..");
		            request.getRequestDispatcher("upadate_contact.jsp").forward(request, response);

				}

			}
            

		} catch (Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("upadate_contact.jsp").forward(request, response);
			
		}

		

	}

}
