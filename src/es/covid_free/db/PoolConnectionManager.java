package es.covid_free.db;

import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class PoolConnectionManager {
	
	public final static Connection getConnection() {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			System.out.println(envCtx.toString());
			DataSource ds = (DataSource) envCtx.lookup("jdbc/covid_free");
			System.out.println(ds.toString());
			
			Connection conn = ds.getConnection();
			
			return conn;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public final static void releaseConnection(Connection conn) {
		try {
			if(conn != null) {
				conn.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
