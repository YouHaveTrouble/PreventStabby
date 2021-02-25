package eu.endermite.togglepvp.config;

import eu.endermite.togglepvp.TogglePvp;
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
    @Getter private final long combat_time;
    @Getter private final boolean punish_for_combat_logout;
    @Getter private final boolean punish_for_combat_logout_announce;
    @Getter private final String punish_for_combat_logout_message;
    @Getter private final String entering_combat;
    @Getter private final String leaving_combat;
    @Getter final String cant_do_that_during_combat;
    @Getter final boolean only_owner_can_interact_with_pet;
    @Getter final long login_protection_time;

    public ConfigCache() {

        Configuration config = TogglePvp.getPlugin().getConfig();

        // Settings
        this.pvp_enabled_by_default = config.getBoolean("settings.pvp_enabled_by_default", false);

        this.lava_and_fire_stopper_enabled = config.getBoolean("settings.lava_and_fire_stopper.enabled", true);
        this.lava_and_fire_stopper_radius = config.getDouble("settings.lava_and_fire_stopper.radius", 2.5);

        this.channeling_enchant_disabled = config.getBoolean("settings.channeling_enchant_disabled", true);

        this.combat_time = config.getLong("settings.combat_time", 25L);
        this.punish_for_combat_logout = config.getBoolean("settings.punish_for_combat_logout.enabled", true);
        this.punish_for_combat_logout_announce = config.getBoolean("settings.punish_for_combat_logout.announce", true);
        this.punish_for_combat_logout_message = config.getString("settings.punish_for_combat_logout.message", "&f%player% logged out while in combat. What a loser.");
        this.only_owner_can_interact_with_pet = config.getBoolean("settings.only_owner_can_interact_with_pet", false);
        this.cache_time = config.getLong("settings.cache_time", 30L);

        this.login_protection_time = config.getLong("settings.login_protection_time", 0);

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
        this.pvp_disabled_other = config.getString("messages.pvp_disabled_others", "&cYou've disabled %player%'s PvP.");
        this.entering_combat = config.getString("messages.entering_combat", "&cEntering combat");
        this.leaving_combat = config.getString("messages.leaving_combat", "&cLeaving combat");
        this.cant_do_that_during_combat = config.getString("messages.cant_do_that_during_combat", "&cYou can't do that while in combat!");
    }
}
