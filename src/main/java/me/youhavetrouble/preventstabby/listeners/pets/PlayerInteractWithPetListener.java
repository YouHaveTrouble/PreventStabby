package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@PreventStabbyListener
public class PlayerInteractWithPetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteractWithPet(org.bukkit.event.player.PlayerInteractEntityEvent event) {

        if (!PreventStabby.getPlugin().getConfigCache().isOnly_owner_can_interact_with_pet()) return;
        if (!(event.getRightClicked() instanceof Tameable)) return;

        Tameable tameable = (Tameable) event.getRightClicked();
        if (tameable.getOwner() == null) return;
        if (tameable.getOwner().getUniqueId().equals(event.getPlayer().getUniqueId())) return;

        event.setCancelled(true);

    }
}
