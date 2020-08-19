package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.util.PluginMessages;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;

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
                    potionEffectType.equals(PotionEffectType.WITHER)) {

                Iterator<LivingEntity> it = event.getAffectedEntities().iterator();
                while(it.hasNext()) {
                    LivingEntity entity = it.next();
                    if(entity instanceof Player) {
                        Player damager = (Player) event.getEntity().getSource();
                        Player victim = (Player) entity;

                        ConfigCache config = TogglePvP.getPlugin().getConfigCache();

                        boolean damagerPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(damager);

                        if (!damagerPvpEnabled) {
                            it.remove();
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_attacker());
                            return;
                        }
                        boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim);
                        if (!victimPvpEnabled) {
                            it.remove();
                            PluginMessages.sendActionBar(damager, config.getCannot_attack_victim());
                        }
                    }
                }
            }
        }
    }

}
