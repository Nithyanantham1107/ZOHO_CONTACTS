package com.zohocontacts.dboperation;

import java.util.ArrayList;
import java.util.List;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dbpojo.Oauth;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;

public class OauthOperation {

	

	public static Oauth addOauth(Oauth oauth, long userID) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			result = query.insert(oauth).execute(userID);

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("OauthOperation", "addOauth", "Failed to insert Oauth data", null);
				return null;
			}

			query.commit();
			LoggerSet.logInfo("OauthOperation", "addOauth", "Oauth added successfully: " + oauth.getAccessToken());
			return oauth;

		} catch (Exception e) {
			LoggerSet.logError("OauthOperation", "addOauth", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static Oauth isOauthExist(String email, String provider, long userID) throws DBOperationException {

		List<Table> resultList = new ArrayList<Table>();

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();
			Oauth oauth = new Oauth();
			oauth.setEmail(email);
			oauth.setOauthProvider(provider);
			oauth.setUserID(userID);

			resultList = query.select(oauth).executeQuery();

			if (resultList.size() <= 0) {

				query.rollBackConnection();
				LoggerSet.logError("OauthOperation", "isOauthExist", "cannot find Oauth data", null);
				return null;
			}
			oauth = (Oauth) resultList.getFirst();
			query.commit();
			LoggerSet.logInfo("OauthOperation", "isOauthExist", "Oauth data found: " + oauth.getEmail());
			return oauth;

		} catch (Exception e) {
			LoggerSet.logError("OauthOperation", "isOauthExist", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static Boolean updateOauth(Oauth oauth, long userID) throws DBOperationException {
		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();

			result = query.update(oauth).execute(userID);

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("OauthOperation", "updateOauth", "cannot Update Oauth data", null);
				return false;
			}

			query.commit();
			LoggerSet.logInfo("OauthOperation", "updateOauth", "Oauth data Updated successfully: " + oauth.getEmail());
			return true;

		} catch (Exception e) {
			LoggerSet.logError("OauthOperation", "updateOauth", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static void setOauthSynncOn(long oauthID, UserData user) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();
			Oauth oauth = new Oauth();
			oauth.setID(oauthID);
			oauth.setSyncState(true);

			result = query.update(oauth).execute(user.getID());

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("OauthOperation", "setOauthSynncOn", "cannot Update Oauth data", null);
			}
			Oauth newOauth = new Oauth();
			newOauth.setUserID(user.getID());

			List<Table> resultList = query.select(newOauth).executeQuery();

			if (resultList != null && resultList.size() > 0) {

				List<Oauth> oauths = new ArrayList<>();

				for (Table oauthdata : resultList) {

					oauths.add((Oauth) oauthdata);

				}

				user.setAllOauth(oauths);

			}

			query.commit();
			LoggerSet.logInfo("OauthOperation", "setOauthSynncOn", "Oauth data Updated successfully: " + oauth.getEmail());

		} catch (

		Exception e) {
			LoggerSet.logError("OauthOperation", "setOauthSynncOn", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

	public static void setOauthSynncOff(long oauthID, UserData user) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();
			Oauth oauth = new Oauth();
			oauth.setID(oauthID);
			oauth.setSyncState(false);

			result = query.update(oauth).execute(user.getID());

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("OauthOperation", "setOauthSynncOff", "cannot Update Oauth data", null);

			}

			Oauth newOauth = new Oauth();
			newOauth.setUserID(user.getID());

			List<Table> resultList = query.select(newOauth).executeQuery();

			if (resultList != null && resultList.size() > 0) {

				List<Oauth> oauths = new ArrayList<>();

				for (Table oauthTable : resultList) {

					oauths.add((Oauth) oauthTable);

				}

				user.setAllOauth(oauths);

			}

			query.commit();
			LoggerSet.logInfo("OauthOperation", "setOauthSynncOff",
					"Oauth data Updated successfully: " + oauth.getEmail());

		} catch (Exception e) {
			LoggerSet.logError("OauthOperation", "setOauthSynncOff", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}
	}

	public static void deleteOauth(long oauthID, UserData user) throws DBOperationException {

		int[] result = { -1, -1 };

		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder();) {

			query.openConnection();
			Oauth oauth = new Oauth();
			oauth.setID(oauthID);

			result = query.delete(oauth).execute(user.getID());

			if (result[0] == -1) {

				query.rollBackConnection();
				LoggerSet.logError("OauthOperation", "deleteOauthSynncOff", "cannot Delet Oauth data", null);

			}

			Oauth newOauth = new Oauth();
			newOauth.setUserID(user.getID());

			List<Table> resultList = query.select(newOauth).executeQuery();

			if (resultList != null && resultList.size() > 0) {

				List<Oauth> oauths = new ArrayList<>();

				for (Table oauthdata : resultList) {

					oauths.add((Oauth) oauthdata);

				}

				user.setAllOauth(oauths);

			}

			query.commit();
			LoggerSet.logInfo("OauthOperation", "deleteOauthSynncOff",
					"Oauth data deleted successfully: " + oauth.getEmail());

		} catch (Exception e) {
			LoggerSet.logError("OauthOperation", "deleteOauthSynncOff", "Exception occurred: " + e.getMessage(), e);

			throw new DBOperationException(e.getMessage());
		}

	}

}
