package eu.endermite.togglepvp.config;

import eu.endermite.togglepvp.TogglePvP;
import lombok.Getter;
import org.bukkit.configuration.Configuration;

public class ConfigCache {

    @Getter private final boolean pvp_enabled_by_default, lava_and_fire_stopper_enabled;
    @Getter private final String pvp_enabled, pvp_disabled, cannot_attack_victim, cannot_attack_attacker, no_permission, no_such_command;
    @Getter private final double lava_and_fire_stopper_radius;

    public ConfigCache() {

        Configuration config = TogglePvP.getPlugin().getConfig();

        // Settings
        this.pvp_enabled_by_default = config.getBoolean("settings.pvp_enabled_by_default", false);

        this.lava_and_fire_stopper_enabled = config.getBoolean("settings.lava_and_fire_stopper.enabled", true);
        this.lava_and_fire_stopper_radius = config.getDouble("settings.lava_and_fire_stopper.radius", 2.5);

        // Messages
        this.pvp_enabled = config.getString("messages.pvp_enabled", "&cYou enabled PvP!");
        this.pvp_disabled = config.getString("messages.pvp_disabled", "&cYou disabled PvP!");
        this.cannot_attack_victim = config.getString("messages.cannot_attack_victim", "&cYou can't attack players that have PvP turned off!");
        this.cannot_attack_attacker = config.getString("messages.cannot_attack_attacker", "&cYou can't attack players while you have PvP turned off!");
        this.no_permission = config.getString("messages.no_permission", "&cYou don't have permission to use that.");
        this.no_such_command = config.getString("messages.no_such_command", "&cNo such command.");
    }
}
