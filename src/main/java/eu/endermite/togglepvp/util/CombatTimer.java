package eu.endermite.togglepvp.util;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.players.SmartCache;

import java.time.Instant;
import java.util.UUID;

public class CombatTimer {

    public static void refreshPlayersCombatTime(UUID uuid) {
        try {
            long now = Instant.now().getEpochSecond();
            long combattime = (long) SmartCache.getPlayerData(uuid).get("combattime");
            if (combattime < now) {
                PluginMessages.sendActionBar(uuid, TogglePvP.getPlugin().getConfigCache().getEntering_combat());
            }
            TogglePvP.getPlugin().getPlayerManager().refreshPlayersCombatTime(uuid);
        } catch (Exception ignored) {}
    }

    public static void refreshPlayersCombatTime(UUID attacker_uuid, UUID victim_uuid) {
        refreshPlayersCombatTime(attacker_uuid);
        refreshPlayersCombatTime(victim_uuid);
    }

}
