package com.zohocontacts.servlets;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserContactOperation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.ContactMail;
import com.zohocontacts.dbpojo.ContactPhone;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;
import com.zohocontacts.utils.TableListComparator;

/**
 * Servlet implementation class UpdateUserContactServlet
 */
public class UpdateUserContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserContactServlet() {
		super();
		
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
			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();
			ContactDetails contactDetail = new ContactDetails();

	

			if ((request.getParameter("f_name") != null && !request.getParameter("f_name").isBlank())
					&& (request.getParameter("gender") != null && !request.getParameter("gender").isBlank())
					&& (request.getParameterValues("phones") != null && request.getParameterValues("phones").length > 0)
					&& (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
					&& (request.getParameterValues("emails") != null && request.getParameterValues("emails").length > 0)
					&& (request.getParameter("contactid") != null && !request.getParameter("contactid").isBlank())

					&& (request.getParameterValues("emailID") != null
							&& request.getParameterValues("emailID").length > 0)
					&& (request.getParameterValues("phoneID") != null
							&& request.getParameterValues("phoneID").length > 0)

			) {

				contactDetail.setID(Integer.parseInt(request.getParameter("contactid")));
				contactDetail.setFirstName(request.getParameter("f_name"));
				contactDetail.setMiddleName(request.getParameter("m_name"));
				contactDetail.setLastName(request.getParameter("l_name"));
				contactDetail.setAddress(request.getParameter("Address"));
				contactDetail.setModifiedAt(Instant.now().toEpochMilli());
				String[] mails = request.getParameterValues("emails");
				String[] mailID = request.getParameterValues("emailID");
				List<ContactMail> addContactMailList = new ArrayList<ContactMail>();

				for (int i = 0; i < mails.length; i++) {
					if (Integer.parseInt(mailID[i]) != -1) {
						int ID = Integer.parseInt(mailID[i]);
						ContactMail tempContactMail = new ContactMail();
						tempContactMail.setID(ID);
						tempContactMail.setContactMailID(mails[i]);
						tempContactMail.setContactID(contactDetail.getID());
						tempContactMail.setModifiedAt(contactDetail.getModifiedAt());
						contactDetail.setContactMail(tempContactMail);

					} else {
						ContactMail tempContactMail = new ContactMail();
						tempContactMail.setContactMailID(mails[i]);
						tempContactMail.setContactID(contactDetail.getID());
						tempContactMail.setModifiedAt(contactDetail.getModifiedAt());
						tempContactMail.setCreatedAt(tempContactMail.getModifiedAt());
						addContactMailList.add(tempContactMail);

					}

				}

				String[] phones = request.getParameterValues("phones");
				String[] phoneID = request.getParameterValues("phoneID");
			List<ContactPhone> addContactPhoneList = new ArrayList<ContactPhone>();
				for (int i = 0; i < phones.length; i++) {

					if (Integer.parseInt(phoneID[i]) != -1) {
						int id = Integer.parseInt(phoneID[i]);
						ContactPhone tempContactPhone = new ContactPhone();
						tempContactPhone.setID(id);
						tempContactPhone.setContactPhone(phones[i]);
						tempContactPhone.setContactID(contactDetail.getID());
						tempContactPhone.setModifiedAt(contactDetail.getModifiedAt());
						contactDetail.setContactPhone(tempContactPhone);

					} else {

						ContactPhone tempContactPhone = new ContactPhone();
						tempContactPhone.setContactPhone(phones[i]);
						tempContactPhone.setContactID(contactDetail.getID());
						tempContactPhone.setModifiedAt(contactDetail.getModifiedAt());
						tempContactPhone.setCreatedAt(tempContactPhone.getModifiedAt());
						addContactPhoneList.add(tempContactPhone);

					}

				}
				ContactDetails contactDB = UserContactOperation.viewSpecificUserContact(userData.getID(),
						contactDetail.getID());

				List<ContactMail> deleteContactMailList = TableListComparator.deleteTableMailListComparator(
						contactDB.getAllContactMail(), contactDetail.getAllContactMail());
				List<ContactPhone> deleteContactPhoneList = TableListComparator.deleteTablePhoneListComparator(
						contactDB.getAllContactphone(), contactDetail.getAllContactphone());

				System.out.println("the contact no to add");
				for (ContactPhone phone : addContactPhoneList) {

					System.out
							.println("here the  Add phone no:" + phone.getContactPhone() + "the id is" + phone.getID());
				}

				System.out.println("the contact no to Update");
				for (ContactPhone phone : contactDetail.getAllContactphone()) {

					System.out.println(
							"here the  Update phone no:" + phone.getContactPhone() + "the id is" + phone.getID());
				}

				System.out.println("the contact no to delete");
				for (ContactPhone phone : deleteContactPhoneList) {

					System.out.println(
							"here the  delete phone no:" + phone.getContactPhone() + "the id is" + phone.getID());
				}

				if (UserContactOperation.deleteAllContactMail(deleteContactMailList, userData.getID())) {
					LoggerSet.logInfo("UpdateUserContactServlet", "doPost", "ContactMails deleted successfully.");

				} else {
					LoggerSet.logInfo("UpdateUserContactServlet", "doPost", "Failed to delete ContactMail.");

				}
				if (UserContactOperation.deleteAllContactPhone(deleteContactPhoneList, userData.getID())) {
					LoggerSet.logInfo("UpdateUserContactServlet", "doPost", "ContactPhone No deleted successfully.");

				} else {
					LoggerSet.logInfo("UpdateUserContactServlet", "doPost", "Failed to delete ContactPhone No.");

				}
				if (UserContactOperation.addContactPhone(addContactPhoneList, userData.getID())) {
					LoggerSet.logInfo("UpdateUserContactServlet", "doPost", "ContactPhoneno added successfully.");

				} else {
					LoggerSet.logInfo("UpdateUserContactServlet", "doPost", "Failed to add ContactPhone No.");

				}

				if (UserContactOperation.addContactMail(addContactMailList, userData.getID())) {
					LoggerSet.logInfo("UpdateUserContactServlet", "doPost", "ContactMails added successfully.");

				} else {
					LoggerSet.logInfo("UpdateUserContactServlet", "doPost", "Failed to added ContactMail.");

				}

				contactDetail.setGender(request.getParameter("gender"));

				contactDetail.setUserID(userData.getID());

				LoggerSet.logInfo("UpdateUserContactServlet", "doPost",
						"Updating contact for user: " + userData.getID() + ", Contact ID: " + contactDetail.getID());

				if (UserContactOperation.updateSpecificUserContact(contactDetail, userData.getID())) {
					response.sendRedirect("home.jsp");
					LoggerSet.logInfo("UpdateUserContactServlet", "doPost", "Contact updated successfully.");
				} else {
					LoggerSet.logWarning("UpdateUserContactServlet", "doPost",
							"Failed to update contact for Contact ID: " + contactDetail.getID());
					request.setAttribute("errorMessage", "Error while trying to update the contact. Please try again.");
					request.getRequestDispatcher("update_contact.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("UpdateUserContactServlet", "doPost", "Input fields are empty.");
				request.setAttribute("errorMessage", "Input fields should not be empty!");
				request.getRequestDispatcher("update_contact.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("UpdateUserContactServlet", "doPost", "Exception occurred during contact update", e);
			request.setAttribute("errorMessage", "An error occurred while processing your request.");
			request.getRequestDispatcher("update_contact.jsp").forward(request, response);
		}
	}
}
