package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dbconnect.DBconnection;
import dbmodel.UserGroup;
import loggerfiles.LoggerSet;

public class UserGroupOperation {
    UserGroup ug;
    private LoggerSet logger = new LoggerSet();

    /**
     * Creates a new user group in the database.
     *
     * @param ug the UserGroup object containing the group details
     * @return true if the group was created successfully, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean createGroup(UserGroup ug) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("INSERT INTO Category (Category_name, created_by) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, ug.getGroupName());
            ps.setInt(2, ug.getUserid());
            int val = ps.executeUpdate();
            if (val == 0) {
                con.rollback();
                logger.logError("UserGroupOperation", "createGroup", "Failed to insert group: " + ug.getGroupName(), null);
                return false;
            }
            ResultSet groups = ps.getGeneratedKeys();
            int groupid;
            if (groups.next()) {
                groupid = groups.getInt(1);
            } else {
                logger.logError("UserGroupOperation", "createGroup", "Failed to retrieve generated group ID", null);
                return false;
            }

            for (int i : ug.getContacId()) {
                ps = con.prepareStatement("INSERT INTO Category_relation VALUES (?, ?);");
                ps.setInt(1, i);
                ps.setInt(2, groupid);
                val = ps.executeUpdate();
                if (val == 0) {
                    con.rollback();
                    logger.logError("UserGroupOperation", "createGroup", "Failed to insert contact ID: " + i + " for group: " + ug.getGroupName(), null);
                    return false;
                }
            }

            con.commit();
            logger.logInfo("UserGroupOperation", "createGroup", "Group created successfully: " + ug.getGroupName());
            return true;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "createGroup", "Exception occurred: " + e.getMessage(), e);
            con.rollback(); // Ensure rollback in case of exception
        } finally {
            con.close();
        }
        return false;
    }

    /**
     * Retrieves all groups created by a specific user.
     *
     * @param userid the ID of the user
     * @return a list of UserGroup objects or null if an error occurs
     * @throws SQLException if a database access error occurs
     */
    public ArrayList<UserGroup> viewAllGroup(int userid) throws SQLException {
        ArrayList<UserGroup> usergroups = new ArrayList<>();
        Connection con = DBconnection.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Category WHERE created_by = ?;");
            ps.setInt(1, userid);
            ResultSet val = ps.executeQuery();

            while (val.next()) {
                ug = new UserGroup();
                ug.setGroupid(val.getInt(1));
                ug.setGroupName(val.getString(2));
                ug.setUserid(val.getInt(3));
                usergroups.add(ug);
            }
            logger.logInfo("UserGroupOperation", "viewAllGroup", "Groups retrieved for user ID: " + userid);
            return usergroups;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "viewAllGroup", "Exception occurred: " + e.getMessage(), e);
        } finally {
            con.close();
        }
        return null;
    }

    /**
     * Deletes a user group by its ID.
     *
     * @param groupid the ID of the group to delete
     * @return true if the group was deleted successfully, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public Boolean deleteUserGroup(int groupid) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Category WHERE Category_id = ?;");
            ps.setInt(1, groupid);
            int val = ps.executeUpdate();
            if (val == 0) {
                logger.logError("UserGroupOperation", "deleteUserGroup", "Failed to delete group with ID: " + groupid, null);
                return false;
            }
            logger.logInfo("UserGroupOperation", "deleteUserGroup", "Group deleted successfully: " + groupid);
            return true;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "deleteUserGroup", "Exception occurred: " + e.getMessage(), e);
        } finally {
            con.close();
        }
        return false;
    }

    /**
     * Retrieves the contact IDs associated with a user group.
     *
     * @param groupid the ID of the group
     * @param userid the ID of the user
     * @return an array of contact IDs or null if an error occurs
     * @throws SQLException if a database access error occurs
     */
    public int[] viewUserGroupContact(int groupid, int userid) throws SQLException {
        Connection con = DBconnection.getConnection();

        try {
            ArrayList<Integer> data = new ArrayList<>();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Category c LEFT JOIN Category_relation cr ON c.Category_id = cr.Category_id WHERE created_by = ? AND c.Category_id = ?;");
            ps.setInt(1, userid);
            ps.setInt(2, groupid);
            ResultSet val = ps.executeQuery();
            while (val.next()) {
                data.add(val.getInt(4));
            }
            int[] contactid = new int[data.size()];
            for (int i = 0; i < data.size(); i++) {
                contactid[i] = data.get(i);
            }
            logger.logInfo("UserGroupOperation", "viewUserGroupContact", "Contacts retrieved for group ID: " + groupid);
            return contactid;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "viewUserGroupContact", "Exception occurred: " + e.getMessage(), e);
        } finally {
            con.close();
        }
        return null;
    }

    /**
     * Updates a user group's information.
     *
     * @param ug the UserGroup object containing updated group details
     * @return true if the group was updated successfully, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateUserGroup(UserGroup ug) throws SQLException {
        Connection con = DBconnection.getConnection();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("UPDATE Category SET Category_name = ? WHERE Category_id = ?;");
            ps.setString(1, ug.getGroupName());
            ps.setInt(2, ug.getGroupid());
            int val = ps.executeUpdate();
            if (val == 0) {
                con.rollback();
                logger.logError("UserGroupOperation", "updateUserGroup", "Failed to update group ID: " + ug.getGroupid(), null);
                return false;
            }

            ps = con.prepareStatement("DELETE FROM Category_relation WHERE Category_id = ?;");
            ps.setInt(1, ug.getGroupid());
            val = ps.executeUpdate();
            if (val == 0) {
                con.rollback();
                logger.logError("UserGroupOperation", "updateUserGroup", "Failed to delete existing relations for group ID: " + ug.getGroupid(), null);
                return false;
            }

            for (int i : ug.getContacId()) {
                ps = con.prepareStatement("INSERT INTO Category_relation VALUES (?, ?);");
                ps.setInt(1, i);
                ps.setInt(2, ug.getGroupid());
                val = ps.executeUpdate();
                if (val == 0) {
                    con.rollback();
                    logger.logError("UserGroupOperation", "updateUserGroup", "Failed to insert contact ID: " + i + " for group ID: " + ug.getGroupid(), null);
                    return false;
                }
            }

            con.commit();
            logger.logInfo("UserGroupOperation", "updateUserGroup", "Group updated successfully: " + ug.getGroupid());
            return true;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "updateUserGroup", "Exception occurred: " + e.getMessage(), e);
            con.rollback(); // Ensure rollback in case of exception
        } finally {
            con.close();
        }
        return false;
    }
}
