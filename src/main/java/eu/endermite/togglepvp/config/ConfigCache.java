package eu.endermite.togglepvp.config;

import eu.endermite.togglepvp.TogglePvP;
import lombok.Getter;
import org.bukkit.configuration.Configuration;

public class ConfigCache {

    @Getter private final boolean pvp_enabled_by_default;
    @Getter private final boolean lava_and_fire_stopper_enabled;
    @Getter private final String pvp_enabled;
    @Getter private final String pvp_disabled;
    @Getter private final String cannot_attack_victim;
    @Getter private final String cannot_attack_attacker;
    @Getter private final String cannot_attack_pets_victim;
    @Getter private final String cannot_attack_pets_attacker;
    @Getter private final String no_permission;
    @Getter private final String no_such_command;
    @Getter private final String pvp_enabled_other;
    @Getter private final String pvp_disabled_other;
    @Getter private final double lava_and_fire_stopper_radius;
    @Getter private final boolean channeling_enchant_disabled;
    @Getter private final long cache_time;

    public ConfigCache() {

        Configuration config = TogglePvP.getPlugin().getConfig();

        // Settings
        this.pvp_enabled_by_default = config.getBoolean("settings.pvp_enabled_by_default", false);

        this.lava_and_fire_stopper_enabled = config.getBoolean("settings.lava_and_fire_stopper.enabled", true);
        this.lava_and_fire_stopper_radius = config.getDouble("settings.lava_and_fire_stopper.radius", 2.5);

        this.channeling_enchant_disabled = config.getBoolean("settings.channeling_enchant_disabled", true);

        this.cache_time = config.getLong("settings.cache_time", 30L);

        // Messages
        this.pvp_enabled = config.getString("messages.pvp_enabled", "&cYou enabled PvP!");
        this.pvp_disabled = config.getString("messages.pvp_disabled", "&cYou disabled PvP!");
        this.cannot_attack_victim = config.getString("messages.cannot_attack_victim", "&cYou can't attack players that have PvP turned off!");
        this.cannot_attack_attacker = config.getString("messages.cannot_attack_attacker", "&cYou can't attack players while you have PvP turned off!");
        this.cannot_attack_pets_victim = config.getString("messages.cannot_attack_pets_victim", "&cYou can't attack pets while you have PvP turned off");
        this.cannot_attack_pets_attacker = config.getString("messages.cannot_attack_pets_attacker", "&cYou can't attack pets of players that have PvP turned off");
        this.no_permission = config.getString("messages.no_permission", "&cYou don't have permission to use that.");
        this.no_such_command = config.getString("messages.no_such_command", "&cNo such command.");
        this.pvp_enabled_other = config.getString("messages.pvp_enabled_others", "&cYou've enabled %player%'s PvP.");
        this.pvp_disabled_other =config.getString("messages.pvp_disabled_others", "&cYou've disabled %player%'s PvP.");
    }
}
