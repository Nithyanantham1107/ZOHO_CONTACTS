package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconnect.DBconnection;
import dbmodel.UserContacts;

public class UserContactOperation {
	UserContacts uc;

	Connection con;

	public UserContacts addUserContact(UserContacts uc) throws SQLException {
		Connection con = DBconnection.getConnection();
		try {
		
			System.out.println(uc.getFname());
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement(
					"insert into Contact_details (user_id,First_name,Middle_name,Last_name,gender,Address) values(?,?,?,?,?,?);",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, uc.getUserid());
			ps.setString(2, uc.getFname());
			ps.setString(3, uc.getMname());
			ps.setString(4, uc.getLname());

			System.out.println(uc.getGender() + "here the gender!!!!");
			ps.setString(5, uc.getGender());
			ps.setString(6, uc.getAddress());
			System.out.println("here 1");
			int val = ps.executeUpdate();
			if (val == 0) {
				System.out.println("Error in inserting contact_details  Table");
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return null;
			}
			System.out.println("here 2");
			ResultSet id = ps.getGeneratedKeys();
			if (id.next()) {
				int gen_contact_id = id.getInt(1);
				uc.setContactid(gen_contact_id);
				ps = con.prepareStatement("insert into Contact_mail values(?,?);");
				ps.setInt(1, gen_contact_id);
				ps.setString(2, uc.getEmail());
				System.out.println("here 3");
				val = ps.executeUpdate();
				if (val == 0) {
					System.out.println("Error in inserting User_data Table");
					con.rollback();
					con.commit();
					con.setAutoCommit(true);
					return null;
				}
				System.out.println("here 4");
				ps = con.prepareStatement("insert into Contact_phone values(?,?);");
				ps.setInt(1, gen_contact_id);
				ps.setString(2, uc.getPhoneno());

				val = ps.executeUpdate();
				if (val == 0) {
					System.out.println("Error in inserting Email_user Table");
					con.rollback();
					con.commit();
					con.setAutoCommit(true);
					return null;
				}

			}
			con.commit();
			con.setAutoCommit(true);

			return uc;

		} catch (Exception e) {

			System.out.println(e);
		} finally {

			con.close();

		}
		return null;

	}

	public ArrayList<UserContacts> viewAllUserContacts(int user_id) throws SQLException {
		Connection con = DBconnection.getConnection();
		try {
			
			ArrayList<UserContacts> user_contacts = new ArrayList<UserContacts>();

			PreparedStatement ps = con.prepareStatement(
					"select * from Contact_details cd left join Contact_mail cm  on cd.contact_id=cm.contact_id left join Contact_phone cp on cp.contact_id=cd.contact_id where user_id=?;");

			ps.setInt(1, user_id);
			ResultSet contacts = ps.executeQuery();
			while (contacts.next()) {
				uc = new UserContacts();
				uc.setUserid(user_id);
				uc.setContactid(contacts.getInt(2));
				uc.setFname(contacts.getString(3));
				uc.setMname(contacts.getString(4));
				uc.setLname(contacts.getString(5));
				uc.setGender(contacts.getString(6));
				uc.setAddress(contacts.getString(7));
				uc.setEmail(contacts.getString(9));
				uc.setPhoneno(contacts.getString(11));
				user_contacts.add(uc);

			}

			return user_contacts;

		} catch (Exception e) {

			System.out.println(e);
		} finally {

			con.close();

		}

		return null;
	}

	public boolean deleteContact(int user_id, int contact_id) throws SQLException {
		Connection con = DBconnection.getConnection();
		try {
			
			System.out.println(user_id + "  here" + contact_id);
			PreparedStatement ps = con.prepareStatement("delete from Contact_details where user_id=? and contact_id=?");
			ps.setInt(1, user_id);
			ps.setInt(2, contact_id);
			int result = ps.executeUpdate();
			System.out.println(result);
			if (result > 0) {
				return true;
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			con.close();
		}
		return false;
	}

	public UserContacts viewSpecificUserContact(int user_id, int contact_id) throws SQLException {
		
		Connection con = DBconnection.getConnection();
		try {
		
			PreparedStatement ps = con.prepareStatement(
					"select * from Contact_details cd left join Contact_mail cm  on cd.contact_id=cm.contact_id left join Contact_phone cp on cp.contact_id=cd.contact_id where user_id=? and cd.contact_id=?;");
			ps.setInt(1, user_id);
			ps.setInt(2, contact_id);
			ResultSet contact = ps.executeQuery();

			if (contact.next()) {

				uc = new UserContacts();

				uc.setUserid(contact.getInt(1));
				uc.setContactid(contact.getInt(2));
				uc.setFname(contact.getString(3));
				uc.setMname(contact.getString(4));
				uc.setLname(contact.getString(5));
				uc.setGender(contact.getString(6));
				uc.setAddress(contact.getString(7));
				uc.setEmail(contact.getString(9));
				uc.setPhoneno(contact.getString(11));

			} else {
				System.out.println("No contact data available for this specic user_id");
				return null;
			}

			return uc;

		} catch (Exception e) {
			System.out.println(e);
		} finally {

			con.close();
		}

		return null;
	}

	public boolean updateSpecificUserContact(UserContacts uc) throws SQLException {
		Connection con = DBconnection.getConnection();
		try {
			
			System.out.println(uc.getFname());
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement(
					"update Contact_details set First_name=?,Middle_name=?,Last_name=?,gender=?,Address=?  where contact_id=?;");

			ps.setString(1, uc.getFname());
			ps.setString(2, uc.getMname());
			ps.setString(3, uc.getLname());
			ps.setString(4, uc.getGender());
			ps.setString(5, uc.getAddress());
			ps.setInt(6, uc.getContactid());

			int val = ps.executeUpdate();
			if (val == 0) {
				System.out.println("Error in updating contacts_details  Table");
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return false;
			}

			ps = con.prepareStatement("update Contact_mail set  contact_email_id=? where contact_id=? ;");

			ps.setString(1, uc.getEmail());
			ps.setInt(2, uc.getContactid());

			val = ps.executeUpdate();
			if (val == 0) {
				System.out.println("Error in updating Contact_mail Table");
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return false;
			}

			ps = con.prepareStatement("update Contact_phone set Contact_phone_no=? where contact_id=?;");

			ps.setString(1, uc.getPhoneno());
			ps.setInt(2, uc.getContactid());

			val = ps.executeUpdate();
			if (val == 0) {
				System.out.println("Error in updating  Contact_phone Table");
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return false;
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

}
