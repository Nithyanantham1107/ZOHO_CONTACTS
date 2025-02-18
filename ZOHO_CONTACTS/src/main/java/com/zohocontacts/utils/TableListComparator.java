package com.zohocontacts.utils;

import java.util.ArrayList;
import java.util.List;

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
}
