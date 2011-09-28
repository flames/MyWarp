package me.taylorkelly.mywarp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionManager {
    private static Connection conn;
    private static final WarpLogger logger = WarpLogger.getLogger();
        
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
            logger.severe("SQL exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            logger.severe("You need the SQLite/MySQL library.", ex);
        }
        return conn;
    }

    public static Connection getConnection() {
        if(conn == null) conn = initialize();
      	return conn;
    }

    public static void closeConnection() {
        if(conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
                logger.severe("Error on Connection close", ex);
            }
        }
    }


}