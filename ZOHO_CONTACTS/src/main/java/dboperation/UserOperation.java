package dboperation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import dbmodel.UserData;
import dbpojo.EmailUser;
import dbpojo.Userdata;
import loggerfiles.LoggerSet;
import querybuilder.QueryBuilder;
import querybuilder.SqlQueryLayer;
import querybuilder.TableSchema;
import querybuilder.TableSchema.Email_user;
import querybuilder.TableSchema.JoinType;
import querybuilder.TableSchema.Login_credentials;
import querybuilder.TableSchema.Operation;
import querybuilder.TableSchema.Statement;
import querybuilder.TableSchema.tables;
import querybuilder.TableSchema.user_data;

public class UserOperation {

	private LoggerSet logger = new LoggerSet();
	
	

	/**
	 * Creates a new user in the database.
	 *
	 * @param data the UserData object containing user details
	 * @return the created UserData object or null if an error occurs
	 * @throws SQLException if a database access error occurs
	 */
	public Userdata createUser(Userdata data) throws SQLException {
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val= {-1,-1};
		int genUserId=0;
		try {
			String password = BCrypt.hashpw(data.getPassword(), BCrypt.gensalt());
//			con.setAutoCommit(false);
			
			qg.openConnection();

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
			
			
			
			
			
			
			 val=qg.insert(tables.user_data, user_data.Name,user_data.password,user_data.phone_no,user_data.address,user_data.timezone)
					.valuesInsert(data.getName(),password,data.getPhoneno(),data.getAddress(),data.getTimezone()).execute(Statement.RETURN_GENERATED_KEYS);
            
			
		
			
			if (val[0] == 0) {
//				con.rollback();
				qg.rollBackConnection();
				logger.logError("UserOperation", "createUser", "Failed to insert user data", null);
				return null;
			}

//			ResultSet id = ps.getGeneratedKeys();
			if (val[1] !=-1){
				 genUserId = val[1];

				// Insert login credentials
//				ps = con.prepareStatement("INSERT INTO Login_credentials VALUES (?, ?);");
//				ps.setInt(1, genUserId);
//				ps.setString(2, data.getUserName());
//				val = ps.executeUpdate();
				
				val=qg.insert(tables.Login_credentials).valuesInsert(genUserId,data.getLoginCredentials().getUserName()).execute();

				if (val[0] == 0) {
//					con.rollback();
					logger.logError("UserOperation", "createUser", "Failed to insert login credentials", null);
					return null;
				}

				// Insert email data
//				ps = con.prepareStatement("INSERT INTO Email_user VALUES (?, ?, ?);");
//				ps.setInt(1, genUserId);
//				ps.setString(2, data.getCurrentEmail());
//				ps.setBoolean(3, true);
//				val = ps.executeUpdate();
				for(EmailUser i :data.getallemail()) {
					
					System.out.println("Email data is"+genUserId+"  "+i.getEmail()+" "+i.getIsPrimary());
					
					
					val=qg.insert(tables.Email_user).valuesInsert(genUserId,i.getEmail(),i.getIsPrimary()).execute();
					
				}
				
				

				if (val[0] == 0) {
//					con.rollback();
					qg.rollBackConnection();
					logger.logError("UserOperation", "createUser", "Failed to insert email data", null);
					return null;
				}
				data.setUserId(genUserId);
			}
//			con.commit();
			qg.commit();
			logger.logInfo("UserOperation", "createUser", "User created successfully: " + data.getLoginCredentials().getUserName());
			return data;

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
	public Userdata isUser(String userName,String password) throws SQLException {
		String[] email = new String[5];
//		Connection con = DBconnection.getConnection();
		Userdata val;
		
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		
		try {
			PreparedStatement ps;
			qg.openConnection();
		

			if (userName.contains("@")) {
//				ps = con.prepareStatement(
//						"SELECT * FROM user_data ud LEFT JOIN Login_credentials lg ON ud.user_id = lg.id "
//								+ "LEFT JOIN Email_user eu ON lg.id = eu.em_id WHERE email = ?;");
//				ps.setString(1, ud.getUserName());
				
				
				val=  (Userdata)   qg.select(tables.user_data)
					.join(JoinType.left,user_data.user_id, Operation.Equal, Login_credentials.id)
					.join(JoinType.left,user_data.user_id, Operation.Equal, Email_user.em_id).where(Email_user.email, Operation.Equal, userName)
					.executeQuery().get(0);
				
			} else {
//				ps = con.prepareStatement(
//						"SELECT * FROM user_data ud LEFT JOIN Login_credentials lg ON ud.user_id = lg.id "
//								+ "LEFT JOIN Email_user eu ON lg.id = eu.em_id WHERE username = ?;");
//				ps.setString(1, ud.getUserName());
				
				val= (Userdata) qg.select(tables.user_data)
						.join(JoinType.left,user_data.user_id, Operation.Equal, Login_credentials.id)
						.join(JoinType.left,user_data.user_id, Operation.Equal, Email_user.em_id).where(Login_credentials.username, Operation.Equal, userName)
						.executeQuery().get(0);
				
			
			}

//			val = ps.executeQuery();
			if (val != null && BCrypt.checkpw(password, val.getPassword())) {
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
				return val;
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
	 * @param ud the UserData object containing updated details
	 * @return true if the update was successful, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
	public boolean userDataUpdate(Userdata ud,String newPassword) throws SQLException {
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		UserData user;
		int[] val= {-1,-1};
		try {
//
//			con.setAutoCommit(false);
			
			qg.openConnection();
			if (ud.getPassword() == null || newPassword == null) {

//				PreparedStatement ps = con.prepareStatement(
//						"UPDATE user_data SET Name = ?, phone_no = ?, address = ?, timezone = ? WHERE user_id = ?;");
//				ps.setString(1, ud.getName());
//				ps.setString(2, ud.getPhoneno());
//				ps.setString(3, ud.getAddress());
//				ps.setString(4, ud.getTimezone());
//				ps.setInt(5, ud.getUserId());
//				val = ps.executeUpdate();
				
				val=qg.update(tables.user_data,user_data.Name,user_data.phone_no,user_data.address,user_data.timezone)
						.valuesUpdate(ud.getName(),ud.getPhoneno(),ud.getAddress(),ud.getTimezone())
						.where(user_data.user_id,Operation.Equal,ud.getUserId()).execute();

			} else {
//				PreparedStatement ps = con.prepareStatement("select password from user_data where user_id=?;");
//				ps.setInt(1, ud.getUserId());
				ArrayList<Object> pass = qg.select(tables.user_data, user_data.password).where(user_data.user_id, Operation.Equal,ud.getUserId()).executeQuery();
//				pass.next();
				
				if(pass.get(0) instanceof UserData) {
					 user=(UserData) pass.get(0);
				}else {
					throw new TypeNotPresentException("unable to cast Userdata from  result list Object", null);
				}
				if (BCrypt.checkpw(ud.getPassword(), user.getPassword())) {

					String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());

//					ps = con.prepareStatement(
//							"UPDATE user_data SET Name = ?, phone_no = ?, address = ?, password = ?, timezone = ? WHERE user_id = ?;");
//					ps.setString(1, ud.getName());
//					ps.setString(2, ud.getPhoneno());
//					ps.setString(3, ud.getAddress());
//					ps.setString(4, password);
//					ps.setString(5, ud.getTimezone());
//					ps.setInt(6, ud.getUserId());
//					val = ps.executeUpdate();
					
					val=qg.update(tables.user_data,user_data.Name,user_data.phone_no,user_data.address,user_data.password,user_data.timezone)
							.valuesUpdate(ud.getName(),ud.getPhoneno(),ud.getAddress(),password,ud.getTimezone())
							.where(user_data.user_id, Operation.Equal, ud.getUserId()).execute();

				} else {
					val[0] = 0;
					logger.logError("UserOperation", "userDataUpdate", "Incorrect password", null);

				}

			}

			if (val[0] == 0) {
//				con.rollback();
				qg.rollBackConnection();
				logger.logError("UserOperation", "userDataUpdate", "Failed to update user data", null);
				return false;
			}

//			PreparedStatement ps = con.prepareStatement("UPDATE Login_credentials SET username = ? WHERE id = ?;");
//			ps.setString(1, ud.getUserName());
//			ps.setInt(2, ud.getUserId());
//			val = ps.executeUpdate();
			
			
			qg.update(tables.Login_credentials, TableSchema.Login_credentials.username)
			.valuesUpdate(ud.getLoginCredentials().getUserName())
			.where(TableSchema.Login_credentials.id,Operation.Equal , ud.getUserId())
			.execute();

			if (val[0] == 0) {
				qg.rollBackConnection();
				
//				con.rollback();
				logger.logError("UserOperation", "userDataUpdate", "Failed to update login credentials", null);
				return false;
			}

//			ps = con.prepareStatement("DELETE FROM Email_user WHERE em_id = ?;");
//			ps.setInt(1, ud.getUserId());
//			val = ps.executeUpdate();
			
			val=qg.delete(tables.Email_user).where(Email_user.em_id, Operation.Equal, ud.getUserId()).execute();

			if (val[0] == 0) {
//				con.rollback();
				
				qg.rollBackConnection();
				logger.logError("UserOperation", "userDataUpdate", "Failed to delete existing emails", null);
				return false;
			}

			for (EmailUser email : ud.getallemail()) {
				if (email != null) {
//					ps = con.prepareStatement("INSERT INTO Email_user VALUES (?, ?, ?);");
//					ps.setInt(1, ud.getUserId());
//					ps.setString(2, email);
//					ps.setBoolean(3, ud.getPrimaryMail()));
//					val = ps.executeUpdate();
					
					
					
					val=qg.insert(tables.Email_user).valuesInsert(ud.getUserId(),email.getEmail(),email.getIsPrimary()).execute();
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
					"User data updated successfully for: " + ud.getLoginCredentials().getUserName());
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
	public Boolean deleteUserProfile(int userId) throws SQLException {
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val= {-1,-1};
		try {
			qg.openConnection();

			 val = qg.delete(tables.user_data)
					.where(user_data.user_id, Operation.Equal, userId).execute();

//			PreparedStatement ps = con.prepareStatement("DELETE FROM user_data WHERE user_id = ?;");
//			ps.setInt(1, userId);
//			int val = ps.executeUpdate();
			if (val[0] != 0) {
				logger.logInfo("UserOperation", "deleteUserProfile",
						"User profile deleted successfully for userId: " + userId);
				return true;
			} else {
				logger.logError("UserOperation", "deleteUserProfile",
						"Failed to delete user profile for userId: " + userId, null);
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
		String[] email = new String[5];
	    Userdata val;
//		Connection con = DBconnection.getConnection();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		try {
//			PreparedStatement ps = con.prepareStatement(
//					"SELECT * FROM user_data ud LEFT JOIN Login_credentials lg ON ud.user_id = lg.id WHERE user_id = ?;");
//			ps.setInt(1, userId);
//			ResultSet val = ps.executeQuery();
			
			qg.openConnection();
			
			val=(Userdata) qg.select(tables.user_data)
					.join(JoinType.left, user_data.user_id, Operation.Equal,Login_credentials.id)
					.join(JoinType.left, user_data.user_id, Operation.Equal, Email_user.em_id)
					.where(user_data.user_id, Operation.Equal, userId).executeQuery().getFirst();
					
			
			
			
			
//			UserData ud = new UserData();

			if (val !=null) {
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
				return val;
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
