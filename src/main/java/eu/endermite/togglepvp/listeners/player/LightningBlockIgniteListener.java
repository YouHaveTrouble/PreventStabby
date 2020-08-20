package eu.endermite.togglepvp.listeners.player;

import eu.endermite.togglepvp.TogglePvP;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

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
                    Player player = (Player) entity;

                    if (!TogglePvP.getPlugin().getPlayerManager().getPlayerPvPState(player.getUniqueId())) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

    }

}
