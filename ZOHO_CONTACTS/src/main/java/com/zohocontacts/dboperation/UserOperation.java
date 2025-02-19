package com.zohocontacts.dboperation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dbpojo.EmailUser;
import com.zohocontacts.dbpojo.LoginCredentials;
import com.zohocontacts.dbpojo.Oauth;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.exception.QueryBuilderException;
import com.zohocontacts.loggerfiles.LoggerSet;

public class UserOperation {

	/**
	 * Creates a new user in the database.
	 *
	 * @param userData the UserData object containing user details
	 * @return the created UserData object or null if an error occurs
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static UserData createUser(UserData userData) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			String password = BCrypt.hashpw(userData.getPassword(), BCrypt.gensalt());

			query.openConnection();
			userData.setPassword(password);

			result = query.insert(userData).execute(-1);

			if (result[0] == 0) {

				query.rollBackConnection();
				LoggerSet.logError("UserOperation", "createUser", "Failed to insert user data", null);
				return null;
			}
			query.commit();
			LoggerSet.logInfo("UserOperation", "createUser",
					"User created successfully: " + userData.getLoginCredentials().getUserName());
			return userData;

		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "createUser", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		} catch (Exception e) {
			LoggerSet.logError("UserOperation", "createUser", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static EmailUser addEmail(EmailUser email, long userID) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			result = query.insert(email).execute(userID);

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("UserOperation", "addEmail", "Failed to insert user data", null);
				return null;
			}
			query.commit();
			LoggerSet.logInfo("UserOperation", "addEmail", "Email added successfully: " + email.getEmail());

			return email;

		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "addEmail", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		} catch (Exception e) {
			LoggerSet.logError("UserOperation", "addEmail", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	/**
	 * Verifies user credentials against the database.
	 *
	 * @param ud the UserData object containing login details
	 * @return the verified UserData object or null if verification fails
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static UserData isUser(String userName, String password) throws DBOperationException {

		List<Table> resultList = new ArrayList<Table>();

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			UserData userData = new UserData();

			if (userName.contains("@")) {

				EmailUser email = new EmailUser();

				email.setEmail(userName);

				resultList = query.select(email).executeQuery();

				if (resultList.size() > 0) {
					email = (EmailUser) resultList.getFirst();
					Oauth oauth = new Oauth();
					LoginCredentials login = new LoginCredentials();
					login.setUserID(email.getEmailId());
					userData.setLoginCredentials(login);
					userData.setEmail(email);
					userData.setOauth(oauth);

					userData.setID(email.getEmailId());

					resultList = query.select(userData).executeQuery();

					if (resultList.size() > 0) {

						userData = (UserData) resultList.getFirst();
					} else {
						LoggerSet.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName,
								null);
						return null;

					}

				} else {
					LoggerSet.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName, null);
					return null;

				}

			} else {

				LoginCredentials login = new LoginCredentials();
				login.setUserName(userName);

				resultList = query.select(login).executeQuery();

				if (resultList.size() > 0) {
					login = (LoginCredentials) resultList.getFirst();
					EmailUser email = new EmailUser();
					email.setEmailID(login.getUserId());
					Oauth oauth = new Oauth();
					userData.setOauth(oauth);
					userData.setLoginCredentials(login);
					userData.setEmail(email);

					userData.setID(login.getUserId());

					resultList = query.select(userData).executeQuery();

					if (resultList.size() > 0) {

						userData = (UserData) resultList.getFirst();
					} else {
						LoggerSet.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName,
								null);
						return null;

					}

				} else {
					LoggerSet.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName, null);
					return null;

				}

			}

			if (resultList.size() > 0 && BCrypt.checkpw(password, userData.getPassword())) {

				LoggerSet.logInfo("UserOperation", "isUser", "User verified successfully: " + userName);
				return userData;
			} else {
				LoggerSet.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName, null);
				return null;
			}

		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "isUser", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		} catch (Exception e) {
			LoggerSet.logError("UserOperation", "isUser", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		}
	}

	/**
	 * Updates user data in the database.
	 *
	 * @param userData the UserData object containing updated details
	 * @return true if the update was successful, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static boolean userDataUpdate(UserData userData, String newPassword) throws DBOperationException {

		UserData user;
		long userId = userData.getID();
		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();
			EmailUser emailData = new EmailUser();
			emailData.setEmailID(userId);

			List<Table> resultList = query.select(emailData).executeQuery();
			if (resultList.size() > 0) {

				for (Table email : resultList) {
					query.delete(email).execute(userId);

				}

			} else {
				query.rollBackConnection();
				LoggerSet.logError("UserOperation", "userDataUpdate", "Failed to delete existing emails", null);
				return false;

			}

			for (EmailUser email : userData.getallemail()) {
				if (email != null) {

					email.setEmailID(userData.getID());

					result = query.insert(email).execute(userId);
					if (result[0] == -1) {

						query.rollBackConnection();
						LoggerSet.logError("UserOperation", "userDataUpdate", "Failed to insert new email: " + email,
								null);
						return false;
					}
				}
			}

			if (userData.getPassword() == null || newPassword == null) {

				result = query.update(userData).execute(userId);

			} else {

				List<Table> users = query.select(userData).executeQuery();
				if (users.size() > 0) {

					user = (UserData) users.getFirst();
				} else {

					query.rollBackConnection();
					LoggerSet.logError("UserOperation", "userDataUpdate", "Failed to find  existing userData", null);
					return false;

				}

				if (BCrypt.checkpw(userData.getPassword(), user.getPassword())) {

					String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
					userData.setPassword(password);

					result = query.update(userData).execute(userId);

				} else {
					result[0] = -1;
					LoggerSet.logError("UserOperation", "userDataUpdate", "Incorrect password", null);

				}

			}

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("UserOperation", "userDataUpdate", "Failed to update user data", null);
				return false;
			}

			query.commit();
			LoggerSet.logInfo("UserOperation", "userDataUpdate",
					"User data updated successfully for: " + userData.getLoginCredentials().getUserName());
			return true;

		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "userDataUpdate", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserOperation", "userDataUpdate", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}
	}

	public static boolean userprofileUpdate(UserData userData, EmailUser emailData) throws DBOperationException {

		long userId = userData.getID();
		int[] result = { -1, -1 };
		List<Table> emails = new ArrayList<Table>();

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			EmailUser email = new EmailUser();
			email.setEmailID(userId);

			emails = query.select(email).executeQuery();

			if (emails.size() > 0) {

				for (Table table : emails) {
					EmailUser currentEmail = (EmailUser) table;

					EmailUser userEmail = new EmailUser();
					userEmail.setID(table.getID());

					if (currentEmail.getEmail().equals(emailData.getEmail())) {

						userEmail.setIsPrimary(true);

					} else {

						userEmail.setIsPrimary(false);
					}

					query.update(userEmail).execute(userId);

				}
			}

			result = query.update(userData).execute(userId);

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("UserOperation", "userprofileUpdate", "Failed to update user data", null);
				return false;
			}

			query.commit();
			LoggerSet.logInfo("UserOperation", "userprofileUpdate",
					"User data updated successfully for: " + userData.getLoginCredentials().getUserName());
			return true;

		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "userprofileUpdate", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserOperation", "userprofileUpdate", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	/**
	 * Deletes a user profile from the database.
	 *
	 * @param userId the ID of the user to delete
	 * @return true if the deletion was successful, false otherwise
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static Boolean deleteUserProfile(UserData userData) throws DBOperationException {

		long userId = userData.getID();
		int[] result = { -1, -1 };
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			result = query.delete(userData).execute(userId);

			if (result[0] != -1) {
				LoggerSet.logInfo("UserOperation", "deleteUserProfile",
						"User profile deleted successfully for userId: " + userData.getID());
				return true;
			} else {
				LoggerSet.logError("UserOperation", "deleteUserProfile",
						"Failed to delete user profile for userId: " + userData.getID(), null);
				return false;
			}
		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "deleteUserProfile", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserOperation", "deleteUserProfile", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		}

	}

	public static List<EmailUser> getAllEmail(EmailUser email) throws DBOperationException {

		List<Table> resultList = new ArrayList<Table>();
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			resultList = query.select(email).executeQuery();

			if (resultList != null && resultList.size() > 0) {

				List<EmailUser> emails = new ArrayList<EmailUser>();
				for (Table table : resultList) {

					emails.add((EmailUser) table);
				}

				return emails;
			} else {
				LoggerSet.logError("UserOperation", "getAllemail",
						"Failed to retrieve user email for userId: " + email.getEmailId(), null);
				return null;
			}
		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "getAllemail", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		} catch (Exception e) {
			LoggerSet.logError("UserOperation", "getAllemail", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static Boolean deleteUserEmail(EmailUser email, long userID) throws DBOperationException {

		int[] result = { -1, -1 };
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {
			query.openConnection();

			result = query.delete(email).execute(userID);

			if (result[0] != -1) {
				LoggerSet.logInfo("UserOperation", "deleteUserEmail",
						"Email deleted successfully for userId: " + userID);
				return true;
			} else {
				LoggerSet.logError("UserOperation", "deleteUserEmail", "Failed to delete email for userId: " + userID,
						null);
				return false;
			}
		} catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "deleteUserEmail", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserOperation", "deleteUserEmail", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	/**
	 * Retrieves user data by user ID.
	 *
	 * @param userId the ID of the user to retrieve
	 * @return the UserData object containing user details or null if not found
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static UserData getUserData(long userId) throws DBOperationException {

		UserData user;
		List<Table> resultList = new ArrayList<>();

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();
			user = new UserData();
			user.setID(userId);
			LoginCredentials login = new LoginCredentials();
			login.setUserID(userId);
			EmailUser email = new EmailUser();
			email.setEmailID(userId);

			Oauth oauth = new Oauth();
			oauth.setUserID(userId);
			user.setOauth(oauth);
			user.setLoginCredentials(login);
			user.setEmail(email);
			resultList = query.select(user).executeQuery();
			if (resultList.size() > 0) {
				user = (UserData) resultList.getFirst();

				LoggerSet.logInfo("UserOperation", "getUserData",
						"User data retrieved successfully for userId: " + userId);
				return user;
			} else {
				LoggerSet.logError("UserOperation", "getUserData", "No user found for userId: " + userId, null);
			}
		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "getUserData", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserOperation", "getUserData", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		}
		return null;
	}

	public static boolean userPasswordChange(UserData oldUser, String oldPassword, String newPassword, long userID)
			throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			if (BCrypt.checkpw(oldPassword, oldUser.getPassword())) {

				String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());

				UserData user = new UserData();
				user.setID(userID);
				user.setPassword(password);

				result = query.update(user).execute(userID);

			} else {
				result[0] = -1;
				LoggerSet.logError("UserOperation", "userPasswordChange", "Incorrect password", null);

			}

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("UserOperation", "userPasswordChange", "Failed to change User password", null);
				return false;
			}

			query.commit();
			LoggerSet.logInfo("UserOperation", "userPasswordChange", "password successfully for userID: " + userID);
			return true;

		}

		catch (QueryBuilderException e) {
			LoggerSet.logError("UserOperation", "userPasswordChange", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException("Error proccesing DBOperation", e);
		}

		catch (Exception e) {
			LoggerSet.logError("UserOperation", "userPasswordChange", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

}
