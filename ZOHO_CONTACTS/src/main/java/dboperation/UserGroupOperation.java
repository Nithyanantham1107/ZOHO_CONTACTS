package dboperation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconnect.DBconnection;
import dbmodel.UserGroup;
import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.Table;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

public class UserGroupOperation {
	UserGroup ug;
	private LoggerSet logger = new LoggerSet();

	/**
	 * Creates a new user group in the database.
	 *
	 * @param category the UserGroup object containing the group details
	 * @return true if the group was created successfully, false otherwise
	 * @throws SQLException if a database access error occurs
	 */

	public boolean createGroup(dbpojo.Category category, int userID) throws SQLException {
		Connection con = DBconnection.getConnection();
		int[] result = { -1, -1 };
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {
//            con.setAutoCommit(false);
//            PreparedStatement ps = con.prepareStatement("INSERT INTO Category (Category_name, created_by) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
//            ps.setString(1, ug.getGroupName());
//            ps.setInt(2, ug.getUserid());
//            int val = ps.executeUpdate();'p0

			qg.openConnection();

//			result = qg.insert(tables.Category, Category.Category_name, Category.created_by, Category.created_time  ,Category.modified_time)
//					.valuesInsert(category.getCategoryName(), category.getCreatedBy(),category.getCreatedAt(), category.getModifiedAt())
//					.execute(Statement.RETURN_GENERATED_KEYS);

			result = qg.insert(category).execute(userID);
			if (result[0] == 0) {
//                con.rollback();
				qg.rollBackConnection();
				logger.logError("UserGroupOperation", "createGroup",
						"Failed to insert group: " + category.getCategoryName(), null);
				return false;
			}
//            ResultSet groups = ps.getGeneratedKeys();
//            int groupid;
//			if (result[1] != -1) {
//
//				category.setCategoryID(result[1]);
//			} else {
//				logger.logError("UserGroupOperation", "createGroup", "Failed to retrieve generated group ID", null);
//				return false;
//			}

//			for (CategoryRelation categoryRelation : category.getCategoryRelation()) {
//                ps = con.prepareStatement("INSERT INTO Category_relation VALUES (?, ?);");
//                ps.setInt(1, i);
//                ps.setInt(2, groupid);
//                val = ps.executeUpdate();
//				categoryRelation.setCategoryID(category.getCategoryID());
//
//				result = qg.insert(tables.Category_relation).valuesInsert(categoryRelation.getContactIDtoJoin(),
//						categoryRelation.getCategoryID(), categoryRelation.getCreatedAt()).execute(Statement.RETURN_GENERATED_KEYS);
//				if (result[0] == 0) {
//                    con.rollback();
//					qg.rollBackConnection();
//					logger.logError("UserGroupOperation", "createGroup", "Failed to insert contact ID: "
//							+ categoryRelation + " for group: " + category.getCategoryName(), null);
//					return false;
//				}

//			}

//            con.commit();
			qg.commit();
			logger.logInfo("UserGroupOperation", "createGroup",
					"Group created successfully: " + category.getCategoryName());
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

		ArrayList<Table> result = new ArrayList<>();
		ArrayList<dbpojo.Category> usergroups = new ArrayList<>();
//        Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
//		ArrayList<Table> data = new ArrayList<>();
		try {

			qg.openConnection();
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM Category WHERE created_by = ?;");
//            ps.setInt(1, userid);
//            ResultSet val = ps.executeQuery();

			dbpojo.Category category = new dbpojo.Category();
			category.setCreatedBY(userid);

//			data = qg.select(tables.Category).where(Category.created_by, Operation.Equal, userid).executeQuery();

			result = qg.select(category).executeQuery();
			if (result.size() > 0) {
				for (Table data : result) {

					usergroups.add((dbpojo.Category) data);
				}

			} else {
				logger.logInfo("UserGroupOperation", "viewAllGroup", " No Groups retrieved for user ID: " + userid);

				return null;
			}

//			for (Object i : data) {
//
//				if (i instanceof dbpojo.Category) {
//					usergroups.add((dbpojo.Category) i);
//				}
//
//			}

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
	 * @param category the ID of the group to delete
	 * @return true if the group was deleted successfully, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
	public Boolean deleteUserGroup(dbpojo.Category category, int userID) throws SQLException {
//        Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };
		try {
			qg.openConnection();
//            PreparedStatement ps = con.prepareStatement("DELETE FROM Category WHERE Category_id = ?;");
//            ps.setInt(1, groupid);
//            int val = ps.executeUpdate();
//			val = qg.delete(tables.Category).where(Category.Category_id, Operation.Equal, category.getCategoryID())
//					.and(Category.created_by, Operation.Equal, category.getCreatedBy()).execute();

			val = qg.delete(category).execute(userID);
			if (val[0] == 0) {
				logger.logError("UserGroupOperation", "deleteUserGroup", "Failed to delete group with ID: " + category,
						null);
				return false;
			}
			logger.logInfo("UserGroupOperation", "deleteUserGroup", "Group deleted successfully: " + category);
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
	 * @param groupId the ID of the group
	 * @param userId  the ID of the user
	 * @return an array of contact IDs or null if an error occurs
	 * @throws SQLException if a database access error occurs
	 */
	public ArrayList<CategoryRelation> viewUserGroupContact(int groupId, int userId) throws SQLException {
//        Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		try {

			qg.openConnection();

			ArrayList<Table> result = new ArrayList<>();
//			ArrayList<dbpojo.Category> categories = new ArrayList<>();
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM Category c LEFT JOIN Category_relation cr ON c.Category_id = cr.Category_id WHERE created_by = ? AND c.Category_id = ?;");
//            ps.setInt(1, userid);
//            ps.setInt(2, groupid);
//            ResultSet val = ps.executeQuery();

//            result=qg.select(tables.Category)
//            		.join(JoinType.left, Category.Category_id, Operation.Equal, Category_relation.Category_id)
//            		.where(Category.created_by, Operation.Equal, userid)
//            		.and(Category.Category_id,Operation.Equal,groupid)
//            		.executeQuery();
			dbpojo.Category category = new dbpojo.Category();
			category.setID(groupId);
			category.setCreatedBY(userId);
			CategoryRelation categoryRelation = new CategoryRelation();
			categoryRelation.setCategoryID(groupId);
			category.setCategoryRelation(categoryRelation);

//			result = qg.select(tables.Category_relation).where(Category_relation.Category_id, Operation.Equal, groupid)
//					.executeQuery();
			result = qg.select(category).executeQuery();

			if (result.size() > 0) {

				category = (dbpojo.Category) result.getFirst();

				logger.logInfo("UserGroupOperation", "viewUserGroupContact",
						"Contacts retrieved for group ID: " + groupId);
				return category.getCategoryRelation();
			} else {

				logger.logInfo("UserGroupOperation", "viewUserGroupContact",
						" NO Contacts retrieved for group ID: " + groupId);
				return null;
			}

//			for (Object i : result) {
//				data.add((CategoryRelation) i);
//			}
//            
//            while (val.next()) {
//                data.add(val.getInt(4));
//            }
//            int[] contactid = new int[data.size()];
//            for (int i = 0; i < data.size(); i++) {
//                contactid[i] = data.get(i);
//            }

		} catch (Exception e) {
			logger.logError("UserGroupOperation", "viewUserGroupContact", "Exception occurred: " + e.getMessage(), e);
		} finally {
//            con.close();
			qg.closeConnection();
		}
		return null;
	}

	public ArrayList<ContactDetails> getGroupContactList(int groupID, int userID, String method) throws SQLException {
//      Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		try {

			qg.openConnection();

			UserContactOperation contactOperation = new UserContactOperation();
			UserGroupOperation userGroupOperation = new UserGroupOperation();

			ArrayList<ContactDetails> userContacts = contactOperation.viewAllUserContacts(userID);

			ArrayList<CategoryRelation> categoryRelation = userGroupOperation.viewUserGroupContact(groupID, userID);

			ArrayList<ContactDetails> contactsInGroup = new ArrayList<ContactDetails>();
			ArrayList<ContactDetails> contactsNotInGroup = new ArrayList<ContactDetails>();
			Boolean state = true;
			for (ContactDetails contact : userContacts) {
				state = true;

				for (CategoryRelation relation : categoryRelation) {
					if (relation.getContactIDtoJoin() == contact.getID()) {

						contactsInGroup.add(contact);
						state = false;
						break;
					}

				}
				if (state) {

					contactsNotInGroup.add(contact);
				}
			}

			if (method.equals("view")) {

				return contactsInGroup;
			} else {

				return contactsNotInGroup;

			}

		} catch (Exception e) {
			logger.logError("UserGroupOperation", "viewUserGroupContact", "Exception occurred: " + e.getMessage(), e);
		} finally {

			qg.closeConnection();
		}
		return null;
	}

	public Category getSpecificGroup(int groupID, int userID) throws SQLException {
//      Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		ArrayList<Table> result = new ArrayList<Table>();
		try {

			qg.openConnection();

			Category group = new Category();
			group.setID(groupID);
			group.setCreatedBY(userID);
			CategoryRelation catr = new CategoryRelation();
			catr.setCategoryID(groupID);
			group.setCategoryRelation(catr);
			result = qg.select(group).executeQuery();
			if (result != null && result.size() > 0) {

				group = (Category) result.getFirst();

				return group;

			} else {

				return null;
			}

		} catch (Exception e) {
			logger.logError("UserGroupOperation", "viewUserGroupContact", "Exception occurred: " + e.getMessage(), e);
		} finally {
//          con.close();
			qg.closeConnection();
		}
		return null;
	}

	/**
	 * SS Updates a user group's information.
	 *
	 * @param category the UserGroup object containing updated group details
	 * @return true if the group was updated successfully, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
	public boolean updateUserGroup(dbpojo.Category category, int userID) throws SQLException {
//        Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] result = { -1, -1 };
		try {

			qg.openConnection();
//            con.setAutoCommit(false);
//            PreparedStatement ps = con.prepareStatement("UPDATE Category SET Category_name = ? WHERE Category_id = ?;");
//            ps.setString(1, ug.getGroupName());
//            ps.setInt(2, ug.getGroupid());
//            int val = ps.executeUpdate();

//			result = qg.update(tables.Category, Category.Category_name, Category.modified_time)
//					.valuesUpdate(category.getCategoryName(), category.getModifiedAt())
//					.where(Category.Category_id, Operation.Equal, category.getCategoryID()).execute();

//			CategoryR categoryRelation =new CategoryRelation();
			ArrayList<CategoryRelation> categoryRelations = category.getCategoryRelation();
			category.setCategoryRelationAll(null);
			System.out.println("here the data in update as follows" + categoryRelations.size());
			for (CategoryRelation dummy : categoryRelations) {
				System.out.println("the name " + categoryRelations.size());
				System.out.println("the id of the relation is as" + dummy.getID());
			}
			CategoryRelation categoryRelation = new CategoryRelation();
			categoryRelation.setCategoryID(category.getID());

			ArrayList<Table> data = qg.select(categoryRelation).executeQuery();

			for (Table relation : data) {

				qg.delete(relation).execute(userID);
			}
			for (Table relation : categoryRelations) {

				qg.insert(relation).execute(userID);
			}

			System.out.println("the group name  update is" + category.getCategoryName());

			result = qg.update(category).execute(userID);

			if (result[0] == 0) {
//                con.rollback();
				qg.rollBackConnection();
				logger.logError("UserGroupOperation", "updateUserGroup",
						"Failed to update group ID: " + category.getID(), null);
				return false;
			}

//            ps = con.prepareStatement("DELETE FROM Category_relation WHERE Category_id = ?;");
//            ps.setInt(1, ug.getGroupid());
//            val = ps.executeUpdate();

//			result = qg.delete(tables.Category_relation)
//					.where(Category_relation.Category_id, Operation.Equal, category.getCategoryID()).execute();
//            if (val[0] == 0) { 

//                con.rollback();
//            	qg.rollBackConnection();
//                logger.logError("UserGroupOperation", "updateUserGroup","Failed to delete existing relations for group ID: " + ug.getCategoryID(), null);
//                return false;
//            }

//			for (CategoryRelation categoryRelation : category.getCategoryRelation()) {
//                ps = con.prepareStatement("INSERT INTO Category_relation VALUES (?, ?);");
//                ps.setInt(1, i);
//                ps.setInt(2, ug.getGroupid());
//                val = ps.executeUpdate();

//				result = qg.insert(tables.Category_relation).valuesInsert(categoryRelation.getContactIDtoJoin(),
//						categoryRelation.getCategoryID(), categoryRelation.getCreatedAt()).execute();
//				if (result[0] == 0) {
//                    con.rollback();
//					qg.rollBackConnection();
//					logger.logError("UserGroupOperation", "updateUserGroup",
//							"Failed to insert contact ID: " + categoryRelation + " for group ID: " + category.getID(),
//							null);
//					return false;
//				}
//			}

//            con.commit();
			qg.commit();
			logger.logInfo("UserGroupOperation", "updateUserGroup", "Group updated successfully: " + category.getID());
			return true;
		} catch (Exception e) {
			logger.logError("UserGroupOperation", "updateUserGroup", "Exception occurred: " + e.getMessage(), e);
//            con.rollback(); 
			qg.rollBackConnection();
		} finally {
//            con.close();
			qg.closeConnection();
		}
		return false;
	}

	public Boolean removeGroupContacts(CategoryRelation categoryRelation, int userID) throws SQLException {
//      Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };
		CategoryRelation catrel = null;
		ArrayList<Table> result = new ArrayList<Table>();
		try {
			qg.openConnection();
//          PreparedStatement ps = con.prepareStatement("DELETE FROM Category WHERE Category_id = ?;");
//          ps.setInt(1, groupid);
//          int val = ps.executeUpdate();
//			val = qg.delete(tables.Category).where(Category.Category_id, Operation.Equal, category.getCategoryID())
//					.and(Category.created_by, Operation.Equal, category.getCreatedBy()).execute();

			result = qg.select(categoryRelation).executeQuery();
			if (result.size() > 0) {

				catrel = (CategoryRelation) result.getFirst();

				val = qg.delete(catrel).execute(userID);
				if (val[0] == -1) {
					logger.logError("UserGroupOperation", "removeGroupContacts",
							"Failed to remove contacts from group with ID: " + categoryRelation.getCategoryID(), null);
					return false;
				}
			} else {

				logger.logError("UserGroupOperation", "removeGroupContacts",
						"Failed to find contacts from group with ID: " + categoryRelation.getCategoryID(), null);
				return false;
			}

			logger.logInfo("UserGroupOperation", "removeGroupContacts",
					"Group  contacts removed successfully: " + categoryRelation.getCategoryID());
			return true;
		} catch (Exception e) {
			logger.logError("UserGroupOperation", "dremoveGroupContacts", "Exception occurred: " + e.getMessage(), e);
		} finally {
//          con.close();
			qg.closeConnection();
		}
		return false;
	}

	public Boolean addGroupContacts(CategoryRelation categoryRelation, int userID) throws SQLException {
//      Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };

		try {
			qg.openConnection();

			val = qg.insert(categoryRelation).execute(userID);
			if (val[0] == -1) {
				logger.logError("UserGroupOperation", "addGroupContacts",
						"Failed to remove contacts from group with ID: " + categoryRelation.getCategoryID(), null);
				return false;
			}

			logger.logInfo("UserGroupOperation", "addGroupContacts",
					"Group  contacts removed successfully: " + categoryRelation.getCategoryID());
			return true;
		} catch (Exception e) {
			logger.logError("UserGroupOperation", "addGroupContacts", "Exception occurred: " + e.getMessage(), e);
		} finally {
//          con.close();
			qg.closeConnection();
		}
		return false;
	}
}
