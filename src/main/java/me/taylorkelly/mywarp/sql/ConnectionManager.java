package me.taylorkelly.mywarp.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.taylorkelly.mywarp.WarpSettings;
import me.taylorkelly.mywarp.utils.WarpLogger;

public class ConnectionManager {
    private static Connection conn;
        
    public static Connection initialize() {
        try {
        	if(WarpSettings.usemySQL == true) {
        		Class.forName("com.mysql.jdbc.Driver");
        		conn = DriverManager.getConnection(WarpSettings.mySQLconn, WarpSettings.mySQLuname, WarpSettings.mySQLpass);
        		conn.setAutoCommit(false);
        		return conn;
        	} else {
        		Class.forName("org.sqlite.JDBC");
        		conn = DriverManager.getConnection("jdbc:sqlite:" + WarpSettings.dataDir.getAbsolutePath() + "/warps.db");
        		conn.setAutoCommit(false);
        		return conn;
        	}
        } catch (SQLException ex) {
            WarpLogger.severe("SQL exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            WarpLogger.severe("You need the SQLite/MySQL library.", ex);
        }
        return conn;
    }

	public static Connection getConnection() {
		if(conn == null) conn = initialize();
		if(WarpSettings.usemySQL) {
			// We probably dont need to do this for SQLite. 
			try {
				if(!conn.isValid(10)) conn = initialize();
			} catch (SQLException ex) {
				WarpLogger.severe("Failed to check SQL status", ex);
			}
		}
		return conn;
	}

    public static void closeConnection() {
		if(conn != null) {
			try {
				if(WarpSettings.usemySQL){
					if(conn.isValid(10)) {
						conn.close();
					}
					conn = null;
				} else {
					conn.close();
					conn = null;
				}
			} catch (SQLException ex) {
				WarpLogger.severe("Error on Connection close", ex);
			}
		}
    }


}