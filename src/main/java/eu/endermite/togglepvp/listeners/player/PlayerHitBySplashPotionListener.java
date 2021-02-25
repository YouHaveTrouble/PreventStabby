package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
                Player damager = (Player) event.getEntity().getShooter();
                Player victim = (Player) entity;
                if (damager == victim)
                    continue;

                SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();

                if (Instant.now().getEpochSecond() < smartCache.getPlayerData(victim.getUniqueId()).getLoginTimestamp()) {
                    event.setIntensity(victim, 0);
                    continue;
                }

                ConfigCache config = TogglePvp.getPlugin().getConfigCache();
                if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                    event.setIntensity(victim, 0);
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                    continue;
                }
                if (!smartCache.getPlayerData(victim.getUniqueId()).isPvpEnabled()) {
                    event.setIntensity(victim, 0);
                    PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                    continue;
                }
                CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getUniqueId());
            }
        }
    }
}
