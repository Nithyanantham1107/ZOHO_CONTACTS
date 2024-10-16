package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dbconnect.DBconnection;
import dbmodel.UserContacts;

/**
 * This class provides operations for managing user contacts,
 * including adding, viewing, updating, and deleting contacts.
 */
public class UserContactOperation {
    UserContacts uc;
    Connection con;

    /**
     * Adds a new user contact to the database.
     *
     * @param uc the UserContacts object containing the contact details
     * @return the UserContacts object with the assigned contact ID or null if an error occurs
     * @throws SQLException if a database access error occurs
     */
    public UserContacts addUserContact(UserContacts uc) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Contact_details (user_id, First_name, Middle_name, Last_name, gender, Address, created_At) VALUES (?, ?, ?, ?, ?, ?, ?);",
                PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, uc.getUserid());
            ps.setString(2, uc.getFname());
            ps.setString(3, uc.getMname());
            ps.setString(4, uc.getLname());
            ps.setString(5, uc.getGender());
            ps.setString(6, uc.getAddress());
            ps.setLong(7, uc.getCreatedAt());

            int val = ps.executeUpdate();
            if (val == 0) {
                con.rollback();
                return null;
            }
            ResultSet id = ps.getGeneratedKeys();
            if (id.next()) {
                int gen_contact_id = id.getInt(1);
                uc.setContactid(gen_contact_id);

                ps = con.prepareStatement("INSERT INTO Contact_mail VALUES (?, ?);");
                ps.setInt(1, gen_contact_id);
                ps.setString(2, uc.getEmail());
                val = ps.executeUpdate();
                if (val == 0) {
                    con.rollback();
                    return null;
                }

                ps = con.prepareStatement("INSERT INTO Contact_phone VALUES (?, ?);");
                ps.setInt(1, gen_contact_id);
                ps.setString(2, uc.getPhoneno());
                val = ps.executeUpdate();
                if (val == 0) {
                    con.rollback();
                    return null;
                }
            }
            con.commit();
            return uc;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
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
    public ArrayList<UserContacts> viewAllUserContacts(int user_id) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            ArrayList<UserContacts> user_contacts = new ArrayList<>();

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM Contact_details cd LEFT JOIN Contact_mail cm ON cd.contact_id = cm.contact_id LEFT JOIN Contact_phone cp ON cp.contact_id = cd.contact_id WHERE user_id = ?;");
            ps.setInt(1, user_id);
            ResultSet contacts = ps.executeQuery();
            while (contacts.next()) {
                uc = new UserContacts();
                uc.setUserid(user_id);
                uc.setContactid(contacts.getInt(2));
                uc.setFname(contacts.getString(3));
                uc.setMname(contacts.getString(4));
                uc.setLname(contacts.getString(5));
                uc.setGender(contacts.getString(6));
                uc.setAddress(contacts.getString(7));
                uc.setCreatedAt(contacts.getLong(8));
                uc.setEmail(contacts.getString(10));
                uc.setPhoneno(contacts.getString(12));

                user_contacts.add(uc);
            }
            return user_contacts;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
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
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Contact_details WHERE user_id = ? AND contact_id = ?");
            ps.setInt(1, user_id);
            ps.setInt(2, contact_id);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
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
    public UserContacts viewSpecificUserContact(int user_id, int contact_id) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM Contact_details cd LEFT JOIN Contact_mail cm ON cd.contact_id = cm.contact_id LEFT JOIN Contact_phone cp ON cp.contact_id = cd.contact_id WHERE user_id = ? AND cd.contact_id = ?;");
            ps.setInt(1, user_id);
            ps.setInt(2, contact_id);
            ResultSet contact = ps.executeQuery();

            if (contact.next()) {
                uc = new UserContacts();
                uc.setUserid(contact.getInt(1));
                uc.setContactid(contact.getInt(2));
                uc.setFname(contact.getString(3));
                uc.setMname(contact.getString(4));
                uc.setLname(contact.getString(5));
                uc.setGender(contact.getString(6));
                uc.setAddress(contact.getString(7));
                uc.setEmail(contact.getString(10));
                uc.setPhoneno(contact.getString(12));
                return uc;
            } else {
                System.out.println("No contact data available for this user_id");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
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
    public boolean updateSpecificUserContact(UserContacts uc) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(
                "UPDATE Contact_details SET First_name = ?, Middle_name = ?, Last_name = ?, gender = ?, Address = ? WHERE contact_id = ?;");

            ps.setString(1, uc.getFname());
            ps.setString(2, uc.getMname());
            ps.setString(3, uc.getLname());
            ps.setString(4, uc.getGender());
            ps.setString(5, uc.getAddress());
            ps.setInt(6, uc.getContactid());

            int val = ps.executeUpdate();
            if (val == 0) {
                con.rollback();
                return false;
            }

            ps = con.prepareStatement("UPDATE Contact_mail SET contact_email_id = ? WHERE contact_id = ?;");
            ps.setString(1, uc.getEmail());
            ps.setInt(2, uc.getContactid());

            val = ps.executeUpdate();
            if (val == 0) {
                con.rollback();
                return false;
            }

            ps = con.prepareStatement("UPDATE Contact_phone SET Contact_phone_no = ? WHERE contact_id = ?;");
            ps.setString(1, uc.getPhoneno());
            ps.setInt(2, uc.getContactid());

            val = ps.executeUpdate();
            if (val == 0) {
                con.rollback();
                return false;
            }
            con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return false;
    }
}
