package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerListener implements ServletContextListener{
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("the context here started see !!..");
		
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("the context ends here see...!!");
	}

}
