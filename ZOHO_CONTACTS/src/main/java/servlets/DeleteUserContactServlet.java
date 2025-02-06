package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dboperation.UserGroupOperation;
import dboperation.UserOperation;
import dbpojo.ContactDetails;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class DeleteUserContactServlet
 */
public class DeleteUserContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation userContactOperation;
	UserGroupOperation userGroupOperation;
	SessionOperation ssessionOperation;
	UserOperation userOperation;
	LoggerSet logger; // LoggerSet instance

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteUserContactServlet() {
		super();
		userContactOperation = new UserContactOperation();
		userGroupOperation = new UserGroupOperation();
		ssessionOperation = new SessionOperation();
		userOperation = new UserOperation();
		logger = new LoggerSet(); // Initialize logger
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("This is called in delete in GET method: " + request.getParameter("user_id"));
	}

	/**
	 * Handles POST requests to delete a user contact.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			if (request.getParameter("contact_id") != null && !request.getParameter("contact_id").isBlank()) {
				String sessionid = (String) request.getAttribute("sessionid");
				CacheModel cachemodel = CacheData.getCache(sessionid);

				Userdata userData = cachemodel.getUserData();

//                int user_id = ud.getUserId();
				int contactID = Integer.parseInt(request.getParameter("contact_id"));

				// check
//                
//                if(cachemodel.getUserContact(contactID)==null) {
//                	
//                	
//                	System.out.println("here contact are null so see it!!");
//                }
				ContactDetails contact = new ContactDetails();
				contact.setID(contactID);
				if (UserContactOperation.deleteContact(contact, userData.getID())) {
//                    ArrayList<ContactDetails> userContacts = UserContactOperation.viewAllUserContacts(userData.getID());

//                    cachemodel.setUserContact(userContacts);

					logger.logInfo("DeleteUserContactServlet", "doPost", "Contact deleted successfully: " + contactID);
					response.sendRedirect("home.jsp");
				} else {
					logger.logWarning("DeleteUserContactServlet", "doPost", "Unable to delete contact: " + contactID);
					request.setAttribute("errorMessage", "Unable to delete contact");
					request.getRequestDispatcher("home.jsp").forward(request, response);
				}
			} else {
				logger.logWarning("DeleteUserContactServlet", "doPost", "Contact ID is null or empty");
				request.setAttribute("errorMessage", "Unable to delete contact because specified contact ID is null");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			logger.logError("DeleteUserContactServlet", "doPost", "Exception occurred while deleting contact", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
	}
}
