package me.youhavetrouble.preventstabby.data;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class Target {

    public static final NamespacedKey playerSourceIdKey = new NamespacedKey("preventstabby", "playersource");

    /**
     * The unique identifier for a player.
     */
    public final UUID playerUuid;

    /**
     * Represents the entity classifier of a target.
     * This indicates what type of entity player's id was assumed from
     */
    public final EntityClassifier classifier;

    private Target(UUID uuid, EntityClassifier classifier) {
        this.playerUuid = uuid;
        this.classifier = classifier;
    }

    /**
     * Get the UUID of the actual player, being owner of a pet, shooter of a projectile, etc.
     *
     * @param entity Base entity to get the player UUID from
     * @return UUID of the actual player, null if not found
     */
    @Nullable
    public static Target getTarget(@NotNull Entity entity) {
        if (entity instanceof Player) return new Target(entity.getUniqueId(), EntityClassifier.PLAYER);

        // Get shooter of projectile
        if (entity instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player shooter) {
                return new Target(shooter.getUniqueId(), EntityClassifier.PLAYER);
            }
        }

        // Get player riding mount
        if (!entity.getPassengers().isEmpty()) {
            Entity passenger = entity.getPassengers().get(0);
            if (passenger instanceof Player) {
                return new Target(passenger.getUniqueId(), EntityClassifier.MOUNT);
            }
        }

        // Get owner of tamed entity
        if (entity instanceof Tameable tameable) {
            if (tameable.getOwner() != null) {
                return new Target(tameable.getOwner().getUniqueId(), EntityClassifier.PET);
            }
        }

        // Try to get player's id from other various entities
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if (!pdc.has(playerSourceIdKey)) return null;
        String id = pdc.get(playerSourceIdKey, PersistentDataType.STRING);
        if (id == null) return null;
        try {
            UUID uuid = UUID.fromString(id);
            return new Target(uuid, EntityClassifier.OTHER);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Assigns the player source ID to the given data holder.
     * @param dataHolder The persistent data holder to assign the player source ID to.
     * @param id The UUID of the player source ID to assign.
     */
    public static void assignPlayerSourceId(@NotNull PersistentDataHolder dataHolder, @NotNull UUID id) {
        dataHolder.getPersistentDataContainer().set(playerSourceIdKey, PersistentDataType.STRING, id.toString());
    }

    public enum EntityClassifier {
        PLAYER,
        PET,
        MOUNT,
        OTHER
    }
}