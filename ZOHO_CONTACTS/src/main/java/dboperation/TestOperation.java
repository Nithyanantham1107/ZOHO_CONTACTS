package dboperation;

import java.sql.SQLException;
import java.time.Instant;

import com.google.gson.Gson;

import dbpojo.Category;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.ContactMail;
import dbpojo.ContactPhone;
import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Userdata;
import exception.DBOperationException;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

public class TestOperation {

	public static void main(String[] args) throws SQLException, DBOperationException {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		qg.openConnection();
		
		
		Gson gson=new Gson();

//		ContactDetails contact=new ContaSystectDetails();
//		contact.setID(68);
//		contact.setFirstName("superman");
//		contact.setMiddleName("devil1");
//		contact.setUserID(70);
//		contact.setLastName("devi2");
//		contact.setGender("male");
//		System.out.println("here gender is"+ contact.getGender());
//		contact.setAddress("New York");
//		contact.setCreatedAt(Instant.now().toEpochMilli());
//		contact.setModifiedAt(contact.getCreatedAt());
//		ContactMail cm=new ContactMail(5, contact.getID(), "super123@gmail.com", contact.getCreatedAt(), contact.getCreatedAt());
//		contact.setContactMail(cm);
//		ContactPhone cp=new ContactPhone(6,contact.getID(), "12000", contact.getCreatedAt(), contact.getCreatedAt());
//		contact.setContactPhone(cp);
//		qg.delete(contact).execute(70);

//		
//		Category category = new Category();
//		category.setID(63);
//		category.setCategoryName("junga");
//		category.setCreatedAt(Instant.now().toEpochMilli());
//		category.setModifiedAt(category.getCreatedAt());
//		category.setCreatedBY(71);
//		CategoryRelation cat = new CategoryRelation(7, 67, category.getID(), category.getModifiedAt(), category.getModifiedAt());
//
//		category.setCategoryRelation(cat);
//		qg.insert(category).execute(71);
		UserGroupOperation userGroupOperation = new UserGroupOperation();
	Category group=	userGroupOperation.getSpecificGroup(67, 26);
//		user.setEmail(email);
//		qg.insert(user).execute(-1);
		System.out.println("the group is here hooray !!!"+group.getCategoryName());
		

//		EmailUser email = new EmailUser();
		qg.closeConnection();

//		email.setID(22);
//		email.setEmailID(76);
//		email.setEmailID(user.getID());
//		email.setEmail("123458@gmail.com");
//		email.setIsPrimary(true);
//		email.setModifiedAt(user.getCreatedAt());
//		email.setCreatedAt(user.getCreatedAt());
//		user.setEmail(email);
//		qg.update(email).execute(76);
//		qg.insert(user).execute(-1);

//	Userdata dummy=  (Userdata)	qg.select(user).executeQuery().getFirst();
//	
//		System.out.println("here username"+dummy.getLoginCredentials().getUserName());

//	
//		qg.delete(email).execute(63);
//		user.setID(63);
//		Userdata  dumm= (Userdata) qg.select(user).executeQuery().getFirst();
//		System.out.println("here the name is"+ dumm.getName());

	}

}
