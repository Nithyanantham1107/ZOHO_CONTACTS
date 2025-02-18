package com.zohocontacts.schedulers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dboperation.UserContactOperation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.Oauth;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.oauth2helper.Oauth2handler;

public class SyncGoogleContacts implements Runnable {
	private static final int SYNCOAUTHTIME = 30 * 60 * 1000;

	public void run() {
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder()) {

			query.openConnection();

			syncGoogleContacts(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void syncGoogleContacts(QueryBuilder query) {

		System.out.println("hello here sync Google contact performed");
		List<Table> resultList = new ArrayList<Table>();
		int[] result = { -1, -1 };
		try {
			long currentTime = Instant.now().toEpochMilli();
			Oauth oauth = new Oauth();
			resultList = query.select(oauth).executeQuery();
			if (resultList.size() > 0) {

				for (Table table : resultList) {

					Oauth oauthData = (Oauth) table;
					boolean isAlreadySynced = currentTime < (oauthData.getOauthSyncTime() + SYNCOAUTHTIME);
					if (oauthData.getSyncState() && !isAlreadySynced) {

						List<ContactDetails> contacts = Oauth2handler.getContacts(oauthData);
						for (ContactDetails contact : contacts) {
							ContactDetails contactDB = UserContactOperation
									.viewOauthSpecificUserContact(oauthData.getUserID(), contact.getOauthContactID());
							if (contactDB == null) {
								contact.setCreatedAt(contact.getModifiedAt());
								UserContactOperation.addUserContact(contact);
							} else {

								if (!(contactDB.getModifiedAt() > contactDB.getOauthContactModifiedTime())) {

									contact.setID(contactDB.getID());
									UserContactOperation.updateSpecificUserContact(contact, oauthData.getUserID());
								}
							}
						}

					}

					oauth.setID(oauthData.getID());
					oauth.setOauthSyncTime(Instant.now().toEpochMilli());
					result = query.update(oauth).execute(oauthData.getUserID());
					if (result[0] == -1) {

						LoggerSet.logWarning("SyncGoogleContacts", "syncGoogleContacts",
								"Error in updating the Oauth sync time ");

					}
				}

			}

		} catch (Exception e) {

			System.out.println(e);
			LoggerSet.logError("SyncGoogleContacts", "syncGoogleContacts", "Error Syncing the google contact: ", e);
		}

	}

}
