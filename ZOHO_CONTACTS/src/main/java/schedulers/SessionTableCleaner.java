package schedulers;

import java.util.ArrayList;

import dbpojo.Session;
import loggerfiles.LoggerSet;
import querybuilder.QueryBuilder;
import querybuilder.SqlQueryLayer;
import querybuilder.TableSchema.Operation;
import querybuilder.TableSchema.tables;

public class SessionTableCleaner implements Runnable {
	QueryBuilder qg;

	public SessionTableCleaner(QueryBuilder qg) {

		this.qg = qg;
	}

	public void run() {
	

	}

}
