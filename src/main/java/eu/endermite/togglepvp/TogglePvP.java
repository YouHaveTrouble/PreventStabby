package eu.endermite.togglepvp;

import eu.endermite.togglepvp.commands.MainCommand;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.listeners.player.*;
import eu.endermite.togglepvp.listeners.wolf.WolfAttackPlayerListener;
import eu.endermite.togglepvp.listeners.wolf.WolfTargettingListener;
import eu.endermite.togglepvp.players.PlayerManager;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.DatabaseSQLite;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class TogglePvP extends JavaPlugin {

    @Getter private static TogglePvP plugin;
    private ConfigCache configCache;
    private PlayerManager playerManager;
    private DatabaseSQLite sqLite;

    @Override
    public void onEnable() {
        plugin = this;
        reloadPluginConfig();
        File dbFile = new File("plugins/TogglePvP");
        sqLite = new DatabaseSQLite("jdbc:sqlite:plugins/TogglePvP/TogglePvP.db", dbFile);
        sqLite.createDatabaseFile();
        sqLite.testConnection();
        playerManager = new PlayerManager();

        SmartCache.runSmartCache();

        getServer().getPluginManager().registerEvents(new PlayerJoinAndLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerAttackListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerHitByProjectileListener(), this);
        getServer().getPluginManager().registerEvents(new AreaEffectCloudApplyListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerHitBySplashPotionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerHitByLightningListener(), this);
        getServer().getPluginManager().registerEvents(new LightningBlockIgniteListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerHitByFireworkListener(), this);
        getServer().getPluginManager().registerEvents(new FishingListener(), this);
        getServer().getPluginManager().registerEvents(new LavaDumpAndIgniteListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPlaceWitherRoseListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerHitByExplosionListener(), this);

        getServer().getPluginManager().registerEvents(new WolfTargettingListener(), this);
        getServer().getPluginManager().registerEvents(new WolfAttackPlayerListener(), this);

        Objects.requireNonNull(getCommand("pvp")).setExecutor(new MainCommand());
        Objects.requireNonNull(getCommand("pvp")).setTabCompleter(new MainCommand());
    }

    public void reloadPluginConfig() {
        saveDefaultConfig();
        reloadConfig();
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

}
