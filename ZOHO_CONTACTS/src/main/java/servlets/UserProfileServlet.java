package servlets;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

	UserOperation userOperation;
	HttpSession session;
	SessionOperation sessionOperation;
	LoggerSet logger; // LoggerSet instance

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserProfileServlet() {
		super();
		userOperation = new UserOperation();
		sessionOperation = new SessionOperation();
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
        	  String sessionID=(String) request.getAttribute("sessionid");
              CacheModel cachemodel=CacheData.getCache(sessionID);
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
                	Userdata userData= new Userdata();
                	LoginCredentials loginCredentials=new LoginCredentials();
                	
                	userData.setModifiedAt(Instant.now().toEpochMilli());
                	loginCredentials.setModifiedAt(userData.getModifiedAt());
					
					
					for(String email : request.getParameterValues("email") ) {
						EmailUser emailUser=new EmailUser();
						emailUser.setEmail(email);
						emailUser.setCreatedAt(userData.getModifiedAt());
						emailUser.setModifiedAt(userData.getModifiedAt());
						
						if(email.equals(request.getParameter("primaryemail"))) {
							
							
							emailUser.setIsPrimary(true);
						}else {
							emailUser.setIsPrimary(false);
						}
						userData.setEmail(emailUser);
					}
					
					loginCredentials.setUserName(request.getParameter("username"));
                	
                	userData.setLoginCredentials(loginCredentials);
                	
                    userData.setID(userSessionData.getID());
                    userData.setName(request.getParameter("Name"));
                    userData.setAddress(request.getParameter("Address"));
//                    ud.setUserName(request.getParameter("username"));
                    userData.setPhoneno(request.getParameter("phone"));
//                    ud.setEmail(request.getParameterValues("email"));
//                    ud.setPrimaryMail(request.getParameter("primaryemail"));
                    userData.setTimezone(request.getParameter("timezone"));
                    
                    
                    
                    if(request.getParameter("password")==null  || request.getParameter("password").isBlank() ||
                       request.getParameter("Newpassword")==null || request.getParameter("Newpassword").isBlank())  {
                    		
//                    	userData.setPassword(null);
//                    	ud.setNewPassword(null);
                    	state=userOperation.userDataUpdate(userData,null);
                    	
                    }else {
                    	userData.setPassword(request.getParameter("password"));
//                    	ud.setNewPassword(request.getParameter("Newpassword"));
                   
                      state=userOperation.userDataUpdate(userData,request.getParameter("Newpassword"));
                    
                    }
                    if (state) {
                    	cachemodel.setUserData(userData);
                       
                        response.sendRedirect("profile.jsp");
                        logger.logInfo("UserProfileServlet", "doPost", "User profile updated successfully for User ID: " + userData.getID());
                    } else {
                        logger.logWarning("UserProfileServlet", "doPost", "Error in updating profile data for User ID: " + userData.getID());
                        request.setAttribute("errorMessage", "Error in updating profile Data");
                        request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                    }
                } else {
                    logger.logWarning("UserProfileServlet", "doPost", "Parameter cannot be empty.");
                    request.setAttribute("errorMessage", "Parameter cannot be empty.");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                }
            } else {
                if (userOperation.deleteUserProfile(userSessionData)) {
                    logger.logInfo("UserProfileServlet", "doPost", "Successfully deleted user profile for User ID: " + userSessionData.getID());
                    
                    SessionOperation sessionOperation=new SessionOperation();
                    sessionOperation.DeleteSessionData(sessionID);
                   
                    response.sendRedirect("index.jsp");
                } else {
                    logger.logWarning("UserProfileServlet", "doPost", "Error in deleting user profile for User ID: " + userSessionData.getID());
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
