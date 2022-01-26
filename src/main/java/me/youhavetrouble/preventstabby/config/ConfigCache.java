package me.youhavetrouble.preventstabby.config;

import me.youhavetrouble.preventstabby.PreventStabby;
import io.github.thatsmusic99.configurationmaster.CMFile;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigCache {

    private final PreventStabby plugin = PreventStabby.getPlugin();

    @Getter private final boolean pvp_enabled_by_default, lava_and_fire_stopper_enabled, channeling_enchant_disabled,
            punish_for_combat_logout, punish_for_combat_logout_announce, only_owner_can_interact_with_pet,
            snowballs_knockback, egg_knockback, block_commands_in_combat, block_teleports_in_combat;
    @Getter private final String pvp_enabled, pvp_disabled, cannot_attack_victim, cannot_attack_attacker,
            cannot_attack_pets_victim, cannot_attack_pets_attacker, no_permission, no_such_command, pvp_enabled_other,
            pvp_disabled_other, punish_for_combat_logout_message, entering_combat, leaving_combat,
            cant_do_that_during_combat, cannot_attack_mounts_attacker, cannot_attack_mounts_victim;
    @Getter private final double lava_and_fire_stopper_radius;
    @Getter private final long cache_time, combat_time, login_protection_time, teleport_protection_time;
    @Getter private final Set<String> combatBlockedCommands = new HashSet<>();

    public ConfigCache() {

        CMFile configFile = new CMFile(plugin, "config") {
            @Override
            public void loadDefaults() {

                addDefault("settings.pvp_enabled_by_default", false, "Decides if pvp should be enabled or disabled by default");

                addComment("settings.lava_and_fire_stopper", "Prevents dumping lava and pufferfish bucket, placing wither roses and lighting blocks on fire near players with pvp off");
                addDefault("settings.lava_and_fire_stopper.enabled", true);
                addDefault("settings.lava_and_fire_stopper.radius", 2.5);

                addDefault("settings.channeling_enchant_disabled", false, "Disables channeling (trident enchant) lightning strike.\nYou may want to keep it disabled because players with pvp off can use it to attack players with pvp on");

                addDefault("settings.only_owner_can_interact_with_pet", false, "Makes it so only pet owner can interact with it. Useful if you don't want people renaming other people's pets.");

                addDefault("settings.combat_time", 25, "Time (in seconds) to keep player in combat");

                addDefault("settings.login_protection_time", 0, "Time (in seconds) that player can't be harmed by other player after logging in");

                addDefault("settings.teleport_protection_time", 0, "Time (in seconds) that player can't be harmed by other player after teleporting");

                addComment("settings.punish_for_combat_logout", "Kill the player if they log out during combat");
                addDefault("settings.punish_for_combat_logout.enabled", true);
                addDefault("settings.punish_for_combat_logout.announce", true);
                addDefault("settings.punish_for_combat_logout.message", "&f%player% logged out while in combat. What a loser.");

                addDefault("settings.snowballs_do_knockback", false, "Set to true if snowballs should cause knockback to players");

                addDefault("settings.eggs_do_knockback", false, "Set to true if eggs should cause knockback to players");

                addComment("settings.block_in_combat", "Set what actions should be blocked while in combat");
                addDefault("settings.block_in_combat.block_commands.enabled", true);
                List<String> defaultCommandsBlocked = new ArrayList<>();
                defaultCommandsBlocked.add("spawn");
                defaultCommandsBlocked.add("tpa");
                defaultCommandsBlocked.add("home");
                addDefault("settings.block_in_combat.block_commands.commands", defaultCommandsBlocked);
                addDefault("settings.block_in_combat.block_teleports", true, "Set if players should be denied teleportation attempts while in combat");

                addDefault("settings.cache_time", 30, "Time (in seconds) to keep player data in memory when players goes offline.\nThis is used for checking if offline players pvp state.\nAdjust only if you know what you're doing. NEVER set to less than 6.");

                addDefault("messages.pvp_enabled", "&cYou enabled PvP!");
                addDefault("messages.pvp_disabled", "&cYou disabled PvP!");
                addDefault("messages.cannot_attack_victim", "&cYou can't attack players that have PvP turned off!");
                addDefault("messages.cannot_attack_attacker", "&cYou can't attack players while you have PvP turned off!");
                addDefault("messages.cannot_attack_pets_victim", "&cYou can't attack pets of players that have PvP turned off");
                addDefault("messages.cannot_attack_pets_attacker", "&cYou can't attack pets while you have PvP turned off");
                addDefault("messages.cannot_attack_mounts_victim", "&cYou can't attack mounts of players that have PvP turned off");
                addDefault("messages.cannot_attack_mounts_attacker", "&cYou can't attack mounts while you have PvP turned off");
                addDefault("messages.no_permission", "&cYou don't have permission to use that.");
                addDefault("messages.no_such_command", "&cNo such command.");
                addDefault("messages.pvp_enabled_others", "&cYou've enabled %player%'s PvP.");
                addDefault("messages.pvp_disabled_others", "&cYou've disabled %player%'s PvP.");
                addDefault("messages.entering_combat", "&cEntering combat");
                addDefault("messages.leaving_combat", "&cLeaving combat");
                addDefault("messages.cant_do_that_during_combat", "&cYou can't do that while in combat!");
            }
        };

        configFile.setDescription("Prevent people from getting stabbed!");
        configFile.addLink("Spigot", "https://www.spigotmc.org/resources/89376/");
        configFile.addLink("Source", "https://github.com/YouHaveTrouble/PreventStabby");

        configFile.load();
        FileConfiguration config = configFile.getConfig();

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

        this.snowballs_knockback = config.getBoolean("settings.snowballs_do_knockback", false);
        this.egg_knockback = config.getBoolean("settings.eggs_do_knockback", false);
        this.block_commands_in_combat = config.getBoolean("settings.block_in_combat.block_commands", true);
        if (block_commands_in_combat) {
            this.combatBlockedCommands.addAll(config.getStringList("settings.block_in_combat.block_commands.commands"));
        }
        this.block_teleports_in_combat = config.getBoolean("settings.block_in_combat.block_teleports", true);

        this.cache_time = config.getLong("settings.cache_time", 30L);

        this.login_protection_time = config.getLong("settings.login_protection_time", 0);
        this.teleport_protection_time = config.getLong("settings.teleport_protection_time", 0);

        // Messages
        this.pvp_enabled = config.getString("messages.pvp_enabled", "&cYou enabled PvP!");
        this.pvp_disabled = config.getString("messages.pvp_disabled", "&cYou disabled PvP!");
        this.cannot_attack_victim = config.getString("messages.cannot_attack_victim", "&cYou can't attack players that have PvP turned off!");
        this.cannot_attack_attacker = config.getString("messages.cannot_attack_attacker", "&cYou can't attack players while you have PvP turned off!");
        this.cannot_attack_pets_victim = config.getString("messages.cannot_attack_pets_victim", "&cYou can't attack pets while you have PvP turned off");
        this.cannot_attack_pets_attacker = config.getString("messages.cannot_attack_pets_attacker", "&cYou can't attack pets of players that have PvP turned off");
        this.cannot_attack_mounts_victim = config.getString("messages.cannot_attack_mounts_victim", "&cYou can't attack mounts of players that have PvP turned off");
        this.cannot_attack_mounts_attacker = config.getString("messages.cannot_attack_mounts_attacker", "&cYou can't attack mounts while you have PvP turned off");
        this.no_permission = config.getString("messages.no_permission", "&cYou don't have permission to use that.");
        this.no_such_command = config.getString("messages.no_such_command", "&cNo such command.");
        this.pvp_enabled_other = config.getString("messages.pvp_enabled_others", "&cYou've enabled %player%'s PvP.");
        this.pvp_disabled_other = config.getString("messages.pvp_disabled_others", "&cYou've disabled %player%'s PvP.");
        this.entering_combat = config.getString("messages.entering_combat", "&cEntering combat");
        this.leaving_combat = config.getString("messages.leaving_combat", "&cLeaving combat");
        this.cant_do_that_during_combat = config.getString("messages.cant_do_that_during_combat", "&cYou can't do that while in combat!");
    }


}
