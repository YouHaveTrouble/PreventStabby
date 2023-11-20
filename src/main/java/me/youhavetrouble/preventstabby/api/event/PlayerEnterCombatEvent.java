package me.youhavetrouble.preventstabby.api.event;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when player enters combat.
 */
public class PlayerEnterCombatEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private boolean cancelled;

    public PlayerEnterCombatEvent(Player player) {
        this.player = player;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerData getPlayerData() {
        return PreventStabby.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
