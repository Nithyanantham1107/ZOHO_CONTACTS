package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dbmodel.UserData;
import dboperation.SessionOperation;
import dboperation.UserOperation;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class UserProfileServlet
 */
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	UserOperation uo;
	HttpSession session;
	SessionOperation so;
	LoggerSet logger; // LoggerSet instance

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserProfileServlet() {
		super();
		uo = new UserOperation();
		so = new SessionOperation();
		logger = new LoggerSet(); // Initialize logger
	}

	/**
	 * Handles GET requests for user profile.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
     * Handles POST requests for updating user profile information or deleting a user account.
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	  String sessionid=(String) request.getAttribute("sessionid");
              CacheModel cachemodel=CacheData.getCache(sessionid);
              Boolean state=false;
              
              
             Userdata userSessionData = cachemodel.getUserData();

            if (request.getParameter("method").equals("update")) {
                if ((request.getParameter("Name") != null && !request.getParameter("Name").isBlank())
                        && (request.getParameter("username") != null && !request.getParameter("username").isBlank())
                        && (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
                        && (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
                        && (request.getParameterValues("email") != null) && !request.getParameter("email").isBlank()
                        && (request.getParameterValues("timezone") != null) && !request.getParameter("timezone").isBlank()) {

//                    ud = new UserData();
                	Userdata ud= new Userdata();
                	LoginCredentials lc=new LoginCredentials();
					
					
					for(String email : request.getParameterValues("email") ) {
						EmailUser eu=new EmailUser();
						eu.setEmail(email);
						
						if(email.equals(request.getParameter("primaryemail"))) {
							
							
							eu.setIsPrimary(true);
						}else {
							eu.setIsPrimary(false);
						}
						ud.setEmail(eu);
					}
					
					lc.setUserName(request.getParameter("username"));
                	
                	
                    ud.setUserId(userSessionData.getUserId());
                    ud.setName(request.getParameter("Name"));
                    ud.setAddress(request.getParameter("Address"));
//                    ud.setUserName(request.getParameter("username"));
                    ud.setPhoneno(request.getParameter("phone"));
//                    ud.setEmail(request.getParameterValues("email"));
//                    ud.setPrimaryMail(request.getParameter("primaryemail"));
                    ud.setTimezone(request.getParameter("timezone"));
                    
                    
                    
                    if(request.getParameter("password")==null  || request.getParameter("password").isBlank() ||
                       request.getParameter("Newpassword")==null || request.getParameter("Newpassword").isBlank())  {
                    		
                    	ud.setPassword(null);
//                    	ud.setNewPassword(null);
                    	state=uo.userDataUpdate(ud,null);
                    	
                    }else {
                    	ud.setPassword(request.getParameter("password"));
//                    	ud.setNewPassword(request.getParameter("Newpassword"));
                   
                      state=uo.userDataUpdate(ud,request.getParameter("Newpassword"));
                    
                    }
                    if (state) {
                    	cachemodel.setUserData(ud);
                       
                        response.sendRedirect("Dashboard.jsp");
                        logger.logInfo("UserProfileServlet", "doPost", "User profile updated successfully for User ID: " + ud.getUserId());
                    } else {
                        logger.logWarning("UserProfileServlet", "doPost", "Error in updating profile data for User ID: " + ud.getUserId());
                        request.setAttribute("errorMessage", "Error in updating profile Data");
                        request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                    }
                } else {
                    logger.logWarning("UserProfileServlet", "doPost", "Parameter cannot be empty.");
                    request.setAttribute("errorMessage", "Parameter cannot be empty.");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                }
            } else {
                if (uo.deleteUserProfile(userSessionData.getUserId())) {
                    logger.logInfo("UserProfileServlet", "doPost", "Successfully deleted user profile for User ID: " + userSessionData.getUserId());
                    
                    SessionOperation so=new SessionOperation();
                    so.DeleteSessionData(sessionid);
                   
                    response.sendRedirect("index.jsp");
                } else {
                    logger.logWarning("UserProfileServlet", "doPost", "Error in deleting user profile for User ID: " + userSessionData.getUserId());
                    request.setAttribute("errorMessage", "Unable to delete user profile data");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            logger.logError("UserProfileServlet", "doPost", "Exception occurred while processing user profile.", e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
        }
    }
}
