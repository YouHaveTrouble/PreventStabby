package me.youhavetrouble.preventstabby;

import me.youhavetrouble.preventstabby.commands.MainCommand;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.hooks.PlaceholderApiHook;
import me.youhavetrouble.preventstabby.hooks.WorldGuardHook;
import me.youhavetrouble.preventstabby.data.PlayerListener;
import me.youhavetrouble.preventstabby.data.PlayerManager;
import me.youhavetrouble.preventstabby.util.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PreventStabby extends JavaPlugin {

    private static PreventStabby plugin;
    private ConfigCache configCache;
    private PlayerManager playerManager;
    private DatabaseSQLite sqLite;
    private static boolean worldGuardHook;

    @Override
    public void onEnable() {
        plugin = this;
        reloadPluginConfig();
        playerManager = new PlayerManager(this);

        // Register listeners TODO
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

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

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderApiHook(this).register();
        }

        Metrics metrics = new Metrics(this, 14074);
    }

    @Override
    public void onLoad() {
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            try {
                WorldGuardHook.init();
                worldGuardHook = true;
            } catch (NoClassDefFoundError e) {
                worldGuardHook = false;
            }
        }
    }

    public static boolean worldGuardHookEnabled() {
        return worldGuardHook;
    }

    public void reloadPluginConfig() {
        configCache = new ConfigCache(this);
    }

    public void reloadPluginConfig(CommandSender commandSender) {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            reloadPluginConfig();
            PluginMessages.sendMessage(commandSender, "PreventStabby configuration reloaded.");
        });
    }

    public ConfigCache getConfigCache() {
        return configCache;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public DatabaseSQLite getSqLite() {return sqLite;}

    public static PreventStabby getPlugin() {
        return plugin;
    }

}
