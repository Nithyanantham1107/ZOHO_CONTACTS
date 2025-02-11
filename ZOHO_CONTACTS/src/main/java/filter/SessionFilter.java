package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dboperation.SessionOperation;
import dboperation.UserOperation;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheModel;

public class SessionFilter implements Filter {

	SessionOperation so;
	UserOperation uo;

	LoggerSet logger;

	public void init(FilterConfig filterConfig) throws ServletException {

		so = new SessionOperation();
		uo = new UserOperation();

		logger = new LoggerSet();

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requesturi = httpRequest.getRequestURI();
//|| requesturi.endsWith("/oauthredirect")
		try {
			if (requesturi.endsWith("/login") || requesturi.endsWith("/signup") || requesturi.endsWith("/")
					|| requesturi.endsWith("/Login.jsp") || requesturi.endsWith("/Signup.jsp")
					|| requesturi.endsWith("/index.jsp") || requesturi.endsWith("/oauthdirector")) {

				chain.doFilter(request, response);
				return;
			}

			String sessionid = SessionOperation.getCustomSessionId(httpRequest.getCookies());

			System.out.println("here data is session " + sessionid);
			if (sessionid == null) {

				httpResponse.sendRedirect("Login.jsp");

				return;

			}

			CacheModel cachemodel = SessionOperation.checkSessionAlive(sessionid);
			if (cachemodel != null) {

				long userid = cachemodel.getUserData().getID();
				Userdata ud = UserOperation.getUserData(userid);

				cachemodel.setUserData(ud);

				SessionOperation.addSessionCacheData(sessionid, cachemodel);
				String clientIp = httpRequest.getRemoteAddr();
				String resource = httpRequest.getRequestURI();
				String httpMethod = httpRequest.getMethod();
				int responseStatus = HttpServletResponse.SC_OK;
				String userAgent = httpRequest.getHeader("User-Agent");

				logger.logAccessSet(clientIp, resource, httpMethod, responseStatus, userAgent);

				System.out.println("hello im filter");

				httpRequest.setAttribute("sessionid", sessionid);
				chain.doFilter(request, response);

			} else {

//				if( ! so.DeleteSessionData(sessionid)) {
//					System.out.println("Error in deleting session data");
//				}

				System.out.println("hello im exiting dude!!");

				Cookie sessionCookie = new Cookie("SESSIONID", null);

				sessionCookie.setMaxAge(0);
				sessionCookie.setPath("/");
				httpResponse.addCookie(sessionCookie);
				httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				httpResponse.setHeader("Pragma", "no-cache");
				httpResponse.setDateHeader("Expires", 0);
				httpResponse.sendRedirect("Login.jsp");

			}

		} catch (Exception e) {

			System.out.println(e);
			e.printStackTrace();
		}

	}

}
