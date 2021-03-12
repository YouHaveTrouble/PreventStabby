package eu.endermite.togglepvp.util;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.api.event.PlayerEnterCombatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.UUID;

public class CombatTimer {

    public static void refreshPlayersCombatTime(UUID uuid) {
        try {
            long now = Instant.now().getEpochSecond();
            long combattime = TogglePvp.getPlugin().getSmartCache().getPlayerData(uuid).getCombattime();

                Player player = Bukkit.getPlayer(uuid);
                Bukkit.getScheduler().runTask(TogglePvp.getPlugin(), () -> {
                    PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player);
                    Bukkit.getPluginManager().callEvent(playerEnterCombatEvent);
                    if (playerEnterCombatEvent.isCancelled())
                        return;
                    TogglePvp.getPlugin().getPlayerManager().refreshPlayersCombatTime(uuid);

                    if (combattime <= now) {
                        PluginMessages.sendActionBar(uuid, TogglePvp.getPlugin().getConfigCache().getEntering_combat());
                    }
                });



        } catch (Exception ignored) {}
    }

    public static void refreshPlayersCombatTime(UUID attacker_uuid, UUID victim_uuid) {
        refreshPlayersCombatTime(attacker_uuid);
        refreshPlayersCombatTime(victim_uuid);
    }

    public static boolean isInCombat(UUID uuid) {
        try {
            return TogglePvp.getPlugin().getPlayerManager().getPlayer(uuid).getCombattime() >= Instant.now().getEpochSecond();
        } catch (Exception e) {
            return false;
        }
    }

}
