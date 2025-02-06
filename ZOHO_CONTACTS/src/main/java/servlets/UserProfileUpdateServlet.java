package servlets;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dboperation.SessionOperation;
import dboperation.UserOperation;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class UserProfileUpdateServlet
 */

public class UserProfileUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserOperation userOperation;
	HttpSession session;
	SessionOperation sessionOperation;
	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserProfileUpdateServlet() {
		super();

		userOperation = new UserOperation();
		sessionOperation = new SessionOperation();
		logger = new LoggerSet();
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
		try {
			String sessionID = (String) request.getAttribute("sessionid");
			CacheModel cachemodel = CacheData.getCache(sessionID);
			Boolean state = false;

			Userdata userSessionData = cachemodel.getUserData();

			if ((request.getParameter("name") != null && !request.getParameter("name").isBlank())
					&& (request.getParameter("username") != null && !request.getParameter("username").isBlank())
					&& (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
					&& (request.getParameter("address") != null && !request.getParameter("address").isBlank())
					&& (request.getParameter("primaryemail") != null) && !request.getParameter("primaryemail").isBlank()
					&& (request.getParameter("timezone") != null) && !request.getParameter("timezone").isBlank()
					&& (request.getParameter("emailID") != null) && !request.getParameter("emailID").isBlank()
					&& (request.getParameter("logID") != null) && !request.getParameter("logID").isBlank()
					) {
int userId=userSessionData.getID();
//                  ud = new UserData();
				Userdata userData = new Userdata();
				LoginCredentials loginCredentials = new LoginCredentials();
				EmailUser emailUser = new EmailUser();
				emailUser.setID(Integer.parseInt(request.getParameter("emailID")));	

				emailUser.setEmail(request.getParameter("primaryemail"));
				emailUser.setModifiedAt(userData.getModifiedAt());
				emailUser.setIsPrimary(true);
//				userData.setEmail(emailUser);
				loginCredentials.setID(Integer.parseInt(request.getParameter("logID")));
				loginCredentials.setUserID(userSessionData.getID());
				loginCredentials.setUserName(request.getParameter("username"));
				loginCredentials.setModifiedAt(userData.getModifiedAt());
				userData.setLoginCredentials(loginCredentials);
				userData.setModifiedAt(Instant.now().toEpochMilli());
				userData.setID(userSessionData.getID());
				userData.setName(request.getParameter("name"));
				userData.setAddress(request.getParameter("address"));
				userData.setPhoneno(request.getParameter("phone"));
				userData.setTimezone(request.getParameter("timezone"));

				state = UserOperation.userprofileUpdate(userData,emailUser);

				if (state) {
					cachemodel.setUserData(UserOperation.getUserData(userId));

					response.sendRedirect("profile.jsp");
					logger.logInfo("UserProfileServlet", "doPost",
							"User profile updated successfully for User ID: " + userData.getID());
				} else {
					logger.logWarning("UserProfileServlet", "doPost",
							"Error in updating profile data for User ID: " + userData.getID());
					request.setAttribute("errorMessage", "Error in updating profile Data");
					request.getRequestDispatcher("profile.jsp").forward(request, response);
				}
			} else {
				logger.logWarning("UserProfileServlet", "doPost", "Parameter cannot be empty.");
				request.setAttribute("errorMessage", "Parameter cannot be empty.");
				request.getRequestDispatcher("profile.jsp").forward(request, response);
			}

		} catch (

				DBOperationException  e) {
			logger.logError("UserProfileServlet", "doPost", "Exception occurred while processing user profile.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("profile.jsp").forward(request, response);
		}
	}

}
