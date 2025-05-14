package me.youhavetrouble.preventstabby.config;

import me.youhavetrouble.preventstabby.PreventStabby;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigCache {

    public final boolean pvp_enabled_by_default,
            bucket_stopper_enabled,
            fire_stopper_enabled,
            block_stopper_enabled,
            punish_for_combat_logout_kill,
            punish_for_combat_logout_announce,
            block_teleports_in_combat,
            allow_fishing_rod_pull;
    public final String pvp_enabled, pvp_disabled, cannot_attack_victim, cannot_attack_attacker,
            cannot_attack_pets_victim, cannot_attack_pets_attacker, no_permission, no_such_command, pvp_enabled_other,
            pvp_disabled_other, punish_for_combat_logout_message, entering_combat, leaving_combat,
            cant_do_that_during_combat, cannot_attack_mounts_attacker, cannot_attack_mounts_victim, force_pvp_on,
            force_pvp_off, force_pvp_none, placeholder_combat_time, placeholder_not_in_combat, cannot_attack_pvp_force_off,
            placeholder_pvp_forced_true, placeholder_pvp_forced_false, placeholder_pvp_forced_none;

    public final String cannot_attack_forced_pvp_off, cannot_attack_teleport_or_spawn_protection_attacker,
            cannot_attack_pets_teleport_or_spawn_protection_attacker, cannot_attack_mounts_teleport_or_spawn_protection_attacker,
            cannot_attack_teleport_or_spawn_protection_victim;

    public final double combat_time, login_protection_time, teleport_protection_time, bucket_stopper_radius,
    fire_stopper_radius, block_stopper_radius;
    private final Set<String> combatBlockedCommands = new HashSet<>();
    private final Set<Material> dangerousBlocks = new HashSet<>();

    private final FileConfiguration config;

    public ConfigCache(PreventStabby plugin) {
        plugin.reloadConfig();
        config = plugin.getConfig();

        migrate("settings.punish_for_combat_logout.enabled",
                "settings.punish_for_combat_logout.kill",
                true);

        // Settings
        this.pvp_enabled_by_default = getBoolean(
                "settings.pvp_enabled_by_default",
                false,
                List.of("Should pvp be enabled by default when the player first joins?")
        );

        this.combat_time = getDouble(
                "settings.combat_time",
                25,
                List.of("How long in seconds should combat last since the last hit")
        );

        this.punish_for_combat_logout_kill = getBoolean(
                "settings.punish_for_combat_logout.kill",
                true,
                List.of("Should players be killed if they log out during combat?")
        );
        this.punish_for_combat_logout_announce = getBoolean(
                "settings.punish_for_combat_logout.announce",
                true,
                List.of("Should we announce that player logged out of combat?")
        );

        List<String> commandsBlockedInCombat = getList(
                "settings.block_in_combat.commands",
                List.of("spawn", "tpa", "home"),
                List.of("Commands to block when player is in combat")
        );
        for (String command : commandsBlockedInCombat) {
            this.combatBlockedCommands.add(command.toLowerCase(Locale.ENGLISH));
        }

        this.block_teleports_in_combat = getBoolean(
                "settings.block_in_combat.teleports",
                false,
                List.of("Block teleportation triggered by plugins while in combat")
        );

        this.login_protection_time = getDouble(
                "settings.login_protection_time",
                0,
                List.of("Protection time after player logs in in seconds")
        );
        this.teleport_protection_time = getDouble(
                "settings.teleport_protection_time",
                0,
                List.of("Protection time after player is teleported in seconds")
        );

        this.allow_fishing_rod_pull = getBoolean(
                "settings.allow_fishing_rod_pull",
                false,
                List.of("Should fishing rod pulling be allowed regardless of players pvp state?")
        );
        this.bucket_stopper_enabled = getBoolean(
                "settings.environmental.bucket_stopper.enabled",
                true,
                List.of("Should plugin block dumping buckets with dangers near players with pvp off?")
        );
        this.bucket_stopper_radius = getDouble(
                "settings.environmental.bucket_stopper.radius",
                2.5,
                List.of("Distance from the player where dumping buckets will be disallowed")
        );
        this.fire_stopper_enabled = getBoolean(
                "settings.environmental.fire_stopper.enabled",
                true,
                List.of("Should plugin block igniting blocks near players with pvp off?")
        );
        this.fire_stopper_radius = getDouble(
                "settings.environmental.fire_stopper.radius",
                2.5,
                List.of("Distance from the player where igniting blocks will be disallowed")
        );
        this.block_stopper_enabled = getBoolean(
                "settings.environmental.block_stopper.enabled",
                true,
                List.of("Should plugin block placing dangerous blocks near players with pvp off?")
        );
        this.block_stopper_radius = getDouble(
                "settings.environmental.block_stopper.radius",
                2.5,
                List.of("Distance from the player where placing dangerous blocks will be disallowed")
        );
        List<String> rawDangerousBlocks = getList(
                "settings.environmental.block_stopper.blocks",
                List.of("tnt", "magma_block", "cactus", "campfire"),
                List.of("List of dangerous blocks that will be blocked when placed near players with pvp off")
        );
        for (String block : rawDangerousBlocks) {
            Material material = Material.matchMaterial(block);
            if (material != null) {
                dangerousBlocks.add(material);
            } else {
                plugin.getLogger().warning("Invalid material: " + block);
            }
        }



        // Messages
        this.pvp_enabled = getString("messages.pvp_enabled", "<red>You enabled PvP!");
        this.pvp_disabled = getString("messages.pvp_disabled", "<red>You disabled PvP!");
        this.cannot_attack_victim = getString("messages.cannot_attack_victim", "<red>You can't attack players that have PvP turned off!");
        this.cannot_attack_attacker = getString("messages.cannot_attack_attacker", "<red>You can't attack players while you have PvP turned off!");
        this.cannot_attack_pets_victim = getString("messages.cannot_attack_pets_victim", "<red>You can't attack pets while you have PvP turned off");
        this.cannot_attack_pets_attacker = getString("messages.cannot_attack_pets_attacker", "<red>You can't attack pets of players that have PvP turned off");
        this.cannot_attack_mounts_victim = getString("messages.cannot_attack_mounts_victim", "<red>You can't attack mounts of players that have PvP turned off");
        this.cannot_attack_mounts_attacker = getString("messages.cannot_attack_mounts_attacker", "<red>You can't attack mounts while you have PvP turned off");
        this.cannot_attack_pvp_force_off = getString("messages.cannot_attack_pvp_force_off", "<red>PvP is forcibly disabled");

        this.no_permission = getString("messages.no_permission", "<red>You don't have permission to use that.");
        this.no_such_command = getString("messages.no_such_command", "<red>No such command.");
        this.pvp_enabled_other = getString("messages.pvp_enabled_others", "<red>You've enabled %player%'s PvP.");
        this.pvp_disabled_other = getString("messages.pvp_disabled_others", "<red>You've disabled %player%'s PvP.");
        this.entering_combat = getString("messages.entering_combat", "<red>Entering combat");
        this.leaving_combat = getString("messages.leaving_combat", "<red>Leaving combat");
        this.cant_do_that_during_combat = getString("messages.cant_do_that_during_combat", "<red>You can't do that while in combat!");
        this.force_pvp_on = getString("messages.force_pvp_on", "PvP is now force enabled");
        this.force_pvp_off = getString("messages.force_pvp_off", "PvP is now force disabled");
        this.force_pvp_none = getString("messages.force_pvp_none", "PvP state is not forced now");

        this.punish_for_combat_logout_message = getString("messages.punish_for_combat_logout.message", "%player%<reset><white> logged out while in combat. What a loser.");

        this.placeholder_combat_time = getString("placeholder.placeholder_combat_time", "Combat time: %time%");
        this.placeholder_not_in_combat = getString("placeholder.not_in_combat", "Not in combat");

        this.placeholder_pvp_forced_true = getString("placeholder.pvp_forced_true", "PvP is forced on");
        this.placeholder_pvp_forced_false = getString("placeholder.pvp_forced_false", "PvP is forced off");
        this.placeholder_pvp_forced_none = getString("placeholder.pvp_forced_none", "PvP is not forced");

        this.cannot_attack_forced_pvp_off = getString("messages.cannot_attack_pvp_force_off", "<red>PvP is forcibly disabled");
        this.cannot_attack_teleport_or_spawn_protection_attacker = getString("messages.cannot_attack_teleport_or_spawn_protection_attacker", "<red>You can't attack players while they have teleport or spawn protection");
        this.cannot_attack_pets_teleport_or_spawn_protection_attacker = getString("messages.cannot_attack_pets_teleport_or_spawn_protection_attacker", "<red>You can't attack pets while you have teleport or spawn protection");
        this.cannot_attack_mounts_teleport_or_spawn_protection_attacker = getString("messages.cannot_attack_mounts_teleport_or_spawn_protection_attacker", "<red>You can't attack mounts while you have teleport or spawn protection");
        this.cannot_attack_teleport_or_spawn_protection_victim = getString("messages.cannot_attack_teleport_or_spawn_protection_victim", "<red>You can't attack players while you have teleport or spawn protection");
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save configuration file! - " + e.getLocalizedMessage());
        }
    }


    public Set<String> getCombatBlockedCommands() {
        return Collections.unmodifiableSet(combatBlockedCommands);
    }

    public Set<Material> getDangerousBlocks() {
        return Collections.unmodifiableSet(dangerousBlocks);
    }

    private String getString(String path, @NotNull String def) {
        return getString(path, def, null);
    }

    private String getString(String path, @NotNull String def, @Nullable List<String> comments) {
        if (config.isSet(path)) return config.getString(path, def);
        config.set(path, def);
        if (comments != null) config.setComments(path, comments);
        return def;
    }

    private boolean getBoolean(String path, boolean def, @Nullable List<String> comments) {
        if (config.isSet(path)) return config.getBoolean(path, def);
        config.set(path, def);
        if (comments != null) config.setComments(path, comments);
        return def;
    }

    private double getDouble(String path, double def, @Nullable List<String> comments) {
        if (config.isSet(path)) return config.getDouble(path, def);
        config.set(path, def);
        if (comments != null) config.setComments(path, comments);
        return def;
    }

    private long getLong(String path, long def, @Nullable List<String> comments) {
        if (config.isSet(path)) return config.getLong(path, def);
        config.set(path, def);
        if (comments != null) config.setComments(path, comments);
        return def;
    }

    private List<String> getList(String path, List<String> def, @Nullable List<String> comments) {
        if (config.isSet(path)) return config.getStringList(path);
        config.set(path, def);
        if (comments != null) config.setComments(path, comments);
        return def;
    }

    private List<String> getList(String path, List<String> def) {
        return getList(path, def, null);
    }

    private void migrate(String oldPath, String newPath, @Nullable Object defaultValue) {
        if (config.isSet(oldPath) && !config.isSet(newPath)) {
            Object value = config.get(oldPath);
            config.set(newPath, value != null ? value : defaultValue);
            config.set(oldPath, null);
        }
    }
}
