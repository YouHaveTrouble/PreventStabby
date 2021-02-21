package eu.endermite.togglepvp.util;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.players.SmartCache;
import java.time.Instant;
import java.util.UUID;

public class CombatTimer {

    public static void refreshPlayersCombatTime(UUID uuid) {
        try {
            long now = Instant.now().getEpochSecond();
            long combattime = TogglePvp.getPlugin().getSmartCache().getPlayerData(uuid).getCombattime();
            if (combattime < now) {
                PluginMessages.sendActionBar(uuid, TogglePvp.getPlugin().getConfigCache().getEntering_combat());
            }
            TogglePvp.getPlugin().getPlayerManager().refreshPlayersCombatTime(uuid);
        } catch (Exception ignored) {}
    }

    public static void refreshPlayersCombatTime(UUID attacker_uuid, UUID victim_uuid) {
        refreshPlayersCombatTime(attacker_uuid);
        refreshPlayersCombatTime(victim_uuid);
    }

    public static boolean isInCombat(UUID uuid) {
        try {
            long combattimer = TogglePvp.getPlugin().getSmartCache().getPlayerData(uuid).getCombattime();
            long now = Instant.now().getEpochSecond();
            return combattimer > now;
        } catch (Exception e) {
            return false;
        }
    }

}
