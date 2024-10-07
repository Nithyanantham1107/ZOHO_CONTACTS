package dboperation;

import java.net.http.HttpRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import com.mysql.cj.protocol.Resultset;

import dbconnect.DBconnection;
import dbmodel.UserData;

public class UserOperation {
	public Connection con;
	public HttpSession session;

	public UserData createUser(UserData data) throws SQLException {
		Connection con = DBconnection.getConnection();
		try {

			System.out.println(data.getName());
			String password = BCrypt.hashpw(data.getPassword(), BCrypt.gensalt());
			con.setAutoCommit(false);

			PreparedStatement ps = con.prepareStatement(
					"insert into user_data (Name,password,phone_no,address) values(?,?,?,?);",
					PreparedStatement.RETURN_GENERATED_KEYS);

			ps.setString(1, data.getName());
			ps.setString(2, password);
			ps.setString(3, data.getPhoneno());
			ps.setString(4, data.getAddress());
			int val = ps.executeUpdate();

			if (val == 0) {
				System.out.println("Error in inserting User_data Table");
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return null;
			}
			ResultSet id = ps.getGeneratedKeys();
			if (id.next()) {

				int gen_user_id = id.getInt(1);

				ps = con.prepareStatement("insert into Login_credentials values(?,?);");
				ps.setInt(1, gen_user_id);
				ps.setString(2, data.getUserName());

				val = ps.executeUpdate();

				if (val == 0) {
					System.out.println("Error in inserting Login_credentials Table");
					con.rollback();
					con.commit();
					con.setAutoCommit(true);
					return null;
				}

				ps = con.prepareStatement("insert into Email_user values(?,?,?);");
				ps.setInt(1, gen_user_id);
				ps.setString(2, data.getCurrentEmail());
				ps.setBoolean(3, true);

				val = ps.executeUpdate();
				if (val == 0) {
					System.out.println("Error in inserting Email_user Table");
					con.rollback();
					con.commit();
					con.setAutoCommit(true);
					return null;
				}
				data.setUserId(gen_user_id);

			}
			con.commit();
			con.setAutoCommit(true);

			return data;

		} catch (Exception e) {

			System.out.println(e);
		} finally {

			con.close();
		}
		return null;

	}



	public UserData isUser(UserData ud) throws SQLException {
		String[] email = new String[5];
		Connection con = DBconnection.getConnection();
		try {

			if (ud.getUserName().contains("@")) {
				System.out.println("verifiaction using Email");

				PreparedStatement ps = con.prepareStatement(
						"select * from  user_data ud left join Login_credentials lg on ud.user_id=lg.id left join Email_user eu on lg.id =eu.em_id  where email=?  ;");
				ps.setString(1, ud.getUserName());

				ResultSet val = ps.executeQuery();
				if (val.next()) {
					System.out.println("checking password" + BCrypt.checkpw(ud.getPassword(), val.getString(3)));
					if (BCrypt.checkpw(ud.getPassword(), val.getString(3))) {
						ud.setUserId(val.getInt(1));
						ud.setName(val.getString(2));
						ud.setPhoneno(val.getString(4));
						ud.setAddress(val.getString(5));
						ud.setCurrentEmail(val.getString(9));
						ud.setUserName(val.getString(7));
						System.out.println("heyyyybdjsj");
						ps = con.prepareStatement("select email,is_primary from Email_user where em_id=?");
						ps.setInt(1, ud.getUserId());
						val = ps.executeQuery();
						int i = 0;
						while (val.next()) {
							if (i < 5) {
								email[i] = val.getString(1);
								if (val.getBoolean(2)) {
									System.out.println("uuuuuuuuuu");
									ud.setPrimaryMail(val.getString(1));
								}
							} else {
								System.out.println("there is more than 5 email in database");
								return null;

							}

							i++;
						}

						ud.setEmail(email);

					} else {
						System.out.println("error in password matching...");
						return null;
					}
					return ud;
				} else {
					return null;
				}

			} else {

				System.out.println("verification using username" + ud.getUserName());

				PreparedStatement ps = con.prepareStatement(
						"select * from  user_data ud left join Login_credentials lg on ud.user_id=lg.id left join Email_user eu on lg.id =eu.em_id  where username=? ;");
				ps.setString(1, ud.getUserName());

				ResultSet val = ps.executeQuery();
				if (val.next()) {
					System.out.println("checking password" + BCrypt.checkpw(ud.getPassword(), val.getString(3)));
					if (BCrypt.checkpw(ud.getPassword(), val.getString(3))) {
						
						ud.setUserId(val.getInt(1));
						ud.setName(val.getString(2));
						ud.setPhoneno(val.getString(4));
						ud.setAddress(val.getString(5));
						ud.setCurrentEmail(val.getString(9));
						ud.setUserName(val.getString(7));

						ps = con.prepareStatement("select email,is_primary from Email_user where em_id=?");
						ps.setInt(1, ud.getUserId());
						val = ps.executeQuery();
						int i = 0;

						while (val.next()) {
							if (i < 5) {
								email[i] = val.getString(1);
								System.out.println("hello hi this email" + val.getString(1));
								if (val.getBoolean(2)) {
									System.out.println("hello hi this email 1111112345" + val.getString(1));
									ud.setPrimaryMail(val.getString(1));
								}
							} else {
								System.out.println("there is more than 5 email in database");
								return null;

							}

							i++;
						}

						ud.setEmail(email);

					} else {
						System.out.println("error in password matching...");
						return null;
					}

					return ud;
				} else {
					return null;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			con.close();
		}
		return null;

	}

	public boolean userDataUpdate(UserData ud) throws SQLException {
		Connection con = DBconnection.getConnection();
		try {

			System.out.println(ud.getName());
			String password = BCrypt.hashpw(ud.getPassword(), BCrypt.gensalt());
			con.setAutoCommit(false);
			
			
			PreparedStatement	ps = con.prepareStatement("update  user_data  set Name=?,phone_no=?,address=?,password=? where user_id=?;");

			ps.setString(1, ud.getName());
			ps.setString(2, ud.getPhoneno());
			ps.setString(3, ud.getAddress());
			ps.setString(4, password);
			ps.setInt(5, ud.getUserId());
			
			
			
		
			int val = ps.executeUpdate();
			if (val == 0) {
				System.out.println("Error in updating user_table Table");
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return false;
			}
			
			ps = con.prepareStatement("update Login_credentials set username=? where id=?;");
			ps.setString(1, ud.getUserName());
			ps.setInt(2, ud.getUserId());

			
			val = ps.executeUpdate();
			if (val == 0) {
				System.out.println("Error in Updating Login_credentials Table");
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return false;
			}
			ps = con.prepareStatement("delete from Email_user where em_id=?");
			ps.setInt(1, ud.getUserId());
			val = ps.executeUpdate();
			if (val == 0) {
				System.out.println(
						"Error in Updating Email_user Table,check availability of user id in Email_user table");
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return false;
			}

			for (String email : ud.getEmail()) {

				if (email != null) {
					System.out.println(email + "next");
					ps = con.prepareStatement("insert into Email_user values(?,?,?);");

					ps.setInt(1, ud.getUserId());
					ps.setString(2, email);
					if (ud.getPrimaryMail().equals(email)) {
						ps.setBoolean(3, true);
					} else {
						ps.setBoolean(3, false);
					}

					val = ps.executeUpdate();
					if (val == 0) {
						System.out.println("Error in Updating  Email_user Table" + email);
						con.rollback();
						con.commit();
						con.setAutoCommit(true);
						return false;
					}
				}
			}
			con.commit();
			con.setAutoCommit(true);

			return true;

		} catch (Exception e) {

			System.out.println(e);
		} finally {

			con.close();
		}
		return false;

	}

	public Boolean deleteUserProfile(int userId) throws SQLException {
		Connection con = DBconnection.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("delete from user_data where user_id=?;");
			ps.setInt(1, userId);
			int val = ps.executeUpdate();
			if (val != 0) {
				return true;
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {

			con.close();
		}
		return false;

	}

	
	
	
	public UserData getUserData(int user_id) throws SQLException {
		String[] email = new String[5];
		Connection con = DBconnection.getConnection();
		try {

		
				

				PreparedStatement ps = con.prepareStatement(
						"select * from  user_data ud left join Login_credentials lg on ud.user_id=lg.id   where user_id=?  ;");
				ps.setInt(1, user_id);

				ResultSet val = ps.executeQuery();
				UserData ud=new UserData();
				if (val.next()) {
					

						ud.setUserId(val.getInt(1));
						ud.setName(val.getString(2));
						ud.setPhoneno(val.getString(4));
						ud.setAddress(val.getString(5));
						
						ud.setUserName(val.getString(7));
						
						ps = con.prepareStatement("select email,is_primary from Email_user where em_id=?");
						ps.setInt(1, ud.getUserId());
						val = ps.executeQuery();
						int i = 0;
						while (val.next()) {
							if (i < 5) {
								email[i] = val.getString(1);
								if (val.getBoolean(2)) {
									
									ud.setCurrentEmail(val.getString(1));
									ud.setPrimaryMail(val.getString(1));
								}
							} else {
								System.out.println("there is more than 5 email in database");
								return null;

							}

							i++;
						}

						ud.setEmail(email);

					
					return ud;
				} else {
					return null;
				}

			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			con.close();
		}
		return null;

	}
	
	
	
	
}
