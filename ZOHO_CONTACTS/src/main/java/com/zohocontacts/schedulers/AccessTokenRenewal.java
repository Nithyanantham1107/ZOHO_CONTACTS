package com.zohocontacts.schedulers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dbpojo.Oauth;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.oauth2helper.Oauth2handler;

public class AccessTokenRenewal implements Runnable {

	public void run() {
		try (QueryBuilder query = new SqlQueryLayer().createQueryBuilder()) {

			query.openConnection();
			googleAccessTokenRenewal(query);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void googleAccessTokenRenewal(QueryBuilder query) {

		List<Table> result = new ArrayList<>();
		System.out.println("hello im Access token Renewer");

		long currentTime;

		try {

			currentTime = Instant.now().toEpochMilli();
			Oauth oauth = new Oauth();
			result = query.select(oauth).executeQuery();

			if (result.size() > 0) {

				for (Table data : result) {

					Oauth oauthData = (Oauth) data;

					if (oauthData.getExpiryTime() < currentTime && oauthData.getRefreshToken() != null) {
						Oauth oauthUpdate = Oauth2handler.refreshAccessToken(oauthData);

						query.update(oauthUpdate).execute(oauthData.getUserID());

					}

				}

			}

		} catch (Exception e) {

			System.out.println(e);
			LoggerSet.logError("AccessTokenRenewal", "AccessTokenRenewal", "Error updating the Oauth table: ", e);
		}

	}

}
