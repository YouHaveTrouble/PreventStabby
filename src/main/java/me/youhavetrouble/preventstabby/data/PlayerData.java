package me.youhavetrouble.preventstabby.data;

import me.youhavetrouble.preventstabby.PreventStabby;

import java.util.UUID;

/**
 * PreventStabby player data keeper.
 */
public class PlayerData {

    private final UUID playerUuid;
    private long lastAccessTimestamp;
    private Long combatStartTimestamp, teleportTimestamp, loginTimestamp;
    private boolean pvpEnabled, lastCombatCheck;

    public PlayerData(UUID playerUuid, boolean pvpEnabled) {
        this.playerUuid = playerUuid;
        this.pvpEnabled = pvpEnabled;
        this.lastCombatCheck = false;
        this.combatStartTimestamp = null;
        this.loginTimestamp = null;
        this.teleportTimestamp = null;
        refreshCacheTime();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    /**
     * Returns true if player has personal pvp enabled, false otherwise.
     * @return True if player has personal pvp enabled, false otherwise.
     */
    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    /**
     * Sets player's personal pvp state.
     * @param pvpEnabled Pvp state to set.
     */
    protected void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    /**
     * Returns player's last access timestamp.
     * @return Player's last access timestamp.
     */
    protected long getLastAccessTimestamp() {
        return lastAccessTimestamp;
    }

    protected boolean isCacheExpired() {
        return System.currentTimeMillis() - lastAccessTimestamp > 60000;
    }

    /**
     * Refreshes the last access to any of the fields
     */
    protected void refreshCacheTime() {
        this.lastAccessTimestamp = System.currentTimeMillis();
    }

    /**
     * Retrieves the timestamp when combat started.
     * @return The timestamp when combat started.
     */
    protected long getCombatStartTimestamp() {
        refreshCacheTime();
        return combatStartTimestamp;
    }

    protected boolean getLastCombatCheckState() {
        return lastCombatCheck;
    }

    protected void setLastCombatCheckState(boolean state) {
        this.lastCombatCheck = state;
    }

    /**
     * Marks the player as in combat.
     */
    public void markInCombat() {
        refreshCacheTime();
        if (PreventStabby.getPlugin().getConfigCache().combat_time <= 0) return;
        this.combatStartTimestamp = System.currentTimeMillis();
    }

    protected void markNotInCombat() {
        refreshCacheTime();
        this.combatStartTimestamp = null;
    }

    /**
     * Sets the login timestamp for the player.
     * @param loginTimestamp The login timestamp to set.
     */
    protected void setLoginTimestamp(long loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    /**
     * Retrieves the login timestamp for the player.
     * @return The login timestamp for the player.
     */
    public Long getLoginTimestamp() {
        return loginTimestamp;
    }

    /**
     * Sets the timestamp of the player's teleport.
     * @param teleportTimestamp The timestamp of the player's teleport.
     */
    public void setTeleportTimestamp(long teleportTimestamp) {
        this.teleportTimestamp = teleportTimestamp;
    }

    /**
     * Checks if the player is currently in combat.
     *
     * @return true if the player is in combat, false otherwise.
     */
    public boolean isInCombat() {
        if (combatStartTimestamp == null) return false;
        return System.currentTimeMillis() - (combatStartTimestamp + (PreventStabby.getPlugin().getConfigCache().combat_time * 1000)) < 0;
    }

    /**
     * Retrieves the number of seconds left until combat ends for the player.
     * @return The number of seconds left until combat ends. -1 if not in combat
     */
    public long getSecondsLeftUntilCombatEnd() {
        if (combatStartTimestamp == null) return -1;
        long timeSinceCombatStart = System.currentTimeMillis() - combatStartTimestamp;
        long combatTimeConfigured = (long) (PreventStabby.getPlugin().getConfigCache().combat_time * 1000);
        return (timeSinceCombatStart < combatTimeConfigured) ? (combatTimeConfigured - timeSinceCombatStart) / 1000 : -1;
    }

    /**
     * Checks if the player has login protection.
     *
     * @return true if the player has login protection, false otherwise.
     */
    public boolean hasLoginProtection() {
        if (loginTimestamp == null) return false;
        return System.currentTimeMillis() - (loginTimestamp + (PreventStabby.getPlugin().getConfigCache().login_protection_time * 1000)) < 0;
    }

    public boolean hasTeleportProtection() {
        if (teleportTimestamp == null) return false;
        return System.currentTimeMillis() - (teleportTimestamp + (PreventStabby.getPlugin().getConfigCache().teleport_protection_time * 1000)) < 0;
    }

    /**
     * Returns true if the player has any form of protection enabled, including login and teleport protection.
     *
     * @return true if the player has protection enabled, false otherwise.
     */
    public boolean isProtected() {
        return  hasLoginProtection() || hasTeleportProtection();
    }
}
