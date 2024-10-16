package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.sql.Timestamp;
import javax.servlet.http.Cookie;
import org.mindrot.jbcrypt.BCrypt;
import dbconnect.DBconnection;

/**
 * This class handles session operations such as generating session IDs,
 * retrieving session IDs from cookies, deleting session data, and checking
 * the status of a session.
 */
public class SessionOperation {

    /**
     * Generates a unique session ID for a user and stores it in the database.
     *
     * @param user_id the ID of the user
     * @return the generated session ID or null if an error occurs
     * @throws SQLException if a database access error occurs
     */
    public String generateSessionId(int user_id) throws SQLException {
        int sessiontimeout = 30 * 60 * 1000; // 30 minutes
        String uuid = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        String sessionplaintext = uuid + timestamp + user_id;
        String sessionid = BCrypt.hashpw(sessionplaintext, BCrypt.gensalt());
        Timestamp sessionexpire = new Timestamp(timestamp + sessiontimeout);
        System.out.println("Created session ID: " + sessionid);
        Connection con = DBconnection.getConnection();
        
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("INSERT INTO Session VALUES (?, ?, ?);");
            ps.setString(1, sessionid);
            ps.setTimestamp(2, sessionexpire);
            ps.setInt(3, user_id);
            int val = ps.executeUpdate();
            if (val == 0) {
                System.out.println("Error inserting into Session table");
                con.rollback();
                return null;
            }

            con.commit();
            return sessionid;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.close();
        }

        return null;
    }

    /**
     * Retrieves the session ID from the cookies.
     *
     * @param cookies the array of cookies from the HTTP request
     * @return the session ID if found, or null if not found
     */
    public String getCustomSessionId(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    System.out.println(cookie.getValue() + " - Cookie value found");
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Deletes the session data associated with the given session ID.
     *
     * @param sessionid the session ID to delete
     * @return true if the session was deleted successfully, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean DeleteSessionData(String sessionid) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            if (sessionid != null) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM Session WHERE session_id = ?;");
                ps.setString(1, sessionid);
                int val = ps.executeUpdate();
                if (val == 0) {
                    System.out.println("Error deleting the session");
                    return false;
                }
            } else {
                System.out.println("Session ID is null");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.close();
        }
        return false;
    }

    /**
     * Checks if a session is still alive and updates its expiration time.
     *
     * @param sessionid the session ID to check
     * @return the user ID associated with the session if alive, 0 if not
     * @throws SQLException if a database access error occurs
     */
    public int checkSessionAlive(String sessionid) throws SQLException {
        int userid = 0;
        Connection con = DBconnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Session WHERE session_id = ?");
            ps.setString(1, sessionid);
            ResultSet val = ps.executeQuery();
            Timestamp currenttime = new Timestamp(System.currentTimeMillis());
            if (val.next()) {
                userid = val.getInt(3);
                int timestatus = currenttime.compareTo(val.getTimestamp(2));
                if (timestatus > 0) {
                    return 0; 
                }
            }

            con.setAutoCommit(false);
            currenttime = new Timestamp(System.currentTimeMillis() + (30 * 60 * 1000)); // Extend session by 30 minutes
            System.out.println("Updated session expiration time: " + currenttime);
            ps = con.prepareStatement("UPDATE Session SET session_expire = ? WHERE session_id = ?");
            ps.setTimestamp(1, currenttime);
            ps.setString(2, sessionid);
            int result = ps.executeUpdate();
            if (result == 0) {
                System.out.println("Error updating the session table: " + sessionid);
                con.rollback();
                return 0;
            }

            return userid;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.close();
        }
        return 0;
    }
}
