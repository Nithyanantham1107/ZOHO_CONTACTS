package schedulers;

import java.time.Instant;
import java.util.ArrayList;

import dboperation.UserContactOperation;
import dbpojo.ContactDetails;
import dbpojo.Oauth;
import dbpojo.Table;
import loggerfiles.LoggerSet;
import oauth2helper.Oauth2handler;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;
import sessionstorage.CacheData;
import sessionstorage.CacheModel;

public class UpdateAndDeleteQueue implements Runnable {
	QueryBuilder qg = null;
	LoggerSet logger = new LoggerSet();

	public void run() {

		this.qg = new SqlQueryLayer().createQueryBuilder();

		this.qg.openConnection();
		AccessTokenRenewal();
		syncGoogleContacts();
		updateSessionQueue();
//		sessionTableCleaner();
		this.qg.closeConnection();
	}

	private void syncGoogleContacts() {

		System.out.println("hello here sync Google contact performed");
		ArrayList<Table> result = new ArrayList<Table>();

		try {

			Oauth oauth = new Oauth();
			result = qg.select(oauth).executeQuery();
			if (result.size() > 0) {

				for (Table table : result) {

					Oauth oauthData = (Oauth) table;

					if (oauthData.getSyncState()) {

						ArrayList<ContactDetails> contacts = Oauth2handler.getContacts(oauthData);
						for (ContactDetails contact : contacts) {
							ContactDetails contactDB = UserContactOperation
									.viewOauthSpecificUserContact(oauthData.getUserID(), contact.getOauthContactID());
							if (contactDB == null) {
								UserContactOperation.addUserContact(contact);
							} else {
								
								contact.setID(contactDB.getID());
								UserContactOperation.updateSpecificUserContact(contact, oauthData.getUserID());
							}
						}

					}

				}

			}

		} catch (Exception e) {

			System.out.println(e);
			logger.logError("UpdateAndDeleteQueue", "syncGoogleContacts", "Error Syncing the google contact: ", e);
		}

	}

	private void AccessTokenRenewal() {

		ArrayList<Table> result = new ArrayList<>();
		System.out.println("hello im Access token Renewer");

		long currentTime;

		try {

			currentTime = Instant.now().toEpochMilli();
			Oauth oauth = new Oauth();
			result = this.qg.select(oauth).executeQuery();

			if (result.size() > 0) {

				for (Table data : result) {

					Oauth oauthData = (Oauth) data;

					if (oauthData.getExpiryTime() < currentTime && oauthData.getRefreshToken() != null) {
						Oauth oauthUpdate = Oauth2handler.refreshAccessToken(oauthData);

						this.qg.update(oauthUpdate).execute(oauthData.getUserID());

					}

				}

			}

		} catch (Exception e) {

			System.out.println(e);
			logger.logError("UpdateAndDeleteQueue", "AccessTokenRenewal", "Error updating the Oauth table: ", e);
		} 

	}

	private void updateSessionQueue() {

		int[] result = { -1, -1 };
		long userID = 0;
		System.out.println("hey here is the update queue");

		try {

			CacheData.setSecondaryActive();

			for (String sessionid : CacheData.getPrimaryUpdateQueue()) {
				CacheModel cachemodel = CacheData.getCache(sessionid);
				if (cachemodel != null) {

					System.out.println("here the session data" + sessionid + "  then last accessed stored"
							+ cachemodel.getsession(sessionid).getLastAccessed());

					dbpojo.Session session = new dbpojo.Session();
					session.setID(cachemodel.getsession(sessionid).getID());
					session.setLastAccessed(cachemodel.getsession(sessionid).getLastAccessed());
					userID = cachemodel.getsession(sessionid).getUserId();
					session.setUserId(userID);

					result = qg.update(session).execute(userID);

					if (result[0] == -1) {
						System.out.println("Error in updating session data to the table ,session id" + sessionid);

					}
					CacheData.removePrimaryUpdateQueue(sessionid);
				} else {
					System.out.println("here the cache data is null for session id" + sessionid);
				}

			}
			CacheData.setSecondaryInactive();
			CacheData.transferSecondaryToPrimary();

		} catch (Exception e) {
			System.out.println(e);
			logger.logError("UpdateAndDeleteQueue", "UpdateQueueSchedule", "Error updating the session table: ", e);
		} finally {

		}

	}

	

}
