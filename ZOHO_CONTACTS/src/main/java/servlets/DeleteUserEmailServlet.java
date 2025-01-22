package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import dbpojo.ContactDetails;
import dbpojo.EmailUser;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class DeleteUserEmailServlet
 */

public class DeleteUserEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation userContactOperation;
	UserGroupOperation userGroupOperation;
	SessionOperation ssessionOperation;
	UserOperation userOperation;
	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteUserEmailServlet() {
		super();

		userContactOperation = new UserContactOperation();
		userGroupOperation = new UserGroupOperation();
		ssessionOperation = new SessionOperation();
		userOperation = new UserOperation();
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

			if (request.getParameter("emailID") != null && !request.getParameter("emailID").isBlank()) {
				String sessionid = (String) request.getAttribute("sessionid");
				CacheModel cachemodel = CacheData.getCache(sessionid);

				Userdata userData = cachemodel.getUserData();

				int emailID = Integer.parseInt(request.getParameter("emailID"));

				EmailUser email = new EmailUser();
				email.setID(emailID);
				email.setEmailID(userData.getID());

				if (userOperation.deleteUserEmail(email, userData.getID())) {

					logger.logInfo("DeleteUserEmailServlet", "doPost", "email deleted successfully: " + emailID);
					response.sendRedirect("changePassword.jsp");
				} else {
					logger.logWarning("DeleteUserEmailServlet", "doPost", "Unable to delete email: " + emailID);
					request.setAttribute("errorMessage", "Unable to delete email");
					request.getRequestDispatcher("changePassword.jsp").forward(request, response);
				}
			} else {
				logger.logWarning("DeleteUserEmailServlet", "doPost", "Email ID is null or empty");
				request.setAttribute("errorMessage", "Unable to delete Email because specified Email ID is null");
				request.getRequestDispatcher("changePassword.jsp").forward(request, response);
			}
		} catch (Exception e) {
			logger.logError("DeleteUserEmailServlet", "doPost", "Exception occurred while deleting Email", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("changePassword.jsp").forward(request, response);
		}
	}

}
