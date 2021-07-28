package me.youhavetrouble.preventstabby.listeners.unspecific;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import java.util.UUID;

/**
 * Listen for lightning strikes and tag the trident spawned ones.
 * Idea from aasmus' PvPToggle plugin
 */
@PreventStabbyListener
public class EntityHitByLightningListener implements Listener {

    /**
     * Cancels damage from lightning strike caused by channeling for players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLightningDamage(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LightningStrike && event.getDamager().getMetadata("TRIDENT").size() >= 1) {
            if (event.getEntity() instanceof Player) {
                UUID victim = event.getEntity().getUniqueId();

                if (PreventStabby.getPlugin().getPlayerManager().hasLoginProtection(victim)) {
                    event.setCancelled(true);
                    return;
                }

                if (!PreventStabby.getPlugin().getSmartCache().getPlayerData(victim).isPvpEnabled()) {
                    event.setCancelled(true);
                }
            } else if (event.getEntity() instanceof Tameable) {
                Tameable victim = (Tameable) event.getEntity();
                if (victim.getOwner() != null && !PreventStabby.getPlugin().getSmartCache().getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Tags the lightning strike
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLightningStrike(LightningStrikeEvent event){
        if(event.getCause() == LightningStrikeEvent.Cause.TRIDENT){
            if (PreventStabby.getPlugin().getConfigCache().isChanneling_enchant_disabled()) {
                event.setCancelled(true);
                return;
            }
            event.getLightning().setMetadata("TRIDENT", new FixedMetadataValue(PreventStabby.getPlugin(), event.getLightning().getLocation()));
        }
    }
}
