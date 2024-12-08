package dboperation;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;

import dbmodel.UserContacts;
import dbmodel.UserData;
import querybuilder.QueryBuilder;
import querybuilder.SqlQueryLayer;
import querybuilder.TableSchema;
import querybuilder.TableSchema.JoinType;
import querybuilder.TableSchema.Operation;

public class TestOperation {

	public static void main(String[] args) {

		
		

	QueryBuilder qg = new SqlQueryLayer().createQueryBuilder();

		qg.openConnection();
	
//		qg.insert(TableSchema.tables.Category,TableSchema.Category.CATEGORY_NAME).valuesInsert("arun").make();
//		qg.select(TableSchema.tables.Email_user, TableSchema.Email_user.em_id,TableSchema.Email_user.is_primary).where(TableSchema.Email_user.em_id, ">", 2).make();
//qg.Delete(TableSchema.tables.Category).make();
//		qg.update(TableSchema.tables.Category, TableSchema.Category.CATEGORY_NAME).valuesUpdate("arunkk").where(TableSchema.Category.CATEGORY_ID, "=", 2).make();
//		qg.Delete(TableSchema.tables.Category_relation).where(TableSchema.Category_relation.CATEGORY_ID, ">",2).make();
	
//		qg.select(TableSchema.tables.Contact_details,TableSchema.Contact_details.user_id,TableSchema.Contact_mail.contact_id).join(JoinType.left,TableSchema.Contact_details.contact_id, Operation.Equal, TableSchema.Contact_mail.contact_id).make();
		ArrayList<Map<String,Object>> result=	qg.select(TableSchema.tables.user_data).where(TableSchema.user_data.user_id, Operation.GreaterEqual, 20).buildQuery();
		
		for(Map<String,Object> data: result) {
		
			System.out.println(data.get("Name")+"  "+data.get("password"));
		}
		
		
		qg.closeConnection();
	}

}
