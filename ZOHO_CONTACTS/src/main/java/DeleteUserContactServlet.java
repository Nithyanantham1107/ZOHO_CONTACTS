

import java.io.IOException;
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

/**
 * Servlet implementation class Delete_user_contact
 */

public class DeleteUserContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      UserContactOperation co;
    
  	  UserContacts uc;
  	  HttpSession session;
      
		// TODO Auto-generated method stub

	 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUserContactServlet() {
        super();
        co=new UserContactOperation();
       
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("this is called in delete in get method"+request.getParameter("user_id"));
	
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		

	    try {
	    	
	    	if( (request.getParameter("contact_id")!=null   && ! request.getParameter("contact_id").isBlank())  ) {
	    		session = request.getSession(false);
				UserData ud = (UserData) session.getAttribute("user");
	    		
	    		int user_id=ud.getUserId();
	    		int contact_id=Integer.parseInt(request.getParameter("contact_id"));
	    		if(co.deleteContact(user_id, contact_id)) {
	    			ArrayList<UserContacts> uc=co.viewAllUserContacts(ud.getUserId());
					
					session.setAttribute("usercontact", uc);
					
	    			response.sendRedirect("Dashboard.jsp");
	    			
	    		}else {
	    			request.setAttribute("errorMessage", "unable to delete contact");
	                request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
	    			
	    		}
	    	}else {
	    		request.setAttribute("errorMessage", "unable to delete contact because specified contact id is null");
                request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
	    		
	    	}
	    	
	    }catch(Exception e){
	    	
	    	
	    	System.out.println(e);
	    	request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
	    }
		
	}

}
