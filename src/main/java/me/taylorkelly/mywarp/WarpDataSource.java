package me.taylorkelly.mywarp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WarpDataSource {
	public final static String sqlitedb = "/warps.db";
    private final static String WARP_TABLE = "CREATE TABLE `warpTable` ("
	    + "`id` INTEGER PRIMARY KEY,"
	    + "`name` varchar(32) NOT NULL DEFAULT 'warp',"
        + "`creator` varchar(32) NOT NULL DEFAULT 'Player',"
        + "`world` varchar(32) NOT NULL DEFAULT '0',"
        + "`x` DOUBLE NOT NULL DEFAULT '0',"
        + "`y` tinyint NOT NULL DEFAULT '0',"
        + "`z` DOUBLE NOT NULL DEFAULT '0',"
        + "`yaw` smallint NOT NULL DEFAULT '0',"
        + "`pitch` smallint NOT NULL DEFAULT '0',"
        + "`publicAll` boolean NOT NULL DEFAULT '1',"
        + "`permissions` text,"
        + "`welcomeMessage` varchar(100) NOT NULL DEFAULT ''"
        + ");";

    public static void initialize() {
        if (!tableExists()) {
            createTable();
        }
        dbTblCheck();
    }

    public static HashMap<String, Warp> getMap() {
        HashMap<String, Warp> ret = new HashMap<String, Warp>();
        Statement statement = null;
        ResultSet set = null;
        try {
            Connection conn = ConnectionManager.getConnection();

            statement = conn.createStatement();
            set = statement.executeQuery("SELECT * FROM warpTable");
            int size = 0;
            while (set.next()) {
                size++;
                int index = set.getInt("id");
                String name = set.getString("name");
                String creator = set.getString("creator");
                String world = set.getString("world");
                double x = set.getDouble("x");
                int y = set.getInt("y");
                double z = set.getDouble("z");
                int yaw = set.getInt("yaw");
                int pitch = set.getInt("pitch");
                boolean publicAll = set.getBoolean("publicAll");
                String permissions = set.getString("permissions");
                String welcomeMessage = set.getString("welcomeMessage");
                Warp warp = new Warp(index, name, creator, world, x, y, z, yaw, pitch, publicAll, permissions, welcomeMessage);
                ret.put(name, warp);
            }
            WarpLogger.info("[MYWARP]: " + size + " warps loaded");
        } catch (SQLException ex) {
            WarpLogger.severe("[MYWARP]: Warp Load Exception");
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (set != null)
                    set.close();
            } catch (SQLException ex) {
            	WarpLogger.severe("[MYWARP]: Warp Load Exception (on close)");
            }
        }
        return ret;
    }

    private static boolean tableExists() {
        ResultSet rs = null;
        try {
            Connection conn = ConnectionManager.getConnection();

            DatabaseMetaData dbm = conn.getMetaData();
            rs = dbm.getTables(null, null, "warpTable", null);
            if (!rs.next())
                return false;
            return true;
        } catch (SQLException ex) {
            Logger log = Logger.getLogger("Minecraft");
            WarpLogger.severe("[MYWARP]: Table Check Exception", ex);
            return false;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException ex) {
                Logger log = Logger.getLogger("Minecraft");
                WarpLogger.severe("[MYWARP]: Table Check SQL Exception (on closing)");
            }
        }
    }

    private static void createTable() {
    	Statement st = null;
    	try {
    		WarpLogger.info("[MyWarp] Creating Database...");
    		Connection conn = ConnectionManager.getConnection();
    		st = conn.createStatement();
    		st.executeUpdate(WARP_TABLE);
    		conn.commit();
    		
    		if(WarpSettings.usemySQL){ 
    			// We need to set auto increment on SQL.
    			String sql = "ALTER TABLE `warpTable` CHANGE `id` `id` INT NOT NULL AUTO_INCREMENT ";
    			WarpLogger.info("[MyWarp] Modifying database for MySQL support");
    			st = conn.createStatement();
    			st.executeUpdate(sql);
    			conn.commit();
    			
    			// Check for old warps.db and import to mysql
    			File sqlitefile = new File(WarpSettings.dataDir.getAbsolutePath() + sqlitedb);
    			if (!sqlitefile.exists()) {
    				WarpLogger.info("[MyWarp] Could not find old " + sqlitedb);
    				return;
    			} else {
	    			WarpLogger.info("[MyWarp] Trying to import warps from warps.db");
	        		Class.forName("org.sqlite.JDBC");
	        		Connection sqliteconn = DriverManager.getConnection("jdbc:sqlite:" + WarpSettings.dataDir.getAbsolutePath() + sqlitedb);
	        		sqliteconn.setAutoCommit(false);
	        		Statement slstatement = sqliteconn.createStatement();
	        		ResultSet slset = slstatement.executeQuery("SELECT * FROM warpTable");
	        		
	        		int size = 0;
	        		while (slset.next()) {
	        			size++;
	        			int index = slset.getInt("id");
	        			String name = slset.getString("name");
	        			String creator = slset.getString("creator");
	        			String world = slset.getString("world");
	        			double x = slset.getDouble("x");
	        			int y = slset.getInt("y");
	        			double z = slset.getDouble("z");
	        			int yaw = slset.getInt("yaw");
	        			int pitch = slset.getInt("pitch");
	        			boolean publicAll = slset.getBoolean("publicAll");
	        			String permissions = slset.getString("permissions");
	        			String welcomeMessage = slset.getString("welcomeMessage");
	        			Warp warp = new Warp(index, name, creator, world, x, y, z, yaw, pitch, publicAll, permissions, welcomeMessage);
	        			addWarp(warp);
	        		}
	        		WarpLogger.info("[MyWarp] Imported " + size + " warps from " + sqlitedb);
	        		WarpLogger.info("[MyWarp] Renaming " + sqlitedb + " to " + sqlitedb + ".old");
	        		if (!sqlitefile.renameTo(new File(WarpSettings.dataDir.getAbsolutePath(), sqlitedb + ".old"))) {
	    				WarpLogger.warning("[MyWarp] Failed to rename " + sqlitedb + "! Please rename this manually!");
	    			}
	        		if (slstatement != null) {
        				slstatement.close();
        			}
        			if (slset != null) {
        				slset.close();
        			}
    				
    				if (sqliteconn != null) {
        				sqliteconn.close();
    				}
    			}
    		}
    	} catch (SQLException e) {
    		WarpLogger.severe("[MyWarp] Create Table Exception", e);
    	} catch (ClassNotFoundException e) {
            WarpLogger.severe("[MyWarp] You need the SQLite library.", e);
    	} finally {
    		try {
    			if (st != null) {
    				st.close();
    			}
    		} catch (SQLException e) {
    			WarpLogger.severe("[MyWarp] Could not create the table (on close)");
    		}
    	}
    }

    public static void addWarp(Warp warp) {
        PreparedStatement ps = null;
        Logger log = Logger.getLogger("Minecraft");
        try {
            Connection conn = ConnectionManager.getConnection();

            ps = conn
                    .prepareStatement("INSERT INTO warpTable (id, name, creator, world, x, y, z, yaw, pitch, publicAll, permissions, welcomeMessage) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, warp.index);
            ps.setString(2, warp.name);
            ps.setString(3, warp.creator);
            ps.setString(4, warp.world);
            ps.setDouble(5, warp.x);
            ps.setInt(6, warp.y);
            ps.setDouble(7, warp.z);
            ps.setInt(8, warp.yaw);
            ps.setInt(9, warp.pitch);
            ps.setBoolean(10, warp.publicAll);
            ps.setString(11, warp.permissionsString());
            ps.setString(12, warp.welcomeMessage);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            WarpLogger.severe("[MYWARP]: Warp Insert Exception", ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                WarpLogger.severe("[MYWARP]: Warp Insert Exception (on close)", ex);
            }
        }
    }

    public static void deleteWarp(Warp warp) {
        PreparedStatement ps = null;
        ResultSet set = null;
        Logger log = Logger.getLogger("Minecraft");
        try {
            Connection conn = ConnectionManager.getConnection();

            ps = conn.prepareStatement("DELETE FROM warpTable WHERE id = ?");
            ps.setInt(1, warp.index);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            WarpLogger.severe("[MYWARP]: Warp Delete Exception", ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (set != null) {
                    set.close();
                }
            } catch (SQLException ex) {
                WarpLogger.severe("[MYWARP]: Warp Delete Exception (on close)", ex);
            }
        }
    }

    public static void publicizeWarp(Warp warp, boolean publicAll) {
        PreparedStatement ps = null;
        ResultSet set = null;
        Logger log = Logger.getLogger("Minecraft");
        try {
            Connection conn = ConnectionManager.getConnection();

            ps = conn.prepareStatement("UPDATE warpTable SET publicAll = ? WHERE id = ?");
            ps.setBoolean(1, publicAll);
            ps.setInt(2, warp.index);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            WarpLogger.severe("[MYWARP]: Warp Publicize Exception", ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (set != null) {
                    set.close();
                }
            } catch (SQLException ex) {
                WarpLogger.severe("[MYWARP]: Warp Publicize Exception (on close)", ex);
            }
        }
    }

    public static void updatePermissions(Warp warp) {
        PreparedStatement ps = null;
        ResultSet set = null;
        Logger log = Logger.getLogger("Minecraft");
        try {
            Connection conn = ConnectionManager.getConnection();

            ps = conn.prepareStatement("UPDATE warpTable SET permissions = ? WHERE id = ?");
            ps.setString(1, warp.permissionsString());
            ps.setInt(2, warp.index);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            WarpLogger.severe("[MYWARP]: Warp Permissions Exception", ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (set != null) {
                    set.close();
                }
            } catch (SQLException ex) {
                WarpLogger.severe("[MYWARP]: Warp Permissions Exception (on close)", ex);
            }
        }
    }

    public static void updateCreator(Warp warp) {
        PreparedStatement ps = null;
        ResultSet set = null;
        Logger log = Logger.getLogger("Minecraft");
        try {
            Connection conn = ConnectionManager.getConnection();

            ps = conn.prepareStatement("UPDATE warpTable SET creator = ? WHERE id = ?");
            ps.setString(1, warp.creator);
            ps.setInt(2, warp.index);
            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            WarpLogger.severe("[MYWARP]: Warp Creator Exception", ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (set != null) {
                    set.close();
                }
            } catch (SQLException ex) {
                WarpLogger.severe("[MYWARP]: Warp Creator Exception (on close)", ex);
            }
        }
    }

    public static void updateWelcomeMessage(Warp warp) {
        PreparedStatement ps = null;
        ResultSet set = null;
        Logger log = Logger.getLogger("Minecraft");
        try {
            Connection conn = ConnectionManager.getConnection();

            ps = conn.prepareStatement("UPDATE warpTable SET welcomeMessage = ? WHERE id = ?");
            ps.setString(1, warp.welcomeMessage);
            ps.setInt(2, warp.index);
            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            WarpLogger.severe("[MYWARP]: Warp Creator Exception", ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (set != null) {
                    set.close();
                }
            } catch (SQLException ex) {
                WarpLogger.severe("[MYWARP]: Warp Creator Exception (on close)", ex);
            }
        }
    }
    
    public static void dbTblCheck() {
    	// Add future modifications to the table structure here
    	
    }

    public static void updateDB(String test, String sql) {
    	// Use same sql for both mysql/sqlite
    	updateDB(test, sql, sql);
    }

    public static void updateDB(String test, String sqlite, String mysql) {
    	// Allowing for differences in the SQL statements for mysql/sqlite.
    	try {
    		Connection conn = ConnectionManager.getConnection();
    		Statement statement = conn.createStatement();
    		statement.executeQuery(test);
    		statement.close();
    	} catch(SQLException ex) {
    		WarpLogger.info("[MYWARP]: Updating database");
    		// Failed the test so we need to execute the updates
    		try {
    			String[] query;
    			if (WarpSettings.usemySQL) {
    				query = mysql.split(";");
    			} else { 
    				query = sqlite.split(";");
    			}

    			Connection conn = ConnectionManager.getConnection();
    			Statement sqlst = conn.createStatement();
    			for (String qry : query) {
    				sqlst.executeUpdate(qry);
    			}
    			conn.commit();
    			sqlst.close();
    		} catch (SQLException exc) {
    			WarpLogger.severe("[MYWARP]: Failed to update the database to the new version - ", exc);
    			ex.printStackTrace();
    		}	
    	}
    }

    public static void updateFieldType(String field, String type) {
    	try {
    		if (!WarpSettings.usemySQL) return;
    		WarpLogger.info("[MYWARP]: Updating database");
    		
    		Connection conn = ConnectionManager.getConnection();
    		DatabaseMetaData meta = conn.getMetaData();

    		ResultSet colRS = null;
    		colRS = meta.getColumns(null, null, "warpTable", null);
    		while (colRS.next()) {
    			String colName = colRS.getString("COLUMN_NAME");
    			String colType = colRS.getString("TYPE_NAME");
    			
    			if (colName.equals(field) && !colType.equals(type))
    			{
    				Statement stm = conn.createStatement();
    				stm.executeUpdate("ALTER TABLE warpTable MODIFY " + field + " " + type + "; ");
    				conn.commit();
    				stm.close();
    				break;
    			}
    		}
    		colRS.close();
    	} catch(SQLException ex) {
    		WarpLogger.severe("[MYWARP]: Failed to update the database to the new version - ", ex);
    		ex.printStackTrace();
    	}
    }

    public static void updatePosition(Warp warp) {
        PreparedStatement ps = null;
        ResultSet set = null;
        Logger log = Logger.getLogger("Minecraft");
        try {
            Connection conn = ConnectionManager.getConnection();

            ps = conn.prepareStatement("UPDATE warpTable SET x = ?, y = ?, z = ?, pitch = ?, yaw = ? WHERE id = ?");
            ps.setDouble(1, warp.x);
            ps.setInt(2, warp.y);
            ps.setDouble(3, warp.z);
            ps.setInt(4, warp.yaw);
            ps.setInt(5, warp.pitch);
            ps.setInt(6, warp.index);
            ps.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            log.log(Level.SEVERE, "[MYWARP]: Warp Creator Exception", ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (set != null) {
                    set.close();
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "[MYWARP]: Warp Creator Exception (on close)", ex);
            }
        }
    }

}
