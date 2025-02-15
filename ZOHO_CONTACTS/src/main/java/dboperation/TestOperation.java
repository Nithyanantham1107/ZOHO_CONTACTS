package dboperation;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;

import com.google.gson.Gson;

import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Table;
import dbpojo.Userdata;
import exception.DBOperationException;
import oauth2helper.Oauth2handler;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;
import sessionstorage.CacheData;

public class TestOperation {

	public static void main(String[] args) throws SQLException, DBOperationException, IOException {

		ArrayList<CategoryRelation> contacts = UserGroupOperation.viewUserGroupContact(81,86);
		System.out.println("size of contacts"+contacts.size());
		for (CategoryRelation contact : contacts) {
			System.out.println("the name of row ID" + contact.getID());
			System.out.println("the name of contact ID" + contact.getContactIDtoJoin());
			System.out.println("the name of gro ID" + contact.getCategoryID());

		}

	}

}
