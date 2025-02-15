package dboperation;

import java.sql.SQLException;
import java.util.ArrayList;

import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.Table;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

public class UserGroupOperation {

	private static LoggerSet logger = new LoggerSet();

	/**
	 * Creates a new user group in the database.
	 *
	 * @param category the UserGroup object containing the group details
	 * @return true if the group was created successfully, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */

	public static boolean createGroup(dbpojo.Category category, long userID) throws DBOperationException {

		int[] result = { -1, -1 };
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {

			qg.openConnection();

			result = qg.insert(category).execute(userID);
			if (result[0] == 0) {

				qg.rollBackConnection();
				logger.logError("UserGroupOperation", "createGroup",
						"Failed to insert group: " + category.getCategoryName(), null);
				return false;
			}

			qg.commit();
			logger.logInfo("UserGroupOperation", "createGroup",
					"Group created successfully: " + category.getCategoryName());
			return true;
		} catch (Exception e) {
			logger.logError("UserGroupOperation", "createGroup", "Exception occurred: " + e.getMessage(), e);
			qg.rollBackConnection();
			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	/**
	 * Retrieves all groups created by a specific user.
	 *
	 * @param userid the ID of the user
	 * @return a list of UserGroup objects or null if an error occurs
	 * @throws DBOperationException
	 * @throws SQLException         if a database access error occurs
	 */
	public static ArrayList<dbpojo.Category> viewAllGroup(long userid) throws DBOperationException {

		ArrayList<Table> result = new ArrayList<>();
		ArrayList<dbpojo.Category> usergroups = new ArrayList<>();

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {

			qg.openConnection();

			dbpojo.Category category = new dbpojo.Category();
			category.setCreatedBY(userid);

			result = qg.select(category).executeQuery();
			if (result.size() > 0) {
				for (Table data : result) {

					usergroups.add((dbpojo.Category) data);
				}

			} else {
				logger.logInfo("UserGroupOperation", "viewAllGroup", " No Groups retrieved for user ID: " + userid);

				return null;
			}

			logger.logInfo("UserGroupOperation", "viewAllGroup", "Groups retrieved for user ID: " + userid);
			return usergroups;
		} catch (Exception e) {
			logger.logError("UserGroupOperation", "viewAllGroup", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	/**
	 * Deletes a user group by its ID.
	 *
	 * @param category the ID of the group to delete
	 * @return true if the group was deleted successfully, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static Boolean deleteUserGroup(dbpojo.Category category, long userID) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };
		try {
			qg.openConnection();

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

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	/**
	 * Retrieves the contact IDs associated with a user group.
	 *
	 * @param groupId the ID of the group
	 * @param userId  the ID of the user
	 * @return an array of contact IDs or null if an error occurs
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static ArrayList<CategoryRelation> viewUserGroupContact(long groupId, long userId)
			throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		try {

			qg.openConnection();

			ArrayList<Table> result = new ArrayList<>();
			dbpojo.Category category = new dbpojo.Category();
			category.setID(groupId);
			category.setCreatedBY(userId);
			CategoryRelation categoryRelation = new CategoryRelation();
			categoryRelation.setCategoryID(groupId);
			category.setCategoryRelation(categoryRelation);

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

		} catch (Exception e) {
			logger.logError("UserGroupOperation", "viewUserGroupContact", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	public static ArrayList<ContactDetails> getGroupContactList(long groupID, long userID, String method)
			throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		try {

			qg.openConnection();

			ArrayList<ContactDetails> userContacts = UserContactOperation.viewAllUserContacts(userID);

			ArrayList<CategoryRelation> categoryRelation = UserGroupOperation.viewUserGroupContact(groupID, userID);
			System.out.println("the the relation size" + categoryRelation.getFirst().getID());
			ArrayList<ContactDetails> contactsInGroup = new ArrayList<ContactDetails>();
			ArrayList<ContactDetails> contactsNotInGroup = new ArrayList<ContactDetails>();
			Boolean state = true;
			for (ContactDetails contact : userContacts) {
				state = true;

				for (CategoryRelation relation : categoryRelation) {
					System.out.println("the group contact ID is:" + contact.getID()+"then teh id from relation"+relation.getContactIDtoJoin()+"then the gorup is "+relation.getCategoryID());
					
					
					
					if (relation.getContactIDtoJoin() == contact.getID()) {
						
						System.out.println("the in in group contact" + contact.getID());
						contactsInGroup.add(contact);
						state = false;
						break;
					}

				}
				if (state) {
					System.out.println("the Not  in in group contact" + contact.getID());

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
			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	public static Category getSpecificGroup(long groupID, long userID) throws DBOperationException {

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

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	/**
	 * SS Updates a user group's information.
	 *
	 * @param category the UserGroup object containing updated group details
	 * @return true if the group was updated successfully, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static boolean updateUserGroup(dbpojo.Category category, long userID) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] result = { -1, -1 };
		try {

			qg.openConnection();

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

				qg.rollBackConnection();
				logger.logError("UserGroupOperation", "updateUserGroup",
						"Failed to update group ID: " + category.getID(), null);
				return false;
			}

			qg.commit();
			logger.logInfo("UserGroupOperation", "updateUserGroup", "Group updated successfully: " + category.getID());
			return true;
		} catch (Exception e) {
			logger.logError("UserGroupOperation", "updateUserGroup", "Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	public static Boolean removeGroupContacts(CategoryRelation categoryRelation, long userID)
			throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };
		CategoryRelation catrel = null;
		ArrayList<Table> result = new ArrayList<Table>();
		try {
			qg.openConnection();

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

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	public static Boolean addGroupContacts(CategoryRelation categoryRelation, long userID) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };

		try {
			qg.openConnection();

			val = qg.insert(categoryRelation).execute(userID);
			if (val[0] == -1) {
				logger.logError("UserGroupOperation", "addGroupContacts",
						"Failed to add contacts from group with ID: " + categoryRelation.getCategoryID(), null);
				return false;
			}

			logger.logInfo("UserGroupOperation", "addGroupContacts",
					"Group  contacts added successfully: " + categoryRelation.getCategoryID());
			return true;
		} catch (Exception e) {
			logger.logError("UserGroupOperation", "addGroupContacts", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}
}
