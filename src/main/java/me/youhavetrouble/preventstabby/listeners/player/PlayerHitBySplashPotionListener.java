package me.youhavetrouble.preventstabby.listeners.player;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import me.youhavetrouble.preventstabby.util.Util;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

@PreventStabbyListener
public class PlayerHitBySplashPotionListener implements Listener {

    /**
     * If thrown potion is applies negative effects and it's thrown by a player it will have no effect on player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitBySplashPotion(org.bukkit.event.entity.PotionSplashEvent event) {

        if (!(event.getEntity().getShooter() instanceof Player)) return;
        boolean harmful = false;

        for (PotionEffect effect : event.getPotion().getEffects()) {
            if (Util.isPotionEffectHarmful(effect.getType())) {
                harmful = true;
                break;
            }
        }
        if (!harmful) return;
        UUID damager = ((Player) event.getEntity().getShooter()).getUniqueId();
        for (LivingEntity entity : event.getAffectedEntities()) {
            if (!(entity instanceof Player)) continue;
            UUID victim = entity.getUniqueId();
            if (damager == victim) continue;
            if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true)) {
                CombatTimer.refreshPlayersCombatTime(damager, victim);
            } else {
                event.setIntensity(entity, 0);
            }

        }
    }
}
