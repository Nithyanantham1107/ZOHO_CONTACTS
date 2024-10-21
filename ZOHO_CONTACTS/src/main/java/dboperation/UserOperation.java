package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import dbconnect.DBconnection;
import dbmodel.UserData;
import loggerfiles.LoggerSet;

public class UserOperation {

    private LoggerSet logger = new LoggerSet();

    /**
     * Creates a new user in the database.
     *
     * @param data the UserData object containing user details
     * @return the created UserData object or null if an error occurs
     * @throws SQLException if a database access error occurs
     */
    public UserData createUser(UserData data) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            String password = BCrypt.hashpw(data.getPassword(), BCrypt.gensalt());
            con.setAutoCommit(false);

            // Insert user data
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO user_data (Name, password, phone_no, address, timezone) VALUES (?, ?, ?, ?, ?);",
                PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, data.getName());
            ps.setString(2, password);
            ps.setString(3, data.getPhoneno());
            ps.setString(4, data.getAddress());
            ps.setString(5, data.getTimezone());
            int val = ps.executeUpdate();

            if (val == 0) {
                con.rollback();
                logger.logError("UserOperation", "createUser", "Failed to insert user data", null);
                return null;
            }

            ResultSet id = ps.getGeneratedKeys();
            if (id.next()) {
                int genUserId = id.getInt(1);

                // Insert login credentials
                ps = con.prepareStatement("INSERT INTO Login_credentials VALUES (?, ?);");
                ps.setInt(1, genUserId);
                ps.setString(2, data.getUserName());
                val = ps.executeUpdate();

                if (val == 0) {
                    con.rollback();
                    logger.logError("UserOperation", "createUser", "Failed to insert login credentials", null);
                    return null;
                }

                // Insert email data
                ps = con.prepareStatement("INSERT INTO Email_user VALUES (?, ?, ?);");
                ps.setInt(1, genUserId);
                ps.setString(2, data.getCurrentEmail());
                ps.setBoolean(3, true);
                val = ps.executeUpdate();

                if (val == 0) {
                    con.rollback();
                    logger.logError("UserOperation", "createUser", "Failed to insert email data", null);
                    return null;
                }
                data.setUserId(genUserId);
            }
            con.commit();
            logger.logInfo("UserOperation", "createUser", "User created successfully: " + data.getUserName());
            return data;

        } catch (Exception e) {
            logger.logError("UserOperation", "createUser", "Exception occurred: " + e.getMessage(), e);
            con.rollback(); // Ensure rollback in case of exception
        } finally {
            con.close();
        }
        return null;
    }

    /**
     * Verifies user credentials against the database.
     *
     * @param ud the UserData object containing login details
     * @return the verified UserData object or null if verification fails
     * @throws SQLException if a database access error occurs
     */
    public UserData isUser(UserData ud) throws SQLException {
        String[] email = new String[5];
        Connection con = DBconnection.getConnection();
        try {
            PreparedStatement ps;
            ResultSet val;

            if (ud.getUserName().contains("@")) {
                ps = con.prepareStatement(
                    "SELECT * FROM user_data ud LEFT JOIN Login_credentials lg ON ud.user_id = lg.id " +
                    "LEFT JOIN Email_user eu ON lg.id = eu.em_id WHERE email = ?;");
                ps.setString(1, ud.getUserName());
            } else {
                ps = con.prepareStatement(
                    "SELECT * FROM user_data ud LEFT JOIN Login_credentials lg ON ud.user_id = lg.id " +
                    "LEFT JOIN Email_user eu ON lg.id = eu.em_id WHERE username = ?;");
                ps.setString(1, ud.getUserName());
            }

            val = ps.executeQuery();
            if (val.next() && BCrypt.checkpw(ud.getPassword(), val.getString(3))) {
                ud.setUserId(val.getInt(1));
                ud.setName(val.getString(2));
                ud.setPhoneno(val.getString(4));
                ud.setAddress(val.getString(5));
                ud.setTimezone(val.getString(6));
                ud.setUserName(val.getString(8));
                ud.setCurrentEmail(val.getString(10));

                ps = con.prepareStatement("SELECT email, is_primary FROM Email_user WHERE em_id = ?;");
                ps.setInt(1, ud.getUserId());
                val = ps.executeQuery();
                int i = 0;
                while (val.next() && i < 5) {
                    email[i] = val.getString(1);
                    if (val.getBoolean(2)) {
                        ud.setPrimaryMail(val.getString(1));
                    }
                    i++;
                }
                ud.setEmail(email);
                logger.logInfo("UserOperation", "isUser", "User verified successfully: " + ud.getUserName());
                return ud;
            } else {
                logger.logError("UserOperation", "isUser", "Invalid credentials for user: " + ud.getUserName(), null);
                return null;
            }

        } catch (Exception e) {
            logger.logError("UserOperation", "isUser", "Exception occurred: " + e.getMessage(), e);
        } finally {
            con.close();
        }
        return null;
    }

    /**
     * Updates user data in the database.
     *
     * @param ud the UserData object containing updated details
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean userDataUpdate(UserData ud) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            String password = BCrypt.hashpw(ud.getPassword(), BCrypt.gensalt());
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                "UPDATE user_data SET Name = ?, phone_no = ?, address = ?, password = ?, timezone = ? WHERE user_id = ?;");
            ps.setString(1, ud.getName());
            ps.setString(2, ud.getPhoneno());
            ps.setString(3, ud.getAddress());
            ps.setString(4, password);
            ps.setString(5, ud.getTimezone());
            ps.setInt(6, ud.getUserId());
            int val = ps.executeUpdate();

            if (val == 0) {
                con.rollback();
                logger.logError("UserOperation", "userDataUpdate", "Failed to update user data", null);
                return false;
            }

            ps = con.prepareStatement("UPDATE Login_credentials SET username = ? WHERE id = ?;");
            ps.setString(1, ud.getUserName());
            ps.setInt(2, ud.getUserId());
            val = ps.executeUpdate();

            if (val == 0) {
                con.rollback();
                logger.logError("UserOperation", "userDataUpdate", "Failed to update login credentials", null);
                return false;
            }

            ps = con.prepareStatement("DELETE FROM Email_user WHERE em_id = ?;");
            ps.setInt(1, ud.getUserId());
            val = ps.executeUpdate();

            if (val == 0) {
                con.rollback();
                logger.logError("UserOperation", "userDataUpdate", "Failed to delete existing emails", null);
                return false;
            }

            for (String email : ud.getEmail()) {
                if (email != null) {
                    ps = con.prepareStatement("INSERT INTO Email_user VALUES (?, ?, ?);");
                    ps.setInt(1, ud.getUserId());
                    ps.setString(2, email);
                    ps.setBoolean(3, ud.getPrimaryMail().equals(email));
                    val = ps.executeUpdate();
                    if (val == 0) {
                        con.rollback();
                        logger.logError("UserOperation", "userDataUpdate", "Failed to insert new email: " + email, null);
                        return false;
                    }
                }
            }
            con.commit();
            logger.logInfo("UserOperation", "userDataUpdate", "User data updated successfully for: " + ud.getUserName());
            return true;

        } catch (Exception e) {
            logger.logError("UserOperation", "userDataUpdate", "Exception occurred: " + e.getMessage(), e);
            con.rollback(); // Ensure rollback in case of exception
        } finally {
            con.close();
        }
        return false;
    }

    /**
     * Deletes a user profile from the database.
     *
     * @param userId the ID of the user to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public Boolean deleteUserProfile(int userId) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM user_data WHERE user_id = ?;");
            ps.setInt(1, userId);
            int val = ps.executeUpdate();
            if (val != 0) {
                logger.logInfo("UserOperation", "deleteUserProfile", "User profile deleted successfully for userId: " + userId);
                return true;
            } else {
                logger.logError("UserOperation", "deleteUserProfile", "Failed to delete user profile for userId: " + userId, null);
                return false;
            }
        } catch (Exception e) {
            logger.logError("UserOperation", "deleteUserProfile", "Exception occurred: " + e.getMessage(), e);
        } finally {
            con.close();
        }
        return false;
    }

    /**
     * Retrieves user data by user ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the UserData object containing user details or null if not found
     * @throws SQLException if a database access error occurs
     */
    public UserData getUserData(int userId) throws SQLException {
        String[] email = new String[5];
        Connection con = DBconnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM user_data ud LEFT JOIN Login_credentials lg ON ud.user_id = lg.id WHERE user_id = ?;");
            ps.setInt(1, userId);
            ResultSet val = ps.executeQuery();
            UserData ud = new UserData();

            if (val.next()) {
                ud.setUserId(val.getInt(1));
                ud.setName(val.getString(2));
                ud.setPhoneno(val.getString(4));
                ud.setAddress(val.getString(5));
                ud.setTimezone(val.getString(6));
                ud.setUserName(val.getString(8));
               

                ps = con.prepareStatement("SELECT email, is_primary FROM Email_user WHERE em_id = ?;");
                ps.setInt(1, userId);
                val = ps.executeQuery();
                int i = 0;
                while (val.next() && i < 5) {
                    email[i] = val.getString(1);
                    if (val.getBoolean(2)) {
                        ud.setPrimaryMail(val.getString(1));
                    }
                    i++;
                }
                ud.setEmail(email);
                logger.logInfo("UserOperation", "getUserData", "User data retrieved successfully for userId: " + userId);
                return ud;
            } else {
                logger.logError("UserOperation", "getUserData", "No user found for userId: " + userId, null);
            }
        } catch (Exception e) {
            logger.logError("UserOperation", "getUserData", "Exception occurred: " + e.getMessage(), e);
        } finally {
            con.close();
        }
        return null;
    }
}
