package eu.endermite.togglepvp.listeners.unspecific;

import eu.endermite.togglepvp.TogglePvp;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class LightningBlockIgniteListener implements Listener {

    /**
     * Lightning strikes from channeling enchant won't ignite blocks if player with pvp off is in radius
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockIgnite(org.bukkit.event.block.BlockIgniteEvent event) {

        if (event.getIgnitingEntity() instanceof LightningStrike && event.getIgnitingEntity().getMetadata("TRIDENT").size() >= 1) {
            LightningStrike lightningStrike = (LightningStrike) event.getIgnitingEntity();

            for (Entity entity : lightningStrike.getNearbyEntities(2,2,2)) {
                if (entity instanceof Player) {
                    Player victim = (Player) entity;
                    if (!TogglePvp.getPlugin().getSmartCache().getPlayerData(victim.getUniqueId()).isPvpEnabled()) {
                        event.setCancelled(true);
                        return;
                    }
                } else if (entity instanceof Tameable) {
                    Tameable victim = (Tameable) entity;
                    if (victim.getOwner() == null) {
                        return;
                    }
                    if (!TogglePvp.getPlugin().getSmartCache().getPlayerData(victim.getOwner().getUniqueId()).isPvpEnabled()) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

    }

}
