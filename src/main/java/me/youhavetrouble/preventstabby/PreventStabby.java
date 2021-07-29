package me.youhavetrouble.preventstabby;

import me.youhavetrouble.preventstabby.commands.MainCommand;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.players.PlayerManager;
import me.youhavetrouble.preventstabby.players.SmartCache;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import me.youhavetrouble.preventstabby.util.DatabaseSQLite;
import lombok.Getter;
import me.youhavetrouble.preventstabby.util.Util;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public final class PreventStabby extends JavaPlugin {

    @Getter private static PreventStabby plugin;
    private ConfigCache configCache;
    protected PlayerManager playerManager;
    protected DatabaseSQLite sqLite;
    protected SmartCache smartCache;

    @Override
    public void onEnable() {
        plugin = this;
        Util.initData();
        reloadPluginConfig();
        File dbFile = new File("plugins/PreventStabby");
        sqLite = new DatabaseSQLite("jdbc:sqlite:plugins/PreventStabby/database.db", dbFile);
        sqLite.createDatabaseFile();
        if (!sqLite.testConnection()) {
            getLogger().severe("Error with accessing database. Check if server has write rights.");
            getLogger().severe("Plugin will now disable.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        playerManager = new PlayerManager();
        smartCache = new SmartCache();
        smartCache.runSmartCache();

        // Register listeners
        Reflections reflections = new Reflections(new String[]{"me.youhavetrouble.preventstabby"});
        Set<Class<?>> listenerClasses = reflections.getTypesAnnotatedWith(PreventStabbyListener.class);
        listenerClasses.forEach((listener)-> {
            try {
                getServer().getPluginManager().registerEvents((org.bukkit.event.Listener) listener.getConstructor().newInstance(), this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                getLogger().severe("Error with registering listeners.");
                getLogger().severe("Plugin will now disable.");
                getServer().getPluginManager().disablePlugin(this);
            }
        });

        // Register command
        PluginCommand pvpCommand = getCommand("pvp");
        if (pvpCommand == null) {
            getLogger().severe("Error with registering commands.");
            getLogger().severe("Plugin will now disable.");
            getServer().getPluginManager().disablePlugin(this);
        }
        MainCommand mainCommand = new MainCommand();
        pvpCommand.setExecutor(mainCommand);
        pvpCommand.setTabCompleter(mainCommand);

        Metrics metrics = new Metrics(this, 10597);
    }

    public void reloadPluginConfig() {
        configCache = new ConfigCache();
    }

    public void reloadPluginConfig(CommandSender commandSender) {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            reloadPluginConfig();
            commandSender.sendMessage("TogglePvP configuration reloaded.");
        });
    }

    public ConfigCache getConfigCache() {
        return configCache;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public DatabaseSQLite getSqLite() {return sqLite;}

    public SmartCache getSmartCache() {
        return smartCache;
    }
}
