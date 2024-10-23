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
 * Servlet implementation class Add_contact_servlet
 */
public class AddContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UserContactOperation co;
    UserContacts uc;
    HttpSession session;
    SessionOperation so;
    UserOperation user_op;
    UserGroupOperation ugo;
    LoggerSet logger; // LoggerSet instance

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddContactServlet() {
        super();
        co = new UserContactOperation();
        so = new SessionOperation();
        user_op = new UserOperation();
        ugo = new UserGroupOperation();
        logger = new LoggerSet(); // Initialize logger
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
        System.out.println("hello I'm called at get method!!");
        response.sendRedirect("Dashboard.jsp");
    }

    /**
     * Handles POST requests to add a new user contact
     *
     * @param request the HttpServletRequest object that contains the request data
     * @param response the HttpServletResponse object used to send a response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ((request.getParameter("f_name") != null && !request.getParameter("f_name").isBlank())
                    && (request.getParameter("gender") != null && !request.getParameter("gender").isBlank())
                    && (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
                    && (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
                    && (request.getParameter("email") != null && !request.getParameter("email").isBlank())) {
                
                uc = new UserContacts();
                
                String sessionid=(String) request.getAttribute("sessionid");
                CacheModel cachemodel=CacheData.getCache(sessionid);
                
                
                UserData ud = cachemodel.getUserData();

                uc.setFname(request.getParameter("f_name"));
                uc.setMname(request.getParameter("m_name"));
                uc.setLname(request.getParameter("l_name"));
                uc.setAddress(request.getParameter("Address"));
                uc.setGender(request.getParameter("gender"));
                uc.setPhoneno(request.getParameter("phone"));
                uc.setUserid(ud.getUserId());
                uc.setEmail(request.getParameter("email"));

                uc.setCreatedAt();
                uc = co.addUserContact(uc);
                if (uc != null) {
                    ArrayList<UserContacts> userContacts = co.viewAllUserContacts(ud.getUserId());
                    cachemodel.setUserContact(userContacts);
       
                    logger.logInfo("AddContactServlet", "doPost", "Contact added successfully for user ID: " + ud.getUserId());
                    request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
                } else {
                    logger.logWarning("AddContactServlet", "doPost", "Error in adding contact for user ID: " + ud.getUserId());
                    request.setAttribute("errorMessage", "Error in adding contact");
                    request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);
                }
            } else {
                logger.logWarning("AddContactServlet", "doPost", "Parameter Data is empty!");
                request.setAttribute("errorMessage", "Parameter Data is empty!!");
                request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.logError("AddContactServlet", "doPost", "Exception occurred", e);
            request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("Add_contacts.jsp").forward(request, response);
        }
    }
}
