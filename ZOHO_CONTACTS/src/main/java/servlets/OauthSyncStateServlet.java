package servlets;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dboperation.OauthOperation;
import dboperation.UserOperation;
import dbpojo.ContactDetails;
import dbpojo.EmailUser;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class OauthSyncStateServlet
 */

public class OauthSyncStateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OauthSyncStateServlet() {
		super();
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
			if ((request.getParameter("operation") != null && !request.getParameter("operation").isBlank()) &&

					(request.getParameter("ID") != null && !request.getParameter("ID").isBlank())) {

				String operation = request.getParameter("operation");
				int oauthID = Integer.parseInt(request.getParameter("ID"));

				String sessionid = (String) request.getAttribute("sessionid");
				CacheModel cachemodel = CacheData.getCache(sessionid);

				Userdata ud = cachemodel.getUserData();

				if (operation.equals("syncoff")) {
					OauthOperation.setOauthSynncOff(oauthID, ud);

				} else if (operation.equals("syncon")) {
					OauthOperation.setOauthSynncOn(oauthID, ud);
				} else if (operation.equals("deletesync")) {
					OauthOperation.deleteOauth(oauthID, ud);
				}

				logger.logWarning("OauthSyncStateServlet", "doPost",
						"successfully Oauth data modified for userID" + ud.getID());

				response.sendRedirect("linkedaccounts.jsp");

			} else {
				logger.logWarning("OauthSyncStateServlet", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			logger.logError("OauthSyncStateServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("linkedaccounts.jsp").forward(request, response);
		}
	}

}
