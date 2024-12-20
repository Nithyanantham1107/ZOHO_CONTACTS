package filter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
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
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

public class SessionFilter implements Filter {

	SessionOperation so;
	UserOperation uo;
	UserContactOperation uco;
	UserGroupOperation ugo;
	LoggerSet logger;

	public void init(FilterConfig filterConfig) throws ServletException {

		so = new SessionOperation();
		uo = new UserOperation();
		uco = new UserContactOperation();
		ugo = new UserGroupOperation();
		logger = new LoggerSet();

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// TODO Auto-generated method stub

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requesturi = httpRequest.getRequestURI();

		try {
			if (requesturi.endsWith("/login") || requesturi.endsWith("/signup") || requesturi.endsWith("/")
					|| requesturi.endsWith("/Login.jsp") || requesturi.endsWith("/Signup.jsp")
					|| requesturi.endsWith("/index.jsp")) {

				chain.doFilter(request, response);
				return;
			}

			String sessionid = so.getCustomSessionId(httpRequest.getCookies());
			System.out.println("here data is session " + sessionid);
			if (sessionid == null) {

				httpResponse.sendRedirect("index.jsp");

				return;

			}
			CacheModel cachemodel = so.checkSessionAlive(sessionid);
			if (cachemodel != null) {
                int userid=cachemodel.getUserData().getUserId();
				Userdata ud = uo.getUserData(userid);
	
				ArrayList<ContactDetails> uc = uco.viewAllUserContacts(userid);
				ArrayList<Category> ug = ugo.viewAllGroup(userid);
				cachemodel.setUserContact(uc);
				cachemodel.setUserData(ud);
				cachemodel.setUserGroup(ug);
				so.addSessionCacheData(sessionid, cachemodel);
				

			} else {

				if( ! so.DeleteSessionData(sessionid)) {
					System.out.println("Error in deleting session data");
				}

				

				System.out.println("hello im exiting dude!!");

				Cookie sessionCookie = new Cookie("SESSIONID", null);

				sessionCookie.setMaxAge(0);

				sessionCookie.setPath("/");
				httpResponse.addCookie(sessionCookie);

				httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				httpResponse.setHeader("Pragma", "no-cache");
				httpResponse.setDateHeader("Expires", 0);
				httpResponse.sendRedirect("index.jsp");

			}
			String clientIp = httpRequest.getRemoteAddr();
			String resource = httpRequest.getRequestURI();
			String httpMethod = "GET";
			int responseStatus = HttpServletResponse.SC_OK;
			String userAgent = httpRequest.getHeader("User-Agent");

			logger.logAccessSet(clientIp, resource, httpMethod, responseStatus, userAgent);

			System.out.println("hello im filter");
			
			httpRequest.setAttribute("sessionid", sessionid);
			chain.doFilter(request, response);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
