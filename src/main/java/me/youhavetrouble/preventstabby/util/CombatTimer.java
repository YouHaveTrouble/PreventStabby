package me.youhavetrouble.preventstabby.util;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.api.event.PlayerEnterCombatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.time.Instant;
import java.util.UUID;

public class CombatTimer {

    public static void refreshPlayersCombatTime(UUID uuid) {
        try {
            long combattime = PreventStabby.getPlugin().getSmartCache().getPlayerData(uuid).getCombatTime();

            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) return;
            PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player);
            Bukkit.getScheduler().runTask(PreventStabby.getPlugin(), () -> {
                Bukkit.getPluginManager().callEvent(playerEnterCombatEvent);
                if (playerEnterCombatEvent.isCancelled()) return;
                PreventStabby.getPlugin().getPlayerManager().refreshPlayersCombatTime(uuid);

                if (combattime <= 0) {
                    PluginMessages.sendActionBar(uuid, PreventStabby.getPlugin().getConfigCache().getEntering_combat());
                }
            });
        } catch (Exception ignored) {
        }
    }

    public static void refreshPlayersCombatTime(UUID... uuid) {
        for (UUID id : uuid) {
            refreshPlayersCombatTime(id);
        }
    }

    public static boolean isInCombat(UUID uuid) {
        try {
            return PreventStabby.getPlugin().getPlayerManager().getPlayer(uuid).getCombatTime() >= Instant.now().getEpochSecond();
        } catch (Exception e) {
            return false;
        }
    }

}
