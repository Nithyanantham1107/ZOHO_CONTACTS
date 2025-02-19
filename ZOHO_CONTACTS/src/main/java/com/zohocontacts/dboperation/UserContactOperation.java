package com.zohocontacts.dboperation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.ContactDetailsSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.Operation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.ContactMail;
import com.zohocontacts.dbpojo.ContactPhone;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.exception.QueryBuilderException;
import com.zohocontacts.loggerfiles.LoggerSet;

/**
 * This class provides operations for managing user contacts, including adding,
 * viewing, updating, and deleting contacts.
 */
public class UserContactOperation {
	private static final int LIMIT = 11;
	private static final int OFFSET = 10;

	public static ContactDetails addUserContact(ContactDetails contactDetails) throws DBOperationException {
		int[] result = { -1, -1 };
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			result = query.insert(contactDetails).execute(contactDetails.getUserID());

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("UserContactOperation", "addUserContact",
						"Failed to insert contact: " + contactDetails.getFirstName(), null);
				return null;
			}

			if (result[0] == 0) {

				query.rollBackConnection();
				LoggerSet.logError("UserContactOperation", "addUserContact",
						"Failed to insert email for contact ID: " + contactDetails.getID(), null);
				return null;
			}

			query.commit();
			LoggerSet.logInfo("UserContactOperation", "addUserContact",
					"Contact added successfully: " + contactDetails.getFirstName());
			return contactDetails;
		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "addUserContact", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "addUserContact", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		}

	}

	/**
	 * Retrieves all contacts for a given user.
	 *
	 * @param userID the ID of the user
	 * @return a list of UserContacts objects or null if an error occurs
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static List<ContactDetails> viewAllUserContacts(long userID) throws DBOperationException {

		List<Table> result;
		List<ContactDetails> contacts = new ArrayList<ContactDetails>();

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			ContactDetails contact = new ContactDetails();
			ContactMail contactMail = new ContactMail();
			ContactPhone contactPhone = new ContactPhone();
			contact.setContactMail(contactMail);
			contact.setContactPhone(contactPhone);
			contact.setUserID(userID);

			result = query.select(contact).executeQuery();

			if (result.size() > 0) {

				for (Table data : result) {

					contacts.add((ContactDetails) data);
				}
				LoggerSet.logInfo("UserContactOperation", "viewAllUserContacts",
						"Contacts retrieved for user ID: " + userID);
				return contacts;
			} else {

				LoggerSet.logInfo("UserContactOperation", "viewAllUserContacts",
						"No Contacts retrieved for user ID: " + userID);

				return null;
			}

		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "viewAllUserContacts", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "viewAllUserContacts", "Exception occurred: " + e.getMessage(),
					e);
			throw new DBOperationException(e.getMessage());

		}

	}

	public static Boolean addContactPhone(List<ContactPhone> phones, long userID) throws DBOperationException {

		int[] result = { -1, -1 };
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			for (ContactPhone phone : phones) {

				result = query.insert(phone).execute(userID);
				if (result[0] == -1) {

					query.rollBackConnection();
					LoggerSet.logInfo("UserContactOperation", "addContactPhone",
							"Failed to  add ContactPhone no for number: " + phone.getContactPhone());
					return false;

				}
			}

			LoggerSet.logInfo("UserContactOperation", "addContactPhone", "  ContactMail  successfully added ");

			return true;

		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "addContactPhone", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "addContactPhone", "Exception occurred: " + e.getMessage(), e);
			throw new DBOperationException(e.getMessage());

		}

	}

	public static Boolean addContactMail(List<ContactMail> mails, long userID) throws DBOperationException {

		int[] result = { -1, -1 };
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			for (ContactMail mail : mails) {

				result = query.insert(mail).execute(userID);
				if (result[0] == -1) {

					query.rollBackConnection();
					LoggerSet.logInfo("UserContactOperation", "addContactMail",
							"Failed to  add ContactMail for Contact ID: " + mail.getID());
					return false;

				}
			}

			LoggerSet.logInfo("UserContactOperation", "addContactMail", "  ContactMail  successfully added ");

			return true;

		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "addContactMail", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "addContactMail", "Exception occurred: " + e.getMessage(), e);
			throw new DBOperationException(e.getMessage());

		}

	}

	/**
	 * Deletes a specific contact for a user.
	 *
	 * @param USERID    the ID of the user
	 * @param CONTACTID the ID of the contact to delete
	 * @return true if the contact was deleted successfully, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static boolean deleteContact(ContactDetails contact, long userID) throws DBOperationException {

		int[] result = { -1, -1 };
		List<Table> resultList = new ArrayList<Table>();
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			resultList = query.select(contact).executeQuery();
			if (resultList.size() > 0) {
				ContactDetails contactDB = (ContactDetails) resultList.getFirst();

				result = query.delete(contact).execute(userID);
				if (result[0] != -1) {

					LoggerSet.logInfo("UserContactOperation", "deleteContact",
							"Contact deleted successfully: " + contactDB.getUserID());
				} else {
					LoggerSet.logError("UserContactOperation", "deleteContact",
							"Failed to delete contact ID: " + contactDB.getID(), null);
				}
				return result[0] > 0;

			} else {

				return false;
			}

		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "deleteContact", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "deleteContact", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		}

	}

	/**
	 * Retrieves a specific contact for a user.
	 *
	 * @param userID    the ID of the user
	 * @param contactID the ID of the contact to retrieve
	 * @return the UserContacts object or null if not found
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static ContactDetails viewSpecificUserContact(long userID, long contactID) throws DBOperationException {

		List<Table> contacts = new ArrayList<Table>();

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			ContactDetails contact = new ContactDetails();
			ContactMail contactMail = new ContactMail();
			ContactPhone contactPhone = new ContactPhone();
			contact.setContactMail(contactMail);
			contact.setContactPhone(contactPhone);
			contact.setUserID(userID);
			contact.setID(contactID);

			contacts = query.select(contact).executeQuery();

			if (contacts.size() > 0) {
				contact = (ContactDetails) contacts.getFirst();
				LoggerSet.logInfo("UserContactOperation", "viewSpecificUserContact",
						"Contact retrieved successfully for contact ID: " + contactID);
				return contact;
			} else {
				LoggerSet.logWarning("UserContactOperation", "viewSpecificUserContact",
						"No contact data available for user ID: " + userID + ", contact ID: " + contactID);
				return null;
			}
		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "viewSpecificUserContact",
					"Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "viewSpecificUserContact",
					"Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
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
	public static boolean updateSpecificUserContact(ContactDetails contact, long userID) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			ContactDetails contactDB = new ContactDetails();
			contact.setID(contact.getID());
			contactDB.setContactMail(new ContactMail());
			contactDB.setContactPhone(new ContactPhone());

			result = query.update(contact).execute(userID);
			if (result[0] == 0) {
				query.rollBackConnection();
				LoggerSet.logError("UserContactOperation", "updateSpecificUserContact",
						"Failed to update contact ID: " + contact.getID(), null);
				return false;
			}
			query.commit();
			LoggerSet.logInfo("UserContactOperation", "updateSpecificUserContact",
					"Contact updated successfully: " + contact.getID());
			return true;
		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "updateSpecificUserContact",
					"Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "updateSpecificUserContact",
					"Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static Boolean deleteAllContactMail(List<ContactMail> mails, long userID) throws DBOperationException {

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			for (ContactMail mail : mails) {
				int[] result = { -1, -1 };
				result = query.delete(mail).execute(userID);

				if (result[0] == -1) {
					query.rollBackConnection();
					LoggerSet.logInfo("UserContactOperation", "deleteAllContactMail",
							"Failed to  delete ContactMail for Contact ID: " + mail.getID());
					return false;
				}

			}

			LoggerSet.logInfo("UserContactOperation", "deleteAllContactMail", "Contact Mail deleted  successfully ");
			return true;
		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "deleteAllContactMail", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "deleteAllContactMail", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException(e.getMessage());

		}

	}

	public static Boolean mergeUserContacts(long[] contactID, long userID) throws DBOperationException {
		int[] result = { -1, -1 };
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			if (contactID.length > 1) {

				query.openConnection();
				ContactDetails mergeContact = new ContactDetails();
				mergeContact.setID(contactID[0]);
				mergeContact.setContactMail(new ContactMail());
				mergeContact.setContactPhone(new ContactPhone());
				List<Table> resultList = query.select(mergeContact).executeQuery();

				if (resultList != null && resultList.size() > 0) {

					List<ContactMail> mails = new ArrayList<ContactMail>();
					List<ContactPhone> phones = new ArrayList<ContactPhone>();
					mergeContact = (ContactDetails) resultList.getFirst();
					for (int i = 1; i < contactID.length; i++) {
						ContactDetails contactToMerge = new ContactDetails();
						contactToMerge.setID(contactID[i]);
						contactToMerge.setContactMail(new ContactMail());
						contactToMerge.setContactPhone(new ContactPhone());
						resultList = query.select(contactToMerge).executeQuery();
						if (resultList != null && resultList.size() > 0) {

							contactToMerge = (ContactDetails) resultList.getFirst();

							for (ContactMail primaryMail : mergeContact.getAllContactMail()) {

								boolean isMailExist = false;
								ContactMail contactmail = null;
								for (ContactMail mergeContactMail : contactToMerge.getAllContactMail()) {

									contactmail = mergeContactMail;
									if (mergeContactMail.getContactMailID().equals(primaryMail.getContactMailID())) {
										isMailExist = true;
										break;

									}

								}

								if (!isMailExist && contactmail != null) {

									mails.add(contactmail);
								}
							}

							for (ContactPhone primaryPhone : mergeContact.getAllContactphone()) {

								boolean isPhoneExist = false;
								ContactPhone contactphone = null;
								for (ContactPhone mergeContactPhone : contactToMerge.getAllContactphone()) {

									contactphone = mergeContactPhone;
									if (mergeContactPhone.getContactPhone().equals(primaryPhone.getContactPhone())) {
										isPhoneExist = true;
										break;

									}

								}

								if (!isPhoneExist && contactphone != null) {

									phones.add(contactphone);
								}
							}
						}

						result = query.delete(contactToMerge).execute(userID);

						if (result[0] == -1) {

							query.rollBackConnection();
							LoggerSet.logInfo("UserContactOperation", "mergeUserContacts",
									"Failed To delete contact for contact ID:" + mergeContact.getID());
							return false;
						}
					}

					for (ContactMail mail : mails) {
						mail.setContactID(mergeContact.getID());
						result = query.insert(mail).execute(userID);
						if (result[0] == -1) {

							query.rollBackConnection();
							LoggerSet.logInfo("UserContactOperation", "mergeUserContacts", "Failed To merge contacts");
							return false;
						}

					}

					for (ContactPhone phone : phones) {
						phone.setContactID(mergeContact.getID());
						result = query.insert(phone).execute(userID);
						if (result[0] == -1) {

							query.rollBackConnection();
							LoggerSet.logInfo("UserContactOperation", "mergeUserContacts", "Failed To merge contacts");
							return false;
						}

					}
					LoggerSet.logInfo("UserContactOperation", "mergeUserContacts", "Contact Merged Successfully");

					return true;
				} else {
					LoggerSet.logInfo("UserContactOperation", "mergeUserContacts",
							"No Contacts Available to perform merge");
					return false;
				}

			} else {
				LoggerSet.logInfo("UserContactOperation", "mergeUserContacts",
						"More than one cotactID should be specified to perform Merge");
				return false;
			}

		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "mergeUserContacts", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (

		Exception e) {
			LoggerSet.logError("UserContactOperation", "mergeUserContacts", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		}

	}

	public static Boolean deleteAllContactPhone(List<ContactPhone> phones, long userID) throws DBOperationException {

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			for (ContactPhone phone : phones) {
				int[] result = { -1, -1 };
				result = query.delete(phone).execute(userID);

				if (result[0] == -1) {
					query.rollBackConnection();
					LoggerSet.logInfo("UserContactOperation", "deleteAllContactPhone",
							"Failed to  delete ContactMail for Contact ID: " + phone.getID());
					return false;
				}

			}

			LoggerSet.logInfo("UserContactOperation", "deleteAllContactPhone", "Contact Mail deleted  successfully ");
			return true;
		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "deleteAllContactPhone", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		} catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "deleteAllContactPhone", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException(e.getMessage());

		}

	}

	public static ContactDetails viewOauthSpecificUserContact(long userID, String oauthContactID)
			throws DBOperationException {

		List<Table> contacts = new ArrayList<Table>();

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();
			ContactDetails contact = new ContactDetails();
			ContactMail contactMail = new ContactMail();
			ContactPhone contactPhone = new ContactPhone();
			contact.setContactMail(contactMail);
			contact.setContactPhone(contactPhone);
			contact.setUserID(userID);
			contact.setOauthContactID(oauthContactID);

			contacts = query.select(contact).executeQuery();

			if (contacts.size() > 0) {
				contact = (ContactDetails) contacts.getFirst();

				LoggerSet.logInfo("UserContactOperation", "viewOauthSpecificUserContact",
						"Contact retrieved successfully for contact ID: " + contact.getID());
				return contact;
			} else {
				LoggerSet.logWarning("UserContactOperation", "viewOauthSpecificUserContact",
						"No contact data available for user ID: " + userID + ", contact ID: " + contact.getID());
				return null;
			}
		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "viewOauthSpecificUserContact",
					"Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "viewOauthSpecificUserContact",
					"Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		}

	}

	public static List<ContactDetails> viewUserContactOffset(int offset, long userID) throws DBOperationException {

		List<Table> contacts = new ArrayList<Table>();
		List<ContactDetails> contactOffset = new ArrayList<ContactDetails>();
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();
			ContactDetails contact = new ContactDetails();
			ContactMail contactMail = new ContactMail();
			ContactPhone contactPhone = new ContactPhone();

			contact.setUserID(userID);

			contacts = query.select(contact).where(ContactDetailsSchema.USERID, Operation.EQUAL, userID).limit(LIMIT)
					.offset(offset * OFFSET).executeQuery();

			if (contacts != null && contacts.size() > 0) {

				for (Table table : contacts) {
					ContactDetails contactDB = (ContactDetails) table;

				}
				LoggerSet.logInfo("UserContactOperation", "viewUserContactOffset",
						"Contact retrieved successfully for page: " + offset);

				System.out.println(
						"###################3here the size of contact retrieve for offset is " + contacts.size());
				return contactOffset;
			} else {
				LoggerSet.logWarning("UserContactOperation", "viewUserContactOffset",
						"No contact data available for user ID: " + userID + ", contact ID: " + contact.getID());
				return null;
			}
		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserContactOperation", "viewUserContactOffset", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		} catch (Exception e) {
			LoggerSet.logError("UserContactOperation", "viewUserContactOffset", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException(e.getMessage());

		}

	}

}
