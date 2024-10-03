package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.cj.protocol.Resultset;

import dbconnect.DBconnection;
import dbmodel.UserGroup;

public class UserGroupOperation {
   UserGroup ug;
	public boolean createGroup(UserGroup ug) throws SQLException {
		Connection con = DBconnection.getConnection();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement("insert into Category (Category_name,created_by) values(?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, ug.getGroupName());
			ps.setInt(2, ug.getUserid());
			int val = ps.executeUpdate();
			if (val == 0) {
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return false;
			}
			ResultSet groups=ps.getGeneratedKeys();
			int groupid;
			if(groups.next()) {
				groupid=groups.getInt(1);
			}else {
				return false;
			}
			
			
			System.out.println("it execute her!!");
			for(int i : ug.getContacId()) {
				System.out.println("contact data"+i);
				
				ps=con.prepareStatement("insert into Category_relation values(?,?);");
				ps.setInt(1, i);
				ps.setInt(2, groupid);
				val=ps.executeUpdate();
				if(val==0) {
					con.rollback();
					con.commit();
					con.setAutoCommit(true);
					return false;
					
				}
				
			}
			
			con.commit();
			con.setAutoCommit(true);
			return true;

		} catch (Exception e) {
			System.out.println(e);

		}finally {

			con.close();
		}

		return false;

	}
	public ArrayList<UserGroup> viewAllGroup(int userid) throws SQLException{
		ArrayList<UserGroup> usergroups=new ArrayList<>();
		Connection con = DBconnection.getConnection();
		
		try {
			
	PreparedStatement ps=con.prepareStatement("select * from Category where created_by=?");
	ps.setInt(1, userid);
	ResultSet val=ps.executeQuery();

	while(val.next()) {
		ug=new UserGroup();
		ug.setGroupid(val.getInt(1));
		ug.setGroupName(val.getString(2));
		ug.setUserid(val.getInt(3));
		usergroups.add(ug);
		
		
		
		
	}
	return usergroups;
		
		
		
	
	
			
			
			
		}catch(Exception e) {
			System.out.println(e);
		}finally {

			con.close();
		}
		return null;
		
	}
	
	public Boolean deleteUserGroup(int groupid) throws SQLException {
		Connection con=DBconnection.getConnection();
		try {
			
			PreparedStatement ps=con.prepareStatement("delete from Category where Category_id=?;");
			ps.setInt(1, groupid);
			int val=ps.executeUpdate();
			if(val==0) {
				
				System.out.println("Error in Deleting the group data in Category table");
				return false;
			}
			
			
			return true;
			
		}catch(Exception e) {
			System.out.println(e);
		}finally {

			con.close();
		}
		
		
		return false;
	}
	
	public int[] viewUserGroupContact(int groupid,int userid) throws SQLException {
		Connection con=DBconnection.getConnection();
	
		try {
		
			ArrayList<Integer> data=new ArrayList<Integer>();
			PreparedStatement ps=con.prepareStatement("select * from Category c left join Category_relation cr on c.Category_id=cr.Category_id where created_by=? and c.Category_id=?;");
			ps.setInt(1, userid);
			ps.setInt(2, groupid);
			ResultSet val=ps.executeQuery();
			System.out.print("from database");
			while(val.next()) {
				data.add(val.getInt(4));
				System.out.print(val.getInt(4));
				
			}
			int[] contactid = new int[data.size()];
			System.out.print("from local data");
			for(int i=0;i<data.size();i++) {
				
				
				contactid[i]=data.get(i);
				System.out.print(contactid[i]);
			}
			return contactid;
			
			
		}catch(Exception e) {
			
			System.out.println(e);
		}finally {

			con.close();
		}
		
		
		return null;
		
	}
	
	
	public boolean updateUserGroup(UserGroup ug) throws SQLException {
		Connection con =DBconnection.getConnection();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement("update Category set Category_name=?,  where Category_id=?;");
			ps.setString(1, ug.getGroupName());
			ps.setInt(2, ug.getUserid());
			int val = ps.executeUpdate();
			if (val == 0) {
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return false;
			}
			
			
			
			System.out.println("it execute here for update!!");
			
			
			ps=con.prepareStatement("delete from Category_relation where Category_id=?;");
			ps.setInt(1, ug.getGroupid());
			val=ps.executeUpdate();
			if(val==0) {
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return false;
			}
			
			for(int i : ug.getContacId()) {
				
				ps=con.prepareStatement("insert into Category_relation values(?,?);");
				ps.setInt(1, i);
				ps.setInt(2, ug.getGroupid());
				val=ps.executeUpdate();
				if(val==0) {
					con.rollback();
					con.commit();
					con.setAutoCommit(true);
					return false;
					
				}
				
			}
			
			con.commit();
			con.setAutoCommit(true);
			return true;
			
			
			
		}catch(Exception e) {
			
			System.out.println(e);
		}finally {

			con.close();
		}
		return false;
	}
}
