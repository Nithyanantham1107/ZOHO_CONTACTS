package dboperation;

import querybuilder.QueryBuilder;
import querybuilder.SqlQueryBuilder;

public class TestOperation {
	public static void main(String[] args) {

		QueryBuilder qg = new SqlQueryBuilder().createQueryBuilder();
		String query = qg.select("user").where("1>0").and("0 <1").build();
		
//		String query = qg.update("user","name='arjun'","age=10","gender='f'").where("userid=10").and("age <30").build();
	
//		String query = qg.Delete("user").where("age>10").and("age <30").build();
//		String query = qg.create("user",qg.column("name", "varchar(30)","NOT NULL"),qg.column("age", "int"),qg.column("gmail","varchar(50)")).build();
		
		System.out.println(query);
	}

}
