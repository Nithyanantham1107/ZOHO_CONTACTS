package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dbconnect.DBconnection;
import dbmodel.UserGroup;
import dbpojo.CategoryRelation;
import loggerfiles.LoggerSet;
import querybuilder.QueryBuilder;
import querybuilder.SqlQueryLayer;
import querybuilder.TableSchema.Category;
import querybuilder.TableSchema.Category_relation;
import querybuilder.TableSchema.JoinType;
import querybuilder.TableSchema.Operation;
import querybuilder.TableSchema.Statement;
import querybuilder.TableSchema.tables;

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
    public boolean createGroup(dbpojo.Category ug) throws SQLException {
        Connection con = DBconnection.getConnection();
        int[] val= {-1,-1};
        QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
        try {
//            con.setAutoCommit(false);
//            PreparedStatement ps = con.prepareStatement("INSERT INTO Category (Category_name, created_by) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
//            ps.setString(1, ug.getGroupName());
//            ps.setInt(2, ug.getUserid());
//            int val = ps.executeUpdate();
        	
        	qg.openConnection();
        	
        	 val=qg.insert(tables.Category, Category.Category_name,Category.created_by)
        			 .valuesInsert(ug.getCategoryName(),ug.getCreatedBy())
        			 .execute(Statement.RETURN_GENERATED_KEYS);
            if (val[0] == 0) {
//                con.rollback();
            	qg.rollBackConnection();
                logger.logError("UserGroupOperation", "createGroup", "Failed to insert group: " + ug.getCategoryName(), null);
                return false;
            }
//            ResultSet groups = ps.getGeneratedKeys();
            int groupid;
            if (val[1]!=-1) {
                groupid = val[1];
            } else {
                logger.logError("UserGroupOperation", "createGroup", "Failed to retrieve generated group ID", null);
                return false;
            }

            for (CategoryRelation i : ug.getCategoryRelation()) {
//                ps = con.prepareStatement("INSERT INTO Category_relation VALUES (?, ?);");
//                ps.setInt(1, i);
//                ps.setInt(2, groupid);
//                val = ps.executeUpdate();
            	
            	val=qg.insert(tables.Category_relation)
            			.valuesInsert(i.getContactIDtoJoin(),groupid)
            			.execute();
                if (val[0] == 0) {
//                    con.rollback();
                	qg.rollBackConnection();
                    logger.logError("UserGroupOperation", "createGroup", "Failed to insert contact ID: " + i + " for group: " + ug.getCategoryName(), null);
                    return false;
                }
            }

//            con.commit();
            qg.commit();
            logger.logInfo("UserGroupOperation", "createGroup", "Group created successfully: " + ug.getCategoryName());
            return true;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "createGroup", "Exception occurred: " + e.getMessage(), e);
            con.rollback(); // Ensure rollback in case of exception
        } finally {
//            con.close();
        	qg.closeConnection();
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
    public ArrayList<dbpojo.Category> viewAllGroup(int userid) throws SQLException {
        ArrayList<dbpojo.Category> usergroups = new ArrayList<>();
//        Connection con = DBconnection.getConnection();
        QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
        ArrayList<Object> data = new ArrayList<>();
        try {
        	
        	qg.openConnection();
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM Category WHERE created_by = ?;");
//            ps.setInt(1, userid);
//            ResultSet val = ps.executeQuery();
        	data=qg.select(tables.Category).where(Category.Category_id,Operation.Equal, userid).executeQuery();
        	
for(Object i :data) {
	
	
	if(i instanceof dbpojo.Category) {
		usergroups.add((dbpojo.Category) i);
	}
	
}
        	
        	
//            while (val.next()) {
//                ug = new UserGroup();
//                ug.setGroupid(val.getInt(1));
//                ug.setGroupName(val.getString(2));
//                ug.setUserid(val.getInt(3));
//                usergroups.add(ug);
//            }
            logger.logInfo("UserGroupOperation", "viewAllGroup", "Groups retrieved for user ID: " + userid);
            return usergroups;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "viewAllGroup", "Exception occurred: " + e.getMessage(), e);
        } finally {
//            con.close();
        	qg.closeConnection();
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
//        Connection con = DBconnection.getConnection();
    	QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
    	int[] val= {-1,-1};
        try {
        	qg.openConnection();
//            PreparedStatement ps = con.prepareStatement("DELETE FROM Category WHERE Category_id = ?;");
//            ps.setInt(1, groupid);
//            int val = ps.executeUpdate();
          	val=qg.Delete(tables.Category)
        			.where(Category.Category_id, Operation.Equal, groupid)
        			.execute();
            if (val[0] == 0) {
                logger.logError("UserGroupOperation", "deleteUserGroup", "Failed to delete group with ID: " + groupid, null);
                return false;
            }
            logger.logInfo("UserGroupOperation", "deleteUserGroup", "Group deleted successfully: " + groupid);
            return true;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "deleteUserGroup", "Exception occurred: " + e.getMessage(), e);
        } finally {
//            con.close();
        	qg.closeConnection();
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
    public ArrayList<CategoryRelation> viewUserGroupContact(int groupid, int userid) throws SQLException {
//        Connection con = DBconnection.getConnection();
        QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
    	

        try {
            ArrayList<Object> result = new ArrayList<>();
            ArrayList<CategoryRelation> data=new ArrayList<>();
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM Category c LEFT JOIN Category_relation cr ON c.Category_id = cr.Category_id WHERE created_by = ? AND c.Category_id = ?;");
//            ps.setInt(1, userid);
//            ps.setInt(2, groupid);
//            ResultSet val = ps.executeQuery();
            
            
//            result=qg.select(tables.Category)
//            		.join(JoinType.left, Category.Category_id, Operation.Equal, Category_relation.Category_id)
//            		.where(Category.created_by, Operation.Equal, userid)
//            		.and(Category.Category_id,Operation.Equal,groupid)
//            		.executeQuery();
            
            result=qg.select(tables.Category_relation)
            		.where(Category_relation.Category_id, Operation.Equal, groupid)
            		.executeQuery();
            
            
            
            for(Object i: result) {
            	data.add((CategoryRelation)i);
            }
//            
//            while (val.next()) {
//                data.add(val.getInt(4));
//            }
//            int[] contactid = new int[data.size()];
//            for (int i = 0; i < data.size(); i++) {
//                contactid[i] = data.get(i);
//            }
            logger.logInfo("UserGroupOperation", "viewUserGroupContact", "Contacts retrieved for group ID: " + groupid);
            return data;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "viewUserGroupContact", "Exception occurred: " + e.getMessage(), e);
        } finally {
//            con.close();
        	qg.closeConnection();
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
    public boolean updateUserGroup(dbpojo.Category ug) throws SQLException {
//        Connection con = DBconnection.getConnection();
    	QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
    	int[] val= {-1,-1};
        try {
        	
        	qg.openConnection();
//            con.setAutoCommit(false);
//            PreparedStatement ps = con.prepareStatement("UPDATE Category SET Category_name = ? WHERE Category_id = ?;");
//            ps.setString(1, ug.getGroupName());
//            ps.setInt(2, ug.getGroupid());
//            int val = ps.executeUpdate();
        	
        	
        	 val=qg.update(tables.Category, Category.Category_name)
        			.valuesUpdate(ug.getCategoryName())
        			.where(Category.Category_id, Operation.Equal, ug.getCategoryID()).execute();
            
            if (val[0] == 0) {
//                con.rollback();
            	qg.rollBackConnection();
                logger.logError("UserGroupOperation", "updateUserGroup", "Failed to update group ID: " + ug.getCategoryID(), null);
                return false;
            }

//            ps = con.prepareStatement("DELETE FROM Category_relation WHERE Category_id = ?;");
//            ps.setInt(1, ug.getGroupid());
//            val = ps.executeUpdate();
            
            
            val=qg.Delete(tables.Category_relation)
            		.where(Category_relation.Category_id, Operation.Equal,ug.getCategoryID())
            		.execute();
            if (val[0] == 0) { 
            	
//                con.rollback();
            	qg.rollBackConnection();
                logger.logError("UserGroupOperation", "updateUserGroup", "Failed to delete existing relations for group ID: " + ug.getCategoryID(), null);
                return false;
            }

            for (CategoryRelation i : ug.getCategoryRelation()) {
//                ps = con.prepareStatement("INSERT INTO Category_relation VALUES (?, ?);");
//                ps.setInt(1, i);
//                ps.setInt(2, ug.getGroupid());
//                val = ps.executeUpdate();
            	
            	
            	
            	val=qg.insert(tables.Category_relation)
            			.valuesInsert(i.getContactIDtoJoin(),ug.getCategoryID())
            			.execute();
                if (val[0] == 0) {
//                    con.rollback();
                	qg.rollBackConnection();
                    logger.logError("UserGroupOperation", "updateUserGroup", "Failed to insert contact ID: " + i + " for group ID: " + ug.getCategoryID(), null);
                    return false;
                }
            }

//            con.commit();
            qg.commit();
            logger.logInfo("UserGroupOperation", "updateUserGroup", "Group updated successfully: " + ug.getCategoryID());
            return true;
        } catch (Exception e) {
            logger.logError("UserGroupOperation", "updateUserGroup", "Exception occurred: " + e.getMessage(), e);
//            con.rollback(); // Ensure rollback in case of exception
            qg.rollBackConnection();
        } finally {
//            con.close();
        	qg.closeConnection();
        }
        return false;
    }
}
