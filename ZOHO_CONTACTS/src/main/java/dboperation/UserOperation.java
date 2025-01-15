package dboperation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import dbmodel.UserData;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Table;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;
import querybuilderconfig.TableSchema;
import querybuilderconfig.TableSchema.Email_user;
import querybuilderconfig.TableSchema.JoinType;
import querybuilderconfig.TableSchema.Login_credentials;
import querybuilderconfig.TableSchema.Operation;
import querybuilderconfig.TableSchema.Statement;
import querybuilderconfig.TableSchema.tables;
import querybuilderconfig.TableSchema.user_data;

public class UserOperation {

	private LoggerSet logger = new LoggerSet();

	/**
	 * Creates a new user in the database.
	 *
	 * @param userdata the UserData object containing user details
	 * @return the created UserData object or null if an error occurs
	 * @throws SQLException if a database access error occurs
	 */
	public Userdata createUser(Userdata userdata) throws SQLException {
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };
		int genUserId = 0;
		try {
			String password = BCrypt.hashpw(userdata.getPassword(), BCrypt.gensalt());
//			con.setAutoCommit(false);

			qg.openConnection();
			userdata.setPassword(password);

			// Insert user data
//			PreparedStatement ps = con.prepareStatement(
//					"INSERT INTO user_data (Name, password, phone_no, address, timezone) VALUES (?, ?, ?, ?, ?);",
//					PreparedStatement.RETURN_GENERATED_KEYS);
//			ps.setString(1, data.getName());
//			ps.setString(2, password);
//			ps.setString(3, data.getPhoneno());
//			ps.setString(4, data.getAddress());
//			ps.setString(5, data.getTimezone());
//			int val = ps.executeUpdate();

//			 val=qg.insert(tables.user_data, user_data.Name,user_data.password,user_data.phone_no,user_data.address,user_data.timezone,user_data.created_time,user_data.modified_time)
//					.valuesInsert(userdata.getName(),userdata.getPassword(),userdata.getPhoneno(),userdata.getAddress(),userdata.getTimezone(),userdata.getCreatedAt(),userdata.getModifiedAt()).execute(Statement.RETURN_GENERATED_KEYS);

			val = qg.insert(userdata).execute(-1);

			if (val[0] == 0) {
//				con.rollback();
				qg.rollBackConnection();
				logger.logError("UserOperation", "createUser", "Failed to insert user data", null);
				return null;
			}

//			ResultSet id = ps.getGeneratedKeys();
//			if (val[1] !=-1){
//				 genUserId = val[1];
//				

			// Insert login credentials
//				ps = con.prepareStatement("INSERT INTO Login_credentials VALUES (?, ?);");
//				ps.setInt(1, genUserId);
//				ps.setString(2, data.getUserName());
//				val = ps.executeUpdate();
//				 LoginCredentials loginCredential=userdata.getLoginCredentials();
//				userdata.getLoginCredentials().setUserID(genUserId);
//				val=qg.insert(tables.Login_credentials,Login_credentials.log_id,Login_credentials.username,Login_credentials.created_time,Login_credentials.modified_time)
//						.valuesInsert(loginCredential.getUserId(),loginCredential.getUserName(),loginCredential.getCreatedAt(),loginCredential.getModifiedAt()).execute();
//
//				if (val[0] == 0) {
//					con.rollback();
//					logger.logError("UserOperation", "createUser", "Failed to insert login credentials", null);
//					return null;
//				}

			// Insert email data
//				ps = con.prepareStatement("INSERT INTO Email_user VALUES (?, ?, ?);");
//				ps.setInt(1, genUserId);
//				ps.setString(2, data.getCurrentEmail());
//				ps.setBoolean(3, true);
//				val = ps.executeUpdate();
//				for(EmailUser email :userdata.getallemail()) {
//					
//					System.out.println("Email data is"+genUserId+"  "+email.getEmail()+" "+email.getIsPrimary());
//					
//					email.setEmailID(genUserId);
//					val=qg.insert(tables.Email_user,Email_user.em_id,Email_user.email,Email_user.is_primary,Email_user.created_time,Email_user.modified_time)
//							.valuesInsert(email.getEmailId(),email.getEmail(),email.getIsPrimary(),email.getCreatedAt(),email.getModifiedAt())
//							.execute();
//					
//				}

//				if (val[0] == 0) {
////					con.rollback();
//					qg.rollBackConnection();
//					logger.logError("UserOperation", "createUser", "Failed to insert email data", null);
//					return null;
//				}
//				userdata.setUserId(genUserId);
//			}
//			con.commit();
			qg.commit();
			logger.logInfo("UserOperation", "createUser",
					"User created successfully: " + userdata.getLoginCredentials().getUserName());
			return userdata;

		} catch (Exception e) {
			logger.logError("UserOperation", "createUser", "Exception occurred: " + e.getMessage(), e);
//			con.rollback(); // Ensure rollback in case of exception

			qg.rollBackConnection();
		} finally {
//			con.close();
			qg.closeConnection();
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
	public Userdata isUser(String userName, String password) throws SQLException {
//		String[] email = new String[5];
//		Connection con = DBconnection.getConnection();
		Userdata val;
		ArrayList<Table> result = new ArrayList<>();

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		try {

			qg.openConnection();

			Userdata user = new Userdata();

			if (userName.contains("@")) {
//				ps = con.prepareStatement(
//						"SELECT * FROM user_data ud LEFT JOIN Login_credentials lg ON ud.user_id = lg.id "
//								+ "LEFT JOIN Email_user eu ON lg.id = eu.em_id WHERE email = ?;");
//				ps.setString(1, ud.getUserName());

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

//				val = (Userdata) qg.select(tables.user_data)
//						.join(JoinType.left, user_data.user_id, Operation.Equal, Login_credentials.log_id)
//						.join(JoinType.left, user_data.user_id, Operation.Equal, Email_user.em_id)
//						.where(Email_user.email, Operation.Equal, userName).executeQuery().get(0);

			} else {
//				ps = con.prepareStatement(
//						"SELECT * FROM user_data ud LEFT JOIN Login_credentials lg ON ud.user_id = lg.id "
//								+ "LEFT JOIN Email_user eu ON lg.id = eu.em_id WHERE username = ?;");
//				ps.setString(1, ud.getUserName());
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

//				val = (Userdata) qg.select(tables.user_data)
//						.join(JoinType.left, user_data.user_id, Operation.Equal, Login_credentials.log_id)
//						.join(JoinType.left, user_data.user_id, Operation.Equal, Email_user.em_id)
//						.where(Login_credentials.username, Operation.Equal, userName).executeQuery().get(0);

			}

//			val = ps.executeQuery();
			if (result.size() >0  && BCrypt.checkpw(password, user.getPassword())) {
//				ud.setUserId(val.getInt(1));
//				ud.setName(val.getString(2));
//				ud.setPhoneno(val.getString(4));
//				ud.setAddress(val.getString(5));
//				ud.setTimezone(val.getString(6));
//				ud.setUserName(val.getString(8));
//				ud.setCurrentEmail(val.getString(10));

//				ps = con.prepareStatement("SELECT email, is_primary FROM Email_user WHERE em_id = ?;");
//				ps.setInt(1, ud.getUserId());
//				val = ps.executeQuery();
//				int i = 0;
//				while (val.next() && i < 5) {
//					email[i] = val.getString(1);
//					if (val.getBoolean(2)) {
//						ud.setPrimaryMail(val.getString(1));
//					}
//					i++;
//				}
//				ud.setEmail(email);
				logger.logInfo("UserOperation", "isUser", "User verified successfully: " + userName);
				return user;
			} else {
				logger.logError("UserOperation", "isUser", "Invalid credentials for user: " + userName, null);
				return null;
			}

		} catch (Exception e) {
			logger.logError("UserOperation", "isUser", "Exception occurred: " + e.getMessage(), e);
		} finally {
//			con.close();
			qg.closeConnection();
		}
		return null;
	}

	/**
	 * Updates user data in the database.
	 *
	 * @param userData the UserData object containing updated details
	 * @return true if the update was successful, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
	public boolean userDataUpdate(Userdata userData, String newPassword) throws SQLException {
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		UserData user;
		int userId=userData.getID();
		int[] result= {-1,-1};
//		int[] val = { -1, -1 };
		try {
//
//			con.setAutoCommit(false);

			qg.openConnection();
			if (userData.getPassword() == null || newPassword == null) {

//				PreparedStatement ps = con.prepareStatement(
//						"UPDATE user_data SET Name = ?, phone_no = ?, address = ?, timezone = ? WHERE user_id = ?;");
//				ps.setString(1, ud.getName());
//				ps.setString(2, ud.getPhoneno());
//				ps.setString(3, ud.getAddress());
//				ps.setString(4, ud.getTimezone());
//				ps.setInt(5, ud.getUserId());
//				val = ps.executeUpdate();

//				val = qg.update(tables.user_data, user_data.Name, user_data.phone_no, user_data.address,
//						user_data.timezone, user_data.modified_time)
//						.valuesUpdate(userData.getName(), userData.getPhoneno(), userData.getAddress(),
//								userData.getTimezone(), userData.getModifiedAt())
//						.where(user_data.user_id, Operation.Equal, userData.getUserId()).execute();

				result=  qg.update(userData).execute(userId);
				
				
			} else {
//				PreparedStatement ps = con.prepareStatement("select password from user_data where user_id=?;");
//				ps.setInt(1, ud.getUserId());
//				ArrayList<Object> pass = qg.select(tables.user_data, user_data.password)
//						.where(user_data.user_id, Operation.Equal, userData.getUserId()).executeQuery();
//				pass.next();

//				if (pass.get(0) instanceof UserData) {
//					user = (UserData) pass.get(0);
//				} else {
//					throw new TypeNotPresentException("unable to cast Userdata from  result list Object", null);
//				}
				if (BCrypt.checkpw(userData.getPassword(), user.getPassword())) {

					String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
					userData.setPassword(password);

//					ps = con.prepareStatement(
//							"UPDATE user_data SET Name = ?, phone_no = ?, address = ?, password = ?, timezone = ? WHERE user_id = ?;");
//					ps.setString(1, ud.getName());
//					ps.setString(2, ud.getPhoneno());
//					ps.setString(3, ud.getAddress());
//					ps.setString(4, password);
//					ps.setString(5, ud.getTimezone());
//					ps.setInt(6, ud.getUserId());
//					val = ps.executeUpdate();

//					val = qg.update(tables.user_data, user_data.Name, user_data.phone_no, user_data.address,
//							user_data.password, user_data.timezone, user_data.modified_time)
//							.valuesUpdate(userData.getName(), userData.getPhoneno(), userData.getAddress(),
//									userData.getPassword(), userData.getTimezone(), userData.getModifiedAt())
//							.where(user_data.user_id, Operation.Equal, userData.getUserId()).execute();
					
					
					result=qg.update(userData).execute(userId);

				} else {
					result[0] = 0;
					logger.logError("UserOperation", "userDataUpdate", "Incorrect password", null);

				}

			}

			if (result[0] == 0) {
//				con.rollback();
				qg.rollBackConnection();
				logger.logError("UserOperation", "userDataUpdate", "Failed to update user data", null);
				return false;
			}

//			PreparedStatement ps = con.prepareStatement("UPDATE Login_credentials SET username = ? WHERE id = ?;");
//			ps.setString(1, ud.getUserName());
//			ps.setInt(2, ud.getUserId());
//			val = ps.executeUpdate();

//			userData.getLoginCredentials().setUserID(userData.getUserId());
//			
//			qg.update(tables.Login_credentials, TableSchema.Login_credentials.username, Login_credentials.modified_time)
//					.valuesUpdate(userData.getLoginCredentials().getUserName(), userData.getModifiedAt())
//					.where(TableSchema.Login_credentials.log_id, Operation.Equal, userData.getUserId()).execute();
//
//			if (val[0] == 0) {
//				qg.rollBackConnection();
//
////				con.rollback();
//				logger.logError("UserOperation", "userDataUpdate", "Failed to update login credentials", null);
//				return false;
//			}

//			ps = con.prepareStatement("DELETE FROM Email_user WHERE em_id = ?;");
//			ps.setInt(1, ud.getUserId());
//			val = ps.executeUpdate();
			for (EmailUser email : userData.getallemail()) {
				val = qg.delete(tables.Email_user).where(Email_user.em_id, Operation.Equal, userData.getID())
						.and(Email_user.email, Operation.Equal, email.getEmail()).execute();

				if (val[0] == 0) {
//					con.rollback();

					qg.rollBackConnection();
					logger.logError("UserOperation", "userDataUpdate", "Failed to delete existing emails", null);
					return false;
				}

			}

			for (EmailUser email : userData.getallemail()) {
				if (email != null) {
//					ps = con.prepareStatement("INSERT INTO Email_user VALUES (?, ?, ?);");
//					ps.setInt(1, ud.getUserId());
//					ps.setString(2, email);
//					ps.setBoolean(3, ud.getPrimaryMail()));
//					val = ps.executeUpdate();

					email.setEmailID(userData.getID());

					val = qg.insert(tables.Email_user, Email_user.em_id, Email_user.email, Email_user.is_primary,
							Email_user.created_time, Email_user.modified_time)
							.valuesInsert(userData.getID(), email.getEmail(), email.getIsPrimary(),
									email.getModifiedAt(), email.getModifiedAt())
							.execute();
					if (val[0] == 0) {
//						con.rollback();

						qg.rollBackConnection();
						logger.logError("UserOperation", "userDataUpdate", "Failed to insert new email: " + email,
								null);
						return false;
					}
				}
			}
//			con.commit();

			qg.commit();
			logger.logInfo("UserOperation", "userDataUpdate",
					"User data updated successfully for: " + userData.getLoginCredentials().getUserName());
			return true;

		}

		catch (Exception e) {
			logger.logError("UserOperation", "userDataUpdate", "Exception occurred: " + e.getMessage(), e);
//			con.rollback(); // Ensure rollback in case of exception

			qg.rollBackConnection();
		} finally {
			qg.closeConnection();
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
	public Boolean deleteUserProfile(Userdata userData) throws SQLException {
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int userId=userData.getID();
		int[] result = { -1, -1 };
		try {
			qg.openConnection();

//			result = qg.delete(tables.user_data).where(user_data.user_id, Operation.Equal, userData.getUserId()).execute();

			
			result=qg.delete(userData).execute(userId);
//			PreparedStatement ps = con.prepareStatement("DELETE FROM user_data WHERE user_id = ?;");
//			ps.setInt(1, userId);
//			int val = ps.executeUpdate();
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
		} finally {

			qg.closeConnection();
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
	public Userdata getUserData(int userId) throws SQLException {
//		String[] email = new String[5];
		Userdata user;
		ArrayList<Table> result=new ArrayList<>();
		
		
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {
//			PreparedStatement ps = con.prepareStatement(
//					"SELECT * FROM user_data ud LEFT JOIN Login_credentials lg ON ud.user_id = lg.id WHERE user_id = ?;");
//			ps.setInt(1, userId);
//			ResultSet val = ps.executeQuery();

			qg.openConnection();
			user=new Userdata();
			user.setID(userId);
			LoginCredentials login=new LoginCredentials();
			login.setUserID(userId);
			EmailUser email=new EmailUser();
			email.setEmailID(userId);
			user.setLoginCredentials(login);
			user.setEmail(email);

//			user = (Userdata) qg.select(tables.user_data)
//					.join(JoinType.left, user_data.user_id, Operation.Equal, Login_credentials.log_id)
//					.join(JoinType.left, user_data.user_id, Operation.Equal, Email_user.em_id)
//					.where(user_data.user_id, Operation.Equal, userId).executeQuery().getFirst();

			
			
			
//			UserData ud = new UserData();

			if (user != null) {
//				ud.setUserId(val.getInt(1));
//				ud.setName(val.getString(2));
//				ud.setPhoneno(val.getString(4));
//				ud.setAddress(val.getString(5));
//				ud.setTimezone(val.getString(6));
//				ud.setUserName(val.getString(8));
//
//				ps = con.prepareStatement("SELECT email, is_primary FROM Email_user WHERE em_id = ?;");
//				ps.setInt(1, userId);
//				val = ps.executeQuery();
//				int i = 0;
//				while (val.next() && i < 5) {
//					email[i] = val.getString(1);
//					if (val.getBoolean(2)) {
//						ud.setPrimaryMail(val.getString(1));
//					}
//					i++;
//				}
//				ud.setEmail(email);
				logger.logInfo("UserOperation", "getUserData",
						"User data retrieved successfully for userId: " + userId);
				return user;
			} else {
				logger.logError("UserOperation", "getUserData", "No user found for userId: " + userId, null);
			}
		} catch (Exception e) {
			logger.logError("UserOperation", "getUserData", "Exception occurred: " + e.getMessage(), e);
		} finally {
//			con.close();
			qg.closeConnection();
		}
		return null;
	}
}
