package helper;

import dbpojo.EmailUser;
import dbpojo.Table;
import dbpojo.Userdata;
import querybuilderconfig.TableSchema.Email_user;

public class UpdateDataGetter {

	public Table getChangedData(Table previousData, Table currentData) {

		if (previousData instanceof Userdata && currentData instanceof Userdata) {

		}

	}

	private Table getChangedUserData(Table previousData, Table currentData) {

		Userdata previous = (Userdata) previousData;
		Userdata current = (Userdata) currentData;

		if (previous.getUserId() == current.getUserId()) {
			current.setUserId(-1);
		}
		if (previous.getName().equals(current.getName())) {

			current.setName(null);
		}
		if (previous.getPassword().equals(current.getPassword())) {

			current.setPassword(null);
		}

		if (previous.getPhoneno().equals(current.getPhoneno())) {

			current.setPhoneno(null);

		}
		if (previous.getAddress().equals(current.getAddress())) {

			current.setAddress(null);
		}
		if (previous.getTimezone().equals(current.getTimezone())) {

			current.setTimezone(null);
		}
		if (previous.getCreatedAt() == current.getCreatedAt()) {
			current.setCreatedAt(-1);
		}
		if (previous.getModifiedAt() == current.getCreatedAt()) {
			current.setModifiedAt(-1);

		}
		if (previous.getLoginCredentials().getUserId() == current.getLoginCredentials().getUserId()) {
			current.setLoginCredentials(null);

		}
//		for (EmailUser email : previous.getallemail()) {
//current.getemail(email.)
//		}

	}

}
