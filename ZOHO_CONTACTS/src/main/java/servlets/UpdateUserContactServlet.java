package servlets;
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
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class UpdateUserContactServlet
 */
public class UpdateUserContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UserContactOperation co;
 
    SessionOperation so;
 
    HttpSession session;
    LoggerSet logger; // LoggerSet instance

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUserContactServlet() {
        super();
        co = new UserContactOperation();
     
        so = new SessionOperation();
        logger = new LoggerSet(); // Initialize logger
    }

    /**
     * Handles POST requests for updating a user's contact information.
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
              
              ContactDetails uc=new ContactDetails();
              
              Userdata ud = cachemodel.getUserData();

            if ((request.getParameter("f_name") != null && !request.getParameter("f_name").isBlank())
                    && (request.getParameter("gender") != null && !request.getParameter("gender").isBlank())
                    && (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
                    && (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
                    && (request.getParameter("email") != null && !request.getParameter("email").isBlank())) {
            	ContactPhone cp = new ContactPhone();
				ContactMail cm = new ContactMail();
				
				cp.setContactPhone(request.getParameter("phone"));
				cm.setContactMailID(request.getParameter("email"));
                uc.setContactID(Integer.parseInt(request.getParameter("contactid")));
                uc.setFirstName(request.getParameter("f_name"));
                uc.setMiddleName(request.getParameter("m_name"));
                uc.setLastName(request.getParameter("l_name"));
                uc.setAddress(request.getParameter("Address"));
                
//                System.out.println("gender of contact "+request.getParameter("gender"));
                uc.setGender(request.getParameter("gender"));
               uc.setContactMail(cm);
               uc.setContactPhone(cp);
                
//                uc.setPhoneno(request.getParameter("phone"));
                uc.setUserID(ud.getUserId());
//                uc.setEmail(request.getParameter("email"));

                logger.logInfo("UpdateUserContactServlet", "doPost", "Updating contact for user: " + ud.getUserId() + ", Contact ID: " + uc.getContactID());

                if (co.updateSpecificUserContact(uc)) {
                    ArrayList<ContactDetails> userContacts = co.viewAllUserContacts(ud.getUserId());
                    cachemodel.setUserContact(userContacts);
                    response.sendRedirect("Dashboard.jsp");
                    logger.logInfo("UpdateUserContactServlet", "doPost", "Contact updated successfully.");
                } else {
                    logger.logWarning("UpdateUserContactServlet", "doPost", "Failed to update contact for Contact ID: " + uc.getContactID());
                    request.setAttribute("errorMessage", "Error while trying to update the contact. Please try again.");
                    request.getRequestDispatcher("update_contact.jsp").forward(request, response);
                }
            } else {
                logger.logWarning("UpdateUserContactServlet", "doPost", "Input fields are empty.");
                request.setAttribute("errorMessage", "Input fields should not be empty!");
                request.getRequestDispatcher("update_contact.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.logError("UpdateUserContactServlet", "doPost", "Exception occurred during contact update", e);
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            request.getRequestDispatcher("update_contact.jsp").forward(request, response);
        }
    }
}
