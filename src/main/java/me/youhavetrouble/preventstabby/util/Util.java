package me.youhavetrouble.preventstabby.util;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Util {

       private static final HashSet<PotionEffectType> harmfulPotions = new HashSet<>();

       public static void initData() {
           harmfulPotions.add(PotionEffectType.BLINDNESS);
           harmfulPotions.add(PotionEffectType.CONFUSION);
           harmfulPotions.add(PotionEffectType.HARM);
           harmfulPotions.add(PotionEffectType.HUNGER);
           harmfulPotions.add(PotionEffectType.POISON);
           harmfulPotions.add(PotionEffectType.SLOW_DIGGING);
           harmfulPotions.add(PotionEffectType.WEAKNESS);
           harmfulPotions.add(PotionEffectType.SLOW);
           harmfulPotions.add(PotionEffectType.WITHER);
       }

    /**
     * @param attacker Player attacking the mount
     * @param mount Entity being ridden
     * @return true if event should be cancelled
     */
    public static boolean processMountAttack(UUID attacker, Entity mount) {

        // Don't cancel attacks on players that have passengers
        if (mount instanceof Player) return false;

        ConfigCache config = PreventStabby.getPlugin().getConfigCache();

        if (!PreventStabby.getPlugin().getPlayerManager().getPlayerPvPState(attacker)) {
            PluginMessages.sendActionBar(attacker, config.getCannot_attack_mounts_attacker());
            return true;
        }

        Set<UUID> playerPassengersWithPvpEnabled = new HashSet<>();

        for (Entity passenger : mount.getPassengers()) {
            if (!(passenger instanceof Player)) continue;
            Player player = (Player) passenger;
            if (PreventStabby.getPlugin().getPlayerManager().getPlayerPvPState(player.getUniqueId()))
                playerPassengersWithPvpEnabled.add(player.getUniqueId());
        }

        if (playerPassengersWithPvpEnabled.isEmpty()) {
            PluginMessages.sendActionBar(attacker, config.getCannot_attack_mounts_victim());
            return true;
        }

        playerPassengersWithPvpEnabled.forEach(CombatTimer::refreshPlayersCombatTime);
        CombatTimer.refreshPlayersCombatTime(attacker);
        return false;
    }

    public static boolean isPotionEffectHarmful(PotionEffectType effect) {
        return harmfulPotions.contains(effect);
    }

}
