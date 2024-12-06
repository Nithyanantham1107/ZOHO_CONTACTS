package servlets;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dbmodel.UserData;
import dbmodel.UserGroup;
import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class DeleteUserGroupServlet
 */
public class DeleteUserGroupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UserGroupOperation ugo;
    HttpSession session;
    SessionOperation so;
    UserOperation user_op;
    UserContactOperation uco;
    UserData ud;
    LoggerSet logger; // LoggerSet instance

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUserGroupServlet() {
        super();
        ugo = new UserGroupOperation();
        so = new SessionOperation();
        user_op = new UserOperation();
        uco = new UserContactOperation();
        logger = new LoggerSet(); // Initialize logger
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * Handles POST requests to delete a user group.
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getParameter("groupid") != null && !request.getParameter("groupid").isBlank()) {
                int groupid = Integer.parseInt(request.getParameter("groupid"));

                // Attempt to delete the user group
                if (ugo.deleteUserGroup(groupid)) {
                	  String sessionid=(String) request.getAttribute("sessionid");
                      CacheModel cachemodel=CacheData.getCache(sessionid);
                      
                      
                      UserData ud = cachemodel.getUserData();
                    ArrayList<UserGroup> userGroups = ugo.viewAllGroup(ud.getUserId());

                    cachemodel.setUserGroup(userGroups);
                    logger.logInfo("DeleteUserGroupServlet", "doPost", "User group deleted successfully: " + groupid);
                    response.sendRedirect("Dashboard.jsp");
                } else {
                    logger.logWarning("DeleteUserGroupServlet", "doPost", "Error in deleting user group: " + groupid);
                    request.setAttribute("errorMessage", "Error in deleting the group");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                }
            } else {
                logger.logWarning("DeleteUserGroupServlet", "doPost", "Group ID is null in delete request");
                request.setAttribute("errorMessage", "Group ID is null in delete post request");
                request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.logError("DeleteUserGroupServlet", "doPost", "Exception occurred while deleting user group", e);
            request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
        }
    }
}
