package dboperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql.Timestamp;

import javax.servlet.http.Cookie;

import org.mindrot.jbcrypt.BCrypt;

import dbconnect.DBconnection;

public class SessionOperation {

	public String generateSessionId(int user_id) throws SQLException {
		int sessiontimeout = 30 * 60 * 1000;
		String uuid = UUID.randomUUID().toString();
		long timestamp = System.currentTimeMillis();
		String sessionplaintext = uuid + timestamp + user_id;
		String sessionid = BCrypt.hashpw(sessionplaintext, BCrypt.gensalt());
		Timestamp sessionexpire = new Timestamp(timestamp + sessiontimeout);
		System.out.println("here created session id is" + sessionid);
		Connection con = DBconnection.getConnection();
		try {

			con.setAutoCommit(false);
			PreparedStatement ps = con.prepareStatement("insert into  Session values(?,?,?);");
			ps.setString(1, sessionid);
			ps.setTimestamp(2, sessionexpire);
			ps.setInt(3, user_id);
			int val = ps.executeUpdate();
			if (val == 0) {

				System.out.println("Error in inserting session Table");
				con.rollback();
				con.commit();
				con.setAutoCommit(true);
				return null;

			}

			con.commit();
			con.setAutoCommit(true);
			return sessionid;
		} catch (Exception e) {

			System.out.println(e);

		} finally {
			con.close();

		}

		return null;

	}

	public String getCustomSessionId(Cookie[] cookies) {

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("SESSIONID".equals(cookie.getName())) {
					
					System.out.println(cookie.getValue()+"here see the cookie value");
					
					return cookie.getValue();
				}
			}
		}
		
		return null;
	}
	
	
	public boolean DeleteSessionData(String sessionid) throws SQLException {
		Connection con=DBconnection.getConnection();
		try {
			if(sessionid !=null) {
			PreparedStatement ps=con.prepareStatement("delete from Session where session_id=?;");
			ps.setString(1, sessionid);
			int val=ps.executeUpdate();
			if(val==0) {
				System.out.println("error in deleting the session");
				return false;
			}
			}else {
				
				System.out.println("Sessionid is null ");
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
	
	
	public int checkSessionAlive(String sessionid) throws SQLException {
		int userid=0;
		Connection con =DBconnection.getConnection();
		try {
			
	    		
			
	    PreparedStatement ps=con.prepareStatement("select * from Session where Session_id=?");
	    ps.setString(1, sessionid);
	    ResultSet val=ps.executeQuery();
	    Timestamp currenttime=new Timestamp(System.currentTimeMillis());
	    if(val.next()) {
	    	userid=val.getInt(3);
	    	 int timestatus= currenttime.compareTo(val.getTimestamp(2));
	       if(timestatus>0) {
	    	  
	    	   return 0;
	    	   
	       }
	    }
	    con.setAutoCommit(false);
	    currenttime=new Timestamp(System.currentTimeMillis()+(30*60*1000));
	    ps=con.prepareStatement("update Session set session_expire=? where Session_id=?");
	 
	    ps.setTimestamp(1, currenttime);
	    ps.setString(2, sessionid);
	    int result=ps.executeUpdate();
	    if(result==0) {
	    	System.out.println("updating the session table");
	    	
	    	con.rollback();
			con.commit();
			con.setAutoCommit(true);
	    	
	    	return 0;
	    }
			
	    return userid;
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			con.close();
		}
		
		return 0;
		
	}

}
