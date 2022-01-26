package me.youhavetrouble.preventstabby.commands;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpToggleCommand {

    public static void toggle(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(PreventStabby.getPlugin(), () -> {
            if (!sender.hasPermission("preventstabby.command.toggle")) {
                String message = ChatColor.translateAlternateColorCodes('&', PreventStabby.getPlugin().getConfigCache().getNo_permission());
                sender.sendMessage(message);
                return;
            }

            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;

                    if (CombatTimer.isInCombat(player.getUniqueId())) {
                        sender.sendMessage(PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                        return;
                    }

                    boolean currentState = PreventStabby.getPlugin().getPlayerManager().togglePlayerPvpState(player.getUniqueId());

                    String message = "";
                    if (currentState) {
                        message = PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getPvp_enabled());
                    } else {
                        message = PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getPvp_disabled());
                    }
                    player.sendMessage(message);
                } else {
                    sender.sendMessage("Try /pvp toggle <player>");
                }
            } else if (args.length == 2) {
                if (!sender.hasPermission("preventstabby.command.toggle.others")) {
                    String message = ChatColor.translateAlternateColorCodes('&', PreventStabby.getPlugin().getConfigCache().getNo_permission());
                    sender.sendMessage(message);
                    return;
                }
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (CombatTimer.isInCombat(player.getUniqueId())) {
                        sender.sendMessage(PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                        return;
                    }
                }
                try {
                    Player player = Bukkit.getPlayer(args[1]);
                    boolean currentState = PreventStabby.getPlugin().getPlayerManager().togglePlayerPvpState(player.getUniqueId());
                    String message;
                    if (currentState) {
                        message = PreventStabby.getPlugin().getConfigCache().getPvp_enabled_other();
                    } else {
                        message = PreventStabby.getPlugin().getConfigCache().getPvp_disabled_other();
                    }
                    sender.sendMessage(PluginMessages.parsePlayerName(player, message));

                } catch (NullPointerException e) {
                    sender.sendMessage(PluginMessages.parseMessage("&cPlayer offline."));
                }
            } else {
                if (sender.hasPermission("preventstabby.command.toggle.others")) {
                    sender.sendMessage("Try /pvp toggle <player>");
                } else {
                    sender.sendMessage("Try /pvp toggle");
                }
            }
        });
    }

    public static void enable(CommandSender sender, String[] args) {
        if (!sender.hasPermission("preventstabby.command.toggle")) {
            String message = ChatColor.translateAlternateColorCodes('&', PreventStabby.getPlugin().getConfigCache().getNo_permission());
            sender.sendMessage(message);
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    sender.sendMessage(PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                    return;
                }
                PreventStabby.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), true);
                String message = PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getPvp_enabled());
                player.sendMessage(message);
            } else {
                sender.sendMessage("Try /pvp enable <player>");
            }
        } else if (args.length == 2) {
            if (!sender.hasPermission("preventstabby.command.toggle.others")) {
                String message = ChatColor.translateAlternateColorCodes('&', PreventStabby.getPlugin().getConfigCache().getNo_permission());
                sender.sendMessage(message);
                return;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    sender.sendMessage(PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                    return;
                }
            }
            try {
                Player player = Bukkit.getPlayer(args[1]);
                String message = PreventStabby.getPlugin().getConfigCache().getPvp_enabled_other();
                sender.sendMessage(PluginMessages.parsePlayerName(player, message));
                PreventStabby.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), true);
            } catch (NullPointerException e) {
                sender.sendMessage(PluginMessages.parseMessage("&cPlayer offline."));
            }
        } else {
            if (sender.hasPermission("preventstabby.command.toggle.others")) {
                sender.sendMessage("Try /pvp enable <player>");
            } else {
                sender.sendMessage("Try /pvp enable");
            }
        }
    }

    public static void disable(CommandSender sender, String[] args) {
        if (!sender.hasPermission("preventstabby.command.toggle")) {
            String message = ChatColor.translateAlternateColorCodes('&', PreventStabby.getPlugin().getConfigCache().getNo_permission());
            sender.sendMessage(message);
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    sender.sendMessage(PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                    return;
                }
                PreventStabby.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), false);
                String message = PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getPvp_disabled());
                player.sendMessage(message);
            } else {
                sender.sendMessage("Try /pvp disable <player>");
            }
        } else if (args.length == 2) {
            if (!sender.hasPermission("preventstabby.command.toggle.others")) {
                String message = PreventStabby.getPlugin().getConfigCache().getNo_permission();
                sender.sendMessage(message);
                return;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    sender.sendMessage(PluginMessages.parseMessage(PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat()));
                    return;
                }
            }
            try {
                Player player = Bukkit.getPlayer(args[1]);
                String message = PreventStabby.getPlugin().getConfigCache().getPvp_disabled_other();
                sender.sendMessage(PluginMessages.parsePlayerName(player, message));
                PreventStabby.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), true);
            } catch (NullPointerException e) {
                sender.sendMessage(PluginMessages.parseMessage("&cPlayer offline."));
            }
        } else {
            if (sender.hasPermission("preventstabby.command.toggle.others")) {
                sender.sendMessage("Try /pvp disable <player>");
            } else {
                sender.sendMessage("Try /pvp disable");
            }
        }
    }

}
