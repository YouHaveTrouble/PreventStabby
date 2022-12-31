package me.youhavetrouble.preventstabby.commands;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.api.event.PlayerTogglePvpEvent;
import me.youhavetrouble.preventstabby.config.PreventStabbyPermission;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpToggleCommand {

    public static void toggle(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(PreventStabby.getPlugin(), () -> {
            if (!PreventStabbyPermission.COMMAND_TOGGLE.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getNo_permission());
                return;
            }

            if (args.length <= 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (CombatTimer.isInCombat(player.getUniqueId())) {
                        PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat());
                        return;
                    }
                    boolean currentState = PreventStabby.getPlugin().getPlayerManager().togglePlayerPvpState(player.getUniqueId());
                    PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, currentState, true);
                    Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                        Bukkit.getPluginManager().callEvent(toggleEvent);
                        if (toggleEvent.isSendMessage()) {
                            if (currentState) {
                                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getPvp_enabled());
                            } else {
                                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getPvp_disabled());
                            }
                        }
                    });
                } else {
                    PluginMessages.sendMessage(sender, "Try /pvp toggle <player>");
                }
            } else if (args.length == 2) {
                if (!PreventStabbyPermission.COMMAND_TOGGLE_OTHERS.doesCommandSenderHave(sender)) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getNo_permission());
                    return;
                }
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (CombatTimer.isInCombat(player.getUniqueId())) {
                        PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat());
                        return;
                    }
                }

                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    PluginMessages.sendMessage(sender, "<red>Player offline.");
                    return;
                }
                boolean currentState = PreventStabby.getPlugin().getPlayerManager().togglePlayerPvpState(player.getUniqueId());
                PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, currentState, false);
                Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                    Bukkit.getPluginManager().callEvent(toggleEvent);
                    if (toggleEvent.isSendMessage()) {
                        if (currentState) {
                            PluginMessages.sendMessage(player, PreventStabby.getPlugin().getConfigCache().getPvp_enabled());
                        } else {
                            PluginMessages.sendMessage(player, PreventStabby.getPlugin().getConfigCache().getPvp_disabled());
                        }
                    }
                });

                String message;
                if (currentState) {
                    message = PreventStabby.getPlugin().getConfigCache().getPvp_enabled_other();
                } else {
                    message = PreventStabby.getPlugin().getConfigCache().getPvp_disabled_other();
                }
                PluginMessages.sendMessage(sender, PluginMessages.parsePlayerName(player, message));
            }
        });
    }

    public static void enable(CommandSender sender, String[] args) {
        if (!PreventStabbyPermission.COMMAND_TOGGLE.doesCommandSenderHave(sender)) {
            PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getNo_permission());
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat());
                    return;
                }
                PreventStabby.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), true);
                PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, true, true);
                Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                    Bukkit.getPluginManager().callEvent(toggleEvent);
                    if (toggleEvent.isSendMessage()) {
                        PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getPvp_enabled());
                    }
                });
            } else {
                PluginMessages.sendMessage(sender, "Try /pvp enable <player>");
            }
        } else if (args.length == 2) {
            if (!PreventStabbyPermission.COMMAND_TOGGLE_OTHERS.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getNo_permission());
                return;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat());
                    return;
                }
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                PluginMessages.sendMessage(sender, "<red>Player offline.");
                return;
            }
            String message = PreventStabby.getPlugin().getConfigCache().getPvp_enabled_other();
            PluginMessages.sendMessage(sender, PluginMessages.parsePlayerName(player, message));
            PreventStabby.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), true);
            PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, true, false);
            Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                Bukkit.getPluginManager().callEvent(toggleEvent);
                if (toggleEvent.isSendMessage()) {
                    PluginMessages.sendMessage(player, PreventStabby.getPlugin().getConfigCache().getPvp_enabled());
                }
            });
        } else {
            if (PreventStabbyPermission.COMMAND_TOGGLE_OTHERS.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, "Try /pvp enable <player>");
            } else {
                PluginMessages.sendMessage(sender, "Try /pvp enable");
            }
        }
    }

    public static void disable(CommandSender sender, String[] args) {
        if (!PreventStabbyPermission.COMMAND_TOGGLE.doesCommandSenderHave(sender)) {
            PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getNo_permission());
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat());
                    return;
                }
                PreventStabby.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), false);
                PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, false, true);
                Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                    Bukkit.getPluginManager().callEvent(toggleEvent);
                    if (toggleEvent.isSendMessage()) {
                        PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getPvp_disabled());
                    }
                });
            } else {
                PluginMessages.sendMessage(sender, "Try /pvp disable <player>");
            }
        } else if (args.length == 2) {
            if (!PreventStabbyPermission.COMMAND_TOGGLE_OTHERS.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getNo_permission());
                return;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (CombatTimer.isInCombat(player.getUniqueId())) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().getCant_do_that_during_combat());
                    return;
                }
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                PluginMessages.sendMessage(sender, "<red>Player offline.");
                return;
            }
            String message = PreventStabby.getPlugin().getConfigCache().getPvp_disabled_other();
            PluginMessages.sendMessage(sender, PluginMessages.parsePlayerName(player, message));
            PreventStabby.getPlugin().getPlayerManager().setPlayerPvpState(player.getUniqueId(), false);
            PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, false, false);
            Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                Bukkit.getPluginManager().callEvent(toggleEvent);
                if (toggleEvent.isSendMessage()) {
                    PluginMessages.sendMessage(player, PreventStabby.getPlugin().getConfigCache().getPvp_disabled());
                }
            });

        } else {
            if (PreventStabbyPermission.COMMAND_TOGGLE_OTHERS.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, "Try /pvp disable <player>");
            } else {
                PluginMessages.sendMessage(sender, "Try /pvp disable");
            }
        }
    }

}
