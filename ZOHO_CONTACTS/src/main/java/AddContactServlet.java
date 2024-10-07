

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
        
        co =new UserContactOperation();
        so=new SessionOperation();
    	user_op = new UserOperation();
	    uco=new UserContactOperation();
	    ugo=new UserGroupOperation();
       
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("hello im called at get  method!!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			session=request.getSession(false);
			UserData ud=(UserData) session.getAttribute("user");
			String sessionid=so.getCustomSessionId(request.getCookies());
			int userid=so.checkSessionAlive(sessionid);
			if(userid !=0) {
				if(ud==null) {
					
					session = request.getSession();
					ud = user_op.getUserData(userid);
					session.setAttribute("user", ud);
					ArrayList<UserContacts> uc=uco.viewAllUserContacts(userid);
					ArrayList<UserGroup> ug=ugo.viewAllGroup(userid);
					session.setAttribute("usercontact", uc);
					session.setAttribute("usergroup", ug);
				}
				
				
				
			}else {
				
				
				 so.DeleteSessionData(sessionid);
				if(session != null ) {
					
					session.invalidate();
				}
				
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
		        response.setHeader("Pragma", "no-cache"); 
		        response.setDateHeader("Expires", 0);
				response.sendRedirect("index.jsp");
				return;
				
				
				
				
			}
			
			
			
			if( (request.getParameter("f_name") !=null && ! request.getParameter("f_name").isBlank()) && 
				(request.getParameter("gender") !=null &&  ! request.getParameter("gender").isBlank()) &&
				(request.getParameter("phone") !=null && ! request.getParameter("phone").isBlank()) && 
				(request.getParameter("Address") !=null  && ! request.getParameter("Address").isBlank()) && 
				(request.getParameter("email") !=null && ! request.getParameter("email").isBlank()) ){
					uc=new UserContacts();
					session=request.getSession(false);
					
					
					uc.setFname(request.getParameter("f_name"));
					uc.setMname(request.getParameter("m_name"));
					uc.setLname(request.getParameter("l_name"));
					uc.setAddress(request.getParameter("Address"));
				    uc.setGender(request.getParameter("gender"));
				    uc.setPhoneno(request.getParameter("phone"));
				    uc.setUserid(ud.getUserId());
				    uc.setEmail(request.getParameter("email"));
					
				    uc=co.addUserContact(uc);
				    if(uc !=null) {
				    	

					   
						ArrayList<UserContacts> uc=co.viewAllUserContacts(ud.getUserId());

						session.setAttribute("usercontact", uc);
				
					  
						request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
					}
					else {
						request.setAttribute("errorMessage", "Error in adding contact");
			            request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);

					}
				
						
					
				}else {
					
					request.setAttribute("errorMessage", "Parameter Data is empty!!");
		            request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);
				}
			
		}catch(Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);
			
		}
	
	}

}
