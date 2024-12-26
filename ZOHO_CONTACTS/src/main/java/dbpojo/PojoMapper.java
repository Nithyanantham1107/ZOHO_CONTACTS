package dbpojo;

import querybuilder.TableSchema;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PojoMapper {

	ResultSet result;

	ArrayList<String> columnNames;
	HashMap<Integer, Object> uniqueList = new HashMap<Integer, Object>();

	ArrayList<Object> data = new ArrayList<Object>();

	public ArrayList<Object> PojoResultSetter(String tablename, ArrayList<String> columnNames, ResultSet result) throws SQLException {

		this.result = result;
		this.columnNames = columnNames;
		Userdata ud = null;
		Category cat = null;
		ContactDetails cd = null;

		try{
			while (this.result.next()) {

				if (tablename.equals(TableSchema.tables.user_data.getTableName())) {
					ud = userDataSetter();
					if (ud != null) {

						this.data.add(ud);
					}

				} else if (tablename.equals(TableSchema.tables.Category.getTableName())) {
					cat = userCategorySetter();
					if (cat != null) {
						this.data.add(cat);
					}

				} else if (tablename.equals(TableSchema.tables.Category_relation.getTableName())) {
					this.data.add(userCategoryRelationSetter());
				} else if (tablename.equals(TableSchema.tables.Contact_details.getTableName())) {
					cd = userContactSetter();
					if (cd != null) {
						this.data.add(cd);
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
		}finally {
			
//			this.result.close();
			
			this.uniqueList.clear();
			
		}

			

		return this.data;
	}

	private Userdata userDataSetter() {

		Userdata ud = null;
		String tablename = TableSchema.tables.user_data.getTableName();

		if (this.uniqueList.get(getInt(tablename + "." + TableSchema.user_data.user_id)) != null) {

			if (this.uniqueList.get(getInt(tablename + "." + TableSchema.user_data.user_id)) instanceof Userdata) {
				ud = (Userdata) this.uniqueList.get(getInt(tablename + "." + TableSchema.user_data.user_id));

			}

			ud.setEmail(userEmailSetter());
//			ud.setSession(userSessionSetter());
			ud.setLoginCredentials(userLoginSetter());

			return null;

		} else {

			ud = new Userdata(getInt(tablename + "." + TableSchema.user_data.user_id),
					getString(tablename + "." + TableSchema.user_data.Name),
					getString(tablename + "." + TableSchema.user_data.password),
					getString(tablename + "." + TableSchema.user_data.phone_no),
					getString(tablename + "." + TableSchema.user_data.address),
					getString(tablename + "." + TableSchema.user_data.timezone));

			this.uniqueList.put(getInt(tablename + "." + TableSchema.user_data.user_id), ud);

			ud.setEmail(userEmailSetter());
//			ud.setSession(userSessionSetter());
			ud.setLoginCredentials(userLoginSetter());
			return ud;

		}

	}

	private Category userCategorySetter() {

		Category cat = null;

		String tablename = TableSchema.tables.Category.getTableName();

		if (this.uniqueList.get(getInt(tablename + "." + TableSchema.Category.Category_id)) != null) {

			if (this.uniqueList.get(getInt(tablename + "." + TableSchema.Category.Category_id)) instanceof Category) {
				cat = (Category) this.uniqueList.get(getInt(tablename + "." + TableSchema.Category.Category_id));
			}

			cat.setCategoryRelation(userCategoryRelationSetter());
			return null;

		} else {

			cat = new Category(getInt(tablename + "." + TableSchema.Category.Category_id),
					getString(tablename + "." + TableSchema.Category.Category_name),
					getInt(tablename + "." + TableSchema.Category.created_by));

			this.uniqueList.put(getInt(tablename + "." + TableSchema.Category.Category_id), cat);

			cat.setCategoryRelation(userCategoryRelationSetter());

			return cat;
		}

	}

	private CategoryRelation userCategoryRelationSetter() {

		CategoryRelation catrel = null;
		String tablename = TableSchema.tables.Category_relation.getTableName();

		catrel = new CategoryRelation(getInt(tablename + "." + TableSchema.Category_relation.contact_id_to_join),
				getInt(tablename + "." + TableSchema.Category_relation.Category_id));

		return catrel;

	}

	private ContactDetails userContactSetter(int... k) {

		ContactDetails cd = null;
		boolean state = true;

		String tablename = TableSchema.tables.Contact_details.getTableName();

		if (k.length > 0) {
			state = false;
		}

//		System.out.println("hsjdc");
		if (this.uniqueList.get(getInt(tablename + "." + TableSchema.Contact_details.contact_id)) != null && state) {

			if (this.uniqueList
					.get(getInt(tablename + "." + TableSchema.Contact_details.contact_id)) instanceof ContactDetails) {
				cd = (ContactDetails) this.uniqueList.get(getInt(tablename + "." + TableSchema.Category.Category_id));
			}

			cd.setContactMail(userMailSetter());
			cd.setContactPhone(userPhoneSetter());

			return null;

		} else {

			cd = new ContactDetails(getInt(tablename + "." + TableSchema.Contact_details.user_id),
					getInt(tablename + "." + TableSchema.Contact_details.contact_id),
					getString(tablename + "." + TableSchema.Contact_details.First_name),
					getString(tablename + "." + TableSchema.Contact_details.Middle_name),
					getString(tablename + "." + TableSchema.Contact_details.Last_name),
					getString(tablename + "." + TableSchema.Contact_details.gender),
					getString(tablename + "." + TableSchema.Contact_details.Address),
					getLong(tablename + "." + TableSchema.Contact_details.created_At));


			if (k.length == 0) {

				this.uniqueList.put(getInt(tablename + "." + TableSchema.Contact_details.contact_id), cd);
			}

			cd.setContactMail(userMailSetter());
			cd.setContactPhone(userPhoneSetter());

			return cd;

		}

	}

	private ContactMail userMailSetter() {

		ContactMail cm = null;
		String tablename = TableSchema.tables.Contact_mail.getTableName();

		cm = new ContactMail(getInt(tablename + "." + TableSchema.Contact_mail.contact_id),

				getString(tablename + "." + TableSchema.Contact_mail.Contact_email_id));

		return cm;

	}

	private ContactPhone userPhoneSetter() {

		ContactPhone cp = null;
		String tablename = TableSchema.tables.Contact_phone.getTableName();

		cp = new ContactPhone(getInt(tablename + "." + TableSchema.Contact_phone.contact_id),

				getString(tablename + "." + TableSchema.Contact_phone.Contact_phone_no));

		return cp;

	}

	private EmailUser userEmailSetter() {

		EmailUser email = null;
		String tablename = TableSchema.tables.Email_user.getTableName();

		email = new EmailUser(getInt(tablename + "." + TableSchema.Email_user.em_id),
				getString(tablename + "." + TableSchema.Email_user.email),

				getBoolean(tablename + "." + TableSchema.Email_user.is_primary));

		return email;

	}

	private LoginCredentials userLoginSetter() {

		LoginCredentials login = null;
		String tablename = TableSchema.tables.Login_credentials.getTableName();

		login = new LoginCredentials(getInt(tablename + "." + TableSchema.Login_credentials.id),
				getString(tablename + "." + TableSchema.Login_credentials.username));

		return login;

	}

	private Session userSessionSetter() {

		Session session = null;
		String tablename = TableSchema.tables.Session.getTableName();

		session = new Session(getString(tablename + "." + TableSchema.Session.Session_id),
				getLong(tablename + "." + TableSchema.Session.last_accessed),
				getInt(tablename + "." + TableSchema.Session.user_id));

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
