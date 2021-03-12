package eu.endermite.togglepvp.players;

import eu.endermite.togglepvp.TogglePvp;

import java.time.Instant;

public class PlayerData {

    private long cachetime, combattime, loginTimestamp, teleportTimestamp;
    private boolean pvpEnabled, lastCombatCheck, inCombat;

    public PlayerData(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
        this.combattime = Instant.now().getEpochSecond()-1;
        this.loginTimestamp = Instant.now().getEpochSecond()-1;
        this.teleportTimestamp = Instant.now().getEpochSecond()-1;
        this.inCombat = false;
        refreshCachetime();
    }

    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    public long getCachetime() {
        return cachetime;
    }

    public void refreshCachetime() {
        this.cachetime = Instant.now().getEpochSecond()+ TogglePvp.getPlugin().getConfigCache().getCache_time();
    }

    public long getCombattime() {
        return combattime;
    }

    public void setCombattime(long combattime) {
        this.combattime = combattime;
    }

    public void refreshCombatTime() {
        this.combattime = Instant.now().getEpochSecond()+ TogglePvp.getPlugin().getConfigCache().getCombat_time();
    }

    public boolean getLastCombatCheck() {
        return lastCombatCheck;
    }

    public void setLastCombatCheck(boolean bool) {
        lastCombatCheck = bool;
    }

    public void setLoginTimestamp(long loginTimestamp) {
        this.loginTimestamp = loginTimestamp + TogglePvp.getPlugin().getConfigCache().getLogin_protection_time();
    }

    public long getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setTeleportTimestamp(long teleportTimestamp) {
        this.teleportTimestamp = teleportTimestamp + TogglePvp.getPlugin().getConfigCache().getTeleport_protection_time();
    }

    public long getTeleportTimestamp() {
        return teleportTimestamp;
    }

    public boolean isInCombat() {
        return inCombat;
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }
}
