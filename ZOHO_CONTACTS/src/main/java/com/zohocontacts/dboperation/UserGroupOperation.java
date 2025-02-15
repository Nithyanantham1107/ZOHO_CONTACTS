package com.zohocontacts.dboperation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dbpojo.Category;
import com.zohocontacts.dbpojo.CategoryRelation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;

public class UserGroupOperation {

	/**
	 * Creates a new user group in the database.
	 *
	 * @param category the UserGroup object containing the group details
	 * @return true if the group was created successfully, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */

	public static boolean createGroup(com.zohocontacts.dbpojo.Category category, long userID)
			throws DBOperationException {

		int[] result = { -1, -1 };
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			result = query.insert(category).execute(userID);
			if (result[0] == 0) {

				query.rollBackConnection();
				LoggerSet.logError("UserGroupOperation", "createGroup",
						"Failed to insert group: " + category.getCategoryName(), null);
				return false;
			}

			query.commit();
			LoggerSet.logInfo("UserGroupOperation", "createGroup",
					"Group created successfully: " + category.getCategoryName());
			return true;
		} catch (Exception e) {
			LoggerSet.logError("UserGroupOperation", "createGroup", "Exception occurred: " + e.getMessage(), e);
			throw new DBOperationException(e.getMessage());

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
	public static List<com.zohocontacts.dbpojo.Category> viewAllGroup(long userid) throws DBOperationException {

		List<Table> resultList = new ArrayList<>();
		List<com.zohocontacts.dbpojo.Category> usergroups = new ArrayList<>();

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			com.zohocontacts.dbpojo.Category category = new com.zohocontacts.dbpojo.Category();
			category.setCreatedBY(userid);

			resultList = query.select(category).executeQuery();
			if (resultList.size() > 0) {
				for (Table table : resultList) {

					usergroups.add((com.zohocontacts.dbpojo.Category) table);
				}

			} else {
				LoggerSet.logInfo("UserGroupOperation", "viewAllGroup", " No Groups retrieved for user ID: " + userid);

				return null;
			}

			LoggerSet.logInfo("UserGroupOperation", "viewAllGroup", "Groups retrieved for user ID: " + userid);
			return usergroups;
		} catch (Exception e) {
			LoggerSet.logError("UserGroupOperation", "viewAllGroup", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

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
	public static Boolean deleteUserGroup(com.zohocontacts.dbpojo.Category category, long userID)
			throws DBOperationException {
		int[] result = { -1, -1 };
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			result = query.delete(category).execute(userID);
			if (result[0] == 0) {
				LoggerSet.logError("UserGroupOperation", "deleteUserGroup",
						"Failed to delete group with ID: " + category, null);
				return false;
			}
			LoggerSet.logInfo("UserGroupOperation", "deleteUserGroup", "Group deleted successfully: " + category);
			return true;
		} catch (Exception e) {
			LoggerSet.logError("UserGroupOperation", "deleteUserGroup", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

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
	public static List<CategoryRelation> viewUserGroupContact(long groupId, long userId) throws DBOperationException {

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			List<Table> resultList = new ArrayList<>();
			com.zohocontacts.dbpojo.Category category = new com.zohocontacts.dbpojo.Category();
			category.setID(groupId);
			category.setCreatedBY(userId);
			CategoryRelation categoryRelation = new CategoryRelation();
			categoryRelation.setCategoryID(groupId);
			category.setCategoryRelation(categoryRelation);

			resultList = query.select(category).executeQuery();

			if (resultList.size() > 0) {

				category = (com.zohocontacts.dbpojo.Category) resultList.getFirst();

				LoggerSet.logInfo("UserGroupOperation", "viewUserGroupContact",
						"Contacts retrieved for group ID: " + groupId);
				return category.getCategoryRelation();
			} else {

				LoggerSet.logInfo("UserGroupOperation", "viewUserGroupContact",
						" NO Contacts retrieved for group ID: " + groupId);
				return null;
			}

		} catch (Exception e) {
			LoggerSet.logError("UserGroupOperation", "viewUserGroupContact", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException(e.getMessage());

		}

	}

	public static List<ContactDetails> getGroupContactList(long groupID, long userID, String method)
			throws DBOperationException {

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			List<ContactDetails> userContacts = UserContactOperation.viewAllUserContacts(userID);
			List<CategoryRelation> categoryRelation = UserGroupOperation.viewUserGroupContact(groupID, userID);
			List<ContactDetails> contactsInGroup = new ArrayList<ContactDetails>();
			List<ContactDetails> contactsNotInGroup = new ArrayList<ContactDetails>();
			Boolean isExist = true;
			for (ContactDetails contact : userContacts) {
				isExist = true;

				for (CategoryRelation relation : categoryRelation) {

					if (relation.getContactIDtoJoin() == contact.getID()) {

						contactsInGroup.add(contact);
						isExist = false;
						break;
					}

				}
				if (isExist) {

					contactsNotInGroup.add(contact);
				}
			}

			if (method.equals("view")) {

				return contactsInGroup;
			} else {

				return contactsNotInGroup;

			}

		} catch (Exception e) {
			LoggerSet.logError("UserGroupOperation", "viewUserGroupContact", "Exception occurred: " + e.getMessage(),
					e);
			throw new DBOperationException(e.getMessage());
		}
	}

	public static Category getSpecificGroup(long groupID, long userID) throws DBOperationException {

		List<Table> resultList = new ArrayList<Table>();
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			Category category = new Category();
			category.setID(groupID);
			category.setCreatedBY(userID);
			CategoryRelation categoryRelation = new CategoryRelation();
			categoryRelation.setCategoryID(groupID);
			category.setCategoryRelation(categoryRelation);
			resultList = query.select(category).executeQuery();
			if (resultList != null && resultList.size() > 0) {

				category = (Category) resultList.getFirst();

				return category;

			} else {

				return null;
			}

		} catch (Exception e) {
			LoggerSet.logError("UserGroupOperation", "viewUserGroupContact", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException(e.getMessage());

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
	public static boolean updateUserGroup(com.zohocontacts.dbpojo.Category category, long userID)
			throws DBOperationException {

		int[] result = { -1, -1 };
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			List<CategoryRelation> categoryRelations = category.getCategoryRelation();
			category.setCategoryRelationAll(null);

			CategoryRelation categoryRelation = new CategoryRelation();
			categoryRelation.setCategoryID(category.getID());

			List<Table> data = query.select(categoryRelation).executeQuery();

			for (Table relation : data) {

				query.delete(relation).execute(userID);
			}
			for (Table relation : categoryRelations) {

				query.insert(relation).execute(userID);
			}

			result = query.update(category).execute(userID);

			if (result[0] == 0) {

				query.rollBackConnection();
				LoggerSet.logError("UserGroupOperation", "updateUserGroup",
						"Failed to update group ID: " + category.getID(), null);
				return false;
			}

			query.commit();
			LoggerSet.logInfo("UserGroupOperation", "updateUserGroup",
					"Group updated successfully: " + category.getID());
			return true;
		} catch (Exception e) {
			LoggerSet.logError("UserGroupOperation", "updateUserGroup", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static Boolean removeGroupContacts(CategoryRelation categoryRelation, long userID)
			throws DBOperationException {
		int[] result = { -1, -1 };
		CategoryRelation dbCategoryRelation = null;
		List<Table> resultList = new ArrayList<Table>();
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			resultList = query.select(categoryRelation).executeQuery();
			if (resultList.size() > 0) {

				dbCategoryRelation = (CategoryRelation) resultList.getFirst();

				result = query.delete(dbCategoryRelation).execute(userID);
				if (result[0] == -1) {
					LoggerSet.logError("UserGroupOperation", "removeGroupContacts",
							"Failed to remove contacts from group with ID: " + categoryRelation.getCategoryID(), null);
					return false;
				}
			} else {

				LoggerSet.logError("UserGroupOperation", "removeGroupContacts",
						"Failed to find contacts from group with ID: " + categoryRelation.getCategoryID(), null);
				return false;
			}

			LoggerSet.logInfo("UserGroupOperation", "removeGroupContacts",
					"Group  contacts removed successfully: " + categoryRelation.getCategoryID());
			return true;
		} catch (Exception e) {
			LoggerSet.logError("UserGroupOperation", "dremoveGroupContacts", "Exception occurred: " + e.getMessage(),
					e);

			throw new DBOperationException(e.getMessage());

		}

	}

	public static Boolean addGroupContacts(CategoryRelation categoryRelation, long userID) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			result = query.insert(categoryRelation).execute(userID);
			if (result[0] == -1) {
				LoggerSet.logError("UserGroupOperation", "addGroupContacts",
						"Failed to add contacts from group with ID: " + categoryRelation.getCategoryID(), null);
				return false;
			}

			LoggerSet.logInfo("UserGroupOperation", "addGroupContacts",
					"Group  contacts added successfully: " + categoryRelation.getCategoryID());
			return true;
		} catch (Exception e) {
			LoggerSet.logError("UserGroupOperation", "addGroupContacts", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}
}
