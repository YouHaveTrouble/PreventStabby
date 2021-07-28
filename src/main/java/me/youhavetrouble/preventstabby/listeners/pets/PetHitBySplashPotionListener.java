package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import me.youhavetrouble.preventstabby.util.Util;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.UUID;

@PreventStabbyListener
public class PetHitBySplashPotionListener implements Listener {

    /**
     * If thrown potion applies negative effects and it's thrown by a player
     * it will have no effect on a pet of a player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetHitBySplashPotion(org.bukkit.event.entity.PotionSplashEvent event) {

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
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof Tameable) {
                Tameable tameable = (Tameable) entity;
                if (tameable.getOwner() == null) continue;
                UUID victim = tameable.getOwner().getUniqueId();
                if (victim == damager) return;

                if (PreventStabby.getPlugin().getPlayerManager().canDamage(damager, victim, true, false))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    event.setCancelled(true);
            }
        }
    }

}
