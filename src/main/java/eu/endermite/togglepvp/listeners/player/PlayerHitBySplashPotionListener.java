package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.util.CombatTimer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@eu.endermite.togglepvp.util.Listener
public class PlayerHitBySplashPotionListener implements Listener {

    private final List<PotionEffectType> harmfulPotions = new ArrayList<>();

    public PlayerHitBySplashPotionListener() {
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
     * If thrown potion is applies negative effects and it's thrown by a player it will ahve no effect on player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitBySplashPotion(org.bukkit.event.entity.PotionSplashEvent event) {

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        boolean harmful = false;

        for (PotionEffect effect : event.getPotion().getEffects()) {
            if (harmfulPotions.contains(effect.getType())) {
                    harmful = true;
                    break;
            }
        }
        if (!harmful)
            return;
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof Player) {
                UUID damager = ((Player) event.getEntity().getShooter()).getUniqueId();
                UUID victim = entity.getUniqueId();
                if (damager == victim)
                    continue;

                if (TogglePvp.getPlugin().getPlayerManager().canDamage(damager, victim, true)) {
                    CombatTimer.refreshPlayersCombatTime(damager, victim);
                } else {
                    event.setIntensity((LivingEntity) entity, 0);
                }
            }
        }
    }
}
