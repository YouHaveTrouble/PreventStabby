package eu.endermite.togglepvp.listeners.wolf;

import eu.endermite.togglepvp.TogglePvp;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@eu.endermite.togglepvp.util.Listener
public class PlayerInteractWithPetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteractWithPet(org.bukkit.event.player.PlayerInteractEntityEvent event) {

        if (!(event.getRightClicked() instanceof Tameable))
            return;

        Tameable tameable = (Tameable) event.getRightClicked();
        if (tameable.getOwner() == null)
            return;

        if (!TogglePvp.getPlugin().getConfigCache().isOnly_owner_can_interact_with_pet())
            return;

        if (tameable.getOwner().getUniqueId().equals(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);

    }
}
