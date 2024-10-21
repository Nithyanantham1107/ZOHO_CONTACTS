package listener;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import loggerfiles.LoggerSet;
import querybuilder.SqlQueryBuilder;
import querybuilder.QueryBuilder;

public class ServerListener implements ServletContextListener{
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("the context here started see !!..");
		LoggerSet.appLog();
		LoggerSet.accessLog();
	   
		
		
		
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("the context ends here see...!!");
	}

}
