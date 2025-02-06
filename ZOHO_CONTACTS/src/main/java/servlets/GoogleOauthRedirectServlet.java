package servlets;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dboperation.OauthOperation;
import dboperation.UserContactOperation;
import dboperation.UserOperation;
import dbpojo.ContactDetails;
import dbpojo.Oauth;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import oauth2helper.Oauth2handler;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class GoogleOauthRedirectServlet
 */

public class GoogleOauthRedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	LoggerSet logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoogleOauthRedirectServlet() {
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

		try {

			String sessionid = (String) request.getAttribute("sessionid");

			System.out.println("here id is" + sessionid + "then " + CacheData.getCache(sessionid));
			CacheModel cachemodel = CacheData.getCache(sessionid);
			Userdata ud = cachemodel.getUserData();
			String code = request.getParameter("code");
			System.out.println("hey hi hello man !!!");
			if (code == null || code.isEmpty()) {
				logger.logError("GoogleOauthRedirectServlet", "doGet", "Authorization code is Empty.", null);
				request.setAttribute("errorMessage", "Authorization code is empty");
				request.getRequestDispatcher("home.jsp").forward(request, response);

			} else {
				Oauth oauth = Oauth2handler.getAccessToken(code, response, ud.getID(), true);

				if (oauth != null) {

					Oauth OauthDB = OauthOperation.isOauthExist(oauth.getEmail(), oauth.getOauthProvider(),
							oauth.getUserID());
					if (OauthDB == null) {
						OauthOperation.addOauth(oauth, ud.getID());
					} else {

//						if (OauthDB.getExpiryTime() < Instant.now().toEpochMilli()) {
//
//							oauth = Oauth2handler.getAccessToken(code, response, ud.getID(), false);
//
//						}
						Oauth oauthUpdate = new Oauth();
						if (oauth.getAccessToken() != null) {
							oauthUpdate.setAccessToken(oauth.getAccessToken());
						}
						if (oauth.getRefreshToken() != null) {
							oauthUpdate.setRefreshToken(oauth.getRefreshToken());
						}

						oauthUpdate.setID(OauthDB.getID());
						oauthUpdate.setModifiedAt(Instant.now().toEpochMilli());
						oauthUpdate.setExpiryTime(oauth.getExpiryTime());
						OauthOperation.updateOauth(oauthUpdate, ud.getID());

					}
					cachemodel.setUserData(UserOperation.getUserData(ud.getID()));

					response.sendRedirect("home.jsp");
				}

			}

		} catch (DBOperationException e) {
			logger.logError("GoogleOauthRedirectServlet", "doGet", "Exception occurred while processing Oauth .", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
