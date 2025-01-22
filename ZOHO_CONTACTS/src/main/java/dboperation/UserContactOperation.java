package dboperation;

import java.sql.SQLException;
import java.util.ArrayList;

import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.Table;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

/**
 * This class provides operations for managing user contacts, including adding,
 * viewing, updating, and deleting contacts.
 */
public class UserContactOperation {

	private LoggerSet logger = new LoggerSet();

	/**
	 * Adds a new user contact to the database.
	 *
	 * @param uc the UserContacts object containing the contact details
	 * @return the UserContacts object with the assigned contact ID or null if an
	 *         error occurs
	 * @throws SQLException if a database access error occurs
	 */

	public ContactDetails addUserContact(ContactDetails contactDetails) throws SQLException {
//        Connection con = DBconnection.getConnection();
		int[] result = { -1, -1 };
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {

			qg.openConnection();
//            con.setAutoCommit(false);
//            PreparedStatement ps = con.prepareStatement(
//                "INSERT INTO Contact_details (user_id, First_name, Middle_name, Last_name, gender, Address, created_At) VALUES (?, ?, ?, ?, ?, ?, ?);",
//                PreparedStatement.RETURN_GENERATED_KEYS);
//            ps.setInt(1, uc.getUserid());
//            ps.setString(2, uc.getFname());
//            ps.setString(3, uc.getMname());
//            ps.setString(4, uc.getLname());
//            ps.setString(5, uc.getGender());
//            ps.setString(6, uc.getAddress());
//            ps.setLong(7, uc.getCreatedAt());
//
//            int val = ps.executeUpdate();

			result = qg.insert(contactDetails).execute(contactDetails.getUserID());

			if (result[0] == -1) {
//                con.rollback();
				qg.rollBackConnection();
				logger.logError("UserContactOperation", "addUserContact",
						"Failed to insert contact: " + contactDetails.getFirstName(), null);
				return null;
			}

			System.out.println("step 2" + result[0] + "   " + result[1]);

//            ResultSet id = ps.getGeneratedKeys();
//            if (result[1] !=-1) {
//                int gen_contact_id = result[1];
//                contactDetails.setContactID(gen_contact_id);
//                contactDetails.getContactMail().setContactID(contactDetails.getContactID());
//                contactDetails.getContactphone().setContactID(contactDetails.getContactID());
//                ps = con.prepareStatement("INSERT INTO Contact_mail VALUES (?, ?);");
//                ps.setInt(1, gen_contact_id);
//                ps.setString(2, uc.getEmail());
//                val = ps.executeUpdate();
//                result=qg.insert(tables.Contact_mail).valuesInsert(contactDetails.getContactMail().getContactID(),contactDetails.getContactMail().getContactMailID(),contactDetails.getContactMail().getCreatedAt(),contactDetails.getContactMail().getModifiedAt()).execute();
//               
//                System.out.println("step 3"+result);

			if (result[0] == 0) {
//                    con.rollback();
				qg.rollBackConnection();
				logger.logError("UserContactOperation", "addUserContact",
						"Failed to insert email for contact ID: " + contactDetails.getID(), null);
				return null;
			}

//                ps = con.prepareStatement("INSERT INTO Contact_phone VALUES (?, ?);");
//                ps.setInt(1, gen_contact_id);
//                ps.setString(2, uc.getPhoneno());
//                val = ps.executeUpdate();

//                
//                result=qg.insert(tables.Contact_phone)
//                		.valuesInsert(contactDetails.getContactphone().getContactID(),contactDetails.getContactphone().getContactPhone(),contactDetails.getContactphone().getCreatedAt(),contactDetails.getContactphone().getModifiedAt()).execute();
//                
//                
//                System.out.println("step 4");
//                if (result[0] == 0) {
////                    con.rollback();
//                	qg.rollBackConnection();
//                    logger.logError("UserContactOperation", "addUserContact", "Failed to insert phone number for contact ID: " + gen_contact_id, null);
//                    return null;
//                }
//            }else {
//            	
//            	qg.rollBackConnection();
//                logger.logError("UserContactOperation", "addUserContact", "Failed to insert contact for name: " + contactDetails.getFirstName(), null);
//                return null;
//            	
//            	
//            }
//            con.commit();
			qg.commit();
			logger.logInfo("UserContactOperation", "addUserContact",
					"Contact added successfully: " + contactDetails.getFirstName());
			return contactDetails;
		} catch (Exception e) {
			logger.logError("UserContactOperation", "addUserContact", "Exception occurred: " + e.getMessage(), e);
//            con.rollback(); // Ensure rollback in case of exception
			qg.rollBackConnection();
		} finally {
//            con.close();
			qg.closeConnection();
		}
		return null;
	}

	/**
	 * Retrieves all contacts for a given user.
	 *
	 * @param user_id the ID of the user
	 * @return a list of UserContacts objects or null if an error occurs
	 * @throws SQLException if a database access error occurs
	 */
	public ArrayList<ContactDetails> viewAllUserContacts(int user_id) throws SQLException {
//        Connection con = DBconnection.getConnection();

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		ArrayList<Table> result = new ArrayList<Table>();
		ArrayList<ContactDetails> contacts = new ArrayList<ContactDetails>();

		try {

			qg.openConnection();
//            ArrayList<UserContacts> user_contacts = new ArrayList<>();

//            PreparedStatement ps = con.prepareStatement(
//                "SELECT * FROM Contact_details cd LEFT JOIN Contact_mail cm ON cd.contact_id = cm.contact_id LEFT JOIN Contact_phone cp ON cp.contact_id = cd.contact_id WHERE user_id = ?;");
//            ps.setInt(1, user_id);
//            ResultSet contacts = ps.executeQuery();
//            while (contacts.next()) {
//                uc = new UserContacts();
//                uc.setUserid(user_id);
//                uc.setContactid(contacts.getInt(2));
//                uc.setFname(contacts.getString(3));
//                uc.setMname(contacts.getString(4));
//                uc.setLname(contacts.getString(5));
//                uc.setGender(contacts.getString(6));
//                uc.setAddress(contacts.getString(7));
//                uc.setCreatedAt(contacts.getLong(8));
//                uc.setEmail(contacts.getString(10));
//                uc.setPhoneno(contacts.getString(12));
//
//                user_contacts.add(uc);
//            }

			ContactDetails contact = new ContactDetails();
			ContactMail contactMail=new ContactMail();
			ContactPhone contactPhone=new ContactPhone();
			contact.setContactMail(contactMail);
			contact.setContactPhone(contactPhone);
			contact.setUserID(user_id);

//            data= qg.select(tables.Contact_details)
//            		.join(JoinType.left, Contact_details.contact_id, Operation.Equal, Contact_mail.contact_id)
//            		.join(JoinType.left, Contact_details.contact_id, Operation.Equal, Contact_phone.contact_id)
//            		.where(Contact_details.user_id, Operation.Equal, user_id).executeQuery();

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

//            for(Object i : data) {
//            	if(i instanceof ContactDetails) {
//            		
//            		uc.add((ContactDetails) i);
//            	}
//            }

//            return user_contacts; logger.logInfo("UserContactOperation", "viewAllUserContacts", "Contacts retrieved for user ID: " + user_id);

		} catch (Exception e) {
			logger.logError("UserContactOperation", "viewAllUserContacts", "Exception occurred: " + e.getMessage(), e);
		} finally {
//            con.close();
			qg.closeConnection();
		}
		return null;
	}

	/**
	 * Deletes a specific contact for a user.
	 *
	 * @param user_id    the ID of the user
	 * @param contact_id the ID of the contact to delete
	 * @return true if the contact was deleted successfully, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
	public boolean deleteContact(ContactDetails contact, int userID) throws SQLException {
//        Connection con = DBconnection.getConnection();
		int[] result = { -1, -1 };
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {
			qg.openConnection();

//            PreparedStatement ps = con.prepareStatement("DELETE FROM Contact_details WHERE user_id = ? AND contact_id = ?");
//            ps.setInt(1, user_id);
//            ps.setInt(2, contact_id);
//            int result = ps.executeUpdate();

//        	 result=qg.delete(tables.Contact_details)
//        			 .where(TableSchema.Contact_details.user_id, Operation.Equal, contact.getUserID())
//        			 .and(TableSchema.Contact_details.contact_id, Operation.Equal,contact.getContactID())
//        			 .execute();

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
		} finally {
//            con.close();
			qg.closeConnection();
		}
		return false;
	}

	/**
	 * Retrieves a specific contact for a user.
	 *
	 * @param user_id    the ID of the user
	 * @param contact_id the ID of the contact to retrieve
	 * @return the UserContacts object or null if not found
	 * @throws SQLException if a database access error occurs
	 */
	public ContactDetails viewSpecificUserContact(int user_id, int contact_id) throws SQLException {
//        Connection con = DBconnection.getConnection();
		ArrayList<Table> contacts = new ArrayList<Table>();

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {
			qg.openConnection();

//            PreparedStatement ps = con.prepareStatement(
//                "SELECT * FROM Contact_details cd LEFT JOIN Contact_mail cm ON cd.contact_id = cm.contact_id LEFT JOIN Contact_phone cp ON cp.contact_id = cd.contact_id WHERE user_id = ? AND cd.contact_id = ?;");
//            ps.setInt(1, user_id);
//            ps.setInt(2, contact_id);
//            ResultSet contact = ps.executeQuery();

//        	uc=(ContactDetails )  qg.select(tables.Contact_details)
//        			.join(JoinType.left,Contact_details.contact_id , Operation.Equal, Contact_mail.contact_id)
//        			.join(JoinType.left, Contact_details.contact_id, Operation.Equal, Contact_phone.contact_id)
//        			.where(Contact_details.user_id,Operation.Equal, user_id)
//        			.and(Contact_details.contact_id, Operation.Equal, contact_id)
//        			.executeQuery().getFirst();

			ContactDetails contact = new ContactDetails();
			ContactMail contactMail=new ContactMail();
			ContactPhone contactPhone=new ContactPhone();
			contact.setContactMail(contactMail);
			contact.setContactPhone(contactPhone);
			contact.setUserID(user_id);
			contact.setID(contact_id);

			contacts = qg.select(contact).executeQuery();

			if (contacts.size() > 0) {
				contact = (ContactDetails) contacts.getFirst();
//                uc = new UserContacts();
//                uc.setUserid(contact.getInt(1));
//                uc.setContactid(contact.getInt(2));
//                uc.setFname(contact.getString(3));
//                uc.setMname(contact.getString(4));
//                uc.setLname(contact.getString(5));
//                uc.setGender(contact.getString(6));
//                uc.setAddress(contact.getString(7));
//                uc.setEmail(contact.getString(10));
//                uc.setPhoneno(contact.getString(12));

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
		} finally {
//            con.close();
			qg.closeConnection();
		}
		return null;
	}

	/**
	 * Updates a specific user's contact information.
	 *
	 * @param contact the UserContacts object containing updated contact details
	 * @return true if the contact was updated successfully, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
	public boolean updateSpecificUserContact(ContactDetails contact, int userID) throws SQLException {
//        Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] result = { -1, -1 };

		try {

			qg.openConnection();
//            con.setAutoCommit(false);
//            PreparedStatement ps = con.prepareStatement(
//                "UPDATE Contact_details SET First_name = ?, Middle_name = ?, Last_name = ?, gender = ?, Address = ? WHERE contact_id = ?;");
//
//            ps.setString(1, uc.getFname());
//            ps.setString(2, uc.getMname());
//            ps.setString(3, uc.getLname());
//            ps.setString(4, uc.getGender());
//            ps.setString(5, uc.getAddress());
//            ps.setInt(6, uc.getContactid());
//
//            int val = ps.executeUpdate();

//         val=qg.update(tables.Contact_details,Contact_details.First_name,Contact_details.Middle_name,Contact_details.Last_name,Contact_details.gender,Contact_details.Address,Contact_details.modified_time)
//        	.valuesUpdate(uc.getFirstName(),uc.getMiddleName(),uc.getLastName(),uc.getGender(),uc.getAddress(),uc.getModifiedAt())
//        	.where(Contact_details.contact_id, Operation.Equal, uc.getContactID()).execute();

			result = qg.update(contact).execute(userID);
			System.out.println("the address here !!" + contact.getAddress());
			if (result[0] == 0) {
//                con.rollback();
				qg.rollBackConnection();
				logger.logError("UserContactOperation", "updateSpecificUserContact",
						"Failed to update contact ID: " + contact.getID(), null);
				return false;
			}

//            ps = con.prepareStatement("UPDATE Contact_mail SET contact_email_id = ? WHERE contact_id = ?;");
//            ps.setString(1, uc.getEmail());
//            ps.setInt(2, uc.getContactid());
//
//            val = ps.executeUpdate();

//            result=qg.update(tables.Contact_mail,Contact_mail.Contact_email_id,Contact_mail.modified_time)
//            		.valuesUpdate(uc.getContactMail().getContactMailID(),uc.getContactMail().getModifiedAt())
//            		.where(Contact_mail.contact_id, Operation.Equal,uc.getContactID())
//            		.execute();

//            if (result[0] == 0) {
//                con.rollback();
//            	qg.rollBackConnection();
//                logger.logError("UserContactOperation", "updateSpecificUserContact", "Failed to update email for contact ID: " + uc.getContactID(), null);
//                return false;
//            }

//            ps = con.prepareStatement("UPDATE Contact_phone SET Contact_phone_no = ? WHERE contact_id = ?;");
//            ps.setString(1, uc.getPhoneno());
//            ps.setInt(2, uc.getContactid());
//
//            val = ps.executeUpdate();

//            result=qg.update(tables.Contact_phone, Contact_phone.Contact_phone_no,Contact_phone.modified_time)
//            		.valuesUpdate(uc.getContactphone().getContactPhone(),uc.getContactphone().getModifiedAt())
//            		.where(Contact_phone.contact_id, Operation.Equal, uc.getContactID())
//            		.execute();

//            if (result[0] == 0) {
//                con.rollback();
//            	qg.rollBackConnection();
//                logger.logError("UserContactOperation", "updateSpecificUserContact", "Failed to update phone number for contact ID: " + uc.getContactID(), null);
//                return false;
//            }
//            con.commit();
			qg.commit();
			logger.logInfo("UserContactOperation", "updateSpecificUserContact",
					"Contact updated successfully: " + contact.getID());
			return true;
		} catch (Exception e) {
			logger.logError("UserContactOperation", "updateSpecificUserContact",
					"Exception occurred: " + e.getMessage(), e);
//            con.rollback(); // Ensure rollback in case of exception
			qg.rollBackConnection();
		} finally {
//            con.close();
			qg.closeConnection();
		}
		return false;
	}
}
