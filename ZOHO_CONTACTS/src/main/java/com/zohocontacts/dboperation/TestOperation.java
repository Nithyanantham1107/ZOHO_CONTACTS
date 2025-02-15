package com.zohocontacts.dboperation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.QueryBuilder;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.SqlQueryLayer;
import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.ContactMail;
import com.zohocontacts.dbpojo.ContactPhone;
import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.exception.DBOperationException;

public class TestOperation {

	public static void main(String[] args) throws SQLException, DBOperationException, IOException {

//		ArrayList<CategoryRelation> contacts = UserGroupOperation.viewUserGroupContact(81,86);
//		System.out.println("size of contacts"+contacts.size());
//		for (CategoryRelation contact : contacts) {
//			System.out.println("the name of row ID" + contact.getID());
//			System.out.println("the name of contact ID" + contact.getContactIDtoJoin());
//			System.out.println("the name of gro ID" + contact.getCategoryID());
//
//		}

		ContactDetails contact = new ContactDetails();
		contact.setID(264);
		contact.setContactMail(new ContactMail());
		contact.setContactPhone(new ContactPhone());
		ArrayList<Table> result = new ArrayList<Table>();
		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		qg.openConnection();

		result = qg.select(contact).executeQuery();
		if (result != null && result.size() > 0) {
			ContactDetails contactDB = (ContactDetails) result.getFirst();
			if (contactDB.getAllContactMail() != null) {
				System.out.println("the mail of row ID" + contactDB.getAllContactMail().getFirst().getContactMailID());
			}

			if (contactDB.getAllContactphone() != null) {

				System.out.println("the phone of row ID" + contactDB.getAllContactphone().getFirst().getContactPhone());

			}

		}

	}

}
