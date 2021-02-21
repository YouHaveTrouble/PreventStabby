package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvp;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.potion.PotionEffectType;
import java.util.Iterator;

@eu.endermite.togglepvp.util.Listener
public class AreaEffectCloudApplyListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onCloudEffects(AreaEffectCloudApplyEvent event) {

        PotionEffectType potionEffectType = event.getEntity().getBasePotionData().getType().getEffectType();

        if (potionEffectType == null)
            return;

        if(event.getEntity().getSource() instanceof Player) {
            if (potionEffectType.equals(PotionEffectType.BLINDNESS) ||
                    potionEffectType.equals(PotionEffectType.CONFUSION) ||
                    potionEffectType.equals(PotionEffectType.HARM) ||
                    potionEffectType.equals(PotionEffectType.HUNGER) ||
                    potionEffectType.equals(PotionEffectType.POISON) ||
                    potionEffectType.equals(PotionEffectType.SLOW_DIGGING) ||
                    potionEffectType.equals(PotionEffectType.WEAKNESS) ||
                    potionEffectType.equals(PotionEffectType.SLOW) ||
                    potionEffectType.equals(PotionEffectType.WITHER)) {

                SmartCache smartCache = TogglePvp.getPlugin().getSmartCache();

                Iterator<LivingEntity> it = event.getAffectedEntities().iterator();
                Player damager = (Player) event.getEntity().getSource();
                while(it.hasNext()) {
                    LivingEntity entity = it.next();
                    if(entity instanceof Player) {

                        Player victim = (Player) entity;
                        if (damager == victim)
                            continue;

                        ConfigCache config = TogglePvp.getPlugin().getConfigCache();
                        if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                            it.remove();
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                            continue;
                        }
                        if (!smartCache.getPlayerData(victim.getUniqueId()).isPvpEnabled()) {
                            it.remove();
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                            continue;
                        }
                        CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getUniqueId());
                    } else if (entity instanceof Tameable) {
                        Tameable victim = (Tameable) entity;
                        if (victim.getOwner() == null || victim.getOwner() == damager) {
                            return;
                        }
                        ConfigCache config = TogglePvp.getPlugin().getConfigCache();
                        if (!smartCache.getPlayerData(damager.getUniqueId()).isPvpEnabled()) {
                            it.remove();
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                            continue;
                        }
                        if (!smartCache.getPlayerData(victim.getUniqueId()).isPvpEnabled()) {
                            it.remove();
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                            continue;
                        }
                        CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getOwner().getUniqueId());
                    }
                }
            }
        }
    }
}
