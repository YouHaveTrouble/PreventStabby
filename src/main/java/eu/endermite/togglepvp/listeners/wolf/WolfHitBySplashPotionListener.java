package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@eu.endermite.togglepvp.util.Listener
public class WolfHitBySplashPotionListener implements Listener {

    /**
     * If thrown potion applies negative effects and it's thrown by a player
     * it will have no effect on a pet of a player with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWolfHitBySplashPotion(org.bukkit.event.entity.PotionSplashEvent event) {

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
            if (entity instanceof Wolf) {
                Player damager = (Player) event.getEntity().getShooter();
                Wolf victim = (Wolf) entity;

                if (victim.getOwner() == null) {
                    return;
                }

                ConfigCache config = TogglePvP.getPlugin().getConfigCache();
                boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
                if (!damagerPvpEnabled) {
                    event.setIntensity(victim, 0);
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                    continue;
                }
                boolean victimPvpEnabled = (boolean) SmartCache.getPlayerData(victim.getOwner().getUniqueId()).get("pvpenabled");
                if (!victimPvpEnabled) {
                    event.setIntensity(victim, 0);
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                    continue;
                }
                CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());
            }
        }
    }

}
