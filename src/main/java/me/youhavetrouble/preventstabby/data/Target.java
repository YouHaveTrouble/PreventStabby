package me.youhavetrouble.preventstabby.data;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class Target {

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

        return null;
    }

    public enum EntityClassifier {
        PLAYER,
        PET,
        MOUNT,
        OTHER
    }
}