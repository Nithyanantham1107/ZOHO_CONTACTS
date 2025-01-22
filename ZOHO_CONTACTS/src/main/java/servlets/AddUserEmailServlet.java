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

import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.EmailUser;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class AddUserEmailServlet
 */

public class AddUserEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation co;
	ContactDetails uc;
	HttpSession session;
	SessionOperation so;
	UserOperation userOp;
	UserGroupOperation ugo;
	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddUserEmailServlet() {
		super();

		co = new UserContactOperation();
		so = new SessionOperation();
		userOp = new UserOperation();
		ugo = new UserGroupOperation();
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
		// TODO Auto-geneAddUserEmailServletrated method stub
		try {
			if ((request.getParameter("newemail") != null && !request.getParameter("newemail").isBlank())) {

				uc = new ContactDetails();

				String sessionid = (String) request.getAttribute("sessionid");
				CacheModel cachemodel = CacheData.getCache(sessionid);

				Userdata ud = cachemodel.getUserData();

				EmailUser email = new EmailUser();
				email.setEmailID(ud.getID());
				email.setEmail(request.getParameter("newemail"));
				email.setIsPrimary(false);
				email.setCreatedAt(Instant.now().toEpochMilli());
				email.setModifiedAt(email.getCreatedAt());

				if (userOp.addEmail(email, ud.getID())) {

					logger.logInfo("AddUserEmailServlet", "doPost",
							"Contact added successfully for user ID: " + ud.getID());
					request.getRequestDispatcher("changePassword.jsp").forward(request, response);
				} else {
					logger.logWarning("AddUserEmailServlet", "doPost",
							"Error in adding contact for user ID: " + ud.getID());
					request.setAttribute("errorMessage", "Error in adding contact");
					request.getRequestDispatcher("changePassword.jsp").forward(request, response);
				}
			} else {
				logger.logWarning("AddUserEmailServlet", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("changePassword.jsp").forward(request, response);
			}
		} catch (Exception e) {
			logger.logError("AddUserEmailServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("changePassword.jsp").forward(request, response);
		}
	}

}
