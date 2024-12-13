package dboperation;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;

import dbmodel.UserContacts;
import dbmodel.UserData;
import dbpojo.CategoryRelation;
import dbpojo.ContactDetails;
import dbpojo.EmailUser;
import dbpojo.Session;
import dbpojo.Userdata;
import querybuilder.QueryBuilder;
import querybuilder.SqlQueryLayer;
import querybuilder.TableSchema;
import querybuilder.TableSchema.Category;
import querybuilder.TableSchema.Category_relation;
import querybuilder.TableSchema.Contact_details;
import querybuilder.TableSchema.JoinType;
import querybuilder.TableSchema.Operation;
import querybuilder.TableSchema.tables;
import querybuilder.TableSchema.user_data;

public class TestOperation {

	public static void main(String[] args) {

		QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		qg.openConnection();

		ArrayList<Object> result = qg.select(TableSchema.tables.Email_user)
				.join(JoinType.left, TableSchema.Email_user.em_id, Operation.Equal, TableSchema.user_data.user_id)
				.where(TableSchema.user_data.user_id, Operation.Equal, 26).buildQuery();
				//.join(JoinType.left, TableSchema.user_data.user_id, Operation.Equal, TableSchema.Login_credentials.id)

		if (result == null) {
			System.out.print("result is null");
		}

		for (Object data : result) {

			Userdata ud = null;

			if (data instanceof Userdata) {

				ud = (Userdata) data;
				System.out.println("User Data :" + ud.getUserId() + "  " + ud.getName() + "  " + ud.getAddress());

				for (EmailUser useremail : ud.getemail()) {

					System.out.println(useremail.getEmail() + "  user id :  " + useremail.getEmailId());

				}

				System.out.println("Login info:   " + ud.getLoginCredentials().getUserId() + "   "
						+ ud.getLoginCredentials().getUserName());
			}else if (data instanceof EmailUser) {
				EmailUser ue = (EmailUser) data;
				System.out.println(ue.toString());
			}

		}
//
//		ArrayList<Object> result = qg.select(TableSchema.tables.Contact_details)
//				.join(JoinType.left, TableSchema.Contact_details.contact_id, Operation.Equal,
//						TableSchema.Contact_mail.contact_id)
//				.join(JoinType.left, TableSchema.Contact_details.contact_id, Operation.Equal,
//						TableSchema.Contact_phone.contact_id)
//				.buildQuery();
//
//		if (result == null) {
//			System.out.print("result is null");
//		}
//
//		ContactDetails cd = null;
//		for (Object data : result) {
//
//			if (data instanceof ContactDetails) {
//				cd = (ContactDetails) data;
//				System.out.println("User Data :" + cd.getFirstName() + "  " + cd.getLastName());
//
//				System.out.println("mail info" + cd.getContactMail().getContactMailID());
//
//				System.out.println("phone info" + cd.getContactphone().getContactPhone());
//			}
//
//		}

//		  ArrayList<Object> result = qg.select(TableSchema.tables.Category).join(JoinType.left,TableSchema.Category.Category_id , Operation.Equal, TableSchema.Category_relation.Category_id).join(JoinType.left, TableSchema.Category_relation.contact_id_to_join, Operation.Equal, TableSchema.Contact_details.contact_id).buildQuery();
//
//			
//			
//			if(result ==null) {
//				System.out.print("result is null");
//			}
//			
//			 dbpojo.Category ca = null;
//			for ( Object data : result) {
//
//				
//			
//				if (data instanceof dbpojo.Category) {
//					ca= (dbpojo.Category) data;
//					
//					
//					
//					System.out.println(" category "  + ca.getCategoryName()+" user"+ ca.getCreatedBy());
//					
//					
//					System.out.println("contact in category");
//					for(CategoryRelation cr: ca.getCategoryRelation()) {
//						
//	                        System.out.println("contact name"+cr.getContactIDtoJoin().getFirstName());
//					}
//				}
//
//
//			}

//		 ArrayList<Object> result2 = qg.select(TableSchema.tables.Email_user).buildQuery();
//			if(result2 ==null) {
//				System.out.print("result is null");
//			}
//			
//			 EmailUser email  = null;
//			for ( Object data : result2) {
//
//				
//			
//				if (data instanceof EmailUser) {
//					email=( EmailUser) data;
//					
//					
//					
//					System.out.println(" Email data "  + email.getEmail()+" user"+ email.getEmailId()+"  is primary"+email.getIsPrimary());
//					
//					
//					
//				}
//
//
//			}

		qg.closeConnection();
	}

}
