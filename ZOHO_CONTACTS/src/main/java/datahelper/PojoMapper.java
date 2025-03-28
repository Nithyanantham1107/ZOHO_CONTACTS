package datahelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Oauth;
import dbpojo.Session;
import dbpojo.Table;
import dbpojo.Userdata;
import querybuilderconfig.TableSchema;
import querybuilderconfig.TableSchema.UserDataSchema;

public class PojoMapper {

	ResultSet result;

	ArrayList<String> columnNames;
	HashMap<Integer, Object> uniqueList = new HashMap<Integer, Object>();

	ArrayList<Table> data = new ArrayList<>();

	public ArrayList<Table> PojoResultSetter(String tablename, ArrayList<String> columnNames, ResultSet result)
			throws SQLException {

		this.result = result;
		this.columnNames = columnNames;
		Userdata userData = null;
		Category category = null;
		ContactDetails contactDetails = null;

		try {
			while (this.result.next()) {

				System.out.println("here tablename" + tablename);

				if (tablename.equals(TableSchema.UserDataSchema.USERID.getTableName())) {
					userData = userDataSetter();
					if (userData != null) {

						this.data.add(userData);
					}

				} else if (tablename.equals(TableSchema.CategorySchema.CATEGORYID.getTableName())) {
					category = userCategorySetter();
					if (category != null) {
						this.data.add(category);
					}

				} else if (tablename.equals(TableSchema.CategoryRelationSchema.ID.getTableName())) {
					this.data.add(userCategoryRelationSetter());
				} else if (tablename.equals(TableSchema.ContactDetailsSchema.CONTACTID.getTableName())) {
					contactDetails = userContactSetter();
					if (contactDetails != null) {
						this.data.add(contactDetails);
					}

				} else if (tablename.equals(TableSchema.ContactMailSchema.ID.getTableName())) {
					this.data.add(userMailSetter());
				} else if (tablename.equals(TableSchema.ContactPhoneSchema.ID.getTableName())) {
					this.data.add(userPhoneSetter());
				} else if (tablename.equals(TableSchema.LoginCredentialsSchema.ID.getTableName())) {
					this.data.add(userLoginSetter());
				} else if (tablename.equals(TableSchema.SessionSchema.ID.getTableName())) {
					this.data.add(userSessionSetter());
				} else if (tablename.equals(TableSchema.EmailUserSchema.ID.getTableName())) {
					this.data.add(userEmailSetter());
				} else if (tablename.equals(TableSchema.OauthSchema.ID.getTableName())) {
					this.data.add(oauthSetter());
				}

			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			this.uniqueList.clear();

		}

		return this.data;
	}

	private Oauth oauthSetter() {

		Oauth oauth = null;
		String tablename = TableSchema.OauthSchema.ID.getTableName();

		oauth = new Oauth(getInt(tablename + "." + TableSchema.OauthSchema.ID.getColumnName()),
				getInt(tablename + "." + TableSchema.OauthSchema.USERID.getColumnName()),
				getString(tablename + "." + TableSchema.OauthSchema.OAUTHPROVIDER.getColumnName()),
				getString(tablename + "." + TableSchema.OauthSchema.REFRESHTOKEN.getColumnName()),
				getString(tablename + "." + TableSchema.OauthSchema.ACCESSTOKEN.getColumnName()),
				getString(tablename + "." + TableSchema.OauthSchema.EMAIL.getColumnName()),
				getBoolean(tablename + "." + TableSchema.OauthSchema.SYNCSTATE.getColumnName()),
				getLong(tablename + "." + TableSchema.OauthSchema.EXPIRYTIME.getColumnName()),
				getLong(tablename + "." + TableSchema.OauthSchema.CREATEDTIME.getColumnName()),
				getLong(tablename + "." + TableSchema.OauthSchema.MODIFIEDTIME.getColumnName()));

		return oauth;

	}

	private Userdata userDataSetter() {

		Userdata userData = null;
		String tablename = TableSchema.UserDataSchema.USERID.getTableName();

		if (this.uniqueList.get(getInt(tablename + "." + TableSchema.UserDataSchema.USERID.getColumnName())) != null) {

			if (this.uniqueList.get(getInt(tablename + "." + TableSchema.UserDataSchema.USERID.getColumnName())) instanceof Userdata) {
				userData = (Userdata) this.uniqueList.get(getInt(tablename + "." + TableSchema.UserDataSchema.USERID.getColumnName()));

			}
			EmailUser email = userEmailSetter();
			LoginCredentials login = userLoginSetter();
			Oauth oauth = oauthSetter();
			if (userData.getOauth(oauth.getID()) == null) {
				userData.setOauth(oauth);

			}

			if (userData.getemail(email.getID()) == null) {
				userData.setEmail(email);

			}
			if (userData.getLoginCredentials() == null) {
				userData.setLoginCredentials(login);
			}
			return null;

		} else {

			userData = new Userdata(getInt(tablename + "." + TableSchema.UserDataSchema.USERID.getColumnName()),
					getString(tablename + "." + TableSchema.UserDataSchema.NAME.getColumnName()),
					getString(tablename + "." + TableSchema.UserDataSchema.PASSWORD.getColumnName()),
					getString(tablename + "." + TableSchema.UserDataSchema.PHONENO.getColumnName()),
					getString(tablename + "." + TableSchema.UserDataSchema.ADDRESS.getColumnName()),
					getString(tablename + "." + TableSchema.UserDataSchema.TIMEZONE.getColumnName()),
					getLong(tablename + "." + TableSchema.UserDataSchema.CREATEDTIME.getColumnName()),
					getLong(tablename + "." + TableSchema.UserDataSchema.MODIFIEDTIME.getColumnName()));

			this.uniqueList.put(getInt(tablename + "." + TableSchema.UserDataSchema.USERID.getColumnName()), userData);

			userData.setEmail(userEmailSetter());
			userData.setOauth(oauthSetter());
			userData.setLoginCredentials(userLoginSetter());
			return userData;

		}

	}

	private Category userCategorySetter() {

		Category category = null;

		String tablename = TableSchema.CategorySchema.CATEGORYID.getTableName();

		if (this.uniqueList.get(getInt(tablename + "." + TableSchema.CategorySchema.CATEGORYID.getColumnName())) != null) {

			if (this.uniqueList.get(getInt(tablename + "." + TableSchema.CategorySchema.CATEGORYID.getColumnName())) instanceof Category) {
				category = (Category) this.uniqueList.get(getInt(tablename + "." + TableSchema.CategorySchema.CATEGORYID.getColumnName()));
			}

			CategoryRelation categoryRelation = userCategoryRelationSetter();

			if (!category.isContactExist(categoryRelation.getContactIDtoJoin())) {

				category.setCategoryRelation(categoryRelation);

			}

			return null;

		} else {

			category = new Category(getInt(tablename + "." + TableSchema.CategorySchema.CATEGORYID.getColumnName()),
					getString(tablename + "." + TableSchema.CategorySchema.CATEGORYNAME.getColumnName()),
					getInt(tablename + "." + TableSchema.CategorySchema.CREATEDBY.getColumnName()),
					getLong(tablename + "." + TableSchema.CategorySchema.CREATEDTIME.getColumnName()),
					getLong(tablename + "." + TableSchema.CategorySchema.MODIFIEDTIME.getColumnName()));

			this.uniqueList.put(getInt(tablename + "." + TableSchema.CategorySchema.CATEGORYID.getColumnName()), category);

			category.setCategoryRelation(userCategoryRelationSetter());

			return category;
		}

	}

	private CategoryRelation userCategoryRelationSetter() {

		CategoryRelation categoryRelation = null;
		String tablename = TableSchema.CategoryRelationSchema.ID.getTableName();

		categoryRelation = new CategoryRelation(getInt(tablename + "." + TableSchema.CategoryRelationSchema.ID.getColumnName()),
				getInt(tablename + "." + TableSchema.CategoryRelationSchema.CONTACTIDTOJOIN.getColumnName()),
				getInt(tablename + "." + TableSchema.CategoryRelationSchema.CATEGORYID.getColumnName()),
				getLong(tablename + "." + TableSchema.CategoryRelationSchema.CREATEDTIME.getColumnName()),
				getLong(tablename + "." + TableSchema.CategoryRelationSchema.MODIFIEDTIME.getColumnName()));

		return categoryRelation;

	}

	private ContactDetails userContactSetter() {

		ContactDetails contactDetails = null;

		String tablename = TableSchema.ContactDetailsSchema.CONTACTID.getTableName();

		if (this.uniqueList.get(getInt(tablename + "." + TableSchema.ContactDetailsSchema.CONTACTID.getColumnName())) != null) {

			if (this.uniqueList
					.get(getInt(tablename + "." + TableSchema.ContactDetailsSchema.CONTACTID.getColumnName())) instanceof ContactDetails) {
				contactDetails = (ContactDetails) this.uniqueList
						.get(getInt(tablename + "." + TableSchema.CategorySchema.CATEGORYID.getColumnName()));
			}

			ContactMail contactMail = userMailSetter();
			ContactPhone contactPhone = userPhoneSetter();

			if (contactDetails.getContactMail() == null) {
				contactDetails.setContactMail(contactMail);
			}

			if (contactDetails.getContactphone() == null) {

				contactDetails.setContactPhone(contactPhone);
			}

			return null;

		} else {

			contactDetails = new ContactDetails(getInt(tablename + "." + TableSchema.ContactDetailsSchema.USERID.getColumnName()),
					getInt(tablename + "." + TableSchema.ContactDetailsSchema.CONTACTID.getColumnName()),
					getInt(tablename + "." + TableSchema.ContactDetailsSchema.OAUTHID.getColumnName()),
					getString(tablename + "." + TableSchema.ContactDetailsSchema.OAUTHCONTACTID.getColumnName()),
					getString(tablename + "." + TableSchema.ContactDetailsSchema.FIRSTNAME.getColumnName()),
					getString(tablename + "." + TableSchema.ContactDetailsSchema.MIDDLENAME.getColumnName()),
					getString(tablename + "." + TableSchema.ContactDetailsSchema.LASTNAME.getColumnName()),
					getString(tablename + "." + TableSchema.ContactDetailsSchema.GENDER.getColumnName()),
					getString(tablename + "." + TableSchema.ContactDetailsSchema.ADDRESS.getColumnName()),
					getLong(tablename + "." + TableSchema.ContactDetailsSchema.CREATEDTIME.getColumnName()),
					getLong(tablename + "." + TableSchema.ContactDetailsSchema.MODIFIEDTIME.getColumnName()));
			contactDetails.setContactMail(userMailSetter());
			contactDetails.setContactPhone(userPhoneSetter());

			return contactDetails;

		}

	}

	private ContactMail userMailSetter() {

		ContactMail contactMail = null;
		String tablename = TableSchema.ContactMailSchema.ID.getTableName();

		contactMail = new ContactMail(

				getInt(tablename + "." + TableSchema.ContactMailSchema.ID.getColumnName()),
				getInt(tablename + "." + TableSchema.ContactMailSchema.CONTACTID.getColumnName()),
				getString(tablename + "." + TableSchema.ContactMailSchema.CONTACTMAILID.getColumnName()),
				getLong(tablename + "." + TableSchema.ContactMailSchema.CREATEDTIME.getColumnName()),
				getLong(tablename + "." + TableSchema.ContactMailSchema.MODIFIEDTIME.getColumnName()));

		return contactMail;

	}

	private ContactPhone userPhoneSetter() {

		ContactPhone contactPhone = null;
		String tablename = TableSchema.ContactPhoneSchema.ID.getTableName();

		contactPhone = new ContactPhone(getInt(tablename + "." + TableSchema.ContactPhoneSchema.ID.getColumnName()),
				getInt(tablename + "." + TableSchema.ContactPhoneSchema.CONTACTID.getColumnName()),
				getString(tablename + "." + TableSchema.ContactPhoneSchema.CONTACTPHONENO.getColumnName()),
				getLong(tablename + "." + TableSchema.ContactPhoneSchema.CREATEDTIME.getColumnName()),
				getLong(tablename + "." + TableSchema.ContactPhoneSchema.MODIFIEDTIME.getColumnName()));

		return contactPhone;

	}

	private EmailUser userEmailSetter() {

		EmailUser emailUser = null;
		String tablename = TableSchema.EmailUserSchema.ID.getTableName();

		emailUser = new EmailUser(

				getInt(tablename + "." + TableSchema.EmailUserSchema.ID.getColumnName()),
				getInt(tablename + "." + TableSchema.EmailUserSchema.EMAILID.getColumnName()),
				getString(tablename + "." + TableSchema.EmailUserSchema.EMAIL.getColumnName()),
				getBoolean(tablename + "." + TableSchema.EmailUserSchema.ISPRIMARY.getColumnName()),
				getLong(tablename + "." + TableSchema.EmailUserSchema.CREATEDTIME.getColumnName()),
				getLong(tablename + "." + TableSchema.EmailUserSchema.MODIFIEDTIME.getColumnName()));

		return emailUser;

	}

	private LoginCredentials userLoginSetter() {

		LoginCredentials login = null;
		String tablename = TableSchema.LoginCredentialsSchema.ID.getTableName();

		login = new LoginCredentials(getInt(tablename + "." + TableSchema.LoginCredentialsSchema.ID.getColumnName()),
				getInt(tablename + "." + TableSchema.LoginCredentialsSchema.LOGID.getColumnName()),
				getString(tablename + "." + TableSchema.LoginCredentialsSchema.USERNAME.getColumnName()),
				getLong(tablename + "." + TableSchema.LoginCredentialsSchema.CREATEDTIME.getColumnName()),
				getLong(tablename + "." + TableSchema.LoginCredentialsSchema.MODIFIEDTIME.getColumnName()));

		return login;

	}

	private Session userSessionSetter() {

		Session session = null;
		String tablename = TableSchema.SessionSchema.ID.getTableName();

		session = new Session(

				getInt(tablename + "." + TableSchema.SessionSchema.ID.getColumnName()),
				getString(tablename + "." + TableSchema.SessionSchema.SESSIONID.getColumnName()),
				getLong(tablename + "." + TableSchema.SessionSchema.LASTACCESSED.getColumnName()),
				getInt(tablename + "." + TableSchema.SessionSchema.USERID.getColumnName()),
				getLong(tablename + "." + TableSchema.SessionSchema.CREATEDTIME.getColumnName()),
				getLong(tablename + "." + TableSchema.SessionSchema.MODIFIEDTIME.getColumnName()));

		return session;

	}

	private int getInt(String column) {
		try {
			if (this.columnNames.contains(column)) {
				if (result.getInt(column) == 0) {
					return -1;
				}
				return result.getInt(column);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return -1;
	}

	private long getLong(String column) {
		try {
			if (this.columnNames.contains(column)) {

				if (result.getLong(column) == 0) {
					return -1;
				}
				return result.getLong(column);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return -1;
	}

	private String getString(String column) {

		try {

			if (this.columnNames.contains(column)) {

				return result.getString(column);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	private boolean getBoolean(String column) {
		try {
			if (this.columnNames.contains(column)) {

				return result.getBoolean(column);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}
}
