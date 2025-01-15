package schedulers;

import java.util.ArrayList;

import dbpojo.Session;
import loggerfiles.LoggerSet;
import querybuilderconfig.QueryBuilder;
import querybuilderconfig.SqlQueryLayer;
import querybuilderconfig.TableSchema.Operation;
import querybuilderconfig.TableSchema.tables;

public class SessionTableCleaner implements Runnable {
	QueryBuilder qg;

	public SessionTableCleaner(QueryBuilder qg) {

		this.qg = qg;
	}

	public void run() {
	

	}

}
