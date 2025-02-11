package dboperation;

import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Oauth;
import dbpojo.Table;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

public class UserOperation {

	private static LoggerSet logger = new LoggerSet();

	/**
	 * Creates a new user in the database.
	 *
	 * @param userdata the UserData object containing user details
	 * @return the created UserData object or null if an error occurs
	 * @throws SQLException         if a database access error occurs
	 * @throws DBOperationException
	 */
	public static Userdata createUser(Userdata userdata) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };

		try {
			String password = BCrypt.hashpw(userdata.getPassword(), BCrypt.gensalt());

			qg.openConnection();
			userdata.setPassword(password);

			val = qg.insert(userdata).execute(-1);

			if (val[0] == 0) {

				qg.rollBackConnection();
				logger.logError("UserOperation", "createUser", "Failed to insert user data", null);
				return null;
			}
			qg.commit();
			logger.logInfo("UserOperation", "createUser",
					"User created successfully: " + userdata.getLoginCredentials().getUserName());
			return userdata;

		} catch (Exception e) {
			logger.logError("UserOperation", "createUser", "Exception occurred: " + e.getMessage(), e);
			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	public static EmailUser addEmail(EmailUser email, long userID) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };

		try {

			qg.openConnection();

			val = qg.insert(email).execute(userID);

			if (val[0] == -1) {

				qg.rollBackConnection();
				logger.logError("UserOperation", "addEmail", "Failed to insert user data", null);
				return null;
			}
			qg.commit();
			logger.logInfo("UserOperation", "addEmail", "Email added successfully: " + email.getEmail());
		
			
			
			return email;

		} catch (Exception e) {
			logger.logError("UserOperation", "addEmail", "Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
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
	public static Userdata isUser(String userName, String password) throws DBOperationException {

		ArrayList<Table> result = new ArrayList<>();

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		try {

			qg.openConnection();

			Userdata user = new Userdata();

			if (userName.contains("@")) {

				EmailUser email = new EmailUser();
				email.setEmail(userName);
				result = qg.select(email).executeQuery();

				if (result.size() > 0) {
					email = (EmailUser) result.getFirst();

					LoginCredentials login = new LoginCredentials();
					login.setUserID(email.getEmailId());
					user.setLoginCredentials(login);
					user.setEmail(email);

					user.setID(email.getEmailId());

					result = qg.select(user).executeQuery();

					if (result.size() > 0) {

						user = (Userdata) result.getFirst();
					} else {
						logger.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName, null);
						return null;

					}

				} else {
					logger.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName, null);
					return null;

				}

			} else {

				LoginCredentials login = new LoginCredentials();
				login.setUserName(userName);

				result = qg.select(login).executeQuery();

				if (result.size() > 0) {
					login = (LoginCredentials) result.getFirst();
					EmailUser email = new EmailUser();
					email.setEmailID(login.getUserId());

					user.setLoginCredentials(login);
					user.setEmail(email);

					user.setID(login.getUserId());

					result = qg.select(user).executeQuery();

					if (result.size() > 0) {

						user = (Userdata) result.getFirst();
					} else {
						logger.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName, null);
						return null;

					}

				} else {
					logger.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName, null);
					return null;

				}

			}

			if (result.size() > 0 && BCrypt.checkpw(password, user.getPassword())) {

				logger.logInfo("UserOperation", "isUser", "User verified successfully: " + userName);
				return user;
			} else {
				logger.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName, null);
				return null;
			}

		} catch (Exception e) {
			logger.logError("UserOperation", "isUser", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		} finally {
			qg.closeConnection();
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
	public static boolean userDataUpdate(Userdata userData, String newPassword) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		Userdata user;
		long userId = userData.getID();
		int[] result = { -1, -1 };

		try {

			qg.openConnection();
			EmailUser emailData = new EmailUser();
			emailData.setEmailID(userId);

			ArrayList<Table> data = qg.select(emailData).executeQuery();
			if (data.size() > 0) {

				for (Table email : data) {
					qg.delete(email).execute(userId);

				}

			} else {
				qg.rollBackConnection();
				logger.logError("UserOperation", "userDataUpdate", "Failed to delete existing emails", null);
				return false;

			}

			for (EmailUser email : userData.getallemail()) {
				if (email != null) {

					email.setEmailID(userData.getID());

					result = qg.insert(email).execute(userId);
					if (result[0] == -1) {

						qg.rollBackConnection();
						logger.logError("UserOperation", "userDataUpdate", "Failed to insert new email: " + email,
								null);
						return false;
					}
				}
			}

			if (userData.getPassword() == null || newPassword == null) {

				result = qg.update(userData).execute(userId);

			} else {

				ArrayList<Table> users = qg.select(userData).executeQuery();
				if (users.size() > 0) {

					user = (Userdata) users.getFirst();
				} else {

					qg.rollBackConnection();
					logger.logError("UserOperation", "userDataUpdate", "Failed to find  existing userData", null);
					return false;

				}

				if (BCrypt.checkpw(userData.getPassword(), user.getPassword())) {

					String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
					userData.setPassword(password);

					result = qg.update(userData).execute(userId);

				} else {
					result[0] = -1;
					logger.logError("UserOperation", "userDataUpdate", "Incorrect password", null);

				}

			}

			if (result[0] == -1) {

				qg.rollBackConnection();
				logger.logError("UserOperation", "userDataUpdate", "Failed to update user data", null);
				return false;
			}

			qg.commit();
			logger.logInfo("UserOperation", "userDataUpdate",
					"User data updated successfully for: " + userData.getLoginCredentials().getUserName());
			return true;

		}

		catch (Exception e) {
			logger.logError("UserOperation", "userDataUpdate", "Exception occurred: " + e.getMessage(), e);
			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {
			qg.closeConnection();
		}

	}

	public static boolean userprofileUpdate(Userdata userData, EmailUser emailData) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		long userId = userData.getID();
		int[] result = { -1, -1 };
		ArrayList<Table> emails = new ArrayList<Table>();

		try {

			qg.openConnection();

			EmailUser email = new EmailUser();
			email.setEmailID(userId);

			emails = qg.select(email).executeQuery();

			if (emails.size() > 0) {

				for (Table data : emails) {
					EmailUser currentEmail = (EmailUser) data;

					EmailUser userEmail = new EmailUser();
					userEmail.setID(data.getID());

					if (currentEmail.getEmail().equals(emailData.getEmail())) {

						userEmail.setIsPrimary(true);

					} else {

						userEmail.setIsPrimary(false);
					}

					qg.update(userEmail).execute(userId);

				}
			}

			result = qg.update(userData).execute(userId);

			if (result[0] == -1) {

				qg.rollBackConnection();
				logger.logError("UserOperation", "userprofileUpdate", "Failed to update user data", null);
				return false;
			}

			qg.commit();
			logger.logInfo("UserOperation", "userprofileUpdate",
					"User data updated successfully for: " + userData.getLoginCredentials().getUserName());
			return true;

		}

		catch (Exception e) {
			logger.logError("UserOperation", "userprofileUpdate", "Exception occurred: " + e.getMessage(), e);
			qg.rollBackConnection();
			throw new DBOperationException(e.getMessage());
		} finally {
			qg.closeConnection();
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
	public static Boolean deleteUserProfile(Userdata userData) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		long userId = userData.getID();
		int[] result = { -1, -1 };
		try {
			qg.openConnection();

			result = qg.delete(userData).execute(userId);

			if (result[0] != -1) {
				logger.logInfo("UserOperation", "deleteUserProfile",
						"User profile deleted successfully for userId: " + userData.getID());
				return true;
			} else {
				logger.logError("UserOperation", "deleteUserProfile",
						"Failed to delete user profile for userId: " + userData.getID(), null);
				return false;
			}
		} catch (Exception e) {
			logger.logError("UserOperation", "deleteUserProfile", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}

	}

	public static Boolean deleteUserEmail(EmailUser email, long userID) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		int[] result = { -1, -1 };
		try {
			qg.openConnection();

			result = qg.delete(email).execute(userID);

			if (result[0] != -1) {
				logger.logInfo("UserOperation", "deleteUserEmail", "Email deleted successfully for userId: " + userID);
				return true;
			} else {
				logger.logError("UserOperation", "deleteUserEmail", "Failed to delete email for userId: " + userID,
						null);
				return false;
			}
		} catch (Exception e) {
			logger.logError("UserOperation", "deleteUserEmail", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
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
	public static Userdata getUserData(long userId) throws DBOperationException {

		Userdata user;
		ArrayList<Table> result = new ArrayList<>();

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {

			qg.openConnection();
			user = new Userdata();
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
			result = qg.select(user).executeQuery();

//			user = (Userdata) qg.select(tables.user_data)
//					.join(JoinType.left, user_data.user_id, Operation.Equal, Login_credentials.log_id)
//					.join(JoinType.left, user_data.user_id, Operation.Equal, Email_user.em_id)
//					.where(user_data.user_id, Operation.Equal, userId).executeQuery().getFirst();

//			UserData ud = new UserData();

			if (result.size() > 0) {
				user = (Userdata) result.getFirst();

				logger.logInfo("UserOperation", "getUserData",
						"User data retrieved successfully for userId: " + userId);
				return user;
			} else {
				logger.logError("UserOperation", "getUserData", "No user found for userId: " + userId, null);
			}
		} catch (Exception e) {
			logger.logError("UserOperation", "getUserData", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());

		} finally {

			qg.closeConnection();
		}
		return null;
	}

	public static boolean userPasswordChange(Userdata oldUser, String oldPassword, String newPassword, long userID)
			throws DBOperationException {
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		int[] result = { -1, -1 };

		try {

			qg.openConnection();

			System.out.println("here the stored password is" + oldUser.getPassword());

			System.out.println("here the provided pass is" + oldPassword);

			if (BCrypt.checkpw(oldPassword, oldUser.getPassword())) {

				String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());

				Userdata user = new Userdata();
				user.setID(userID);
				user.setPassword(password);

				result = qg.update(user).execute(userID);

			} else {
				result[0] = -1;
				logger.logError("UserOperation", "userPasswordChange", "Incorrect password", null);

			}

			if (result[0] == -1) {

				qg.rollBackConnection();
				logger.logError("UserOperation", "userPasswordChange", "Failed to change User password", null);
				return false;
			}

			qg.commit();
			logger.logInfo("UserOperation", "userPasswordChange", "password successfully for userID: " + userID);
			return true;

		}

		catch (Exception e) {
			logger.logError("UserOperation", "userPasswordChange", "Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {
			qg.closeConnection();
		}

	}

}
