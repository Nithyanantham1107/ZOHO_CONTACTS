package schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import dbpojo.Oauth;
import dbpojo.Table;
import loggerfiles.LoggerSet;
import oauth2helper.Oauth2handler;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;
import sessionstorage.CacheData;

public class DeleteOauthContactScheduler implements Runnable {
	QueryBuilder qg = null;
	LoggerSet logger = new LoggerSet();

	public void run() {

		this.qg = new SqlQueryLayer().createQueryBuilder();

		this.qg.openConnection();
		deleteGoogleContacts();
		this.qg.closeConnection();

	}

	private void deleteGoogleContacts() {

		System.out.println("............................hello here  Delete Google contact performed");

		System.out.println(
				"............................hello here  Delete Google contact performed and the size here is.."
						+ CacheData.getDeleteCache().size());
		ArrayList<Table> result = new ArrayList<Table>();

		try {
			ConcurrentHashMap<String, Long> oauthDeleteContact = CacheData.getDeleteCache();
			for (String resourceName : oauthDeleteContact.keySet()) {
				System.out.println("............................hello " + resourceName);
				Oauth oauth = new Oauth();
				System.out.println("OauthID"+oauthDeleteContact.get(resourceName)+" then resource name"+resourceName);
				oauth.setID(oauthDeleteContact.get(resourceName));
				result = qg.select(oauth).executeQuery();
				if (result.size() > 0) {
					Oauth oauthDB=(Oauth) result.getFirst();
					System.out.println("here the access token is"+oauthDB.getAccessToken());
					Oauth2handler.deleteOauthContact(resourceName, oauthDB.getAccessToken());
				}

			}

		

		} catch (Exception e) {

			System.out.println(e);
			logger.logError("deleteOauthContactScheduler", "deleteGoogleContacts", "Error In deleting google contact: ",
					e);
		}finally {
			
			
			CacheData.clearDeletecontact();
		}

	}

}
