package me.youhavetrouble.preventstabby.players;

import me.youhavetrouble.preventstabby.PreventStabby;

import java.time.Instant;
import java.util.UUID;

/**
 * PreventStabby player data keeper.<br>
 */
public class PlayerData {

    private final UUID playerUuid;
    private long cachetime, combattime, loginTimestamp, teleportTimestamp;
    private boolean pvpEnabled, lastCombatCheck, inCombat;

    public PlayerData(UUID playerUuid, boolean pvpEnabled) {
        this.playerUuid = playerUuid;
        this.pvpEnabled = pvpEnabled;
        this.combattime = Instant.now().getEpochSecond()-1;
        this.loginTimestamp = Instant.now().getEpochSecond()-1;
        this.teleportTimestamp = Instant.now().getEpochSecond()-1;
        this.inCombat = false;
        refreshCacheTime();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    /**
     * Returns true if player has personal pvp enabled, false otherwise.
     * @return True if player has personal pvp enabled, false otherwise.
     * @see PlayerManager#getPlayerPvPState(UUID) 
     */
    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    /**
     * Sets player's personal pvp state.
     * @param pvpEnabled Pvp state to set.
     */
    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    protected long getCachetime() {
        return cachetime;
    }

    protected void refreshCacheTime() {
        this.cachetime = Instant.now().getEpochSecond() + PreventStabby.getPlugin().getConfigCache().getCache_time();
    }

    /**
     * Time left until the end of combat in seconds.
     * @return Time left until the end of combat in seconds.<br>
     * Return of 0 means out of combat or about to be out of combat.
     */
    public long getCombatTime() {
        return Math.max(combattime - Instant.now().getEpochSecond(), 0);
    }
    
    protected void setCombattime(long combattime) {
        this.combattime = combattime;
    }

    /**
     * Sets player in combat and sets combat time to the interval set in config.
     * @see PlayerManager#refreshPlayersCombatTime(UUID)
     */
    public void refreshCombatTime() {
        this.combattime = Instant.now().getEpochSecond()+ PreventStabby.getPlugin().getConfigCache().getCombat_time();
    }

    protected boolean getLastCombatCheck() {
        return lastCombatCheck;
    }

    protected void setLastCombatCheck(boolean bool) {
        lastCombatCheck = bool;
    }

    protected void setLoginTimestamp(long loginTimestamp) {
        this.loginTimestamp = loginTimestamp + PreventStabby.getPlugin().getConfigCache().getLogin_protection_time()-1;
    }

    protected long getLoginTimestamp() {
        return loginTimestamp;
    }

    protected void setTeleportTimestamp(long teleportTimestamp) {
        this.teleportTimestamp = teleportTimestamp + PreventStabby.getPlugin().getConfigCache().getTeleport_protection_time()-1;
    }

    protected long getTeleportTimestamp() {
        return teleportTimestamp;
    }

    /**
     * Returns player's current combat state.
     * @return Player's current combat state.
     */
    public boolean isInCombat() {
        return inCombat;
    }

    protected void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }
}
