package dboperation;

import java.util.ArrayList;

import dbpojo.Oauth;
import dbpojo.Table;
import dbpojo.Userdata;
import exception.DBOperationException;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

public class OauthOperation {

	private static LoggerSet logger = new LoggerSet();

	public static Oauth addOauth(Oauth oauth, int userID) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };

		try {

			qg.openConnection();

			val = qg.insert(oauth).execute(userID);

			if (val[0] == -1) {

				qg.rollBackConnection();
				logger.logError("OauthOperation", "addOauth", "Failed to insert Oauth data", null);
				return null;
			}

			qg.commit();
			logger.logInfo("OauthOperation", "addOauth", "Oauth added successfully: " + oauth.getAccessToken());
			return oauth;

		} catch (Exception e) {
			logger.logError("OauthOperation", "addOauth", "Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	public static Oauth isOauthExist(String email, String provider, int userID) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		ArrayList<Table> result = new ArrayList<Table>();

		try {

			qg.openConnection();
			Oauth oauth = new Oauth();
			oauth.setEmail(email);
			oauth.setOauthProvider(provider);
			oauth.setUserID(userID);

			result = qg.select(oauth).executeQuery();

			if (result.size() <= 0) {

				qg.rollBackConnection();
				logger.logError("OauthOperation", "isOauthExist", "cannot find Oauth data", null);
				return null;
			}
			oauth = (Oauth) result.getFirst();
			qg.commit();
			logger.logInfo("OauthOperation", "isOauthExist", "Oauth data found: " + oauth.getEmail());
			return oauth;

		} catch (Exception e) {
			logger.logError("OauthOperation", "isOauthExist", "Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	public static Boolean updateOauth(Oauth oauth, int userID) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };

		try {

			qg.openConnection();

			val = qg.update(oauth).execute(userID);

			if (val[0] == -1) {

				qg.rollBackConnection();
				logger.logError("OauthOperation", "updateOauth", "cannot Update Oauth data", null);
				return false;
			}

			qg.commit();
			logger.logInfo("OauthOperation", "updateOauth", "Oauth data Updated successfully: " + oauth.getEmail());
			return true;

		} catch (Exception e) {
			logger.logError("OauthOperation", "updateOauth", "Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	public static void setOauthSynncOn(int oauthID, Userdata user) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };

		try {

			qg.openConnection();
			Oauth oauth = new Oauth();
			oauth.setID(oauthID);
			oauth.setSyncState(true);

			val = qg.update(oauth).execute(user.getID());

			if (val[0] == -1) {

				qg.rollBackConnection();
				logger.logError("OauthOperation", "setOauthSynncOn", "cannot Update Oauth data", null);
			}
			Oauth newOauth = new Oauth();
			newOauth.setUserID(user.getID());

			ArrayList<Table> result = qg.select(newOauth).executeQuery();

			if (result != null && result.size() > 0) {

				ArrayList<Oauth> oauths = new ArrayList<>();

				for (Table oauthdata : result) {

					oauths.add((Oauth) oauthdata);

				}

				user.setAllOauth(oauths);

			}

			qg.commit();
			logger.logInfo("OauthOperation", "setOauthSynncOn", "Oauth data Updated successfully: " + oauth.getEmail());

		} catch (

		Exception e) {
			logger.logError("OauthOperation", "setOauthSynncOn", "Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	public static void setOauthSynncOff(int oauthID, Userdata user) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };

		try {

			qg.openConnection();
			Oauth oauth = new Oauth();
			oauth.setID(oauthID);
			oauth.setSyncState(false);

			val = qg.update(oauth).execute(user.getID());
			

			if (val[0] == -1) {

				qg.rollBackConnection();
				logger.logError("OauthOperation", "setOauthSynncOff", "cannot Update Oauth data", null);

			}
			
			
			Oauth newOauth = new Oauth();
			newOauth.setUserID(user.getID());

			ArrayList<Table> result = qg.select(newOauth).executeQuery();

			if (result != null && result.size() > 0) {

				ArrayList<Oauth> oauths = new ArrayList<>();

				for (Table oauthdata : result) {

					oauths.add((Oauth) oauthdata);

				}

				user.setAllOauth(oauths);

			}

			qg.commit();
			logger.logInfo("OauthOperation", "setOauthSynncOff",
					"Oauth data Updated successfully: " + oauth.getEmail());

		} catch (Exception e) {
			logger.logError("OauthOperation", "setOauthSynncOff", "Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

	public static void deleteOauth(int oauthID, Userdata user) throws DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		int[] val = { -1, -1 };

		try {

			qg.openConnection();
			Oauth oauth = new Oauth();
			oauth.setID(oauthID);

			val = qg.delete(oauth).execute(user.getID());

			if (val[0] == -1) {

				qg.rollBackConnection();
				logger.logError("OauthOperation", "deleteOauthSynncOff", "cannot Delet Oauth data", null);

			}
			
			Oauth newOauth = new Oauth();
			newOauth.setUserID(user.getID());

			ArrayList<Table> result = qg.select(newOauth).executeQuery();

			if (result != null && result.size() > 0) {

				ArrayList<Oauth> oauths = new ArrayList<>();

				for (Table oauthdata : result) {

					oauths.add((Oauth) oauthdata);

				}

				user.setAllOauth(oauths);

			}

			qg.commit();
			logger.logInfo("OauthOperation", "deleteOauthSynncOff",
					"Oauth data deleted successfully: " + oauth.getEmail());

		} catch (Exception e) {
			logger.logError("OauthOperation", "deleteOauthSynncOff", "Exception occurred: " + e.getMessage(), e);

			qg.rollBackConnection();

			throw new DBOperationException(e.getMessage());
		} finally {

			qg.closeConnection();
		}

	}

}
