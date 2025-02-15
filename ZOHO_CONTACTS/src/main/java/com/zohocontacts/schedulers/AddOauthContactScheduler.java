package com.zohocontacts.schedulers;

import java.util.ArrayList;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dbpojo.Oauth;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.oauth2helper.Oauth2handler;
import com.zohocontacts.sessionstorage.CacheData;

public class AddOauthContactScheduler implements Runnable {

	QueryBuilder qg = null;
	
	public void run() {

		this.qg = new SqlQueryLayer().createQueryBuilder();

		this.qg.openConnection();
		AddGoogleContacts();
		this.qg.closeConnection();

	}

	private void AddGoogleContacts() {

		System.out.println("............................hello here  Delete Google contact performed");

		System.out.println(
				"............................hello here  Delete Google contact performed and the size here is.."
						+ CacheData.getDeleteCache().size());
		ArrayList<Table> result = new ArrayList<Table>();

		try {
			Map<String, Long> oauthDeleteContact = CacheData.getDeleteCache();
			for (String resourceName : oauthDeleteContact.keySet()) {
				System.out.println("............................hello " + resourceName);
				Oauth oauth = new Oauth();
				System.out.println(
						"OauthID" + oauthDeleteContact.get(resourceName) + " then resource name" + resourceName);
				oauth.setID(oauthDeleteContact.get(resourceName));
				result = qg.select(oauth).executeQuery();
				if (result.size() > 0) {
					Oauth oauthDB = (Oauth) result.getFirst();
					System.out.println("here the access token is" + oauthDB.getAccessToken());
					Oauth2handler.deleteOauthContact(resourceName, oauthDB.getAccessToken());
				}

			}

		} catch (Exception e) {

			System.out.println(e);
			LoggerSet.logError("deleteOauthContactScheduler", "deleteGoogleContacts", "Error In deleting google contact: ",
					e);
		} finally {

			CacheData.clearDeletecontact();
		}

	}

}
