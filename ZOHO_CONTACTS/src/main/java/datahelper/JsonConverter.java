package datahelper;

import java.util.ArrayList;
import java.util.Map;

import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Session;
import dbpojo.Table;
import querybuilderconfig.TableSchema.Category_relation;
import querybuilderconfig.TableSchema.Contact_details;
import querybuilderconfig.TableSchema.Contact_mail;
import querybuilderconfig.TableSchema.Email_user;
import querybuilderconfig.TableSchema.Login_credentials;
import querybuilderconfig.TableSchema.user_data;

public class JsonConverter {

	public static StringBuilder ConvertPojoToJson(Table table, int userId) {
		StringBuilder json = new StringBuilder();
		ArrayList<String> parentKey = new ArrayList<String>();

		Map<String, Object> pojoData = table.getSettedData();

		pojoData.remove(user_data.modified_time.toString());

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

		json.append("\""+"parent"+"\""+":{");

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

			json.append(",");
			json.append("\"" + user_data.user_id + "\"");
			json.append(":");
			json.append(userId);

		}
		json.append("}");

		json.append("}");

		return json;
	}

	public static String[] comparePojoJson(Table previousPojoData, Table currentPojoData,int userID) {

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

		json[0] = ConvertPojoToJson(previousPojoData,userID).toString();
		json[1] = ConvertPojoToJson(currentPojoData,userID).toString();

		System.out.println("here see convert" + json[0]);

		System.out.println("here see convert" + json[1]);

		return json;
	}

	public static Boolean isParent(Table table, String column) {

		if (table instanceof LoginCredentials) {
			if (column.equals(Login_credentials.log_id.toString())) {
				return true;
			}
		} else if (table instanceof EmailUser) {
			if (column.equals(Email_user.em_id.toString())) {

				return true;
			}
		} else if (table instanceof ContactDetails) {
			if (column.equals(Contact_details.user_id.toString())) {

				return true;
			}

		} else if (table instanceof ContactMail || table instanceof ContactPhone) {

			if (column.equals(Contact_mail.contact_id.toString())) {
				return true;
			}
		} else if (table instanceof Category) {
			if (column.equals(querybuilderconfig.TableSchema.Category.created_by.toString())) {
				return true;
			}
		} else if (table instanceof CategoryRelation) {
			if (column.equals(Category_relation.Category_id.toString())
					|| column.equals(Category_relation.contact_id_to_join.toString())) {
				return true;
			}
		} else if (table instanceof Session) {
			if (column.equals(querybuilderconfig.TableSchema.Session.user_id.toString())) {

				return true;
			}
		}

		return false;
	}

}
