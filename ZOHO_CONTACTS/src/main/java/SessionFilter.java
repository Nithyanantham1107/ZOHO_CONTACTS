
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

public class SessionFilter implements Filter {

	SessionOperation so;
	UserOperation uo;
	UserContactOperation uco;
	UserGroupOperation ugo;

	public void init(FilterConfig filterConfig) throws ServletException {

		so = new SessionOperation();
		uo = new UserOperation();
		uco = new UserContactOperation();
		ugo = new UserGroupOperation();

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
			HttpSession session = httpRequest.getSession(false);
			UserData ud = (UserData) session.getAttribute("user");
			System.out.println((ud==null) +"herre see data");
			String sessionid = so.getCustomSessionId(httpRequest.getCookies());
			System.out.println("here data is session "+sessionid);
			if(sessionid==null) {
				System.out.println("heshdjshvsdjs dude!!");
				httpResponse.sendRedirect("index.jsp");
				System.out.println("firs..");
				return;
				
			}
			int userid = so.checkSessionAlive(sessionid);
			if (userid != 0) {

				
				if (ud == null) {

					session = httpRequest.getSession();
					ud = uo.getUserData(userid);
					session.setAttribute("user", ud);
					ArrayList<UserContacts> uc = uco.viewAllUserContacts(userid);
					ArrayList<UserGroup> ug = ugo.viewAllGroup(userid);
					session.setAttribute("usercontact", uc);
					session.setAttribute("usergroup", ug);
				}

			} else {

				so.DeleteSessionData(sessionid);

				if (session != null) {

					session.invalidate();
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
			
			
			System.out.println("hello im filter");
			chain.doFilter(request, response);
		} catch (Exception e) {
			System.out.println(e);
		}

		

	}

}
