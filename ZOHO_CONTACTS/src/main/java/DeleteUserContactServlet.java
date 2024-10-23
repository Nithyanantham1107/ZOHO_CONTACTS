import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dbmodel.UserContacts;
import dbmodel.UserData;
import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class DeleteUserContactServlet
 */
public class DeleteUserContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UserContactOperation uco;
    UserGroupOperation ugo;
    SessionOperation so;
    UserOperation user_op;
    LoggerSet logger; // LoggerSet instance

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUserContactServlet() {
        super();
        uco = new UserContactOperation();
        ugo = new UserGroupOperation();
        so = new SessionOperation();
        user_op = new UserOperation();
        logger = new LoggerSet(); // Initialize logger
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
        System.out.println("This is called in delete in GET method: " + request.getParameter("user_id"));
    }

    /**
     * Handles POST requests to delete a user contact.
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            
            if (request.getParameter("contact_id") != null && !request.getParameter("contact_id").isBlank()) {
            	 String sessionid=(String) request.getAttribute("sessionid");
                 CacheModel cachemodel=CacheData.getCache(sessionid);
                 
                 
                 UserData ud = cachemodel.getUserData();
               

                int user_id = ud.getUserId();
                int contact_id = Integer.parseInt(request.getParameter("contact_id"));

               
                if (uco.deleteContact(user_id, contact_id)) {
                    ArrayList<UserContacts> userContacts = uco.viewAllUserContacts(ud.getUserId());
                  
                    cachemodel.setUserContact(userContacts);
                    
                    
                    logger.logInfo("DeleteUserContactServlet", "doPost", "Contact deleted successfully: " + contact_id);
                    response.sendRedirect("Dashboard.jsp");
                } else {
                    logger.logWarning("DeleteUserContactServlet", "doPost", "Unable to delete contact: " + contact_id);
                    request.setAttribute("errorMessage", "Unable to delete contact");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                }
            } else {
                logger.logWarning("DeleteUserContactServlet", "doPost", "Contact ID is null or empty");
                request.setAttribute("errorMessage", "Unable to delete contact because specified contact ID is null");
                request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.logError("DeleteUserContactServlet", "doPost", "Exception occurred while deleting contact", e);
            request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
        }
    }
}
