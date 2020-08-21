package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.CombatTimer;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
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

                Iterator<LivingEntity> it = event.getAffectedEntities().iterator();
                Player damager = (Player) event.getEntity().getSource();
                while(it.hasNext()) {
                    LivingEntity entity = it.next();
                    if(entity instanceof Player) {

                        Player victim = (Player) entity;
                        if (damager == victim)
                            continue;

                        ConfigCache config = TogglePvP.getPlugin().getConfigCache();
                        boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
                        if (!damagerPvpEnabled) {
                            it.remove();
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                            continue;
                        }
                        boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                        if (!victimPvpEnabled) {
                            it.remove();
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                            continue;
                        }
                        CombatTimer.refreshPlayersCombatTime(damager.getUniqueId(), victim.getUniqueId());
                    } else if (entity instanceof Wolf) {
                        Wolf victim = (Wolf) entity;
                        if (victim.getOwner() == null) {
                            return;
                        }
                        ConfigCache config = TogglePvP.getPlugin().getConfigCache();
                        boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager.getUniqueId());
                        if (!damagerPvpEnabled) {
                            it.remove();
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                            continue;
                        }
                        boolean victimPvpEnabled = (boolean) SmartCache.getPlayerData(victim.getOwner().getUniqueId()).get("pvpenabled");
                        if (!victimPvpEnabled) {
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
