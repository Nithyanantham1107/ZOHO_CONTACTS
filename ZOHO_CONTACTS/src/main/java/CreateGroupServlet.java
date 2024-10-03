

import java.io.IOException;
import java.util.ArrayList;

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
 * Servlet implementation class CreateGroupServlet
 */

public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      UserGroup ug; 
      UserGroupOperation ugo;
      HttpSession session;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateGroupServlet() {
        super();
        ugo=new UserGroupOperation();
        
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("hello this post of cre"
				+ "ate group"+request.getParameter("groupName"));
		System.out.println("here method of call is" + request.getParameter("methodtype"));
		
		System.out.println("here method of data is" + request.getParameter("groupdata"));
		for(String i :request.getParameterValues("contact_ids")) {
			
			System.out.println("hello this contact id is"+ i);
		}
		try {
			if(request.getParameter("methodtype") !=null && request.getParameter("methodtype").equals("create")) {
				System.out.println("1St check");
				if((request.getParameter("groupName") != null && ! request.getParameter("groupName").isBlank())&& 
				   request.getParameterValues("contact_ids")!=null) {
					
					
					int j=0;
					ug=new UserGroup();
					session=request.getSession(false);
					UserData ud=(UserData) session.getAttribute("user");
					int[] contactid=new int[request.getParameterValues("contact_ids").length];
					ug.setGroupName(request.getParameter("groupName"));
					
					System.out.println("chey here update is here see");
					for(String i : request.getParameterValues("contact_ids")) {
						if(i !=null) {
							
							contactid[j]=Integer.parseInt(i);
							j++;
							
						}
						
						
						
					}
					ug.setUserid(ud.getUserId());
					ug.setcontactid(contactid);
					System.out.println("here anothe set of update here");
					if(ugo.createGroup(ug)) {
						System.out.println("creation of group is successfull");
						ArrayList<UserGroup> usergroup=ugo.viewAllGroup(ud.getUserId());
						if(usergroup!=null) {
							session.setAttribute("usergroup", usergroup);
							response.sendRedirect("Dashboard.jsp");
							
							
						}else {
							System.out.println("failed to view all groups of the user");
							
							request.setAttribute("errorMessage", "failed to view all groups of the user");
		                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
							
						}
						
					}else {
						
						request.setAttribute("errorMessage", "failed to create"+ request.getParameter("groupName") +" group");
	                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
						
	                    
	                    
					}
				}else {
					System.out.println("Group data is null");
					request.setAttribute("errorMessage", "Group data is null");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);

				}
				
				
			}else {
				
				System.out.println("here updation of group is performed");
				
				if(request.getParameter("groupName") != null && request.getParameterValues("contact_ids")!=null) {
					int j=0;
					ug=new UserGroup();
					session=request.getSession(false);
					UserData ud=(UserData) session.getAttribute("user");
					int[] contactid=new int[request.getParameterValues("contact_ids").length];
					ug.setGroupName(request.getParameter("groupName"));
					for(String i : request.getParameterValues("contact_ids")) {
						if(i !=null) {
						contactid[j]=Integer.parseInt(i);
						
						
						
						j++;
						}
						
						
					}
					ug.setUserid(ud.getUserId());
					ug.setcontactid(contactid);
					ug.setGroupid(Integer.parseInt(request.getParameter("groupid")));
					if(ugo.updateUserGroup(ug)) {
						System.out.println("updation  of group is successfull");
						ArrayList<UserGroup> usergroup=ugo.viewAllGroup(ud.getUserId());
						if(usergroup!=null) {
							session.setAttribute("usergroup", usergroup);
							response.sendRedirect("Dashboard.jsp");
							
							
						}else {
							System.out.println("failed to view all groups of the user");
							
							request.setAttribute("errorMessage", "failed to view all groups of the user");
		                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
						}
						
					}else {
						request.setAttribute("errorMessage", "failed to update User Group Data");
	                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
						
					
					}
				}else {
					System.out.println("Group data is null");
					request.setAttribute("errorMessage", "Group data is null");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
					
				}
				
				
			}
			
			
			
			
		}catch(Exception e) {
			System.out.println(e);
			request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
		}
	}

}
