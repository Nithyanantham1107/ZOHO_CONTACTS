package dboperation;

import java.sql.SQLException;
import java.util.ArrayList;

import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.Table;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

/**
 * This class provides operations for managing user contacts, including adding,
 * viewing, updating, and deleting contacts.
 */
public class UserContactOperation {

	private static LoggerSet logger = new LoggerSet();



	public static ContactDetails addUserContact(ContactDetails contactDetails) throws DBOperationException {
int[] result = { -1, -1 };
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {

			qg.openConnection();

			result = qg.insert(contactDetails).execute(contactDetails.getUserID());

			if (result[0] == -1) {

				qg.rollBackConnection();
				logger.logError("UserContactOperation", "addUserContact",
						"Failed to insert contact: " + contactDetails.getFirstName(), null);
				return null;
			}

			System.out.println("step 2" + result[0] + "   " + result[1]);


			if (result[0] == 0) {

				qg.rollBackConnection();
				logger.logError("UserContactOperation", "addUserContact",
						"Failed to insert email for contact ID: " + contactDetails.getID(), null);
				return null;
			}


			qg.commit();
			logger.logInfo("UserContactOperation", "addUserContact",
					"Contact added successfully: " + contactDetails.getFirstName());
			return contactDetails;
		} catch (Exception e) {
			logger.logError("UserContactOperation", "addUserContact", "Exception occurred: " + e.getMessage(), e);
	qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());

		} finally {
//            con.close();
			qg.closeConnection();
		}

	}

	/**
	 * Retrieves all contacts for a given user.
	 *
	 * @param user_id the ID of the user
	 * @return a list of UserContacts objects or null if an error occurs
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static ArrayList<ContactDetails> viewAllUserContacts(int user_id) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		ArrayList<Table> result = new ArrayList<Table>();
		ArrayList<ContactDetails> contacts = new ArrayList<ContactDetails>();

		try {

			qg.openConnection();


			ContactDetails contact = new ContactDetails();
			ContactMail contactMail = new ContactMail();
			ContactPhone contactPhone = new ContactPhone();
			contact.setContactMail(contactMail);
			contact.setContactPhone(contactPhone);
			contact.setUserID(user_id);


			result = qg.select(contact).executeQuery();

			if (result.size() > 0) {

				for (Table data : result) {

					contacts.add((ContactDetails) data);
				}
				logger.logInfo("UserContactOperation", "viewAllUserContacts",
						"Contacts retrieved for user ID: " + user_id);
				return contacts;
			} else {

				logger.logInfo("UserContactOperation", "viewAllUserContacts",
						"No Contacts retrieved for user ID: " + user_id);

				return null;
			}

		} catch (Exception e) {
			logger.logError("UserContactOperation", "viewAllUserContacts", "Exception occurred: " + e.getMessage(), e);
			throw new DBOperationException(e.getMessage());

		} finally {
			qg.closeConnection();
		}

	}

	/**
	 * Deletes a specific contact for a user.
	 *
	 * @param user_id    the ID of the user
	 * @param contact_id the ID of the contact to delete
	 * @return true if the contact was deleted successfully, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static boolean deleteContact(ContactDetails contact, int userID) throws DBOperationException {

		int[] result = { -1, -1 };
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {
			qg.openConnection();


			result = qg.delete(contact).execute(userID);

			if (result[0] > 0) {
				logger.logInfo("UserContactOperation", "deleteContact",
						"Contact deleted successfully: " + contact.getUserID());
			} else {
				logger.logError("UserContactOperation", "deleteContact",
						"Failed to delete contact ID: " + contact.getID(), null);
			}
			return result[0] > 0;
		} catch (Exception e) {
			logger.logError("UserContactOperation", "deleteContact", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	/**
	 * Retrieves a specific contact for a user.
	 *
	 * @param user_id    the ID of the user
	 * @param contact_id the ID of the contact to retrieve
	 * @return the UserContacts object or null if not found
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static ContactDetails viewSpecificUserContact(int user_id, int contact_id) throws DBOperationException {

		ArrayList<Table> contacts = new ArrayList<Table>();

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {
			qg.openConnection();


			ContactDetails contact = new ContactDetails();
			ContactMail contactMail = new ContactMail();
			ContactPhone contactPhone = new ContactPhone();
			contact.setContactMail(contactMail);
			contact.setContactPhone(contactPhone);
			contact.setUserID(user_id);
			contact.setID(contact_id);

			contacts = qg.select(contact).executeQuery();

			if (contacts.size() > 0) {
				contact = (ContactDetails) contacts.getFirst();

				logger.logInfo("UserContactOperation", "viewSpecificUserContact",
						"Contact retrieved successfully for contact ID: " + contact_id);
				return contact;
			} else {
				logger.logWarning("UserContactOperation", "viewSpecificUserContact",
						"No contact data available for user ID: " + user_id + ", contact ID: " + contact_id);
				return null;
			}
		} catch (Exception e) {
			logger.logError("UserContactOperation", "viewSpecificUserContact", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	/**
	 * Updates a specific user's contact information.
	 *
	 * @param contact the UserContacts object containing updated contact details
	 * @return true if the contact was updated successfully, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static boolean updateSpecificUserContact(ContactDetails contact, int userID) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] result = { -1, -1 };

		try {
			qg.openConnection();
			result = qg.update(contact).execute(userID);
			System.out.println("the Mail here !!" + contact.getContactMail().getContactMailID());
			if (result[0] == 0) {
				qg.rollBackConnection();
				logger.logError("UserContactOperation", "updateSpecificUserContact","Failed to update contact ID: " + contact.getID(), null);
				return false;
			}
			qg.commit();
			logger.logInfo("UserContactOperation", "updateSpecificUserContact",
					"Contact updated successfully: " + contact.getID());
			return true;
		} catch (Exception e) {
			logger.logError("UserContactOperation", "updateSpecificUserContact",
					"Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	public static ContactDetails viewOauthSpecificUserContact(int user_id, String oauthContactID)
			throws DBOperationException {

		ArrayList<Table> contacts = new ArrayList<Table>();

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {
			qg.openConnection();
			ContactDetails contact = new ContactDetails();
			ContactMail contactMail = new ContactMail();
			ContactPhone contactPhone = new ContactPhone();
			contact.setContactMail(contactMail);
			contact.setContactPhone(contactPhone);
			contact.setUserID(user_id);
			contact.setOauthContactID(oauthContactID);

			contacts = qg.select(contact).executeQuery();

			if (contacts.size() > 0) {
				contact = (ContactDetails) contacts.getFirst();

				logger.logInfo("UserContactOperation", "viewOauthSpecificUserContact",
						"Contact retrieved successfully for contact ID: " + contact.getID());
				return contact;
			} else {
				logger.logWarning("UserContactOperation", "viewOauthSpecificUserContact",
						"No contact data available for user ID: " + user_id + ", contact ID: " + contact.getID());
				return null;
			}
		} catch (Exception e) {
			logger.logError("UserContactOperation", "viewOauthSpecificUserContact",
					"Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

}
