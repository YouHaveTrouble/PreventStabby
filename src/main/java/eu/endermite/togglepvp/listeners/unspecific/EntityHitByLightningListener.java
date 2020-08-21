package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvP;
import eu.endermite.togglepvp.players.SmartCache;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Listen for lightning strikes and tag the trident spawned ones.
 * Idea from aasmus' PvPToggle plugin
 */
@eu.endermite.togglepvp.util.Listener
public class EntityHitByLightningListener implements Listener {

    /**
     * Cancels damage from lightning strike caused by channeling for players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLightningDamage(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LightningStrike && event.getDamager().getMetadata("TRIDENT").size() >= 1) {
            if (event.getEntity() instanceof Player) {
                Player victim = (Player) event.getEntity();
                boolean victimPvpEnabled = TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(victim.getUniqueId());
                if (!victimPvpEnabled) {
                    event.setCancelled(true);
                }
            } else if (event.getEntity() instanceof Wolf) {
                Wolf victim = (Wolf) event.getEntity();
                boolean victimPvpEnabled = (boolean) SmartCache.getPlayerData(victim.getOwner().getUniqueId()).get("pvpenabled");
                if (!victimPvpEnabled) {
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
            if (TogglePvP.getPlugin().getConfigCache().isChanneling_enchant_disabled()) {
                event.setCancelled(true);
                return;
            }
            event.getLightning().setMetadata("TRIDENT", new FixedMetadataValue(TogglePvP.getPlugin(), event.getLightning().getLocation()));
        }
    }
}
