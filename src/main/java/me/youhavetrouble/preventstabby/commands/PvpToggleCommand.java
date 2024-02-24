package me.youhavetrouble.preventstabby.commands;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.api.event.PlayerTogglePvpEvent;
import me.youhavetrouble.preventstabby.config.PreventStabbyPermission;
import me.youhavetrouble.preventstabby.data.PlayerManager;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpToggleCommand {

    private static final PlayerManager playerManager = PreventStabby.getPlugin().getPlayerManager();

    public static void toggle(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(PreventStabby.getPlugin(), () -> {
            if (!PreventStabbyPermission.COMMAND_TOGGLE.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().no_permission);
                return;
            }

            if (args.length <= 1) {
                if (!(sender instanceof Player player)) {
                    PluginMessages.sendMessage(sender, "Try /pvp toggle <player>");
                    return;
                }
                if (playerManager.getPlayer(player.getUniqueId()).isInCombat()) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().cant_do_that_during_combat);
                    return;
                }
                playerManager.togglePlayerPvpState(player.getUniqueId()).thenAccept(newState -> {
                    PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, newState, true);
                    Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                        if (PlayerTogglePvpEvent.getHandlerList().getRegisteredListeners().length > 0) {
                            Bukkit.getPluginManager().callEvent(toggleEvent);
                        }
                        if (!toggleEvent.isSendMessage()) return;
                        if (newState) {
                            PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().pvp_enabled);
                        } else {
                            PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().pvp_disabled);
                        }
                    });
                });

            } else if (args.length == 2) {
                if (!PreventStabbyPermission.COMMAND_TOGGLE_OTHERS.doesCommandSenderHave(sender)) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().no_permission);
                    return;
                }
                if (sender instanceof Player player) {
                    if (playerManager.getPlayer(player.getUniqueId()).isInCombat()) {
                        PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().cant_do_that_during_combat);
                        return;
                    }
                }

                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    PluginMessages.sendMessage(sender, "<red>Player offline.");
                    return;
                }
                playerManager.togglePlayerPvpState(player.getUniqueId()).thenAccept(newState -> {
                    PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, newState, false);
                    Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                        if (PlayerTogglePvpEvent.getHandlerList().getRegisteredListeners().length > 0) {
                            Bukkit.getPluginManager().callEvent(toggleEvent);
                        }
                        if (toggleEvent.isSendMessage()) {
                            if (newState) {
                                PluginMessages.sendMessage(player, PreventStabby.getPlugin().getConfigCache().pvp_enabled);
                            } else {
                                PluginMessages.sendMessage(player, PreventStabby.getPlugin().getConfigCache().pvp_disabled);
                            }
                        }
                        String message;
                        if (newState) {
                            message = PreventStabby.getPlugin().getConfigCache().pvp_enabled_other;
                        } else {
                            message = PreventStabby.getPlugin().getConfigCache().pvp_disabled_other;
                        }
                        PluginMessages.sendMessage(sender, PluginMessages.parsePlayerName(player, message));
                    });
                });
            }
        });
    }

    public static void enable(CommandSender sender, String[] args) {
        if (!PreventStabbyPermission.COMMAND_TOGGLE.doesCommandSenderHave(sender)) {
            PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().no_permission);
            return;
        }
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                PluginMessages.sendMessage(sender, "Try /pvp enable <player>");
                return;
            }
            if (playerManager.getPlayer(player.getUniqueId()).isInCombat()) {
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().cant_do_that_during_combat);
                return;
            }
            playerManager.setPlayerPvpState(player.getUniqueId(), true);
            PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, true, true);
            Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                if (PlayerTogglePvpEvent.getHandlerList().getRegisteredListeners().length > 0) {
                    Bukkit.getPluginManager().callEvent(toggleEvent);
                }
                if (toggleEvent.isSendMessage()) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().pvp_enabled);
                }
            });

        } else if (args.length == 2) {
            if (!PreventStabbyPermission.COMMAND_TOGGLE_OTHERS.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().no_permission);
                return;
            }
            if (sender instanceof Player player) {
                if (playerManager.getPlayer(player.getUniqueId()).isInCombat()) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().cant_do_that_during_combat);
                    return;
                }
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                PluginMessages.sendMessage(sender, "<red>Player offline.");
                return;
            }
            String message = PreventStabby.getPlugin().getConfigCache().pvp_enabled_other;
            PluginMessages.sendMessage(sender, PluginMessages.parsePlayerName(player, message));
            playerManager.setPlayerPvpState(player.getUniqueId(), true);
            PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, true, false);
            Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                if (PlayerTogglePvpEvent.getHandlerList().getRegisteredListeners().length > 0) {
                    Bukkit.getPluginManager().callEvent(toggleEvent);
                }
                if (toggleEvent.isSendMessage()) {
                    PluginMessages.sendMessage(player, PreventStabby.getPlugin().getConfigCache().pvp_enabled);
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
            PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().no_permission);
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player player) {
                if (playerManager.getPlayer(player.getUniqueId()).isInCombat()) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().cant_do_that_during_combat);
                    return;
                }
                playerManager.setPlayerPvpState(player.getUniqueId(), false);
                PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, false, true);
                Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                    if (PlayerTogglePvpEvent.getHandlerList().getRegisteredListeners().length > 0) {
                        Bukkit.getPluginManager().callEvent(toggleEvent);
                    }
                    if (toggleEvent.isSendMessage()) {
                        PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().pvp_disabled);
                    }
                });
            } else {
                PluginMessages.sendMessage(sender, "Try /pvp disable <player>");
            }
        } else if (args.length == 2) {
            if (!PreventStabbyPermission.COMMAND_TOGGLE_OTHERS.doesCommandSenderHave(sender)) {
                PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().no_permission);
                return;
            }
            if (sender instanceof Player player) {
                if (playerManager.getPlayer(player.getUniqueId()).isInCombat()) {
                    PluginMessages.sendMessage(sender, PreventStabby.getPlugin().getConfigCache().cant_do_that_during_combat);
                    return;
                }
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                PluginMessages.sendMessage(sender, "<red>Player offline.");
                return;
            }
            String message = PreventStabby.getPlugin().getConfigCache().pvp_disabled_other;
            PluginMessages.sendMessage(sender, PluginMessages.parsePlayerName(player, message));
            playerManager.setPlayerPvpState(player.getUniqueId(), false);
            PlayerTogglePvpEvent toggleEvent = new PlayerTogglePvpEvent(player, false, false);
            Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                if (PlayerTogglePvpEvent.getHandlerList().getRegisteredListeners().length > 0) {
                    Bukkit.getPluginManager().callEvent(toggleEvent);
                }
                if (toggleEvent.isSendMessage()) {
                    PluginMessages.sendMessage(player, PreventStabby.getPlugin().getConfigCache().pvp_disabled);
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
