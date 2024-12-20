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
import dbmodel.UserContacts;
import dbmodel.UserData;
import dbmodel.UserGroup;
import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import dbpojo.Category;
import dbpojo.ContactDetails;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Userdata;
import validation.UserValidation;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class SignupServlet
 */
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserOperation user_op;
	Userdata ud;
	UserGroupOperation ugo;
	UserContactOperation uco;
	UserValidation uservalidate;
	HttpSession session;
	SessionOperation so;
	
	
	LoggerSet logger; // LoggerSet instance

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignupServlet() {
		super();
		user_op = new UserOperation();
		uservalidate = new UserValidation();
		uco = new UserContactOperation();
		ugo = new UserGroupOperation();
		so = new SessionOperation();
		logger = new LoggerSet();
		
		
		// Initialize logger
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * Handles POST requests for user sign-up.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ((request.getParameter("password") != null && !request.getParameter("password").isBlank())
					&& (request.getParameter("Name") != null && !request.getParameter("Name").isBlank())
					&& (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
					&& (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
					&& (request.getParameter("email") != null && !request.getParameter("email").isBlank())) {

				if (uservalidate.validateUserPassword(request.getParameter("password"))) {
					ud = new Userdata();
					LoginCredentials lc=new LoginCredentials();
					EmailUser eu=new EmailUser();
					lc.setUserName(request.getParameter("username"));
					eu.setEmail(request.getParameter("email"));
					eu.setIsPrimary(true);
					ud.setName(request.getParameter("Name"));
					ud.setAddress(request.getParameter("Address"));
//					ud.setUserName(request.getParameter("username"));
					ud.setPhoneno(request.getParameter("phone"));
//					ud.setPrimaryMail(request.getParameter("email"));
					ud.setPassword(request.getParameter("password"));
					ud.setCurrentEmail(request.getParameter("email"));
					ud.setTimezone(request.getParameter("timezone"));
					ud = user_op.createUser(ud);

					if (ud != null) {
						so = new SessionOperation();
						String sessionid = so.generateSessionId(ud.getUserId());
						Cookie sessionCookie = new Cookie("SESSIONID", sessionid);
						sessionCookie.setHttpOnly(true);
						response.addCookie(sessionCookie);
                        CacheModel cachemodel=CacheData.getCache(sessionid);
						ArrayList<ContactDetails> uc = uco.viewAllUserContacts(ud.getUserId());
						ArrayList<Category> ug = ugo.viewAllGroup(ud.getUserId());
						cachemodel.setUserData(ud);
						cachemodel.setUserContact(uc);
						cachemodel.setUserGroup(ug);
						

						response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
						response.setHeader("Pragma", "no-cache");
						response.setDateHeader("Expires", 0);
						response.sendRedirect("Dashboard.jsp");

						logger.logInfo("SignupServlet", "doPost", "User signed up successfully: " + lc.getUserName());
					} else {
						logger.logWarning("SignupServlet", "doPost", "User creation failed.");
						request.setAttribute("errorMessage", "An error occurred while creating user");
						request.getRequestDispatcher("Signup.jsp").forward(request, response);
					}
				} else {
					logger.logWarning("SignupServlet", "doPost",
							"Password validation failed for user: " + request.getParameter("username"));
					request.setAttribute("errorMessage",
							"Password should contain at least one lower case, one upper case, and numbers.");
					request.getRequestDispatcher("Signup.jsp").forward(request, response);
				}
			} else {
				logger.logWarning("SignupServlet", "doPost", "Required parameters are missing.");
				request.setAttribute("errorMessage", "Parameters should not be empty!");
				request.getRequestDispatcher("Signup.jsp").forward(request, response);
			}
		} catch (Exception e) {
			logger.logError("SignupServlet", "doPost", "Exception occurred during signup", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Signup.jsp").forward(request, response);
		}
	}
}
