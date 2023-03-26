package me.youhavetrouble.preventstabby.api.event;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.players.PlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when player gets their personal pvp state toggled.
 */
public class PlayerTogglePvpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final OfflinePlayer player;
    private final boolean newState;
    private boolean sendMessage;

    public PlayerTogglePvpEvent(OfflinePlayer player, boolean newState, boolean sendMessage) {
        this.player = player;
        this.newState = newState;
        this.sendMessage = sendMessage;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public PlayerData getPlayerData() {
        return PreventStabby.getPlugin().getPlayerManager().getPlayer(player.getUniqueId());
    }

    /**
     * Returns the state player's pvp state was toggled to.
     * @return The state player's pvp state was toggled to.
     */
    public boolean newState() {
        return newState;
    }

    /**
     * Returns true if the state message will be sent to the player, false otherwise
     * @return True if the state message will be sent to the player, false otherwise
     */
    public boolean isSendMessage() {
        return sendMessage;
    }

    /**
     * If this is true at the end of event pipeline, message with the current state will be sent to player.
     */
    public void setSendMessage(boolean sendMessage) {
        this.sendMessage = sendMessage;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
