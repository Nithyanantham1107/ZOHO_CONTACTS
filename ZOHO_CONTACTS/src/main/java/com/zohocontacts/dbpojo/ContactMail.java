package com.zohocontacts.dbpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.ContactMailSchema;
import com.zohocontacts.dataquerybuilder.querybuilderconfig.TableSchema.ContactPhoneSchema;
import com.zohocontacts.dbpojo.tabledesign.Table;

public class ContactMail implements Table {
	private long id = -1;
	private long contactID = -1;
	private String contactEmailID;
	private long createdAt = -1;
	private String labelName;
	private long modifiedAt = -1;
	private Map<String, Object> settedData = new HashMap<String, Object>();

	public ContactMail(Map<String, Object> tableData) {

		settedData.clear();

		if (tableData.get(ContactMailSchema.ID.getColumnName()) != null) {

			setID((long) tableData.get(ContactMailSchema.ID.getColumnName()));
		}

		if (tableData.get(ContactMailSchema.CONTACTMAILID.getColumnName()) != null) {

			setContactMailID((String) tableData.get(ContactMailSchema.CONTACTMAILID.getColumnName()));
		}

		if (tableData.get(ContactMailSchema.CONTACTID.getColumnName()) != null) {

			setContactID((long) tableData.get(ContactMailSchema.CONTACTID.getColumnName()));
		}

		if (tableData.get(ContactMailSchema.CREATEDTIME.getColumnName()) != null) {

			setCreatedAt((long) tableData.get(ContactMailSchema.CREATEDTIME.getColumnName()));
		}
		if (tableData.get(ContactMailSchema.MODIFIEDTIME.getColumnName()) != null) {

			setModifiedAt((long) tableData.get(ContactMailSchema.MODIFIEDTIME.getColumnName()));
		}

		if (tableData.get(ContactPhoneSchema.LABELNAME.getColumnName()) != null) {

			setLabelName((String) tableData.get(ContactPhoneSchema.LABELNAME.getColumnName()));
		}
	}

	public ContactMail() {

	}

	public void setLabelName(String labelName) {

		this.labelName = labelName;
		settedData.put(ContactMailSchema.LABELNAME.getColumnName(), getLabelName());

	}

	public String getLabelName() {

		return this.labelName;
	}

	public String getTableName() {

		return ContactMailSchema.ID.getTableName();
	}

	public String getPrimaryIDName() {

		return ContactMailSchema.ID.getPrimaryKey();
	}

	public void setID(long id) {

		this.id = id;
		settedData.put(ContactMailSchema.ID.getColumnName(), getID());
	}

	public long getID() {
		return this.id;
	}

	public void setCreatedAt(long createdAt) {

		this.createdAt = createdAt;
		settedData.put(ContactMailSchema.CREATEDTIME.getColumnName(), getCreatedAt());
	}

	public long getCreatedAt() {

		return this.createdAt;
	}

	public void setModifiedAt(long modifiedAt) {

		this.modifiedAt = modifiedAt;
		settedData.put(ContactMailSchema.MODIFIEDTIME.getColumnName(), getModifiedAt());
	}

	public long getModifiedAt() {

		return this.modifiedAt;

	}

	public void setContactID(long ContactID) {
		this.contactID = ContactID;
		settedData.put(ContactMailSchema.CONTACTID.getColumnName(), getContactID());
	}

	public long getContactID() {
		return this.contactID;
	}

	public void setContactMailID(String ContactMailID) {
		this.contactEmailID = ContactMailID;

		settedData.put(ContactMailSchema.CONTACTMAILID.getColumnName(), getContactMailID());
	}

	public String getContactMailID() {
		return contactEmailID;
	}

	public Map<String, Object> getSettedData() {

		return settedData;
	}

	@Override
	public List<String> getTableColumnNames() {

		return ContactMailSchema.ID.getColumns();
	}

	@Override
	public Table getNewTable(Map<String, Object> tableData) {

		return new ContactMail(tableData);
	}

}
