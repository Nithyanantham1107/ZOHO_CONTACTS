package servlets;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dboperation.SessionOperation;
import dboperation.UserContactOperation;
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

/**
 * Servlet implementation class UpdateUserContactServlet
 */
public class UpdateUserContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserContactOperation contactOperation;

	SessionOperation sessionOperation;

	HttpSession session;
	LoggerSet logger; // LoggerSet instance

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserContactServlet() {
		super();
		contactOperation = new UserContactOperation();

		sessionOperation = new SessionOperation();
		logger = new LoggerSet(); // Initialize logger
	}

	/**
	 * Handles POST requests for updating a user's contact information.
	 *
	 * @param request  the HttpServletRequest object that contains the request data
	 * @param response the HttpServletResponse object used to send a response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input or output error occurs
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String sessionid = (String) request.getAttribute("sessionid");
			CacheModel cachemodel = CacheData.getCache(sessionid);

			ContactDetails contactDetail = new ContactDetails();

			Userdata userData = cachemodel.getUserData();

			if ((request.getParameter("f_name") != null && !request.getParameter("f_name").isBlank())
					&& (request.getParameter("gender") != null && !request.getParameter("gender").isBlank())
					&& (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
					&& (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
					&& (request.getParameter("email") != null && !request.getParameter("email").isBlank())
					&& (request.getParameter("contactid") != null && !request.getParameter("contactid").isBlank())
					&& (request.getParameter("contactemailid") != null
							&& !request.getParameter("contactemailid").isBlank())
					&& (request.getParameter("contactphoneid") != null
							&& !request.getParameter("contactphoneid").isBlank())) {
				ContactPhone contactPhone = new ContactPhone();
				ContactMail contactMail = new ContactMail();

				contactMail.setID(Integer.parseInt(request.getParameter("contactemailid")));
				contactPhone.setID(Integer.parseInt(request.getParameter("contactphoneid")));
				contactPhone.setContactPhone(request.getParameter("phone"));
				contactMail.setContactMailID(request.getParameter("email"));
				contactDetail.setID(Integer.parseInt(request.getParameter("contactid")));
				contactDetail.setFirstName(request.getParameter("f_name"));
				contactDetail.setMiddleName(request.getParameter("m_name"));
				contactDetail.setLastName(request.getParameter("l_name"));
				contactDetail.setAddress(request.getParameter("Address"));
				contactDetail.setModifiedAt(Instant.now().toEpochMilli());
				contactPhone.setModifiedAt(contactDetail.getModifiedAt());
				contactMail.setModifiedAt(contactDetail.getModifiedAt());
				contactPhone.setContactID(contactDetail.getID());
				contactMail.setContactID(contactDetail.getID());
				contactDetail.setGender(request.getParameter("gender"));
				contactDetail.setContactMail(contactMail);
				contactDetail.setContactPhone(contactPhone);
				contactDetail.setUserID(userData.getID());

				logger.logInfo("UpdateUserContactServlet", "doPost",
						"Updating contact for user: " + userData.getID() + ", Contact ID: " + contactDetail.getID());

				if (UserContactOperation.updateSpecificUserContact(contactDetail, userData.getID())) {
//					ArrayList<ContactDetails> userContacts = UserContactOperation.viewAllUserContacts(userData.getID());
//					cachemodel.setUserContact(userContacts);
					response.sendRedirect("home.jsp");
					logger.logInfo("UpdateUserContactServlet", "doPost", "Contact updated successfully.");
				} else {
					logger.logWarning("UpdateUserContactServlet", "doPost",
							"Failed to update contact for Contact ID: " + contactDetail.getID());
					request.setAttribute("errorMessage", "Error while trying to update the contact. Please try again.");
					request.getRequestDispatcher("update_contact.jsp").forward(request, response);
				}
			} else {
				logger.logWarning("UpdateUserContactServlet", "doPost", "Input fields are empty.");
				request.setAttribute("errorMessage", "Input fields should not be empty!");
				request.getRequestDispatcher("update_contact.jsp").forward(request, response);
			}
		} catch (DBOperationException  e) {
			logger.logError("UpdateUserContactServlet", "doPost", "Exception occurred during contact update", e);
			request.setAttribute("errorMessage", "An error occurred while processing your request.");
			request.getRequestDispatcher("update_contact.jsp").forward(request, response);
		}
	}
}
