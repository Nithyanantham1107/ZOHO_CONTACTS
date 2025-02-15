package com.zohocontacts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zohocontacts.dboperation.SessionOperation;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.SessionCacheHandler;

/**
 * Servlet implementation class LogoutServlet
 */

public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	HttpSession session;
	
	String servletPath="/sessioncache";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        
   
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			session = request.getSession(false);
			String sessionID = SessionOperation.getCustomSessionId(request.getCookies());

			if (session != null) {
				session.invalidate();
			}
			System.out.println("the size of the cache server data is"+CacheData.getServers());
			System.out.println("here the server befor delete port:"+CacheData.getServerInfo().getServerPort()+"then the size of cache is:"+CacheData.getServers().size());
			SessionOperation.DeleteSessionData(sessionID);

			SessionCacheHandler.sendCacheDeleteRequest(CacheData.getServers(), servletPath, sessionID);
			Cookie sessionCookie = new Cookie("SESSIONID", null);
			sessionCookie.setMaxAge(0);
			sessionCookie.setPath("/");
			response.addCookie(sessionCookie);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.sendRedirect("Login.jsp");
		} catch (Exception e) {
			LoggerSet.logError("LogoutServlet", "doGet", "Error during logout", e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
