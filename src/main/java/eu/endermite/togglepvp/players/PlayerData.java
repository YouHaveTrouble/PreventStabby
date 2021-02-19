package eu.endermite.togglepvp.players;

import eu.endermite.togglepvp.TogglePvP;

import java.time.Instant;

public class PlayerData {

    private long cachetime, combattime;
    private boolean pvpEnabled;

    public PlayerData(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
        this.combattime = Instant.now().getEpochSecond()-1;
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
        this.cachetime = Instant.now().getEpochSecond()+TogglePvP.getPlugin().getConfigCache().getCache_time();
    }

    public long getCombattime() {
        return combattime;
    }

    public void setCombattime(long combattime) {
        this.combattime = combattime;
    }

    public void refreshCombatTime() {
        this.combattime = Instant.now().getEpochSecond()+TogglePvP.getPlugin().getConfigCache().getCombat_time();
    }
}
