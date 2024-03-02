package me.youhavetrouble.preventstabby.config;

import me.youhavetrouble.preventstabby.PreventStabby;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConfigCache {

    public final boolean pvp_enabled_by_default, lava_and_fire_stopper_enabled, channeling_enchant_disabled,
            punish_for_combat_logout, punish_for_combat_logout_announce, only_owner_can_interact_with_pet,
            snowballs_knockback, egg_knockback, block_commands_in_combat, block_teleports_in_combat, allow_fishing_rod_pull;
    public final String pvp_enabled, pvp_disabled, cannot_attack_victim, cannot_attack_attacker,
            cannot_attack_pets_victim, cannot_attack_pets_attacker, no_permission, no_such_command, pvp_enabled_other,
            pvp_disabled_other, punish_for_combat_logout_message, entering_combat, leaving_combat,
            cant_do_that_during_combat, cannot_attack_mounts_attacker, cannot_attack_mounts_victim, force_pvp_on,
            force_pvp_off, force_pvp_none, placeholder_combat_time, placeholder_not_in_combat, cannot_attack_pvp_force_off,
            placeholder_pvp_forced_true, placeholder_pvp_forced_false, placeholder_pvp_forced_none;

    public final String cannot_attack_forced_pvp_off, cannot_attack_teleport_or_spawn_protection_attacker,
            cannot_attack_pets_teleport_or_spawn_protection_attacker, cannot_attack_mounts_teleport_or_spawn_protection_attacker,
            cannot_attack_teleport_or_spawn_protection_victim;

    public final double lava_and_fire_stopper_radius;
    public final long combat_time, login_protection_time, teleport_protection_time, cache_time;
    private final Set<String> combatBlockedCommands = new HashSet<>();

    public ConfigCache(PreventStabby plugin) {

        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        // Settings
        this.pvp_enabled_by_default = config.getBoolean("settings.pvp_enabled_by_default", false);

        this.lava_and_fire_stopper_enabled = config.getBoolean("settings.lava_and_fire_stopper.enabled", true);
        this.lava_and_fire_stopper_radius = config.getDouble("settings.lava_and_fire_stopper.radius", 2.5);

        this.channeling_enchant_disabled = config.getBoolean("settings.channeling_enchant_disabled", true);

        this.combat_time = config.getLong("settings.combat_time", 25L);
        this.punish_for_combat_logout = config.getBoolean("settings.punish_for_combat_logout.enabled", true);
        this.punish_for_combat_logout_announce = config.getBoolean("settings.punish_for_combat_logout.announce", true);
        this.punish_for_combat_logout_message = config.getString("settings.punish_for_combat_logout.message", "%player%<reset><white> logged out while in combat. What a loser.");
        this.only_owner_can_interact_with_pet = config.getBoolean("settings.only_owner_can_interact_with_pet", false);

        this.allow_fishing_rod_pull = config.getBoolean("settings.allow_fishing_rod_pull", false);
        this.snowballs_knockback = config.getBoolean("settings.snowballs_do_knockback", false);
        this.egg_knockback = config.getBoolean("settings.eggs_do_knockback", false);
        this.block_commands_in_combat = config.getBoolean("settings.block_in_combat.block_commands", true);
        if (block_commands_in_combat) {
            this.combatBlockedCommands.addAll(config.getStringList("settings.block_in_combat.block_commands.commands"));
        }
        this.block_teleports_in_combat = config.getBoolean("settings.block_in_combat.block_teleports", false);

        this.cache_time = config.getLong("settings.cache_time", 30L);

        this.login_protection_time = config.getLong("settings.login_protection_time", 0);
        this.teleport_protection_time = config.getLong("settings.teleport_protection_time", 0);

        // Messages
        this.pvp_enabled = config.getString("messages.pvp_enabled", "<red>You enabled PvP!");
        this.pvp_disabled = config.getString("messages.pvp_disabled", "<red>You disabled PvP!");
        this.cannot_attack_victim = config.getString("messages.cannot_attack_victim", "<red>You can't attack players that have PvP turned off!");
        this.cannot_attack_attacker = config.getString("messages.cannot_attack_attacker", "<red>You can't attack players while you have PvP turned off!");
        this.cannot_attack_pets_victim = config.getString("messages.cannot_attack_pets_victim", "<red>You can't attack pets while you have PvP turned off");
        this.cannot_attack_pets_attacker = config.getString("messages.cannot_attack_pets_attacker", "<red>You can't attack pets of players that have PvP turned off");
        this.cannot_attack_mounts_victim = config.getString("messages.cannot_attack_mounts_victim", "<red>You can't attack mounts of players that have PvP turned off");
        this.cannot_attack_mounts_attacker = config.getString("messages.cannot_attack_mounts_attacker", "<red>You can't attack mounts while you have PvP turned off");
        this.cannot_attack_pvp_force_off = config.getString("messages.cannot_attack_pvp_force_off", "<red>PvP is forcibly disabled");

        this.no_permission = config.getString("messages.no_permission", "<red>You don't have permission to use that.");
        this.no_such_command = config.getString("messages.no_such_command", "<red>No such command.");
        this.pvp_enabled_other = config.getString("messages.pvp_enabled_others", "<red>You've enabled %player%'s PvP.");
        this.pvp_disabled_other = config.getString("messages.pvp_disabled_others", "<red>You've disabled %player%'s PvP.");
        this.entering_combat = config.getString("messages.entering_combat", "<red>Entering combat");
        this.leaving_combat = config.getString("messages.leaving_combat", "<red>Leaving combat");
        this.cant_do_that_during_combat = config.getString("messages.cant_do_that_during_combat", "<red>You can't do that while in combat!");
        this.force_pvp_on = config.getString("messages.force_pvp_on", "PvP is now force enabled");
        this.force_pvp_off = config.getString("messages.force_pvp_off", "PvP is now force disabled");
        this.force_pvp_none = config.getString("messages.force_pvp_none", "PvP state is not forced now");

        this.placeholder_combat_time = config.getString("placeholder.placeholder_combat_time", "Combat time: %time%");
        this.placeholder_not_in_combat = config.getString("placeholder.not_in_combat", "Not in combat");

        this.placeholder_pvp_forced_true = config.getString("placeholder.pvp_forced_true", "PvP is forced on");
        this.placeholder_pvp_forced_false = config.getString("placeholder.pvp_forced_false", "PvP is forced off");
        this.placeholder_pvp_forced_none = config.getString("placeholder.pvp_forced_none", "PvP is not forced");

        this.cannot_attack_forced_pvp_off = config.getString("messages.cannot_attack_pvp_force_off", "<red>PvP is forcibly disabled");
        this.cannot_attack_teleport_or_spawn_protection_attacker = config.getString("messages.cannot_attack_teleport_or_spawn_protection_attacker", "<red>You can't attack players while they have teleport or spawn protection");
        this.cannot_attack_pets_teleport_or_spawn_protection_attacker = config.getString("messages.cannot_attack_pets_teleport_or_spawn_protection_attacker", "<red>You can't attack pets while you have teleport or spawn protection");
        this.cannot_attack_mounts_teleport_or_spawn_protection_attacker = config.getString("messages.cannot_attack_mounts_teleport_or_spawn_protection_attacker", "<red>You can't attack mounts while you have teleport or spawn protection");
        this.cannot_attack_teleport_or_spawn_protection_victim = config.getString("messages.cannot_attack_teleport_or_spawn_protection_victim", "<red>You can't attack players while you have teleport or spawn protection");
    }


    public Set<String> getCombatBlockedCommands() {
        return Collections.unmodifiableSet(combatBlockedCommands);
    }


}
