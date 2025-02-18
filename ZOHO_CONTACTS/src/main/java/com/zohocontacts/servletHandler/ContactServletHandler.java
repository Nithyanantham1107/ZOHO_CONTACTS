package com.zohocontacts.servletHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zohocontacts.dboperation.UserContactOperation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.ContactMail;
import com.zohocontacts.dbpojo.ContactPhone;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;
import com.zohocontacts.utils.TableListComparator;

public class ContactServletHandler {

	public static void AddContactRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			if ((request.getParameter("f_name") != null && !request.getParameter("f_name").isBlank())

					&& (request.getParameter("gender") != null && !request.getParameter("gender").isBlank())
					&& (request.getParameterValues("phones") != null && request.getParameterValues("phones").length > 0)
					&& (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
					&& (request.getParameterValues("emails") != null && request.getParameterValues("emails").length > 0)

					&& (request.getParameterValues("phonelabels") != null
							&& request.getParameterValues("phonelabels").length > 0)
					&& (request.getParameterValues("emaillabels") != null
							&& request.getParameterValues("emaillabels").length > 0)) {

				ContactDetails contact = new ContactDetails();

				String[] mails = request.getParameterValues("emails");
				String[] phones = request.getParameterValues("phones");
				String[] phonelabels = request.getParameterValues("phonelabels");
				String[] emaillabels = request.getParameterValues("emaillabels");
				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				contact.setCreatedAt(Instant.now().toEpochMilli());
				contact.setModifiedAt(contact.getCreatedAt());

				contact.setFirstName(request.getParameter("f_name"));
				contact.setMiddleName(request.getParameter("m_name"));
				contact.setLastName(request.getParameter("l_name"));
				contact.setAddress(request.getParameter("Address"));
				contact.setGender(request.getParameter("gender"));
				contact.setUserID(userData.getID());

				for (int i = 0; i < mails.length; i++) {

					ContactMail tempMail = new ContactMail();
					tempMail.setContactMailID(mails[i]);
					tempMail.setLabelName(emaillabels[i]);
					tempMail.setCreatedAt(contact.getCreatedAt());
					tempMail.setModifiedAt(contact.getCreatedAt());
					contact.setContactMail(tempMail);

				}

				for (int i = 0; i < phones.length; i++) {

					ContactPhone tempPhone = new ContactPhone();
					tempPhone.setContactPhone(phones[i]);
					tempPhone.setLabelName(phonelabels[i]);
					tempPhone.setCreatedAt(contact.getCreatedAt());
					tempPhone.setModifiedAt(contact.getCreatedAt());
					contact.setContactPhone(tempPhone);
				}

				contact.setCreatedAt(Instant.now().toEpochMilli());
				;
				contact = UserContactOperation.addUserContact(contact);
				if (contact != null) {

					LoggerSet.logInfo("AddContactServlet", "doPost",
							"Contact added successfully for user ID: " + userData.getID());

					response.sendRedirect("home.jsp");
				} else {
					LoggerSet.logWarning("AddContactServlet", "doPost",
							"Error in adding contact for user ID: " + userData.getID());
					request.setAttribute("errorMessage", "Error in adding contact");
					request.getRequestDispatcher("addcontacts.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("AddContactServlet", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("addcontacts.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("AddContactServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("addcontacts.jsp").forward(request, response);
		}

	}

	public static void ContactViewRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			System.out.println("hello im update contact");
			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();
			if (request.getParameter("contact_id") != null) {
				long userID = userData.getID();
				long contactID = Long.parseLong(request.getParameter("contact_id"));
				ContactDetails contact = UserContactOperation.viewSpecificUserContact(userID, contactID);
				if (contact != null) {

					request.setAttribute("user_spec_contact", contact);
					request.getRequestDispatcher("contactview.jsp").forward(request, response);
					LoggerSet.logInfo("ContactViewRedirectServlet", "doPost",
							"Retrieved specific contact info for Contact ID: " + contactID);
				} else {
					LoggerSet.logWarning("ContactViewRedirectServlet", "doPost",
							"Failed to retrieve info for Contact ID: " + contactID);
					request.setAttribute("errorMessage", "Can't retrieve info of specific user");
					request.getRequestDispatcher("home.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("ContactViewRedirectServlet", "doPost", "Contact ID parameter is missing.");
				request.setAttribute("errorMessage", "Contact ID parameter is missing.");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("ContactViewRedirectServlet", "doPost",
					"Exception occurred while retrieving specific contact.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}

	}

	public static void DeleteContactRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			if (request.getParameter("contact_id") != null && !request.getParameter("contact_id").isBlank()) {

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				int contactID = Integer.parseInt(request.getParameter("contact_id"));

				ContactDetails contact = new ContactDetails();
				contact.setID(contactID);
				if (UserContactOperation.deleteContact(contact, userData.getID())) {

					LoggerSet.logInfo("DeleteUserContactServlet", "doPost",
							"Contact deleted successfully: " + contactID);
					response.sendRedirect("home.jsp");
				} else {
					LoggerSet.logWarning("DeleteUserContactServlet", "doPost",
							"Unable to delete contact: " + contactID);
					request.setAttribute("errorMessage", "Unable to delete contact");
					request.getRequestDispatcher("home.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("DeleteUserContactServlet", "doPost", "Contact ID is null or empty");
				request.setAttribute("errorMessage", "Unable to delete contact because specified contact ID is null");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("DeleteUserContactServlet", "doPost", "Exception occurred while deleting contact", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
	}

	public static void mergeContactRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();

			if (request.getParameterValues("contact_ids") != null
					&& request.getParameterValues("contact_ids").length > 0) {
				long[] contactID = new long[request.getParameterValues("contact_ids").length];
				for (int i = 0; i < request.getParameterValues("contact_ids").length; i++) {
					contactID[i] = Long.parseLong(request.getParameterValues("contact_ids")[i]);

				}
				if (UserContactOperation.mergeUserContacts(contactID, userData.getID())) {

					response.sendRedirect("home.jsp");
					LoggerSet.logInfo("mergeContactRequestHandler", "doPost", "Contact Successfully merged: ");
				} else {
					LoggerSet.logWarning("mergeContactRequestHandler", "doPost", "Failed to merge contact ");
					request.setAttribute("errorMessage", "Cant merge the specified contact");
					request.getRequestDispatcher("home.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("mergeContactRequestHandler", "doPost", "Contact ID parameter is missing.");
				request.setAttribute("errorMessage", "Contact ID parameter is missing.");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("UserSpecificContactRetrievalServlet", "doPost",
					"Exception occurred while retrieving specific contact.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
	}

	public static void specificContactRetrievalRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();

			if (request.getParameter("contact_id") != null) {
				long userID = userData.getID();
				long contactID = Long.parseLong(request.getParameter("contact_id"));
				ContactDetails userContact = UserContactOperation.viewSpecificUserContact(userID, contactID);
				if (userContact != null) {

					request.setAttribute("user_spec_contact", userContact);
					request.getRequestDispatcher("updatecontact.jsp").forward(request, response);
					LoggerSet.logInfo("UserSpecificContactRetrievalServlet", "doPost",
							"Retrieved specific contact info for Contact ID: " + contactID);
				} else {
					LoggerSet.logWarning("UserSpecificContactRetrievalServlet", "doPost",
							"Failed to retrieve info for Contact ID: " + contactID);
					request.setAttribute("errorMessage", "Can't retrieve info of specific user");
					request.getRequestDispatcher("home.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("UserSpecificContactRetrievalServlet", "doPost",
						"Contact ID parameter is missing.");
				request.setAttribute("errorMessage", "Contact ID parameter is missing.");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("UserSpecificContactRetrievalServlet", "doPost",
					"Exception occurred while retrieving specific contact.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
	}

	public static void contactUpdateRequestHandler(HttpServletRequest request, HttpServletResponse response)
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
					&& (request.getParameterValues("phonelabels") != null
							&& request.getParameterValues("phonelabels").length > 0)
					&& (request.getParameterValues("emaillabels") != null
							&& request.getParameterValues("emaillabels").length > 0)) {

				contactDetail.setID(Integer.parseInt(request.getParameter("contactid")));
				contactDetail.setFirstName(request.getParameter("f_name"));
				contactDetail.setMiddleName(request.getParameter("m_name"));
				contactDetail.setLastName(request.getParameter("l_name"));
				contactDetail.setAddress(request.getParameter("Address"));
				contactDetail.setModifiedAt(Instant.now().toEpochMilli());

				String[] phonelabels = request.getParameterValues("phonelabels");
				String[] emaillabels = request.getParameterValues("emaillabels");

				String[] mails = request.getParameterValues("emails");
				String[] mailID = request.getParameterValues("emailID");
				List<ContactMail> addContactMailList = new ArrayList<ContactMail>();

				for (int i = 0; i < mails.length; i++) {
					if (Integer.parseInt(mailID[i]) != -1) {
						int ID = Integer.parseInt(mailID[i]);
						ContactMail tempContactMail = new ContactMail();
						tempContactMail.setID(ID);
						tempContactMail.setContactMailID(mails[i]);
						tempContactMail.setLabelName(emaillabels[i]);
						tempContactMail.setContactID(contactDetail.getID());
						tempContactMail.setModifiedAt(contactDetail.getModifiedAt());
						contactDetail.setContactMail(tempContactMail);

					} else {
						ContactMail tempContactMail = new ContactMail();
						tempContactMail.setContactMailID(mails[i]);
						tempContactMail.setContactID(contactDetail.getID());
						tempContactMail.setLabelName(emaillabels[i]);
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
						tempContactPhone.setLabelName(phonelabels[i]);
						tempContactPhone.setContactPhone(phones[i]);
						tempContactPhone.setContactID(contactDetail.getID());
						tempContactPhone.setModifiedAt(contactDetail.getModifiedAt());
						contactDetail.setContactPhone(tempContactPhone);

					} else {

						ContactPhone tempContactPhone = new ContactPhone();
						tempContactPhone.setContactPhone(phones[i]);
						tempContactPhone.setLabelName(phonelabels[i]);
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
					request.getRequestDispatcher("updatecontact.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("UpdateUserContactServlet", "doPost", "Input fields are empty.");
				request.setAttribute("errorMessage", "Input fields should not be empty!");
				request.getRequestDispatcher("updatecontact.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("UpdateUserContactServlet", "doPost", "Exception occurred during contact update", e);
			request.setAttribute("errorMessage", "An error occurred while processing your request.");
			request.getRequestDispatcher("updatecontact.jsp").forward(request, response);
		}

	}

}
