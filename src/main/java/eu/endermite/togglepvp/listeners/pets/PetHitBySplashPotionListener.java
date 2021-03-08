package eu.endermite.togglepvp.listeners.pets;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PetHitBySplashPotionListener implements Listener {

    /**
     * If thrown potion applies negative effects and it's thrown by a player
     * it will have no effect on a pet of a player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPetHitBySplashPotion(org.bukkit.event.entity.PotionSplashEvent event) {

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        boolean harmful = false;

        for (PotionEffect effect : event.getPotion().getEffects()) {
            if (effect.getType().equals(PotionEffectType.BLINDNESS) ||
                    effect.getType().equals(PotionEffectType.CONFUSION) ||
                    effect.getType().equals(PotionEffectType.HARM) ||
                    effect.getType().equals(PotionEffectType.HUNGER) ||
                    effect.getType().equals(PotionEffectType.POISON) ||
                    effect.getType().equals(PotionEffectType.SLOW_DIGGING) ||
                    effect.getType().equals(PotionEffectType.WEAKNESS) ||
                    effect.getType().equals(PotionEffectType.SLOW) ||
                    effect.getType().equals(PotionEffectType.WITHER)) {
                harmful = true;
            }
        }
        if (!harmful)
            return;
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof Tameable) {
                UUID damager = ((Player) event.getEntity().getShooter()).getUniqueId();
                Tameable tameable = (Tameable) entity;

                if (tameable.getOwner() == null)
                    continue;

                UUID victim = tameable.getOwner().getUniqueId();

                if (victim == damager)
                    return;

                if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true, false))
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                else
                    event.setCancelled(true);
            }
        }
    }

}
