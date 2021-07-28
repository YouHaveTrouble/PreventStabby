package me.youhavetrouble.preventstabby.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * NOT IMPLEMENTED YET
 */
@Deprecated
public class PlayerTogglePvpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private boolean newState, sendMessage;

    public PlayerTogglePvpEvent(Player player, boolean newState, boolean sendMessage) {
        this.player = player;
        this.newState = newState;
        this.sendMessage = sendMessage;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean newState() {
        return newState;
    }

    public void setNewState(boolean newState) {
        this.newState = newState;
    }

    public boolean isSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(boolean sendMessage) {
        this.sendMessage = sendMessage;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
