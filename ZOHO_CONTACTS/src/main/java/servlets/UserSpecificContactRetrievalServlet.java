package servlets;
import java.io.IOException;
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
import dbpojo.ContactDetails;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class UserSpecificContactRetrievalServlet
 */
public class UserSpecificContactRetrievalServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UserContactOperation co;
    HttpSession session;
    UserOperation user_op;
    LoggerSet logger; // LoggerSet instance

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserSpecificContactRetrievalServlet() {
        super();
        co = new UserContactOperation();
        user_op = new UserOperation();
        logger = new LoggerSet(); // Initialize logger
    }

    /**
     * Handles GET requests for the servlet.
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * Handles POST requests for retrieving a specific user's contact information.
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	
        	   
        	
        	  String sessionid=(String) request.getAttribute("sessionid");
              CacheModel cachemodel=CacheData.getCache(sessionid);
              
              System.out.println("hello im update contact");
              Userdata ud = cachemodel.getUserData();

            if (request.getParameter("contact_id") != null) {
                int user_id = ud.getUserId();
                int contact_id = Integer.parseInt(request.getParameter("contact_id"));
                ContactDetails uc = co.viewSpecificUserContact(user_id, contact_id);
//                System.out.println("contact mail +" uc.getContactMail().getContactMailID());
//            	System.out.println("contact Phone +" uc.getContactphone().getContactPhone());
                if (uc != null) {
                	
//                	 System.out.println("contact mail +" uc.getContactMail().getContactMailID());
//                 	System.out.println("contact Phone +" uc.getContactphone().getContactPhone());
                    request.setAttribute("user_spec_contact", uc);
                    request.getRequestDispatcher("Update_contact.jsp").forward(request, response);
                    logger.logInfo("UserSpecificContactRetrievalServlet", "doPost", "Retrieved specific contact info for Contact ID: " + contact_id);
                } else {
                    logger.logWarning("UserSpecificContactRetrievalServlet", "doPost", "Failed to retrieve info for Contact ID: " + contact_id);
                    request.setAttribute("errorMessage", "Can't retrieve info of specific user");
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                }
            } else {
                logger.logWarning("UserSpecificContactRetrievalServlet", "doPost", "Contact ID parameter is missing.");
                request.setAttribute("errorMessage", "Contact ID parameter is missing.");
                request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.logError("UserSpecificContactRetrievalServlet", "doPost", "Exception occurred while retrieving specific contact.", e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
}
