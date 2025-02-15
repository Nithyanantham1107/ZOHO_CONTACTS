package servlets;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.User;

import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserOperation;
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class ChangeUserPasswordServlet
 */

public class ChangeUserPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	UserContactOperation contactOperation;
	UserOperation userOp;
	SessionOperation sessionOperation;

	HttpSession session;
	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangeUserPasswordServlet() {
		super();

		contactOperation = new UserContactOperation();
		userOp = new UserOperation();
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
			String sessionid = (String) request.getAttribute("sessionid");
			CacheModel cachemodel = CacheData.getCache(sessionid);

			

			Userdata userData = cachemodel.getUserData();

			if (((request.getParameter("password") != null && !request.getParameter("password").isBlank())
					&& (request.getParameter("Newpassword") != null
							&& !request.getParameter("Newpassword").isBlank()))) {

				Userdata oldUser = userData;

				String oldPassword = request.getParameter("password");
				String newPassword = request.getParameter("Newpassword");

				if (UserOperation.userPasswordChange(oldUser, oldPassword, newPassword, userData.getID())) {

					response.sendRedirect("changePassword.jsp");
					logger.logInfo(" ChangeUserPasswordServlet", "doPost", "User Password Update successfully.");
				} else {
					logger.logWarning(" ChangeUserPasswordServlet", "doPost",
							"Failed to change Password for User ID: " + userData.getID());
					request.setAttribute("errorMessage",
							"Error while trying to update the User Password. Please try again.");
					request.getRequestDispatcher("changePassword.jsp").forward(request, response);
				}
			} else {
				logger.logWarning(" ChangeUserPasswordServlet", "doPost", "Input fields are empty.");
				request.setAttribute("errorMessage", "Input fields should not be empty!");
				request.getRequestDispatcher("changePassword.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			logger.logError(" ChangeUserPasswordServlet", "doPost", "Exception occurred during Password Update", e);
			request.setAttribute("errorMessage", "An error occurred while processing your request.");
			request.getRequestDispatcher("changePassword.jsp").forward(request, response);
		}
	}

}
