package eu.endermite.togglepvp.util;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseSQLite {

    private String url;
    private File folder;

    public DatabaseSQLite(String url, File folder) {
        this.url = url;
        this.folder = folder;

    }

    public boolean createDatabaseFile() {
        this.folder.mkdir();
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                Statement statement = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS `players` (`player_uuid` varchar(36) UNIQUE PRIMARY KEY, `pvpenabled` boolean);";
                statement.execute(sql);
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void testConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public HashMap<String, Object> getPlayerInfo(Player p) {
        HashMap<String, Object> dataHashMap = new HashMap<>();

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement insertnewuser = conn.createStatement();
            try {
                String newuserdata = "INSERT OR IGNORE INTO `players` (player_uuid, pvpenabled) VALUES ('" + p.getUniqueId().toString() + "', " + TogglePvP.getPlugin().getConfigCache().isPvp_enabled_by_default() + ")";
                insertnewuser.execute(newuserdata);
            } catch (SQLException e) {
                if (e.getErrorCode() != 19) {
                    e.printStackTrace();
                }
            }
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM `players` WHERE `player_uuid` = '" + p.getUniqueId().toString() + "';";
            statement.execute(sql);
            ResultSet result = statement.getResultSet();
            dataHashMap.put("pvpenabled", result.getBoolean("pvpenabled"));

            conn.close();
            return dataHashMap;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void updatePlayerInfo(Player p, HashMap<String, Object> data) {

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement insertnewuser = conn.createStatement();
            try {
                String newuserdata = "UPDATE `players` SET pvpenabled = "+data.get("pvpenabled")+" WHERE `player_uuid` = '"+p.getUniqueId().toString()+"';";
                insertnewuser.execute(newuserdata);
            } catch (SQLException e) {
                TogglePvP.getPlugin().getLogger().severe("Error while saving player data!");
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
