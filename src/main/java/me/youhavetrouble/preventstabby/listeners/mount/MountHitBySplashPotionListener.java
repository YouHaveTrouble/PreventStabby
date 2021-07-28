package me.youhavetrouble.preventstabby.listeners.mount;

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
public class MountHitBySplashPotionListener implements Listener {

    /**
     * If thrown potion applies negative effects and it's thrown by a player
     * it will have no effect on a pet of a player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMountHitBySplashPotion(org.bukkit.event.entity.PotionSplashEvent event) {
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
            if (entity.getPassengers().isEmpty()) continue;
            if (Util.processMountAttack(damager, entity))
                event.setIntensity(entity, 0);
        }
    }

}
