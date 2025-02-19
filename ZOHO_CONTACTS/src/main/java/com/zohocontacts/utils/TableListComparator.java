package com.zohocontacts.utils;

import java.util.ArrayList;
import java.util.List;

import com.zohocontacts.dbpojo.ContactDetails;
import com.zohocontacts.dbpojo.ContactMail;
import com.zohocontacts.dbpojo.ContactPhone;

public class TableListComparator {

	public static List<ContactMail> deleteTableMailListComparator(List<ContactMail> dbContactMail,
			List<ContactMail> clientContactMail) {
		List<ContactMail> deleteTableList = new ArrayList<ContactMail>();
		for (ContactMail dbTable : dbContactMail) {
			Boolean isExist = false;
			for (ContactMail table : clientContactMail) {

				if (table.getID() == dbTable.getID()) {

					isExist = true;
					break;

				}

			}

			if (!isExist) {

				deleteTableList.add(dbTable);
			}

		}

		return deleteTableList;

	}

	public static List<ContactPhone> deleteTablePhoneListComparator(List<ContactPhone> dbContactPhone,
			List<ContactPhone> clientPhoneList) {
		List<ContactPhone> deleteTableList = new ArrayList<ContactPhone>();
		for (ContactPhone dbTable : dbContactPhone) {
			Boolean isExist = false;
			for (ContactPhone table : clientPhoneList) {

				if (table.getID() == dbTable.getID()) {

					isExist = true;
					break;

				}

			}

			if (!isExist) {

				deleteTableList.add(dbTable);
			}

		}

		return deleteTableList;

	}

	public static ContactDetails contactComparater(ContactDetails contactDB, ContactDetails userContact) {

		ContactDetails comparedContact = new ContactDetails();

		if (!contactDB.getFirstName().equals(userContact.getFirstName())) {
			comparedContact.setFirstName(userContact.getFirstName());

		}

		if (!contactDB.getMiddleName().equals(userContact.getMiddleName())) {
			comparedContact.setMiddleName(userContact.getMiddleName());

		}

		if (!contactDB.getLastName().equals(userContact.getLastName())) {
			comparedContact.setLastName(userContact.getLastName());

		}

		if (!contactDB.getGender().equals(userContact.getGender())) {
			comparedContact.setGender(userContact.getGender());

		}
		if (!contactDB.getAddress().equals(userContact.getAddress())) {
			comparedContact.setAddress(userContact.getAddress());

		}

		if (!contactDB.getOauthContactID().equals(userContact.getOauthContactID())) {
			comparedContact.setOauthContactID(userContact.getOauthContactID());

		}

		if (contactDB.getOauthContactModifiedTime() != userContact.getOauthContactModifiedTime()) {
			comparedContact.setOauthcContactModifiedTime(userContact.getOauthContactModifiedTime());

		}

		comparedContact.setAllContactMail(userContact.getAllContactMail());

		comparedContact.setAllContactPhone(userContact.getAllContactphone());

		return comparedContact;

	}
}
