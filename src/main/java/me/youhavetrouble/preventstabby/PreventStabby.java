package me.youhavetrouble.preventstabby;

import me.youhavetrouble.preventstabby.commands.MainCommand;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.hooks.PlaceholderApiHook;
import me.youhavetrouble.preventstabby.hooks.WorldGuardHook;
import me.youhavetrouble.preventstabby.players.PlayerManager;
import me.youhavetrouble.preventstabby.players.SmartCache;
import me.youhavetrouble.preventstabby.util.*;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
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
    private PlayerManager playerManager;

    private DamageUtil damageUtil;
    private DatabaseSQLite sqLite;
    private SmartCache smartCache;
    private static boolean worldGuardHook;

    private static BukkitAudiences adventure;

    @Override
    public void onEnable() {
        plugin = this;
        Util.initData();
        reloadPluginConfig();
        File dbFile = new File("plugins/PreventStabby");
        sqLite = new DatabaseSQLite("jdbc:sqlite:plugins/PreventStabby/database.db", dbFile, getLogger());
        sqLite.createDatabaseFile();
        if (!sqLite.testConnection()) {
            getLogger().severe("Error with accessing database. Check if server has write rights.");
            getLogger().severe("Plugin will now disable.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        adventure = BukkitAudiences.create(this);
        playerManager = new PlayerManager();
        damageUtil = new DamageUtil(this);
        smartCache = new SmartCache();
        smartCache.runSmartCache();

        // Register listeners
        Reflections reflections = new Reflections((Object[]) new String[]{"me.youhavetrouble.preventstabby"});
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
        configCache = new ConfigCache();
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

    public DamageUtil getDamageUtil() {
        return damageUtil;
    }

    public DatabaseSQLite getSqLite() {return sqLite;}

    public SmartCache getSmartCache() {
        return smartCache;
    }

    public static BukkitAudiences getAudiences() {
        return adventure;
    }
}
