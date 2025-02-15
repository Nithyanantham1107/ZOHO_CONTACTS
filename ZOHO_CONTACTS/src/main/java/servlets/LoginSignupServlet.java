package servlets;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
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
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;
import validation.UserValidation;

/**
 * Servlet implementation class LoginSignupServlet
 */
public class LoginSignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserOperation userOperation;
	UserGroupOperation userGroupOperation;
	UserContactOperation userContactOperation;
//	UserData ud;
	Userdata userData;
	LoginCredentials loginCredential;
	SessionOperation sessionOperation;
	UserValidation userValidate;
	HttpSession session;
	LoggerSet logger;
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginSignupServlet() {
		super();
		
		userOperation = new UserOperation();
		userValidate = new UserValidation();
		userContactOperation = new UserContactOperation();
		userGroupOperation = new UserGroupOperation();
		sessionOperation = new SessionOperation();
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
			String sessionID = SessionOperation.getCustomSessionId(request.getCookies());

			if (session != null) {
				session.invalidate();
			}
			SessionOperation.DeleteSessionData(sessionID);
			Cookie sessionCookie = new Cookie("SESSIONID", null);
			sessionCookie.setMaxAge(0);
			sessionCookie.setPath("/");
			response.addCookie(sessionCookie);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.sendRedirect("Login.jsp");
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

				if (userValidate.validateUserPassword(request.getParameter("password"))) {
					

					userData = new Userdata();
					userData = UserOperation.isUser(request.getParameter("username"),request.getParameter("password"));
					if (userData != null) {
						String sessionid = SessionOperation.generateSessionId(userData.getID());
						Cookie sessionCookie = new Cookie("SESSIONID", sessionid);
						sessionCookie.setHttpOnly(true);
						response.addCookie(sessionCookie);
						CacheModel cachemodel=CacheData.getCache(sessionid);
	cachemodel.setUserData(userData);

				
						
						
						logger.logInfo("LoginSignupServlet", "doPost",
								"User logged in successfully: " + userData.getName());
						response.sendRedirect("home.jsp");
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
		} catch (DBOperationException  e) {
			logger.logError("LoginSignupServlet", "doPost", "Exception occurred during login", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		}
	}
}
