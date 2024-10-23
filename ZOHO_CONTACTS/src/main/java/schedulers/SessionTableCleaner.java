package schedulers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbconnect.DBconnection;
import loggerfiles.LoggerSet;

public class SessionTableCleaner implements Runnable {

	public void run() {
		System.out.println("hello im Session Table cleaner");
		Connection con = DBconnection.getConnection();
		LoggerSet logger = new LoggerSet();
		long sessionExpire;
		long currentTime;
		try {

			currentTime = System.currentTimeMillis() / 1000;
			PreparedStatement ps = con.prepareStatement("select Session_id,session_expire from Session;");
			ResultSet val = ps.executeQuery();
			while (val.next()) {
				sessionExpire = val.getLong(2);
				if (currentTime - sessionExpire < (5 * 60)) {

					ps = con.prepareStatement("delete from Session where Session_id=?");
					ps.setString(1, val.getString(1));
					ps.executeUpdate();
					

				}

			}

		} catch (Exception e) {

			System.out.println(e);
			logger.logError("Servletlistener", "SessionTableCleaner", "Error updating the session table: ", e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

}
