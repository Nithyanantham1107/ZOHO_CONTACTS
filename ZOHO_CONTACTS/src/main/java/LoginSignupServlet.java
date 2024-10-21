import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
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
import loggerfiles.LoggerSet;
import validation.UserValidation;

/**
 * Servlet implementation class LoginSignupServlet
 */
public class LoginSignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UserOperation user_op;
    UserGroupOperation ugo;
    UserContactOperation uco;
    UserData ud;
    SessionOperation so;
    UserValidation uservalidate;
    HttpSession session;
    LoggerSet logger; // LoggerSet instance

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginSignupServlet() {
        super();
        user_op = new UserOperation();
        uservalidate = new UserValidation();
        uco = new UserContactOperation();
        ugo = new UserGroupOperation();
        so = new SessionOperation();
        logger = new LoggerSet(); // Initialize logger
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            session = request.getSession(false);
            String sessionid = so.getCustomSessionId(request.getCookies());

            if (session != null) {
                session.invalidate();
            }
            so.DeleteSessionData(sessionid);
            Cookie sessionCookie = new Cookie("SESSIONID", null);
            sessionCookie.setMaxAge(0);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.sendRedirect("index.jsp");
        } catch (Exception e) {
            logger.logError("LoginSignupServlet", "doGet", "Error during logout", e);
        }
    }

    /**
     * Handles POST requests for user login.
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ((request.getParameter("username") != null && !request.getParameter("username").isBlank())
                    && (request.getParameter("password") != null && !request.getParameter("password").isBlank())) {

                if (uservalidate.validateUserPassword(request.getParameter("password"))) {
                    ud = new UserData();
                    ud.setUserName(request.getParameter("username"));
                    ud.setPassword(request.getParameter("password"));

                    ud = user_op.isUser(ud);
                    if (ud != null) {
                        String sessionid = so.generateSessionId(ud.getUserId());
                        Cookie sessionCookie = new Cookie("SESSIONID", sessionid);
                        sessionCookie.setHttpOnly(true);
                        response.addCookie(sessionCookie);
                        session = request.getSession();
                        session.setAttribute("user", ud);
                        ArrayList<UserContacts> uc = uco.viewAllUserContacts(ud.getUserId());
                        ArrayList<UserGroup> ug = ugo.viewAllGroup(ud.getUserId());
                        session.setAttribute("usercontact", uc);
                        session.setAttribute("usergroup", ug);

                        logger.logInfo("LoginSignupServlet", "doPost", "User logged in successfully: " + ud.getUserName());
                        response.sendRedirect("Dashboard.jsp");
                    } else {
                        logger.logWarning("LoginSignupServlet", "doPost", "Invalid username or password for user: " + request.getParameter("username"));
                        request.setAttribute("errorMessage", "Invalid username and password");
                        request.getRequestDispatcher("Login.jsp").forward(request, response);
                    }
                } else {
                    logger.logWarning("LoginSignupServlet", "doPost", "Password validation failed for user: " + request.getParameter("username"));
                    request.setAttribute("errorMessage", "Password is too long or missing cases and numbers");
                    request.getRequestDispatcher("Login.jsp").forward(request, response);
                }
            } else {
                logger.logWarning("LoginSignupServlet", "doPost", "Username or password is empty");
                request.setAttribute("errorMessage", "Username and password should not be empty");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.logError("LoginSignupServlet", "doPost", "Exception occurred during login", e);
            request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
}
