package com.zohocontacts.dataquerybuilder.querybuilder.datahelper;

import java.util.ArrayList;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategoryRelationSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.CategorySchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.ContactDetailsSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.ContactMailSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.EmailUserSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.LoginCredentialsSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.OauthSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.SessionSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.UserDataSchema;
import com.zohocontacts.dbpojo.CategoryRelation;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.ContactMail;
import com.zohocontacts.dbpojo.ContactPhone;
import com.zohocontacts.dbpojo.EmailUser;
import com.zohocontacts.dbpojo.LoginCredentials;
import com.zohocontacts.dbpojo.Oauth;
import com.zohocontacts.dbpojo.tabledesign.Table;

public class JsonConverter {

	public static StringBuilder ConvertPojoToJson(Table table, long userId) {
		StringBuilder json = new StringBuilder();
		ArrayList<String> parentKey = new ArrayList<String>();

		Map<String, Object> pojoData = table.getSettedData();

		pojoData.remove(UserDataSchema.MODIFIEDTIME.toString());

		json.append("{");

		for (Map.Entry<String, Object> data : pojoData.entrySet()) {

			if (!isParent(table, data.getKey())) {

				json.append("\"" + data.getKey() + "\"");
				json.append(':');

				if (data.getValue() instanceof String) {
					json.append("\"" + data.getValue() + "\"");

				} else {

					json.append(data.getValue());
				}
				json.append(",");

			} else {

				parentKey.add(data.getKey());
			}

		}

		json.append("\"" + "parent" + "\"" + ":{");

		for (int i = 0; i < parentKey.size(); i++) {

			json.append("\"" + parentKey.get(i) + "\"");
			json.append(":");
			if (pojoData.get(parentKey.get(i)) instanceof String) {
				json.append("\"" + pojoData.get(parentKey.get(i)) + "\"");

			} else {

				json.append(pojoData.get(parentKey.get(i)));
			}

			if (i < parentKey.size() - 1) {
				json.append(",");
			}

		}

		if (table instanceof CategoryRelation || table instanceof ContactMail || table instanceof ContactPhone) {
			if (parentKey.size() > 0) {
				json.append(",");
			}

			json.append("\"" + UserDataSchema.USERID.getColumnName() + "\"");
			json.append(":");
			json.append(userId);

		}
		json.append("}");

		json.append("}");

		return json;
	}

	public static String[] comparePojoJson(Table previousPojoData, Table currentPojoData, long userID) {

		String[] json = new String[2];
		ArrayList<String> keysToRemove = new ArrayList<String>();

		Map<String, Object> oldData = previousPojoData.getSettedData();
		Map<String, Object> newData = currentPojoData.getSettedData();

		for (Map.Entry<String, Object> data : oldData.entrySet()) {
			if (!newData.containsKey(data.getKey())) {
				keysToRemove.add(data.getKey());
			}

		}
		for (String key : keysToRemove) {

			oldData.remove(key);
		}

		json[0] = ConvertPojoToJson(previousPojoData, userID).toString();
		json[1] = ConvertPojoToJson(currentPojoData, userID).toString();

		System.out.println("here see convert" + json[0]);

		System.out.println("here see convert" + json[1]);

		return json;
	}

	public static Boolean isParent(Table table, String column) {

		if (table instanceof LoginCredentials) {
			if (column.equals(LoginCredentialsSchema.LOGID.getColumnName())) {
				return true;
			}
		} else if (table instanceof EmailUser) {
			if (column.equals(EmailUserSchema.EMAILID.getColumnName())) {

				return true;
			}
		} else if (table instanceof ContactDetails) {
			if (column.equals(ContactDetailsSchema.USERID.getColumnName())) {

				return true;
			}

		} else if (table instanceof ContactMail || table instanceof ContactPhone) {

			if (column.equals(ContactMailSchema.CONTACTID.getColumnName())) {
				return true;
			}
		} else if (table instanceof CategoryRelation) {
			if (column.equals(CategorySchema.CREATEDBY.getColumnName())) {
				return true;
			}
		} else if (table instanceof CategoryRelation) {
			if (column.equals(CategoryRelationSchema.CATEGORYID.getColumnName())
					|| column.equals(CategoryRelationSchema.CONTACTIDTOJOIN.getColumnName())) {
				return true;
			}
		} else if (table instanceof com.zohocontacts.dbpojo.Session) {
			if (column.equals(SessionSchema.USERID.getColumnName())) {

				return true;
			}
		} else if (table instanceof Oauth) {
			if (column.equals(OauthSchema.USERID.getColumnName())) {

				return true;
			}
		}

		return false;
	}

}
