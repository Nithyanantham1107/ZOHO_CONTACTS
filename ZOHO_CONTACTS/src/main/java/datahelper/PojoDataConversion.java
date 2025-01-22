package datahelper;

import java.util.LinkedList;
import java.util.Queue;

import dbpojo.AuditLog;
import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Session;
import dbpojo.Table;
import dbpojo.Userdata;
import querybuilderconfig.TableSchema.Audit_log;
import querybuilderconfig.TableSchema.Category_relation;
import querybuilderconfig.TableSchema.Contact_details;
import querybuilderconfig.TableSchema.Contact_mail;
import querybuilderconfig.TableSchema.Contact_phone;
import querybuilderconfig.TableSchema.Email_user;
import querybuilderconfig.TableSchema.Login_credentials;
import querybuilderconfig.TableSchema.user_data;

public class PojoDataConversion {

	private static Queue<String> pojoKey;
	private static Queue<Object> pojoValue;
	private static StringBuilder json = new StringBuilder();
	private static int id = -1;

	public static changedStateContainer getChang1edPojoData(Table newData, Table oldData) {

//		StringBuilder json = null;
		pojoKey = new LinkedList<>();
		pojoValue = new LinkedList<>();
		json.setLength(0);

		changedStateContainer changedState = new changedStateContainer();
		if (newData == null || oldData == null) {
			return changedState;
		}

		if (oldData instanceof Userdata && newData instanceof Userdata) {
			userDataConverter(newData, oldData);

		} else if (oldData instanceof Category && newData instanceof Category) {
			categoryConverter(newData, oldData);

		} else if (oldData instanceof CategoryRelation && newData instanceof CategoryRelation) {

			categoryRelationConverter(newData, oldData);

		} else if (oldData instanceof ContactDetails && newData instanceof ContactDetails) {
			contactDetailsConverter(newData, oldData);

		} else if (oldData instanceof ContactMail && newData instanceof ContactMail) {
			contactMailConverter(newData, oldData);
		} else if (oldData instanceof ContactPhone && newData instanceof ContactPhone) {
			contactPhoneConverter(newData, oldData);
		} else if (oldData instanceof LoginCredentials && newData instanceof LoginCredentials) {
			loginCredentialsConverter(newData, oldData);
		} else if (oldData instanceof Session && newData instanceof Session) {
			sessionConverter(newData, oldData);
		} else if (oldData instanceof EmailUser && newData instanceof EmailUser) {
			emailConverter(newData, oldData);
		} else if (oldData instanceof AuditLog && newData instanceof AuditLog) {

			auditLogConverter(newData, oldData);
		}

		changedState.setOldData(oldData);
		changedState.setNewData(newData);

		return changedState;

	}

	public static PojoDataContainer convertPojoData(Table newData) {

//		StringBuilder json = null;
		pojoKey = new LinkedList<>();

		pojoValue = new LinkedList<>();
		json.setLength(0);

		id = -1;
		Table oldData = null;

		if (newData instanceof Userdata) {
			json = userDataConverter(newData, oldData);

		} else if (newData instanceof Category) {
			json = categoryConverter(newData, oldData);

		} else if (newData instanceof CategoryRelation) {

			json = categoryRelationConverter(newData, oldData);

		} else if (newData instanceof ContactDetails) {
			json = contactDetailsConverter(newData, oldData);

		} else if (newData instanceof ContactMail) {
			json = contactMailConverter(newData, oldData);
		} else if (newData instanceof ContactPhone) {
			json = contactPhoneConverter(newData, oldData);
		} else if (newData instanceof LoginCredentials) {
			json = loginCredentialsConverter(newData, oldData);
		} else if (newData instanceof Session) {
			json = sessionConverter(newData, oldData);
		} else if (newData instanceof EmailUser) {
			json = emailConverter(newData, oldData);
		} else if (newData instanceof AuditLog) {

			json = auditLogConverter(newData, oldData);
		}

		json.deleteCharAt(json.length() - 1);
		json.append("}");
		System.out.println("data " + json.toString());

		PojoDataContainer pojoDataContainer = new PojoDataContainer();
		pojoDataContainer.setID(id);
		pojoDataContainer.setJson(json.toString());
		pojoDataContainer.setPojoKey(pojoKey);
		pojoDataContainer.setPojoValue(pojoValue);

		return pojoDataContainer;

	}

	private static StringBuilder userDataConverter(Table data, Table previousData) {
		Userdata currentUserData = (Userdata) data;
		Userdata previousUserData = null;

		if (previousData != null) {
			previousUserData = (Userdata) previousData;

		}

		StringBuilder json = new StringBuilder("{");

		pojoIDSetter(user_data.user_id, currentUserData, previousUserData, json);
		pojoCreatedTimeSetter(user_data.created_time, currentUserData, previousUserData, json);
		pojoModifiedTimeSetter(user_data.modified_time, currentUserData, previousUserData, json);

		if (currentUserData.getName() != null) {

			if (previousUserData != null && (previousUserData.getName().equals(currentUserData.getName()))) {

				previousUserData.setName(null);
				currentUserData.setName(null);
			} else {

				pojoJsonDataFiller(user_data.Name, currentUserData.getName(), json);

				pojoKeyValueFiller(user_data.Name, currentUserData.getName());

			}

		}
		if (currentUserData.getPassword() != null) {

			if (previousUserData != null && (previousUserData.getPassword().equals(currentUserData.getPassword()))) {

				previousUserData.setPassword(null);
				currentUserData.setPassword(null);
			} else {

				pojoJsonDataFiller(user_data.password, currentUserData.getPassword(), json);
				pojoKeyValueFiller(user_data.password, currentUserData.getPassword());

			}

		}
		if (currentUserData.getPhoneno() != null) {

			if (previousUserData != null && (previousUserData.getPhoneno().equals(currentUserData.getPhoneno()))) {

				previousUserData.setPhoneno(null);
				currentUserData.setPhoneno(null);
			} else {

				pojoKeyValueFiller(user_data.phone_no, currentUserData.getPhoneno());

				pojoJsonDataFiller(user_data.phone_no, currentUserData.getPhoneno(), json);

			}

		}
		if (currentUserData.getAddress() != null) {

			if (previousUserData != null && (previousUserData.getAddress()).equals(currentUserData.getAddress())) {

				previousUserData.setAddress(null);
				currentUserData.setAddress(null);
			} else {

				pojoJsonDataFiller(user_data.address, currentUserData.getAddress(), json);
				pojoKeyValueFiller(user_data.address, currentUserData.getAddress());

			}

		}

		if (currentUserData.getTimezone() != null) {

			if (previousUserData != null && (previousUserData.getTimezone().equals(currentUserData.getTimezone()))) {

				previousUserData.setTimezone(null);
				currentUserData.setTimezone(null);
			} else {

				pojoJsonDataFiller(user_data.timezone, currentUserData.getTimezone(), json);
				pojoKeyValueFiller(user_data.timezone, currentUserData.getTimezone());

			}

		}

		return json;

	}

	private static StringBuilder categoryRelationConverter(Table data, Table previousData) {
		CategoryRelation currentCategoryRelation = (CategoryRelation) data;
		CategoryRelation previousCategoryRelation = null;

		if (previousData != null) {
			previousCategoryRelation = (CategoryRelation) previousData;
		}

		StringBuilder json = new StringBuilder("{");

		pojoIDSetter(Category_relation.ID, currentCategoryRelation, previousCategoryRelation, json);
		pojoCreatedTimeSetter(Category_relation.created_time, currentCategoryRelation, previousCategoryRelation, json);
		pojoModifiedTimeSetter(Category_relation.modified_time, currentCategoryRelation, previousCategoryRelation,
				json);
		if (currentCategoryRelation.getCategoryID() != -1) {

			if (previousCategoryRelation != null
					&& (previousCategoryRelation.getCategoryID() == currentCategoryRelation.getCategoryID())) {

				previousCategoryRelation.setCategoryID(-1);
				currentCategoryRelation.setCategoryID(-1);
			} else {

				pojoJsonDataFiller(Category_relation.Category_id, currentCategoryRelation.getCategoryID(), json);
				pojoKeyValueFiller(Category_relation.Category_id, currentCategoryRelation.getCategoryID());

			}

		}

		if (currentCategoryRelation.getContactIDtoJoin() != -1) {
			if (previousCategoryRelation != null && (previousCategoryRelation
					.getContactIDtoJoin() == currentCategoryRelation.getContactIDtoJoin())) {

				currentCategoryRelation.setContactIDtoJoin(-1);
				previousCategoryRelation.setContactIDtoJoin(-1);

			} else {

				pojoJsonDataFiller(Category_relation.contact_id_to_join, currentCategoryRelation.getContactIDtoJoin(),
						json);
				pojoKeyValueFiller(Category_relation.contact_id_to_join, currentCategoryRelation.getContactIDtoJoin());

			}

		}

		return json;

	}

	private static StringBuilder categoryConverter(Table data, Table previousData) {
		Category currentCategory = (Category) data;

		Category previousCategory = null;

		if (previousData != null) {

			previousCategory = (Category) previousData;

		}

		StringBuilder json = new StringBuilder("{");

		pojoIDSetter(querybuilderconfig.TableSchema.Category.Category_id, currentCategory, previousCategory, json);
		pojoCreatedTimeSetter(querybuilderconfig.TableSchema.Category.created_time, currentCategory, previousCategory,
				json);
		pojoModifiedTimeSetter(querybuilderconfig.TableSchema.Category.modified_time, currentCategory, previousCategory,
				json);

		if (currentCategory.getCategoryName() != null) {

			if (previousCategory != null
					&& (previousCategory.getCategoryName().equals(currentCategory.getCategoryName()))) {

				previousCategory.setCategoryName(null);
				currentCategory.setCategoryName(null);
			} else {

				pojoJsonDataFiller(querybuilderconfig.TableSchema.Category.Category_name,
						currentCategory.getCategoryName(), json);
				pojoKeyValueFiller(querybuilderconfig.TableSchema.Category.Category_name,
						currentCategory.getCategoryName());

			}

		}
		if (currentCategory.getCreatedBy() != -1) {

			pojoJsonDataFiller(querybuilderconfig.TableSchema.Category.created_by, currentCategory.getCreatedBy(),
					json);
			pojoKeyValueFiller(querybuilderconfig.TableSchema.Category.created_by, currentCategory.getCreatedBy());

		}

		return json;

	}

	private static StringBuilder contactDetailsConverter(Table data, Table previousData) {
		ContactDetails currentContact = (ContactDetails) data;

		ContactDetails previousContact = null;

		if (previousData != null) {

			previousContact = (ContactDetails) previousData;

		}
		StringBuilder json = new StringBuilder("{");

		pojoIDSetter(Contact_details.contact_id, currentContact, previousContact, json);
		pojoCreatedTimeSetter(Contact_details.created_time, currentContact, previousContact, json);
		pojoModifiedTimeSetter(Contact_details.modified_time, currentContact, previousContact, json);

		if (currentContact.getUserID() != -1) {

			pojoJsonDataFiller(Contact_details.user_id, currentContact.getUserID(), json);
			pojoKeyValueFiller(Contact_details.user_id, currentContact.getUserID());

		}
		if (currentContact.getFirstName() != null) {

			if (previousContact != null && (previousContact.getFirstName().equals(currentContact.getFirstName()))) {

				previousContact.setFirstName(null);
				currentContact.setFirstName(null);
			} else {
				pojoJsonDataFiller(Contact_details.First_name, currentContact.getFirstName(), json);
				pojoKeyValueFiller(Contact_details.First_name, currentContact.getFirstName());

			}

		}
		if (currentContact.getMiddleName() != null) {

			if (previousContact != null && (previousContact.getMiddleName().equals(currentContact.getMiddleName()))) {

				previousContact.setMiddleName(null);
				currentContact.setMiddleName(null);
			} else {
				pojoJsonDataFiller(Contact_details.Middle_name, currentContact.getMiddleName(), json);
				pojoKeyValueFiller(Contact_details.Middle_name, currentContact.getMiddleName());

			}

		}
		if (currentContact.getLastName() != null) {

			if (previousContact != null && (previousContact.getLastName().equals(currentContact.getLastName()))) {

				previousContact.setLastName(null);
				currentContact.setLastName(null);
			} else {
				pojoJsonDataFiller(Contact_details.Last_name, currentContact.getLastName(), json);
				pojoKeyValueFiller(Contact_details.Last_name, currentContact.getLastName());

			}

		}
		if (currentContact.getGender() != null) {

			if (previousContact != null && (previousContact.getGender().equals(currentContact.getGender()))) {

				previousContact.setGender(null);
				currentContact.setGender(null);
			} else {
				pojoJsonDataFiller(Contact_details.gender, currentContact.getGender(), json);
				pojoKeyValueFiller(Contact_details.gender, currentContact.getGender());

			}

		}
		if (currentContact.getAddress() != null) {

			if (previousContact != null && (previousContact.getAddress().equals(currentContact.getAddress()))) {

				previousContact.setAddress(null);
				currentContact.setAddress(null);
			} else {
				pojoJsonDataFiller(Contact_details.Address, currentContact.getAddress(), json);
				pojoKeyValueFiller(Contact_details.Address, currentContact.getAddress());

			}

		}

		return json;

	}

	private static StringBuilder contactMailConverter(Table data, Table previousData) {
		ContactMail currentContactMail = (ContactMail) data;

		ContactMail previousContactMail = null;

		if (previousData != null) {

			previousContactMail = (ContactMail) previousData;
		}
		StringBuilder json = new StringBuilder("{");

		pojoIDSetter(Contact_mail.ID, currentContactMail, previousContactMail, json);
		pojoCreatedTimeSetter(Contact_mail.created_time, currentContactMail, previousContactMail, json);
		pojoModifiedTimeSetter(Contact_mail.modified_time, currentContactMail, previousContactMail, json);

		if (currentContactMail.getContactID() != -1) {

			pojoJsonDataFiller(Contact_mail.contact_id, currentContactMail.getContactID(), json);
			pojoKeyValueFiller(Contact_mail.contact_id, currentContactMail.getContactID());

		}
		if (currentContactMail.getContactMailID() != null) {

			if (previousContactMail != null
					&& (previousContactMail.getContactMailID().equals(currentContactMail.getContactMailID()))) {
				previousContactMail.setContactMailID(null);
				currentContactMail.setContactMailID(null);
			} else {

				pojoJsonDataFiller(Contact_mail.Contact_email_id, currentContactMail.getContactMailID(), json);
				pojoKeyValueFiller(Contact_mail.Contact_email_id, currentContactMail.getContactMailID());

			}

		}

		return json;

	}

	private static StringBuilder contactPhoneConverter(Table data, Table previousData) {
		ContactPhone currentContactPhone = (ContactPhone) data;

		ContactPhone previousContactPhone = null;

		if (previousData != null) {

			previousContactPhone = (ContactPhone) previousData;
		}
		StringBuilder json = new StringBuilder("{");
		pojoIDSetter(Contact_phone.ID, currentContactPhone, previousContactPhone, json);
		pojoCreatedTimeSetter(Contact_phone.created_time, currentContactPhone, previousContactPhone, json);
		pojoModifiedTimeSetter(Contact_phone.modified_time, currentContactPhone, previousContactPhone, json);

		if (currentContactPhone.getContactID() != -1) {

			pojoJsonDataFiller(Contact_phone.contact_id, currentContactPhone.getContactID(), json);
			pojoKeyValueFiller(Contact_phone.contact_id, currentContactPhone.getContactID());

		}
		if (currentContactPhone.getContactPhone() != null) {

			if (previousContactPhone != null
					&& (previousContactPhone.getContactPhone().equals(currentContactPhone.getContactPhone()))) {

				previousContactPhone.setContactPhone(null);
				currentContactPhone.setContactPhone(null);

			} else {

				pojoJsonDataFiller(Contact_phone.Contact_phone_no, currentContactPhone.getContactPhone(), json);
				pojoKeyValueFiller(Contact_phone.Contact_phone_no, currentContactPhone.getContactPhone());

			}

		}

		return json;

	}

	private static StringBuilder loginCredentialsConverter(Table data, Table previousData) {
		LoginCredentials currentLogin = (LoginCredentials) data;

		LoginCredentials previousLogin = null;

		id = currentLogin.getID();
		if (previousData != null) {

			previousLogin = (LoginCredentials) previousData;
		}
		StringBuilder json = new StringBuilder("{");

		pojoIDSetter(Login_credentials.ID, currentLogin, previousLogin, json);
		pojoModifiedTimeSetter(Login_credentials.modified_time, currentLogin, previousLogin, json);
		pojoCreatedTimeSetter(Login_credentials.created_time, currentLogin, previousLogin, json);

		if (currentLogin.getUserId() != -1) {

			pojoJsonDataFiller(Login_credentials.log_id, currentLogin.getUserId(), json);
			pojoKeyValueFiller(Login_credentials.log_id, currentLogin.getUserId());

		}
		if (currentLogin.getUserName() != null) {

			if (previousLogin != null && (previousLogin.getUserName().equals(currentLogin.getUserName()))) {

				previousLogin.setUserName(null);
				currentLogin.setUserName(null);
			} else {

				pojoJsonDataFiller(Login_credentials.username, currentLogin.getUserName(), json);
				pojoKeyValueFiller(Login_credentials.username, currentLogin.getUserName());
			}

		}

		return json;

	}

	private static StringBuilder sessionConverter(Table data, Table previousData) {
		Session currentSession = (Session) data;
		Session previousSession = null;
		id = currentSession.getID();
		if (previousData != null) {

			previousSession = (Session) previousData;
		}

		StringBuilder json = new StringBuilder("{");

		pojoIDSetter(querybuilderconfig.TableSchema.Session.ID, currentSession, previousSession, json);
		pojoModifiedTimeSetter(querybuilderconfig.TableSchema.Session.modified_time, currentSession, previousSession,
				json);
		pojoCreatedTimeSetter(querybuilderconfig.TableSchema.Session.created_time, currentSession, previousSession,
				json);
		if (currentSession.getSessionId() != null) {

			if (previousSession != null && (previousSession.getSessionId().equals(currentSession.getSessionId()))) {

				previousSession.setSessionID(null);
				currentSession.setSessionID(null);
			} else {
				pojoJsonDataFiller(querybuilderconfig.TableSchema.Session.Session_id, currentSession.getSessionId(),
						json);
				pojoKeyValueFiller(querybuilderconfig.TableSchema.Session.Session_id, currentSession.getSessionId());
			}
		}

		if (currentSession.getLastAccessed() != -1) {

			if (previousSession != null && (previousSession.getLastAccessed() == currentSession.getLastAccessed())) {

				previousSession.setLastAccessed(-1);
				currentSession.setLastAccessed(-1);
			} else {

				pojoJsonDataFiller(querybuilderconfig.TableSchema.Session.last_accessed,
						currentSession.getLastAccessed(), json);
				pojoKeyValueFiller(querybuilderconfig.TableSchema.Session.last_accessed,
						currentSession.getLastAccessed());
			}

		}

		if (currentSession.getUserId() != -1) {
			pojoJsonDataFiller(querybuilderconfig.TableSchema.Session.user_id, currentSession.getUserId(), json);
			pojoKeyValueFiller(querybuilderconfig.TableSchema.Session.user_id, currentSession.getUserId());

		}
		return json;

	}

	private static StringBuilder emailConverter(Table data, Table previousData) {
		EmailUser currentEmail = (EmailUser) data;

		EmailUser previousEmail = null;
		id = currentEmail.getID();
		if (previousData != null) {

			previousEmail = (EmailUser) previousData;
		}
		StringBuilder json = new StringBuilder("{");
		pojoIDSetter(Email_user.ID, currentEmail, previousEmail, json);
		pojoModifiedTimeSetter(Email_user.modified_time, currentEmail, previousEmail, json);
		pojoCreatedTimeSetter(Email_user.created_time, currentEmail, previousEmail, json);

		if (currentEmail.getEmailId() != -1) {

			pojoJsonDataFiller(querybuilderconfig.TableSchema.Email_user.em_id, currentEmail.getEmailId(), json);
			pojoKeyValueFiller(querybuilderconfig.TableSchema.Email_user.em_id, currentEmail.getEmailId());

		}

		if (currentEmail.getEmail() != null) {

			if (previousEmail != null && (previousEmail.getEmail().equals(currentEmail.getEmail()))) {

				previousEmail.setEmail(null);
				currentEmail.setEmail(null);
			} else {

				pojoJsonDataFiller(querybuilderconfig.TableSchema.Email_user.email, currentEmail.getEmail(), json);
				pojoKeyValueFiller(querybuilderconfig.TableSchema.Email_user.email, currentEmail.getEmail());

			}

		}

		if (currentEmail.getIsPrimary() != null) {

			if (previousEmail != null && (previousEmail.getIsPrimary() == currentEmail.getIsPrimary())) {

				previousEmail.setIsPrimary(null);
				currentEmail.setIsPrimary(null);
			} else {

				pojoJsonDataFiller(querybuilderconfig.TableSchema.Email_user.is_primary, currentEmail.getIsPrimary(),
						json);
				pojoKeyValueFiller(querybuilderconfig.TableSchema.Email_user.is_primary, currentEmail.getIsPrimary());

			}

		}

		return json;

	}

	public static StringBuilder auditLogConverter(Table data, Table previousData) {
		AuditLog currentAudit = (AuditLog) data;
		AuditLog previousAuditLog = null;

		if (previousData != null) {

			previousAuditLog = (AuditLog) previousData;
		}

		StringBuilder json = new StringBuilder("{");

		pojoIDSetter(Audit_log.ID, currentAudit, previousData, json);
		pojoCreatedTimeSetter(Audit_log.created_at, currentAudit, previousAuditLog, json);

		if (currentAudit.getPreviousState() != null) {

			if (previousAuditLog != null
					&& (previousAuditLog.getPreviousState().equals(currentAudit.getPreviousState()))) {

				currentAudit.setPreviousState(null);
				previousAuditLog.setPreviousState(null);
			} else {

				pojoJsonDataFiller(Audit_log.previous_state, currentAudit.getPreviousState(), json);
				pojoKeyValueFiller(Audit_log.previous_state, currentAudit.getPreviousState());
			}

		}

		if (currentAudit.getChangedState() != null) {

			if (previousAuditLog != null
					&& (previousAuditLog.getChangedState().equals(currentAudit.getChangedState()))) {

				currentAudit.setChangedState(null);
				previousAuditLog.setChangedState(null);
			} else {

				pojoJsonDataFiller(Audit_log.changed_state, currentAudit.getChangedState(), json);
				pojoKeyValueFiller(Audit_log.changed_state, currentAudit.getChangedState());
			}

		}

		if (currentAudit.getAuditTableName() != null) {

			if (previousAuditLog != null
					&& (previousAuditLog.getAuditTableName().equals(currentAudit.getAuditTableName()))) {

				currentAudit.setAuditTableName(null);
				previousAuditLog.setAuditTableName(null);
			} else {

				pojoJsonDataFiller(Audit_log.table_name, currentAudit.getAuditTableName(), json);
				pojoKeyValueFiller(Audit_log.table_name, currentAudit.getAuditTableName());
			}

		}

		if (currentAudit.getOperation() != null) {

			if (previousAuditLog != null && (previousAuditLog.getOperation() != null
					&& previousAuditLog.getOperation() == currentAudit.getOperation())) {

				currentAudit.setOperation(null);
				previousAuditLog.setOperation(null);
			} else {

				pojoJsonDataFiller(Audit_log.operation, currentAudit.getOperation(), json);
				pojoKeyValueFiller(Audit_log.operation, currentAudit.getOperation());
			}

		}

		if (currentAudit.getCreatedBy() != -1) {

			if (previousAuditLog != null && (previousAuditLog.getCreatedBy() == currentAudit.getCreatedBy())) {

				currentAudit.setCreatedBy(-1);
				previousAuditLog.setCreatedBy(-1);
			} else {

				pojoJsonDataFiller(Audit_log.created_by, currentAudit.getCreatedBy(), json);
				pojoKeyValueFiller(Audit_log.created_by, currentAudit.getCreatedBy());
			}

		}

		return json;

	}

	private static void pojoJsonDataFiller(querybuilderconfig.Table column, Object columnValue, StringBuilder json) {

		if (columnValue instanceof String) {

			json.append("\"" + column + "\"" + ":" + "\"" + columnValue + "\"" + ",");

		} else {

			json.append("\"" + column + "\"" + ":" + columnValue + ",");

		}

	}

	private static void pojoKeyValueFiller(querybuilderconfig.Table column, Object columnValue) {

		pojoKey.add(column.getTableName() + "." + column.toString());
		pojoValue.add(columnValue);

	}

	private static void pojoCreatedTimeSetter(querybuilderconfig.Table column, Table newTable, Table oldData,
			StringBuilder json) {

		if (newTable.getCreatedAt() != -1) {

			if (oldData != null && (oldData.getCreatedAt() == newTable.getCreatedAt())) {

				newTable.setCreatedAt(-1);
				oldData.setCreatedAt(-1);
			} else {

				pojoJsonDataFiller(column, newTable.getCreatedAt(), json);
				pojoKeyValueFiller(column, newTable.getCreatedAt());
			}

		}

	}

	private static void pojoModifiedTimeSetter(querybuilderconfig.Table column, Table newTable, Table oldData,
			StringBuilder json) {

		if (newTable.getModifiedAt() != -1) {

			if (oldData != null && (oldData.getModifiedAt() == newTable.getModifiedAt())) {

				newTable.setModifiedAt(-1);
				oldData.setModifiedAt(-1);
			} else {

//				pojoJsonDataFiller(column, newTable.getModifiedAt(), json);
				pojoKeyValueFiller(column, newTable.getModifiedAt());
			}

		}

	}

	private static void pojoIDSetter(querybuilderconfig.Table column, Table newTable, Table oldData,
			StringBuilder json) {
		id = newTable.getID();

		if (newTable.getID() != -1) {

			pojoJsonDataFiller(column, newTable.getID(), json);

		}

	}

}
