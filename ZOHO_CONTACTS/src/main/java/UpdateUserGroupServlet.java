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
import dboperation.UserGroupOperation;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class UpdateUserGroupServlet
 */
public class UpdateUserGroupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UserGroupOperation ugo;
    UserGroup ug;
    HttpSession session;
    SessionOperation so;
    LoggerSet logger; // LoggerSet instance

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUserGroupServlet() {
        super();
        ugo = new UserGroupOperation();
        ug = new UserGroup();
        so = new SessionOperation();
        logger = new LoggerSet(); // Initialize logger
    }

    /**
     * Handles POST requests for updating user group information.
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ((request.getParameter("groupid") != null && !request.getParameter("groupid").isBlank())
                    && (request.getParameter("groupName") != null && !request.getParameter("groupName").isBlank())) {

            	  String sessionid=(String) request.getAttribute("sessionid");
                  CacheModel cachemodel=CacheData.getCache(sessionid);
                  
                  
                  UserData ud = cachemodel.getUserData();
                ug.setUserid(ud.getUserId());
                ug.setGroupid(Integer.parseInt(request.getParameter("groupid")));
                ug.setGroupName(request.getParameter("groupName"));

                int[] value = ugo.viewUserGroupContact(ug.getGroupid(), ug.getUserid());
                if (value != null) {
                    ug.setcontactid(value);
                } else {
                    logger.logWarning("UpdateUserGroupServlet", "doPost", "Group contact is null for Group ID: " + ug.getGroupid());
                }
             
                request.setAttribute("usergroupupdate", ug);
                request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                logger.logInfo("UpdateUserGroupServlet", "doPost", "User group updated successfully: " + ug.getGroupName());

            } else {
                logger.logWarning("UpdateUserGroupServlet", "doPost", "Group name should not be empty.");
                request.setAttribute("errorMessage", "Group name should not be empty.");
                request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.logError("UpdateUserGroupServlet", "doPost", "Exception occurred while updating user group.", e);
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
        }
    }
}
