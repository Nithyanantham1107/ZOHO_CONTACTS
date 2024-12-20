package servlets;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import dbpojo.ContactDetails;
import dbpojo.LoginCredentials;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import querybuilder.TableSchema.Category;
import querybuilder.TableSchema.Contact_details;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;
import validation.UserValidation;

/**
 * Servlet implementation class LoginSignupServlet
 */
public class LoginSignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserOperation user_op;
	UserGroupOperation ugo;
	UserContactOperation uco;
//	UserData ud;
	Userdata ud;
	LoginCredentials lc;
	SessionOperation so;
	UserValidation uservalidate;
	HttpSession session;
	LoggerSet logger;
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginSignupServlet() {
		super();
		
		user_op = new UserOperation();
		uservalidate = new UserValidation();
		uco = new UserContactOperation();
		ugo = new UserGroupOperation();
		so = new SessionOperation();
		logger = new LoggerSet();
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			session = request.getSession(false);
			String sessionid = so.getCustomSessionId(request.getCookies());

			if (session != null) {
				session.invalidate();
			}
			so.DeleteSessionData(sessionid);
			Cookie sessionCookie = new Cookie("SESSIONID", null);
			sessionCookie.setMaxAge(0);
			sessionCookie.setPath("/");
			response.addCookie(sessionCookie);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.sendRedirect("index.jsp");
		} catch (Exception e) {
			logger.logError("LoginSignupServlet", "doGet", "Error during logout", e);
		}
	}

	/**
	 * Handles POST requests for user login.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ((request.getParameter("username") != null && !request.getParameter("username").isBlank())
					&& (request.getParameter("password") != null && !request.getParameter("password").isBlank())) {

				if (uservalidate.validateUserPassword(request.getParameter("password"))) {
					
//					ud.setUserName(request.getParameter("username"));
//					ud.setPassword(request.getParameter("password"));
//     lc=new LoginCredentials()
					ud = new Userdata();
					ud = user_op.isUser(request.getParameter("username"),request.getParameter("password"));
					if (ud != null) {
						String sessionid = so.generateSessionId(ud.getUserId());
						Cookie sessionCookie = new Cookie("SESSIONID", sessionid);
						sessionCookie.setHttpOnly(true);
						response.addCookie(sessionCookie);
						CacheModel cachemodel=CacheData.getCache(sessionid);
						ArrayList<ContactDetails> uc = uco.viewAllUserContacts(ud.getUserId());
						ArrayList<dbpojo.Category> ug = ugo.viewAllGroup(ud.getUserId());
						cachemodel.setUserData(ud);
						cachemodel.setUserContact(uc);
						cachemodel.setUserGroup(ug);
                        
				
						
						
						logger.logInfo("LoginSignupServlet", "doPost",
								"User logged in successfully: " + ud.getName());
						response.sendRedirect("Dashboard.jsp");
					} else {
						logger.logWarning("LoginSignupServlet", "doPost",
								"Invalid username or password for user: " + request.getParameter("username"));
						request.setAttribute("errorMessage", "Invalid username and password");
						request.getRequestDispatcher("Login.jsp").forward(request, response);
					}
				} else {
					logger.logWarning("LoginSignupServlet", "doPost",
							"Password validation failed for user: " + request.getParameter("username"));
					request.setAttribute("errorMessage", "Password is too long or missing cases and numbers");
					request.getRequestDispatcher("Login.jsp").forward(request, response);
				}
			} else {
				logger.logWarning("LoginSignupServlet", "doPost", "Username or password is empty");
				request.setAttribute("errorMessage", "Username and password should not be empty");
				request.getRequestDispatcher("Login.jsp").forward(request, response);
			}
		} catch (Exception e) {
			logger.logError("LoginSignupServlet", "doPost", "Exception occurred during login", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		}
	}
}
