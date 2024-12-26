package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dbconnect.DBconnection;
import dbmodel.UserContacts;
import dbpojo.ContactDetails;
import loggerfiles.LoggerSet;
import querybuilder.QueryBuilder;
import querybuilder.SqlQueryLayer;
import querybuilder.TableSchema;
import querybuilder.TableSchema.Contact_details;
import querybuilder.TableSchema.Contact_mail;
import querybuilder.TableSchema.Contact_phone;
import querybuilder.TableSchema.JoinType;
import querybuilder.TableSchema.Operation;
import querybuilder.TableSchema.Statement;
import querybuilder.TableSchema.tables;

/**
 * This class provides operations for managing user contacts,
 * including adding, viewing, updating, and deleting contacts.
 */
public class UserContactOperation {
   
    private LoggerSet logger = new LoggerSet();

    /**
     * Adds a new user contact to the database.
     *
     * @param uc the UserContacts object containing the contact details
     * @return the UserContacts object with the assigned contact ID or null if an error occurs
     * @throws SQLException if a database access error occurs
     */

    
    
    
    public ContactDetails addUserContact(ContactDetails ud) throws SQLException {
//        Connection con = DBconnection.getConnection();
        int[]  val= {-1,-1};
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
        	
        	val=qg.insert(tables.Contact_details, Contact_details.user_id,Contact_details.First_name,Contact_details.Middle_name,Contact_details.Last_name,Contact_details.gender,Contact_details.Address,Contact_details.created_At)
        			.valuesInsert(ud.getUserID(),ud.getFirstName(),ud.getMiddleName(),ud.getLastName(),ud.getGender(),ud.getAddress(),ud.getCreatedAt())
        			.execute(Statement.RETURN_GENERATED_KEYS);
        	System.out.println("step 1"+val[0]+" "+val[1]);
            if (val[0] == -1) {
//                con.rollback();
            	qg.rollBackConnection();
                logger.logError("UserContactOperation", "addUserContact", "Failed to insert contact: " + ud.getFirstName(), null);
                return null;
            }
            
            System.out.println("step 2"+val[0]+"   "+val[1]);
            
//            ResultSet id = ps.getGeneratedKeys();
            if (val[1] !=-1) {
                int gen_contact_id = val[1];
                ud.setContactID(gen_contact_id);

//                ps = con.prepareStatement("INSERT INTO Contact_mail VALUES (?, ?);");
//                ps.setInt(1, gen_contact_id);
//                ps.setString(2, uc.getEmail());
//                val = ps.executeUpdate();
                val=qg.insert(tables.Contact_mail).valuesInsert(gen_contact_id,ud.getContactMail().getContactMailID()).execute();
               
                System.out.println("step 3"+val);
                
                if (val[0] == 0) {
//                    con.rollback();
                	qg.rollBackConnection();
                    logger.logError("UserContactOperation", "addUserContact", "Failed to insert email for contact ID: " + gen_contact_id, null);
                    return null;
                }

//                ps = con.prepareStatement("INSERT INTO Contact_phone VALUES (?, ?);");
//                ps.setInt(1, gen_contact_id);
//                ps.setString(2, uc.getPhoneno());
//                val = ps.executeUpdate();
                
                
                
                
                val=qg.insert(tables.Contact_phone)
                		.valuesInsert(gen_contact_id,ud.getContactphone().getContactPhone()).execute();
                
                
                System.out.println("step 4");
                if (val[0] == 0) {
//                    con.rollback();
                	qg.rollBackConnection();
                    logger.logError("UserContactOperation", "addUserContact", "Failed to insert phone number for contact ID: " + gen_contact_id, null);
                    return null;
                }
            }else {
            	
            	qg.rollBackConnection();
                logger.logError("UserContactOperation", "addUserContact", "Failed to insert contact for name: " + ud.getFirstName(), null);
                return null;
            	
            	
            }
//            con.commit();
            qg.commit();
            logger.logInfo("UserContactOperation", "addUserContact", "Contact added successfully: " + ud.getFirstName());
            return ud;
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
        ArrayList<ContactDetails> uc=new ArrayList<ContactDetails>();
        ArrayList<Object> data;
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
            
            data= qg.select(tables.Contact_details)
            		.join(JoinType.left, Contact_details.contact_id, Operation.Equal, Contact_mail.contact_id)
            		.join(JoinType.left, Contact_details.contact_id, Operation.Equal, Contact_phone.contact_id)
            		.where(Contact_details.user_id, Operation.Equal, user_id).executeQuery();
          
            
            for(Object i : data) {
            	if(i instanceof ContactDetails) {
            		
            		uc.add((ContactDetails) i);
            	}
            }
            
            
            
            logger.logInfo("UserContactOperation", "viewAllUserContacts", "Contacts retrieved for user ID: " + user_id);
//            return user_contacts;
            return uc;
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
     * @param user_id the ID of the user
     * @param contact_id the ID of the contact to delete
     * @return true if the contact was deleted successfully, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteContact(int user_id, int contact_id) throws SQLException {
        Connection con = DBconnection.getConnection();
        int[] result= {-1,-1};
        QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
        try {
        	qg.openConnection();
        	  
        	
//            PreparedStatement ps = con.prepareStatement("DELETE FROM Contact_details WHERE user_id = ? AND contact_id = ?");
//            ps.setInt(1, user_id);
//            ps.setInt(2, contact_id);
//            int result = ps.executeUpdate();
        	
        	
        	 result=qg.delete(tables.Contact_details)
        			 .where(TableSchema.Contact_details.user_id, Operation.Equal, user_id)
        			 .and(TableSchema.Contact_details.contact_id, Operation.Equal,contact_id)
        			 .execute();
        	
        	
            if (result[0] > 0) {
                logger.logInfo("UserContactOperation", "deleteContact", "Contact deleted successfully: " + contact_id);
            } else {
                logger.logError("UserContactOperation", "deleteContact", "Failed to delete contact ID: " + contact_id, null);
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
     * @param user_id the ID of the user
     * @param contact_id the ID of the contact to retrieve
     * @return the UserContacts object or null if not found
     * @throws SQLException if a database access error occurs
     */
    public ContactDetails viewSpecificUserContact(int user_id, int contact_id) throws SQLException {
//        Connection con = DBconnection.getConnection();
        ContactDetails uc;
        QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
        try {
        	qg.openConnection();
        	
//            PreparedStatement ps = con.prepareStatement(
//                "SELECT * FROM Contact_details cd LEFT JOIN Contact_mail cm ON cd.contact_id = cm.contact_id LEFT JOIN Contact_phone cp ON cp.contact_id = cd.contact_id WHERE user_id = ? AND cd.contact_id = ?;");
//            ps.setInt(1, user_id);
//            ps.setInt(2, contact_id);
//            ResultSet contact = ps.executeQuery();

        	uc=(ContactDetails )  qg.select(tables.Contact_details)
        			.join(JoinType.left,Contact_details.contact_id , Operation.Equal, Contact_mail.contact_id)
        			.join(JoinType.left, Contact_details.contact_id, Operation.Equal, Contact_phone.contact_id)
        			.where(Contact_details.user_id,Operation.Equal, user_id)
        			.and(Contact_details.contact_id, Operation.Equal, contact_id)
        			.executeQuery().getFirst();
        	
        	
        	

        	
        	
            if (uc!=null) {
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
            	
                logger.logInfo("UserContactOperation", "viewSpecificUserContact", "Contact retrieved successfully for contact ID: " + contact_id);
                return uc;
            } else {
                logger.logWarning("UserContactOperation", "viewSpecificUserContact", "No contact data available for user ID: " + user_id + ", contact ID: " + contact_id);
                return null;
            }
        } catch (Exception e) {
            logger.logError("UserContactOperation", "viewSpecificUserContact", "Exception occurred: " + e.getMessage(), e);
        } finally {
//            con.close();
        	qg.closeConnection();
        }
        return null;
    }

    /**
     * Updates a specific user's contact information.
     *
     * @param uc the UserContacts object containing updated contact details
     * @return true if the contact was updated successfully, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateSpecificUserContact(ContactDetails uc) throws SQLException {
//        Connection con = DBconnection.getConnection();
        QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
        int[] val= {-1,-1};
        
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
        	
        	
         val=qg.update(tables.Contact_details,Contact_details.First_name,Contact_details.Middle_name,Contact_details.Last_name,Contact_details.gender,Contact_details.Address)
        	.valuesUpdate(uc.getFirstName(),uc.getMiddleName(),uc.getLastName(),uc.getGender(),uc.getAddress())
        	.where(Contact_details.contact_id, Operation.Equal, uc.getContactID()).execute();
         
         
         System.out.println("the address here !!"+uc.getAddress());
            if (val[0] == 0) {
//                con.rollback();
            	qg.rollBackConnection();
                logger.logError("UserContactOperation", "updateSpecificUserContact", "Failed to update contact ID: " + uc.getContactID(), null);
                return false;
            }

//            ps = con.prepareStatement("UPDATE Contact_mail SET contact_email_id = ? WHERE contact_id = ?;");
//            ps.setString(1, uc.getEmail());
//            ps.setInt(2, uc.getContactid());
//
//            val = ps.executeUpdate();
            
            
            val=qg.update(tables.Contact_mail,Contact_mail.Contact_email_id)
            		.valuesUpdate(uc.getContactMail().getContactMailID())
            		.where(Contact_mail.contact_id, Operation.Equal,uc.getContactID())
            		.execute();
            
            if (val[0] == 0) {
//                con.rollback();
            	qg.rollBackConnection();
                logger.logError("UserContactOperation", "updateSpecificUserContact", "Failed to update email for contact ID: " + uc.getContactID(), null);
                return false;
            }

//            ps = con.prepareStatement("UPDATE Contact_phone SET Contact_phone_no = ? WHERE contact_id = ?;");
//            ps.setString(1, uc.getPhoneno());
//            ps.setInt(2, uc.getContactid());
//
//            val = ps.executeUpdate();
            
            
            
            val=qg.update(tables.Contact_phone, Contact_phone.Contact_phone_no)
            		.valuesUpdate(uc.getContactphone().getContactPhone())
            		.where(Contact_phone.contact_id, Operation.Equal, uc.getContactID())
            		.execute();
            
            if (val[0] == 0) {
//                con.rollback();
            	qg.rollBackConnection();
                logger.logError("UserContactOperation", "updateSpecificUserContact", "Failed to update phone number for contact ID: " + uc.getContactID(), null);
                return false;
            }
//            con.commit();
            qg.commit();
            logger.logInfo("UserContactOperation", "updateSpecificUserContact", "Contact updated successfully: " + uc.getContactID());
            return true;
        } catch (Exception e) {
            logger.logError("UserContactOperation", "updateSpecificUserContact", "Exception occurred: " + e.getMessage(), e);
//            con.rollback(); // Ensure rollback in case of exception
            qg.rollBackConnection();
        } finally {
//            con.close();
        	qg.closeConnection();
        }
        return false;
    }
}
