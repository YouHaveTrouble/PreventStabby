package me.youhavetrouble.preventstabby.util;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.PlayerData;

import java.io.File;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

public class DatabaseSQLite {

    private final String url;
    private final File folder;
    private final Logger logger;

    public DatabaseSQLite(String url, File folder, Logger logger) {
        this.url = url;
        this.folder = folder;
        this.logger = logger;
        createDatabaseFile();
    }

    private void createDatabaseFile() {
        this.folder.mkdir();
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn == null) return;
            DatabaseMetaData meta = conn.getMetaData();
            logger.info("The driver name is " + meta.getDriverName());
            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `players` (`player_uuid` varchar(36) UNIQUE PRIMARY KEY, `pvpenabled` boolean);";
            statement.execute(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public PlayerData getPlayerInfo(UUID uuid) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT OR IGNORE INTO `players` (player_uuid, pvpenabled) VALUES (?, ?); SELECT * FROM `players` WHERE `player_uuid` = ?;"
            );
            statement.setString(1, uuid.toString());
            statement.setBoolean(2, PreventStabby.getPlugin().getConfigCache().pvp_enabled_by_default);
            statement.setString(3, uuid.toString());
            statement.executeQuery();
            ResultSet result = statement.getResultSet();
            boolean state = result.getBoolean("pvpenabled");
            return new PlayerData(uuid, state);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void updatePlayerInfo(UUID uuid, PlayerData data) {
        try (Connection conn = DriverManager.getConnection(url)) {
            try {
                PreparedStatement insertnewuser = conn.prepareStatement("UPDATE `players` SET pvpenabled = ? WHERE `player_uuid` = ?;");
                insertnewuser.setBoolean(1, data.isPvpEnabled());
                insertnewuser.setString(2, uuid.toString());
            } catch (SQLException exception) {
                logger.severe("Error while saving player data!");
                exception.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
