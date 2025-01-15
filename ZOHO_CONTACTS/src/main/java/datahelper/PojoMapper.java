package datahelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import dbpojo.Userdata;
import querybuilderconfig.TableSchema;

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
				
			System.out.println("here tablename"+tablename);

				if (tablename.equals(TableSchema.tables.user_data.getTableName())) {
					userData = userDataSetter();
					if (userData != null) {

						this.data.add(userData);
					}

				} else if (tablename.equals(TableSchema.tables.Category.getTableName())) {
					category = userCategorySetter();
					if (category != null) {
						this.data.add(category);
					}

				} else if (tablename.equals(TableSchema.tables.Category_relation.getTableName())) {
					this.data.add(userCategoryRelationSetter());
				} else if (tablename.equals(TableSchema.tables.Contact_details.getTableName())) {
					contactDetails = userContactSetter();
					if (contactDetails != null) {
						this.data.add(contactDetails);
					}

				} else if (tablename.equals(TableSchema.tables.Contact_mail.getTableName())) {
					this.data.add(userMailSetter());
				} else if (tablename.equals(TableSchema.tables.Contact_phone.getTableName())) {
					this.data.add(userPhoneSetter());
				} else if (tablename.equals(TableSchema.tables.Login_credentials.getTableName())) {
					this.data.add(userLoginSetter());
				} else if (tablename.equals(TableSchema.tables.Session.getTableName())) {
					this.data.add(userSessionSetter());
				} else if (tablename.equals(TableSchema.tables.Email_user.getTableName())) {
					this.data.add(userEmailSetter());
				}

			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

//			this.result.close();

			this.uniqueList.clear();

		}

		return this.data;
	}

	private Userdata userDataSetter() {

		Userdata userData = null;
		String tablename = TableSchema.tables.user_data.getTableName();

		if (this.uniqueList.get(getInt(tablename + "." + TableSchema.user_data.user_id)) != null) {

			if (this.uniqueList.get(getInt(tablename + "." + TableSchema.user_data.user_id)) instanceof Userdata) {
				userData = (Userdata) this.uniqueList.get(getInt(tablename + "." + TableSchema.user_data.user_id));

			}
			EmailUser email = userEmailSetter();
			LoginCredentials login = userLoginSetter();

			if (userData.getemail(email.getID()) == null) {
				userData.setEmail(email);

			}
			if (userData.getLoginCredentials() == null) {
				userData.setLoginCredentials(login);
			}

//			ud.setSession(userSessionSetter());

			return null;

		} else {

			userData = new Userdata(

					getInt(tablename + "." + TableSchema.user_data.user_id),
					getString(tablename + "." + TableSchema.user_data.Name),
					getString(tablename + "." + TableSchema.user_data.password),
					getString(tablename + "." + TableSchema.user_data.phone_no),
					getString(tablename + "." + TableSchema.user_data.address),
					getString(tablename + "." + TableSchema.user_data.timezone),
					getLong(tablename + "." + TableSchema.user_data.created_time),
					getLong(tablename + "." + TableSchema.user_data.modified_time));

			this.uniqueList.put(getInt(tablename + "." + TableSchema.user_data.user_id), userData);

			userData.setEmail(userEmailSetter());
//			ud.setSession(userSessionSetter());
			userData.setLoginCredentials(userLoginSetter());
			return userData;

		}

	}

	private Category userCategorySetter() {

		Category category = null;

		String tablename = TableSchema.tables.Category.getTableName();

		if (this.uniqueList.get(getInt(tablename + "." + TableSchema.Category.Category_id)) != null) {

			if (this.uniqueList.get(getInt(tablename + "." + TableSchema.Category.Category_id)) instanceof Category) {
				category = (Category) this.uniqueList.get(getInt(tablename + "." + TableSchema.Category.Category_id));
			}

			CategoryRelation categoryRelation = userCategoryRelationSetter();

			if (!category.isContactExist(categoryRelation.getContactIDtoJoin())) {

				category.setCategoryRelation(categoryRelation);

			}

			return null;

		} else {

			category = new Category(getInt(tablename + "." + TableSchema.Category.Category_id),
					getString(tablename + "." + TableSchema.Category.Category_name),
					getInt(tablename + "." + TableSchema.Category.created_by),
					getLong(tablename + "." + TableSchema.Category.created_time),
					getLong(tablename + "." + TableSchema.Category.modified_time));

			this.uniqueList.put(getInt(tablename + "." + TableSchema.Category.Category_id), category);

			category.setCategoryRelation(userCategoryRelationSetter());

			return category;
		}

	}

	private CategoryRelation userCategoryRelationSetter() {

		CategoryRelation categoryRelation = null;
		String tablename = TableSchema.tables.Category_relation.getTableName();

		categoryRelation = new CategoryRelation(getInt(tablename + "." + TableSchema.Category_relation.ID),
				getInt(tablename + "." + TableSchema.Category_relation.contact_id_to_join),
				getInt(tablename + "." + TableSchema.Category_relation.Category_id),
				getLong(tablename + "." + TableSchema.Category_relation.created_time),
				getLong(tablename + "." + TableSchema.Category_relation.modified_time));

		return categoryRelation;

	}

	private ContactDetails userContactSetter() {

		ContactDetails contactDetails = null;
//		boolean state = true;

		String tablename = TableSchema.tables.Contact_details.getTableName();

//		if (k.length > 0) {
//			state = false;
//		}

//		System.out.println("hsjdc");
		if (this.uniqueList.get(getInt(tablename + "." + TableSchema.Contact_details.contact_id)) != null) {

			if (this.uniqueList
					.get(getInt(tablename + "." + TableSchema.Contact_details.contact_id)) instanceof ContactDetails) {
				contactDetails = (ContactDetails) this.uniqueList
						.get(getInt(tablename + "." + TableSchema.Category.Category_id));
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

			contactDetails = new ContactDetails(getInt(tablename + "." + TableSchema.Contact_details.user_id),
					getInt(tablename + "." + TableSchema.Contact_details.contact_id),
					getString(tablename + "." + TableSchema.Contact_details.First_name),
					getString(tablename + "." + TableSchema.Contact_details.Middle_name),
					getString(tablename + "." + TableSchema.Contact_details.Last_name),
					getString(tablename + "." + TableSchema.Contact_details.gender),
					getString(tablename + "." + TableSchema.Contact_details.Address),
					getLong(tablename + "." + TableSchema.Contact_details.created_time),
					getLong(tablename + "." + TableSchema.Contact_details.modified_time));

//			if (k.length == 0) {
//
//				this.uniqueList.put(getInt(tablename + "." + TableSchema.Contact_details.contact_id), cd);
//			}

			contactDetails.setContactMail(userMailSetter());
			contactDetails.setContactPhone(userPhoneSetter());

			return contactDetails;

		}

	}

	private ContactMail userMailSetter() {

		ContactMail contactMail = null;
		String tablename = TableSchema.tables.Contact_mail.getTableName();

		contactMail = new ContactMail(

				getInt(tablename + "." + TableSchema.Contact_mail.ID),
				getInt(tablename + "." + TableSchema.Contact_mail.contact_id),
				getString(tablename + "." + TableSchema.Contact_mail.Contact_email_id),
				getLong(tablename + "." + TableSchema.Contact_mail.created_time),
				getLong(tablename + "." + TableSchema.Contact_mail.modified_time));

		return contactMail;

	}

	private ContactPhone userPhoneSetter() {

		ContactPhone contactPhone = null;
		String tablename = TableSchema.tables.Contact_phone.getTableName();

		contactPhone = new ContactPhone(getInt(tablename + "." + TableSchema.Contact_phone.ID),
				getInt(tablename + "." + TableSchema.Contact_phone.contact_id),
				getString(tablename + "." + TableSchema.Contact_phone.Contact_phone_no),
				getLong(tablename + "." + TableSchema.Contact_phone.created_time),
				getLong(tablename + "." + TableSchema.Contact_phone.modified_time));

		return contactPhone;

	}

	private EmailUser userEmailSetter() {

		EmailUser emailUser = null;
		String tablename = TableSchema.tables.Email_user.getTableName();
		System.out.println("here execute query column name"+tablename +"ID"+getInt(tablename + "." + TableSchema.Email_user.ID));
		System.out.println("here execute query column name"+tablename +"ID"+getString(tablename + "." + TableSchema.Email_user.email));
		
		emailUser = new EmailUser(

				getInt(tablename + "." + TableSchema.Email_user.ID),
				getInt(tablename + "." + TableSchema.Email_user.em_id),
				getString(tablename + "." + TableSchema.Email_user.email),
				getBoolean(tablename + "." + TableSchema.Email_user.is_primary),
				getLong(tablename + "." + TableSchema.Email_user.created_time),
				getLong(tablename + "." + TableSchema.Email_user.modified_time));

		
		
		return emailUser;

	}

	private LoginCredentials userLoginSetter() {

		LoginCredentials login = null;
		String tablename = TableSchema.tables.Login_credentials.getTableName();

		login = new LoginCredentials(getInt(tablename + "." + TableSchema.Login_credentials.ID),
				getInt(tablename + "." + TableSchema.Login_credentials.log_id),
				getString(tablename + "." + TableSchema.Login_credentials.username),
				getLong(tablename + "." + TableSchema.Login_credentials.created_time),
				getLong(tablename + "." + TableSchema.Login_credentials.modified_time));

		return login;

	}

	private Session userSessionSetter() {

		Session session = null;
		String tablename = TableSchema.tables.Session.getTableName();

		session = new Session(

				getInt(tablename + "." + TableSchema.Session.ID),
				getString(tablename + "." + TableSchema.Session.Session_id),
				getLong(tablename + "." + TableSchema.Session.last_accessed),
				getInt(tablename + "." + TableSchema.Session.user_id),
				getLong(tablename + "." + TableSchema.Session.created_time),
				getLong(tablename + "." + TableSchema.Session.modified_time));

		return session;

	}

	private int getInt(String column) {
		try {
			if (this.columnNames.contains(column)) {

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
