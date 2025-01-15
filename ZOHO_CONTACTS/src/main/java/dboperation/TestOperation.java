package dboperation;

import java.time.Instant;

import dbpojo.EmailUser;
import dbpojo.LoginCredentials;
import dbpojo.Userdata;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;

public class TestOperation {

	public static void main(String[] args) {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();
		qg.openConnection();
//		EmailUser email = new EmailUser();
//		email.setEmailID(63);
//		
//		email.setEmail("1237890@gmail.com");
//		email.setID(14);
//		email.setIsPrimary(true);
//		email.setModifiedAt(Instant.now().toEpochMilli());
//		email.setCreatedAt(email.getModifiedAt());

//		Userdata user2=new Userdata();
//		user2.setID(63);
//		user2.setName("devil1");
//		changedStateContainer changed=  PojoDataConversion.getChangedPojoData(user, user2);
//		PojoDataContainer contain=PojoDataConversion.convertPojoData(user);
//		PojoDataContainer contain2=PojoDataConversion.convertPojoData(user2);
//		System.out.println("here new"+ user.getID());
//		System.out.println("here old"+user2.getID());

//		user.setPassword("jsfvskc");
//		user.setPhoneno("113461261");
//		user.setAddress("gotham");
//		user.setTimezone("asia");
//		user.setCreatedAt(Instant.now().toEpochMilli());
//		user.setModifiedAt(user.getCreatedAt());
//
//		LoginCredentials login = new LoginCredentials();
//		login.setUserID(user.getID());
//		user.setLoginCredentials(login);
//		login.setUserName("heloooo");
//		login.setCreatedAt(user.getCreatedAt());
//		login.setModifiedAt(user.getModifiedAt());
//		EmailUser email = new EmailUser();
//		email.setEmailID(user.getID());
//		user.setEmail(email);
//		email.setEmail("123457890@gmail.com");
//		email.setIsPrimary(false);
//		email.setCreatedAt(user.getCreatedAt());
//		email.setModifiedAt(user.getCreatedAt());
//
//		user.setEmail(email);
//		user.setLoginCredentials(login);
//		qg.insert(user).execute(-1);

//		ContactDetails contact=new ContactDetails();
//		contact.setID(64);
//		contact.setFirstName("devil12345");
//		contact.setMiddleName("devil1");
//		contact.setUserID(63);
//		contact.setLastName("devil2");
//		contact.setGender("male");
//		contact.setAddress("gotham");
//		contact.setCreatedAt(Instant.now().toEpochMilli());
//		contact.setModifiedAt(contact.getCreatedAt());
//		ContactMail cm=new ContactMail(0, 0, "123213@gmail.com", contact.getCreatedAt(), contact.getCreatedAt());
//		contact.setContactMail(cm);
//		ContactPhone cp=new ContactPhone(0, 0, "12345586", contact.getCreatedAt(), contact.getCreatedAt());
//		contact.setContactPhone(cp);

//		Category category = new Category();
//		category.setID(62);
//		category.setCategoryName("summa");
//		category.setCreatedAt(Instant.now().toEpochMilli());
//		category.setModifiedAt(category.getCreatedAt());
//		category.setCreatedBY(63);
//		CategoryRelation cat = new CategoryRelation(-1, 66, 62, category.getModifiedAt(), category.getModifiedAt());
//
//		category.setCategoryRelation(cat);
		
		
		Userdata user=new Userdata();
		user.setID(65);
		user.setName("arun11111");
		user.setPassword("password");
		user.setPhoneno("12345");
		user.setAddress("gotham");
		user.setTimezone("asia/kolkata");
		user.setCreatedAt(Instant.now().toEpochMilli());
		user.setModifiedAt(user.getCreatedAt());
		LoginCredentials login=new LoginCredentials();
		login.setID(15);
		login.setUserID(user.getID());
		login.setUserName("arun117");
		login.setCreatedAt(user.getCreatedAt());
		login.setModifiedAt(user.getCreatedAt());
		user.setLoginCredentials(login);
		EmailUser email=new EmailUser();
		email.setID(16);
		email.setEmailID(user.getID());
		email.setEmail("arun1233123323@gmail.com");
		email.setIsPrimary(false);
		email.setModifiedAt(user.getCreatedAt());
		email.setCreatedAt(user.getCreatedAt());
		user.setEmail(email);
		qg.delete(user).execute(65);

//	
//		qg.delete(email).execute(63);
//		user.setID(63);
//		Userdata  dumm= (Userdata) qg.select(user).executeQuery().getFirst();
//		System.out.println("here the name is"+ dumm.getName());
		qg.closeConnection();

	}

}
