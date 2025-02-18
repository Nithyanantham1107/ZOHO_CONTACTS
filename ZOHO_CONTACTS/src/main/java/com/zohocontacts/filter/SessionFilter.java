package com.zohocontacts.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.SessionOperation;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;

public class SessionFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requesturi = httpRequest.getRequestURI();

		List<String> excludedPaths = Arrays.asList("/login", "/signup", "/", "/Login.jsp", "/Signup.jsp", "/index.jsp",
				"/sessioncache", "/servercache");

		try {
			if (excludedPaths.stream().anyMatch(requesturi::endsWith)) {

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

				String clientIp = httpRequest.getRemoteAddr();
				String resource = httpRequest.getRequestURI();
				String httpMethod = httpRequest.getMethod();
				int responseStatus = HttpServletResponse.SC_OK;
				String userAgent = httpRequest.getHeader("User-Agent");

				LoggerSet.logAccessSet(clientIp, resource, httpMethod, responseStatus, userAgent);

				System.out.println("hello im filter");
				ThreadLocalStorage.setCurrentUserCache(cachemodel);
				chain.doFilter(request, response);

			} else {

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

		} catch (

		Exception e) {

			System.out.println(e);
			e.printStackTrace();
		} finally {
			ThreadLocalStorage.remove();
		}

	}

}
